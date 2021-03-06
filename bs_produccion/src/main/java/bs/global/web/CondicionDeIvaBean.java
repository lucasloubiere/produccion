/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.global.web;

import bs.global.modelo.CondicionDeIva;
import bs.global.rn.CondicionDeIvaRN;
import bs.global.util.JsfUtil;
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

public class CondicionDeIvaBean extends GenericBean implements Serializable {
    
    private CondicionDeIva condicionDeIva;
    private List<CondicionDeIva> lista;        
    
    
    @EJB private CondicionDeIvaRN condicionDeIvaRN;
    
    public CondicionDeIvaBean() {
        
    }
    
    @PostConstruct
    public void init(){
        txtBusqueda = "";
        mostrarDebaja = false;        
        nuevo();
        buscar();
    }
    
    public void nuevo(){
        condicionDeIva = new CondicionDeIva();
        esNuevo = true;
        buscaMovimiento = false;
    }
    
    public void guardar(boolean nuevo){
        
        try {
            if (condicionDeIva != null) {
                condicionDeIvaRN.guardar(condicionDeIva, esNuevo);
                esNuevo = false;                
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
            }
        } catch (Exception ex) {
            Logger.getLogger(CondicionDeIvaBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }
    
    public void eliminar(){
        try {
            condicionDeIvaRN.eliminar(condicionDeIva);
            nuevo();            
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(CondicionDeIvaBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }        
    }
    
    public void buscar(){
                
        lista = condicionDeIvaRN.getListaByBusqueda(txtBusqueda, mostrarDebaja);        
    }    
    
    public void onSelect(SelectEvent event) {        
        condicionDeIva = (CondicionDeIva) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void seleccionar(CondicionDeIva c){
        condicionDeIva = c;
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public List<CondicionDeIva> complete(String query) {
        try {
            lista = condicionDeIvaRN.getListaByBusqueda(query, false);    
            return lista;
            
        } catch (Exception ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<CondicionDeIva>();
        }
    }    
     
    public void habilitaDeshabilita(String habDes){
        
        try {
            condicionDeIva.getAuditoria().setDebaja(habDes);
            condicionDeIvaRN.guardar(condicionDeIva, false);            
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }
    
    public CondicionDeIva getCondicionDeIva() {
        return condicionDeIva;
    }

    public void setCondicionDeIva(CondicionDeIva condicionDeIva) {
        this.condicionDeIva = condicionDeIva;
    }

    public List<CondicionDeIva> getLista() {
        return lista;
    }

    public void setLista(List<CondicionDeIva> lista) {
        this.lista = lista;
    }    
    
   
}


