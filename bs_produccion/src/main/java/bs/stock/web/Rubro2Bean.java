/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.stock.web;

import bs.global.web.GenericBean;
import bs.global.util.JsfUtil;
import bs.stock.modelo.Rubro2;
import bs.stock.modelo.TipoProducto;
import bs.stock.rn.Rubro2RN;
import bs.stock.rn.TipoProductoRN;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;

 
 
@ManagedBean
@ViewScoped
public class Rubro2Bean extends GenericBean implements Serializable{
    
    private Rubro2 rubro;
    private TipoProducto tipoProducto;
    private List<Rubro2> lista;
        
    @EJB private Rubro2RN rubroRN;
    @EJB private TipoProductoRN tipoProductoRN;
    
    public Rubro2Bean(){
        
    }
    
    @PostConstruct
    public void init(){
        txtBusqueda = "";
        mostrarDebaja = false;
        
        tipoProducto = tipoProductoRN.getLista(false).get(0);
        
        nuevo();
        buscar();
    }
    
    public void nuevo(){
        
        esNuevo = true;
        buscaMovimiento = false;
        rubro = new Rubro2();
                
        if(tipoProducto != null){
            rubro.setTipoProducto(tipoProducto.getCodigo());            
        }        
    }
    
    public void guardar(boolean nuevo){
        
        try {
            if (rubro != null) {
                
                rubroRN.guardar(rubro, esNuevo);
                esNuevo = false;   
                
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
                
                if (nuevo){
                    nuevo();
                }
            }else{
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(Rubro2Bean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }
    
   
    
    public void habilitaDeshabilita(String habDes){
        
        try {
            rubro.getAuditoria().setDebaja(habDes);
            rubroRN.guardar(rubro, false);            
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");
            
        } catch (Exception ex) {
            Logger.getLogger(Rubro2Bean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }
    
    
    public void eliminar(){
        try {
            rubroRN.eliminar(rubro);
            nuevo();            
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }        
    }
    
    public void buscar(){
        
        String codTipo = "";
        
        if(tipoProducto!=null){
            codTipo = tipoProducto.getCodigo();
        }
        
        lista = rubroRN.getListaByBusqueda(codTipo,txtBusqueda, mostrarDebaja,cantidadRegistros);        
        
    }
    
    
    public List<Rubro2> complete(String query) {
        try {
            lista = rubroRN.getListaByBusqueda(tipoProducto.getCodigo(),query, false,10);
            return lista;
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Rubro2>();
        }
    }
    
    public void onSelect(SelectEvent event) {
        rubro = (Rubro2) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void seleccionar (Rubro2 r){
        
        rubro = r;        
        esNuevo = false;
        buscaMovimiento = false;
    }
    
//    public void imprimir(){
//        
//        if(rubro==null){
//            JsfUtil.addErrorMessage("No hay entidad seleccionada, nada para imprimir");
//        }
//    }
    
    
    public void asignarTipo(){
        
        if(tipoProducto!=null){
            rubro.setTipoProducto(tipoProducto.getCodigo());
        }
        
    }
    
    
    public Rubro2 getRubro() {
        return rubro;
    }

    public void setRubro(Rubro2 r) {
        this.rubro = r;
    }

    public List<Rubro2> getLista() {
        return lista;
    }

    public void setLista(List<Rubro2> lista) {
        this.lista = lista;
    }   

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
    
    
}
