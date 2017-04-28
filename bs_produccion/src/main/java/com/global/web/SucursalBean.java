/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.web;

import com.global.util.GenericBean;
import com.global.util.JsfUtil;
import com.global.modelo.Sucursal;
import com.global.rn.SucursalRN;
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

public class SucursalBean extends GenericBean implements Serializable  {
    
    private Sucursal sucursal;
    private List<Sucursal> lista;
        
    @EJB private SucursalRN sucursalRN;
    
    public SucursalBean(){
        
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
        sucursal = new Sucursal();
    }
    
    public void guardar(boolean nuevo){
        
        try {
            if (sucursal != null) {
                
                sucursalRN.guardar(sucursal, esNuevo);
                esNuevo = false;                
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
                
                if (nuevo){
                    nuevo();
                }
            }else{
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(SucursalBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }
    
    public void habilitaDeshabilita(String habDes){
        
        try {
            sucursal.getAuditoria().setDebaja(habDes);
            sucursalRN.guardar(sucursal, false);            
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");
            
        } catch (Exception ex) {
            Logger.getLogger(SucursalBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }
    
    public void eliminar(){
        try {
            sucursalRN.eliminar(sucursal);
            nuevo();            
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }        
    }
    
    public void buscar(){
        lista = sucursalRN.getListaByBusqueda(txtBusqueda, mostrarDebaja,cantidadRegistros);
    }
    
    public List<Sucursal> complete(String query) {
        try {
            lista = sucursalRN.getListaByBusqueda(query, false, cantidadRegistros);
            return lista;
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Sucursal>();
        }
    }
    
    public void onSelect(SelectEvent event) {
        sucursal = (Sucursal) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void seleccionar (Sucursal d){
        
        sucursal = d;
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void imprimir(){
        
        if(sucursal==null){
            JsfUtil.addErrorMessage("No hay entidad seleccionada, nada para imprimir");
        }
    }
    
    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public List<Sucursal> getLista() {
        return lista;
    }

    public void setLista(List<Sucursal> lista) {
        this.lista = lista;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
