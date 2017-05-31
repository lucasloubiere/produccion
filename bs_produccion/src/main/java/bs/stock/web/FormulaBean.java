/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.web;

import bs.global.web.GenericBean;
import bs.global.util.JsfUtil;
import bs.stock.modelo.Formula;
import bs.stock.rn.FormulaRN;
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
public class FormulaBean extends GenericBean implements Serializable{
    
    private Formula formula;
    private List<Formula> lista;
        
    @EJB private FormulaRN formulaRN;
    
    public FormulaBean(){
        
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
        formula = new Formula();
    }
    
    public void guardar(boolean nuevo){
        
        try {
            if (formula != null) {
                
                formulaRN.guardar(formula, esNuevo);
                esNuevo = false;                
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
                
                if (nuevo){
                    nuevo();
                }
            }else{
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(FormulaBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }
    
    public void habilitaDeshabilita(String habDes){
        
        try {
            formula.getAuditoria().setDebaja(habDes);
            formulaRN.guardar(formula, false);            
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");
            
        } catch (Exception ex) {
            Logger.getLogger(FormulaBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }
    
    public void eliminar(){
        try {
            formulaRN.eliminar(formula);
            nuevo();            
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }        
    }
    
    public void buscar(){
        lista = formulaRN.getListaByBusqueda(txtBusqueda, mostrarDebaja,cantidadRegistros);
    }
    
    public List<Formula> complete(String query) {
        try {
            lista = formulaRN.getListaByBusqueda(query, false, cantidadRegistros);
            return lista;
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Formula>();
        }
    }
    
    public void onSelect(SelectEvent event) {
        formula = (Formula) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void seleccionar (Formula d){
        
        formula = d;
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void imprimir(){
        
        if(formula==null){
            JsfUtil.addErrorMessage("No hay entidad seleccionada, nada para imprimir");
        }
    }
    
    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public List<Formula> getLista() {
        return lista;
    }

    public void setLista(List<Formula> lista) {
        this.lista = lista;
    }
    
}
