/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.stock.web;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.web.GenericBean;
import bs.global.util.JsfUtil;
import bs.global.util.ReportFactory;
import bs.seguridad.web.UsuarioSessionBean;
import bs.stock.modelo.ItemMovimientoStock;
import bs.stock.modelo.ItemProductoStock;
import bs.stock.modelo.MovimientoStock;
import bs.stock.modelo.Producto;
import bs.stock.rn.MovimientoStockRN;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Claudio
 */
@ManagedBean
@ViewScoped
public class MovimientoStockBean extends GenericBean implements Serializable{

    @EJB protected MovimientoStockRN movimientoStockRN;        
    
    @ManagedProperty(value = "#{usuarioSessionBean}")
    protected UsuarioSessionBean usuarioSessionBean;

    @ManagedProperty(value = "#{productoBean}")
    protected ProductoBean productoBean;
        
//    @ManagedProperty(value = "#{consultaStock}")
//    protected ConsultaStock consultaStock;
    
//    @ManagedProperty(value = "#{mascaraStockBean}")
//    protected MascaraStockBean mascaraStockBean;
    
    @ManagedProperty(value = "#{depositoBean}")
    protected DepositoBean depositoBean;
    
    @ManagedProperty(value = "#{formularioStockBean}")
    protected FormularioStockBean formularioStockBean;
                
    @ManagedProperty(value = "#{reportFactory}")
    protected ReportFactory reportFactory;

    protected String SUCURS = "";    
    protected String MODST = "";    
    protected String CODST = "";
        
    protected MovimientoStock m;
    protected ItemProductoStock item;
    protected List<MovimientoStock> movimientos;

    protected Date fechaMinima;


    /** Creates a new instance of MovimientoInventarioBean */
    public MovimientoStockBean() {
        
        fechaMinima = new Date();
        muestraReporte = false;
    }
    
    public void iniciarVariables(){
        
        try {
            if(!beanIniciado){  
                
                if(SUCURS==null)SUCURS = "";                
                if(MODST==null) MODST = "";
                if(CODST==null) CODST = "";
                
                nuevoMovimiento();    
                beanIniciado = true;
            }            
        }catch (Exception e){
           JsfUtil.addErrorMessage("Error al iniciar el bean " + e.getMessage());
        }        
    }
    
    public void guardar(boolean nuevo){
        
        try {            
            movimientoStockRN.guardar(m);
            JsfUtil.addInfoMessage("El documento " + m.getComprobante().getDescripcion()+ "-" + m.getNumeroFormulario() + " se guardó correctamente", "");

            if (nuevo) {
                m = movimientoStockRN.nuevoMovimiento(MODST, CODST, SUCURS);
                depositoBean.setDeposito(null);
                productoBean.setProducto(null);
            }
        } catch (Exception ex) {

            m.getItemsProducto().add(movimientoStockRN.nuevoItemProducto(m));
            JsfUtil.addErrorMessage(ex.getMessage());
        }

    }
    
    public void nuevoMovimiento(){
        
        super.iniciar();
        
        try {
            
            nombreArchivo = "";            
            buscaMovimiento = false;  
            muestraReporte = false;
            
            m = movimientoStockRN.nuevoMovimiento(MODST, CODST, SUCURS);
            
        } catch (ExcepcionGeneralSistema egs) {
            JsfUtil.addErrorMessage("nuevoMovimiento" + egs);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, egs);
        }      
    }

    public void agregarItem(ItemMovimientoStock nItem){
        try {
          
            movimientoStockRN.puedoAgregarItem(m, nItem);            
            nItem.setTodoOk(true);            
            
            //Cargarmos un nuevo item en blanco
            m.getItemsProducto().add(movimientoStockRN.nuevoItemProducto(m));
            
            productoBean.setProducto(null);
            
        } catch (ExcepcionGeneralSistema ex) {            
            JsfUtil.addErrorMessage("agregarItem" +ex);
        }
    }

    public void eliminarItem(ItemMovimientoStock nItem){

        if(movimientoStockRN.eliminarItemProducto(m, nItem)){
            JsfUtil.addWarningMessage("Se ha borrado el item "+ nItem.getProducto().getDescripcion() + "");
        }else{
            JsfUtil.addErrorMessage("No es posible borrar el item "+ nItem.getProducto().getDescripcion() + "");
        }
    }
    
    public void procesarDeposito(){
        
        if(depositoBean!=null && m!=null){                        
            m.setDeposito(depositoBean.getDeposito());            
        }
    }
    
    public void procesarDepositoTransferencia(){
        
        if(depositoBean!=null && m!=null){                        
            m.setDepositoTransferencia(depositoBean.getDeposito());            
        }
    }
    
    public void procesarProducto(){
      
        if(productoBean.getProducto()!=null && m!=null){
            
            Producto p = productoBean.getProducto();            
            ItemProductoStock ip = m.getItemsProducto().get(m.getItemsProducto().size()-1);
            
            ip.setProducto(p);            
            ip.setUnidadMedida(p.getUnidadDeMedida());                                    
            ip.setAtributo1("");
            ip.setAtributo2("");
            ip.setAtributo3("");
            ip.setAtributo4("");
            ip.setAtributo5("");
            ip.setAtributo6("");
            ip.setAtributo7("");            
            
        }
    } 
    
//    public void procesarStock(){
//      
//        if(consultaStock.getItemStock()!=null && m!=null && item!=null){
//                   
//            Stock s = consultaStock.getItemStock();
//                        
//            item.setAtributo1(s.getAtributo1());
//            item.setAtributo2(s.getAtributo2());
//            item.setAtributo3(s.getAtributo3());
//            item.setAtributo4(s.getAtributo4());
//            item.setAtributo5(s.getAtributo5());
//            item.setAtributo6(s.getAtributo6());
//            item.setAtributo7(s.getAtributo7());             
//        }
//    } 
      
    public void nuevaBusqueda(){
                
        if(m!=null && m.getFormulario()!=null){
            formulario = m.getFormulario();
        }        
        buscaMovimiento = true;
    }
    
    public void resetParametros(){

//        formulario = null;
        numeroFormularioDesde = null;
        numeroFormularioHasta = null;
        fechaMovimientoDesde = null;
        fechaMovimientoHasta = null;
        muestraReporte = false;
        solicitaEmail = false;        
        movimientos = null;        
        
    }
    
    public void buscarMovimiento(){
        
        if(!validarParametros()){
            return;
        }
        cargarFiltroBusqueda();
        
        movimientos = movimientoStockRN.getListaByBusqueda(filtro, cantidadRegistros);
        
        if(movimientos==null || movimientos.isEmpty()){
            JsfUtil.addWarningMessage("No se han encontrado movimientos");
        }
    }
    
    public boolean validarParametros(){
        
        if(formulario==null){
            JsfUtil.addWarningMessage("Seleccione un formulario");
            return false;
        }
        
        if(numeroFormularioDesde!=null && numeroFormularioHasta!=null){
            if(numeroFormularioDesde > numeroFormularioHasta){
                JsfUtil.addWarningMessage("Número de formulario desde es mayor al número de formulario hasta");
                return false;
            }                    
        }                
        return true;
    }
    
    public void cargarFiltroBusqueda(){
        
        filtro.clear();
        
        if(formulario!=null){
            filtro.put("formulario.codigo = ","'"+formulario.getCodigo()+"'");
        }
        
        if(numeroFormularioDesde!=null){
            
            filtro.put("numeroFormulario >=",String.valueOf(numeroFormularioDesde));
        }

        if(numeroFormularioHasta!=null){
            
            filtro.put("numeroFormulario <=",String.valueOf(numeroFormularioHasta));
        }
        
        if(fechaMovimientoDesde!=null){
            
            filtro.put("fechaMovimiento >= ", JsfUtil.getFechaSQL(fechaMovimientoDesde));
        }
        
        if(fechaMovimientoHasta!=null){
            
            filtro.put("fechaMovimiento <= ", JsfUtil.getFechaSQL(fechaMovimientoHasta));
        }       
    }
    
    public void seleccionarMovimiento(MovimientoStock mSel){
        
        m = mSel;           
        buscaMovimiento = false;
    }
    
    public void procesarFormulario(){
      
        if(formularioStockBean.getFormulario()!=null){            
            formulario = formularioStockBean.getFormulario();
        }
    }
    
    /**
    public void procesarMascaraStock(){
      
        if(mascaraStockBean.getMascaraStock()!=null && m!=null){
            
            m.setMascara(mascaraStockBean.getMascaraStock());
            
            if(mascaraStockBean.getMascaraStock().getItems()!=null){
                
                m.getItemsProducto().clear();
                
                for(ItemMascaraStock im:mascaraStockBean.getMascaraStock().getItems()){
                    
                    ItemProductoStock ip = movimientoStockRN.nuevoItemProducto(m);                    
                    
                    ip.setProducto(im.getProducto());            
                    ip.setUnidadMedida(im.getProducto().getUnidadDeMedida());                        
                    ip.setPrecio(im.getProducto().getPrecioReposicion());
                    ip.setCantidad(im.getCantidad());
                    ip.setAtributo1("");
                    ip.setAtributo2("");
                    ip.setAtributo3("");
                    ip.setAtributo4("");
                    ip.setAtributo5("");
                    ip.setAtributo6("");
                    ip.setAtributo7("");  
                    ip.setTodoOk(true);
                    
                    m.getItemsProducto().add(ip);
                }
            }
        }
    } 
    */
    
    public void imprimir(){

        try {
            
            if (m.getFormulario().getNombreReporte() ==null){
                throw new ExcepcionGeneralSistema("El comprobante no tienen reporte asociado");
            }
            
            Map parameters = new HashMap();
            
            String pathReport = FacesContext.getCurrentInstance().getExternalContext().getRealPath(m.getFormulario().getNombreReporte()+".jasper");
            nombreArchivo = m.getComprobante().getCodigo()+"-"+m.getNumeroFormulario();

            
            parameters.put("ID", m.getId());
            parameters.put("CANT_COPIAS", m.getComprobante().getCopias());
           
            reportFactory.exportReportToPdfFile(pathReport, nombreArchivo, parameters);
            muestraReporte = true;

        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage("No se encontró el archivo: " + m.getFormulario().getNombreReporte()+".jasper");
            muestraReporte = false;
        } catch (ExcepcionGeneralSistema e){            
            JsfUtil.addErrorMessage("No se puede imprimir a pdf " +  e);
            muestraReporte = false;
        } catch (JRException e) {
            JsfUtil.addErrorMessage("No se puede imprimir a pdf " +  e);
            muestraReporte = false;
        }
    }
    
    //-------------------------------------------------------------------------

    public ProductoBean getProductoBean() {
        return productoBean;
    }

    public void setProductoBean(ProductoBean productoBean) {
        this.productoBean = productoBean;
    }

    public UsuarioSessionBean getUsuarioSessionBean() {
        return usuarioSessionBean;
    }

    public void setUsuarioSessionBean(UsuarioSessionBean usuarioSessionBean) {
        this.usuarioSessionBean = usuarioSessionBean;
    }
    
    public MovimientoStock getM() {
        return m;
    }

    public void setM(MovimientoStock m) {
        this.m = m;
    }

    public Date getFechaMinima() {
        return fechaMinima;
    }

    public void setFechaMinima(Date fechaMinima) {
        this.fechaMinima = fechaMinima;
    }

    public DepositoBean getDepositoBean() {
        return depositoBean;
    }

    public void setDepositoBean(DepositoBean depositoBean) {
        this.depositoBean = depositoBean;
    }
    
    public String getSUCURS() {
        return SUCURS;
    }

    public void setSUCURS(String SUCURS) {
        this.SUCURS = SUCURS;
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

    public List<MovimientoStock> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoStock> movimientos) {
        this.movimientos = movimientos;
    }

    public FormularioStockBean getFormularioStockBean() {
        return formularioStockBean;
    }

    public void setFormularioStockBean(FormularioStockBean formularioStockBean) {
        this.formularioStockBean = formularioStockBean;
    }
    
    public boolean isDetalleVacio() {
                  
        detalleVacio = true;
        
        if(m==null) return detalleVacio;
                
        if(m.getItemsProducto()!=null){
                        
            if(m.getItemsProducto().size()==1 && m.getId()!=null){                
                detalleVacio = false;                
            }
            
            if(m.getItemsProducto().size()>1){                
                detalleVacio = false;
            }
        }else{
            detalleVacio = true;
        }
        
        return detalleVacio;
    }
    
    @Override
    public void setDetalleVacio(boolean detalleVacio) {
        this.detalleVacio = detalleVacio;
    }
    
    public ItemProductoStock getItem() {
        return item;
    }

    public void setItem(ItemProductoStock item) {
        this.item = item;
    }    

    public ReportFactory getReportFactory() {
        return reportFactory;
    }

    public void setReportFactory(ReportFactory reportFactory) {
        this.reportFactory = reportFactory;
    }
    
}
