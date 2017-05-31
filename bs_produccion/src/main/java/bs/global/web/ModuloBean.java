/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.global.web;

import bs.global.util.JsfUtil;
import bs.global.modelo.Modulo;
import bs.global.rn.ModuloRN;
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
 * @author lloubiere
 */

@ManagedBean
@ViewScoped

public class ModuloBean extends GenericBean implements Serializable {
 private Modulo modulo;
    private List<Modulo> lista;
        
    @EJB private ModuloRN moduloRN;
    
    public ModuloBean(){
        
    }
    
    @PostConstruct
    public void init(){
        txtBusqueda = "";
        mostrarDebaja = false;
        nuevo();
        buscar();
    }
    
    public void nuevo(){
        
        esNuevo = true;
        buscaMovimiento = false;
        modulo = new Modulo();
    }
    
    public void guardar(boolean nuevo){
        
        try {
            if (modulo != null) {
                
                moduloRN.guardar(modulo, esNuevo);
                esNuevo = false;                
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
                
                if (nuevo){
                    nuevo();
                }
            }else{
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(ModuloBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }
    
    public void habilitaDeshabilita(String habDes){
        
        try {
            modulo.getAuditoria().setDebaja(habDes);
            moduloRN.guardar(modulo, false);            
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");
            
        } catch (Exception ex) {
            Logger.getLogger(ModuloBean.class.getName()).log(Level.SEVERE, null, ex);
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
        lista = moduloRN.getListaByBusqueda(txtBusqueda, mostrarDebaja,cantidadRegistros);
    }
    
    public List<Modulo> complete(String query) {
        try {
            lista = moduloRN.getListaByBusqueda(query, false, cantidadRegistros);
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
    
    public void seleccionar (Modulo d){
        
        modulo = d;
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void imprimir(){
        
        if(modulo==null){
            JsfUtil.addErrorMessage("No hay entidad seleccionada, nada para imprimir");
        }
    }
    
    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public List<Modulo> getLista() {
        return lista;
    }

    public void setLista(List<Modulo> lista) {
        this.lista = lista;
    }
    
}
