/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.web;

import com.global.util.GenericBean;
import com.global.util.JsfUtil;
import com.stock.modelo.UnidadDeMedida;
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

    private UnidadDeMedida unidadDeMedida;
    private List<UnidadDeMedida> lista;
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
        unidadDeMedida = new UnidadDeMedida();
    }

    public void guardar(boolean nuevo) {

        try {
            if (unidadDeMedida == null) {
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

    public void habilitaDesahabilita(String habDes) {
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

    public List<UnidadDeMedida> complete(String query) {
        try {
            lista = unidadDeMedidaRN.getListaByBusqueda(query, false, cantidadRegistros);    
            return lista;
            
        } catch (Exception ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<UnidadDeMedida>();
        }
    }

    
    public void onselec(SelectEvent event) {
        unidadDeMedida = (UnidadDeMedida) event.getObject(); 
        esNuevo = false;
        buscaMovimiento = false;
        
    }

    public void seleccionar(UnidadDeMedida u) {
        unidadDeMedida = u;
        esNuevo = false;
        buscaMovimiento = false;

    }

    public UnidadDeMedida getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(UnidadDeMedida unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
    }

    public List<UnidadDeMedida> getLista() {
        return lista;
    }

    public void setLista(List<UnidadDeMedida> lista) {
        this.lista = lista;
    }

}
