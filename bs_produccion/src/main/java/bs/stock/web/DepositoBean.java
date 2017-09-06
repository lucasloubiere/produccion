/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.web;

import bs.global.util.JsfUtil;
import bs.global.web.GenericBean;
import bs.stock.modelo.Deposito;
import bs.stock.rn.DepositoRN;
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
public class DepositoBean extends GenericBean implements Serializable{
    
    private Deposito deposito;
    private List<Deposito> lista;
        
    @EJB private DepositoRN depositoRN;
    
    public DepositoBean(){
        
    }
    
    @PostConstruct
    public void init(){
        cantidadRegistros = 0;
        txtBusqueda = "";
        mostrarDebaja = false;
        nuevo();
        buscar();
    }
    
    public void nuevo(){
        
        esNuevo = true;
        buscaMovimiento = false;
        deposito = new Deposito();
    }
    
    public void guardar(boolean nuevo){
        
        try {
            if (deposito != null) {
                
                depositoRN.guardar(deposito, esNuevo);
                esNuevo = false;                
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
                
                if (nuevo){
                    nuevo();
                }
            }else{
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(DepositoBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }
    
    public void habilitaDeshabilita(String habDes){
        
        try {
            deposito.getAuditoria().setDebaja(habDes);
            depositoRN.guardar(deposito, false);            
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");
            
        } catch (Exception ex) {
            Logger.getLogger(DepositoBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }
    
    public void eliminar(){
        try {
            depositoRN.eliminar(deposito);
            nuevo();            
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }        
    }
    
    public void buscar(){
        lista = depositoRN.getDepositoByBusqueda(txtBusqueda, mostrarDebaja,cantidadRegistros);
    }
    
    public List<Deposito> complete(String query) {
        try {
            lista = depositoRN.getDepositoByBusqueda(query, false, cantidadRegistros);
            return lista;
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Deposito>();
        }
    }
    
    public void onSelect(SelectEvent event) {
        deposito = (Deposito) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void seleccionar (Deposito d){
        
        deposito = d;
        esNuevo = false;
        buscaMovimiento = false;
    }
 
    
    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public List<Deposito> getLista() {
        return lista;
    }

    public void setLista(List<Deposito> lista) {
        this.lista = lista;
    }
    
}
