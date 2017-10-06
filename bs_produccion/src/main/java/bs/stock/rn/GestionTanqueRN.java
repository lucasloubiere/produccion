/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.modelo.Formulario;
import bs.global.modelo.Sucursal;
import bs.global.rn.FormularioRN;
import bs.global.rn.SucursalRN;
import bs.stock.dao.ComprobanteStockDAO;
import bs.stock.dao.GestionTanqueDAO;
import bs.stock.modelo.ComprobanteStock;
import bs.stock.modelo.GestionTanque;
import bs.stock.modelo.ItemGestionTanque;
import bs.stock.modelo.ItemProductoStock;
import bs.stock.modelo.MovimientoStock;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author lloubiere
 */
@Stateless
public class GestionTanqueRN {

    //@EJB private MonedaRN monedaRN;
    @EJB
    private GestionTanqueDAO gestionTanqueDAO;
    @EJB
    private ComprobanteStockDAO comprobanteDAO;
    @EJB
    private FormularioRN formularioRN;
    @EJB
    private SucursalRN sucursalRN;
    @EJB
    private MovimientoStockRN movimientoStockRN;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public synchronized void guardar(GestionTanque gestionTanque) throws Exception {

        //Validamos que se pueda guardar el comprobante
        controlComprobante(gestionTanque, false);

        generarMovimientosStock(gestionTanque);

        if (gestionTanque.getId() == null) {

            Integer ultimoNumero = formularioRN.tomarProximoNumero(gestionTanque.getFormulario());
            gestionTanque.setNumeroFormulario(ultimoNumero);

            gestionTanqueDAO.crear(gestionTanque);
        } else {
            gestionTanqueDAO.editar(gestionTanque);
        }

        gestionTanque.setPersistido(true);
    }

    /*
     * Se utiliza para generar movimientos desde el modulo de stock
     * este metodo incremenda el nro de formulario
     */
    public GestionTanque nuevoMovimiento(String MODST, String CODST, String SUCURS) throws ExcepcionGeneralSistema {

        ComprobanteStock comprobante = comprobanteDAO.getComprobante(MODST, CODST);
        Sucursal sucursal = sucursalRN.getSucursal(SUCURS);

        if (comprobante == null) {
            throw new ExcepcionGeneralSistema("No se encontró comprobante de stock " + MODST + "-" + CODST);
        }
        if (sucursal == null) {
            throw new ExcepcionGeneralSistema("No se encontró sucursal " + SUCURS);
        }

        //Buscamos el formulario correspondiente
        Formulario formulario = formularioRN.getFormulario(comprobante, sucursal, "X");

        if (formulario == null) {
            throw new ExcepcionGeneralSistema("No se encontró formulario de stock para el comprobante (" + CODST + ") "
                    + "para la sucursal (" + SUCURS + ") "
                    + "con la condición de iva (X) ");
        }

        GestionTanque m = crearMovimiento(comprobante, formulario, sucursal);

        return m;
    }

    /**
     * Se utiliza para generar movimientos de stock automáticos desde otros
     * módulos
     *
     * @param comprobante
     * @param formulario
     * @param sucursal
     * @return
     * @throws ExcepcionGeneralSistema
     */
    public GestionTanque nuevoMovimientoAutomatico(ComprobanteStock comprobante, Formulario formulario, Sucursal sucursal) throws ExcepcionGeneralSistema {

        return crearMovimiento(comprobante, formulario, sucursal);
    }

    /**
     * Se utiliza para generar movimientos de stock automáticos
     *
     * @param comprobante objeto comprobante a generar
     * @param formulario obejeto formulario a generar
     * @return movimiento de stock
     * @throws ExcepcionGeneralSistema
     */
    private GestionTanque crearMovimiento(ComprobanteStock comprobante, Formulario formulario, Sucursal sucursal) throws ExcepcionGeneralSistema {

        if (sucursal == null) {
            throw new ExcepcionGeneralSistema("La sucursal no puede ser nula");
        }

        if (comprobante == null) {
            throw new ExcepcionGeneralSistema("El comprobante de stock no puede ser nulo");
        }

        if (comprobante.getTipoMovimiento() == null) {
            throw new ExcepcionGeneralSistema("El comprobante no tiene definido el tipo de movimiento de inventario");
        }

        if (formulario == null) {
            throw new ExcepcionGeneralSistema("El formulario de stock no puede ser nulo");
        }

        GestionTanque m = new GestionTanque();
//        Moneda moneda = monedaRN.getMoneda("USD");
//        BigDecimal cotizacion  = monedaRN.getCotizacionDia("USD");

        m.setPersistido(false);
        m.setFormulario(formulario);
        m.setComprobante(comprobante);
        m.setNumeroFormulario(formulario.getUltimoNumero() + 1);
        m.setFechaMovimiento(new Date());
        m.setSucursal(sucursal);

        //Generamos el item producto vacío
        m.setItems(new ArrayList<ItemGestionTanque>());
        return m;
    }

    private ItemGestionTanque nuevoItemMovimiento(GestionTanque m) {

        ItemGestionTanque nItem = new ItemGestionTanque();
        nItem.setGestionTanque(m);

        return nItem;
    }

    /**
     * Validaciones previas a guardar el movimiento
     *
     * @param m Movimiento de stock
     * @param permiteVacio permite guardar comprobante vacío
     * @throws bs.global.excepciones.ExcepcionGeneralSistema
     */
    public void controlComprobante(GestionTanque m, boolean permiteVacio) throws ExcepcionGeneralSistema {

        if (m.getId() != null) {
            throw new ExcepcionGeneralSistema("No es posible modificar un comprobante de stock");
        }

//        Modulo modulo  = null;
//        
//        if (!m.getFechaMovimiento().after(modulo.getfechahabilitacionDesde) & !m.getFechaMovimiento().before(modulo.getfechahabilitacionHasta)){
//            throw new ExcepcionGeneralSistema("La fecha del movimiento no está dentro del período habilitado");
//        }
        if (!permiteVacio && m.getItems().isEmpty()) {
            throw new ExcepcionGeneralSistema("El detalle está vacío, no es posible guardar el comprobante de gestión");
        }

    }

    public void puedoAgregarItem(GestionTanque m, ItemGestionTanque nItem) throws ExcepcionGeneralSistema {

        if (nItem == null) {
            throw new ExcepcionGeneralSistema("No se ha creado el item");
        }

        if (nItem.getProducto() == null) {
            throw new ExcepcionGeneralSistema("Seleccione un producto para agregar al comprobante");
        }

    }

    public List<GestionTanque> getListaByBusqueda(Map<String, String> filtro, int cantidadRegistros) {

        return gestionTanqueDAO.getListaByBusqueda(filtro, cantidadRegistros);
    }

    public boolean existeComprobante(GestionTanque ms) {

        Map<String, String> filtro = gestionTanqueDAO.getFiltro();

        filtro.put(" numeroFormulario = ", "'" + ms.getNumeroFormulario() + "'");
        filtro.put(" formulario.codigo = ", "'" + ms.getFormulario().getCodigo() + "'");
        filtro.put(" sucursal.codigo = ", "'" + ms.getSucursal().getCodigo() + "'");

        return (gestionTanqueDAO.getObjeto(GestionTanque.class, filtro) != null);
    }

    public GestionTanque getUltimoRegistro() {

        GestionTanque gestionAnterior = gestionTanqueDAO.getUltimoRegistro();

        if (gestionAnterior == null) {

            gestionAnterior = new GestionTanque();
            gestionAnterior.setFechaMovimiento(new Date());
        }

        return gestionAnterior;

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void generarMovimientosStock(GestionTanque gestionTanque) throws ExcepcionGeneralSistema, Exception {

        for (ItemGestionTanque itemGestion : gestionTanque.getItems()) {

            if (itemGestion.getProducto() == null) {
                continue;
            }

            //Mayor a 0
            if (itemGestion.getStockCalculado().compareTo(BigDecimal.ZERO) == 1) {

                MovimientoStock movIngreso = movimientoStockRN.nuevoMovimiento("ST", "IPA", "0001");

                movIngreso.setFechaMovimiento(gestionTanque.getFechaMovimiento());
                movIngreso.setDeposito(itemGestion.getDeposito());
                movIngreso.setNoValidaStockDisponible(true);

                ItemProductoStock itemProducto = movIngreso.getItemsProducto().get(movIngreso.getItemsProducto().size() - 1);

                itemProducto.setProducto(itemGestion.getProducto());
                itemProducto.setUnidadMedida(itemGestion.getProducto().getUnidadDeMedida());
                itemProducto.setDeposito(itemGestion.getDeposito());

                itemProducto.setAtributo1("VICENTIN");
                itemProducto.setAtributo2("N/D");

                itemProducto.setCantidad(itemGestion.getStockCalculado());
                itemProducto.setTodoOk(true);

                movimientoStockRN.asignarCantidadStock(movIngreso);
                movimientoStockRN.controlComprobante(movIngreso, false);
                movimientoStockRN.generarStock(movIngreso);
                Integer ultimoNumero = formularioRN.tomarProximoNumero(movIngreso.getFormulario());
                movIngreso.setNumeroFormulario(ultimoNumero);

                movIngreso.setGestionTanque(gestionTanque);
                gestionTanque.getMovimientosStock().add(movIngreso);

            }

            //Menor a 0
            if (itemGestion.getStockCalculado().compareTo(BigDecimal.ZERO) == -1) {

                MovimientoStock movEgreso = movimientoStockRN.nuevoMovimiento("ST", "EPA", "0001");

                movEgreso.setFechaMovimiento(gestionTanque.getFechaMovimiento());
                movEgreso.setDeposito(itemGestion.getDeposito());
                movEgreso.setNoValidaStockDisponible(true);

                ItemProductoStock itemProducto =  movEgreso.getItemsProducto().get(movEgreso.getItemsProducto().size() - 1);

                itemProducto.setProducto(itemGestion.getProducto());
                itemProducto.setUnidadMedida(itemGestion.getProducto().getUnidadDeMedida());
                itemProducto.setDeposito(itemGestion.getDeposito());

                itemProducto.setAtributo1("VICENTIN");
                itemProducto.setAtributo2("N/D");

                itemProducto.setCantidad(itemGestion.getStockCalculado());
                itemProducto.setTodoOk(true);

                movimientoStockRN.asignarCantidadStock(movEgreso);
                movimientoStockRN.controlComprobante(movEgreso, false);
                movimientoStockRN.generarStock(movEgreso);
                Integer ultimoNumero = formularioRN.tomarProximoNumero(movEgreso.getFormulario());
                movEgreso.setNumeroFormulario(ultimoNumero);

                movEgreso.setGestionTanque(gestionTanque);
                gestionTanque.getMovimientosStock().add(movEgreso);
            }
        }

    }

}
