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
import bs.global.util.JsfUtil;
import bs.produccion.dao.ProduccionDAO;
import bs.produccion.modelo.CircuitoProduccion;
import bs.produccion.modelo.ItemAnulacionProduccion;
import bs.produccion.modelo.ItemAplicacionProduccion;
import bs.produccion.modelo.ItemCircuitoProduccionProduccion;
import bs.produccion.modelo.ItemCircuitoProduccionStock;
import bs.produccion.modelo.ItemComponenteProduccion;
import bs.produccion.modelo.ItemDetalleItemMovimientoProduccion;
import bs.produccion.modelo.ItemMovimientoProduccion;
import bs.produccion.modelo.ItemProcesoProduccion;
import bs.produccion.modelo.ItemProductoProduccion;
import bs.produccion.modelo.MovimientoProduccion;
import bs.produccion.modelo.TipoMovimientoProduccion;
import bs.produccion.vista.PendienteProduccionDetalle;
import bs.produccion.vista.PendienteProduccionGrupo;
import bs.stock.modelo.ComposicionFormula;
import bs.stock.modelo.ComprobanteStock;
import bs.stock.modelo.MovimientoStock;
import bs.stock.rn.ComposicionFormulaRN;
import bs.stock.rn.ComprobanteStockRN;
import bs.stock.rn.MovimientoStockRN;
import bs.stock.rn.ParametroStockRN;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
    private ParametroStockRN parametroStockRN;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public synchronized MovimientoProduccion guardar(MovimientoProduccion m) throws Exception {

        borrarItemsNoValidos(m);
        generarDatosAdicionales(m);

        if (m.getId() == null) {
            generarMovimientosAdicionales(m);
            tomarNumeroFormulario(m);

            if (m.getCircuito().getAutomatizaParteProduccion().equals("S")) {

                //produccionDAO.crear(m);
                //produccionDAO.crear(m.getValeConsumo());
                //produccionDAO.crear(m.getParteProceso());
                if (m.getValeConsumo() != null) {
                    tomarNumeroFormulario(m.getValeConsumo());
                    m.getValeConsumo().setPersistido(true);
                    guardarComprobanteStock(m.getValeConsumo().getMovimientoStock());
                }

                if (m.getParteProceso() != null) {
                    tomarNumeroFormulario(m.getParteProceso());
                    m.getParteProceso().setPersistido(true);
                }
            }

            puedoGuardarMovimiento(m);
            produccionDAO.crear(m);

            if (sincronizarIdAplicacion(m)) {
                m = produccionDAO.editar(m);
            }

        } else {
            sincronizarIdAplicacion(m);

            puedoGuardarMovimiento(m);
            m = produccionDAO.editar(m);
        }

        m.setPersistido(true);
        return m;
    }

    private void generarMovimientosAdicionales(MovimientoProduccion m) throws ExcepcionGeneralSistema, Exception {

        if (m.getComprobanteStock() != null) {

            MovimientoStock ms = movimientoStockRN.generarComprobante(m);
            m.setMovimientoStock(ms);
        }
    }

    /**
     * Se utiliza para generar movimeintos no predeterminados
     *
     * @param circuito
     * @return
     * @throws ExcepcionGeneralSistema
     */
    public List<Comprobante> getComprobantesCircuito(CircuitoProduccion circuito) throws ExcepcionGeneralSistema {

        List<Comprobante> listaComprobante = new ArrayList<Comprobante>();

        if (circuito != null) {

            if (circuito.getActualizaProduccion().equals("S")) {
                //Cargamos la lista de comprobantes de produccion disponibles
                for (ItemCircuitoProduccionProduccion i : circuito.getItemCircuitoProduccion()) {
                    //System.out.println("Comprobante produccion: " + i.getComprobante());
                    listaComprobante.add(i.getComprobante());
                }
            }

            if (circuito.getActualizaStock().equals("S")) {
                //Cargamos la lista de comprobantes de stock disponibles
                for (ItemCircuitoProduccionStock i : circuito.getItemCircuitoStock()) {
                    //System.out.println("Comprobante stock: " + i.getComprobante());
                    listaComprobante.add(i.getComprobante());
                }
            }

            if (listaComprobante.isEmpty()) {
                throw new ExcepcionGeneralSistema("No existen comprobantes para el circuito seleccionado");
            }

        } else {
            throw new ExcepcionGeneralSistema("El circuito no puede ser nulo");
        }

        return listaComprobante;
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

//        if (comprobanteVC != null) {
//            MovimientoProduccion mVC = crearMovimiento(circuito, comprobanteVC, TipoMovimientoProduccion.VC, sucursal, sucursalStock);
//            m.setValeConsumo(mVC);
//        }
//
//        if (comprobantePR != null) {
//            MovimientoProduccion mPR = crearMovimiento(circuito, comprobantePR, TipoMovimientoProduccion.PR, sucursal, sucursalStock);
//            m.setParteProceso(mPR);
//        }
        return m;
    }

    public MovimientoProduccion nuevoMovimientoFromPendiente(CircuitoProduccion circuito, Sucursal sucursal, Sucursal sucursalStock, 
            List<PendienteProduccionDetalle> itemsPendientes) throws ExcepcionGeneralSistema, Exception {

        if (!tengoItemsSeleccionados(itemsPendientes)) {
            throw new ExcepcionGeneralSistema("No existen items seleccionados para generar el movimiento");
        }

        MovimientoProduccion m = nuevoMovimiento(circuito, sucursal, sucursalStock);
        generarItemsFromPendiente(m, itemsPendientes);
        asignarFormulario(m);

        if (circuito.getAgregaItems().equals("S")) {
            //Cargarmos un nuevo item en blanco en caso de que quieran guardar sin agregar un items
            m.getItemsProducto().add((ItemProductoProduccion) itemMovimientoRN.nuevoItemProducto(m));
        }
        return m;
    }
    
    public MovimientoProduccion nuevoMovimientoFromItems(CircuitoProduccion circuito, Sucursal sucursal, Sucursal sucursalStock,
            Object itemsMovimiento) throws ExcepcionGeneralSistema, Exception {

        if (!tengoItemsConCantidad(itemsMovimiento)) {
            throw new ExcepcionGeneralSistema("No se han ingresado cantidades para generar el movimiento");
        }

        MovimientoProduccion m = nuevoMovimiento(circuito, sucursal, sucursalStock);
        generarItemsFromItemMovimiento(m, (List<ItemMovimientoProduccion>) itemsMovimiento);
        asignarFormulario(m);
        
        return m;
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
        m.setMonedaRegistracion(comprobante.getMonedaRegistracion());
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
        generarItemsDetalleVacio(m);
        //Generamos los items serie de programación
        generarItemsSerie(m);
        generarItemsAnulacion(m);
        borrarItemsSerie(m);
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

                    System.out.println("Operario: " + i.getOperario());

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
     *
     * @param m
     * @throws ExcepcionGeneralSistema
     */
    public void generarItemsDetalleVacio(MovimientoProduccion m) throws ExcepcionGeneralSistema {

        if (m.getItemsProducto() == null) {
            return;
        }

        if (m.getCircuito().getActualizaStock().equals("S")) {
            for (ItemProductoProduccion i : m.getItemsProducto()) {

                //Si el detalle está vacio lo llenamos, de lo contrario ya viene cargado desde la pagina
                if (i.getItemDetalle().isEmpty()) {
                    generarItemDetalle(i);
                }
            }
        }
    }

    public void generarItemDetalle(ItemProductoProduccion i) throws ExcepcionGeneralSistema {

        if (i.getItemDetalle() == null) {
            i.setItemDetalle(new ArrayList<ItemDetalleItemMovimientoProduccion>());
        }

        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        List<Object[]> lSeries = null;

        //Verificamos que el movimiento sea vale de consumo para que busque los números de
        //serie del requerimiento
        if (i.getMovimiento().getCircuito().getAdministraAtributo1().equals("S")
                && i.getMovimiento().getTipoMovimiento().equals(TipoMovimientoProduccion.VC)) {

//             lSeries = produccionDAO.getSerieFromRequerimiento(i);
        } else {

            //Buscamos la lista de serie del item aplicado
            lSeries = new ArrayList<Object[]>();
        }

        if (!lSeries.isEmpty()) {

            for (Object[] nSerie : lSeries) {

                ItemDetalleItemMovimientoProduccion d = itemMovimientoRN.nuevoItemDetalle(i);

                d.setCantidad(i.getCantidad());
                d.setCantidad(i.getCantidad());

                if (nSerie[0] != null) {
                    d.setAtributo1(String.valueOf(nSerie[0]));
                }
            }
        } else {

            ItemDetalleItemMovimientoProduccion d = itemMovimientoRN.nuevoItemDetalle(i);
            d.setCantidad(i.getCantidad());
            d.setCantidad(i.getCantidad());

            if (i.getAtributo1() != null) {
                d.setAtributo1(i.getAtributo1());
            }
        }
    }

    /**
     * Se generan los item para guardar las series registradas, esto va fuera
     * del stock por que es imposible registrar serie por serie, solo para tener
     * un registro
     *
     * @param m Movimiento de producción
     * @throws ExcepcionGeneralSistema
     */
    public void generarItemsSerie(MovimientoProduccion m) throws ExcepcionGeneralSistema {

        if (m.getItemsProducto() == null) {
            return;
        }

//        for(ItemProductoProduccion i: m.getItemProducto()){
//
//            if(i.getSerdes()==null || i.getSerhas()==null) return;
//
//            if (!i.getSerdes().isEmpty() && !i.getSerhas().isEmpty()) {
//
//                Integer serieHasta = null;
//                Integer serieDesde = null;
//                try {
//                    serieDesde = Integer.parseInt(i.getSerdes());
//                    serieHasta = Integer.parseInt(i.getSerhas());
//                } catch (NumberFormatException numberFormatException) {
//                    throw new ExcepcionGeneralSistema("Los número de serie deben ser numéricos");
//                }
//
//                if (serieHasta < serieDesde) {
//                    throw new ExcepcionGeneralSistema("Serie hasta no puede ser menor a serie desde");
//                }
//
//                int cantGenerar = serieHasta - serieDesde + 1;
//
////                System.out.println("Cantidad a generar:" + cantGenerar);
////                System.out.println("Cantidad de item:" + i.getCantidad());
//
//                if (!(new BigDecimal(cantGenerar)).equals(i.getCantidad())){
//                    throw new ExcepcionGeneralSistema("La cantidad a ingresar no coincide con la cantidad de números de serie");
//                }
//
//                if (i.getItemSerie() == null) {
//                    i.setItemSerie(new ArrayList<ItemProductoProduccionSerie>());
//                }
//
//                for (int x = serieDesde; x <= serieHasta; x++) {
//
//                    ItemProductoProduccionSerie d = itemMovimientoRN.nuevoItemSerie(i);
//
//                    d.setCantid(BigDecimal.ONE);
//                    d.setCantst(BigDecimal.ONE);
//                    d.setNserie("" + x);
//
//                    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
//                    d.setNfecha(formateador.format(m.getFchmov()));
//
//                    //Agrega el detalle al producto
//                    d.setItemProducto(i);
//                    i.getItemSerie().add(d);
//
//                }
//            }
//        }
    }

    /**
     * Generamos los items del movimientos en base a los items pendientes
     *
     * @param m Movimiento de produccción
     * @param itemsPendiente items pendientes necesarios para generar el
     * movimiento
     * @throws ExcepcionGeneralSistema
     *
     */
    public void generarItemsFromPendiente(MovimientoProduccion m, List<PendienteProduccionDetalle> itemsPendiente) throws ExcepcionGeneralSistema {

        if (itemsPendiente.isEmpty()) {
//            System.out.println("Detalle de pendientes vacío");
            return;
        }

        int cantSel = 0;
        for (PendienteProduccionDetalle i : itemsPendiente) {

            if (i.isSeleccionado()) {

                cantSel++;
                ItemProductoProduccion nItem = itemMovimientoRN.nuevoItemProducto(m);

                nItem.setProducto(i.getProducto());
                nItem.setProductoOriginal(i.getProducto());
                nItem.setUnidadMedida(i.getUnidadMedida());

                if (i.getCantidad() != null && i.getCantidad().compareTo(BigDecimal.ZERO) > 0) {
                    nItem.setCantidad(i.getCantidad());
                    nItem.setCantidadOriginal(i.getCantidad());
                    nItem.setCantidadStock(i.getCantidad());
                } else {
                    nItem.setCantidad(i.getPendiente());
                    nItem.setCantidadOriginal(i.getPendiente());
                    nItem.setCantidadStock(i.getPendiente());
                }

                nItem.setActualizaStock(i.getStocks());

                if (i.getFormul() != null && !i.getFormul().isEmpty()) {
                    ComposicionFormula composicionFormula = composicionFormulaRN.getComprosicionFormula(i.getArtcod(), i.getFormul());
                    nItem.setComposicionFormula(composicionFormula);
                }
                //Si tiene asignado la toma de numero de serie se lo asignamos
                //Ahora toma nro de serie de hora de ruta
//                if (m.getCircuito().getTomaNumeroSerieDesdeParteProduccion().equals("S")
//                        && m.getTipoMovimiento().equals(TipoMovimientoProduccion.PP)) {
//
//                    nItem.setNserie(String.format("%06d", i.getNrofor()));
//                }
                generarItemDetalle(nItem);
                nItem.setTodoOk(true);
                //Agregamos el item a la lista
                m.getItemsProducto().add(nItem);

                //-----------------------------------------------------------
                //Verificamos si el circuito aplica a items pendietnes
                if (m.getCircuito().getNoCancelaPendiente().equals("N")) {

                    //Genera los items aplicación
                    ItemAplicacionProduccion iApl = itemMovimientoRN.nuevoItemAplicacion(m, i);

                    iApl.setProducto(i.getProducto());
                    iApl.setProductoOriginal(i.getProducto());
                    iApl.setUnidadMedida(i.getUnidadMedida());
                    iApl.setCantidad(i.getPendiente().negate());
                    iApl.setCantidadOriginal(i.getPendiente().negate());
                    iApl.setCantidadStock(i.getPendiente());
                    iApl.setActualizaStock(i.getStocks());

                    if (i.getFormul() != null && !i.getFormul().isEmpty()) {
                        ComposicionFormula composicionFormula = composicionFormulaRN.getComprosicionFormula(i.getArtcod(), i.getFormul());
                        iApl.setComposicionFormula(composicionFormula);
                    }

                    iApl.setTodoOk(true);
                    //Agregamos el item a la lista
                    m.getItemsProductoAplicacion().add(iApl);
                }
            }
        }
        if (cantSel == 0) {
            throw new ExcepcionGeneralSistema("No ha seleccionado ningún producto");
        }
    }
    
    /**
     * Generamos los items del movimientos en base a los items del movmiento original
     *
     * @param m Movimiento de produccción
     * @param itemsMovimiento items pendientes necesarios para generar el
     * movimiento
     * @throws ExcepcionGeneralSistema
     *
     */
    public void generarItemsFromItemMovimiento(MovimientoProduccion m, List<ItemMovimientoProduccion> itemsMovimiento) throws ExcepcionGeneralSistema {

        if (itemsMovimiento.isEmpty()) {
//            System.out.println("Detalle de pendientes vacío");
            return;
        }

        int cantSel = 0;
        for (ItemMovimientoProduccion i : itemsMovimiento) {

            if (i.getProduccion()!=null && i.getProduccion().compareTo(BigDecimal.ZERO)> 0) {
                
                cantSel++;
                ItemProductoProduccion nItem = itemMovimientoRN.nuevoItemProducto(m);

                nItem.setProducto(i.getProducto());
                nItem.setProductoOriginal(i.getProducto());
                nItem.setUnidadMedida(i.getUnidadMedida());

                if (i.getProduccion() != null && i.getProduccion().compareTo(BigDecimal.ZERO) > 0) {
                    nItem.setCantidad(i.getProduccion());
                    nItem.setCantidadOriginal(i.getCantidad());
                    nItem.setCantidadStock(i.getProduccion());
                } else {
                    nItem.setCantidad(i.getPendiente());
                    nItem.setCantidadOriginal(i.getCantidad());
                    nItem.setCantidadStock(i.getPendiente());
                }

                nItem.setActualizaStock(i.getActualizaStock());

                if (i.getComposicionFormula()!= null) {                    
                    nItem.setComposicionFormula(i.getComposicionFormula());
                }
                //Si tiene asignado la toma de numero de serie se lo asignamos
                //Ahora toma nro de serie de hora de ruta
//                if (m.getCircuito().getTomaNumeroSerieDesdeParteProduccion().equals("S")
//                        && m.getTipoMovimiento().equals(TipoMovimientoProduccion.PP)) {
//
//                    nItem.setNserie(String.format("%06d", i.getNrofor()));
//                }
                generarItemDetalle(nItem);
                nItem.setTodoOk(true);
                //Agregamos el item a la lista
                m.getItemsProducto().add(nItem);

                //-----------------------------------------------------------
                //Verificamos si el circuito aplica a items pendietnes
                if (m.getCircuito().getNoCancelaPendiente().equals("N")) {

                    //Genera los items aplicación
                    ItemAplicacionProduccion iApl = itemMovimientoRN.nuevoItemAplicacion(m, i);

                    iApl.setProducto(i.getProducto());
                    iApl.setProductoOriginal(i.getProducto());
                    iApl.setUnidadMedida(i.getUnidadMedida());
                    iApl.setCantidad(i.getProduccion().negate());
                    iApl.setCantidadOriginal(i.getProduccion().negate());
                    iApl.setCantidadStock(i.getProduccion());
                    iApl.setActualizaStock(i.getActualizaStock());

                    if (i.getComposicionFormula()!= null) {                        
                        iApl.setComposicionFormula(i.getComposicionFormula());
                    }

                    iApl.setTodoOk(true);
                    //Agregamos el item a la lista
                    m.getItemsProductoAplicacion().add(iApl);
                }
            }
        }
        if (cantSel == 0) {
            throw new ExcepcionGeneralSistema("No ha seleccionado ningún producto");
        }
    }

    /**
     * Los items de movimiento de inventario se generan a partir de los items
     * aperturas de acuerdo a los número de serie ingresados
     *
     * @param m Movimiento de producción
     * @throws ExcepcionGeneralSistema
     */
//    private void generarItemsMovimientoInventario(MovimientoProduccion m) throws ExcepcionGeneralSistema {
//
//        if(m.getMovimientoStock()==null){
//
//            throw new ExcepcionGeneralSistema("El circuito define que actualiza stock, pero el comprobante no fue generado o no está configurado");
//        }
//
//        m.getMovimientoInventario().getItemProducto().clear();
//
//        for(ItemProductoProduccion ip: m.getItemProducto()){
//
//            for(ItemDetalleItemMovimientoProduccion id:ip.getItemDetalle()){
//
//                ST_ItemProducto is = new MovimientoInventarioRN().nuevoItemProducto(m.getMovimientoInventario());
//                is.setCantid(id.getCantidad());
//                is.setProducto(id.getProducto());
//                System.out.println("Serie:" + id.getNserie());
//                is.setNserie(id.getNserie());
//                is.setProductoPRD(id.getProducto());
//                m.getMovimientoInventario().getItemProducto().add(is);
//            }
//        }
//    }
    public void generarItemsAnulacion(MovimientoProduccion m) throws ExcepcionGeneralSistema {

        //Verificamos si el circuito es de anulacion y revierte pendiente
        if (m.getCircuito().getEsAnulacion().equals("S")) {

            //Si es hoja de ruta solo aplicamos los items
            if (m.getTipoMovimiento().equals(TipoMovimientoProduccion.OP)) {
                return;
            }

            if (m.getItemsProductoAplicacion() == null) {
                return;
            }

            //Si la lista de items de anulación es nula, creamos una nueva
            if (m.getItemsProductoAnulacion() == null) {
                m.setItemsProductoAnulacion(new ArrayList<ItemAnulacionProduccion>());
            }

            for (ItemAplicacionProduccion iapl : m.getItemsProductoAplicacion()) {

                ItemAnulacionProduccion ianu = itemMovimientoRN.nuevoItemAnulacion(m);
                ianu.setProducto(iapl.getProducto());
                ianu.setProductoOriginal(iapl.getProducto());
                ianu.setUnidadMedida(iapl.getProducto().getUnidadDeMedida());
                ianu.setCantidad(iapl.getCantidad().negate());
                ianu.setCantidadStock(iapl.getCantidad().negate());
                ianu.setCantidadOriginal(iapl.getCantidad().negate());

                if (iapl.getComposicionFormula() != null) {
                    ianu.setComposicionFormula(iapl.getComposicionFormula());
                }

//                if(iapl.getComposicionFormula()!=null){
//                    ianu.getComposicionFormula(iapl.getComposicionFormula());
//                }
                //ianu.setItemAplicacion(iapl);
                m.getItemsProductoAnulacion().add(ianu);
            }

        }
    }

    public void sincronizarCantidades(MovimientoProduccion m) {

        for (ItemProductoProduccion i : m.getItemsProducto()) {

            if (i.getCantidad() == null || i.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                JsfUtil.addErrorMessage("Ingrese un valor de cantidad válido. Mayor a 0");
                return;
            }

            if (m.getItemsProductoAplicacion() != null) {

                for (ItemAplicacionProduccion a : m.getItemsProductoAplicacion()) {

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

    public MovimientoProduccion getMovimiento(Integer id, boolean calculaPendienteItem) {

        MovimientoProduccion m = produccionDAO.getMovimiento(id);

        if (m == null) {
            return null;
        }

        Map<String, String> filtro = new HashMap<String, String>();

        if (calculaPendienteItem) {

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
                filtro.put("artcod = ", "'"+i.getProducto().getCodigo()+"'");

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
                filtro.put("artcod =", "'"+i.getProducto().getCodigo()+"'");

                PendienteProduccionDetalle pd = produccionDAO.getPendienteDetalle(filtro);
                
                if (pd != null) {
                    i.setPendiente(pd.getPendiente());
                } 
            }
        }

        return m;
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

        if (mov.getItemsProductoAplicacion() != null) {
            for (ItemAplicacionProduccion a : mov.getItemsProductoAplicacion()) {

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
            ItemAplicacionProduccion remove = mov.getItemsProductoAplicacion().remove(indiceItemAplicacion);
            if (remove != null) {
                fItemBorrado = true;
            }
        }

        return fItemBorrado;
    }

    public boolean eliminarItemDetalle(ItemProductoProduccion ip, ItemDetalleItemMovimientoProduccion nItem) throws ExcepcionGeneralSistema {

        boolean fItemBorrado = false;
        int i = 0;
        int indiceItemProducto = -1;

        //Verificamos que siempre quede 1 items detalle
        if (ip.getItemDetalle().size() == 1) {

            throw new ExcepcionGeneralSistema("No es posible eliminar item de apertura, la cantidad mínima es un item");
        }

        //Buscamos el indice del item a borrar
        for (ItemDetalleItemMovimientoProduccion id : ip.getItemDetalle()) {

            if (id.getProducto() != null) {
                if (id.equals(nItem)) {
                    indiceItemProducto = i;
                }
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

            for (ItemAplicacionProduccion ia : m.getItemsProductoAplicacion()) {

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

            if (!im.isTodoOk()) {
                indiceBorrar[i] = "S";
            }
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
    
    public boolean tengoItemsConCantidad(Object itemsPendientes) {

        if (itemsPendientes == null || ((List<ItemMovimientoProduccion>)itemsPendientes).isEmpty()) {
            return false;
        }

        for (ItemMovimientoProduccion i : (List<ItemMovimientoProduccion>)itemsPendientes) {

            if (i.getPendiente()!= null && i.getPendiente().compareTo(BigDecimal.ZERO) > 0) {
                return true;
            }
        }
        return false;
    }

}
