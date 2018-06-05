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
import bs.produccion.modelo.ItemHorarioProduccion;
import bs.produccion.modelo.ItemMovimientoProduccion;
import bs.produccion.modelo.ItemProcesoProduccion;
import bs.produccion.modelo.ItemProductoProduccion;
import bs.produccion.modelo.MovimientoProduccion;
import bs.produccion.modelo.TipoMovimientoProduccion;
import bs.produccion.vista.PendienteProduccionDetalle;
import bs.produccion.vista.PendienteProduccionGrupo;
import bs.stock.modelo.ComposicionFormula;
import bs.stock.modelo.ComprobanteStock;
import bs.stock.modelo.ItemComposicionFormulaComponente;
import bs.stock.modelo.ItemComposicionFormulaProceso;
import bs.stock.modelo.MovimientoStock;
import bs.stock.modelo.ParametroStock;
import bs.stock.modelo.Producto;
import bs.stock.rn.ComposicionFormulaRN;
import bs.stock.rn.MovimientoStockRN;
import bs.stock.rn.ParametroStockRN;
import bs.stock.rn.StockRN;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private StockRN stockRN;
    @EJB
    private ParametroStockRN parametroStockRN;
    @EJB
    private ParametroProduccionRN parametroProduccionRN;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public synchronized MovimientoProduccion guardar(MovimientoProduccion movimiento) throws Exception {

        sincronizarCantidades(movimiento);
        controlComprobante(movimiento);

        if (movimiento.getId() == null) {

            generarMovimientosAdicionales(movimiento);

            if (!movimiento.isNoSincronizaNumeroFormulario() && movimiento.getNumeroFormulario() > 0) {
                tomarNumeroFormulario(movimiento);
            }

            produccionDAO.crear(movimiento);

            if (sincronizarIdAplicacion(movimiento)) {
                movimiento = produccionDAO.editar(movimiento);
            }

        } else {

            sincronizarIdAplicacion(movimiento);
            movimiento = produccionDAO.editar(movimiento);
        }

        movimiento.setPersistido(true);

        return movimiento;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public MovimientoProduccion editar(MovimientoProduccion movimiento) {

        return produccionDAO.editar(movimiento);

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public synchronized MovimientoProduccion guardarSincronizacion(MovimientoProduccion movimiento) throws Exception {

        if (movimiento.getId() == null) {

            sincronizarCantidades(movimiento);
            controlComprobante(movimiento);

            generarMovimientosAdicionales(movimiento);

            if (!movimiento.isNoSincronizaNumeroFormulario() && movimiento.getNumeroFormulario() > 0) {
                tomarNumeroFormulario(movimiento);
            }

            produccionDAO.crear(movimiento);

            if (sincronizarIdAplicacion(movimiento)) {
                movimiento = produccionDAO.editar(movimiento);
            }

        } else {

            sincronizarIdAplicacion(movimiento);
            movimiento = produccionDAO.editar(movimiento);
        }

        movimiento.setPersistido(true);

        return movimiento;
    }

    public void generarMovimientosAdicionales(MovimientoProduccion m) throws ExcepcionGeneralSistema, Exception {

        if (m.getCircuito().getActualizaStock().equals("S") && m.getComprobanteStock() == null) {
            throw new ExcepcionGeneralSistema("El circuito define que actualiza stock, pero el comprobante de sotck es nulo ");
        }

        MovimientoStock ms = movimientoStockRN.generarComprobante(m);
        m.setMovimientoStock(ms);
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
            comprobanteST = circuito.getComprobanteStock();
        }
        
        MovimientoProduccion m = crearMovimiento(circuito, comprobante, circuito.getTipoMovimiento(), sucursal, sucursalStock);

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
            PendienteProduccionGrupo pendienteGrupo,
            List<PendienteProduccionDetalle> itemsPendientes) throws ExcepcionGeneralSistema, Exception {

        if (!tengoItemsSeleccionados(itemsPendientes)) {
            throw new ExcepcionGeneralSistema("No existen items seleccionados para generar el movimiento");
        }

        MovimientoProduccion m = nuevoMovimiento(circuito, sucursal, sucursalStock);

        m.setPlanta(pendienteGrupo.getPlanta());

        if (m.getTipoMovimiento().equals(TipoMovimientoProduccion.PP)) {
            generarItemsProductoFromPendiente(m, itemsPendientes);
        }

        if (m.getTipoMovimiento().equals(TipoMovimientoProduccion.VC)) {
            generarItemsComponenteFromPendiente(m, itemsPendientes);
        }

        if (m.getTipoMovimiento().equals(TipoMovimientoProduccion.PR)) {
            generarItemsProcesoFromPendiente(m, itemsPendientes);
        }

        if (m.getTipoMovimiento().equals(TipoMovimientoProduccion.PH)) {
            generarItemsHorarioFromPendiente(m, itemsPendientes);
        }

        asignarFormulario(m);

//        if (circuito.getPermiteAgregarItems().equals("S")) {
//            //Cargarmos un nuevo item en blanco en caso de que quieran guardar sin agregar un items
//            m.getItemsProducto().add((ItemProductoProduccion) nuevoItemProducto(m));
//        }
        return m;
    }

    public MovimientoProduccion nuevoMovimientoFromItems(CircuitoProduccion circuito, Sucursal sucursal, Sucursal sucursalStock,
            Object itemsMovimiento) throws ExcepcionGeneralSistema, Exception {

        MovimientoProduccion movimientoProduccion = nuevoMovimiento(circuito, sucursal, sucursalStock);

        if (movimientoProduccion.getTipoMovimiento().equals(TipoMovimientoProduccion.PP)) {
            generarItemsProductoFromItemMovimiento(movimientoProduccion, (List<ItemProductoProduccion>) itemsMovimiento);
        }

        if (movimientoProduccion.getTipoMovimiento().equals(TipoMovimientoProduccion.VC)) {
            generarItemsComponenteFromItemMovimiento(movimientoProduccion, (List<ItemComponenteProduccion>) itemsMovimiento);
        }

        if (movimientoProduccion.getTipoMovimiento().equals(TipoMovimientoProduccion.PR)) {
            generarItemsProcesoFromItemMovimiento(movimientoProduccion, (List<ItemProcesoProduccion>) itemsMovimiento);
        }

        if (movimientoProduccion.getTipoMovimiento().equals(TipoMovimientoProduccion.PH)) {
            generarItemsHorarioFromItemMovimiento(movimientoProduccion, (List<ItemHorarioProduccion>) itemsMovimiento);
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
//        m.setMonedaRegistracion(comprobante.getMonedaRegistracion());
        m.setMonedaSecundaria(moneda);

        asignarFormulario(m);

        if (comprobante.getDeposito() != null) {
            m.setDeposito(comprobante.getDeposito());
        }

        if (comprobante.getDepositoTransferencia() != null) {
            m.setDepositoTransferencia(comprobante.getDepositoTransferencia());
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
                item = nuevoItemProducto(movimiento);
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
            movimiento.getItemsProducto().add((ItemProductoProduccion) nuevoItemProducto(movimiento));

        } catch (Exception ex) {
            throw new ExcepcionGeneralSistema("No es posible agregar item " + ex);
        }
    }

    public void agregarComponentesYProcesos(MovimientoProduccion movimiento, ItemProductoProduccion itemProducto) throws ExcepcionGeneralSistema {
        
        if(itemProducto.getFormula()!=null){
            System.err.println("le falta la formula...");
            return;
        }
        
        if(itemProducto.getProducto()!=null){
            System.err.println("le falta el producto...");
            return;
        }
        
        System.err.println("me muestra...");
               
        ComposicionFormula composicionFormula = composicionFormulaRN.getComprosicionFormula(itemProducto.getProducto().getCodigo(), itemProducto.getFormula().getCodigo());
        
        if (composicionFormula != null) {

            if (composicionFormula.getItemsComponente() == null && composicionFormula.getItemsProceso() == null) {

                throw new ExcepcionGeneralSistema("La formula del producto seleccionado no contiene componentes ni procesos ");

            } else {

                if (composicionFormula.getItemsComponente() != null) {

                    movimiento.getItemsComponente().clear();

                    for (ItemComposicionFormulaComponente i : composicionFormula.getItemsComponente()) {
                        
                        System.err.println("su argolla...");

                        ItemComponenteProduccion itemComponente = nuevoItemComponente(movimiento);

                        itemComponente.setProducto(i.getProductoComponente());
                        itemComponente.setProductoOriginal(i.getProductoComponente());
                        itemComponente.setGrupo(itemProducto.getGrupo());
                        itemComponente.setUnidadMedida(i.getProductoComponente().getUnidadDeMedida());
                        itemComponente.setActualizaStock(i.getProductoComponente().getGestionaStock());

                        itemComponente.setCantidadNominal(i.getCantidadNominal());
                        itemComponente.setCantidad(itemProducto.getCantidad().multiply(itemComponente.getCantidadNominal()));
                        itemComponente.setCantidadStock(itemProducto.getCantidad().multiply(itemComponente.getCantidadNominal()));
                        itemComponente.setCantidadOriginal(itemProducto.getCantidad().multiply(itemComponente.getCantidadNominal()));

                        movimiento.getItemsComponente().add(itemComponente);
                    }
                }

                if (composicionFormula.getItemsProceso() != null) {

                    movimiento.getItemsProceso().clear();

                    for (ItemComposicionFormulaProceso i : composicionFormula.getItemsProceso()) {

                        ItemProcesoProduccion itemProceso = nuevoItemProceso(movimiento);

                        itemProceso.setProducto(i.getProductoComponente());
                        itemProceso.setProductoOriginal(i.getProductoComponente());
                        itemProceso.setUnidadMedida(i.getProductoComponente().getUnidadDeMedida());
                        itemProceso.setActualizaStock(i.getProductoComponente().getGestionaStock());

                        itemProceso.setCantidadNominal(i.getCantidadNominal());
                        itemProceso.setCantidad(itemProducto.getCantidad().multiply(itemProceso.getCantidadNominal()));
                        itemProceso.setCantidadStock(itemProducto.getCantidad().multiply(itemProceso.getCantidadNominal()));
                        itemProceso.setCantidadOriginal(itemProducto.getCantidad().multiply(itemProceso.getCantidadNominal()));

                        movimiento.getItemsProceso().add(itemProceso);
                    }
                }
            }

        } else {
            throw new ExcepcionGeneralSistema("El producto (" + itemProducto.getProducto().getCodigo() + "-" + itemProducto.getProducto().getCodigo() + ") seleccionado no tiene una fórmula de producción definida");
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

    public void actualizarCantidades(MovimientoProduccion movimiento, ItemMovimientoProduccion itemProducto) throws ExcepcionGeneralSistema {

        itemProducto.setCantidadStock(itemProducto.getCantidad());

        //Actualizamos la cantidad original solo si es un movimiento directo
        if (movimiento.getCircuito().getCircom().equals(movimiento.getCircuito().getCirapl())) {
            itemProducto.setCantidadOriginal(itemProducto.getCantidad());
        }

        //Si es una orden de produccion vaciamos y volvemos a cargar los componentes
        if (movimiento.getCircuito().getTipoMovimiento() == TipoMovimientoProduccion.OP) {

            if (movimiento.getItemsComponente() != null) {
                for (ItemComponenteProduccion itemComponente : movimiento.getItemsComponente()) {

                    if (itemProducto.getGrupo().equals(itemComponente.getGrupo())) {
                        itemComponente.setCantidad(itemProducto.getCantidad().multiply(itemComponente.getCantidadNominal()));
                        itemComponente.setCantidadStock(itemProducto.getCantidad().multiply(itemComponente.getCantidadNominal()));
                        itemComponente.setCantidadOriginal(itemProducto.getCantidad().multiply(itemComponente.getCantidadNominal()));
                    }
                }
            }
        }

//        if (movimiento.getCircuito().getAutomatizaParteProduccion().equals("S")) {
//
//            ComposicionFormula composicionFormula = composicionFormulaRN.getComprosicionFormula(nItem.getProducto().getCodigo(), "STD");
//
//            if (composicionFormula != null) {
//                if (composicionFormula.getItemsComponente() == null) {
//                    JsfUtil.addWarningMessage("La formula del producto seleccionado no contiene componentes");
//
//                } else {
//                    for (ItemComposicionFormula i : composicionFormula.getItemsComponente()) {
//
//                        if (movimiento.getValeConsumo() != null) {
//
//                            //Actualizamos la cantidad para los items materia prima
//                            for (ItemProductoProduccion ivc : movimiento.getValeConsumo().getItemsProducto()) {
//
//                                if (ivc.getProducto().equals(i.getProductoComponente())) {
//
//                                    ivc.setCantidad(nItem.getCantidad().multiply(i.getCantidadNominal()));
//                                    ivc.setCantidadStock(nItem.getCantidad().multiply(i.getCantidadNominal()));
//
////                                    actualizarAtributos(ivc);
//                                }
//                            }
//                        }
//
//                        //Actualizamos la cantidad para los items proceso
//                        if (movimiento.getParteProceso() != null) {
//
//                            for (ItemProductoProduccion ipp : movimiento.getParteProceso().getItemsProducto()) {
//
//                                if (ipp.getProducto().equals(i.getProductoComponente())) {
//
//                                    ipp.setCantidad(nItem.getCantidad().multiply(i.getCantidadNominal()));
//                                    ipp.setCantidadStock(nItem.getCantidad().multiply(i.getCantidadNominal()));
//
////                                    actualizarAtributos(ipp);
//                                }
//                            }
//                        }
//                    }
//                }
//            } else {
//                JsfUtil.addWarningMessage("El producto seleccionado no tiene una fórmula de producción definida");
//            }
//        }
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
            ItemDetalleItemMovimientoProduccion nuevoItemDetalle = nuevoItemDetalleItemProducto(item);
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
        //ItemDetalleItemMovimientoProduccion nuevoItemDetalle = nuevoItemDetalle(item);
    }

    /**
     * Actualizamos los atributos de stock, por el momento solo maneja nro de
     * serie
     *
     * @param item
     */
    public void actualizarAtributosComponente(ItemComponenteProduccion item) {

        if (item.getItemDetalle() == null || item.getItemDetalle().isEmpty()) {
            ItemDetalleItemComponenteProduccion nuevoItemDetalle = nuevoItemDetalleItemComponente(item);
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
        //ItemDetalleItemMovimientoProduccion nuevoItemDetalle = nuevoItemDetalle(item);
    }

    private void generarDatosAdicionalesAut(MovimientoProduccion m) throws ExcepcionGeneralSistema, Exception {

        sincronizarCantidades(m);

        if (m.getValeConsumo() != null) {
            sincronizarCantidades(m.getValeConsumo());
        }

        if (m.getParteProceso() != null) {
            sincronizarCantidades(m.getParteProceso());
        }
    }

    public void controlComprobante(MovimientoProduccion movimiento) throws ExcepcionGeneralSistema, Exception {

        String sErrores = "";

        if (movimiento.getId() != null) {
//            if (movimiento.getFormulario().getModfor().equals("PD")) {
//                sErrores += "- No es posible modificar un comprobante de producción \n";
//            }

            if (movimiento.getFormulario().getModfor().equals("ST")) {
                sErrores += "- No es posible modificar un comprobante de stock \n";
            }
        }

        if (movimiento.getTipoMovimiento().equals(TipoMovimientoProduccion.OP)) {

            if (movimiento.getItemsProducto().isEmpty()) {
                sErrores += "- El detalle de productos en " + movimiento.getFormulario().getDescripcion() + ", está vacío, no se puede guardar\n";
            }
        }

        if (movimiento.getTipoMovimiento().equals(TipoMovimientoProduccion.PP)) {

            if (movimiento.getItemsProducto().isEmpty()) {
                sErrores += "- El detalle de productos en " + movimiento.getFormulario().getDescripcion() + ", está vacío, no se puede guardar\n";
            }
        }

        if (movimiento.getTipoMovimiento().equals(TipoMovimientoProduccion.PR)) {

            if (movimiento.getItemsProceso().isEmpty()) {
                sErrores += "- El detalle de procesos en " + movimiento.getFormulario().getDescripcion() + ", está vacío, no se puede guardar\n";
            }
        }

        if (movimiento.getTipoMovimiento().equals(TipoMovimientoProduccion.VC)) {

            if (movimiento.getItemsComponente().isEmpty()) {
                sErrores += "- El detalle de consumos en " + movimiento.getFormulario().getDescripcion() + ", está vacío, no se puede guardar\n";
            }
        }

        if (movimiento.getTipoMovimiento().equals(TipoMovimientoProduccion.PH)) {

            if (movimiento.getItemsHorario().isEmpty()) {
                sErrores += "- El detalle de operarios en " + movimiento.getFormulario().getDescripcion() + ", está vacío, no se puede guardar\n";
            }
        }

        if (movimiento.getPlanta() == null) {
            sErrores += "- No se definió la planta de producción en " + movimiento.getFormulario().getDescripcion() + "\n";
        }

        // Controlamos los items producto
        if (movimiento.getItemsProducto() != null && !movimiento.getItemsProducto().isEmpty()) {

            for (ItemProductoProduccion i : movimiento.getItemsProducto()) {

                i.setConError(false);

                if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) == 0) {
                    i.setConError(true);
                    sErrores += "- El item " + i.getProducto().getDescripcion() + " no tiene un dato válido en cantidad\n";
                }

                if (i.getActualizaStock() == null) {
                    i.setConError(true);
                    sErrores += "- El item " + i.getProducto().getDescripcion() + " no tiene definido la actualizaicón de stock\n";
                }

                if (movimiento.getCircuito().getActualizaStock().equals("S")) {

                    if (i.getItemDetalle() == null || i.getItemDetalle().isEmpty()) {
                        i.setConError(true);
                        sErrores += "- El item " + i.getProducto().getDescripcion() + " no tiene items de apertura\n";
                    }
                    sErrores += controlItemsDetalleProducto(i);
                }
            }
        }

        //Controlamos los items componentes
        if (movimiento.getItemsComponente() != null && !movimiento.getItemsComponente().isEmpty()) {

            for (ItemComponenteProduccion i : movimiento.getItemsComponente()) {

                i.setConError(false);

                if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) == 0) {
                    i.setConError(true);
                    sErrores += "- El item " + i.getProducto().getDescripcion() + " no tiene un dato válido en cantidad\n";
                }

                if (i.getActualizaStock() == null) {
                    i.setConError(true);
                    sErrores += "- El item " + i.getProducto().getDescripcion() + " no tiene definido la actualizaicón de stock\n";
                }

                if (movimiento.getCircuito().getActualizaStock().equals("S")) {

                    if (i.getItemDetalle() == null || i.getItemDetalle().isEmpty()) {
                        i.setConError(true);
                        sErrores += "- El item " + i.getProducto().getDescripcion() + " no tiene items de apertura\n";
                    }

                    sErrores += controlItemsDetalleComponente(i);
                }
            }
        }

        //Controlamos los items proceso
        if (movimiento.getItemsProceso() != null && !movimiento.getItemsProceso().isEmpty()) {

            for (ItemProcesoProduccion i : movimiento.getItemsProceso()) {

                i.setConError(false);

                if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) == 0) {
                    i.setConError(true);
                    sErrores += "- El item " + i.getProducto().getDescripcion() + " no tiene un dato válido en cantidad\n";
                }

                if (!movimiento.getTipoMovimiento().equals(TipoMovimientoProduccion.OP)) {
                    if (i.getOperario() == null) {
                        i.setConError(true);
                        sErrores += "- No ingresó operario en item " + i.getProducto().getDescripcion() + "\n";
                    }
                }
            }
        }

        //Controlamos los items horario
        if (movimiento.getItemsHorario() != null && !movimiento.getItemsHorario().isEmpty()) {

            for (ItemHorarioProduccion i : movimiento.getItemsHorario()) {

                i.setConError(false);

                if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) == 0) {
                    i.setConError(true);
                    sErrores += "- El item " + i.getProducto().getDescripcion() + " no tiene un dato válido en cantidad\n";
                }

                if (!movimiento.getTipoMovimiento().equals(TipoMovimientoProduccion.OP)) {
                    if (i.getOperario() == null) {
                        i.setConError(true);
                        sErrores += "- No ingresó operario en item " + i.getProducto().getDescripcion() + "\n";
                    }
                }
            }
        }

        if (!sErrores.isEmpty()) {
            throw new ExcepcionGeneralSistema(sErrores);
        }
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
                ItemProductoProduccion nItem = nuevoItemProducto(m);
                generarItemFromItemPendiente(m, itemPendiente, nItem);
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
                ItemComponenteProduccion nItem = nuevoItemComponente(m);
                generarItemFromItemPendiente(m, itemPendiente, nItem);

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
                ItemProcesoProduccion nItem = nuevoItemProceso(m);
                generarItemFromItemPendiente(m, itemPendiente, nItem);
            }
        }
        if (cantSel == 0) {
            throw new ExcepcionGeneralSistema("No ha seleccionado ningún componente");
        }
    }

    public void generarItemsHorarioFromPendiente(MovimientoProduccion m, List<PendienteProduccionDetalle> itemsPendiente) throws ExcepcionGeneralSistema {

        if (itemsPendiente.isEmpty()) {
            return;
        }

        int cantSel = 0;
        for (PendienteProduccionDetalle itemPendiente : itemsPendiente) {

            if (itemPendiente.isSeleccionado()) {

                cantSel++;
                ItemHorarioProduccion nItem = nuevoItemHorario(m);
                generarItemFromItemPendiente(m, itemPendiente, nItem);
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
     * @param movimiento Movimiento de produccción
     * @param itemsMovimiento items pendientes necesarios para generar el
     * movimiento
     * @throws ExcepcionGeneralSistema
     *
     */
    public void generarItemsProductoFromItemMovimiento(MovimientoProduccion movimiento, List<ItemProductoProduccion> itemsMovimiento) throws ExcepcionGeneralSistema, Exception {

        String sErrores = "";

        if (itemsMovimiento.isEmpty()) {
            return;
        }

        int cantSel = 0;
        for (ItemProductoProduccion item : itemsMovimiento) {

            sErrores += controlCantidadesItemsDetalleProducto(item);

            //Si tieen cantidad cero no genera el item
            if (item.getProduccion() != null && item.getProduccion().compareTo(BigDecimal.ZERO) > 0) {

                cantSel++;
                ItemProductoProduccion nItem = nuevoItemProducto(movimiento);
                generarItemFromItemMovimiento(movimiento, item, nItem);
            }
        }

        if (cantSel == 0) {
            sErrores += "No ha seleccionado ningún producto para el comprobante " + movimiento.getFormulario().getDescripcion() + "\n";
        }

        if (!sErrores.isEmpty()) {
            throw new ExcepcionGeneralSistema(sErrores);
        }
    }

    /**
     * Generamos los items componentes del movimientos en base a los items del
     * movmiento original
     *
     * @param movimiento Movimiento de produccción
     * @param itemsMovimiento items pendientes necesarios para generar el
     * movimiento
     * @throws ExcepcionGeneralSistema
     *
     */
    public void generarItemsComponenteFromItemMovimiento(MovimientoProduccion movimiento, List<ItemComponenteProduccion> itemsMovimiento) throws ExcepcionGeneralSistema, Exception {

        String sErrores = "";

        if (itemsMovimiento.isEmpty()) {
            return;
        }

        int cantSel = 0;
        for (ItemComponenteProduccion item : itemsMovimiento) {

            sErrores += controlCantidadesItemsDetalleComponente(item);

            //Si tiene cantidad cero no genera el item
            if (item.getProduccion() != null && item.getProduccion().compareTo(BigDecimal.ZERO) > 0) {

                cantSel++;
                ItemComponenteProduccion itemNuevo = nuevoItemComponente(movimiento);
                generarItemFromItemMovimiento(movimiento, item, itemNuevo);
            }
        }

        if (cantSel == 0) {
            sErrores += "No ha seleccionado ningún producto para el comprobante " + movimiento.getFormulario().getDescripcion() + "\n";
        }

        if (!sErrores.isEmpty()) {
            throw new ExcepcionGeneralSistema(sErrores);
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
    public void generarItemsProcesoFromItemMovimiento(MovimientoProduccion movimientoProduccion, List<ItemProcesoProduccion> itemsMovimiento) throws ExcepcionGeneralSistema {

        if (itemsMovimiento.isEmpty()) {
            return;
        }

        int cantSel = 0;
        for (ItemProcesoProduccion itemMovimientoProduccion : itemsMovimiento) {

            if (itemMovimientoProduccion.getProduccion() != null && itemMovimientoProduccion.getProduccion().compareTo(BigDecimal.ZERO) > 0) {

                cantSel++;
                ItemProcesoProduccion nItem = nuevoItemProceso(movimientoProduccion);
                generarItemFromItemMovimiento(movimientoProduccion, itemMovimientoProduccion, nItem);

            }
        }
        if (cantSel == 0) {
            throw new ExcepcionGeneralSistema("No ha seleccionado ningún producto");
        }
    }

    /**
     * Generamos los items horarios del movimientos en base a los items del
     * movmiento original
     *
     * @param movimientoProduccion Movimiento de produccción
     * @param itemsMovimiento items pendientes necesarios para generar el
     * movimiento
     * @throws ExcepcionGeneralSistema
     *
     */
    public void generarItemsHorarioFromItemMovimiento(MovimientoProduccion movimientoProduccion, List<ItemHorarioProduccion> itemsMovimiento) throws ExcepcionGeneralSistema {

        if (itemsMovimiento.isEmpty()) {
            return;
        }

        int cantSel = 0;
        for (ItemHorarioProduccion itemMovimientoProduccion : itemsMovimiento) {

            if (itemMovimientoProduccion.getProduccion() != null && itemMovimientoProduccion.getProduccion().compareTo(BigDecimal.ZERO) > 0) {

                cantSel++;
                ItemHorarioProduccion nItem = nuevoItemHorario(movimientoProduccion);
                generarItemFromItemMovimiento(movimientoProduccion, itemMovimientoProduccion, nItem);

            }
        }
        if (cantSel == 0) {
            throw new ExcepcionGeneralSistema("No ha seleccionado ningún producto");
        }
    }

    public void sincronizarCantidades(MovimientoProduccion m) {

        for (ItemProductoProduccion i : m.getItemsProducto()) {

            if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
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

            sincronizarCantidadesDetalleProducto(i);
        }

        for (ItemComponenteProduccion i : m.getItemsComponente()) {

            if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
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

            sincronizarCantidadesDetalleComponente(i);
        }
    }

    public void sincronizarCantidadesDetalleProducto(ItemProductoProduccion itemProducto) {

        //Si el detalle es nulo, lo creo
        if (itemProducto.getItemDetalle() == null) {
            itemProducto.setItemDetalle(new ArrayList<ItemDetalleItemProductoProduccion>());
        }

        //Si el detalle está vacío, agrego un item
        if (itemProducto.getItemDetalle().isEmpty()) {

            ItemDetalleItemProductoProduccion itemDetalle = nuevoItemDetalleItemProducto(itemProducto);
            itemProducto.getItemDetalle().add(itemDetalle);
        }

        //Si el detalle tiene 1 solo item, asigno la misma cantidad.
        if (itemProducto.getItemDetalle().size() == 1) {
            itemProducto.getItemDetalle().get(0).setCantidad(itemProducto.getCantidad());
        }
    }

    public void sincronizarCantidadesDetalleComponente(ItemComponenteProduccion itemComponente) {

        //Si el detalle es nulo, lo creo
        if (itemComponente.getItemDetalle() == null) {
            itemComponente.setItemDetalle(new ArrayList<ItemDetalleItemComponenteProduccion>());
        }

        //Si el detalle está vacío, agrego un item
        if (itemComponente.getItemDetalle().isEmpty()) {

            ItemDetalleItemComponenteProduccion itemDetalle = nuevoItemDetalleItemComponente(itemComponente);
            itemComponente.getItemDetalle().add(itemDetalle);
        }

        //Si el detalle tiene 1 solo item, asigno la misma cantidad.
        if (itemComponente.getItemDetalle().size() == 1) {
            itemComponente.getItemDetalle().get(0).setCantidad(itemComponente.getCantidad());
        }
    }

    public MovimientoProduccion getMovimiento(Integer id) {

        return produccionDAO.getMovimiento(id);
    }

    public MovimientoProduccion getMovimiento(Integer id, boolean calculaPendienteItem, boolean generaItems) throws ExcepcionGeneralSistema {

        if (id == null) {
            return null;
        }

        MovimientoProduccion m = produccionDAO.getMovimiento(id);

        if (m == null) {
            return null;
        }

        if (calculaPendienteItem) {
            calcularPendientes(m);
        }

        if (generaItems) {
            sincronizarCantidades(m);
            ponerItemsDetalleEnCero(m);
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

//                ItemProductoProduccion ip = getItemProducto(idPK);
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

    public void tomarNumeroFormulario(MovimientoProduccion m) throws ExcepcionGeneralSistema {

        //Si no actualiza proveedores ni stock, entonces es facturación
        m.setNumeroFormulario(formularioRN.tomarProximoNumero(m.getFormulario()));

    }

    public List<MovimientoProduccion> getListaByBusqueda(Map<String, String> filtro, boolean soloPendientes, int cantMax) {

        return produccionDAO.getListaByBusqueda(filtro, soloPendientes, cantMax);
    }

    public boolean sincronizarIdAplicacion(MovimientoProduccion m) {

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

        if (m.getItemsHorario() != null) {
            for (ItemHorarioProduccion ic : m.getItemsHorario()) {

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
            filtro.put("artcod = ", "'" + i.getProducto().getCodigo() + "'");

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

        for (ItemHorarioProduccion i : m.getItemsHorario()) {

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
//        stockRN.recalcularStock();
        //if(movimientoProduccion.getMovimientoStock()!=null){
        //    movimientoStockRN.eliminarMovimiento(movimientoProduccion.getMovimientoStock());
        //}        
    }

    //Cargamos y mostramos solo los procesos asociados al producto a producir seleccionado.
    public List<ItemProcesoProduccion> getItemProcesoByGrupo(MovimientoProduccion op, String grupo) {

        List<ItemProcesoProduccion> procesosSeleccionados = new ArrayList<ItemProcesoProduccion>();

        for (ItemProcesoProduccion iProceso : op.getItemsProceso()) {

            if (iProceso.getGrupo().equals(grupo)) {
                procesosSeleccionados.add(iProceso);
            }
        }
        return procesosSeleccionados;
    }

    //Cargamos y mostramos solo los productos asociados al producto a producir seleccionado.
    public List<ItemProductoProduccion> getItemProductoByGrupo(MovimientoProduccion op, String grupo) {

        List<ItemProductoProduccion> productoSeleccionados = new ArrayList<ItemProductoProduccion>();

        for (ItemProductoProduccion iProducto : op.getItemsProducto()) {

            if (iProducto.getGrupo().equals(grupo)) {
                productoSeleccionados.add(iProducto);
            }
        }
        return productoSeleccionados;
    }

    //Cargamos y mostramos solo los componentes asociados al producto a producir seleccionado.
    public List<ItemComponenteProduccion> getItemComponenteByGrupo(MovimientoProduccion op, String grupo) {

        List<ItemComponenteProduccion> componenteSeleccionados = new ArrayList<ItemComponenteProduccion>();

        for (ItemComponenteProduccion iComponente : op.getItemsComponente()) {

            if (iComponente.getGrupo().equals(grupo)) {
                componenteSeleccionados.add(iComponente);
            }
        }
        return componenteSeleccionados;
    }

    //Cargamos y mostramos solo los horario asociados al producto a producir seleccionado.
    public List<ItemHorarioProduccion> getItemHorarioByGrupo(MovimientoProduccion op, String grupo) {

        List<ItemHorarioProduccion> horarioSeleccionado = new ArrayList<ItemHorarioProduccion>();

        for (ItemHorarioProduccion iHorario : op.getItemsHorario()) {

            if (iHorario.getGrupo().equals(grupo)) {
                horarioSeleccionado.add(iHorario);
            }
        }
        return horarioSeleccionado;
    }

    public ItemProductoProduccion nuevoItemProducto(MovimientoProduccion m) {

        ItemProductoProduccion nItem = new ItemProductoProduccion();

        nItem.setNroitm(m.getItemsProducto().size() + 1);
        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(m);
        nItem.setMovimientoOriginal(m);

        return nItem;
    }

    public ItemComponenteProduccion nuevoItemComponente(MovimientoProduccion m) {

        ItemComponenteProduccion nItem = new ItemComponenteProduccion();

        nItem.setNroitm(m.getItemsComponente().size() + 1);
        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(m);
        nItem.setMovimientoOriginal(m);

        return nItem;

    }

    public ItemProcesoProduccion nuevoItemProceso(MovimientoProduccion m) {

        ItemProcesoProduccion nItem = new ItemProcesoProduccion();

        nItem.setNroitm(m.getItemsProceso().size() + 1);
        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(m);
        nItem.setMovimientoOriginal(m);

        return nItem;
    }

    public ItemHorarioProduccion nuevoItemHorario(MovimientoProduccion m) {

        ItemHorarioProduccion itemHorario = new ItemHorarioProduccion();

        Producto producto = null;
        try {
            producto = parametroProduccionRN.getParametro().getProductoHorario();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        if (producto == null) {
            return null;
        }

        itemHorario.setNroitm(m.getItemsProceso().size() + 1);
        itemHorario.setProducto(producto);
        itemHorario.setProductoOriginal(producto);
        itemHorario.setGrupo("-");
        itemHorario.setUnidadMedida(producto.getUnidadDeMedida());
        itemHorario.setActualizaStock("N");
        itemHorario.setMovimiento(m);
        itemHorario.setMovimientoAplicacion(m);
        itemHorario.setMovimientoOriginal(m);

        return itemHorario;
    }

    public ItemAplicacionProduccion nuevoItemAplicacion(MovimientoProduccion m, PendienteProduccionDetalle ip) {

        ItemAplicacionProduccion nItem = new ItemAplicacionProduccion();
        nItem.setIdItemAplicacion(ip.getIdIapl());
        nItem.setNroitm(m.getItemsAplicacion().size() + 1);

        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(ip.getMovimientoAplicacion());
        nItem.setMovimientoOriginal(ip.getMovimientoAplicacion());

        return nItem;
    }

    public ItemAplicacionProduccion nuevoItemAplicacion(MovimientoProduccion m, ItemMovimientoProduccion ip) {

        ItemAplicacionProduccion nItem = new ItemAplicacionProduccion();
        nItem.setIdItemAplicacion(ip.getId());
        nItem.setNroitm(m.getItemsAplicacion().size() + 1);

        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(ip.getMovimientoAplicacion());
        nItem.setMovimientoOriginal(ip.getMovimientoAplicacion());

        return nItem;
    }

    public ItemDetalleItemProductoProduccion nuevoItemDetalleItemProducto(ItemProductoProduccion itemProducto) {

        ItemDetalleItemProductoProduccion itemDetalle = new ItemDetalleItemProductoProduccion();

        itemDetalle.setAtributo1(itemProducto.getAtributo1());
        itemDetalle.setAtributo2(itemProducto.getAtributo2());
        itemDetalle.setAtributo3(itemProducto.getAtributo3());
        itemDetalle.setAtributo4(itemProducto.getAtributo4());
        itemDetalle.setAtributo5(itemProducto.getAtributo5());
        itemDetalle.setAtributo6(itemProducto.getAtributo6());
        itemDetalle.setAtributo7(itemProducto.getAtributo7());

        itemDetalle.setProducto(itemProducto.getProducto());
        itemDetalle.setProductoOriginal(itemProducto.getProductoOriginal());

        itemDetalle.setCantidad(BigDecimal.ZERO);
        itemDetalle.setUnidadMedida(itemProducto.getUnidadMedida());

        itemDetalle.setItemProducto(itemProducto);
        return itemDetalle;
    }

    public ItemDetalleItemProductoProduccion nuevoItemDetalleItemProductoFromItem(ItemProductoProduccion itemProducto, ItemDetalleItemProductoProduccion itemDetalleCopiar) {

        ItemDetalleItemProductoProduccion itemDetalle = new ItemDetalleItemProductoProduccion();

        itemDetalle.setAtributo1(itemDetalleCopiar.getAtributo1());
        itemDetalle.setAtributo2(itemDetalleCopiar.getAtributo2());
        itemDetalle.setAtributo3(itemDetalleCopiar.getAtributo3());
        itemDetalle.setAtributo4(itemDetalleCopiar.getAtributo4());
        itemDetalle.setAtributo5(itemDetalleCopiar.getAtributo5());
        itemDetalle.setAtributo6(itemDetalleCopiar.getAtributo6());
        itemDetalle.setAtributo7(itemDetalleCopiar.getAtributo7());

        itemDetalle.setProducto(itemDetalleCopiar.getProducto());
        itemDetalle.setProductoOriginal(itemDetalleCopiar.getProductoOriginal());

        itemDetalle.setCantidad(itemDetalleCopiar.getCantidad());
        itemDetalle.setUnidadMedida(itemDetalleCopiar.getUnidadMedida());

        itemDetalle.setItemProducto(itemProducto);
        return itemDetalle;
    }

    public ItemDetalleItemComponenteProduccion nuevoItemDetalleItemComponente(ItemComponenteProduccion itemComponente) {

        ItemDetalleItemComponenteProduccion itemDetalle = new ItemDetalleItemComponenteProduccion();

        itemDetalle.setAtributo1(itemComponente.getAtributo1());
        itemDetalle.setAtributo2(itemComponente.getAtributo2());
        itemDetalle.setAtributo3(itemComponente.getAtributo3());
        itemDetalle.setAtributo4(itemComponente.getAtributo4());
        itemDetalle.setAtributo5(itemComponente.getAtributo5());
        itemDetalle.setAtributo6(itemComponente.getAtributo6());
        itemDetalle.setAtributo7(itemComponente.getAtributo7());

        itemDetalle.setProducto(itemComponente.getProducto());
        itemDetalle.setProductoOriginal(itemComponente.getProductoOriginal());

        itemDetalle.setCantidad(itemComponente.getCantidad());
        itemDetalle.setUnidadMedida(itemComponente.getUnidadMedida());

        itemDetalle.setItemComponente(itemComponente);

        return itemDetalle;
    }

    public ItemDetalleItemComponenteProduccion nuevoItemDetalleItemComponenteFromItem(ItemComponenteProduccion itemComponente, ItemDetalleItemComponenteProduccion itemDetalleCopiar) {

        ItemDetalleItemComponenteProduccion itemDetalle = new ItemDetalleItemComponenteProduccion();

        itemDetalle.setAtributo1(itemDetalleCopiar.getAtributo1());
        itemDetalle.setAtributo2(itemDetalleCopiar.getAtributo2());
        itemDetalle.setAtributo3(itemDetalleCopiar.getAtributo3());
        itemDetalle.setAtributo4(itemDetalleCopiar.getAtributo4());
        itemDetalle.setAtributo5(itemDetalleCopiar.getAtributo5());
        itemDetalle.setAtributo6(itemDetalleCopiar.getAtributo6());
        itemDetalle.setAtributo7(itemDetalleCopiar.getAtributo7());

        itemDetalle.setProducto(itemDetalleCopiar.getProducto());
        itemDetalle.setProductoOriginal(itemDetalleCopiar.getProductoOriginal());

        itemDetalle.setCantidad(itemDetalleCopiar.getCantidad());
        itemDetalle.setUnidadMedida(itemDetalleCopiar.getUnidadMedida());

        itemDetalle.setItemComponente(itemComponente);

        return itemDetalle;
    }

    public void agregarItemDetalleProducto(ItemProductoProduccion nItem) {

        ItemDetalleItemProductoProduccion nItemD = nuevoItemDetalleItemProducto(nItem);
        nItemD.setCantidad(BigDecimal.ZERO);
        nItem.getItemDetalle().add(nItemD);

    }

    public void agregarItemDetalleComponente(ItemComponenteProduccion nItem) {

        ItemDetalleItemComponenteProduccion nItemD = nuevoItemDetalleItemComponente(nItem);
        nItemD.setCantidad(BigDecimal.ZERO);
        nItem.getItemDetalle().add(nItemD);
    }

    public void generarItemFromItemMovimiento(MovimientoProduccion movimiento, ItemMovimientoProduccion itemCopiar, ItemMovimientoProduccion itemNuevo) throws ExcepcionGeneralSistema {

        itemNuevo.setProducto(itemCopiar.getProducto());
        itemNuevo.setProductoOriginal(itemCopiar.getProducto());
        itemNuevo.setUnidadMedida(itemCopiar.getUnidadMedida());
        itemNuevo.setOperario(itemCopiar.getOperario());
        itemNuevo.setPrecio(itemCopiar.getPrecio());

        itemNuevo.setAtributo1(itemCopiar.getAtributo1());
        itemNuevo.setAtributo2(itemCopiar.getAtributo2());
        itemNuevo.setAtributo3(itemCopiar.getAtributo3());
        itemNuevo.setAtributo4(itemCopiar.getAtributo4());
        itemNuevo.setAtributo5(itemCopiar.getAtributo5());
        itemNuevo.setAtributo6(itemCopiar.getAtributo6());
        itemNuevo.setAtributo7(itemCopiar.getAtributo7());

        if (itemCopiar.getProduccion() != null && itemCopiar.getProduccion().compareTo(BigDecimal.ZERO) > 0) {
            itemNuevo.setCantidad(itemCopiar.getProduccion());
            itemNuevo.setCantidadOriginal(itemCopiar.getCantidad());
            itemNuevo.setCantidadStock(itemCopiar.getProduccion());
        } else {
            itemNuevo.setCantidad(BigDecimal.ZERO);
            itemNuevo.setCantidadOriginal(BigDecimal.ZERO);
            itemNuevo.setCantidadStock(BigDecimal.ZERO);
        }

        itemNuevo.setActualizaStock(itemCopiar.getActualizaStock());
        itemNuevo.setGrupo(itemCopiar.getGrupo());

        if (itemCopiar.getFormula()!= null) {
            itemNuevo.setFormula(itemCopiar.getFormula());
        }

        //Si tiene asignado la toma de numero de serie se lo asignamos
        //Ahora toma nro de serie de hora de ruta
        if (movimiento.getTipoMovimiento().equals(TipoMovimientoProduccion.PP)) {

            SimpleDateFormat sdfAnio = new SimpleDateFormat("yy");
            SimpleDateFormat sdfSemana = new SimpleDateFormat("ww");
            SimpleDateFormat sdfDiaSemana = new SimpleDateFormat("uu");

            String nroLote = String.valueOf(itemCopiar.getMovimiento().getNumeroFormulario()) + "-";
            nroLote = nroLote + sdfAnio.format(new Date());
            nroLote = nroLote + sdfSemana.format(new Date());
            nroLote = nroLote + sdfDiaSemana.format(new Date());

            itemNuevo.setAtributo3(nroLote);
        }

        itemNuevo.setTodoOk(true);

        if (itemNuevo instanceof ItemProductoProduccion) {

            movimiento.getItemsProducto().add((ItemProductoProduccion) itemNuevo);
        }

        if (itemNuevo instanceof ItemComponenteProduccion) {

            movimiento.getItemsComponente().add((ItemComponenteProduccion) itemNuevo);
        }

        if (itemNuevo instanceof ItemProcesoProduccion) {

            movimiento.getItemsProceso().add((ItemProcesoProduccion) itemNuevo);
        }

        //-----------------------------------------------------------
        //Verificamos si el circuito aplica a items pendietnes y si el item tiene movimiento a aplicar
        if (movimiento.getCircuito().getNoCancelaPendiente().equals("N") && itemCopiar.getMovimientoAplicacion() != null) {

            //Genera los items aplicación
            ItemAplicacionProduccion iApl = nuevoItemAplicacion(movimiento, itemCopiar);

            iApl.setProducto(itemCopiar.getProducto());
            iApl.setProductoOriginal(itemCopiar.getProducto());
            iApl.setUnidadMedida(itemCopiar.getUnidadMedida());
            iApl.setCantidad(itemCopiar.getProduccion().negate());
            iApl.setCantidadOriginal(itemCopiar.getProduccion().negate());
            iApl.setCantidadStock(itemCopiar.getProduccion());
            iApl.setActualizaStock(itemCopiar.getActualizaStock());
            iApl.setGrupo(itemCopiar.getGrupo());

            iApl.setOperario(itemCopiar.getOperario());
            iApl.setPrecio(itemCopiar.getPrecio());

            if (itemCopiar.getFormula()!= null) {
                iApl.setFormula(itemCopiar.getFormula());
            }

            iApl.setTodoOk(true);
            //Agregamos el item a la lista
            movimiento.getItemsAplicacion().add(iApl);
        }
    }

    public void generarItemFromItemPendiente(MovimientoProduccion movimiento, PendienteProduccionDetalle itemPendiente, ItemMovimientoProduccion itemNuevo) throws ExcepcionGeneralSistema {

        itemNuevo.setProducto(itemPendiente.getProducto());
        itemNuevo.setProductoOriginal(itemPendiente.getProducto());
        itemNuevo.setUnidadMedida(itemPendiente.getUnidadMedida());

        if (itemPendiente.getCantidad() != null && itemPendiente.getCantidad().compareTo(BigDecimal.ZERO) > 0) {
            itemNuevo.setCantidad(itemPendiente.getCantidad());
            itemNuevo.setCantidadOriginal(itemPendiente.getCantidad());
            itemNuevo.setCantidadStock(itemPendiente.getCantidad());
        } else {
            itemNuevo.setCantidad(itemPendiente.getPendiente());
            itemNuevo.setCantidadOriginal(itemPendiente.getPendiente());
            itemNuevo.setCantidadStock(itemPendiente.getPendiente());
        }

        itemNuevo.setActualizaStock(itemPendiente.getStocks());
        itemNuevo.setGrupo(itemPendiente.getGrupo());
        itemNuevo.setOperario(itemPendiente.getOperario());

        if (itemPendiente.getFormul() != null && !itemPendiente.getFormul().isEmpty()) {
            ComposicionFormula composicionFormula = composicionFormulaRN.getComprosicionFormula(itemPendiente.getArtcod(), itemPendiente.getFormul());
//            itemNuevo.setFormula(composicionFormula);
        }

        itemNuevo.setTodoOk(true);

        if (itemNuevo instanceof ItemProductoProduccion) {

//            if (movimiento.getCircuito().getActualizaStock().equals("S")) {
//                generarItemDetalleItemProducto((ItemProductoProduccion) itemNuevo, null);
//            }
            movimiento.getItemsProducto().add((ItemProductoProduccion) itemNuevo);
        }

        if (itemNuevo instanceof ItemComponenteProduccion) {

//            if (movimiento.getCircuito().getActualizaStock().equals("S")) {
//                generarItemDetalleItemComponente((ItemComponenteProduccion) itemNuevo, null);
//            }
            movimiento.getItemsComponente().add((ItemComponenteProduccion) itemNuevo);
        }

        if (itemNuevo instanceof ItemProcesoProduccion) {

            movimiento.getItemsProceso().add((ItemProcesoProduccion) itemNuevo);
        }

        if (itemNuevo instanceof ItemHorarioProduccion) {

            movimiento.getItemsHorario().add((ItemHorarioProduccion) itemNuevo);
        }

        //-----------------------------------------------------------
        //Verificamos si el circuito aplica a items pendietnes
        if (movimiento.getCircuito().getNoCancelaPendiente().equals("N")) {

            //Genera los items aplicación
            ItemAplicacionProduccion iApl = nuevoItemAplicacion(movimiento, itemPendiente);

            iApl.setProducto(itemPendiente.getProducto());
            iApl.setProductoOriginal(itemPendiente.getProducto());
            iApl.setUnidadMedida(itemPendiente.getUnidadMedida());
            iApl.setCantidad(itemPendiente.getPendiente().negate());
            iApl.setCantidadOriginal(itemPendiente.getPendiente().negate());
            iApl.setCantidadStock(itemPendiente.getPendiente());
            iApl.setActualizaStock(itemPendiente.getStocks());
            iApl.setGrupo(itemPendiente.getGrupo());
            iApl.setOperario(itemPendiente.getOperario());

            if (itemPendiente.getFormul() != null && !itemPendiente.getFormul().isEmpty()) {
                ComposicionFormula composicionFormula = composicionFormulaRN.getComprosicionFormula(itemPendiente.getArtcod(), itemPendiente.getFormul());
//                iApl.setFormula(composicionFormula);
            }

            iApl.setTodoOk(true);
            //Agregamos el item a la lista
            movimiento.getItemsAplicacion().add(iApl);
        }
    }

//    public void generarItemDetalleItemProductosss(ItemProductoProduccion item, ItemProductoProduccion itemCopiar) throws ExcepcionGeneralSistema {
//
//        if (item.getItemDetalle() == null) {
//            item.setItemDetalle(new ArrayList<ItemDetalleItemProductoProduccion>());
//        }
//
//        if (itemCopiar == null || itemCopiar.getItemDetalle() == null || itemCopiar.getItemDetalle().isEmpty()) {
//
//            ItemDetalleItemProductoProduccion itemDetalle = nuevoItemDetalleItemProducto(item);
//            item.getItemDetalle().add(itemDetalle);
//
//        } else {
//
//            for (ItemDetalleItemProductoProduccion itemDetalleCopiar : itemCopiar.getItemDetalle()) {
//
//                ItemDetalleItemProductoProduccion itemDetalle = nuevoItemDetalleItemProductoFromItem(item, itemDetalleCopiar);
//                item.getItemDetalle().add(itemDetalle);
//            }
//        }
//    }
//
//    public void generarItemDetalleItemComponentess(ItemComponenteProduccion item, ItemComponenteProduccion itemCopiar) throws ExcepcionGeneralSistema {
//
//        if (item.getItemDetalle() == null) {
//            item.setItemDetalle(new ArrayList<ItemDetalleItemComponenteProduccion>());
//        }
//
//        if (itemCopiar == null || itemCopiar.getItemDetalle() == null || itemCopiar.getItemDetalle().isEmpty()) {
//
//            ItemDetalleItemComponenteProduccion itemDetalle = nuevoItemDetalleItemComponente(item);
//            item.getItemDetalle().add(itemDetalle);
//
//        } else {
//
//            for (ItemDetalleItemComponenteProduccion itemDetalleCopiar : itemCopiar.getItemDetalle()) {
//
//                ItemDetalleItemComponenteProduccion itemDetalle = nuevoItemDetalleItemComponenteFromItem(item, itemDetalleCopiar);
//                item.getItemDetalle().add(itemDetalle);
//            }
//        }
//    }
    public boolean eliminarItemDetalleProducto(ItemProductoProduccion ip, ItemDetalleItemMovimientoProduccion nItem) throws ExcepcionGeneralSistema {

        boolean fItemBorrado = false;
        int i = 0;
        int indiceItemProducto = -1;

        //Verificamos que siempre quede 1 items detalle
        if (ip.getItemDetalle().size() == 1) {

            throw new ExcepcionGeneralSistema("No es posible eliminar item de apertura, la cantidad mínima es un item");
        }

        //Buscamos el indice del item a borrar
        for (ItemDetalleItemMovimientoProduccion id : ip.getItemDetalle()) {

            if (id.getId() == null) {
                if (id.getNroitm() == nItem.getNroitm()) {
                    indiceItemProducto = i;
                }
            } else if (id.getId().equals(nItem.getId())) {
                indiceItemProducto = i;
            }
            i++;
        }

        //Borramos los items productos
        if (indiceItemProducto >= 0) {
            ItemDetalleItemMovimientoProduccion remove = ip.getItemDetalle().remove(indiceItemProducto);

            if (remove != null) {
                fItemBorrado = true;
            }
        }
        return fItemBorrado;
    }

    public boolean eliminarItemDetalleComponente(ItemComponenteProduccion ip, ItemDetalleItemMovimientoProduccion nItem) throws ExcepcionGeneralSistema {

        boolean fItemBorrado = false;
        int i = 0;
        int indiceItemProducto = -1;

        //Verificamos que siempre quede 1 items detalle
        if (ip.getItemDetalle().size() == 1) {

            throw new ExcepcionGeneralSistema("No es posible eliminar item de apertura, la cantidad mínima es un item");
        }

        //Buscamos el indice del item a borrar
        for (ItemDetalleItemMovimientoProduccion id : ip.getItemDetalle()) {

            if (id.getId() == null) {
                if (id.getNroitm() == nItem.getNroitm()) {
                    indiceItemProducto = i;
                }
            } else if (id.getId().equals(nItem.getId())) {
                indiceItemProducto = i;
            }
            i++;
        }

        //Borramos los items productos
        if (indiceItemProducto >= 0) {
            ItemDetalleItemMovimientoProduccion remove = ip.getItemDetalle().remove(indiceItemProducto);

            if (remove != null) {
                fItemBorrado = true;
            }
        }
        return fItemBorrado;
    }

    public ItemProductoProduccion getItemProducto(Integer id) {
        return produccionDAO.getItemProducto(id);
    }

    public ItemAplicacionProduccion getItemAplicacion(Integer id) {
        return produccionDAO.getItemProductoAplicacion(id);
    }

    public ItemComponenteProduccion getItemComponente(Integer id) {
        return produccionDAO.getItemComponente(id);
    }

    public String controlItemsDetalleProducto(ItemProductoProduccion item) throws Exception {

        String sErrores = "";

        ParametroStock parametroStock = parametroStockRN.getParametro();
        //variable temporal para comparar la suma de los items
        BigDecimal cantItems = BigDecimal.ZERO;

        for (ItemDetalleItemProductoProduccion itemDetalle : item.getItemDetalle()) {

            //Acumulamos las cantidades
            cantItems = cantItems.add(itemDetalle.getCantidad().setScale(2, BigDecimal.ROUND_HALF_DOWN));

            if (itemDetalle.getCantidad().equals(BigDecimal.ZERO)) {
                item.setConError(true);
                sErrores += "- No puede tener un item de apertura con cantidad cero en " + item.getProducto().getDescripcion() + "\n";
            }

            //Si el circuito define que controla atributos los validamos
            if (item.getMovimiento().getCircuito().getAdministraAtributo1().equals("S")) {
                if (itemDetalle.getAtributo1() == null || itemDetalle.getAtributo1().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 1 (" + parametroStock.getDescripcionAtributo1() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }

            if (item.getMovimiento().getCircuito().getAdministraAtributo2().equals("S")) {
                if (itemDetalle.getAtributo2() == null || itemDetalle.getAtributo2().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 2 (" + parametroStock.getDescripcionAtributo2() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }

            if (item.getMovimiento().getCircuito().getAdministraAtributo3().equals("S")) {
                if (itemDetalle.getAtributo3() == null || itemDetalle.getAtributo3().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 3 (" + parametroStock.getDescripcionAtributo3() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }

            if (item.getMovimiento().getCircuito().getAdministraAtributo4().equals("S")) {
                if (itemDetalle.getAtributo4() == null || itemDetalle.getAtributo4().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 4 (" + parametroStock.getDescripcionAtributo4() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }
        }

        if (!item.getCantidad().setScale(2, BigDecimal.ROUND_FLOOR).equals(cantItems.setScale(2, BigDecimal.ROUND_FLOOR))) {

            item.setConError(true);
            sErrores += "- No coincide la cantidad (" + item.getCantidad() + ") de " + item.getProducto().getDescripcion()
                    + " con la sumatoria en de los items de apertura (" + cantItems + ")\n";
        }

        return sErrores;

    }

    /**
     * Se controla que la cantidad ingresada en el campo producción y la
     * cantidad de los items de apertura coincidan.
     *
     * @param itemOrden
     * @return
     * @throws Exception
     */
    public String controlCantidadesItemsDetalleProducto(ItemProductoProduccion itemOrden) throws Exception {

        String sErrores = "";

        ParametroStock parametroStock = parametroStockRN.getParametro();
        //variable temporal para comparar la suma de los items
        BigDecimal cantItems = BigDecimal.ZERO;

        for (ItemDetalleItemProductoProduccion itemDetalle : itemOrden.getItemDetalle()) {

            //Acumulamos las cantidades
            cantItems = cantItems.add(itemDetalle.getCantidad().setScale(2, BigDecimal.ROUND_HALF_DOWN));

        }

        if (!itemOrden.getProduccion().setScale(2, BigDecimal.ROUND_FLOOR).equals(cantItems.setScale(2, BigDecimal.ROUND_FLOOR))) {

            itemOrden.setConError(true);
            sErrores += "- No coincide la cantidad (" + itemOrden.getProduccion() + ") de " + itemOrden.getProducto().getDescripcion()
                    + " con la sumatoria en de los items de apertura (" + cantItems + ")\n";
        }

        return sErrores;

    }

    public String controlItemsDetalleComponente(ItemComponenteProduccion item) throws Exception {

        String sErrores = "";

        ParametroStock parametroStock = parametroStockRN.getParametro();
        //variable temporal para comparar la suma de los items
        BigDecimal cantItems = BigDecimal.ZERO;

        for (ItemDetalleItemComponenteProduccion itemDetalle : item.getItemDetalle()) {

            //Acumulamos las cantidades
            cantItems = cantItems.add(itemDetalle.getCantidad().setScale(2, BigDecimal.ROUND_HALF_DOWN));

            if (itemDetalle.getCantidad().equals(BigDecimal.ZERO)) {
                item.setConError(true);
                sErrores += "- No puede tener un item de apertura con cantidad cero en " + item.getProducto().getDescripcion() + "\n";
            }

            //Si el circuito define que controla atributos los validamos
            if (item.getMovimiento().getCircuito().getAdministraAtributo1().equals("S")) {
                if (itemDetalle.getAtributo1() == null || itemDetalle.getAtributo1().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 1 (" + parametroStock.getDescripcionAtributo1() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }

            if (item.getMovimiento().getCircuito().getAdministraAtributo2().equals("S")) {
                if (itemDetalle.getAtributo2() == null || itemDetalle.getAtributo2().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 2 (" + parametroStock.getDescripcionAtributo2() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }

            if (item.getMovimiento().getCircuito().getAdministraAtributo3().equals("S")) {
                if (itemDetalle.getAtributo3() == null || itemDetalle.getAtributo3().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 3 (" + parametroStock.getDescripcionAtributo3() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }

            if (item.getMovimiento().getCircuito().getAdministraAtributo4().equals("S")) {
                if (itemDetalle.getAtributo4() == null || itemDetalle.getAtributo4().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 4 (" + parametroStock.getDescripcionAtributo4() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }
        }

        if (!item.getCantidad().setScale(2, BigDecimal.ROUND_FLOOR).equals(cantItems.setScale(2, BigDecimal.ROUND_FLOOR))) {

            item.setConError(true);
            sErrores += "- No coincide la cantidad (" + item.getCantidad() + ") de " + item.getProducto().getDescripcion()
                    + " con la sumatoria en de los items de apertura (" + cantItems + ")\n";
        }

        return sErrores;
    }

    /**
     * Se controla que la cantidad ingresada en el campo producción y la
     * cantidad de los items de apertura coincidan.
     *
     * @param itemOrden
     * @return
     * @throws Exception
     */
    public String controlCantidadesItemsDetalleComponente(ItemComponenteProduccion itemOrden) throws Exception {

        String sErrores = "";

        ParametroStock parametroStock = parametroStockRN.getParametro();
        //variable temporal para comparar la suma de los items
        BigDecimal cantItems = BigDecimal.ZERO;

        for (ItemDetalleItemComponenteProduccion itemDetalle : itemOrden.getItemDetalle()) {

            //Acumulamos las cantidades
            cantItems = cantItems.add(itemDetalle.getCantidad().setScale(2, BigDecimal.ROUND_HALF_DOWN));

        }

        if (!itemOrden.getProduccion().setScale(2, BigDecimal.ROUND_FLOOR).equals(cantItems.setScale(2, BigDecimal.ROUND_FLOOR))) {

            itemOrden.setConError(true);
            sErrores += "- No coincide la cantidad (" + itemOrden.getProduccion() + ") de " + itemOrden.getProducto().getDescripcion()
                    + " con la sumatoria en de los items de apertura (" + cantItems + ")\n";
        }

        return sErrores;

    }

    /**
     * Generar los items detalle con los números de atributos para el stock
     * Genera un item detalle para todos los items productos, componentes
     *
     * @param movimientoProduccion
     * @throws ExcepcionGeneralSistema
     */
//    public void generarItemsDetallesVacios(MovimientoProduccion movimientoProduccion) throws ExcepcionGeneralSistema {
//
////        if (movimientoProduccion.getItemsProducto() != null) {
////
////            for (ItemProductoProduccion itemProducto : movimientoProduccion.getItemsProducto()) {
////
////                //Si el detalle está vacio lo llenamos, de lo contrario ya viene cargado desde la pagina
////                if (itemProducto.getItemDetalle().isEmpty()) {
////                    generarItemDetalleItemProducto(itemProducto, null);
////                }
////            }
////        }
////
////        if (movimientoProduccion.getItemsComponente() != null) {
////
////            for (ItemComponenteProduccion itemComponente : movimientoProduccion.getItemsComponente()) {
////
////                //Si el detalle está vacio lo llenamos, de lo contrario ya viene cargado desde la pagina
////                if (itemComponente.getItemDetalle().isEmpty()) {
////                    generarItemDetalleItemComponente(itemComponente, null);
////                }
////            }
////        }
//    }
    public void ponerItemsDetalleEnCero(MovimientoProduccion movimiento) {

        if (movimiento.getItemsProducto() != null) {

            for (ItemProductoProduccion itemProducto : movimiento.getItemsProducto()) {

                if (itemProducto.getItemDetalle() != null) {

                    for (ItemDetalleItemProductoProduccion item : itemProducto.getItemDetalle()) {
                        item.setCantidad(BigDecimal.ZERO);
                    }
                }
            }
        }

        if (movimiento.getItemsComponente() != null) {

            for (ItemComponenteProduccion itemComponente : movimiento.getItemsComponente()) {

                if (itemComponente.getItemDetalle() != null) {

                    for (ItemDetalleItemComponenteProduccion item : itemComponente.getItemDetalle()) {
                        item.setCantidad(BigDecimal.ZERO);
                    }
                }
            }
        }
    }

    public boolean tengoItemsConCantidad(Object itemsPendientes) {

        if (itemsPendientes == null || ((List<ItemMovimientoProduccion>) itemsPendientes).isEmpty()) {
            return false;
        }

        for (ItemMovimientoProduccion i : (List<ItemMovimientoProduccion>) itemsPendientes) {

            if (i.getProduccion() != null && i.getProduccion().compareTo(BigDecimal.ZERO) > 0) {
                return true;
            }

            if (i instanceof ItemProductoProduccion) {

                for (ItemDetalleItemProductoProduccion d : ((ItemProductoProduccion) i).getItemDetalle()) {

                    if (d.getCantidad().compareTo(BigDecimal.ZERO) > 0) {
                        return true;
                    }
                }
            }

            if (i instanceof ItemComponenteProduccion) {

                for (ItemDetalleItemComponenteProduccion d : ((ItemComponenteProduccion) i).getItemDetalle()) {

                    if (d.getCantidad().compareTo(BigDecimal.ZERO) > 0) {
                        return true;
                    }
                }
            }

        }

        return false;
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

    /**
     * Eliminar un item componente de un movimiento
     *
     * @param mov movimiento del cual se eliminará el item
     * @param nItem item a eliminar
     */
    public boolean eliminarItemComponente(MovimientoProduccion mov, ItemComponenteProduccion nItem) {

        //Buscar el metodo eliminar items de facturación
        return false;
    }

    /**
     * Eliminar un item proceso de un movimiento
     *
     * @param mov movimiento del cual se eliminará el item
     * @param nItem item a eliminar
     */
    public boolean eliminarItemProceso(MovimientoProduccion mov, ItemProcesoProduccion nItem) {

        //Buscar el metodo eliminar items de facturación
        return false;
    }

    /**
     * Eliminar un item horario de un movimiento
     *
     * @param mov movimiento del cual se eliminará el item
     * @param nItem item a eliminar
     */
    public boolean eliminarItemHorario(MovimientoProduccion mov, ItemHorarioProduccion nItem) {

        //Buscar el metodo eliminar items de facturación
        return false;
    }

    public void asignarProducto(ItemProductoProduccion itemProducto, Producto producto) throws ExcepcionGeneralSistema {

        if (itemProducto.getMovimiento().getMonedaSecundaria() == null) {
            JsfUtil.addWarningMessage("El comprobante no tiene una moneda secundaria asignada");
            return;
        }

        itemProducto.setProducto(producto);
        itemProducto.setProductoOriginal(producto);
        itemProducto.setGrupo(producto.getCodigo());
        itemProducto.setUnidadMedida(producto.getUnidadDeMedida());
        itemProducto.setActualizaStock(producto.getGestionaStock());

        //Si es comprobante de Orden de producción, agregamos los componentes
        if (itemProducto.getMovimiento().getCircuito().getTipoMovimiento() == TipoMovimientoProduccion.OP) {

            agregarComponentesYProcesos(itemProducto.getMovimiento(), itemProducto);
        }
    }

    public void asignarComponente(ItemComponenteProduccion itemComponente, Producto producto) throws ExcepcionGeneralSistema {

        if (itemComponente.getMovimiento().getMonedaSecundaria() == null) {
            JsfUtil.addWarningMessage("El comprobante no tiene una moneda secundaria asignada");
            return;
        }

        itemComponente.setProducto(producto);
        itemComponente.setProductoOriginal(producto);
        itemComponente.setGrupo(producto.getCodigo());
        itemComponente.setUnidadMedida(producto.getUnidadDeMedida());
        itemComponente.setActualizaStock(producto.getGestionaStock());

    }

    public void asignarProductoItemDetalleProducto(ItemProductoProduccion itemProducto) {

        if (itemProducto.getMovimiento().getCircuito().getActualizaProduccion().equals("S")) {
            return;
        }

        if (itemProducto.getItemDetalle() != null) {
            itemProducto.getItemDetalle().clear();
        }

        ItemDetalleItemMovimientoProduccion itemDetalle = nuevoItemDetalleItemProducto(itemProducto);
        itemDetalle.setAtributo1(itemProducto.getAtributo1());
        itemDetalle.setAtributo2(itemProducto.getAtributo2());
        itemDetalle.setAtributo3(itemProducto.getAtributo3());
        itemDetalle.setAtributo4(itemProducto.getAtributo4());
        itemDetalle.setAtributo5(itemProducto.getAtributo5());
        itemDetalle.setAtributo6(itemProducto.getAtributo6());
        itemDetalle.setAtributo7(itemProducto.getAtributo7());
        itemDetalle.setCantidad(itemProducto.getCantidad());

        if (itemProducto.getCantidad() != null) {
            itemDetalle.setCantidad(itemProducto.getCantidad());
        }
    }

    public void asignarProductoItemDetalleComponente(ItemComponenteProduccion itemComponente) {

        if (itemComponente.getMovimiento().getCircuito().getActualizaProduccion().equals("S")) {
            return;
        }

        if (itemComponente.getItemDetalle() != null) {
            itemComponente.getItemDetalle().clear();
        }

        ItemDetalleItemComponenteProduccion itemDetalle = nuevoItemDetalleItemComponente(itemComponente);
        itemDetalle.setAtributo1(itemComponente.getAtributo1());
        itemDetalle.setAtributo2(itemComponente.getAtributo2());
        itemDetalle.setAtributo3(itemComponente.getAtributo3());
        itemDetalle.setAtributo4(itemComponente.getAtributo4());
        itemDetalle.setAtributo5(itemComponente.getAtributo5());
        itemDetalle.setAtributo6(itemComponente.getAtributo6());
        itemDetalle.setAtributo7(itemComponente.getAtributo7());
        itemDetalle.setCantidad(itemComponente.getCantidad());

        if (itemComponente.getCantidad() != null) {
            itemDetalle.setCantidad(itemComponente.getCantidad());
        }
    }

}
