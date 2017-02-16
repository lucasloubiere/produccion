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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

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
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
            } else {
                JsfUtil.addErrorMessage("No hay datos para guardar");
            }
        } catch (Exception exception) {
        }
    }

    public void habilitaDesahabilita(String habdes) {
        
    }

    public void eliminar() {
    }

    public void buscar() {
    }

    public void complete() {
    }

    public void onselec() {
    }

    public void seleccionar() {

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
