/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.global.web;

import bs.administracion.modelo.Modulo;
import bs.administracion.rn.ModuloRN;
import bs.global.modelo.Comprobante;
import bs.global.modelo.CondicionDeIva;
import bs.global.modelo.Formulario;
import bs.global.modelo.Sucursal;
import bs.global.rn.ComprobanteRN;
import bs.global.rn.CondicionDeIvaRN;
import bs.global.rn.FormularioRN;
import bs.global.rn.SucursalRN;
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
