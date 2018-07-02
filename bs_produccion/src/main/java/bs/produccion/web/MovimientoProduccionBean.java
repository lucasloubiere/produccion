/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.web;


import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.modelo.Formulario;
import bs.global.modelo.Sucursal;
import bs.global.rn.FormularioPorSituacionIVARN;
import bs.global.rn.SucursalRN;
import bs.global.util.JsfUtil;
import bs.global.web.GenericBean;
import bs.produccion.modelo.CircuitoProduccion;
import bs.produccion.modelo.DepartamentoProduccion;
import bs.produccion.modelo.ItemComponenteProduccion;
import bs.produccion.modelo.ItemDetalleMovimientoProduccion;
import bs.produccion.modelo.ItemHorarioProduccion;
import bs.produccion.modelo.ItemProcesoProduccion;
import bs.produccion.modelo.ItemProductoProduccion;
import bs.produccion.modelo.MovimientoProduccion;
import bs.produccion.modelo.TipoMovimientoProduccion;
import bs.produccion.rn.CircuitoProduccionRN;
import bs.produccion.rn.ProduccionRN;
import bs.produccion.vista.PendienteProduccionDetalle;
import bs.produccion.vista.PendienteProduccionGrupo;
import bs.seguridad.web.UsuarioSessionBean;
import bs.stock.modelo.Formula;
import bs.stock.modelo.Producto;
import bs.stock.web.FormulaBean;
import bs.stock.web.ProductoBean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.wizard.Wizard;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Claudio
 */
@ManagedBean
@ViewScoped
public class MovimientoProduccionBean extends GenericBean implements Serializable {

    @EJB
    private ProduccionRN produccionRN;            
    @EJB
    private CircuitoProduccionRN circuitoRN;
    @EJB
    private SucursalRN sucursalRN;

    protected @EJB
    FormularioPorSituacionIVARN formularioPorSituacionIVARN;

    protected MovimientoProduccion m;
    protected ItemProductoProduccion itemProducto;
    protected List<MovimientoProduccion> movimientos;
    protected CircuitoProduccion circuito;

    protected String CIRCOM = "";
    protected String CIRAPL = "";

    protected String SUCPD = "";
    protected String MODPD = "";
    protected String CODPD = "";

    protected String SUCST = "";
    protected String MODST = "";
    protected String CODST = "";

    protected String SUCPR = "";
    protected String MODPR = "";
    protected String CODPR = "";

    protected String SUCVC = "";
    protected String MODVC = "";
    protected String CODVC = "";

    @ManagedProperty(value = "#{usuarioSessionBean}")
    protected UsuarioSessionBean usuarioSessionBean;

    @ManagedProperty(value = "#{productoBean}")
    protected ProductoBean productoBean;
    
    @ManagedProperty(value = "#{formulaBean}")
    protected FormulaBean formulaBean;

    @ManagedProperty(value = "#{formularioProduccionBean}")
    protected FormularioProduccionBean formularioProduccionBean;

    //Fecha para control
    protected Date fechaMinina;

    //Datos para generar movimientos aplicados
    protected List<PendienteProduccionGrupo> movimientosPendientes;
    protected List<PendienteProduccionDetalle> itemsPendiente;
    protected PendienteProduccionGrupo movimientoPendiente;

    protected Sucursal sucursal;
    protected Sucursal sucursalStock;
    protected Sucursal sucursalParteProceso;
    protected Sucursal sucursalValeConsumo;

    //Atributos para valores por defecto
    DepartamentoProduccion departamento;
    String prioridad = "";

    /**
     * Creates a new instance of RequerimientoProduccionBean
     */
    /**
     * Creates a new instance of OrdenProduccionBean
     */
    public MovimientoProduccionBean() {

        fechaMinina = new Date();
        titulo = "Movimiento de Producción";
        muestraReporte = false;
        numeroFormularioDesde = 1;
    }

    public void iniciarVariables() {

        try {
            if (!beanIniciado) {
                formulario = new Formulario();
                iniciarMovimiento();
                beanIniciado = true;
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Error al iniciar el bean " + ex);
        }
    }

    public void iniciarMovimiento() {

        super.iniciar();

        try {

            movimientoPendiente = new PendienteProduccionGrupo();
            movimientosPendientes = null;
            itemsPendiente = new ArrayList<PendienteProduccionDetalle>();

            muestraReporte = false;
            solicitaEmail = false;
            buscaMovimiento = false;

            nombreArchivo = "";
            esAnulacion = false;

            numeroFormularioDesde = null;
            numeroFormularioHasta = null;
            fechaMovimientoDesde = null;
            fechaMovimientoHasta = null;

            sucursal = sucursalRN.getSucursal(SUCPD);
            sucursalStock = sucursalRN.getSucursal(SUCST);
            sucursalParteProceso = sucursalRN.getSucursal(SUCPR);
            sucursalValeConsumo = sucursalRN.getSucursal(SUCVC);

            if (sucursalStock == null) {
                sucursalStock = sucursal;
            }

            if (sucursalParteProceso == null) {
                sucursalParteProceso = sucursal;
            }

            if (sucursalValeConsumo == null) {
                sucursalValeConsumo = sucursal;
            }

            if (sucursal == null) {
                sucursal = sucursalStock;
            }

            iniciarCircuito(CIRCOM, CIRAPL);

            if (!seleccionaPendiente) {
                m = produccionRN.nuevoMovimiento(circuito, sucursal, sucursalStock);
            }

            cargarFormulariosBusqueda();
            aplicarDatosPorDefecto();

        } catch (ExcepcionGeneralSistema ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("iniciarMovimiento: " + ex);

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void iniciarCircuito(String circom, String cirapl) throws ExcepcionGeneralSistema {

        if (circom != null && cirapl != null) {
            circuito = circuitoRN.getCircuito(circom, cirapl);
        }

        if (circuito == null) {
            JsfUtil.addErrorMessage("No se encontró circuito " + circom + "-" + cirapl);
            return;
        }

        circuitoRN.cargarCircuitosRelacionados(circuito);

        if (circuito.getActualizaProduccion().equals("S") && MODPD != null && CODPD != null) {

            circuito.setComprobanteProduccion(circuitoRN.getComprobanteProduccion(circom, cirapl, MODPD, CODPD));

        } else if (circuito.getActualizaStock().equals("S") && MODST != null && CODST != null) {

            circuito.setComprobanteStock(circuitoRN.getComprobanteStock(circom, cirapl, MODST, CODST));
        }

        if (circuito.getAutomatizaParteProduccion().equals("S")) {

            circuito.setComprobanteParteProceso(circuitoRN.getComprobanteProduccion(circom, cirapl, MODPR, CODPR));
            circuito.setComprobanteValeConsumo(circuitoRN.getComprobanteStock(circom, cirapl, MODVC, CODVC));
        }

        if (circom == null) {
            circom = "";
        }
        seleccionaPendiente = (!circom.equals(cirapl));
    }

    public void nuevoMovimiento() {

        m = null;

        if (seleccionaPendiente) {
            Wizard w = (Wizard) FacesContext.getCurrentInstance().getViewRoot().findComponent("fPendiente").findComponent("wizPendiente");
            w.setStep("filtro");

            AccordionPanel a = (AccordionPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("fPendiente").findComponent("accordion");
            a.setActiveIndex("0");
        }
        iniciarMovimiento();
    }

    public void guardar(boolean nuevo) {

        try {
            m = produccionRN.guardar(m);

            if (m.getComprobante().getModulo().equals("PD")) {
                JsfUtil.addInfoMessage("El documento " + m.getFormulario().getDescripcion() + "-" + m.getNumeroFormulario() + " se guardó correctamente", "");
            } else if (m.getMovimientoStock() != null) {
                JsfUtil.addInfoMessage("El documento " + m.getMovimientoStock().getFormulario().getDescripcion() + "-" + m.getMovimientoStock().getNumeroFormulario() + " se guardó correctamente", "");
            }

            if (m.getValeConsumo() != null && m.getValeConsumo().isPersistido()) {
                JsfUtil.addInfoMessage("El documento " + m.getValeConsumo().getComprobante().getDescripcion() + "-" + m.getValeConsumo().getNumeroFormulario() + " se guardó correctamente", "");
            }

            if (m.getParteProceso() != null && m.getParteProceso().isPersistido()) {
                JsfUtil.addInfoMessage("El documento " + m.getParteProceso().getComprobante().getDescripcion() + "-" + m.getParteProceso().getNumeroFormulario() + " se guardó correctamente", "");
            }

            if (nuevo) {
                nuevoMovimiento();
            }

        } catch (Exception ex) {

            if (circuito.getPermiteAgregarItems().equals("S")) {
                //Cargarmos un nuevo item en blanco en caso de que quieran guardar sin agregar un items
                m.getItemsProducto().add((ItemProductoProduccion) produccionRN.nuevoItemProducto(m));
            }
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
    
    public void nuevoItemProducto() {

        try {
            productoBean.setProducto(null);
            //Cargarmos un nuevo item en blanco
            m.getItemsProducto().add((ItemProductoProduccion) produccionRN.nuevoItemProducto(m));

        } catch (Exception ex) {

            JsfUtil.addErrorMessage("No es posible agregar un nuevo item " + ex);
        }
    }

    public void nuevoItemComponente() {

        try {
            productoBean.setProducto(null);
            //Cargarmos un nuevo item en blanco
            m.getItemsComponente().add((ItemComponenteProduccion) produccionRN.nuevoItemComponente(m));

        } catch (Exception ex) {

            JsfUtil.addErrorMessage("No es posible agregar un nuevo item " + ex);
        }
    }

    public void nuevoItemProceso() {

        try {
            productoBean.setProducto(null);
            //Cargarmos un nuevo item en blanco
            m.getItemsProceso().add((ItemProcesoProduccion) produccionRN.nuevoItemProceso(m));

        } catch (Exception ex) {

            JsfUtil.addErrorMessage("No es posible agregar un nuevo item " + ex);
        }
    }

    public void nuevoItemHorario() {

        try {

            //Cargarmos un nuevo item en blanco
            m.getItemsHorario().add((ItemHorarioProduccion) produccionRN.nuevoItemHorario(m));

        } catch (Exception ex) {

            JsfUtil.addErrorMessage("No es posible agregar un nuevo item " + ex);
        }
    }

    public void procesarProducto() {

        if (m.getMonedaSecundaria() == null) {
            JsfUtil.addWarningMessage("El comprobante no tiene una moneda secundaria asignada");
            return;
        }

        if (productoBean.getProducto() != null && m != null) {

            Producto p = productoBean.getProducto();
            ItemProductoProduccion ip = m.getItemsProducto().get(m.getItemsProducto().size() - 1);

            ip.setProducto(p);
            ip.setProductoOriginal(p);
            ip.setUnidadMedida(p.getUnidadDeMedida());
            ip.setActualizaStock(p.getGestionaStock());
        }
    }
    
    public void procesarFormula() {
          
        if (formulaBean.getFormula() != null && m != null && itemProducto != null) {

            try {
                Formula formula = formulaBean.getFormula();                
                produccionRN.asignarFormula(itemProducto, formula);

            } catch (ExcepcionGeneralSistema ex) {

                JsfUtil.addErrorMessage("No es posible agregar componentes y procesos " + ex);
            }
        }
    }
   

    public void agregarItemDetalleProducto(ItemProductoProduccion nItem) {
        try {

            produccionRN.agregarItemDetalleProducto(nItem);

        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
    
    public void agregarItemDetalleComponente(ItemComponenteProduccion nItem) {
        try {

            produccionRN.agregarItemDetalleComponente(nItem);

        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage(ex.getMessage());
        }
    }
   
    public boolean puedoAgregarItem(ItemProductoProduccion nItem) {

        if ((circuito.getItemUnico().equals("S")) && (m.getItemsProducto().size() > 1)) {
            JsfUtil.addErrorMessage("Ha superado la cantidad máxima de items, no puede continuar agregando");
            return false;
        }

        if (nItem.getCantidad() == null || nItem.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
            JsfUtil.addErrorMessage("Ingrese un valor de cantidad válido. Mayor a 0");
            return false;
        }

        if (productoBean.getProducto() == null) {
            JsfUtil.addErrorMessage("Seleccione un producto para agregar al comprobante");
            return false;
        }

        return true;
    }

    public void eliminarItemProducto(ItemProductoProduccion nItem) {

        if (produccionRN.eliminarItemProducto(m, nItem)) {
            JsfUtil.addWarningMessage("Se ha borrado el item " + nItem.getProducto().getDescripcion() + "");
        } else {
            JsfUtil.addWarningMessage("No se ha borrado el item " + nItem.getProducto().getDescripcion() + "");
        }
    }

    /**
     * Se utiliza para el parte de producción, para elimimar items de los vales
     * de consumo y pr
     *
     * @param mov movimiento del cual se eliminará el item
     * @param nItem item a eliminar
     */
    public void eliminarItemProducto(MovimientoProduccion mov, ItemProductoProduccion nItem) {

        if (produccionRN.eliminarItemProducto(mov, nItem)) {
            JsfUtil.addWarningMessage("Se ha borrado el item " + nItem.getProducto().getDescripcion() + "");
        } else {
            JsfUtil.addWarningMessage("No se ha borrado el item " + nItem.getProducto().getDescripcion() + "");
        }

    }

    public void eliminarItemDetalleProducto(ItemProductoProduccion ip, ItemDetalleMovimientoProduccion nItem) {

        try {
            if (produccionRN.eliminarItemDetalleProducto(ip, nItem)) {

                JsfUtil.addWarningMessage("Se ha borrado el item " + nItem.getAtributo1() + "");
            } else {

                JsfUtil.addInfoMessage("No se encontró el item a borrar");
            }
        } catch (ExcepcionGeneralSistema e) {
            JsfUtil.addErrorMessage(e.getMessage());
        }
    }
    
    public void eliminarItemDetalleComponente(ItemComponenteProduccion ip, ItemDetalleMovimientoProduccion nItem) {

        try {
            if (produccionRN.eliminarItemDetalleComponente(ip, nItem)) {

                JsfUtil.addWarningMessage("Se ha borrado el item " + nItem.getAtributo1() + "");
            } else {

                JsfUtil.addInfoMessage("No se encontró el item a borrar");
            }
        } catch (ExcepcionGeneralSistema e) {
            JsfUtil.addErrorMessage(e.getMessage());
        }
    }


    public void actualizarCantidades(ItemProductoProduccion nItem) {

        try {
            produccionRN.actualizarCantidades(m, nItem);
        } catch (ExcepcionGeneralSistema ex) {
            JsfUtil.addErrorMessage("Error: " + ex);
        }
    }

    public void verPendientes(CircuitoProduccion c) {

        try {
            iniciarCircuito(c.getCircom(), c.getCirapl());
            m = null;

            if (seleccionaPendiente) {
                Wizard w = (Wizard) FacesContext.getCurrentInstance().getViewRoot().findComponent("fPendiente").findComponent("wizPendiente");
                w.setStep("filtro");

                AccordionPanel a = (AccordionPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("fPendiente").findComponent("accordion");
                a.setActiveIndex("0");
            }

        } catch (Exception ex) {
            JsfUtil.addErrorMessage("No es posible seleccionar pendientes");
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String procesoSeleccionPendientes(FlowEvent event) {

        if (event.getNewStep().equals("filtro")) {
            JsfUtil.addInfoMessage("Aplique los filtros que considere");
        }

        if (event.getNewStep().equals("pendiente_grupo")) {
            try {
                if (m != null) {

                    JsfUtil.addInfoMessage("El comprobante fue generado, no puede regresar al paso anterior");
                    return event.getOldStep();
                }

                seleccionarMovimientosPendientes();

                if (movimientosPendientes == null || movimientosPendientes.isEmpty()) {
                    JsfUtil.addInfoMessage("No se encontraron pendientes para este circuito");
                    return event.getOldStep();
                }

            } catch (Exception ex) {
                JsfUtil.addErrorMessage("No es posible procesar pendientes: " + ex);
            }
        }

        return event.getNewStep();
    }

    public void seleccionarMovimientosPendientes() throws ExcepcionGeneralSistema {

        cargarFiltroGrupo();

        movimientoPendiente = new PendienteProduccionGrupo();
        movimientosPendientes = produccionRN.getMovimientosPendiente(filtroGrupo);

        itemsPendiente = new ArrayList<PendienteProduccionDetalle>();
        seleccionaTodo = false;

        if (movimientosPendientes != null && !movimientosPendientes.isEmpty()) {
            JsfUtil.addInfoMessage("Seleccione un comprobante para ver los pendientes");
        }
    }

    public void seleccionarItemPendiente(PendienteProduccionGrupo pg, boolean muestraMensaje) {

        movimientoPendiente = pg;
        cargarFiltroDetalle();
        itemsPendiente = produccionRN.getItemsPendiente(filtroDetalle);

        if (itemsPendiente.isEmpty() && muestraMensaje) {
            JsfUtil.addWarningMessage("No se han encontrado items pendientes");
        }
    }

    //Seleccionar todos los items pendientes 
    public void seleccionarTodo() {

        produccionRN.seleccionarTodo(itemsPendiente, seleccionaTodo);

    }

//        try {
//                if (itemsPendiente == null) {
//            JsfUtil.addErrorMessage("No existen items pendientes");
//            return event.getOldStep();
//        }
//
//        if (itemsPendiente.isEmpty()) {
//            JsfUtil.addErrorMessage("No existen items pendientes");
//            return event.getOldStep();
//        }
//
//        m = produccionRN.nuevoMovimiento(circuito, sucursal, sucursalStock);
//        aplicarDatosPorDefecto();
//        produccionRN.generarItems(m, itemsPendiente);
//
//        //Generamos automaticamente el comprobante
//        if (m.getValeConsumo() != null) {
//
//            //Genera y carga filtro para seleccionar pendientes de vale de consumo
//            Map<String, String> filtroDetalleVC = new HashMap<String, String>();
//            //filtroDetalleVC.put("modapl=", "'" + movimientoPendiente.getModapl() + "'");
//            //filtroDetalleVC.put("codapl=", "'" + movimientoPendiente.getCodapl() + "'");
//            //filtroDetalleVC.put("nroapl=", "" + movimientoPendiente.getNroapl());
//            filtroDetalleVC.put("circom = ", "'" + CIRAPL + "'");
//            filtroDetalleVC.put("expapl > ", "0");
//            filtroDetalleVC.put("producto.tippro=", "'MAT'");
//
//            //Seleccionamos los items pendientes
//            List<PendienteProduccionDetalle> itemsPendienteVC = produccionRN.getItemPendiente(filtroDetalleVC);
//            //Marca todos los items como seleccionados
//            produccionRN.seleccionarTodo(itemsPendienteVC, true);
//            //Genera los items para el comprobante
//            if (itemsPendienteVC != null) {
//
//                if (itemsPendienteVC.isEmpty()) {
//                    m.setValeConsumo(null);
//                } else {
//                    produccionRN.generarItems(m.getValeConsumo(), itemsPendienteVC);
//                }
//            }
//        }
//
//        //Generamos automaticamente el comprobante parte proceso
//        if (m.getParteProceso() != null) {
//
//            //Genera y carga filtro para seleccionar pendientes de parte proceso
//            Map<String, String> filtroDetallePR = new HashMap<String, String>();
////                    filtroDetallePR.put("modapl=", "'" + movimientoPendiente.getModapl() + "'");
////                    filtroDetallePR.put("codapl=", "'" + movimientoPendiente.getCodapl() + "'");
////                    filtroDetallePR.put("nroapl=", "" + movimientoPendiente.getNroapl());
//            filtroDetallePR.put("circom = ", "'" + CIRAPL + "'");
//            filtroDetallePR.put("expapl > ", "0");
//            filtroDetallePR.put("producto.tippro=", "'PRC'");
//
//            //Seleccionamos los items pendientes
//            List<PendienteProduccionDetalle> itemsPendientePR = produccionRN.getItemPendiente(filtroDetallePR);
//            //Marca todos los items como seleccionados
//            produccionRN.seleccionarTodo(itemsPendientePR, true);
//            //Genera los items para el comprobante
//            if (itemsPendientePR != null) {
//
//                if (itemsPendientePR.isEmpty()) {
//                    m.setParteProceso(null);
//                } else {
//                    produccionRN.generarItems(m.getParteProceso(), itemsPendientePR);
//                }
//            }
//        }
//
//    }
//    catch (Exception e
//
//    
//        ) {
//                e.printStackTrace();
//        JsfUtil.addErrorMessage("Error al generar comprobante", e.getMessage());
//        return event.getOldStep();
//    }
    public void finalizarProcesoSeleccionPendiente() {

        RequestContext context = RequestContext.getCurrentInstance();

        try {
            if (!produccionRN.tengoItemsSeleccionados(itemsPendiente)) {
                JsfUtil.addErrorMessage("No existen items pendientes seleccionados para generar el movimiento");
                context.addCallbackParam("todoOk", false);
                return;
            }

            m = produccionRN.nuevoMovimientoFromPendiente(circuito, sucursal, sucursalStock, itemsPendiente);
            aplicarDatosPorDefecto();

            context.addCallbackParam("todoOk", true);

            movimientoPendiente = null;
            itemsPendiente = null;

        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Error al generar comprobante", "- " + ex);
            log.log(Level.SEVERE, "Error al generar comprobante", ex);
            context.addCallbackParam("todoOk", false);
        }
    }

    public void generarMovimientoFromItems(Object items) throws Exception {

        m = produccionRN.nuevoMovimientoFromItems(circuito, sucursal, sucursalStock, items);
        aplicarDatosPorDefecto();

    }

    public void cargarFiltroGrupo() {

        filtroGrupo.clear();

        if (numeroFormularioDesde != null) {
            if (numeroFormularioDesde > 0) {
                filtroGrupo.put("nrofor = ", "" + numeroFormularioDesde);
            }
        }

        filtroGrupo.put("circom = ", "'" + circuito.getCirapl() + "'");

        if (circuito.getTipoMovimiento().equals(TipoMovimientoProduccion.VC)) {

            filtroGrupo.put("formul = ", "''");
            filtroGrupo.put("stocks =", "'S'");
        }

        if (circuito.getTipoMovimiento().equals(TipoMovimientoProduccion.PP)) {
            filtroGrupo.put("formul <> ", "''");
        }

        if (circuito.getTipoMovimiento().equals(TipoMovimientoProduccion.PR)) {
            filtroGrupo.put("formul = ", "''");
            filtroGrupo.put("stocks = ", "'N'");
        }
    }

    public void cargarFiltroDetalle() {

        filtroDetalle.clear();

        if (movimientoPendiente == null) {
            return;
        }

        filtroDetalle.put("modfor=", "'" + movimientoPendiente.getModfor() + "'");
        filtroDetalle.put("codfor=", "'" + movimientoPendiente.getCodfor() + "'");
        filtroDetalle.put("nrofor=", "" + movimientoPendiente.getNrofor());

        if (circuito.getTipoMovimiento().equals(TipoMovimientoProduccion.VC)) {

            filtroDetalle.put("tipitm = ", "'C'");            
        }

        if (circuito.getTipoMovimiento().equals(TipoMovimientoProduccion.PP)) {
            filtroDetalle.put("tipitm = ", "'P'");            
        }

        if (circuito.getTipoMovimiento().equals(TipoMovimientoProduccion.PR)) {
            filtroDetalle.put("tipitm = ", "'P'");            
        }
        
        System.err.println(filtroDetalle);
    }

    public void imprimir() {

//        try {
////            System.out.println("Nombre reporte " + m.getFormulario().getNombreReporte());
//
//            if (m.getFormulario().getNombreReporte()==null){
//                throw new ExcepcionGeneralSistema("El comprobante no tienen reporte asociado");
//            }
//
//            //JasperReport masterReport = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("/isd/produccion/reporte/"+ m.getFormulario().getNombreReporte()+".jasper"));
//            JasperReport masterReport = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream( m.getFormulario().getNombreReporte()+".jasper"));
//
//            Map parameters = new HashMap();
//            parameters.put("MODFOR", m.getModfor());
//            parameters.put("CODFOR", m.getCodfor());
//            parameters.put("NROFOR", m.getNrofor());
//
//            empresaRN.cargarDatosEmpresa(parameters);
//
//            String nombreArchivo = m.getComprobante().getCodigo()+"-"+m.getNrofor();
//
//            ReportFactory reportFactory = new ReportFactory();
//            reportFactory.verReportePDF(masterReport, nombreArchivo, parameters);
//
//        } catch (NullPointerException npe) {
//            JsfUtil.addErrorMessage("No se encontró el archivo: " + m.getFormulario().getNombreReporte()+".jasper");
//        } catch (Exception e){
//            e.printStackTrace();
//            JsfUtil.addErrorMessage("No se puede imprimir a pdf " + e.getMessage());
//        }
    }

    public void imprimir(String modulo) {

//        try {
////            System.out.println("Nombre reporte " + m.getFormulario().getNombreReporte());
//
//            if (mov.getFormulario().getNombreReporte()==null){
//                throw new ExcepcionGeneralSistema("El comprobante no tienen reporte asociado");
//            }
//            
//            //JasperReport masterReport = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("/isd/produccion/reporte/"+ m.getFormulario().getNombreReporte()+".jasper"));
//            JasperReport masterReport = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream( mov.getFormulario().getNombreReporte()+".jasper"));
//
//            Map parameters = new HashMap();
//            parameters.put("MODFOR", mov.getModfor());
//            parameters.put("CODFOR", mov.getCodfor());
//            parameters.put("NROFOR", mov.getNrofor());
//
//            empresaRN.cargarDatosEmpresa(parameters);
//
//            String nombreArchivo = mov.getComprobante().getCodigo()+"-"+mov.getNrofor();
//
//            ReportFactory reportFactory = new ReportFactory();
//            reportFactory.verReportePDF(masterReport, nombreArchivo, parameters);
//
//        } catch (NullPointerException npe) {
//            JsfUtil.addErrorMessage("No se encontró el archivo: " + mov.getFormulario().getNombreReporte()+".jasper");
//        } catch (Exception e){
//            e.printStackTrace();
//            JsfUtil.addErrorMessage("No se puede imprimir a pdf " + e.getMessage());
//        }
    }

    public void nuevaBusqueda() {

        if (m != null && m.getFormulario() != null) {
            formulario = m.getFormulario();
        }

        buscaMovimiento = true;
    }

    public void buscarMovimiento() {

        if (!validarParametros()) {
            return;
        }
        cargarFiltroBusqueda();
        movimientos = produccionRN.getListaByBusqueda(filtro, seleccionaPendiente, cantidadRegistros);

        if (movimientos == null || movimientos.isEmpty()) {
            JsfUtil.addWarningMessage("No se han encontrado movimientos");
        }
    }

    public boolean validarParametros() {

        if (formulario == null) {
            JsfUtil.addWarningMessage("Seleccione un formulario");
            return false;
        }

        if (fechaMovimientoDesde != null && fechaMovimientoHasta != null) {
            if (fechaMovimientoHasta.before(fechaMovimientoDesde)) {
                JsfUtil.addWarningMessage("La fecha de desde, no puede ser mayor a la fecha hasta.");
                return false;
            }
        }

        if (numeroFormularioDesde != null && numeroFormularioHasta != null) {
            if (numeroFormularioDesde > numeroFormularioHasta) {
                JsfUtil.addWarningMessage("Número de formulario desde es mayor al número de formulario hasta");
                return false;
            }
        }

        return true;
    }

    public void cargarFiltroBusqueda() {

        filtro.clear();

        filtro.put("circuito.circom =", "'" + CIRCOM + "'");

        if (formulario != null) {
            filtro.put("formulario.codigo = ", "'" + formulario.getCodigo() + "'");
        }

        if (numeroFormularioDesde != null) {

            filtro.put("numeroFormulario >=", String.valueOf(numeroFormularioDesde));
        }

        if (numeroFormularioHasta != null) {

            filtro.put("numeroFormulario <=", String.valueOf(numeroFormularioHasta));
        }

        if (fechaMovimientoDesde != null) {

            filtro.put("fechaMovimiento >= ", JsfUtil.getFechaSQL(fechaMovimientoDesde));
        }

        if (fechaMovimientoHasta != null) {

            filtro.put("fechaMovimiento <= ", JsfUtil.getFechaSQL(fechaMovimientoHasta));
        }

        if (seleccionaPendiente) {
            //filtro.put("itemsProducto.")
        }
    }

    public void resetParametros() {

//        formulario = null;
        numeroFormularioDesde = null;
        numeroFormularioHasta = null;
        fechaMovimientoDesde = null;
        fechaMovimientoHasta = null;
        muestraReporte = false;
        solicitaEmail = false;
        movimientos = null;
        indiceWizard = 0;
    }

    public void seleccionarMovimiento(MovimientoProduccion mSel) {

        if (m.getId() == mSel.getId()) {
            return;
        }

        m = mSel;
        buscaMovimiento = false;
    }

    protected void aplicarDatosPorDefecto() {

        if (departamento != null) {
            m.setDepartamento(departamento);
        }

        if (!prioridad.isEmpty()) {
            m.setPrioridad(prioridad);
        }
    }

    private void cargarFormulariosBusqueda() {

        if (circuito.getActualizaProduccion().equals("S") && MODPD != null && CODPD != null) {

            formularioProduccionBean.setLista(formularioPorSituacionIVARN.getFormularioByComprobante(circuito.getComprobanteProduccion()));
        }

        if (circuito.getActualizaStock().equals("S") && MODST != null && CODST != null) {

            formularioProduccionBean.setLista(formularioPorSituacionIVARN.getFormularioByComprobante(circuito.getComprobanteStock()));
        }

        if (formularioProduccionBean.getLista().size() == 1) {
            formulario = formularioProduccionBean.getLista().get(0);
        }
    }

    //--------------------------------------------------------------------------
    public MovimientoProduccion getM() {
        return m;
    }

    public void setM(MovimientoProduccion m) {
        this.m = m;
    }

    public CircuitoProduccion getCircuito() {
        return circuito;
    }

    public void setCircuito(CircuitoProduccion circuito) {
        this.circuito = circuito;
    }

    public String getCIRCOM() {
        return CIRCOM;
    }

    public void setCIRCOM(String CIRCOM) {
        this.CIRCOM = CIRCOM;
    }

    public String getCIRAPL() {
        return CIRAPL;
    }

    public void setCIRAPL(String CIRAPL) {
        this.CIRAPL = CIRAPL;
    }

    public String getSUCPD() {
        return SUCPD;
    }

    public void setSUCPD(String SUCPD) {
        this.SUCPD = SUCPD;
    }

    public String getMODPD() {
        return MODPD;
    }

    public void setMODPD(String MODPD) {
        this.MODPD = MODPD;
    }

    public String getCODPD() {
        return CODPD;
    }

    public void setCODPD(String CODPD) {
        this.CODPD = CODPD;
    }

    public String getSUCST() {
        return SUCST;
    }

    public void setSUCST(String SUCST) {
        this.SUCST = SUCST;
    }

    public String getMODST() {
        return MODST;
    }

    public void setMODST(String MODST) {
        this.MODST = MODST;
    }

    public String getCODST() {
        return CODST;
    }

    public void setCODST(String CODST) {
        this.CODST = CODST;
    }

    public UsuarioSessionBean getUsuarioSessionBean() {
        return usuarioSessionBean;
    }

    public void setUsuarioSessionBean(UsuarioSessionBean usuarioSessionBean) {
        this.usuarioSessionBean = usuarioSessionBean;
    }

    public ProductoBean getProductoBean() {
        return productoBean;
    }

    public void setProductoBean(ProductoBean productoBean) {
        this.productoBean = productoBean;
    }

    public FormularioProduccionBean getFormularioProduccionBean() {
        return formularioProduccionBean;
    }

    public void setFormularioProduccionBean(FormularioProduccionBean formularioProduccionBean) {
        this.formularioProduccionBean = formularioProduccionBean;
    }

    public Date getFechaMinina() {
        return fechaMinina;
    }

    public void setFechaMinina(Date fechaMinina) {
        this.fechaMinina = fechaMinina;
    }

    public List<PendienteProduccionGrupo> getMovimientosPendientes() {
        return movimientosPendientes;
    }

    public void setMovimientosPendientes(List<PendienteProduccionGrupo> movimientosPendientes) {
        this.movimientosPendientes = movimientosPendientes;
    }

    public List<PendienteProduccionDetalle> getItemsPendiente() {
        return itemsPendiente;
    }

    public void setItemsPendiente(List<PendienteProduccionDetalle> itemsPendiente) {
        this.itemsPendiente = itemsPendiente;
    }

    public PendienteProduccionGrupo getMovimientoPendiente() {
        return movimientoPendiente;
    }

    public void setMovimientoPendiente(PendienteProduccionGrupo movimientoPendiente) {
        this.movimientoPendiente = movimientoPendiente;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Sucursal getSucursalStock() {
        return sucursalStock;
    }

    public void setSucursalStock(Sucursal sucursalStock) {
        this.sucursalStock = sucursalStock;
    }

    public DepartamentoProduccion getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoProduccion departamento) {
        this.departamento = departamento;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public List<MovimientoProduccion> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoProduccion> movimientos) {
        this.movimientos = movimientos;
    }

    public String getSUCPR() {
        return SUCPR;
    }

    public void setSUCPR(String SUCPR) {
        this.SUCPR = SUCPR;
    }

    public String getMODPR() {
        return MODPR;
    }

    public void setMODPR(String MODPR) {
        this.MODPR = MODPR;
    }

    public String getCODPR() {
        return CODPR;
    }

    public void setCODPR(String CODPR) {
        this.CODPR = CODPR;
    }

    public String getSUCVC() {
        return SUCVC;
    }

    public void setSUCVC(String SUCVC) {
        this.SUCVC = SUCVC;
    }

    public String getMODVC() {
        return MODVC;
    }

    public void setMODVC(String MODVC) {
        this.MODVC = MODVC;
    }

    public String getCODVC() {
        return CODVC;
    }

    public void setCODVC(String CODVC) {
        this.CODVC = CODVC;
    }

    public Sucursal getSucursalParteProceso() {
        return sucursalParteProceso;
    }

    public void setSucursalParteProceso(Sucursal sucursalParteProceso) {
        this.sucursalParteProceso = sucursalParteProceso;
    }

    public Sucursal getSucursalValeConsumo() {
        return sucursalValeConsumo;
    }

    public void setSucursalValeConsumo(Sucursal sucursalValeConsumo) {
        this.sucursalValeConsumo = sucursalValeConsumo;
    }

    public ItemProductoProduccion getItemProducto() {
        return itemProducto;
    }

    public void setItemProducto(ItemProductoProduccion itemProducto) {
        this.itemProducto = itemProducto;
    }

    public FormulaBean getFormulaBean() {
        return formulaBean;
    }

    public void setFormulaBean(FormulaBean formulaBean) {
        this.formulaBean = formulaBean;
    }
    
}
