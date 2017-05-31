/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.seguridad.web;

import bs.global.web.GenericBean;
import bs.global.util.JsfUtil;
import bs.seguridad.modelo.Usuario;
import bs.seguridad.rn.UsuarioRN;
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
public class UsuarioBean extends GenericBean implements Serializable {

  private Usuario usuario;
  private List<Usuario> lista;
  
  @EJB private UsuarioRN usuarioRN;
    
    public UsuarioBean(){
        
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
        usuario = new Usuario();
    }
    
    public void guardar(boolean nuevo){
        
        try {
            if (usuario != null) {
                
                usuarioRN.guardar(usuario, esNuevo);
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
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }
    
    public void habilitaDeshabilita(String habDes){
        
        try {
            usuario.getAuditoria().setDebaja(habDes);
            usuarioRN.guardar(usuario, false);
            buscar();            
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");
            
        } catch (Exception ex) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }
    
    public void eliminar(){
        try {
            usuarioRN.eliminar(usuario);
            nuevo();
            buscar();            
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }        
    }
    
    public void buscar(){
        lista = usuarioRN.getLista(txtBusqueda, mostrarDebaja, cantidadRegistros);
    }
    
    public List<Usuario> complete(String query) {
        try {
            lista = usuarioRN.getLista(txtBusqueda, mostrarDebaja, cantidadRegistros);
            return lista;
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Usuario>();
        }
    }
    
    public void onSelect(SelectEvent event) {
        usuario = (Usuario) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void seleccionar (Usuario d){
        
        usuario = d;
        esNuevo = false;
        buscaMovimiento = false;
    }
    
    public void imprimir(){
        
        if(usuario==null){
            JsfUtil.addErrorMessage("Usuario no seleccionado, nada para imprimir");
        }
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario deposito) {
        this.usuario = deposito;
    }

    public List<Usuario> getLista() {
        return lista;
    }

    public void setLista(List<Usuario> lista) {
        this.lista = lista;
    }
}