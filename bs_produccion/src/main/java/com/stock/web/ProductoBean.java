/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.web;

import com.global.util.GenericBean;
import com.global.util.JsfUtil;
import com.stock.modelo.Producto;
import com.stock.modelo.TipoProducto;
import com.stock.rn.ProductoRN;
import com.stock.rn.TipoProductoRN;
import java.io.Serializable;
import java.util.ArrayList;
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
 * @author lloubiere
 */
@ManagedBean
@ViewScoped
public class ProductoBean extends GenericBean implements Serializable{

    @EJB private ProductoRN productoRN;
    @EJB private TipoProductoRN tipoProductoRN;

    private Producto producto;
    private List<Producto> lista;
    
    private List<TipoProducto> tipos;
    private TipoProducto tipoProducto;

    
    private boolean mostrarEquivalenciaDebaja;
  
    @ManagedProperty(value = "#{rubro1Bean}")
    private Rubro1Bean rubro1Bean;
    
    @ManagedProperty(value = "#{rubro2Bean}")
    private Rubro2Bean rubro2Bean; 
   

    
    /** Creates a new instance of ProductoBean */
    public ProductoBean() {
        
    }

    @PostConstruct
    public void init(){
        
        super.iniciar();
                
        tipos = tipoProductoRN.getLista(false);
        if(!tipos.isEmpty()){
            tipoProducto = tipos.get(0);
        }               
                                                 
        
        nuevo();        
        buscar();
    }
    
    public void setearTipo(String codTipo){
        
       if(codTipo!=null){
           tipoProducto = tipoProductoRN.getTipoProducto(codTipo);
//           buscar();
       } 
    }
         
    public void nuevo() {

        producto = new Producto();        
        esNuevo = true;  
        buscaMovimiento = false;
    }

    
    public void guardar(boolean nuevo){        
        try {
            if (producto != null) {
                
                if(!puedoGuardar()) return;
                
                productoRN.guardar(producto, esNuevo);
                esNuevo = false;
                buscaMovimiento = false;
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
                
                if(nuevo){
                    nuevo();
                }
                
            }else{
                JsfUtil.addErrorMessage("No hay datos para guardar");
            }
            
            
        } catch (Exception ex) {
            Logger.getLogger(ProductoBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }
    
    public boolean puedoGuardar(){
        
        if(producto.getCodigo()==null || producto.getCodigo().isEmpty()){
            JsfUtil.addErrorMessage("El código no puede estar vacío");
            return false;
        }
        
        return true;        
    }

    public void habilitaDeshabilita(String habDes){
        
        try {
            producto.getAuditoria().setDebaja(habDes);
            productoRN.guardar(producto,false);
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");
            
        } catch (Exception ex) {
            Logger.getLogger(ProductoBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }
    
    public void eliminar(){
        try {
            productoRN.eliminar(producto);
            nuevo();
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(ProductoBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }        
    }
    
    public void buscar(){
        
        String codTipo = "";
        
        if(tipoProducto!=null){
            codTipo = tipoProducto.getCodigo();            
        }
        
        lista = productoRN.getListaByBusqueda(codTipo,txtBusqueda, mostrarDebaja, cantidadRegistros);
    }    
    
    public void onSelect(SelectEvent event) {  
        
        producto = (Producto) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
        
    }
    
    public void seleccionar(Producto e){
        producto = e;
        esNuevo = false;
        buscaMovimiento = false;
        }
    
    public List<Producto> complete(String query) {
        try {
            String codTipo = "";
        
            if(tipoProducto!=null){
                codTipo = tipoProducto.getCodigo();            
            }
                        
            lista = productoRN.getListaByBusqueda(codTipo, query,mostrarDebaja,10);
            return lista;
            
        } catch (Exception ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Producto>();
        }
    }
    
    
    public void asignarCodigoProducto(){
        
        System.err.println("asignarCodigoProducto");
        
        if(producto.getTipoProducto()==null){
            JsfUtil.addWarningMessage("Seleccione el tipo para poder generar el código del producto");
            producto.setCodigo("");
            return;
        }
        
        if(producto.getRubro1() == null){
            JsfUtil.addWarningMessage("Seleccione el rubro para poder generar el código del producto");
            producto.setCodigo("");
            return;
        }
        
        if(producto.getRubro2() == null){
            JsfUtil.addWarningMessage("Seleccione el sub-rubro para poder generar el código del producto");
            producto.setCodigo("");
            return;
        }
        
        String codigo = productoRN.getProximoCodigo(producto.getTipoProducto().getCodigo(), 
                producto.getRubro1().getCodigo(), 
                producto.getRubro2().getCodigo());
        
        producto.setCodigo(codigo);
        
    }
    
    public void limpiarCodigo(){
                
        if(producto.getTipoProducto()!=null){
            tipoProducto = producto.getTipoProducto();
        }
        
        rubro1Bean.setTipoProducto(tipoProducto);
        rubro1Bean.buscar();

        rubro2Bean.setTipoProducto(tipoProducto);
        rubro2Bean.buscar();
        
        producto.setRubro1(null);
        producto.setRubro2(null);
        producto.setCodigo("");
    }
   
   
   
    
    public List<Producto> getLista() {
        return lista;
    }

    public void setLista(List<Producto> lista) {
        this.lista = lista;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }    

    public List<TipoProducto> getTipos() {
        return tipos;
    }

    public void setTipos(List<TipoProducto> tipos) {
        this.tipos = tipos;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

  

    public Rubro1Bean getRubro1Bean() {
        return rubro1Bean;
    }

    public void setRubro1Bean(Rubro1Bean rubro1Bean) {
        this.rubro1Bean = rubro1Bean;
    }

    public Rubro2Bean getRubro2Bean() {
        return rubro2Bean;
    }

    public void setRubro2Bean(Rubro2Bean rubro2Bean) {
        this.rubro2Bean = rubro2Bean;
    }
 

    public boolean isMostrarEquivalenciaDebaja() {
        return mostrarEquivalenciaDebaja;
    }

    public void setMostrarEquivalenciaDebaja(boolean mostrarEquivalenciaDebaja) {
        this.mostrarEquivalenciaDebaja = mostrarEquivalenciaDebaja;
    }
    
    
}
