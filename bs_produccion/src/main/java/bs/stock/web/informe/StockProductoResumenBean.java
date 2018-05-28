/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.stock.web.informe;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.modelo.Temporal;
import bs.global.rn.TemporalRN;
import bs.global.util.InformeBase;
import bs.global.util.JsfUtil;
import bs.stock.modelo.Producto;
import bs.stock.rn.ProductoRN;
import bs.stock.web.ProductoBean;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Claudio
 */
@ManagedBean
@ViewScoped
public class StockProductoResumenBean extends InformeBase implements Serializable{
    
    @EJB ProductoRN productoRN;
    
    @EJB
    TemporalRN temporalRN;
    
    private Date fechaHoraHasta;    
    private Producto producto;
    private List<Producto> lista;
    
    @ManagedProperty(value = "#{productoBean}")
    protected ProductoBean productoBean;
            
    /**
     * Creates a new instance of ImpresionComprobanteFacturacionBean
     */
    public StockProductoResumenBean() {
    }
    
    @PostConstruct
    public void init(){
    
        fechaHoraHasta = new Date();
        
        productoBean.setCantidadRegistros(500);
        productoBean.buscar();
        
    }

    @Override
    public void validarDatos() throws ExcepcionGeneralSistema {
        
        String mensaje = "";
        todoOk = true;
        
        if(fechaHoraHasta==null){
            mensaje ="Fecha hasta no puede estar en blanco";
        }
                
        if(!mensaje.isEmpty()){
            todoOk = false;
            throw new ExcepcionGeneralSistema(mensaje);
        }        
    }

    @Override
    public void cargarParametros() throws ExcepcionGeneralSistema {
        
        parameters.put("FCHHHAS", JsfUtil.getTimeStampSQL(fechaHoraHasta));
               
        if (lista == null || lista.isEmpty()) {
            
        }else{
            guardarProductosSeleccionados(lista);
        }
                
        nombreArchivo = "ST_STOCK_PRODUCTO_FECHA";
        reporte = reporteRN.getReporte(codigoReporte);
        //pathReporte = "stock/informe/ST_STOCK_PRODUCTO_FECHA.jasper";
               
    }
        
    public void procesarProducto(){
        
        if(productoBean.getProducto()!=null){            
            
            producto = productoBean.getProducto();
            todoOk = false;
                        
        }
    }
    
    @Override
    public void resetParametros(){
        
        
        fechaHoraHasta = new Date();        
        producto = null; 
        todoOk = false;
        muestraReporte = false;
        
    }
    
    public void guardarProductosSeleccionados(List<Producto> seleccion){
        
        temporalRN.vaciarTabla();
        
        for(Producto p:seleccion){
            
            try {
                Temporal t = new Temporal(p.getCodigo());
                temporalRN.guardar(t);
            } catch (Exception ex) {
                Logger.getLogger(StockDepositoFechaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public void onItemSelectProducto(SelectEvent event) {
        producto = (Producto) event.getObject();  
    }

    public Date getFechaHoraHasta() {
        return fechaHoraHasta;
    }

    public void setFechaHoraHasta(Date fechaHoraHasta) {
        this.fechaHoraHasta = fechaHoraHasta;
    }
    
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public ProductoBean getProductoBean() {
        return productoBean;
    }

    public void setProductoBean(ProductoBean productoBean) {
        this.productoBean = productoBean;
    }

    public List<Producto> getLista() {
        return lista;
    }

    public void setLista(List<Producto> lista) {
        this.lista = lista;
    }
    
    
}
