/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.sincronizacion.web;

import bs.global.util.JsfUtil;
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
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lloubiere
 */
@ManagedBean
@SessionScoped
public class TestWS {

    private Date fecha;
    private String empresa;

    @EJB
    private MovimientoStockRN movimientoStockRN;
    @EJB
    private DepositoRN depositoRN;
    @EJB
    private ProductoRN productoRN;

    /**
     * Creates a new instance of TestWS
     */
    public TestWS() {
    }

    public void test() {

        try {
            
            int cantMovimientosProcesados = 0;

            Client client = Client.create();

            client.addFilter(new HTTPBasicAuthFilter("sistemastock", "n8a49s6v17e416f4c"));

            WebResource webResource = client.resource("http://192.168.15.55:8080/sgc/rest/secure/detalleBalanza/fecha/" + JsfUtil.getFechaWS(fecha) + "/codigoPlanta/0002/empresa/" + empresa);

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
                        Deposito deposito = depositoRN.getDepositoByCodigoReferencia(String.valueOf(mb.getPlataformaId()));
                        Producto producto = productoRN.getProducto(mb.getProductoCodigo());

                        if (mb.getOperacion().equals("CARGA")) {

                            ms = movimientoStockRN.nuevoMovimiento("ST", "INGB", "0001");
                        }

                        if (mb.getOperacion().equals("DESCARGA")) {
                            ms = movimientoStockRN.nuevoMovimiento("ST", "EGRB", "0001");
                        }

                        if (ms == null || deposito == null || producto == null) {
                            continue;
                        }

                        ms.setNumeroFormulario(Integer.valueOf(mb.getNroComprobante()));
                        ms.setNoSincronizaNumeroFormulario(true);
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

                        ip.setCantidad(new BigDecimal(mb.getNetoNeto()));
                        ip.setTodoOk(true);

                        //Cargarmos un nuevo item en blanco
                        ms.getItemsProducto().add(movimientoStockRN.nuevoItemProducto(ms));

                        try {
                            movimientoStockRN.guardar(ms);
                            cantMovimientosProcesados++;
                            System.out.println("El documento " + ms.getComprobante().getDescripcion() + "-" + ms.getNumeroFormulario() + " se guardó correctamente");

                        } catch (Exception ex) {
                            System.err.println("Error guardando comprobante " + ex);

                        }
                    }
                }
            }

            JsfUtil.addInfoMessage("Proceso finalizado. Se procesaron " + cantMovimientosProcesados + " movmientos");

        } catch (Exception e) {

            e.printStackTrace();
            JsfUtil.addErrorMessage("Ha ocurrido un error en la sincronización " + e);
        }

    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

}
