/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguridad.web;


import com.seguridad.modelo.Usuario;
import com.seguridad.rn.UsuarioRN;
import java.io.IOException;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author ide
 */

@ManagedBean
@SessionScoped
public class UsuarioSessionBean implements Serializable {

    @EJB private UsuarioRN usuarioRN;
    
    private String nUsuario; 
    private String sPassword;
    private Usuario usuario;
    private boolean estaRegistrado;

    /**
     * Creates a new instance of UsuarioSesionBean
     */
    public UsuarioSessionBean() {
        
    }
    
    public String login(){
        
        usuario = null;
        estaRegistrado = false;      
        Usuario usuAux = usuarioRN.getUsuarioByNombre(nUsuario);

        if(usuAux==null){
            
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontró usuario");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);           
           return "";
        }

        if(!usuAux.getPassword().equals(sPassword)){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password incorrecto");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);           
            return "";
        }

        usuario = usuAux;
        estaRegistrado = true;      
        
        
        return "home";
    } 
    
    public void checkLogin() throws IOException {
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        
        System.err.println(usuario);
        
        if (usuario==null) {
            
//            context.redirect(context.getRequestContextPath()+"/seguridad/login.jsf");
        }
    }

    public String getnUsuario() {
        return nUsuario;
    }

    public void setnUsuario(String nUsuario) {
        this.nUsuario = nUsuario;
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }
     
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isEstaRegistrado() {
        return estaRegistrado;
    }

    public void setEstaRegistrado(boolean estaRegistrado) {
        this.estaRegistrado = estaRegistrado;
    }
    
    
}
