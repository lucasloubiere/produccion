/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.administracion.web;


import com.administracion.modelo.Empresa;
import com.administracion.rn.EmpresaRN;
import com.global.util.GenericBean;
import com.global.util.JsfUtil;
import com.global.web.AplicacionBean;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;


/** 
 * @author Claudio
 */

@ManagedBean
@ViewScoped
public class EmpresaBean extends GenericBean implements Serializable {
        
    @EJB private EmpresaRN empresaRN;

    private Empresa empresa;

    @ManagedProperty(value = "#{aplicacionBean}")
    private AplicacionBean aplicacionBean;
    
    /** Creates a new instance of ParametrosBean */
    public EmpresaBean() {

    }
   
    @PostConstruct
    private void init(){
        try {
            empresa = empresaRN.getEmpresa("01");
        } catch (Exception ex) {
            Logger.getLogger(ParametroBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String verEmpresa(){
        return "../administracion/tablas/empresa.xhtml";
    }

    public void guardar(){
        try {
            
            empresaRN.guardar(empresa);            
            aplicacionBean.actualizarDatos();
            JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
        } catch (Exception ex) {

            Logger.getLogger(ParametroBean.class.getName()).log(Level.SEVERE, "Error al guardar datos", ex);
        }
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public AplicacionBean getAplicacionBean() {
        return aplicacionBean;
    }

    public void setAplicacionBean(AplicacionBean aplicacionBean) {
        this.aplicacionBean = aplicacionBean;
    }
    
}
