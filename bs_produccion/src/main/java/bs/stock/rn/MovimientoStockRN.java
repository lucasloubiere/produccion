/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.rn;

import bs.administracion.rn.ModuloRN;
import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.modelo.Formulario;
import bs.global.modelo.Moneda;
import bs.global.modelo.Sucursal;
import bs.global.rn.FormularioRN;
import bs.global.rn.MonedaRN;
import bs.global.rn.SucursalRN;
import bs.produccion.modelo.ItemDetalleMovimientoProduccion;
import bs.produccion.modelo.ItemProductoProduccion;
import bs.produccion.modelo.MovimientoProduccion;
import bs.stock.dao.ComprobanteStockDAO;
import bs.stock.dao.MovimientoStockDAO;
import bs.stock.modelo.ComprobanteStock;
import bs.stock.modelo.Deposito;
import bs.stock.modelo.ItemMovimientoStock;
import bs.stock.modelo.ItemProductoStock;
import bs.stock.modelo.ItemTransferenciaStock;
import bs.stock.modelo.MovimientoStock;
import bs.stock.modelo.Producto;
import bs.stock.modelo.Stock;
import bs.stock.modelo.TipoItemMovimiento;
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
 * @author Claudio
 */
@Stateless
public class MovimientoStockRN {

    //@EJB private MonedaRN monedaRN;
    @EJB
    private MovimientoStockDAO inventarioDAO;
    @EJB
    private ComprobanteStockDAO comprobanteDAO;
    @EJB
    private StockRN stockRN;
    @EJB
    private FormularioRN formularioRN;
    @EJB
    private SucursalRN sucursalRN;
    @EJB
    private ModuloRN moduloRN;
    @EJB
    private MonedaRN monedaRN;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public synchronized void guardar(MovimientoStock movimiento) throws Exception {

        borrarItemsNoValidos(movimiento);
        generarItemTransferencia(movimiento);
        asignarDepositoItems(movimiento);
        asignarCantidadStock(movimiento);

        //Validamos que se pueda guardar el comprobante
        controlComprobante(movimiento, false);

        if (movimiento.getId() == null) {

            generarStock(movimiento);

            if (!movimiento.isNoSincronizaNumeroFormulario() && movimiento.getNumeroFormulario() > 0) {

                Integer ultimoNumero = formularioRN.tomarProximoNumero(movimiento.getFormulario());
                movimiento.setNumeroFormulario(ultimoNumero);

            }
            inventarioDAO.crear(movimiento);
        } else {
            inventarioDAO.editar(movimiento);
            stockRN.recalcularStock();

        }

        movimiento.setPersistido(true);
    }

    /*
     * Se utiliza para generar movimientos desde el modulo de stock
     * este metodo incremenda el nro de formulario
     */
    public MovimientoStock nuevoMovimiento(String MODST, String CODST, String SUCURS) throws ExcepcionGeneralSistema {

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

        MovimientoStock m = crearMovimiento(comprobante, formulario, sucursal);

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
    public MovimientoStock nuevoMovimientoAutomatico(ComprobanteStock comprobante, Formulario formulario, Sucursal sucursal) throws ExcepcionGeneralSistema {

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
    private MovimientoStock crearMovimiento(ComprobanteStock comprobante, Formulario formulario, Sucursal sucursal) throws ExcepcionGeneralSistema {

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

        MovimientoStock m = new MovimientoStock();
        Moneda moneda = monedaRN.getMoneda("USD");
        BigDecimal cotizacion = monedaRN.getCotizacionDia("USD");

        m.setPersistido(false);
        m.setFormulario(formulario);
        m.setComprobante(comprobante);
        m.setNumeroFormulario(formulario.getUltimoNumero() + 1);
        m.setFechaMovimiento(new Date());
        m.setSucursal(sucursal);
        m.setTipoMovimiento(comprobante.getTipoMovimiento());
        m.setMonedaSecundaria(moneda);
        m.setMonedaRegistracion(monedaRN.getMoneda("ARS"));
        m.setCotizacion(cotizacion);

        if (comprobante.getDeposito() != null) {
            m.setDeposito(comprobante.getDeposito());
        }

        if (comprobante.getDepositoTransferencia() != null) {
            m.setDepositoTransferencia(comprobante.getDepositoTransferencia());
        }

        //Generamos el item producto vacío
        m.setItemsProducto(new ArrayList<ItemProductoStock>());
        m.getItemsProducto().add(nuevoItemProducto(m));

        return m;
    }

    public ItemProductoStock nuevoItemProducto(MovimientoStock m) {

        return (ItemProductoStock) nuevoItemMovimiento(TipoItemMovimiento.P, m);

    }

    public ItemTransferenciaStock nuevoItemTransferencia(MovimientoStock m) {

        return (ItemTransferenciaStock) nuevoItemMovimiento(TipoItemMovimiento.T, m);

    }

    private ItemMovimientoStock nuevoItemMovimiento(TipoItemMovimiento ti, MovimientoStock m) {

        ItemMovimientoStock nItem;

        if (ti.equals(TipoItemMovimiento.P)) {
            nItem = new ItemProductoStock();
        } else {
            nItem = new ItemTransferenciaStock();
        }

//        nItem.setNroitm(m.getItemsProducto().size()+1);
//        nItem.setSucursal(m.getSucursal().getCodigo());
//          nItem.setFechaMovimiento(m.getFechaMovimiento());
//        nItem.setMonedaSecundaria(m.getMonedaSecundaria());
//        nItem.setCotizacion(m.getCotizacion());
        nItem.setAtributo1(m.getAtributo1() != null ? m.getAtributo1() : "");
        nItem.setAtributo2(m.getAtributo2() != null ? m.getAtributo2() : "");
        nItem.setAtributo3(m.getAtributo3() != null ? m.getAtributo3() : "");
        nItem.setAtributo4(m.getAtributo4() != null ? m.getAtributo4() : "");
        nItem.setAtributo5(m.getAtributo5() != null ? m.getAtributo5() : "");
        nItem.setAtributo6(m.getAtributo6() != null ? m.getAtributo6() : "");
        nItem.setAtributo7(m.getAtributo7() != null ? m.getAtributo7() : "");

        nItem.setMovimiento(m);

        return nItem;
    }

    /**
     * Validaciones previas a guardar el movimiento
     *
     * @param m Movimiento de stock
     * @param permiteVacio permite guardar comprobante vacío
     * @throws bs.global.excepciones.ExcepcionGeneralSistema
     */
    public void controlComprobante(MovimientoStock m, boolean permiteVacio) throws ExcepcionGeneralSistema {

//        if (m.getId() != null) {
//            throw new ExcepcionGeneralSistema("No es posible modificar un comprobante de stock");
//        }
//        Modulo modulo  = null;
//        
//        if (!m.getFechaMovimiento().after(modulo.getfechahabilitacionDesde) & !m.getFechaMovimiento().before(modulo.getfechahabilitacionHasta)){
//            throw new ExcepcionGeneralSistema("La fecha del movimiento no está dentro del período habilitado");
//        }
        if (!permiteVacio && m.getItemsProducto().isEmpty()) {
            throw new ExcepcionGeneralSistema("El detalle está vacío, no es posible guardar el comprobante de stock");
        }

        //Verificamos que el deposito ingreso siempre esté cargado
        if (m.getDeposito() == null) {
            throw new ExcepcionGeneralSistema("El depósito no puede ser nulo");
        }

        //Si es transferencia el deposito de egreso tiene que estar cargado
        if (m.getTipoMovimiento().equals("T")) {

            if (m.getDepositoTransferencia() == null) {
                throw new ExcepcionGeneralSistema("El depósito para transferencia no puede ser nulo");
            }

            if (m.getDeposito().equals(m.getDepositoTransferencia())) {
                throw new ExcepcionGeneralSistema("El depósito de egreso y de ingreso no pueden ser iguales");
            }
        }

        if (m.getFechaMovimiento() == null) {
            throw new ExcepcionGeneralSistema("La fecha no puede estar en blanco");
        }

        if (m.getSucursal() == null) {
            throw new ExcepcionGeneralSistema("La sucursal no puede estar en blanco");
        }

        controlItemsProducto(m);

    }

    public void puedoAgregarItem(MovimientoStock m, ItemMovimientoStock nItem) throws ExcepcionGeneralSistema {

        if (nItem == null) {
            throw new ExcepcionGeneralSistema("No se ha creado el item");
        }

        if (m.getDeposito() == null) {
            throw new ExcepcionGeneralSistema("Seleccione el depósito");
        }

        if (nItem.getProducto() == null) {
            throw new ExcepcionGeneralSistema("Seleccione un producto para agregar al comprobante");
        }

        if (m.getTipoMovimiento().equals("I")
                || m.getTipoMovimiento().equals("E")
                || m.getTipoMovimiento().equals("T")) {

            if (nItem.getCantidad() == null || nItem.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ExcepcionGeneralSistema("Ingrese un valor de cantidad mayor a 0 para " + nItem.getProducto().getDescripcion());
            }
        }

        //Si es ajuste solamente validamos que sea distinto de cero
        if (m.getTipoMovimiento().equals("A")) {

            if (nItem.getCantidad() == null || nItem.getCantidad().compareTo(BigDecimal.ZERO) == 0) {
                throw new ExcepcionGeneralSistema("Ingrese un valor de cantidad distinto a 0 para " + nItem.getProducto().getDescripcion());
            }
        }

        if (m.getTipoMovimiento().equals("T")) {

            if (m.getDepositoTransferencia() == null) {
                throw new ExcepcionGeneralSistema("Seleccione el depósito de egreso");
            }
        }

        //Control de atributos de stock
        if (nItem.getProducto() != null
                && nItem.getProducto().getAdministraAtributo1().equals("S")
                && nItem.getAtributo1().isEmpty()) {

            throw new ExcepcionGeneralSistema("Ingrese el atributo 1 para el producto " + nItem.getProducto().getDescripcion());
        }

        if (nItem.getProducto() != null
                && nItem.getProducto().getAdministraAtributo2().equals("S")
                && nItem.getAtributo2().isEmpty()) {

            throw new ExcepcionGeneralSistema("Ingrese el atributo 2 para el producto " + nItem.getProducto().getDescripcion());
        }

        if (nItem.getProducto() != null
                && nItem.getProducto().getAdministraAtributo3().equals("S")
                && nItem.getAtributo3().isEmpty()) {

            throw new ExcepcionGeneralSistema("Ingrese el atributo 3 para el producto " + nItem.getProducto().getDescripcion());
        }

        if (nItem.getProducto() != null
                && nItem.getProducto().getAdministraAtributo4().equals("S")
                && nItem.getAtributo4().isEmpty()) {

            throw new ExcepcionGeneralSistema("Ingrese el atributo 4 para el producto " + nItem.getProducto().getDescripcion());
        }

        if (nItem.getProducto() != null
                && nItem.getProducto().getAdministraAtributo5().equals("S")
                && nItem.getAtributo5().isEmpty()) {

            throw new ExcepcionGeneralSistema("Ingrese el atributo 5 para el producto " + nItem.getProducto().getDescripcion());
        }

        if (nItem.getProducto() != null
                && nItem.getProducto().getAdministraAtributo6().equals("S")
                && nItem.getAtributo6().isEmpty()) {

            throw new ExcepcionGeneralSistema("Ingrese el atributo 6 para el producto " + nItem.getProducto().getDescripcion());
        }

        if (nItem.getProducto() != null
                && nItem.getProducto().getAdministraAtributo7().equals("S")
                && nItem.getAtributo7().isEmpty()) {

            throw new ExcepcionGeneralSistema("Ingrese el atributo 7 para el producto " + nItem.getProducto().getDescripcion());
        }

        for (ItemProductoStock ip : m.getItemsProducto()) {

            if (ip.getProducto() != null
                    && ip.getProducto().equals(nItem.getProducto())
                    && ip.getAtributo1().equals(nItem.getAtributo1())
                    && ip.getAtributo2().equals(nItem.getAtributo2())
                    && ip.getAtributo3().equals(nItem.getAtributo3())
                    && ip.getAtributo4().equals(nItem.getAtributo4())
                    && ip.getAtributo5().equals(nItem.getAtributo5())
                    && ip.getAtributo6().equals(nItem.getAtributo6())
                    && ip.getAtributo7().equals(nItem.getAtributo7())
                    && ip.isTodoOk()) {

                String mensaje = "El producto "
                        + nItem.getProducto().getDescripcion()
                        + (nItem.getProducto().getAdministraAtributo1().equals("S") ? "| Atributo1 " + nItem.getAtributo1() : "")
                        + (nItem.getProducto().getAdministraAtributo2().equals("S") ? "| Atributo2 " + nItem.getAtributo2() : "")
                        + (nItem.getProducto().getAdministraAtributo3().equals("S") ? "| Atributo3 " + nItem.getAtributo3() : "")
                        + (nItem.getProducto().getAdministraAtributo4().equals("S") ? "| Atributo4 " + nItem.getAtributo4() : "")
                        + (nItem.getProducto().getAdministraAtributo5().equals("S") ? "| Atributo5 " + nItem.getAtributo5() : "")
                        + (nItem.getProducto().getAdministraAtributo6().equals("S") ? "| Atributo6 " + nItem.getAtributo6() : "")
                        + (nItem.getProducto().getAdministraAtributo7().equals("S") ? "| Atributo7 " + nItem.getAtributo7() : "")
                        + " ya existe en el comprobante";

                throw new ExcepcionGeneralSistema(mensaje);
            }

            if (!ip.getProducto().getGestionaStock().equals("S")) {
                throw new ExcepcionGeneralSistema("El producto " + nItem.getProducto().getDescripcion() + " no gestiona stock");
            }

            if (m.getTipoMovimiento().equals("E")
                    && !m.isNoValidaStockDisponible()
                    && m.getDeposito().getSigno().equals("+")) {

                nItem.setStocks(nItem.getCantidad().negate());
                Stock s = new Stock(nItem);

                if (!stockRN.isProductoDisponible(s)) {

                    String mensaje = "Stock insuficiente. Hay " + s.getStockDisponible() + " " + s.getProducto().getUnidadDeMedida().getCodigo()
                            + " disponible/s para " + nItem.getProducto().getDescripcion() + " en Deposito " + nItem.getDeposito().getDescripcion()
                            + (nItem.getProducto().getAdministraAtributo1().equals("S") ? "| Atributo1 " + nItem.getAtributo1() : "")
                            + (nItem.getProducto().getAdministraAtributo2().equals("S") ? "| Atributo2 " + nItem.getAtributo2() : "")
                            + (nItem.getProducto().getAdministraAtributo3().equals("S") ? "| Atributo3 " + nItem.getAtributo3() : "")
                            + (nItem.getProducto().getAdministraAtributo4().equals("S") ? "| Atributo4 " + nItem.getAtributo4() : "")
                            + (nItem.getProducto().getAdministraAtributo5().equals("S") ? "| Atributo5 " + nItem.getAtributo5() : "")
                            + (nItem.getProducto().getAdministraAtributo6().equals("S") ? "| Atributo6 " + nItem.getAtributo6() : "")
                            + (nItem.getProducto().getAdministraAtributo7().equals("S") ? "| Atributo7 " + nItem.getAtributo7() : "");

                    throw new ExcepcionGeneralSistema(mensaje);
                }
            }

            if (m.getTipoMovimiento().equals("T")
                    && m.getDepositoTransferencia() != null
                    && !m.isNoValidaStockDisponible()
                    && m.getDepositoTransferencia().getSigno().equals("+")) {

                nItem.setStocks(nItem.getCantidad().negate());
                Stock s = new Stock(nItem);

                s.setDeposi(m.getDepositoTransferencia().getCodigo());
                s.setDeposito(m.getDepositoTransferencia());

                if (!stockRN.isProductoDisponible(s)) {

                    String mensaje = "Stock insuficiente. Hay " + s.getStockDisponible() + " " + s.getProducto().getUnidadDeMedida().getCodigo()
                            + " disponible/s para " + nItem.getProducto().getDescripcion() + " en Deposito " + nItem.getDeposito().getDescripcion()
                            + (nItem.getProducto().getAdministraAtributo1().equals("S") ? "| Atributo1 " + nItem.getAtributo1() : "")
                            + (nItem.getProducto().getAdministraAtributo2().equals("S") ? "| Atributo2 " + nItem.getAtributo2() : "")
                            + (nItem.getProducto().getAdministraAtributo3().equals("S") ? "| Atributo3 " + nItem.getAtributo3() : "")
                            + (nItem.getProducto().getAdministraAtributo4().equals("S") ? "| Atributo4 " + nItem.getAtributo4() : "")
                            + (nItem.getProducto().getAdministraAtributo5().equals("S") ? "| Atributo5 " + nItem.getAtributo5() : "")
                            + (nItem.getProducto().getAdministraAtributo6().equals("S") ? "| Atributo6 " + nItem.getAtributo6() : "")
                            + (nItem.getProducto().getAdministraAtributo7().equals("S") ? "| Atributo7 " + nItem.getAtributo7() : "");

                    throw new ExcepcionGeneralSistema(mensaje);
                }
            }
        }
    }

    public void controlItemsProducto(MovimientoStock m) throws ExcepcionGeneralSistema {

        for (ItemProductoStock i : m.getItemsProducto()) {

            if (i.getCantidad() == null || i.getCantidad().equals(BigDecimal.ZERO)) {

                throw new ExcepcionGeneralSistema("Ingrese una valor válido para la cantidad en " + i.getProducto().getDescripcion());
            }

            // Controlamos el ingreso de los atributos de stock
            if (i.getProducto() != null
                    && i.getProducto().getAdministraAtributo1().equals("S")
                    && i.getAtributo1().isEmpty()) {

                throw new ExcepcionGeneralSistema("Ingrese el atributo 1 para el producto " + i.getProducto().getDescripcion());
            }

            if (i.getProducto() != null
                    && i.getProducto().getAdministraAtributo2().equals("S")
                    && i.getAtributo2().isEmpty()) {

                throw new ExcepcionGeneralSistema("Ingrese el atributo 2 para el producto " + i.getProducto().getDescripcion());
            }

            if (i.getProducto() != null
                    && i.getProducto().getAdministraAtributo3().equals("S")
                    && i.getAtributo3().isEmpty()) {

                throw new ExcepcionGeneralSistema("Ingrese el atributo 3 para el producto " + i.getProducto().getDescripcion());
            }

            if (i.getProducto() != null
                    && i.getProducto().getAdministraAtributo4().equals("S")
                    && i.getAtributo4().isEmpty()) {

                throw new ExcepcionGeneralSistema("Ingrese el atributo 4 para el producto " + i.getProducto().getDescripcion());
            }

            if (i.getProducto() != null
                    && i.getProducto().getAdministraAtributo5().equals("S")
                    && i.getAtributo5().isEmpty()) {

                throw new ExcepcionGeneralSistema("Ingrese el atributo 5 para el producto " + i.getProducto().getDescripcion());
            }

            if (i.getProducto() != null
                    && i.getProducto().getAdministraAtributo6().equals("S")
                    && i.getAtributo6().isEmpty()) {

                throw new ExcepcionGeneralSistema("Ingrese el atributo 6 para el producto " + i.getProducto().getDescripcion());
            }

            if (i.getProducto() != null
                    && i.getProducto().getAdministraAtributo7().equals("S")
                    && i.getAtributo7().isEmpty()) {

                throw new ExcepcionGeneralSistema("Ingrese el atributo 7 para el producto " + i.getProducto().getDescripcion());
            }

            if (m.getTipoMovimiento().equals("E")
                    && !m.isNoValidaStockDisponible()
                    && m.getDeposito().getSigno().equals("+")) {

                Stock s = new Stock(i);
                //Es un egreso de stock por lo tanto convertimos la cantidad a negativo
                s.setStocks(s.getStocks().negate());

                if (!stockRN.isProductoDisponible(s)) {

                    String mensaje = "Stock insuficiente. Hay " + s.getStockDisponible() + " " + s.getProducto().getUnidadDeMedida().getCodigo()
                            + " disponible/s para " + i.getProducto().getDescripcion() + " en Deposito " + i.getDeposito().getCodigo()
                            + (i.getProducto().getAdministraAtributo1().equals("S") ? "| Atributo1 " + i.getAtributo1() : "")
                            + (i.getProducto().getAdministraAtributo2().equals("S") ? "| Atributo2 " + i.getAtributo2() : "")
                            + (i.getProducto().getAdministraAtributo3().equals("S") ? "| Atributo3 " + i.getAtributo3() : "")
                            + (i.getProducto().getAdministraAtributo4().equals("S") ? "| Atributo4 " + i.getAtributo4() : "")
                            + (i.getProducto().getAdministraAtributo5().equals("S") ? "| Atributo5 " + i.getAtributo5() : "")
                            + (i.getProducto().getAdministraAtributo6().equals("S") ? "| Atributo6 " + i.getAtributo6() : "")
                            + (i.getProducto().getAdministraAtributo7().equals("S") ? "| Atributo7 " + i.getAtributo7() : "");

                    throw new ExcepcionGeneralSistema(mensaje);
                }
            }
        }

        if (m.getItemTransferencia() != null
                && m.getDepositoTransferencia() != null
                && !m.isNoValidaStockDisponible()
                && m.getDepositoTransferencia().getSigno().equals("+")) {

            for (ItemTransferenciaStock i : m.getItemTransferencia()) {

                Stock s = new Stock(i);
                if (!stockRN.isProductoDisponible(s)) {

                    String mensaje = "Stock insuficiente. Hay " + s.getStockDisponible() + " " + s.getProducto().getUnidadDeMedida().getCodigo()
                            + " disponible/s para " + i.getProducto().getDescripcion() + " en Deposito " + i.getDeposito().getCodigo()
                            + (i.getProducto().getAdministraAtributo1().equals("S") ? "| Atributo1 " + i.getAtributo1() : "")
                            + (i.getProducto().getAdministraAtributo2().equals("S") ? "| Atributo2 " + i.getAtributo2() : "")
                            + (i.getProducto().getAdministraAtributo3().equals("S") ? "| Atributo3 " + i.getAtributo3() : "")
                            + (i.getProducto().getAdministraAtributo4().equals("S") ? "| Atributo4 " + i.getAtributo4() : "")
                            + (i.getProducto().getAdministraAtributo5().equals("S") ? "| Atributo5 " + i.getAtributo5() : "")
                            + (i.getProducto().getAdministraAtributo6().equals("S") ? "| Atributo6 " + i.getAtributo6() : "")
                            + (i.getProducto().getAdministraAtributo7().equals("S") ? "| Atributo7 " + i.getAtributo7() : "");

                    throw new ExcepcionGeneralSistema(mensaje);

                }
            }
        }
    }

    /**
     * Generamos los items de transferencia para registrar la salida del stock.
     *
     * @param m Movimiento de stock
     */
    public void generarItemTransferencia(MovimientoStock m) throws ExcepcionGeneralSistema {

        //Verificamos que se un movimiento de tipo transferencia
        if (m.getTipoMovimiento().equals("T")) {
            //Generamos la lista vacía
            m.setItemTransferencia(new ArrayList<ItemTransferenciaStock>());

            if (m.getId() != null) {
                inventarioDAO.borraItemsTransferencia(m.getId());
            }
        } else {
            return;
        }

        if (m.getItemsProducto() != null) {

            for (ItemProductoStock i : m.getItemsProducto()) {

                if (i.getProducto() != null) {
                    ItemTransferenciaStock t = nuevoItemTransferencia(m);
                    t.setProducto(i.getProducto());
                    t.setUnidadMedida(i.getUnidadMedida());
//                    t.setPrecio(i.getPrecio());                    
                    t.setAtributo1(i.getAtributo1());
                    t.setAtributo2(i.getAtributo2());
                    t.setAtributo3(i.getAtributo3());
                    t.setAtributo4(i.getAtributo4());
                    t.setAtributo5(i.getAtributo5());
                    t.setAtributo6(i.getAtributo6());
                    t.setAtributo7(i.getAtributo7());

                    if (i.getCantidad() == null) {
                        throw new ExcepcionGeneralSistema("Cantidad en blanco");
                    }

                    t.setCantidad(i.getCantidad());
//                    t.setStocks(i.getCantidad().negate());
                    m.getItemTransferencia().add(t);
                }
            }
        }
    }

    /**
     * Generar los objetos stock donde se almacena el stock de los productos por
     * deposito, fecha, etc
     *
     * @param m Movimiento de stock
     * @throws Exception
     */
    public synchronized void generarStock(MovimientoStock m) throws Exception {

        for (ItemProductoStock i : m.getItemsProducto()) {

            Stock nStock = new Stock(i);
            stockRN.guardar(nStock);
        }

        if (m.getItemTransferencia() != null) {

            for (ItemTransferenciaStock i : m.getItemTransferencia()) {

                Stock nStock = new Stock(i);
                stockRN.guardar(nStock);
            }
        }
    }

    public void asignarDepositoItems(MovimientoStock m) {

        if (m.getItemsProducto() != null) {

            for (ItemMovimientoStock i : m.getItemsProducto()) {

                i.setFechaMovimiento(m.getFechaMovimiento());
                i.setDeposito(m.getDeposito());
            }
        }

        //Aplicamos el deposito a los items de transferencia
        if (m.getTipoMovimiento().equals("T")) {

            if (m.getItemTransferencia() != null) {
                for (ItemMovimientoStock i : m.getItemTransferencia()) {

                    i.setFechaMovimiento(m.getFechaMovimiento());
                    i.setDeposito(m.getDepositoTransferencia());
                }
            }
        }
    }

    public void asignarCantidadStock(MovimientoStock m) {

        if (m.getItemsProducto() == null) {
            return;
        }

        for (ItemMovimientoStock i : m.getItemsProducto()) {

            //Si es un egreso actualizamos el stock en negativo
            if (m.getTipoMovimiento().equals("E")) {
                i.setStocks(i.getCantidad().negate());
            } else {
                i.setStocks(i.getCantidad());
            }
        }

        if (m.getItemTransferencia() != null) {
            for (ItemMovimientoStock i : m.getItemTransferencia()) {
                i.setStocks(i.getCantidad().negate());
            }
        }
    }

    /**
     * Borramos de la lista los items que no son válidos para guardar y que
     * pudieran generar errores
     *
     * @param m Movimiento de Stock
     */
    public void borrarItemsNoValidos(MovimientoStock m) {

        if (m.getItemsProducto() == null) {
            return;
        }

        String indiceBorrar[] = new String[m.getItemsProducto().size()];

        //Seteamos los valores en -1
        for (int i = 0; i < indiceBorrar.length; i++) {
            indiceBorrar[i] = "N";
        }

        for (int i = 0; i < m.getItemsProducto().size(); i++) {

            ItemProductoStock im = m.getItemsProducto().get(i);

            if (im.getProducto() == null) {
                indiceBorrar[i] = "S";
                continue;
            }

            if (im.getProducto() == null && !im.isTodoOk()) {
                indiceBorrar[i] = "S";
            }
        }

        for (int i = 0; i < indiceBorrar.length; i++) {
            if (indiceBorrar[i].equals("S")) {
                ItemProductoStock remove = m.getItemsProducto().remove(i);
            }
        }

    }

    /**
     * Eliminar un item de un movimiento
     *
     * @param m movimiento del cual se eliminará el item
     * @param nItem item a eliminar
     * @return éxito si la eliminación fue exitosa
     */
    public boolean eliminarItemProducto(MovimientoStock m, ItemMovimientoStock nItem) {

        if (m.getId() != null) {
            return false;
        }

        boolean fItemBorrado = false;
        int i = 0;
        int indiceItemProducto = -1;

        for (ItemProductoStock ip : m.getItemsProducto()) {

            if (ip.getProducto() != null) {

                if (ip.getProducto().equals(nItem.getProducto()) && ip.getCantidad().equals(nItem.getCantidad())) {
                    indiceItemProducto = i;
                }
            }
            i++;
        }

        //Borramos los items productos
        if (indiceItemProducto >= 0) {
            ItemProductoStock remove = m.getItemsProducto().remove(indiceItemProducto);
            if (remove != null) {
                fItemBorrado = true;
            }
        }

        return fItemBorrado;
    }

    public List<MovimientoStock> getListaByBusqueda(Map<String, String> filtro, int cantidadRegistros) {

        return inventarioDAO.getListaByBusqueda(filtro, cantidadRegistros);
    }

    public MovimientoStock getMovimiento(String codfor, String sucurs, Integer nrofor) {

        Map<String, String> filtro = inventarioDAO.getFiltro();

        filtro.put(" numeroFormulario = ", "'" + nrofor + "'");
        filtro.put(" formulario.codigo = ", "'" + codfor + "'");
        filtro.put(" sucursal.codigo = ", "'" + sucurs + "'");

        return inventarioDAO.getObjeto(MovimientoStock.class, filtro);
    }

    public boolean existeComprobante(MovimientoStock ms) {

        Map<String, String> filtro = inventarioDAO.getFiltro();

        filtro.put(" numeroFormulario = ", "'" + ms.getNumeroFormulario() + "'");
        filtro.put(" formulario.codigo = ", "'" + ms.getFormulario().getCodigo() + "'");
        filtro.put(" sucursal.codigo = ", "'" + ms.getSucursal().getCodigo() + "'");

        return (inventarioDAO.getObjeto(MovimientoStock.class, filtro) != null);

    }

    public BigDecimal getStockAFecha(Producto p, Deposito d, Date fecha) {
        return inventarioDAO.getStockAFecha(p, d, fecha);
    }

    public List<ItemMovimientoStock> getMovimientosEntreFechas(Producto p, Deposito d, Date fDesde, Date fHasta) {

        return inventarioDAO.getMovimientosEntreFechas(p, d, fDesde, fHasta);
    }

    public BigDecimal getCantidadFromMovimiento(String tipoMovimiento, Producto producto, Deposito deposito, Date fechaMovimientoDesde, Date fechaMovimientoHasta) {

        return inventarioDAO.getCantidadFromMovimiento(tipoMovimiento, producto, deposito, fechaMovimientoDesde, fechaMovimientoHasta);

    }

    public void eliminarMovimiento(Integer id) {

        inventarioDAO.eliminar(MovimientoStock.class, id);
        stockRN.recalcularStock();

    }

    public MovimientoStock generarComprobante(MovimientoProduccion movimientoProduccion) throws ExcepcionGeneralSistema, Exception {

        Formulario formulario = formularioRN.getFormulario(movimientoProduccion.getComprobanteStock(), movimientoProduccion.getSucursalStock(), "X");

        MovimientoStock movimientoStock = crearMovimiento(movimientoProduccion.getComprobanteStock(), formulario, movimientoProduccion.getSucursalStock());

        movimientoStock.setFechaMovimiento(movimientoProduccion.getFechaMovimiento());
        movimientoStock.setMonedaSecundaria(movimientoProduccion.getMonedaSecundaria());
        movimientoStock.setMonedaRegistracion(movimientoProduccion.getMonedaRegistracion());
        movimientoStock.setCotizacion(movimientoProduccion.getCotizacion());

        movimientoStock.setDeposito(movimientoProduccion.getDeposito());
        movimientoStock.setDepositoTransferencia(movimientoProduccion.getDepositoTransferencia());

        movimientoStock.getItemsProducto().clear();

        for (ItemProductoProduccion itemProductoProduccion : movimientoProduccion.getItemsProducto()) {

            if (itemProductoProduccion.getProducto() != null
                    && itemProductoProduccion.getProducto().getTipoProducto().getGestionaStock().equals("S")
                    && itemProductoProduccion.getProducto().getGestionaStock().equals("S")) {

                for (ItemDetalleMovimientoProduccion itemDetalleProduccion : itemProductoProduccion.getItemDetalle()) {
                    
                    ItemProductoStock itemProductoStock = nuevoItemProducto(movimientoStock);
                    itemProductoStock.setProducto(itemDetalleProduccion.getProducto());
                    itemProductoStock.setObservaciones(itemProductoProduccion.getObservaciones());

                    if (itemDetalleProduccion.getProducto().getAdministraAtributo1().equals("S")
                            && itemDetalleProduccion.getAtributo1() != null
                            && !itemDetalleProduccion.getAtributo1().isEmpty()) {

                        itemProductoStock.setAtributo1(itemDetalleProduccion.getAtributo1());
                    }

                    if (itemDetalleProduccion.getProducto().getAdministraAtributo2().equals("S")
                            && itemDetalleProduccion.getAtributo2() != null
                            && !itemDetalleProduccion.getAtributo2().isEmpty()) {

                        itemProductoStock.setAtributo2(itemDetalleProduccion.getAtributo2());
                    }

                    if (itemDetalleProduccion.getProducto().getAdministraAtributo3().equals("S")
                            && itemDetalleProduccion.getAtributo3() != null
                            && !itemDetalleProduccion.getAtributo3().isEmpty()) {

                        itemProductoStock.setAtributo3(itemDetalleProduccion.getAtributo3());
                    }

                    if (itemDetalleProduccion.getProducto().getAdministraAtributo4().equals("S")
                            && itemDetalleProduccion.getAtributo4() != null
                            && !itemDetalleProduccion.getAtributo4().isEmpty()) {

                        itemProductoStock.setAtributo4(itemDetalleProduccion.getAtributo4());
                    }

                    if (itemDetalleProduccion.getProducto().getAdministraAtributo5().equals("S")
                            && itemDetalleProduccion.getAtributo5() != null
                            && !itemDetalleProduccion.getAtributo5().isEmpty()) {

                        itemProductoStock.setAtributo5(itemDetalleProduccion.getAtributo5());
                    }

                    if (itemDetalleProduccion.getProducto().getAdministraAtributo6().equals("S")
                            && itemDetalleProduccion.getAtributo6() != null
                            && !itemDetalleProduccion.getAtributo6().isEmpty()) {

                        itemProductoStock.setAtributo6(itemDetalleProduccion.getAtributo6());
                    }

                    if (itemDetalleProduccion.getProducto().getAdministraAtributo7().equals("S")
                            && itemDetalleProduccion.getAtributo7() != null
                            && !itemDetalleProduccion.getAtributo7().isEmpty()) {

                        itemProductoStock.setAtributo7(itemDetalleProduccion.getAtributo7());
                    }

                    itemProductoStock.setCantidad(itemDetalleProduccion.getCantidad());
                    itemProductoStock.setUnidadMedida(itemDetalleProduccion.getUnidadMedida());
                    itemProductoStock.setPrecio(itemDetalleProduccion.getProducto().getPrecioReposicion());
                    itemProductoStock.setTodoOk(true);

                    itemProductoStock.setMovimiento(movimientoStock);
                    movimientoStock.getItemsProducto().add(itemProductoStock);
                }
            }
        }

        borrarItemsNoValidos(movimientoStock);
        generarItemTransferencia(movimientoStock);
        asignarDepositoItems(movimientoStock);
        asignarCantidadStock(movimientoStock);
        controlComprobante(movimientoStock, true);
        generarStock(movimientoStock);

        if (movimientoStock.getItemsProducto().isEmpty()) {
            movimientoStock = null;
        }

        return movimientoStock;

//        if (m.getCircuito().getActzst()=='S' && (m.getModfor().equals("ST"))){
//
//            ComprobanteStock cs = comprobanteStockRN.getComprobante(m.getComprobante().getModulo(),m.getComprobante().getCodigo());
//            MovimientoStock ms = movimientoStockRN.nuevoMovimientoAutomático(cs, m.getFormulario());
//
//            ms.setFechaMovimiento(m.getFechaMovimiento());
//
//            if(ms.getTipoMovimiento().equals(TipoMovimientoStock.T)
//                    && m.getDeposito()!=null 
//                    && m.getDepositoTransferencia()!=null){
//
//                ms.setDeposito(m.getDeposito());                
//                ms.setDepositoTransferencia(m.getDepositoTransferencia());                
//            }
//
//            if(ms.getTipoMovimiento().equals(TipoMovimientoStock.I)){
//                ms.setDeposito(m.getDeposito());                
//            }
//
//            if(ms.getTipoMovimiento().equals(TipoMovimientoStock.E)){
//                ms.setDepositoTransferencia(m.getDepositoTransferencia());
//                
//            }
//            
//            m.setMovimientoStock(ms);
//            generarItemsMovimientoInventario(m);
//
//            generarDatosAdicionales(ms);
//            puedoGuardarMovimiento(ms);
//        }
    }

    /**
     * Comparamos los cambios e igualamanos los comprobantes de stock de modo
     * tal que si se recibe un comprobante que ha sido modificado, la
     * modificación se replicará
     *
     * @param movExistente Movimiento stock persistido
     * @param ms Movimientos de stock nuevo
     */
    public void igualarComprobantes(MovimientoStock movExistente, MovimientoStock ms) {

        if (movExistente.getDeposito() != null && !movExistente.getDeposito().equals(ms.getDeposito())) {
            movExistente.setDeposito(ms.getDeposito());
        }

        if (movExistente.getDepositoTransferencia() != null && !movExistente.getDepositoTransferencia().equals(ms.getDepositoTransferencia())) {
            movExistente.setDepositoTransferencia(ms.getDepositoTransferencia());
        }

        int itmNuevo = 0;
        int itmExiste = 0;

        for (ItemProductoStock ipnuevo : movExistente.getItemsProducto()) {

            itmNuevo++;
            itmExiste = 0;

            for (ItemProductoStock ipexistente : movExistente.getItemsProducto()) {

                itmExiste++;

                if (itmExiste == itmNuevo) {

                    ipexistente.setProducto(ipnuevo.getProducto());
                    ipexistente.setDeposito(ipnuevo.getDeposito());
                    ipexistente.setCantidad(ipnuevo.getCantidad());
                    ipexistente.setUnidadMedida(ipnuevo.getUnidadMedida());

                    ipexistente.setAtributo1(ipnuevo.getAtributo1());
                    ipexistente.setAtributo2(ipnuevo.getAtributo2());
                    ipexistente.setAtributo3(ipnuevo.getAtributo3());
                    ipexistente.setAtributo4(ipnuevo.getAtributo4());
                    ipexistente.setAtributo5(ipnuevo.getAtributo5());
                }
            }
        }

    }

}
