/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguridad.web;

import com.global.util.GenericBean;
import com.global.util.JsfUtil;
import com.seguridad.modelo.EstadoUsuario;
import com.seguridad.rn.EstadoUsuarioRN;
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
 * @author ide
 */
@ManagedBean
@ViewScoped
public class EstadoUsuarioBean extends GenericBean implements Serializable {

  private EstadoUsuario estadoUsuario;
  private List<EstadoUsuario> lista;
  
  @EJB private EstadoUsuarioRN estadoUsuarioRN;
    
    public EstadoUsuarioBean(){
        
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
        estadoUsuario = new EstadoUsuario();
    }
    
    public void guardar(boolean nuevo){
        
        try {
            if (estadoUsuario != null) {
                
                estadoUsuarioRN.guardar(estadoUsuario, esNuevo);
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
            Logger.getLogger(EstadoUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }
    
    public void habilitaDeshabilita(String habDes){
        
        try {
            estadoUsuario.getAuditoria().setDebaja(habDes);
            estadoUsuarioRN.guardar(estadoUsuario, false);
            buscar();            
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");
            
        } catch (Exception ex) {
            Logger.getLogger(EstadoUsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }
    
    public void eliminar(){
        try {
            estadoUsuarioRN.eliminar(estadoUsuario);
            nuevo();
            buscar();            
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }        
    }
    
    public void buscar(){
        lista = estadoUsuarioRN.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
    }
    
    public List<EstadoUsuario> complete(String query) {
        try {
            lista = estadoUsuarioRN.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
            return lista;
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<EstadoUsuario>();
        }
    }
    
    public void onSelect(SelectEvent event) {
        estadoUsuario = (EstadoUsuario) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void seleccionar(EstadoUsuario d){
        
        estadoUsuario = d;
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void imprimir(){
        
        if(estadoUsuario==null){
            JsfUtil.addErrorMessage("EstadoUsuario no seleccionado, nada para imprimir");
        }
    }
    
    public EstadoUsuario getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(EstadoUsuario deposito) {
        this.estadoUsuario = deposito;
    }

    public List<EstadoUsuario> getLista() {
        return lista;
    }

    public void setLista(List<EstadoUsuario> lista) {
        this.lista = lista;
    }
}