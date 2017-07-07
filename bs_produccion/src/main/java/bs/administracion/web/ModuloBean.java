/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.administracion.web;

import bs.administracion.modelo.Modulo;
import bs.administracion.rn.ModuloRN;
import bs.global.util.JsfUtil;
import bs.global.web.GenericBean;
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

/**
 *
 * @author Agustin
 */
@ManagedBean
@ViewScoped
public class ModuloBean extends GenericBean implements Serializable{
    
    private Modulo modulo;
    private List<Modulo> lista;
        
    @EJB private ModuloRN moduloRN;
    
    public ModuloBean() {
        
    }
    
    @PostConstruct
    public void init(){
        
        super.iniciar();
        nuevo();
        buscar();
    }
    
    public void nuevo(){
        
        esNuevo = true;
        buscaMovimiento = false;
        modulo = new Modulo();
    }
    
    public void guardar(boolean nuevo) {
        
        try{
            if (modulo != null) {
                
                moduloRN.guardar(modulo, esNuevo);
                esNuevo = false;                
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
                
                if(nuevo){
                    nuevo();
                }
            }else{
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }
    
    public void habilitaDeshabilita(String habDes) {
        
        try {
            modulo.getAuditoria().setDebaja(habDes);
            moduloRN.guardar(modulo, false);            
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");
            
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }
    
    public void eliminar(){
        try {
            moduloRN.eliminar(modulo);
            nuevo();            
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }
    }
    
    public void buscar(){
        lista = moduloRN.getModuloByBusqueda(txtBusqueda, mostrarDebaja);
    }
    
    public List<Modulo> complete(String query) {
        try {
            lista = moduloRN.getModuloByBusqueda(query, false);
            return lista;
        } catch (Exception ex) {
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Modulo>();
        }
    }
    
    public void onSelect(SelectEvent event) {        
        modulo = (Modulo) event.getObject(); 
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void seleccionar(Modulo m){
        
        modulo = m;
        esNuevo = false;
        buscaMovimiento = false;
    }

    public void imprimir(){
        
        if(modulo==null){
            JsfUtil.addErrorMessage("No hay entidad seleccionada, nada para imprimir");
        }        
    }
    
    public Modulo getModulo () {
        return modulo ;
    }

    public void setModulo (Modulo  m) {
        this.modulo = m;
    }

    public List<Modulo > getLista() {
        return lista;
    }

    public void setLista(List<Modulo > lista) {
        this.lista = lista;
    }
}
