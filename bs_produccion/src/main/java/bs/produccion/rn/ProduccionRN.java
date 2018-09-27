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
import bs.produccion.modelo.ItemComponenteProduccion;
import bs.produccion.modelo.ItemDetalleComponenteProduccion;
import bs.produccion.modelo.ItemDetalleMovimientoProduccion;
import bs.produccion.modelo.ItemDetalleProductoProduccion;
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
import bs.stock.modelo.Formula;
import bs.stock.modelo.ItemComposicionFormula;
import bs.stock.modelo.ItemComposicionFormulaComponente;
import bs.stock.modelo.ItemComposicionFormulaProceso;
import bs.stock.modelo.MovimientoStock;
import bs.stock.modelo.ParametroStock;
import bs.stock.modelo.Producto;
import bs.stock.modelo.Sector;
import bs.stock.rn.ComposicionFormulaRN;
import bs.stock.rn.MovimientoStockRN;
import bs.stock.rn.ParametroStockRN;
import bs.stock.rn.ProductoRN;
import bs.stock.rn.StockRN;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    protected ParametroProduccionRN parametroProduccionRN;
    @EJB
    private ProduccionDAO produccionDAO;
    @EJB
    private ParametroStockRN parametroStockRN;
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
    @EJB
    private StockRN stockRN;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public synchronized MovimientoProduccion guardar(MovimientoProduccion movimiento) throws Exception {

        sincronizarCantidades(movimiento);
        reorganizarNroItem(movimiento);
        generarDatosAdicionales(movimiento);
        controlComprobante(movimiento);
        if (movimiento.getId() == null) {

            generarMovimientosAdicionales(movimiento);
            if (!movimiento.isNoSincronizaNumeroFormulario() && movimiento.getNumeroFormulario() > 0) {
                tomarNumeroFormulario(movimiento);
            }

            produccionDAO.crear(movimiento);
        } else {
            movimiento = produccionDAO.editar(movimiento);
        }
        actualizarCantidadesPendientes(movimiento);
        return movimiento;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public MovimientoProduccion editar(MovimientoProduccion movimiento) {

        return produccionDAO.editar(movimiento);

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public synchronized MovimientoProduccion guardarSincronizacion(MovimientoProduccion movimiento) throws Exception {

        if (movimiento.getId() == null) {

            reorganizarNroItem(movimiento);
            generarDatosAdicionales(movimiento);
            controlComprobante(movimiento);

            generarMovimientosAdicionales(movimiento);

            if (!movimiento.isNoSincronizaNumeroFormulario() && movimiento.getNumeroFormulario() > 0) {
                tomarNumeroFormulario(movimiento);
            }
            produccionDAO.crear(movimiento);
        } else {
            movimiento = produccionDAO.editar(movimiento);
        }

        actualizarCantidadesPendientes(movimiento);
        return movimiento;
    }

    public void guardarComprobanteStock(MovimientoStock m) throws Exception {

        if (m != null) {
            movimientoStockRN.guardar(m);
        }
    }

    public void generarMovimientosAdicionales(MovimientoProduccion m) throws ExcepcionGeneralSistema, Exception {

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
        }else{
                m.setDeposito(comprobante.getDeposito());
                m.setDepositoTransferencia(comprobante.getDepositoTransferencia());
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

        asignarFormulario(m);

        return m;
    }

    public MovimientoProduccion nuevoMovimientoFromItems(CircuitoProduccion circuito, Sucursal sucursal, Sucursal sucursalStock,
            Object itemsMovimiento) throws ExcepcionGeneralSistema, Exception {

        MovimientoProduccion movimiento = nuevoMovimiento(circuito, sucursal, sucursalStock);

        if (movimiento.getTipoMovimiento().equals(TipoMovimientoProduccion.PP)) {
            generarItemsProductoFromItemMovimiento(movimiento, (List<ItemProductoProduccion>) itemsMovimiento);
        }

        if (movimiento.getTipoMovimiento().equals(TipoMovimientoProduccion.VC)) {
            generarItemsComponenteFromItemMovimiento(movimiento, (List<ItemComponenteProduccion>) itemsMovimiento);
        }

        if (movimiento.getTipoMovimiento().equals(TipoMovimientoProduccion.PR)) {
            generarItemsProcesoFromItemMovimiento(movimiento, (List<ItemMovimientoProduccion>) itemsMovimiento);
        }

        asignarFormulario(movimiento);

        return movimiento;
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

        if (circuito.getCircuitoComienzo().equals(circuito.getCircuitoAplicacion())) {
//            m.getItemsProducto().add((ItemProductoProduccion) nuevoItemProducto(m));
        }

        return m;
    }

    public ItemProductoProduccion nuevoItemProducto(MovimientoProduccion m) {

        ItemProductoProduccion nItem = new ItemProductoProduccion();

        nItem.setNroitm(m.getItemsProducto().size() + 1);
        nItem.setMovimiento(m);
        nItem.setMovimientoOriginal(m);
        nItem.setDeposito(m.getDeposito());   
        nItem.setHoraInicio(m.getHoraInicio());        

        return nItem;
    }

    public ItemComponenteProduccion nuevoItemComponente(MovimientoProduccion m) {

        ItemComponenteProduccion nItem = new ItemComponenteProduccion();

        nItem.setNroitm(m.getItemsComponente().size() + 1);
        nItem.setMovimiento(m);
        nItem.setMovimientoOriginal(m);
        nItem.setDeposito(m.getDeposito());        

        return nItem;

    }

    public ItemProcesoProduccion nuevoItemProceso(MovimientoProduccion m) {

        ItemProcesoProduccion nItem = new ItemProcesoProduccion();

        nItem.setNroitm(m.getItemsProceso().size() + 1);
        nItem.setMovimiento(m);
        nItem.setMovimientoOriginal(m);        

        return nItem;
    }

    public ItemHorarioProduccion nuevoItemHorario(MovimientoProduccion m) {

        try {
            ItemHorarioProduccion nItem = new ItemHorarioProduccion();

            nItem.setNroitm(m.getItemsProceso().size() + 1);
            nItem.setMovimiento(m);
            nItem.setMovimientoOriginal(m);

            Producto producto = parametroProduccionRN.getParametro().getProductoHorario();
            nItem.setProducto(producto);
            nItem.setProductoOriginal(producto);
            nItem.setUnidadMedida(producto.getUnidadDeMedida());
            nItem.setActualizaStock(producto.getGestionaStock());
            return nItem;

        } catch (Exception ex) {
            Logger.getLogger(ProduccionRN.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public ItemDetalleProductoProduccion nuevoItemDetalleItemProducto(ItemProductoProduccion itemProducto) {

        ItemDetalleProductoProduccion itemDetalle = new ItemDetalleProductoProduccion();

        itemDetalle.setAtributo1(itemProducto.getAtributo1());
        itemDetalle.setAtributo2(itemProducto.getAtributo2());
        itemDetalle.setAtributo3(itemProducto.getAtributo3());
        itemDetalle.setAtributo4(itemProducto.getAtributo4());
        itemDetalle.setAtributo5(itemProducto.getAtributo5());
        itemDetalle.setAtributo6(itemProducto.getAtributo6());
        itemDetalle.setAtributo7(itemProducto.getAtributo7());

        itemDetalle.setProducto(itemProducto.getProducto());
        itemDetalle.setCantidad(BigDecimal.ZERO);
        itemDetalle.setUnidadMedida(itemProducto.getUnidadMedida());

        itemDetalle.setItemProducto(itemProducto);

        sincronizarCantidadesItemDetalleProducto(itemProducto);

        return itemDetalle;
    }

    public ItemDetalleProductoProduccion nuevoItemDetalleItemProductoFromItem(ItemProductoProduccion itemProducto, ItemDetalleProductoProduccion itemDetalleCopiar) {

        ItemDetalleProductoProduccion itemDetalle = new ItemDetalleProductoProduccion();

        itemDetalle.setAtributo1(itemDetalleCopiar.getAtributo1());
        itemDetalle.setAtributo2(itemDetalleCopiar.getAtributo2());
        itemDetalle.setAtributo3(itemDetalleCopiar.getAtributo3());
        itemDetalle.setAtributo4(itemDetalleCopiar.getAtributo4());
        itemDetalle.setAtributo5(itemDetalleCopiar.getAtributo5());
        itemDetalle.setAtributo6(itemDetalleCopiar.getAtributo6());
        itemDetalle.setAtributo7(itemDetalleCopiar.getAtributo7());

        itemDetalle.setProducto(itemDetalleCopiar.getProducto());
        itemDetalle.setCantidad(itemDetalleCopiar.getCantidad());
        itemDetalle.setUnidadMedida(itemDetalleCopiar.getUnidadMedida());

        itemDetalle.setItemProducto(itemProducto);

        sincronizarCantidadesItemDetalleProducto(itemProducto);

        return itemDetalle;
    }

    public ItemDetalleComponenteProduccion nuevoItemDetalleItemComponente(ItemComponenteProduccion itemComponente) {

        ItemDetalleComponenteProduccion itemDetalle = new ItemDetalleComponenteProduccion();

        itemDetalle.setAtributo1(itemComponente.getAtributo1());
        itemDetalle.setAtributo2(itemComponente.getAtributo2());
        itemDetalle.setAtributo3(itemComponente.getAtributo3());
        itemDetalle.setAtributo4(itemComponente.getAtributo4());
        itemDetalle.setAtributo5(itemComponente.getAtributo5());
        itemDetalle.setAtributo6(itemComponente.getAtributo6());
        itemDetalle.setAtributo7(itemComponente.getAtributo7());

        itemDetalle.setProducto(itemComponente.getProducto());
        itemDetalle.setCantidad(BigDecimal.ZERO);
        itemDetalle.setUnidadMedida(itemComponente.getUnidadMedida());

        itemDetalle.setItemComponente(itemComponente);

        sincronizarCantidadesItemDetalleComponente(itemComponente);

        return itemDetalle;
    }

    public ItemDetalleComponenteProduccion nuevoItemDetalleItemComponenteFromItem(ItemComponenteProduccion itemComponente, ItemDetalleComponenteProduccion itemDetalleCopiar) {

        ItemDetalleComponenteProduccion itemDetalle = new ItemDetalleComponenteProduccion();

        itemDetalle.setAtributo1(itemDetalleCopiar.getAtributo1());
        itemDetalle.setAtributo2(itemDetalleCopiar.getAtributo2());
        itemDetalle.setAtributo3(itemDetalleCopiar.getAtributo3());
        itemDetalle.setAtributo4(itemDetalleCopiar.getAtributo4());
        itemDetalle.setAtributo5(itemDetalleCopiar.getAtributo5());
        itemDetalle.setAtributo6(itemDetalleCopiar.getAtributo6());
        itemDetalle.setAtributo7(itemDetalleCopiar.getAtributo7());

        itemDetalle.setProducto(itemDetalleCopiar.getProducto());
        itemDetalle.setCantidad(itemDetalleCopiar.getCantidad());
        itemDetalle.setUnidadMedida(itemDetalleCopiar.getUnidadMedida());

        itemDetalle.setItemComponente(itemComponente);

        sincronizarCantidadesItemDetalleComponente(itemComponente);

        return itemDetalle;
    }

    //-------------------------------------------------------------------------------------------
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
            itemNuevo.setCantidadPendiente(itemCopiar.getCantidad());
            itemNuevo.setCantidadOriginal(itemCopiar.getCantidad());
            itemNuevo.setCantidadStock(itemCopiar.getProduccion());
        } else {
            itemNuevo.setCantidad(BigDecimal.ZERO);
            itemNuevo.setCantidadPendiente(BigDecimal.ZERO);
            itemNuevo.setCantidadOriginal(BigDecimal.ZERO);
            itemNuevo.setCantidadStock(BigDecimal.ZERO);
        }

        itemNuevo.setActualizaStock(itemCopiar.getActualizaStock());
        itemNuevo.setGrupo(itemCopiar.getGrupo());

        if (itemCopiar.getComposicionFormula() != null) {
            itemNuevo.setComposicionFormula(itemCopiar.getComposicionFormula());
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

            //Verificamos si el circuito aplica a items pendientes 
            if (movimiento.getCircuito().getNoCancelaPendiente().equals("N")) {
                ((ItemProductoProduccion) itemNuevo).setItemAplicado(((ItemProductoProduccion) itemCopiar));
            }

            if (movimiento.getCircuito().getActualizaStock().equals("S")) {
                generarItemDetalleItemProducto((ItemProductoProduccion) itemNuevo, (ItemProductoProduccion) itemCopiar);
            }

            movimiento.getItemsProducto().add((ItemProductoProduccion) itemNuevo);
        }

        if (itemNuevo instanceof ItemComponenteProduccion) {

            //Verificamos si el circuito aplica a items pendientes 
            if (movimiento.getCircuito().getNoCancelaPendiente().equals("N")) {
                ((ItemComponenteProduccion) itemNuevo).setItemAplicado(((ItemComponenteProduccion) itemCopiar));
            }

            if (movimiento.getCircuito().getActualizaStock().equals("S")) {
                generarItemDetalleItemComponente((ItemComponenteProduccion) itemNuevo, (ItemComponenteProduccion) itemCopiar);
            }

            movimiento.getItemsComponente().add((ItemComponenteProduccion) itemNuevo);
        }

        if (itemNuevo instanceof ItemProcesoProduccion) {

            //Verificamos si el circuito aplica a items pendientes 
            if (movimiento.getCircuito().getNoCancelaPendiente().equals("N")) {
                ((ItemProcesoProduccion) itemNuevo).setItemAplicado(((ItemProcesoProduccion) itemCopiar));
            }

            movimiento.getItemsProceso().add((ItemProcesoProduccion) itemNuevo);
        }

    }

    public void generarItemFromItemPendiente(MovimientoProduccion movimiento, PendienteProduccionDetalle itemPendiente, ItemMovimientoProduccion itemNuevo) throws ExcepcionGeneralSistema {

        itemNuevo.setProducto(itemPendiente.getProducto());
        itemNuevo.setProductoOriginal(itemPendiente.getProducto());
        itemNuevo.setUnidadMedida(itemPendiente.getUnidadMedida());

        if (itemPendiente.getCantidad() != null && itemPendiente.getCantidad().compareTo(BigDecimal.ZERO) > 0) {
            itemNuevo.setCantidad(itemPendiente.getCantidad());
            itemNuevo.setCantidadPendiente(itemPendiente.getCantidad());
            itemNuevo.setCantidadOriginal(itemPendiente.getCantidad());
            itemNuevo.setCantidadStock(itemPendiente.getCantidad());
        } else {
            itemNuevo.setCantidad(itemPendiente.getPendiente());
            itemNuevo.setCantidadOriginal(itemPendiente.getPendiente());
            itemNuevo.setCantidadStock(itemPendiente.getPendiente());
        }

        itemNuevo.setActualizaStock(itemPendiente.getStocks());
        itemNuevo.setGrupo(itemPendiente.getGrupo());

        if (itemPendiente.getFormul() != null && !itemPendiente.getFormul().isEmpty()) {
            ComposicionFormula composicionFormula = composicionFormulaRN.getComprosicionFormula(itemPendiente.getArtcod(), itemPendiente.getFormul());
            itemNuevo.setComposicionFormula(composicionFormula);
        }

        itemNuevo.setTodoOk(true);

        if (itemNuevo instanceof ItemProductoProduccion) {

            //Verificamos si el circuito aplica a items pendientes 
            if (movimiento.getCircuito().getNoCancelaPendiente().equals("N")) {
                ItemProductoProduccion itemAplicado = produccionDAO.getItemProducto(itemPendiente.getIdIapl());
                ((ItemProductoProduccion) itemNuevo).setItemAplicado(itemAplicado);
            }

            if (movimiento.getCircuito().getActualizaStock().equals("S")) {
                generarItemDetalleItemProducto((ItemProductoProduccion) itemNuevo, null);
            }

            movimiento.getItemsProducto().add((ItemProductoProduccion) itemNuevo);

            sincronizarCantidadesItemDetalleProducto((ItemProductoProduccion) itemNuevo);
        }

        if (itemNuevo instanceof ItemComponenteProduccion) {

            //Verificamos si el circuito aplica a items pendientes 
            if (movimiento.getCircuito().getNoCancelaPendiente().equals("N")) {
                ItemComponenteProduccion itemAplicado = produccionDAO.getItemComponente(itemPendiente.getIdIapl());
                ((ItemComponenteProduccion) itemNuevo).setItemAplicado(itemAplicado);
            }

            if (movimiento.getCircuito().getActualizaStock().equals("S")) {
                generarItemDetalleItemComponente((ItemComponenteProduccion) itemNuevo, null);
            }

            movimiento.getItemsComponente().add((ItemComponenteProduccion) itemNuevo);

            sincronizarCantidadesItemDetalleComponente((ItemComponenteProduccion) itemNuevo);
        }

        if (itemNuevo instanceof ItemProcesoProduccion) {

            //Verificamos si el circuito aplica a items pendientes 
            if (movimiento.getCircuito().getNoCancelaPendiente().equals("N")) {
                ItemProcesoProduccion itemAplicado = produccionDAO.getItemProceso(itemPendiente.getIdIapl());
                ((ItemProcesoProduccion) itemNuevo).setItemAplicado(itemAplicado);
            }

            movimiento.getItemsProceso().add((ItemProcesoProduccion) itemNuevo);
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

            sErrores += controlCantidadesItemsDetalleProducto(item, item.getItemDetalleTemporal());

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

            sErrores += controlCantidadesItemsDetalleComponente(item, item.getItemDetalleTemporal());

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
    public void generarItemsProcesoFromItemMovimiento(MovimientoProduccion movimientoProduccion, List<ItemMovimientoProduccion> itemsMovimiento) throws ExcepcionGeneralSistema {

        if (itemsMovimiento.isEmpty()) {
            return;
        }

        int cantSel = 0;
        for (ItemMovimientoProduccion itemMovimientoProduccion : itemsMovimiento) {

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

    public void generarItemDetalleItemProducto(ItemProductoProduccion item, ItemProductoProduccion itemCopiar) throws ExcepcionGeneralSistema {

        if (item.getItemDetalle() == null) {
            item.setItemDetalle(new ArrayList<ItemDetalleProductoProduccion>());
        }

        if (itemCopiar == null || itemCopiar.getItemDetalleTemporal() == null || itemCopiar.getItemDetalleTemporal().isEmpty()) {

            ItemDetalleProductoProduccion itemDetalle = nuevoItemDetalleItemProducto(item);
            item.getItemDetalle().add(itemDetalle);

        } else {

            for (ItemDetalleProductoProduccion itemDetalleCopiar : itemCopiar.getItemDetalleTemporal()) {

                ItemDetalleProductoProduccion itemDetalle = nuevoItemDetalleItemProductoFromItem(item, itemDetalleCopiar);
                item.getItemDetalle().add(itemDetalle);
            }
        }
    }

    /**
     * Generamos 1 item detalle para el item principal, si itemCopiar no es
     * nulo, compiamos los atributos
     *
     * @param item
     * @param itemCopiar
     * @throws ExcepcionGeneralSistema
     */
    public void generarItemDetalleItemComponente(ItemComponenteProduccion item, ItemComponenteProduccion itemCopiar) throws ExcepcionGeneralSistema {

        if (item.getItemDetalle() == null) {
            item.setItemDetalle(new ArrayList<ItemDetalleComponenteProduccion>());
        }

        if (itemCopiar == null || itemCopiar.getItemDetalleTemporal() == null || itemCopiar.getItemDetalleTemporal().isEmpty()) {

            ItemDetalleComponenteProduccion itemDetalle = nuevoItemDetalleItemComponente(item);
            item.getItemDetalle().add(itemDetalle);

        } else {

            for (ItemDetalleComponenteProduccion itemDetalleCopiar : itemCopiar.getItemDetalleTemporal()) {

                ItemDetalleComponenteProduccion itemDetalle = nuevoItemDetalleItemComponenteFromItem(item, itemDetalleCopiar);
                item.getItemDetalle().add(itemDetalle);
            }
        }
    }

//    /**
//     * Generar los items detalle con los números de atributos para el stock
//     * Genera un item detalle para todos los items productos, componentes
//     *
//     * @param movimientoProduccion
//     * @throws ExcepcionGeneralSistema
//     */
//    public void generarItemsDetallesVacio(MovimientoProduccion movimientoProduccion) throws ExcepcionGeneralSistema {
//
//        if (movimientoProduccion.getItemsProducto() != null) {
//
//            for (ItemProductoProduccion itemProducto : movimientoProduccion.getItemsProducto()) {
//
//                //Si el detalle está vacio lo llenamos, de lo contrario ya viene cargado desde la pagina
//                if (itemProducto.getItemDetalle().isEmpty()) {
//                    generarItemDetalleItemProducto(itemProducto, null);
//                }
//            }
//        }
//
//        if (movimientoProduccion.getItemsComponente() != null) {
//
//            for (ItemComponenteProduccion itemComponente : movimientoProduccion.getItemsComponente()) {
//
//                //Si el detalle está vacio lo llenamos, de lo contrario ya viene cargado desde la pagina
//                if (itemComponente.getItemDetalle().isEmpty()) {
//                    generarItemDetalleItemComponente(itemComponente, null);
//                }
//            }
//        }
//    }
    //-------------------------------------------------------------------------------------------    
    public void agregarItemDetalleProducto(ItemProductoProduccion nItem) {

        ItemDetalleProductoProduccion nItemD = nuevoItemDetalleItemProducto(nItem);
        nItemD.setCantidad(BigDecimal.ZERO);
        nItem.getItemDetalle().add(nItemD);

    }

    public void agregarItemDetalleTemporalProducto(ItemProductoProduccion nItem) {

        ItemDetalleProductoProduccion nItemD = nuevoItemDetalleItemProducto(nItem);
        nItemD.setCantidad(BigDecimal.ZERO);
        nItem.getItemDetalleTemporal().add(nItemD);

    }

    public void agregarItemDetalleComponente(ItemComponenteProduccion nItem) {

        ItemDetalleComponenteProduccion nItemD = nuevoItemDetalleItemComponente(nItem);
        nItemD.setCantidad(BigDecimal.ZERO);
        nItem.getItemDetalle().add(nItemD);
    }

    public void agregarItemDetalleTemporalComponente(ItemComponenteProduccion nItem) {

        ItemDetalleComponenteProduccion nItemD = nuevoItemDetalleItemComponente(nItem);
        nItemD.setCantidad(BigDecimal.ZERO);
        nItem.getItemDetalleTemporal().add(nItemD);
    }

    //------------------------------------------------------------------------------------------------------------------
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

            for (ItemDetalleMovimientoProduccion id : item.getItemDetalle()) {

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

    public void asignarComponente(ItemComponenteProduccion itemComponente, String grupo, Producto producto) throws ExcepcionGeneralSistema {

        if (itemComponente.getMovimiento().getMonedaSecundaria() == null) {
            JsfUtil.addWarningMessage("El comprobante no tiene una moneda secundaria asignada");
            return;
        }

        itemComponente.setProducto(producto);
        itemComponente.setProductoOriginal(producto);
        itemComponente.setGrupo(grupo);
        itemComponente.setUnidadMedida(producto.getUnidadDeMedida());
        itemComponente.setActualizaStock(producto.getGestionaStock());

    }

    public void asignarFormula(ItemProductoProduccion itemProducto, Formula formula) throws ExcepcionGeneralSistema {

        itemProducto.setFormula(formula);

        //Si es comprobante de Orden de producción, agregamos los componentes
        if (itemProducto.getMovimiento().getCircuito().getTipoMovimiento() == TipoMovimientoProduccion.OP) {

            agregarComponentesYProcesos(itemProducto.getMovimiento(), itemProducto);
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

    public void agregarComponentesYProcesos(MovimientoProduccion movimiento, ItemProductoProduccion itemProducto) throws ExcepcionGeneralSistema {

        if (itemProducto.getFormula() == null) {
            return;
        }

        if (itemProducto.getProducto() == null) {
            return;
        }

        ComposicionFormula composicionFormula = composicionFormulaRN.getComprosicionFormula(itemProducto.getProducto().getCodigo(), "STD");
        itemProducto.setComposicionFormula(composicionFormula);

        if (composicionFormula != null) {

            if (composicionFormula.getItemsComponente() == null && composicionFormula.getItemsProceso() == null) {

                throw new ExcepcionGeneralSistema("La formula del producto seleccionado no contiene componentes ni procesos ");

            } else {

                if (composicionFormula.getItemsComponente() != null) {

                    movimiento.getItemsComponente().clear();

                    for (ItemComposicionFormulaComponente i : composicionFormula.getItemsComponente()) {

                        ItemComponenteProduccion itmComp = nuevoItemComponente(movimiento);
                        BigDecimal cntNominal = i.getCantidadNominal();

                        itmComp.setProducto(i.getProductoComponente());
                        itmComp.setProductoOriginal(i.getProductoComponente());
                        itmComp.setUnidadMedida(i.getUnidadMedidaItem());
                        itmComp.setCantidadNominal(cntNominal);
                        itmComp.setCantidad(itemProducto.getCantidad().multiply(cntNominal));
                        itmComp.setCantidadStock(itemProducto.getCantidad().multiply(cntNominal));
                        itmComp.setCantidadOriginal(itemProducto.getCantidad().multiply(cntNominal));
                        itmComp.setUnidadMedida(i.getProductoComponente().getUnidadDeMedida());
                        itmComp.setActualizaStock(i.getProductoComponente().getGestionaStock());
                        itmComp.setGrupo(itemProducto.getGrupo());
                        
                        System.err.println("movimiento.getDeposito() " + movimiento.getDeposito());
                        
                        itmComp.setDeposito(movimiento.getDeposito());
                        movimiento.getItemsComponente().add(itmComp);
                    }
                }

                if (composicionFormula.getItemsProceso() != null) {

                    movimiento.getItemsProceso().clear();

                    for (ItemComposicionFormulaProceso i : composicionFormula.getItemsProceso()) {

                        ItemProcesoProduccion itmComp = nuevoItemProceso(movimiento);
                        BigDecimal cntNominal = i.getCantidadNominal();

                        itmComp.setProducto(i.getProductoComponente());
                        itmComp.setProductoOriginal(i.getProductoComponente());
                        itmComp.setUnidadMedida(i.getUnidadMedidaItem());
                        itmComp.setCantidadNominal(cntNominal);
                        itmComp.setCantidad(itemProducto.getCantidad().multiply(cntNominal));
                        itmComp.setCantidadStock(itemProducto.getCantidad().multiply(cntNominal));
                        itmComp.setCantidadOriginal(itemProducto.getCantidad().multiply(cntNominal));
                        itmComp.setUnidadMedida(i.getProductoComponente().getUnidadDeMedida());
                        itmComp.setActualizaStock(i.getProductoComponente().getGestionaStock());
                        itmComp.setGrupo(itemProducto.getGrupo());

                        movimiento.getItemsProceso().add(itmComp);
                    }
                }
            }

        } else {
            throw new ExcepcionGeneralSistema("El producto (" + itemProducto.getProducto().getCodigo() + "-" + itemProducto.getProducto().getDescripcion() + ") seleccionado no tiene una fórmula de producción definida");
        }
    }

    public void generarDatosAdicionales(MovimientoProduccion m) throws ExcepcionGeneralSistema, Exception {

        sincronizarCantidades(m);
        //generarItemsDetallesVacio(m);

    }

    public void controlComprobante(MovimientoProduccion movimiento) throws ExcepcionGeneralSistema, Exception {

        String sErrores = "";

        if (movimiento.getId() != null) {
            if (movimiento.getFormulario().getModfor().equals("PD")) {
                sErrores += "- No es posible modificar un comprobante de producción \n";
            }

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

//        if (movimiento.getCircuito().getActualizaStock().equals("S") && movimiento.getMovimientoStock() == null) {
//
//            throw new ExcepcionGeneralSistema("El circuito en " + movimiento.getFormulario().getDescripcion() + ", define que actualiza stock, pero el comprobante no fue generado o no está configurado");
//        }
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

        if (!sErrores.isEmpty()) {
            throw new ExcepcionGeneralSistema(sErrores);
        }
    }

    public void actualizarCantidades(MovimientoProduccion movimiento) throws ExcepcionGeneralSistema {

        if (movimiento.getItemsProducto() != null && !movimiento.getItemsProducto().isEmpty()) {

            for (ItemProductoProduccion item : movimiento.getItemsProducto()) {

                item.setCantidadStock(item.getCantidad());
                //Actualizamos la cantidad original solo si es un movimiento directo
                if (movimiento.getCircuito().getCircom().equals(movimiento.getCircuito().getCirapl())) {
                    item.setCantidadOriginal(item.getCantidad());
                }

                //Si es una hoja de ruta vaciamos y volvemos a cargar los componentes
                if (movimiento.getCircuito().getTipoMovimiento() == TipoMovimientoProduccion.OP) {

                    movimiento.getItemsComponente().clear();
                    agregarComponentesYProcesos(movimiento, item);
                }

                sincronizarCantidadesItemDetalleProducto(item);
            }
        }

        if (movimiento.getItemsComponente() != null && !movimiento.getItemsComponente().isEmpty()) {

            for (ItemComponenteProduccion item : movimiento.getItemsComponente()) {

                item.setCantidadStock(item.getCantidad());
                //Actualizamos la cantidad original solo si es un movimiento directo
                if (movimiento.getCircuito().getCircom().equals(movimiento.getCircuito().getCirapl())) {
                    item.setCantidadOriginal(item.getCantidad());
                }

                sincronizarCantidadesItemDetalleComponente(item);
            }
        }        
    }

    /**
     * Actualizamos los atributos de stock, por el momento solo maneja nro de
     * serie
     *
     * @param item
     */
    public void actualizarAtributosProducto(ItemProductoProduccion item) {

        if (item.getItemDetalle() == null || item.getItemDetalle().isEmpty()) {
            ItemDetalleMovimientoProduccion nuevoItemDetalle = nuevoItemDetalleItemProducto(item);
        }

        for (ItemDetalleMovimientoProduccion id : item.getItemDetalle()) {

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
            ItemDetalleComponenteProduccion nuevoItemDetalle = nuevoItemDetalleItemComponente(item);
        }

        for (ItemDetalleComponenteProduccion id : item.getItemDetalle()) {

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

    public void sincronizarCantidades(MovimientoProduccion m) {

        if (m.getItemsProducto() != null) {

            for (ItemProductoProduccion i : m.getItemsProducto()) {

                if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                    return;
                }
                i.setCantidadPendiente(i.getCantidad());
                i.setCantidadStock(i.getCantidad());
                i.setCantidadOriginal(i.getCantidad());
            }
        }

        if (m.getItemsComponente() != null) {

            for (ItemComponenteProduccion i : m.getItemsComponente()) {

                if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                    return;
                }
                i.setCantidadPendiente(i.getCantidad());
                i.setCantidadStock(i.getCantidad());
                i.setCantidadOriginal(i.getCantidad());
            }
        }

        if (m.getItemsProceso() != null) {

            for (ItemProcesoProduccion i : m.getItemsProceso()) {

                if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                    return;
                }
                i.setCantidadPendiente(i.getCantidad());
                i.setCantidadStock(i.getCantidad());
                i.setCantidadOriginal(i.getCantidad());
            }
        }

        if (m.getItemsHorario() != null) {

            for (ItemHorarioProduccion i : m.getItemsHorario()) {

                if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                    return;
                }
                i.setCantidadPendiente(i.getCantidad());
                i.setCantidadStock(i.getCantidad());
                i.setCantidadOriginal(i.getCantidad());
            }
        }

    }

    public void actualizarCantidadesPendientes(MovimientoProduccion m) {

        if (m.getItemsProducto() != null) {

            for (ItemProductoProduccion i : m.getItemsProducto()) {

                if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                    return;
                }
                if (i.getItemAplicado() != null) {
                    BigDecimal aplicadoActual = produccionDAO.getCantidadAplicadaItem(i.getItemAplicado().getId());
                    BigDecimal pendiente = i.getItemAplicado().getCantidad().add(aplicadoActual.negate());
                    i.getItemAplicado().setCantidadPendiente(pendiente);
                    produccionDAO.editar(i.getItemAplicado());
                }
            }
        }

        if (m.getItemsComponente() != null) {

            for (ItemComponenteProduccion i : m.getItemsComponente()) {

                if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                    return;
                }
                if (i.getItemAplicado() != null) {
                    BigDecimal aplicadoActual = produccionDAO.getCantidadAplicadaItem(i.getItemAplicado().getId());
                    BigDecimal pendiente = i.getItemAplicado().getCantidad().add(aplicadoActual.negate());
                    i.getItemAplicado().setCantidadPendiente(pendiente);
                    produccionDAO.editar(i.getItemAplicado());
                }
            }
        }

        if (m.getItemsProceso() != null) {

            for (ItemProcesoProduccion i : m.getItemsProceso()) {

                if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                    return;
                }
                if (i.getItemAplicado() != null) {
                    BigDecimal aplicadoActual = produccionDAO.getCantidadAplicadaItem(i.getItemAplicado().getId());
                    BigDecimal pendiente = i.getItemAplicado().getCantidad().add(aplicadoActual.negate());
                    i.getItemAplicado().setCantidadPendiente(pendiente);
                    produccionDAO.editar(i.getItemAplicado());
                }
            }
        }

        if (m.getItemsHorario() != null) {

            for (ItemHorarioProduccion i : m.getItemsHorario()) {

                if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                    return;
                }
                if (i.getItemAplicado() != null) {
                    BigDecimal aplicadoActual = produccionDAO.getCantidadAplicadaItem(i.getItemAplicado().getId());
                    BigDecimal pendiente = i.getItemAplicado().getCantidad().add(aplicadoActual.negate());
                    i.getItemAplicado().setCantidadPendiente(pendiente);
                    produccionDAO.editar(i.getItemAplicado());
                }
            }
        }
    }

    public void reorganizarNroItem(MovimientoProduccion movimiento) {

        //Reorganizamos los números de items
        int i = 1;

        if (movimiento.getItemsProducto() != null) {
            for (ItemProductoProduccion ip : movimiento.getItemsProducto()) {
                ip.setNroitm(i);
                i++;
                int d = 0;

                for (ItemDetalleProductoProduccion itemDetalle : ip.getItemDetalle()) {
                    itemDetalle.setNroitm(d);
                    d++;
                }
            }
        }

        if (movimiento.getItemsComponente() != null) {
            for (ItemComponenteProduccion ip : movimiento.getItemsComponente()) {
                ip.setNroitm(i);
                i++;
                int d = 0;

                for (ItemDetalleComponenteProduccion itemDetalle : ip.getItemDetalle()) {
                    itemDetalle.setNroitm(d);
                    d++;
                }
            }
        }

        if (movimiento.getItemsProceso() != null) {
            for (ItemProcesoProduccion ip : movimiento.getItemsProceso()) {
                ip.setNroitm(i);
                i++;
                int d = 0;
            }
        }

        if (movimiento.getItemsHorario() != null) {
            for (ItemHorarioProduccion ip : movimiento.getItemsHorario()) {
                ip.setNroitm(i);
                i++;
                int d = 0;
            }
        }

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

    @Deprecated
    public boolean tengoItemsConCantidad(CircuitoProduccion circuito, Object itemsPendientes) {

        if (itemsPendientes == null || ((List<ItemMovimientoProduccion>) itemsPendientes).isEmpty()) {
            return false;
        }

        if (circuito.getNoControlaPendiente().equals("N")) {
            return true;
        }

        for (ItemMovimientoProduccion i : (List<ItemMovimientoProduccion>) itemsPendientes) {

            if (i.getCantidadPendiente() != null && i.getCantidadPendiente().compareTo(BigDecimal.ZERO) > 0) {
                return true;
            }
        }
        return false;
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

                for (ItemDetalleProductoProduccion d : ((ItemProductoProduccion) i).getItemDetalle()) {

                    if (d.getCantidad().compareTo(BigDecimal.ZERO) > 0) {
                        return true;
                    }
                }
            }

            if (i instanceof ItemComponenteProduccion) {

                for (ItemDetalleComponenteProduccion d : ((ItemComponenteProduccion) i).getItemDetalle()) {

                    if (d.getCantidad().compareTo(BigDecimal.ZERO) > 0) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    public void seleccionarTodo(List<PendienteProduccionDetalle> itemsPendiente, boolean seleccionarTodo) {

        if (itemsPendiente == null) {
            return;
        }

        for (PendienteProduccionDetalle i : itemsPendiente) {
            i.setSeleccionado(seleccionarTodo);
        }
    }

    public void ponerItemsDetalleEnCero(MovimientoProduccion movimiento) {

        if (movimiento.getItemsProducto() != null) {

            for (ItemProductoProduccion itemProducto : movimiento.getItemsProducto()) {

                if (itemProducto.getItemDetalle() != null) {

                    for (ItemDetalleProductoProduccion item : itemProducto.getItemDetalle()) {
                        item.setCantidad(BigDecimal.ZERO);
                    }
                }
            }
        }

        if (movimiento.getItemsComponente() != null) {

            for (ItemComponenteProduccion itemComponente : movimiento.getItemsComponente()) {

                if (itemComponente.getItemDetalle() != null) {

                    for (ItemDetalleComponenteProduccion item : itemComponente.getItemDetalle()) {
                        item.setCantidad(BigDecimal.ZERO);
                    }
                }
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

    public String controlItemsDetalleProducto(ItemProductoProduccion item) throws Exception {

        String sErrores = "";

        ParametroStock parametroStock = parametroStockRN.getParametro();
        //variable temporal para comparar la suma de los items
        BigDecimal cantItems = BigDecimal.ZERO;

        for (ItemDetalleProductoProduccion itemDetalle : item.getItemDetalle()) {

            //Acumulamos las cantidades
            cantItems = cantItems.add(itemDetalle.getCantidad().setScale(2, BigDecimal.ROUND_HALF_DOWN));

            if (itemDetalle.getCantidad().equals(BigDecimal.ZERO)) {
                item.setConError(true);
                sErrores += "- No puede tener un item de apertura con cantidad cero en " + item.getProducto().getDescripcion() + "\n";
            }

            //Si el circuito define que controla atributos los validamos
            if (item.getMovimiento().getCircuito().getAdministraAtributo1().equals("S")
                    && itemDetalle.getProducto().getAdministraAtributo1().equals("S")) {

                if (itemDetalle.getAtributo1() == null || itemDetalle.getAtributo1().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 1 (" + parametroStock.getDescripcionAtributo1() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }

            if (item.getMovimiento().getCircuito().getAdministraAtributo2().equals("S")
                    && itemDetalle.getProducto().getAdministraAtributo2().equals("S")) {
                if (itemDetalle.getAtributo2() == null || itemDetalle.getAtributo2().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 2 (" + parametroStock.getDescripcionAtributo2() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }

            if (item.getMovimiento().getCircuito().getAdministraAtributo3().equals("S")
                    && itemDetalle.getProducto().getAdministraAtributo3().equals("S")) {
                if (itemDetalle.getAtributo3() == null || itemDetalle.getAtributo3().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 3 (" + parametroStock.getDescripcionAtributo3() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }

            if (item.getMovimiento().getCircuito().getAdministraAtributo4().equals("S")
                    && itemDetalle.getProducto().getAdministraAtributo4().equals("S")) {
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
     * @param itemsDetalle
     * @return
     * @throws Exception
     */
    public String controlCantidadesItemsDetalleProducto(ItemProductoProduccion itemOrden,
            List<ItemDetalleProductoProduccion> itemsDetalle) throws Exception {

        String sErrores = "";
        ParametroStock parametroStock = parametroStockRN.getParametro();
        //variable temporal para comparar la suma de los items
        BigDecimal cantItems = BigDecimal.ZERO;

        for (ItemDetalleProductoProduccion itemDetalle : itemsDetalle) {

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

        for (ItemDetalleComponenteProduccion itemDetalle : item.getItemDetalle()) {

            //Acumulamos las cantidades
            cantItems = cantItems.add(itemDetalle.getCantidad().setScale(2, BigDecimal.ROUND_HALF_DOWN));

            if (itemDetalle.getCantidad().equals(BigDecimal.ZERO)) {
                item.setConError(true);
                sErrores += "- No puede tener un item de apertura con cantidad cero en " + item.getProducto().getDescripcion() + "\n";
            }

            //Si el circuito define que controla atributos los validamos
            if (item.getMovimiento().getCircuito().getAdministraAtributo1().equals("S")
                    && item.getProducto().getAdministraAtributo1().equals("S")) {
                if (itemDetalle.getAtributo1() == null || itemDetalle.getAtributo1().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 1 (" + parametroStock.getDescripcionAtributo1() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }

            if (item.getMovimiento().getCircuito().getAdministraAtributo2().equals("S")
                    && item.getProducto().getAdministraAtributo2().equals("S")) {
                if (itemDetalle.getAtributo2() == null || itemDetalle.getAtributo2().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 2 (" + parametroStock.getDescripcionAtributo2() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }

            if (item.getMovimiento().getCircuito().getAdministraAtributo3().equals("S")
                    && item.getProducto().getAdministraAtributo3().equals("S")) {
                if (itemDetalle.getAtributo3() == null || itemDetalle.getAtributo3().isEmpty()) {
                    item.setConError(true);
                    sErrores += "- Ingrese el atributo 3 (" + parametroStock.getDescripcionAtributo3() + ") para el producto " + itemDetalle.getProducto().getDescripcion() + "\n";
                }
            }

            if (item.getMovimiento().getCircuito().getAdministraAtributo4().equals("S")
                    && item.getProducto().getAdministraAtributo4().equals("S")) {
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
    public String controlCantidadesItemsDetalleComponente(ItemComponenteProduccion itemOrden, List<ItemDetalleComponenteProduccion> itemsDetalle) throws Exception {

        String sErrores = "";

        ParametroStock parametroStock = parametroStockRN.getParametro();
        //variable temporal para comparar la suma de los items
        BigDecimal cantItems = BigDecimal.ZERO;

        for (ItemDetalleComponenteProduccion itemDetalle : itemsDetalle) {

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

    //-------------------------------------------------------------------------------------------
    /**
     * Eliminar un item de un movimiento
     *
     * @param movimiento movimiento del cual se eliminará el item
     * @param nItem item a eliminar
     */
    public boolean eliminarItemProducto(MovimientoProduccion movimiento, ItemProductoProduccion nItem) {

        boolean fItemBorrado = false;
        int i = 0;
        int indiceItemProducto = -1;
        int indiceItemAplicacion = -1;

        for (ItemProductoProduccion ip : movimiento.getItemsProducto()) {

            if (ip.getProducto() != null) {

                if (ip.getProducto().equals(nItem.getProducto())) {
                    indiceItemProducto = i;
                }
            }
            i++;
        }
        i = 0;

        //Borramos los items productos
        if (indiceItemProducto >= 0) {
            ItemProductoProduccion remove = movimiento.getItemsProducto().remove(indiceItemProducto);
            if (remove != null) {
                fItemBorrado = true;
            }
        }

        reorganizarNroItem(movimiento);
        return fItemBorrado;
    }

    public void eliminarMovimiento(MovimientoProduccion movimientoProduccion) throws Exception {

        produccionDAO.eliminar(MovimientoProduccion.class, movimientoProduccion.getId());
        stockRN.recalcularStock();
        //if(movimientoProduccion.getMovimientoStock()!=null){
        //    movimientoStockRN.eliminarMovimiento(movimientoProduccion.getMovimientoStock());
        //}        
    }

    public boolean eliminarItemDetalleProducto(ItemProductoProduccion ip, ItemDetalleMovimientoProduccion nItem) throws ExcepcionGeneralSistema {

        boolean fItemBorrado = false;
        int i = 0;
        int indiceItemProducto = -1;

        //Verificamos que siempre quede 1 items detalle
        if (ip.getItemDetalle().size() == 1) {

            throw new ExcepcionGeneralSistema("No es posible eliminar item de apertura, la cantidad mínima es un item");
        }

        //Buscamos el indice del item a borrar
        for (ItemDetalleMovimientoProduccion id : ip.getItemDetalle()) {

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
            ItemDetalleMovimientoProduccion remove = ip.getItemDetalle().remove(indiceItemProducto);

            if (remove != null) {
                fItemBorrado = true;
            }
        }
        return fItemBorrado;
    }

    public boolean eliminarItemDetalleTemporalProducto(ItemProductoProduccion ip, ItemDetalleMovimientoProduccion nItem) throws ExcepcionGeneralSistema {

        boolean fItemBorrado = false;
        int i = 0;
        int indiceItemProducto = -1;

        //Verificamos que siempre quede 1 items detalle
        if (ip.getItemDetalleTemporal().size() == 1) {

            throw new ExcepcionGeneralSistema("No es posible eliminar item de apertura, la cantidad mínima es un item");
        }

        //Buscamos el indice del item a borrar
        for (ItemDetalleMovimientoProduccion id : ip.getItemDetalleTemporal()) {

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
            ItemDetalleMovimientoProduccion remove = ip.getItemDetalleTemporal().remove(indiceItemProducto);

            if (remove != null) {
                fItemBorrado = true;
            }
        }
        return fItemBorrado;
    }

    public boolean eliminarItemDetalleComponente(ItemComponenteProduccion ip, ItemDetalleMovimientoProduccion nItem) throws ExcepcionGeneralSistema {

        boolean fItemBorrado = false;
        int i = 0;
        int indiceItemProducto = -1;

        //Verificamos que siempre quede 1 items detalle
        if (ip.getItemDetalle().size() == 1) {

            throw new ExcepcionGeneralSistema("No es posible eliminar item de apertura, la cantidad mínima es un item");
        }

        //Buscamos el indice del item a borrar
        for (ItemDetalleMovimientoProduccion id : ip.getItemDetalle()) {

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
            ItemDetalleMovimientoProduccion remove = ip.getItemDetalle().remove(indiceItemProducto);

            if (remove != null) {
                fItemBorrado = true;
            }
        }
        return fItemBorrado;
    }

    public boolean eliminarItemDetalleTemporalComponente(ItemComponenteProduccion ip, ItemDetalleMovimientoProduccion nItem) throws ExcepcionGeneralSistema {

        boolean fItemBorrado = false;
        int i = 0;
        int indiceItemProducto = -1;

        //Verificamos que siempre quede 1 items detalle
        if (ip.getItemDetalleTemporal().size() == 1) {

            throw new ExcepcionGeneralSistema("No es posible eliminar item de apertura, la cantidad mínima es un item");
        }

        //Buscamos el indice del item a borrar
        for (ItemDetalleMovimientoProduccion id : ip.getItemDetalleTemporal()) {

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
            ItemDetalleMovimientoProduccion remove = ip.getItemDetalleTemporal().remove(indiceItemProducto);

            if (remove != null) {
                fItemBorrado = true;
            }
        }
        return fItemBorrado;
    }

    //-------------------------------------------------------------------------------------------
    // CONSULTAS
    //-------------------------------------------------------------------------------------------    
    public MovimientoProduccion getMovimiento(Integer id) {

        return produccionDAO.getMovimiento(id);
    }

    public MovimientoProduccion getMovimiento(String codFormulario, Integer numeroFormulario) {

        return produccionDAO.getMovimiento(codFormulario, numeroFormulario);
    }

    public List<MovimientoProduccion> getListaByBusqueda(Map<String, String> filtro, boolean soloPendientes, int cantMax) {

        return produccionDAO.getListaByBusqueda(filtro, soloPendientes, cantMax);
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

    public List<ItemProcesoProduccion> getItemProcesoByGrupoSector(MovimientoProduccion op, String grupo, Sector sector) {

        List<ItemProcesoProduccion> procesosSeleccionados = new ArrayList<ItemProcesoProduccion>();

        for (ItemProcesoProduccion iProceso : op.getItemsProceso()) {

            if (iProceso.getGrupo().equals(grupo)) {

                if (iProceso.getProducto().getSector() == null || sector == null) {
                    procesosSeleccionados.add(iProceso);
                } else if (iProceso.getProducto().getSector().getCodigo().equals(sector.getCodigo())) {
                    procesosSeleccionados.add(iProceso);
                }
            }
        }
        return procesosSeleccionados;
    }

    //Cargamos y mostramos solo los productos asociados al producto a producir seleccionado.
    public List<ItemProductoProduccion> getItemProductoByGrupo(MovimientoProduccion op, String grupo) throws ExcepcionGeneralSistema {

        List<ItemProductoProduccion> productoSeleccionados = new ArrayList<ItemProductoProduccion>();

        for (ItemProductoProduccion iProducto : op.getItemsProducto()) {

            if (iProducto.getGrupo().equals(grupo)) {

                if (iProducto.getItemDetalleTemporal() == null) {
                    iProducto.setItemDetalleTemporal(new ArrayList<ItemDetalleProductoProduccion>());
                }

                ItemDetalleProductoProduccion itemDetalle = nuevoItemDetalleItemProducto(iProducto);
                iProducto.getItemDetalleTemporal().add(itemDetalle);
                productoSeleccionados.add(iProducto);
            }
        }
        return productoSeleccionados;
    }

    //Cargamos y mostramos solo los componentes asociados al producto a producir seleccionado.
    public List<ItemComponenteProduccion> getItemComponenteByGrupo(MovimientoProduccion op, String grupo) throws ExcepcionGeneralSistema {

        List<ItemComponenteProduccion> componenteSeleccionados = new ArrayList<ItemComponenteProduccion>();

        for (ItemComponenteProduccion iComponente : op.getItemsComponente()) {

            if (iComponente.getGrupo().equals(grupo)) {

                if (iComponente.getItemDetalleTemporal() == null) {
                    iComponente.setItemDetalleTemporal(new ArrayList<ItemDetalleComponenteProduccion>());
                }

                ItemDetalleComponenteProduccion itemDetalle = nuevoItemDetalleItemComponente(iComponente);
                iComponente.getItemDetalleTemporal().add(itemDetalle);

                componenteSeleccionados.add(iComponente);
            }
        }
        return componenteSeleccionados;
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

    public ItemProductoProduccion getItemProducto(Integer id) {
        return produccionDAO.getItemProducto(id);
    }

    public ItemComponenteProduccion getItemComponente(Integer id) {
        return produccionDAO.getItemComponente(id);
    }

    public void sincronizarCantidadesItemDetalleComponente(ItemComponenteProduccion itemComponente) {

        if (itemComponente.getItemDetalle() != null) {
            if (itemComponente.getItemDetalle().size() == 1) {
                itemComponente.getItemDetalle().get(0).setCantidad(itemComponente.getProduccion());
            }
        }
    }

    public void sincronizarCantidadesItemDetalleProducto(ItemProductoProduccion itemProducto) {

        if (itemProducto.getItemDetalle() != null) {
            if (itemProducto.getItemDetalle().size() == 1) {
                itemProducto.getItemDetalle().get(0).setCantidad(itemProducto.getProduccion());
            }
        }

    }

    public void sincronizarCantidadesItemDetalleTemporalComponente(ItemComponenteProduccion itemComponente) {

        if (itemComponente.getItemDetalleTemporal() != null) {
            if (itemComponente.getItemDetalleTemporal().size() == 1) {
                itemComponente.getItemDetalleTemporal().get(0).setCantidad(itemComponente.getProduccion());
            }
        }
    }

    public void sincronizarCantidadesItemDetalleTemporalProducto(ItemProductoProduccion itemProducto) {

        if (itemProducto.getItemDetalleTemporal() != null) {
            if (itemProducto.getItemDetalleTemporal().size() == 1) {
                itemProducto.getItemDetalleTemporal().get(0).setCantidad(itemProducto.getProduccion());
            }
        }

    }

}
