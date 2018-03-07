/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.rn;

import bs.administracion.rn.ParametrosRN;
import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.modelo.Comprobante;
import bs.global.modelo.Formulario;
import bs.global.modelo.Moneda;
import bs.global.modelo.Sucursal;
import bs.global.rn.FormularioRN;
import bs.global.rn.MonedaRN;
import bs.global.rn.SucursalRN;
import bs.global.util.JsfUtil;
import bs.produccion.dao.ProduccionDAO;
import bs.produccion.modelo.CircuitoProduccion;
import bs.produccion.modelo.ItemAplicacionProduccion;
import bs.produccion.modelo.ItemComponenteProduccion;
import bs.produccion.modelo.ItemDetalleItemComponenteProduccion;
import bs.produccion.modelo.ItemDetalleItemMovimientoProduccion;
import bs.produccion.modelo.ItemDetalleItemProductoProduccion;
import bs.produccion.modelo.ItemMovimientoProduccion;
import bs.produccion.modelo.ItemProcesoProduccion;
import bs.produccion.modelo.ItemProductoProduccion;
import bs.produccion.modelo.MovimientoProduccion;
import bs.produccion.modelo.TipoMovimientoProduccion;
import bs.produccion.vista.PendienteProduccionDetalle;
import bs.produccion.vista.PendienteProduccionGrupo;
import bs.stock.modelo.ComposicionFormula;
import bs.stock.modelo.ComprobanteStock;
import bs.stock.modelo.ItemComposicionFormula;
import bs.stock.modelo.ItemComposicionFormulaComponente;
import bs.stock.modelo.ItemComposicionFormulaProceso;
import bs.stock.modelo.MovimientoStock;
import bs.stock.modelo.Producto;
import bs.stock.rn.ComposicionFormulaRN;
import bs.stock.rn.ComprobanteStockRN;
import bs.stock.rn.MovimientoStockRN;
import bs.stock.rn.ProductoRN;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * @author ctrosch
 */
@Stateless
public class ProduccionRN {

    @EJB
    private MonedaRN monedaRN;
    @EJB
    protected ParametrosRN parametrosRN;
    @EJB
    private ProduccionDAO produccionDAO;
    @EJB
    private ItemMovimientoProduccionRN itemMovimientoRN;
    @EJB
    private ComprobanteStockRN comprobanteStockRN;
    @EJB
    protected ComposicionFormulaRN composicionFormulaRN;
    @EJB
    private CircuitoProduccionRN circuitoRN;
    @EJB
    private MovimientoStockRN movimientoStockRN;
    @EJB
    private FormularioRN formularioRN;
    @EJB
    private SucursalRN sucursalRN;
    @EJB
    private ProductoRN productoRN;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public synchronized MovimientoProduccion guardar(MovimientoProduccion movimiento) throws Exception {

        borrarItemsNoValidos(movimiento);
        generarDatosAdicionales(movimiento);

        if (movimiento.getId() == null) {

            generarMovimientosAdicionales(movimiento);

            if (!movimiento.isNoSincronizaNumeroFormulario() && movimiento.getNumeroFormulario() > 0) {
                tomarNumeroFormulario(movimiento);
            }

            if (movimiento.getCircuito().getAutomatizaParteProduccion().equals("S")) {

                if (movimiento.getValeConsumo() != null) {
                    tomarNumeroFormulario(movimiento.getValeConsumo());
                    movimiento.getValeConsumo().setPersistido(true);
                    guardarComprobanteStock(movimiento.getValeConsumo().getMovimientoStock());
                }

                if (movimiento.getParteProceso() != null) {
                    tomarNumeroFormulario(movimiento.getParteProceso());
                    movimiento.getParteProceso().setPersistido(true);
                }
            }

            puedoGuardarMovimiento(movimiento);
            produccionDAO.crear(movimiento);

            if (sincronizarIdAplicacion(movimiento)) {
                movimiento = produccionDAO.editar(movimiento);
            }

        } else {

            sincronizarIdAplicacion(movimiento);
            puedoGuardarMovimiento(movimiento);
            movimiento = produccionDAO.editar(movimiento);
        }

        movimiento.setPersistido(true);

        return movimiento;
    }

    private void generarMovimientosAdicionales(MovimientoProduccion m) throws ExcepcionGeneralSistema, Exception {

        if (m.getComprobanteStock() != null) {
            MovimientoStock ms = movimientoStockRN.generarComprobante(m);
            m.setMovimientoStock(ms);
        }
    }

    /**
     *
     * @param circom circuito inicial
     * @param cirapl circuito aplicación
     * @param modPD modulo comprobante de producción
     * @param codPD código comprobante de producción
     * @param sucPD sucursal comprobante producción
     * @return Movimiento de producción generado
     * @throws ExcepcionGeneralSistema
     */
    public MovimientoProduccion nuevoMovimiento(
            String circom, String cirapl,
            String modPD, String codPD, String sucPD) throws ExcepcionGeneralSistema {

        CircuitoProduccion circuito = circuitoRN.iniciarCircuito(circom, cirapl, modPD, codPD, sucPD);
        Sucursal sucursal = sucursalRN.getSucursal(sucPD);
        Sucursal sucursalStock = sucursalRN.getSucursal(sucPD);

        return nuevoMovimiento(circuito, sucursal, sucursalStock);

    }

    /**
     *
     * @param circom circuito inicial
     * @param cirapl circuito aplicación
     * @param modPD modulo comprobante de producción
     * @param codPD código comprobante de producción
     * @param sucPD sucursal comprobante producción
     * @param modST modulo comprobante stock
     * @param codST código comprobante stock
     * @param sucST sucursal comprobante stock
     * @return Movimiento de producción generado
     * @throws ExcepcionGeneralSistema
     */
    public MovimientoProduccion nuevoMovimiento(String circom, String cirapl,
            String modPD, String codPD, String sucPD,
            String modST, String codST, String sucST) throws ExcepcionGeneralSistema {

        CircuitoProduccion circuito = circuitoRN.iniciarCircuito(circom, cirapl, modPD, codPD, sucPD, modST, codST, sucST);
        Sucursal sucursal = sucursalRN.getSucursal(sucPD);
        Sucursal sucursalStock = sucursalRN.getSucursal(sucPD);

        return nuevoMovimiento(circuito, sucursal, sucursalStock);

    }

    public MovimientoProduccion nuevoMovimiento(CircuitoProduccion circuito, Sucursal sucursal, Sucursal sucursalStock) throws ExcepcionGeneralSistema {

        if (circuito == null) {
            throw new ExcepcionGeneralSistema("No es posible crear un nuevo movimiento si el circuito es nulo");
        }

        Comprobante comprobante = null;
        ComprobanteStock comprobanteST = null;

        if (circuito.getActualizaProduccion().equals("S")) {

            comprobante = circuito.getComprobanteProduccion();

        } else if (circuito.getActualizaStock().equals("S")) {

            comprobante = circuito.getComprobanteStock();
        }

//        //Comprobante principal
//        Comprobante comprobantePD = circuito.getComprobanteProduccion();
//        //Comprobante adicional  - Vale de consumo
//        Comprobante comprobanteVC = circuito.getComprobanteValeConsumo();
//        //Comprobante adicional  - Parte proceso
//        Comprobante comprobantePR = circuito.getComprobanteParteProceso();
        MovimientoProduccion m = crearMovimiento(circuito, comprobante, circuito.getTipoMovimiento(), sucursal, sucursalStock);

        comprobanteST = circuito.getComprobanteStock();

        if (comprobanteST != null) {
            m.setComprobanteStock(comprobanteST);

            if (comprobanteST.getDeposito() != null) {
                m.setDeposito(comprobanteST.getDeposito());
            }

            if (comprobanteST.getDepositoTransferencia() != null) {
                m.setDepositoTransferencia(comprobanteST.getDepositoTransferencia());
            }
        }

        return m;
    }

    public MovimientoProduccion nuevoMovimientoFromPendiente(CircuitoProduccion circuito, Sucursal sucursal, Sucursal sucursalStock,
            List<PendienteProduccionDetalle> itemsPendientes) throws ExcepcionGeneralSistema, Exception {

        if (!tengoItemsSeleccionados(itemsPendientes)) {
            throw new ExcepcionGeneralSistema("No existen items seleccionados para generar el movimiento");
        }

        MovimientoProduccion m = nuevoMovimiento(circuito, sucursal, sucursalStock);

        if (m.getTipoMovimiento().equals(TipoMovimientoProduccion.PP)) {
            generarItemsProductoFromPendiente(m, itemsPendientes);
        }

        if (m.getTipoMovimiento().equals(TipoMovimientoProduccion.VC)) {
            generarItemsComponenteFromPendiente(m, itemsPendientes);
        }

        if (m.getTipoMovimiento().equals(TipoMovimientoProduccion.PR)) {
            generarItemsProcesoFromPendiente(m, itemsPendientes);
        }

        asignarFormulario(m);

        if (circuito.getAgregaItems().equals("S")) {
            //Cargarmos un nuevo item en blanco en caso de que quieran guardar sin agregar un items
            m.getItemsProducto().add((ItemProductoProduccion) itemMovimientoRN.nuevoItemProducto(m));
        }
        return m;
    }

    public MovimientoProduccion nuevoMovimientoFromItems(CircuitoProduccion circuito, Sucursal sucursal, Sucursal sucursalStock,
            Object itemsMovimiento) throws ExcepcionGeneralSistema, Exception {

        MovimientoProduccion movimientoProduccion = nuevoMovimiento(circuito, sucursal, sucursalStock);

        if (movimientoProduccion.getTipoMovimiento().equals(TipoMovimientoProduccion.PP)) {
            generarItemsProductoFromItemMovimiento(movimientoProduccion, (List<ItemMovimientoProduccion>) itemsMovimiento);
        }

        if (movimientoProduccion.getTipoMovimiento().equals(TipoMovimientoProduccion.VC)) {
            generarItemsComponenteFromItemMovimiento(movimientoProduccion, (List<ItemMovimientoProduccion>) itemsMovimiento);
        }

        if (movimientoProduccion.getTipoMovimiento().equals(TipoMovimientoProduccion.PR)) {
            generarItemsProcesoFromItemMovimiento(movimientoProduccion, (List<ItemMovimientoProduccion>) itemsMovimiento);
        }

        asignarFormulario(movimientoProduccion);

        return movimientoProduccion;
    }

    private MovimientoProduccion crearMovimiento(CircuitoProduccion circuito, Comprobante comprobante, TipoMovimientoProduccion tm, Sucursal sucursal, Sucursal sucursalStock) throws ExcepcionGeneralSistema {

        if (comprobante == null) {
            throw new ExcepcionGeneralSistema("El comprobante no puede ser nulo");
        }

        MovimientoProduccion m = new MovimientoProduccion();
        Moneda moneda = monedaRN.getMoneda(parametrosRN.getParametro().getCodigoMonedaSecundaria());

        m.setPersistido(false);
        m.setCircuito(circuito);
        m.setComprobante(comprobante);
        m.setSucursal(sucursal);
        m.setSucursalStock(sucursalStock);
        m.setTipoMovimiento(tm);        
        m.setMonedaSecundaria(moneda);

        asignarFormulario(m);

        if (comprobante.getDeposito() != null) {
            m.setDeposito(comprobante.getDeposito());
        }

        if (comprobante.getDepositoTransferencia() != null) {
            m.setDepositoTransferencia(comprobante.getDepositoTransferencia());
        }

        if (circuito.getCircuitoComienzo().equals(circuito.getCircuitoAplicacion())) {
            m.getItemsProducto().add((ItemProductoProduccion) itemMovimientoRN.nuevoItemProducto(m));
        }

        return m;
    }

    public void guardarComprobanteStock(MovimientoStock m) throws Exception {

        if (m != null) {
            movimientoStockRN.guardar(m);
        }
    }

    public void agregarItem(MovimientoProduccion movimiento, Producto producto, BigDecimal cantidad) throws ExcepcionGeneralSistema {
        try {

            String sError = puedoAgregarItem(movimiento, producto, cantidad);

            if (!sError.isEmpty()) {
                throw new ExcepcionGeneralSistema(sError);
            }

            ItemProductoProduccion item = movimiento.getItemsProducto().get(movimiento.getItemsProducto().size() - 1);

            if (item == null) {
                item = itemMovimientoRN.nuevoItemProducto(movimiento);
            }

            item.setProducto(producto);
            item.setProductoOriginal(producto);
            item.setUnidadMedida(producto.getUnidadDeMedida());
            item.setActualizaStock(producto.getGestionaStock());
            item.setCantidad(cantidad);
            item.setCantidadStock(cantidad);
            item.setCantidadOriginal(cantidad);

            for (ItemDetalleItemMovimientoProduccion id : item.getItemDetalle()) {

                id.setCantidad(item.getCantidad());
                id.setUnidadMedida(item.getUnidadMedida());

                id.setAtributo1(item.getAtributo1());
                id.setAtributo2(item.getAtributo2());
                id.setAtributo3(item.getAtributo3());
                id.setAtributo4(item.getAtributo4());
                id.setAtributo5(item.getAtributo5());
                id.setAtributo6(item.getAtributo6());
                id.setAtributo7(item.getAtributo7());
            }

            //Si es comprobante de Orden de producción, agregamos los componentes
            if (movimiento.getCircuito().getTipoMovimiento() == TipoMovimientoProduccion.OP) {

                agregarComponentesYProcesos(movimiento, item);
            }

            //Cargarmos un nuevo item en blanco
            item.setTodoOk(true);
            movimiento.getItemsProducto().add((ItemProductoProduccion) itemMovimientoRN.nuevoItemProducto(movimiento));

        } catch (Exception ex) {
            throw new ExcepcionGeneralSistema("No es posible agregar item " + ex);
        }
    }

    public void agregarComponentesYProcesos(MovimientoProduccion movimiento, ItemProductoProduccion nItem) throws ExcepcionGeneralSistema {

        ComposicionFormula composicionFormula = composicionFormulaRN.getComprosicionFormula(nItem.getProducto().getCodigo(), "STD");
        nItem.setComposicionFormula(composicionFormula);

        if (composicionFormula != null) {

            if (composicionFormula.getItemsComponente() == null && composicionFormula.getItemsProceso() == null) {

                throw new ExcepcionGeneralSistema("La formula del producto seleccionado no contiene componentes ni procesos ");

            } else {

                if (composicionFormula.getItemsComponente() != null) {

                    movimiento.getItemsComponente().clear();

                    for (ItemComposicionFormulaComponente i : composicionFormula.getItemsComponente()) {

                        ItemComponenteProduccion itmComp = itemMovimientoRN.nuevoItemComponente(movimiento);
                        BigDecimal cntNominal = i.getCantidadNominal();

                        itmComp.setProducto(i.getProductoComponente());
                        itmComp.setProductoOriginal(i.getProductoComponente());
                        itmComp.setUnidadMedida(i.getUnidadMedidaItem());
                        itmComp.setCantidad(nItem.getCantidad().multiply(cntNominal));
                        itmComp.setCantidadStock(nItem.getCantidad().multiply(cntNominal));
                        itmComp.setCantidadOriginal(nItem.getCantidad().multiply(cntNominal));
                        itmComp.setUnidadMedida(i.getProductoComponente().getUnidadDeMedida());
                        itmComp.setActualizaStock(i.getProductoComponente().getGestionaStock());

                        movimiento.getItemsComponente().add(itmComp);
                    }
                }

                if (composicionFormula.getItemsProceso() != null) {

                    movimiento.getItemsProceso().clear();

                    for (ItemComposicionFormulaProceso i : composicionFormula.getItemsProceso()) {

                        ItemProcesoProduccion itmComp = itemMovimientoRN.nuevoItemProceso(movimiento);
                        BigDecimal cntNominal = i.getCantidadNominal();

                        itmComp.setProducto(i.getProductoComponente());
                        itmComp.setProductoOriginal(i.getProductoComponente());
                        itmComp.setUnidadMedida(i.getUnidadMedidaItem());
                        itmComp.setCantidad(nItem.getCantidad().multiply(cntNominal));
                        itmComp.setCantidadStock(nItem.getCantidad().multiply(cntNominal));
                        itmComp.setCantidadOriginal(nItem.getCantidad().multiply(cntNominal));
                        itmComp.setUnidadMedida(i.getProductoComponente().getUnidadDeMedida());
                        itmComp.setActualizaStock(i.getProductoComponente().getGestionaStock());

                        movimiento.getItemsProceso().add(itmComp);
                    }
                }
            }

        } else {
            throw new ExcepcionGeneralSistema("El producto (" + nItem.getProducto().getCodigo() + "-" + nItem.getProducto().getCodigo() + ") seleccionado no tiene una fórmula de producción definida");
        }
    }

    public String puedoAgregarItem(MovimientoProduccion movimiento, Producto producto, BigDecimal cantidad) {

        String sError = "";

        if ((movimiento.getCircuito().getItemUnico().equals("S")) && (movimiento.getItemsProducto().size() > 1)) {
            sError += "Ha superado la cantidad máxima de items, no puede continuar agregando\n";

        }

        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            sError += "Ingrese un valor de cantidad válido. Mayor a 0\n";

        }

        if (producto == null) {
            sError += "Seleccione un producto para agregar al comprobante\n";

        }

        return sError;
    }

    public void actualizarCantidades(MovimientoProduccion movimiento, ItemProductoProduccion nItem) throws ExcepcionGeneralSistema {

        nItem.setCantidadStock(nItem.getCantidad());

        //Actualizamos la cantidad original solo si es un movimiento directo
        if (movimiento.getCircuito().getCircom().equals(movimiento.getCircuito().getCirapl())) {
            nItem.setCantidadOriginal(nItem.getCantidad());
        }

        //Si es una hoja de ruta vaciamos y volvemos a cargar los componentes
        if (movimiento.getCircuito().getTipoMovimiento() == TipoMovimientoProduccion.OP) {

            movimiento.getItemsComponente().clear();
            agregarComponentesYProcesos(movimiento, nItem);
        }

        if (movimiento.getCircuito().getAutomatizaParteProduccion().equals("S")) {

            ComposicionFormula composicionFormula = composicionFormulaRN.getComprosicionFormula(nItem.getProducto().getCodigo(), "STD");

            if (composicionFormula != null) {
                if (composicionFormula.getItemsComponente() == null) {
                    JsfUtil.addWarningMessage("La formula del producto seleccionado no contiene componentes");

                } else {
                    for (ItemComposicionFormula i : composicionFormula.getItemsComponente()) {

                        if (movimiento.getValeConsumo() != null) {

                            //Actualizamos la cantidad para los items materia prima
                            for (ItemProductoProduccion ivc : movimiento.getValeConsumo().getItemsProducto()) {

                                if (ivc.getProducto().equals(i.getProductoComponente())) {

                                    ivc.setCantidad(nItem.getCantidad().multiply(i.getCantidadNominal()));
                                    ivc.setCantidadStock(nItem.getCantidad().multiply(i.getCantidadNominal()));

//                                    actualizarAtributos(ivc);
                                }
                            }
                        }

                        //Actualizamos la cantidad para los items proceso
                        if (movimiento.getParteProceso() != null) {

                            for (ItemProductoProduccion ipp : movimiento.getParteProceso().getItemsProducto()) {

                                if (ipp.getProducto().equals(i.getProductoComponente())) {

                                    ipp.setCantidad(nItem.getCantidad().multiply(i.getCantidadNominal()));
                                    ipp.setCantidadStock(nItem.getCantidad().multiply(i.getCantidadNominal()));

//                                    actualizarAtributos(ipp);
                                }
                            }
                        }
                    }
                }
            } else {
                JsfUtil.addWarningMessage("El producto seleccionado no tiene una fórmula de producción definida");
            }
        }

//        actualizarAtributos(nItem);
    }

    /**
     * Actualizamos los atributos de stock, por el momento solo maneja nro de
     * serie
     *
     * @param item
     */
    public void actualizarAtributosProducto(ItemProductoProduccion item) {

        if (item.getItemDetalle() == null || item.getItemDetalle().isEmpty()) {
            ItemDetalleItemMovimientoProduccion nuevoItemDetalle = itemMovimientoRN.nuevoItemDetalleItemProducto(item);
        }

        for (ItemDetalleItemMovimientoProduccion id : item.getItemDetalle()) {

            id.setCantidad(item.getCantidad());

            if (item.getAtributo1() != null && !item.getAtributo1().isEmpty()) {
                id.setAtributo1(item.getAtributo1());
            }
            if (item.getAtributo2() != null && !item.getAtributo2().isEmpty()) {
                id.setAtributo2(item.getAtributo2());
            }
            if (item.getAtributo3() != null && !item.getAtributo3().isEmpty()) {
                id.setAtributo3(item.getAtributo3());
            }
            if (item.getAtributo3() != null && !item.getAtributo3().isEmpty()) {
                id.setAtributo3(item.getAtributo3());
            }
            if (item.getAtributo4() != null && !item.getAtributo4().isEmpty()) {
                id.setAtributo4(item.getAtributo4());

            }
            if (item.getAtributo5() != null && !item.getAtributo5().isEmpty()) {
                id.setAtributo5(item.getAtributo5());
            }
            if (item.getAtributo6() != null && !item.getAtributo6().isEmpty()) {
                id.setAtributo6(item.getAtributo6());
            }
            if (item.getAtributo7() != null && !item.getAtributo7().isEmpty()) {
                id.setAtributo7(item.getAtributo7());
            }
        }
        //item.getItemDetalle().clear();
        //ItemDetalleItemMovimientoProduccion nuevoItemDetalle = itemMovimientoRN.nuevoItemDetalle(item);
    }

    /**
     * Actualizamos los atributos de stock, por el momento solo maneja nro de
     * serie
     *
     * @param item
     */
    public void actualizarAtributosComponente(ItemComponenteProduccion item) {

        if (item.getItemDetalle() == null || item.getItemDetalle().isEmpty()) {
            ItemDetalleItemComponenteProduccion nuevoItemDetalle = itemMovimientoRN.nuevoItemDetalleItemComponente(item);
        }

        for (ItemDetalleItemComponenteProduccion id : item.getItemDetalle()) {

            id.setCantidad(item.getCantidad());

            if (item.getAtributo1() != null && !item.getAtributo1().isEmpty()) {
                id.setAtributo1(item.getAtributo1());
            }
            if (item.getAtributo2() != null && !item.getAtributo2().isEmpty()) {
                id.setAtributo2(item.getAtributo2());
            }
            if (item.getAtributo3() != null && !item.getAtributo3().isEmpty()) {
                id.setAtributo3(item.getAtributo3());
            }
            if (item.getAtributo3() != null && !item.getAtributo3().isEmpty()) {
                id.setAtributo3(item.getAtributo3());
            }
            if (item.getAtributo4() != null && !item.getAtributo4().isEmpty()) {
                id.setAtributo4(item.getAtributo4());

            }
            if (item.getAtributo5() != null && !item.getAtributo5().isEmpty()) {
                id.setAtributo5(item.getAtributo5());
            }
            if (item.getAtributo6() != null && !item.getAtributo6().isEmpty()) {
                id.setAtributo6(item.getAtributo6());
            }
            if (item.getAtributo7() != null && !item.getAtributo7().isEmpty()) {
                id.setAtributo7(item.getAtributo7());
            }
        }
        //item.getItemDetalle().clear();
        //ItemDetalleItemMovimientoProduccion nuevoItemDetalle = itemMovimientoRN.nuevoItemDetalle(item);
    }

    private void generarDatosAdicionalesAut(MovimientoProduccion m) throws ExcepcionGeneralSistema, Exception {

        generarDatosAdicionales(m);

        if (m.getValeConsumo() != null) {
            generarDatosAdicionales(m.getValeConsumo());
        }

        if (m.getParteProceso() != null) {
            generarDatosAdicionales(m.getParteProceso());
        }
    }

    private void generarDatosAdicionales(MovimientoProduccion m) throws ExcepcionGeneralSistema, Exception {

        sincronizarCantidades(m);
        generarItemsDetallesVacio(m);

    }

    public void puedoGuardarMovmientoAut(MovimientoProduccion m) throws ExcepcionGeneralSistema {

        puedoGuardarMovimiento(m);

        if (m.getValeConsumo() != null) {
            puedoGuardarMovimiento(m.getValeConsumo());
        }

        if (m.getParteProceso() != null) {
            puedoGuardarMovimiento(m.getParteProceso());
        }
    }

    public void puedoGuardarMovimiento(MovimientoProduccion m) throws ExcepcionGeneralSistema {

        if (m.getItemsProducto().isEmpty()) {
            throw new ExcepcionGeneralSistema("El detalle de productos está vacío, no se puede guardar");
        }

        if (m.getCircuito().getActualizaStock().equals("S") && m.getMovimientoStock() == null) {

            throw new ExcepcionGeneralSistema("El circuito define que actualiza stock, pero el comprobante no fue generado o no está configurado");
        }

        if (m.getPlanta() == null) {
            throw new ExcepcionGeneralSistema("No se definió la planta de producción");
        }

        for (ItemProductoProduccion i : m.getItemsProducto()) {

            i.setConError(false);

            if (i.getActualizaStock() == null) {
                i.setConError(true);
                throw new ExcepcionGeneralSistema("El item " + i.getProducto().getDescripcion() + " no tiene definido la actualizaicón de stock");
            }

            if (m.getCircuito().getActualizaStock().equals("S")) {

                if (i.getItemDetalle() == null || i.getItemDetalle().isEmpty()) {
                    i.setConError(true);
                    throw new ExcepcionGeneralSistema("El item " + i.getProducto().getDescripcion() + " no tiene items de apertura");
                }

                //variable temporal para comparar la suma de los items
                BigDecimal cantItems = BigDecimal.ZERO;
                for (ItemDetalleItemMovimientoProduccion d : i.getItemDetalle()) {

                    //Acumulamos las cantidades
                    cantItems = cantItems.add(d.getCantidad().setScale(2, BigDecimal.ROUND_HALF_DOWN));

                    if (d.getCantidad().equals(BigDecimal.ZERO)) {
                        i.setConError(true);
                        throw new ExcepcionGeneralSistema("No puede tener un item de apertura con cantidad cero en " + i.getProducto().getDescripcion());
                    }

                    //Si el producto controla stock por numero de serie solicita obligatoriamente
                    if (d.getProducto().getAdministraAtributo1().equals("S")) {
                        if (d.getAtributo1().isEmpty()) {
                            i.setConError(true);
                            throw new ExcepcionGeneralSistema("No puede tener un item de apertura con serie en blanco en " + i.getProducto().getDescripcion());
                        }
                    }
                }

                if (!i.getCantidad().setScale(2, BigDecimal.ROUND_FLOOR).equals(cantItems)) {

                    i.setConError(true);
                    throw new ExcepcionGeneralSistema("No coincide la cantidad (" + i.getCantidad() + ") de " + i.getProducto().getDescripcion()
                            + " con la sumatoria en de los items de apertura (" + cantItems + ")");
                }

                if (m.getTipoMovimiento().equals(TipoMovimientoProduccion.PR)) {

                    if (i.getOperario() == null) {
                        i.setConError(true);
                        throw new ExcepcionGeneralSistema("No ingresó operario en item " + i.getProducto().getDescripcion());
                    }
                }
            }
        }
    }

    /**
     * Generar los items detalle con los números de atributos para el stock
     * Genera un item detalle para todos los items productos, componentes
     *
     * @param movimientoProduccion
     * @throws ExcepcionGeneralSistema
     */
    public void generarItemsDetallesVacio(MovimientoProduccion movimientoProduccion) throws ExcepcionGeneralSistema {

        if (movimientoProduccion.getItemsProducto() != null) {

            for (ItemProductoProduccion itemProducto : movimientoProduccion.getItemsProducto()) {

                //Si el detalle está vacio lo llenamos, de lo contrario ya viene cargado desde la pagina
                if (itemProducto.getItemDetalle().isEmpty()) {
                    generarItemDetalleItemProducto(itemProducto);
                }
            }
        }

        if (movimientoProduccion.getItemsComponente() != null) {

            for (ItemComponenteProduccion itemComponente : movimientoProduccion.getItemsComponente()) {

                //Si el detalle está vacio lo llenamos, de lo contrario ya viene cargado desde la pagina
                if (itemComponente.getItemDetalle().isEmpty()) {
                    generarItemDetalleItemComponente(itemComponente);
                }
            }
        }

    }

    public void generarItemDetalleItemProducto(ItemProductoProduccion itemProducto) throws ExcepcionGeneralSistema {

        if (itemProducto.getItemDetalle() == null) {
            itemProducto.setItemDetalle(new ArrayList<ItemDetalleItemProductoProduccion>());
        }

        ItemDetalleItemProductoProduccion itemDetalle = itemMovimientoRN.nuevoItemDetalleItemProducto(itemProducto);
        itemProducto.getItemDetalle().add(itemDetalle);
    }

    public void generarItemDetalleItemComponente(ItemComponenteProduccion itemProducto) throws ExcepcionGeneralSistema {

        if (itemProducto.getItemDetalle() == null) {
            itemProducto.setItemDetalle(new ArrayList<ItemDetalleItemComponenteProduccion>());
        }

        ItemDetalleItemComponenteProduccion itemDetalle = itemMovimientoRN.nuevoItemDetalleItemComponente(itemProducto);
        itemProducto.getItemDetalle().add(itemDetalle);
    }

    /**
     * Generamos los items producto del movimientos en base a los items
     * pendientes
     *
     * @param m Movimiento de produccción
     * @param itemsPendiente items pendientes necesarios para generar el
     * movimiento
     * @throws ExcepcionGeneralSistema
     *
     */
    public void generarItemsProductoFromPendiente(MovimientoProduccion m, List<PendienteProduccionDetalle> itemsPendiente) throws ExcepcionGeneralSistema {

        if (itemsPendiente.isEmpty()) {
            return;
        }

        int cantSel = 0;
        for (PendienteProduccionDetalle itemPendiente : itemsPendiente) {

            if (itemPendiente.isSeleccionado()) {

                cantSel++;
                ItemProductoProduccion nItem = itemMovimientoRN.nuevoItemProducto(m);

                itemMovimientoRN.generarItemFromItemPendiente(m, itemPendiente, nItem);
                generarItemDetalleItemProducto(nItem);
                //Agregamos el item a la lista
                m.getItemsProducto().add(nItem);
            }
        }
        if (cantSel == 0) {
            throw new ExcepcionGeneralSistema("No ha seleccionado ningún producto");
        }
    }

    /**
     * Generamos los items componente del movimientos en base a los items
     * pendientes
     *
     * @param m Movimiento de produccción
     * @param itemsPendiente items pendientes necesarios para generar el
     * movimiento
     * @throws ExcepcionGeneralSistema
     *
     */
    public void generarItemsComponenteFromPendiente(MovimientoProduccion m, List<PendienteProduccionDetalle> itemsPendiente) throws ExcepcionGeneralSistema {

        if (itemsPendiente.isEmpty()) {
            return;
        }

        int cantSel = 0;
        for (PendienteProduccionDetalle itemPendiente : itemsPendiente) {

            if (itemPendiente.isSeleccionado()) {

                cantSel++;
                ItemComponenteProduccion nItem = itemMovimientoRN.nuevoItemComponente(m);
                itemMovimientoRN.generarItemFromItemPendiente(m, itemPendiente, nItem);
                generarItemDetalleItemComponente(nItem);
                //Agregamos el item a la lista
                m.getItemsComponente().add(nItem);

            }
        }
        if (cantSel == 0) {
            throw new ExcepcionGeneralSistema("No ha seleccionado ningún componente");
        }
    }

    /**
     * Generamos los items componente del movimientos en base a los items
     * pendientes
     *
     * @param m Movimiento de produccción
     * @param itemsPendiente items pendientes necesarios para generar el
     * movimiento
     * @throws ExcepcionGeneralSistema
     *
     */
    public void generarItemsProcesoFromPendiente(MovimientoProduccion m, List<PendienteProduccionDetalle> itemsPendiente) throws ExcepcionGeneralSistema {

        if (itemsPendiente.isEmpty()) {
            return;
        }

        int cantSel = 0;
        for (PendienteProduccionDetalle itemPendiente : itemsPendiente) {

            if (itemPendiente.isSeleccionado()) {

                cantSel++;
                ItemProcesoProduccion nItem = itemMovimientoRN.nuevoItemProceso(m);

                itemMovimientoRN.generarItemFromItemPendiente(m, itemPendiente, nItem);                
                //Agregamos el item a la lista
                m.getItemsProceso().add(nItem);
            }
        }
        if (cantSel == 0) {
            throw new ExcepcionGeneralSistema("No ha seleccionado ningún componente");
        }
    }

    /**
     * Generamos los items producto del movimientos en base a los items del
     * movmiento original
     *
     * @param movimientoProduccion Movimiento de produccción
     * @param itemsMovimiento items pendientes necesarios para generar el
     * movimiento
     * @throws ExcepcionGeneralSistema
     *
     */
    public void generarItemsProductoFromItemMovimiento(MovimientoProduccion movimientoProduccion, List<ItemMovimientoProduccion> itemsMovimiento) throws ExcepcionGeneralSistema {

        if (itemsMovimiento.isEmpty()) {
            return;
        }

        int cantSel = 0;
        for (ItemMovimientoProduccion itemMovimientoProduccion : itemsMovimiento) {

            if (itemMovimientoProduccion.getProduccion() != null && itemMovimientoProduccion.getProduccion().compareTo(BigDecimal.ZERO) > 0) {

                cantSel++;
                ItemProductoProduccion nItem = itemMovimientoRN.nuevoItemProducto(movimientoProduccion);

                itemMovimientoRN.generarItemFromItemMovimiento(movimientoProduccion, itemMovimientoProduccion, nItem);

                if (movimientoProduccion.getCircuito().getActualizaStock().equals("S")) {
                    generarItemDetalleItemProducto(nItem);
                }

                //Agregamos el item a la lista
                movimientoProduccion.getItemsProducto().add(nItem);
            }
        }
        if (cantSel == 0) {
            throw new ExcepcionGeneralSistema("No ha seleccionado ningún producto");
        }
    }

    /**
     * Generamos los items componentes del movimientos en base a los items del
     * movmiento original
     *
     * @param movimientoProduccion Movimiento de produccción
     * @param itemsMovimiento items pendientes necesarios para generar el
     * movimiento
     * @throws ExcepcionGeneralSistema
     *
     */
    public void generarItemsComponenteFromItemMovimiento(MovimientoProduccion movimientoProduccion, List<ItemMovimientoProduccion> itemsMovimiento) throws ExcepcionGeneralSistema {

        if (itemsMovimiento.isEmpty()) {
            return;
        }

        int cantSel = 0;
        for (ItemMovimientoProduccion itemMovimientoProduccion : itemsMovimiento) {

            if (itemMovimientoProduccion.getProduccion() != null && itemMovimientoProduccion.getProduccion().compareTo(BigDecimal.ZERO) > 0) {

                cantSel++;
                ItemComponenteProduccion nItem = itemMovimientoRN.nuevoItemComponente(movimientoProduccion);
                itemMovimientoRN.generarItemFromItemMovimiento(movimientoProduccion, itemMovimientoProduccion, nItem);

                if (movimientoProduccion.getCircuito().getActualizaStock().equals("S")) {
                    generarItemDetalleItemComponente(nItem);
                }

                //Agregamos el item a la lista
                movimientoProduccion.getItemsComponente().add(nItem);
            }
        }
        if (cantSel == 0) {
            throw new ExcepcionGeneralSistema("No ha seleccionado ningún producto");
        }
    }

    /**
     * Generamos los items procesos del movimientos en base a los items del
     * movmiento original
     *
     * @param movimientoProduccion Movimiento de produccción
     * @param itemsMovimiento items pendientes necesarios para generar el
     * movimiento
     * @throws ExcepcionGeneralSistema
     *
     */
    public void generarItemsProcesoFromItemMovimiento(MovimientoProduccion movimientoProduccion, List<ItemMovimientoProduccion> itemsMovimiento) throws ExcepcionGeneralSistema {

        if (itemsMovimiento.isEmpty()) {
            return;
        }

        int cantSel = 0;
        for (ItemMovimientoProduccion itemMovimientoProduccion : itemsMovimiento) {

            if (itemMovimientoProduccion.getProduccion() != null && itemMovimientoProduccion.getProduccion().compareTo(BigDecimal.ZERO) > 0) {

                cantSel++;
                ItemProcesoProduccion nItem = itemMovimientoRN.nuevoItemProceso(movimientoProduccion);
                itemMovimientoRN.generarItemFromItemMovimiento(movimientoProduccion, itemMovimientoProduccion, nItem);

                //Agregamos el item a la lista
                movimientoProduccion.getItemsProceso().add(nItem);

            }
        }
        if (cantSel == 0) {
            throw new ExcepcionGeneralSistema("No ha seleccionado ningún producto");
        }
    }

    public void sincronizarCantidades(MovimientoProduccion m) {

        for (ItemProductoProduccion i : m.getItemsProducto()) {

            if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                JsfUtil.addErrorMessage("Ingrese un valor de cantidad válido. Mayor a 0");
                return;
            }

            if (m.getItemsAplicacion() != null) {

                for (ItemAplicacionProduccion a : m.getItemsAplicacion()) {

                    if (i.getProducto().equals(a.getProducto())) {

                        //Items aplicación van con valores negativos
                        a.setCantidad(i.getCantidad().negate());
                        a.setCantidadStock(i.getCantidad().negate());
                        a.setCantidadOriginal(i.getCantidad().negate());
                    }
                }
            }

            i.setCantidadStock(i.getCantidad());
            i.setCantidadOriginal(i.getCantidad());
        }
    }

    public MovimientoProduccion getMovimiento(Integer id) {

        return produccionDAO.getMovimiento(id);
    }

    public MovimientoProduccion getMovimiento(Integer id, boolean calculaPendienteItem, boolean generaItems) throws ExcepcionGeneralSistema {

        MovimientoProduccion m = produccionDAO.getMovimiento(id);

        if (m == null) {
            return null;
        }

        if (calculaPendienteItem) {
            calcularPendientes(m);
        }

        if (generaItems) {            
            generarItemsDetallesVacio(m);
        }

        return m;
    }

    public MovimientoProduccion getMovimiento(String codFormulario, Integer numeroFormulario) {

        return produccionDAO.getMovimiento(codFormulario, numeroFormulario);
    }

    public PendienteProduccionGrupo getMovimientoPendiente(Map<String, String> filtro) {

        return produccionDAO.getPendienteGrupo(filtro);
    }

    public List<PendienteProduccionGrupo> getMovimientosPendiente(Map<String, String> filtro) {

        return produccionDAO.getPendientesGrupo(filtro);
    }

    public List<PendienteProduccionDetalle> getItemsPendiente(Map<String, String> filtro) {

        return produccionDAO.getPendientesDetalle(filtro);
    }

    public PendienteProduccionDetalle getItemPendiente(Map<String, String> filtro) {

        return produccionDAO.getPendienteDetalle(filtro);
    }

    /**
     * Eliminar un item de un movimiento
     *
     * @param mov movimiento del cual se eliminará el item
     * @param nItem item a eliminar
     */
    public boolean eliminarItemProducto(MovimientoProduccion mov, ItemProductoProduccion nItem) {

        boolean fItemBorrado = false;
        int i = 0;
        int indiceItemProducto = -1;
        int indiceItemAplicacion = -1;

        for (ItemProductoProduccion ip : mov.getItemsProducto()) {

            if (ip.getProducto() != null) {

                if (ip.getProducto().equals(nItem.getProducto())) {
                    indiceItemProducto = i;
                }
            }
            i++;
        }
        i = 0;

        if (mov.getItemsAplicacion() != null) {
            for (ItemAplicacionProduccion a : mov.getItemsAplicacion()) {

                if (a.getProducto().equals(nItem.getProducto())) {
                    indiceItemAplicacion = i;
                }
                i++;
            }
        }

        //Borramos los items productos
        if (indiceItemProducto >= 0) {
            ItemProductoProduccion remove = mov.getItemsProducto().remove(indiceItemProducto);
            if (remove != null) {
                fItemBorrado = true;
            }
        }

        //Borramos los items aplicación si existen
        if (indiceItemAplicacion >= 0) {
            ItemAplicacionProduccion remove = mov.getItemsAplicacion().remove(indiceItemAplicacion);
            if (remove != null) {
                fItemBorrado = true;
            }
        }

        return fItemBorrado;
    }
    
    public void seleccionarTodo(List<PendienteProduccionDetalle> itemsPendiente, boolean seleccionarTodo) {

        if (itemsPendiente == null) {
            return;
        }

        for (PendienteProduccionDetalle i : itemsPendiente) {
            i.setSeleccionado(seleccionarTodo);
        }
    }

    /**
     * Si existe items series de programación los borramos cuando el movimiento
     * es de anulación
     *
     * @param m Movmiento de producción
     */
    private void borrarItemsSerie(MovimientoProduccion m) throws ExcepcionGeneralSistema {

        //Verificamos si el circuito es de anulacion
        if (m.getCircuito().getEsAnulacion().equals("S")) {

            for (ItemAplicacionProduccion ia : m.getItemsAplicacion()) {

//                ItemProductoProduccion ip = itemMovimientoRN.getItemProducto(idPK);
//
//                if (ip != null) {
//
//                    produccionDAO.borrarItemsSerie(ip);
//                }
            }
        }
    }

    public void asignarFormulario(MovimientoProduccion m) throws ExcepcionGeneralSistema {

        Formulario formulario = null;
        //Buscamos el formulario correspondiente
        if (m.getCircuito().getActualizaProduccion().equals("S")) {

            formulario = formularioRN.getFormulario(m.getComprobante(), m.getSucursal(), "X");

        } else if (m.getCircuito().getActualizaStock().equals("S")) {

            formulario = formularioRN.getFormulario(m.getComprobante(), m.getSucursalStock(), "X");
        }

        if (formulario != null) {

            m.setNumeroFormulario(formulario.getUltimoNumero() + 1);
            m.setFormulario(formulario);
        }
    }

    private void tomarNumeroFormulario(MovimientoProduccion m) throws ExcepcionGeneralSistema {

        //Si no actualiza proveedores ni stock, entonces es facturación
        m.setNumeroFormulario(formularioRN.tomarProximoNumero(m.getFormulario()));

//        if (m.getComprobante()!= null && m.getCircuito().getActualizaProveedor().equals("S")) {
//
//            m.getMovimientoProveedor().setNumeroFormulario(formularioRN.tomarProximoNumero(m.getMovimientoProveedor().getFormulario()));
//            m.setNumeroFormulario(m.getMovimientoProveedor().getNumeroFormulario());
//
//            if (m.getComprobanteTesoreria() != null && m.getMovimientoTesoreria() != null) {
//
//                if (!m.getMovimientoTesoreria().getFormulario().equals(m.getMovimientoTesoreria().getFormulario())) {
//                    m.getMovimientoTesoreria().setNumeroFormulario(formularioRN.tomarProximoNumero(m.getMovimientoTesoreria().getFormulario()));
//                } else {
//                    m.getMovimientoTesoreria().setNumeroFormulario(m.getMovimientoProveedor().getNumeroFormulario());
//                }
//            }
//
//            if (m.getComprobanteStock() != null && m.getMovimientoStock() != null) {
//                if (!m.getFormulario().equals(m.getMovimientoStock().getFormulario())) {
//                    m.getMovimientoStock().setNumeroFormulario(formularioRN.tomarProximoNumero(m.getMovimientoStock().getFormulario()));
//                } else {
//                    m.getMovimientoStock().setNumeroFormulario(m.getMovimientoProveedor().getNumeroFormulario());
//                }
//            }
//
//        } else if (m.getComprobanteStock() != null && m.getMovimientoStock() != null) {
//
//            m.getMovimientoStock().setNumeroFormulario(formularioRN.tomarProximoNumero(m.getMovimientoStock().getFormulario()));
//            m.setNumeroFormulario(m.getMovimientoStock().getNumeroFormulario());
//
//        } else {
// //Si no actualiza proveedores ni stock, entonces es facturación
//            m.setNumeroFormulario(formularioRN.tomarProximoNumero(m.getFormulario()));
//           
//        }
    }

    public List<MovimientoProduccion> getListaByBusqueda(Map<String, String> filtro, boolean soloPendientes, int cantMax) {

        return produccionDAO.getListaByBusqueda(filtro, soloPendientes, cantMax);
    }

    private void borrarItemsNoValidos(MovimientoProduccion m) {

        if (m.getItemsProducto() == null) {
            return;
        }

        String indiceBorrar[] = new String[m.getItemsProducto().size()];

        //Seteamos los valores en -1
        for (int i = 0; i < indiceBorrar.length; i++) {
            indiceBorrar[i] = "N";
        }

        for (int i = 0; i < m.getItemsProducto().size(); i++) {

            ItemMovimientoProduccion im = m.getItemsProducto().get(i);

            if (im.getProducto() == null) {
                indiceBorrar[i] = "S";
                continue;
            }

//            if (!im.isTodoOk()) {
//                indiceBorrar[i] = "S";
//            }
        }

        for (int i = 0; i < indiceBorrar.length; i++) {

            if (indiceBorrar[i].equals("S")) {
                ItemMovimientoProduccion remove = m.getItemsProducto().remove(i);
            }
        }
    }

    private boolean sincronizarIdAplicacion(MovimientoProduccion m) {

        boolean sincronizar = false;

        for (ItemProductoProduccion ip : m.getItemsProducto()) {

            if (ip.getIdItemAplicacion() == null) {
                ip.setIdItemAplicacion(ip.getId());
                sincronizar = true;
            }
        }

        if (m.getItemsComponente() != null) {
            for (ItemComponenteProduccion ic : m.getItemsComponente()) {

                if (ic.getIdItemAplicacion() == null) {
                    ic.setIdItemAplicacion(ic.getId());
                    sincronizar = true;
                }
            }
        }

        if (m.getItemsProceso() != null) {
            for (ItemProcesoProduccion ic : m.getItemsProceso()) {

                if (ic.getIdItemAplicacion() == null) {
                    ic.setIdItemAplicacion(ic.getId());
                    sincronizar = true;
                }
            }
        }

        return sincronizar;
    }

    public boolean tengoItemsSeleccionados(List<PendienteProduccionDetalle> itemsPendientes) {

        if (itemsPendientes == null || itemsPendientes.isEmpty()) {
            return false;
        }

        for (PendienteProduccionDetalle p : itemsPendientes) {

            if (p.isSeleccionado()) {
                return true;
            }
        }
        return false;
    }

    public boolean tengoItemsConCantidad(CircuitoProduccion circuito, Object itemsPendientes) {

        if (itemsPendientes == null || ((List<ItemMovimientoProduccion>) itemsPendientes).isEmpty()) {
            return false;
        }

        if (circuito.getNoControlaPendiente().equals("N")) {
            return true;
        }

        for (ItemMovimientoProduccion i : (List<ItemMovimientoProduccion>) itemsPendientes) {

            if (i.getPendiente() != null && i.getPendiente().compareTo(BigDecimal.ZERO) > 0) {
                return true;
            }
        }
        return false;
    }

    public void calcularPendientes(MovimientoProduccion m) {

        if (m == null) {
            return;
        }

        Map<String, String> filtro = new HashMap<String, String>();

        for (ItemProductoProduccion i : m.getItemsProducto()) {

            filtro.clear();
            filtro.put("modfor=", "'" + m.getFormulario().getModfor() + "'");
            filtro.put("codfor=", "'" + m.getFormulario().getCodigo() + "'");
            filtro.put("nrofor=", "" + m.getNumeroFormulario());
            filtro.put("formul <> ", "''");
            filtro.put("stocks = ", "'S'");

            PendienteProduccionDetalle pd = produccionDAO.getPendienteDetalle(filtro);

            if (pd != null) {
                i.setPendiente(pd.getPendiente());
            }
        }

        for (ItemComponenteProduccion i : m.getItemsComponente()) {

            filtro.clear();
            filtro.put("modfor=", "'" + m.getFormulario().getModfor() + "'");
            filtro.put("codfor=", "'" + m.getFormulario().getCodigo() + "'");
            filtro.put("nrofor=", "" + m.getNumeroFormulario());
            filtro.put("formul = ", "''");
            filtro.put("stocks =", "'S'");
            filtro.put("artcod = ", "'" + i.getProducto().getCodigo() + "'");

            PendienteProduccionDetalle pd = produccionDAO.getPendienteDetalle(filtro);

            if (pd != null) {
                i.setPendiente(pd.getPendiente());
            }
        }

        for (ItemProcesoProduccion i : m.getItemsProceso()) {

            filtro.clear();
            filtro.put("modfor=", "'" + m.getFormulario().getModfor() + "'");
            filtro.put("codfor=", "'" + m.getFormulario().getCodigo() + "'");
            filtro.put("nrofor=", "" + m.getNumeroFormulario());
            filtro.put("formul = ", "''");
            filtro.put("stocks =", "'N'");
            filtro.put("artcod =", "'" + i.getProducto().getCodigo() + "'");

            PendienteProduccionDetalle pd = produccionDAO.getPendienteDetalle(filtro);

            if (pd != null) {
                i.setPendiente(pd.getPendiente());
            }
        }

    }

    public void eliminarMovimiento(MovimientoProduccion movimientoProduccion) throws Exception {

        produccionDAO.eliminar(MovimientoProduccion.class, movimientoProduccion.getId());
        movimientoStockRN.recalcularStock();
        //if(movimientoProduccion.getMovimientoStock()!=null){
        //    movimientoStockRN.eliminarMovimiento(movimientoProduccion.getMovimientoStock());
        //}        
    }

    public boolean tengoItemsConCantidad(Object itemsPendientes) {

        if (itemsPendientes == null || ((List<ItemMovimientoProduccion>) itemsPendientes).isEmpty()) {
            return false;
        }

        for (ItemMovimientoProduccion i : (List<ItemMovimientoProduccion>) itemsPendientes) {

            if (i.getProduccion() != null && i.getProduccion().compareTo(BigDecimal.ZERO) > 0) {
                return true;
            }
        }
        return false;
    }

}
