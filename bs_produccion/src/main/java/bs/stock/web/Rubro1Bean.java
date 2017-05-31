/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.stock.web;

import bs.global.web.GenericBean;
import bs.global.util.JsfUtil;
import bs.stock.modelo.Rubro1;
import bs.stock.modelo.TipoProducto;
import bs.stock.rn.Rubro1RN;
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
public class Rubro1Bean extends GenericBean implements Serializable{
    
    private Rubro1 rubro;
    private TipoProducto tipoProducto;
    private List<Rubro1> lista;
        
    @EJB private Rubro1RN rubroRN;
    @EJB private TipoProductoRN tipoProductoRN;
    
    public Rubro1Bean(){
        
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
        rubro = new Rubro1();
                
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
            Logger.getLogger(Rubro1Bean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }
    
   
    
    public void habilitaDeshabilita(String habDes){
        
        try {
            rubro.getAuditoria().setDebaja(habDes);
            rubroRN.guardar(rubro, false);            
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");
            
        } catch (Exception ex) {
            Logger.getLogger(Rubro1Bean.class.getName()).log(Level.SEVERE, null, ex);
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
    
    
    public List<Rubro1> complete(String query) {
        try {
            lista = rubroRN.getListaByBusqueda(tipoProducto.getCodigo(),query, false,10);
            return lista;
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Rubro1>();
        }
    }
    
    public void onSelect(SelectEvent event) {
        rubro = (Rubro1) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void seleccionar (Rubro1 r){
        
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
    
    
    public Rubro1 getRubro() {
        return rubro;
    }

    public void setRubro(Rubro1 r) {
        this.rubro = r;
    }

    public List<Rubro1> getLista() {
        return lista;
    }

    public void setLista(List<Rubro1> lista) {
        this.lista = lista;
    }   

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
    
    
}

