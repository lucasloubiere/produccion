/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguridad.web;

import com.global.util.GenericBean;
import com.global.util.JsfUtil;
import com.seguridad.modelo.TipoUsuario;
import com.seguridad.rn.TipoUsuarioRN;
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

public class TipoUsuarioBean extends GenericBean implements Serializable{

    private TipoUsuario tipoUsuario;
    private List<TipoUsuario> lista;
  
  @EJB private TipoUsuarioRN tipoUsuarioRN;
  
  public TipoUsuarioBean(){
    
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
        tipoUsuario = new TipoUsuario();
    }
    
    public void guardar(boolean nuevo){
        
        try {
            if (tipoUsuario != null) {
                
                tipoUsuarioRN.guardar(tipoUsuario, esNuevo);
                esNuevo = false;
                buscar();                
                JsfUtil.addInfoMessage("Los datos seguardaron correctamente");                
                if (nuevo){
                    nuevo();
                }
            }else{
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(TipoUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }
    
    public void habilitaDeshabilita(String habDes){
        
        try {
            tipoUsuario.getAuditoria().setDebaja(habDes);
            tipoUsuarioRN.guardar(tipoUsuario, false);
            buscar();            
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");
            
        } catch (Exception ex) {
            Logger.getLogger(TipoUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }
    
    public void eliminar(){
        try {
            tipoUsuarioRN.eliminar(tipoUsuario);
            nuevo();
            buscar();            
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }        
    }
    
    public void buscar(){
        lista = tipoUsuarioRN.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
    }
    
    public List<TipoUsuario> complete(String query) {
        try {
            lista = tipoUsuarioRN.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
            return lista;
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<TipoUsuario>();
        }
    }
    
    public void onSelect(SelectEvent event) {
        tipoUsuario = (TipoUsuario) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void seleccionar(TipoUsuario d){
        
        tipoUsuario = d;
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void imprimir(){
        
        if(tipoUsuario==null){
            JsfUtil.addErrorMessage("TipoUsuario no seleccionado, nada para imprimir");
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<TipoUsuario> getLista() {
        return lista;
    }

    public void setLista(List<TipoUsuario> lista) {
        this.lista = lista;
    }
}
