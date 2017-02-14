/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.seguridad.web;

import com.seguridad.modelo.EstadoUsuario;
import com.seguridad.modelo.TipoUsuario;
import com.seguridad.rn.EstadoUsuarioRN;
import com.seguridad.rn.TipoUsuarioRN;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author ctrosch
 */
@ManagedBean (name="conversoresSeguridad")
@ViewScoped
public class ConversoresSeguridad implements Serializable{

    @EJB private EstadoUsuarioRN estadoRN;
    @EJB private TipoUsuarioRN tipoRN;
    
    /** Creates a new instance of ConversoresBean */
    public ConversoresSeguridad() {
        
    }

    public Converter getEstado() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }
                                
                EstadoUsuario e = estadoRN.getEstado(Integer.valueOf(value));
                return e;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((EstadoUsuario) value).getId() + "";
                }
            }
        };
    }

    public Converter getTipo() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }

                TipoUsuario t = tipoRN.getTipo(Integer.valueOf(value));
                return t;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((TipoUsuario) value).getId() + "";
                }
            }
        };
    }
   
}
