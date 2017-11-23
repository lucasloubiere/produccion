/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.sincronizacion.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.util.JeeUtil;
import bs.sincronizacion.dao.SincronizacionDAO;
import bs.sincronizacion.modelo.LogSincronizacion;
import bs.sincronizacion.modelo.Sincronizacion;
import bs.sincronizacion.ws.DatosBalanaza;
import bs.sincronizacion.ws.MovimientoBalanza;
import bs.stock.modelo.Deposito;
import bs.stock.modelo.ItemProductoStock;
import bs.stock.modelo.MovimientoStock;
import bs.stock.modelo.Producto;
import bs.stock.rn.DepositoRN;
import bs.stock.rn.MovimientoStockRN;
import bs.stock.rn.ProductoRN;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author lloubiere
 */
@Stateless
public class SincronizacionRN implements Serializable {

    @EJB
    private SincronizacionDAO sincronizacionDAO;
    @EJB
    private MovimientoStockRN movimientoStockRN;
    @EJB
    private DepositoRN depositoRN;
    @EJB
    private ProductoRN productoRN;
    @EJB
    private LogSincronizacionRN logSincronizacionRN;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardar(Sincronizacion s, boolean esNuevo) throws Exception {

        if (esNuevo) {

            if (sincronizacionDAO.getObjeto(Sincronizacion.class, s.getCodigo()) != null) {
                throw new ExcepcionGeneralSistema("Ya existe sincronizacion con el código" + s.getCodigo());
            }
            sincronizacionDAO.crear(s);
        } else {
            sincronizacionDAO.editar(s);
        }
    }

    public void eliminar(Sincronizacion s) throws Exception {

        sincronizacionDAO.eliminar(s);
    }

    public Sincronizacion getSincronizacion(String cod) {
        return sincronizacionDAO.getSincronizacion(cod);
    }

    public List<Sincronizacion> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja, int cantidadRegistros) {

        return sincronizacionDAO.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
    }

    public String sincronizarMovimientosBalanza(Date fecha, String empresa){

        LogSincronizacion logSincronizacion = new LogSincronizacion();
        String log = null;
        String sResultado = "";

        try {

            logSincronizacion.setFecha(fecha);
            logSincronizacion.setErrores("");

            if (log == null) {
                log = "";
            }

            int cantMovimientosProcesados = 0;

            Client client = Client.create();

            client.addFilter(new HTTPBasicAuthFilter("sistemastock", "n8a49s6v17e416f4c"));

            WebResource webResource = client.resource("http://192.168.15.55:8080/sgc/rest/secure/detalleBalanza/fecha/" + JeeUtil.getFechaWS(fecha) + "/codigoPlanta/0002/empresa/" + empresa);

            ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            DatosBalanaza datos = response.getEntity(DatosBalanaza.class);

            if (datos != null) {

                for (MovimientoBalanza mb : datos.getData()) {

                    if (mb.getOperacion() != null && !mb.getOperacion().isEmpty()) {

                        MovimientoStock ms = null;

                        if (mb.getOperacion().equals("DESCARGA")) {
                            ms = movimientoStockRN.nuevoMovimiento("ST", "INGB", "0001");
                        }

                        if (mb.getOperacion().equals("CARGA")) {
                            ms = movimientoStockRN.nuevoMovimiento("ST", "EGRB", "0001");
                        }

                        if (mb.getPlataformaId() == null) {
                            log += JeeUtil.getFechaWS(fecha) + " - No se encontró código de plataforma en comprobante " + mb.getNroComprobante() + " \n";
                            continue;
                        }

                        if (mb.getProductoCodigo() == null || mb.getProductoCodigo().isEmpty()) {
                            log += JeeUtil.getFechaWS(fecha) + " - No se encontró código de producto en comprobante " + mb.getNroComprobante() + " \n";
                            continue;
                        }

                        Deposito deposito = null;

                        if (empresa.equals("1")) {

                            deposito = depositoRN.getDepositoByCodigoReferencia(String.valueOf(mb.getPlataformaId()));
                        } else if (empresa.equals("2")) {
                            deposito = depositoRN.getDepositoByCodigoReferencia2(String.valueOf(mb.getPlataformaId()));
                        }

                        Producto producto = productoRN.getProducto(mb.getProductoCodigo());

                        if (ms == null) {
                            log += JeeUtil.getFechaWS(fecha) + " - No pudo crearse movimiento, codigo de operación vacío " + mb.getNroComprobante() + " \n";
                            continue;
                        }

                        if (deposito == null) {
                            log += JeeUtil.getFechaWS(fecha) + " - No se encontró depósito (" + mb.getPlataformaId() + ") en comprobante " + mb.getNroComprobante() + " \n";
                            continue;
                        }

                        if (producto == null) {
                            log += JeeUtil.getFechaWS(fecha) + " - No se encontró producto (" + mb.getProductoCodigo() + ") en comprobante " + mb.getNroComprobante() + " \n";
                            continue;
                        }

                        ms.setFechaMovimiento(JeeUtil.getFechaYHora(fecha, mb.getHoraSalida()));
                        ms.setNumeroFormulario(Integer.valueOf(mb.getNroComprobante()));
                        ms.setNoSincronizaNumeroFormulario(true);
                        ms.setNoValidaStockDisponible(true);
                        ms.setDeposito(deposito);

                        ItemProductoStock ip = ms.getItemsProducto().get(ms.getItemsProducto().size() - 1);

                        ip.setProducto(producto);
                        ip.setUnidadMedida(producto.getUnidadDeMedida());
                        ip.setDeposito(ms.getDeposito());

                        if (empresa.equals("1")) {
                            ip.setAtributo1("VICENTIN");
                        }

                        if (empresa.equals("2")) {
                            ip.setAtributo1("ALGODONERA");
                        }

                        if (empresa.equals("99")) {
                            ip.setAtributo1("BUYANOR");
                        }

                        ip.setAtributo2("N/D");
                        if (mb.getCosecha() != null && !mb.getCosecha().isEmpty()) {
                            ip.setAtributo2(mb.getCosecha());
                        }

                        ip.setCantidad(new BigDecimal(mb.getNetoNeto()));
                        ip.setTodoOk(true);

                        //Cargarmos un nuevo item en blanco
                        ms.getItemsProducto().add(movimientoStockRN.nuevoItemProducto(ms));

                        try {

                            if (!movimientoStockRN.existeComprobante(ms)) {

                                movimientoStockRN.guardar(ms);
                                cantMovimientosProcesados++;
                            }

                        } catch (Exception ex) {
                            log += JeeUtil.getFechaWS(fecha) + " - No es posible guardar el movimiento comprobante " + mb.getNroComprobante() + " - " + ex + " \n";
                            System.err.println("Error guardando comprobante " + ex);
                        }
                    }
                }
            }

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            sResultado = "Proceso finalizado. Se procesaron " + cantMovimientosProcesados + " movimientos del día " + format.format(fecha)+" \n";

            if (log.isEmpty()) {
                log = "Sin errores";
                logSincronizacion.setEstado("OK");
            } else {
                logSincronizacion.setEstado("Con Falla");
            }

            logSincronizacion.setErrores("Proceso finalizado. Se procesaron " + cantMovimientosProcesados + " movimientos del día " + format.format(fecha) + "\n" + log);
            logSincronizacionRN.guardar(logSincronizacion, true);

        } catch (Exception e) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            sResultado = "Ha ocurrido un error en la sincronización " + e;
            logSincronizacion.setEstado("Con Falla");
            logSincronizacion.setErrores(e.getMessage());
            try {
                logSincronizacionRN.guardar(logSincronizacion, true);
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return sResultado;
    }
     
    @Schedule(hour = "5,17", minute = "0", second = "0")
    public void tareaProgramada() {

        System.err.println("Sincronizando movimientos balanza Vicentín " + new Date());
        sincronizarMovimientosBalanza(new Date(), "1");

        System.err.println("Sincronizando movimientos balanza Algodonera" + new Date());
        sincronizarMovimientosBalanza(new Date(), "2");
    }

}
