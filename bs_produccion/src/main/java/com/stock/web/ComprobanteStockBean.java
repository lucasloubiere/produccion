/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stock.web;


import com.global.modelo.FormularioPorSituacionIVA;
import com.global.rn.FormularioPorSituacionIVARN;
import com.global.util.GenericBean;
import com.global.util.JsfUtil;
import com.global.web.FormularioSituacionIVABean;
import com.stock.modelo.ComprobanteStock;
import com.stock.rn.ComprobanteStockRN;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Claudio
 */
@ManagedBean
@ViewScoped
public class ComprobanteStockBean extends GenericBean implements Serializable{

    @EJB private ComprobanteStockRN comprobanteRN;
    @EJB private FormularioPorSituacionIVARN formularioSituacionIvaRN;
    
    
    private ComprobanteStock comprobante;
    private List<ComprobanteStock> lista;
    
    private boolean mostrarConceptoDebaja;
    private boolean mostrarFormularioSituacionIvaDebaja;
    
    @ManagedProperty(value = "#{formularioSituacionIVABean}")
    private FormularioSituacionIVABean formularioSituacionIVABean;
    
    /**
     * Creates a new instance of ComprobanteStockBean
     */
    public ComprobanteStockBean() {
        
    }
    
    @PostConstruct
    public void init(){
        
        super.iniciar();
        
        mostrarConceptoDebaja = false;        
        nuevo();
        buscar();        
        formularioSituacionIVABean.iniciarFiltro("ST");
    }
    
    public void nuevo(){
        
        esNuevo = true;
        buscaMovimiento = false;
        comprobante = new ComprobanteStock();
        comprobante.setModulo("ST");
    }
    
    public void guardar(boolean nuevo){
                
        try {
            if (comprobante != null) {
                
                comprobanteRN.guardar(comprobante, esNuevo);
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
    
    public void habilitaDeshabilita(String habDes){
        
        try {
            comprobante.getAuditoria().setDebaja(habDes);
            comprobanteRN.guardar(comprobante, false);            
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");
            
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " +  ex);
        }
    }
        
    public void eliminar(){
        try {
            comprobanteRN.eliminar(comprobante);
            nuevo();            
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " +  ex);
        }        
    }
    
    public void buscar(){
        lista = comprobanteRN.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);        
    } 
    
    public List<ComprobanteStock> complete(String query) {
        try {
            lista = comprobanteRN.getListaByBusqueda(query, false,cantidadRegistros);    
            return lista;
            
        } catch (Exception ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<ComprobanteStock>();
        }
    }
    
    public void onSelect(SelectEvent event) {        
        comprobante = (ComprobanteStock) event.getObject(); 
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void seleccionar(ComprobanteStock v){
        
        comprobante = v;
        esNuevo = false;
        buscaMovimiento = false;
    }

    public void imprimir(){
        
        if(comprobante==null){
            JsfUtil.addErrorMessage("No hay entidad seleccionada, nada para imprimir");
        }        
    }
        
    public void nuevoFormularioSituacionIva(){
        
        if(comprobante!=null){
            
            if(comprobante.getFormulariosPorSituacionIVA()==null){
                comprobante.setFormulariosPorSituacionIVA(new ArrayList<FormularioPorSituacionIVA>());
            }
            
            formularioSituacionIVABean.setComprobante(comprobante);
            formularioSituacionIVABean.nuevo();
            
        }else{
            
            JsfUtil.addErrorMessage("No existe un comprobante para asociarle un formulario por situación de iva");
        }        
    }
    
    public void seleccionarFormulario(FormularioPorSituacionIVA fsi){
        
        if(comprobante!=null){
            
            formularioSituacionIVABean.seleccionar(fsi);
            
        }else{
            
            JsfUtil.addErrorMessage("No existe un comprobante para asociarle un formulario por situación de iva");
        }           
    }
    
    public void sincronizarFormulario(){
        
        if(comprobante!=null){
            comprobante.setFormulariosPorSituacionIVA(formularioSituacionIvaRN.getListaByComprobante(comprobante, mostrarFormularioSituacionIvaDebaja));            
        }
    }
    
    //---------------------------------------------------------------------------
    
    public ComprobanteStock getComprobante() {
        return comprobante;
    }

    public void setComprobante(ComprobanteStock comprobante) {
        this.comprobante = comprobante;
    }

    public List<ComprobanteStock> getLista() {
        return lista;
    }

    public void setLista(List<ComprobanteStock> lista) {
        this.lista = lista;
    }

    public boolean isMostrarConceptoDebaja() {
        return mostrarConceptoDebaja;
    }

    public void setMostrarConceptoDebaja(boolean mostrarConceptoDebaja) {
        this.mostrarConceptoDebaja = mostrarConceptoDebaja;
    }

    public FormularioSituacionIVABean getFormularioSituacionIVABean() {
        return formularioSituacionIVABean;
    }

    public void setFormularioSituacionIVABean(FormularioSituacionIVABean formularioSituacionIVABean) {
        this.formularioSituacionIVABean = formularioSituacionIVABean;
    }

    public boolean isMostrarFormularioSituacionIvaDebaja() {
        return mostrarFormularioSituacionIvaDebaja;
    }

    public void setMostrarFormularioSituacionIvaDebaja(boolean mostrarFormularioSituacionIvaDebaja) {
        this.mostrarFormularioSituacionIvaDebaja = mostrarFormularioSituacionIvaDebaja;
    }
    
}
