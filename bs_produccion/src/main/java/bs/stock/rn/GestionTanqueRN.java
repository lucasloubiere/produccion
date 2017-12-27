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
import bs.stock.modelo.Deposito;
import bs.stock.modelo.GestionTanque;
import bs.stock.modelo.ItemGestionTanque;
import bs.stock.modelo.ItemProductoStock;
import bs.stock.modelo.MovimientoStock;
import bs.stock.modelo.Producto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
    private DepositoRN depositoRN;
    @EJB
    private MovimientoStockRN movimientoStockRN;
    @EJB
    private StockRN stockRN;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public synchronized void guardar(GestionTanque gestionTanque) throws Exception {

        //Validamos que se pueda guardar el comprobante
        controlComprobante(gestionTanque, false);

        if (gestionTanque.getId() != null) {

            List<Integer> idsMovimientos = new ArrayList<Integer>();

            for (MovimientoStock m : gestionTanque.getMovimientosStock()) {

                idsMovimientos.add(m.getId());
            }

            for (Integer id : idsMovimientos) {

                movimientoStockRN.eliminarMovimiento(id);
            }
            
            gestionTanque.getMovimientosStock().clear();
        }

        generarMovimientosStock(gestionTanque);

        if (gestionTanque.getId() == null) {

            Integer ultimoNumero = formularioRN.tomarProximoNumero(gestionTanque.getFormulario());
            gestionTanque.setNumeroFormulario(ultimoNumero);
            gestionTanqueDAO.crear(gestionTanque);

        } else {
            
            gestionTanqueDAO.editar(gestionTanque);
            
            GestionTanque gestionTanqueSiguiente = gestionTanqueDAO.getProximaGestion(gestionTanque);
            
            if(gestionTanqueSiguiente!=null){
                guardar(gestionTanqueSiguiente);
            }            
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

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 4);
        c.set(Calendar.MINUTE, 0);

        m.setFechaMovimiento(c.getTime());
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

//        if (m.getId() != null) {
//            throw new ExcepcionGeneralSistema("No es posible modificar un comprobante de stock");
//        }
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

//            gestionAnterior = new GestionTanque();
//            gestionAnterior.setFechaMovimiento(new Date());
        }

        return gestionAnterior;

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void generarMovimientosStock(GestionTanque gestionTanque) throws ExcepcionGeneralSistema, Exception {

        for (ItemGestionTanque itemGestion : gestionTanque.getItems()) {

            if (itemGestion.getProducto() == null) {
                continue;
            }

            Calendar c = Calendar.getInstance();
            c.setTime(gestionTanque.getFechaMovimiento());
            c.add(Calendar.MINUTE, -1);

            //Mayor a 0
            if (itemGestion.getStockCalculado().compareTo(BigDecimal.ZERO) == 1) {

                MovimientoStock movIngreso = movimientoStockRN.nuevoMovimiento("ST", "IPA", "0001");

                movIngreso.setFechaMovimiento(c.getTime());
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

                movEgreso.setFechaMovimiento(c.getTime());
                movEgreso.setDeposito(itemGestion.getDeposito());
                movEgreso.setNoValidaStockDisponible(true);

                ItemProductoStock itemProducto = movEgreso.getItemsProducto().get(movEgreso.getItemsProducto().size() - 1);

                itemProducto.setProducto(itemGestion.getProducto());
                itemProducto.setUnidadMedida(itemGestion.getProducto().getUnidadDeMedida());
                itemProducto.setDeposito(itemGestion.getDeposito());

                itemProducto.setAtributo1("VICENTIN");
                itemProducto.setAtributo2("N/D");

                itemProducto.setCantidad(itemGestion.getStockCalculado().negate());
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

    public void obtenerDatos(GestionTanque gestionTanque) throws ExcepcionGeneralSistema {

        if (gestionTanque.getSector() == null) {
            throw new ExcepcionGeneralSistema("Debe seleccionar el sector");
        }

        Map<String, String> filtro = gestionTanqueDAO.getFiltro();
        filtro.clear();
        filtro.put("calculaStock IN ", "('M','F')");
        filtro.put("sector.codigo = ", "'" + gestionTanque.getSector().getCodigo() + "'");

        List<Deposito> depositos = depositoRN.getDepositoByBusqueda(filtro, "", true, 0);

        /**
         * Obtenemos la última gestión guardada, anterior a la fecha de la
         * actual gestión.
         */
        GestionTanque gestionAnterior = getUltimoRegistro();// 

        if (gestionAnterior == null) {
            gestionAnterior = new GestionTanque();
            gestionAnterior.setFechaMovimiento(gestionTanque.getFechaMovimiento());
        }

        Calendar c = Calendar.getInstance();
        c.setTime(gestionAnterior.getFechaMovimiento());
        c.add(Calendar.MINUTE, 1);
        gestionAnterior.setFechaMovimiento(c.getTime());

        gestionTanque.getItems().clear();

        for (Deposito deposito : depositos) {

            ItemGestionTanque item = new ItemGestionTanque();
            item.setDeposito(deposito);

            Producto producto = stockRN.getProductoByDepositoConStock(deposito);
            item.setProducto(producto);

            if (deposito != null && producto != null) {

                item.setStockInicial(movimientoStockRN.getStockAFecha(producto, deposito, gestionAnterior.getFechaMovimiento()));

                BigDecimal transferencias = movimientoStockRN.getCantidadFromMovimiento("T", producto, deposito, gestionAnterior.getFechaMovimiento(), gestionTanque.getFechaMovimiento());
                BigDecimal ajustes = movimientoStockRN.getCantidadFromMovimiento("A", producto, deposito, gestionAnterior.getFechaMovimiento(), gestionTanque.getFechaMovimiento());
                BigDecimal ingresos = movimientoStockRN.getCantidadFromMovimiento("I", producto, deposito, gestionAnterior.getFechaMovimiento(), gestionTanque.getFechaMovimiento());
                BigDecimal egresos = movimientoStockRN.getCantidadFromMovimiento("E", producto, deposito, gestionAnterior.getFechaMovimiento(), gestionTanque.getFechaMovimiento());

                if (ingresos == null) {
                    ingresos = BigDecimal.ZERO;
                }
                if (egresos == null) {
                    egresos = BigDecimal.ZERO;
                }
                if (transferencias == null) {
                    transferencias = BigDecimal.ZERO;
                }
                if (ajustes == null) {
                    ajustes = BigDecimal.ZERO;
                }

                if (transferencias.compareTo(BigDecimal.ZERO) > 0) {
                    ingresos = ingresos.add(transferencias);
                }

                if (ajustes.compareTo(BigDecimal.ZERO) > 0) {
                    ingresos = ingresos.add(ajustes);
                }

                if (transferencias.compareTo(BigDecimal.ZERO) < 0) {
                    egresos = egresos.add(transferencias);
                }

                if (ajustes.compareTo(BigDecimal.ZERO) < 0) {
                    egresos = egresos.add(ajustes);
                }

                item.setIngresos(ingresos);
                item.setEgresos(egresos);

                calcularStock(item);

                if (item.getStockInicial().compareTo(BigDecimal.ZERO) > 0
                        || item.getIngresos().compareTo(BigDecimal.ZERO) > 0
                        || item.getEgresos().compareTo(BigDecimal.ZERO) > 0) {

                    item.setDepositoConStock(true);

                } else {
                    item.setProducto(null);
                }
            }

            item.setGestionTanque(gestionTanque);
            gestionTanque.getItems().add(item);
        }

        ordenarItems(gestionTanque);
    }
    
    public void calcularStock(ItemGestionTanque i) {

        if (i.getStockInicial() == null) {
            i.setStockInicial(BigDecimal.ZERO);
        }
        if (i.getIngresos() == null) {
            i.setIngresos(BigDecimal.ZERO);
        }
        if (i.getEgresos() == null) {
            i.setEgresos(BigDecimal.ZERO);
        }

        if (i.getMedida() == null) {
            i.setMedida(BigDecimal.ZERO);
        }

        if (i.getDeposito().getCalculaStock().equals("M")) {

            if (i.getMedida().compareTo(BigDecimal.ZERO) > 0) {
                i.setStockFinal((i.getMedida().multiply(i.getDeposito().getConstante()).add(i.getDeposito().getSumando())).divide(i.getDeposito().getDivisor(), 2, RoundingMode.HALF_UP));
                i.setStockFinal(i.getStockFinal().multiply(new BigDecimal("1000")));
            } else {
                i.setStockFinal(BigDecimal.ZERO);
            }
        }

        i.setStockCalculado(i.getStockInicial().negate().add(i.getIngresos().negate()).add(i.getEgresos().negate()).add(i.getStockFinal()));
    }
    
    public void ordenarItems(GestionTanque gestionTanque) {

        Collections.sort(gestionTanque.getItems(), new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                //return new Integer(p1.getEdad()).compareTo(new Integer(p2.getEdad()));
                ItemGestionTanque item1 = (ItemGestionTanque) o1;
                ItemGestionTanque item2 = (ItemGestionTanque) o2;

                String cod1 = (item1.getProducto() == null ? "99999" : item1.getProducto().getCodigo());
                String cod2 = (item2.getProducto() == null ? "99999" : item2.getProducto().getCodigo());

                return (new Integer(cod1)).compareTo(new Integer(cod2));

            }
        });
    }

    public void asignarProducto(ItemGestionTanque itemGestionTanque, Producto producto) throws ExcepcionGeneralSistema {

        if (itemGestionTanque.getDeposito() == null) {
            throw new ExcepcionGeneralSistema("El deposito en el item no puede se nulo");
        }

        if (itemGestionTanque.isDepositoConStock()) {
            throw new ExcepcionGeneralSistema("El deposito ya tienen producto con stock en este deposito");
        }

        itemGestionTanque.setProducto(producto);

    }

}
