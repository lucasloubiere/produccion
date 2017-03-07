/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stock.web;

import com.stock.modelo.TipoProducto;
import com.stock.rn.TipoProductoRN;
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
@ManagedBean (name="conversoresStock")
@ViewScoped
public class ConversoresStock implements Serializable{
    
    @EJB private TipoProductoRN tipoProductoRN;

    
    public Converter getTipoProducto() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }

                TipoProducto t = tipoProductoRN.getTipoProducto(value);
                return t;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((TipoProducto) value).getCodigo()+ "";
                }
            }
        };
    }
   
}
