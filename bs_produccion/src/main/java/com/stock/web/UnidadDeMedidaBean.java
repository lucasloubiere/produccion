/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.web;

import com.global.util.GenericBean;
import com.global.util.JsfUtil;
import com.stock.modelo.UnidadMedida;
import com.stock.rn.UnidadDeMedidaRN;
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
public class UnidadDeMedidaBean extends GenericBean {

    private UnidadMedida unidadDeMedida;
    private List<UnidadMedida> lista;
    @EJB
    private UnidadDeMedidaRN unidadDeMedidaRN;

    /**
     * Creates a new instance of UnidadDeMedidaBean
     */
    public UnidadDeMedidaBean() {

    }

    @PostConstruct
    public void init() {

        txtBusqueda = "";
        mostrarDebaja = false;
        nuevo();
        buscar();
    }

    public void nuevo() {

        esNuevo = true;
        buscaMovimiento = false;
        unidadDeMedida = new UnidadMedida();
    }

    public void guardar(boolean nuevo) {

        try {
            if (unidadDeMedida != null) {
                unidadDeMedidaRN.guardar(unidadDeMedida, esNuevo);
                esNuevo = false;
                buscar(); 
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
                if (nuevo){
                    nuevo();
                }
            } else {
                JsfUtil.addErrorMessage("No hay datos para guardar");
            }
        } catch (Exception exception) {
        }
    }

    public void habilitaDeshabilita(String habDes) {
        try {
            unidadDeMedida.getAuditoria().setDebaja(habDes);
            unidadDeMedidaRN.guardar(unidadDeMedida, false);
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");

        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }
    }

    public void eliminar() {

        try {
            unidadDeMedidaRN.eliminar(unidadDeMedida);
            nuevo();
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);

        }
    }

    public void buscar() {
        
         lista = unidadDeMedidaRN.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
    
    }

    public List<UnidadMedida> complete(String query) {
        try {
            lista = unidadDeMedidaRN.getListaByBusqueda(query, false, cantidadRegistros);    
            return lista;
            
        } catch (Exception ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<UnidadMedida>();
        }
    }

    
    public void onSelect(SelectEvent event) {
        unidadDeMedida = (UnidadMedida) event.getObject(); 
        esNuevo = false;
        buscaMovimiento = false;
        
    }

    public void seleccionar(UnidadMedida u) {
        unidadDeMedida = u;
        esNuevo = false;
        buscaMovimiento = false;

    }

    public UnidadMedida getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(UnidadMedida unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
    }

    public List<UnidadMedida> getLista() {
        return lista;
    }

    public void setLista(List<UnidadMedida> lista) {
        this.lista = lista;
    }

}
