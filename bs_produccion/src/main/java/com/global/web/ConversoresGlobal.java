/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.global.web;

import com.global.modelo.Modulo;
import com.global.modelo.Sucursal;
import com.global.rn.ModuloRN;
import com.global.rn.SucursalRN;
import com.stock.rn.FormulaRN;
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
@ManagedBean (name="conversoresGlobal")
@ViewScoped
public class ConversoresGlobal implements Serializable{
   

    @EJB private FormulaRN formulaRN;
    @EJB private ModuloRN moduloRN;
    @EJB private SucursalRN sucursalRN;
    
    
    
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
     
     public Converter getSucursal() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }

                Sucursal m = moduloRN.getSucursal(value);
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
    
}
