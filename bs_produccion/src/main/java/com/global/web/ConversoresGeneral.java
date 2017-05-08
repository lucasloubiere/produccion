/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.global.web;

import com.global.modelo.Comprobante;
import com.global.modelo.CondicionDeIva;
import com.global.modelo.Formulario;
import com.global.modelo.Modulo;
import com.global.modelo.Sucursal;
import com.global.rn.ComprobanteRN;
import com.global.rn.CondicionDeIvaRN;
import com.global.rn.FormularioRN;
import com.global.rn.ModuloRN;
import com.global.rn.SucursalRN;
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
@ManagedBean (name="conversoresGeneral")
@ViewScoped
public class ConversoresGeneral implements Serializable{
  
    
    @EJB private ModuloRN moduloRN;
    @EJB private SucursalRN sucursalRN;
    @EJB private CondicionDeIvaRN condicionDeIvaRN;
    @EJB private ComprobanteRN comprobanteRN;
    @EJB private FormularioRN formularioRN;
    
    
    
     public Converter getModulo() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }

                Modulo m = moduloRN.getModulo(value);
                return m;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Modulo) value).getCodigo()+ "";
                }
            }
        };
    }
     
     public Converter getFormulario() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }

                String a[] = value.split(",");
                String modulo[] = a[0].split("=");
                String codigo[] = a[1].split("=");


                Formulario f = formularioRN.getFormulario(modulo[1], codigo[1]);
                return f;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    
                    return "modulo=" + ((Formulario) value).getModfor()+ ", codigo=" + ((Formulario) value).getCodigo()+"";

                }
            }
        };
    }
     
     public Converter getSucursal() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }

                Sucursal m = sucursalRN.getSucursal(value);
                return m;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Sucursal) value).getCodigo()+ "";
                }
            }
        };
    }
     
     public Converter getCondicionDeIva() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }
                
                return condicionDeIvaRN.getCondicionDeIva(value);
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {

                    return ((CondicionDeIva) value).getCodigo() + "";
                }
            }
        };
    }
     
     public Converter getComprobante() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }

                String a[] = value.split(",");
                String modcom[] = a[0].split("=");
                String codcom[] = a[1].split("=");


                Comprobante c = comprobanteRN.getComprobante(modcom[1], codcom[1]);
                return c;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    
                    return "modulo=" + ((Comprobante) value).getModulo() + ", codigo=" + ((Comprobante) value).getCodigo()+"";

                }
            }
        };
    }
    
}
