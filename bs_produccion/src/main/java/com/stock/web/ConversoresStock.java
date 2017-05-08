/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stock.web;

import com.global.modelo.Modulo;
import com.global.rn.ModuloRN;
import com.global.rn.SucursalRN;
import com.stock.modelo.Deposito;
import com.stock.modelo.Formula;
import com.stock.modelo.Producto;
import com.stock.modelo.Rubro1;
import com.stock.modelo.Rubro2;
import com.stock.modelo.TipoProducto;
import com.stock.modelo.UnidadMedida;
import com.stock.rn.DepositoRN;
import com.stock.rn.FormulaRN;
import com.stock.rn.ProductoRN;
import com.stock.rn.Rubro1RN;
import com.stock.rn.Rubro2RN;
import com.stock.rn.TipoProductoRN;
import com.stock.rn.UnidadMedidaRN;
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
    
    @EJB private ProductoRN productoRN;
    @EJB private TipoProductoRN tipoProductoRN;
    @EJB private UnidadMedidaRN unidadDeMedidaRN;
    @EJB private Rubro1RN rubro1RN;
    @EJB private Rubro2RN rubro2RN;
    @EJB private FormulaRN formulaRN;
    @EJB private ModuloRN moduloRN;
    @EJB private SucursalRN sucursalRN;
    @EJB private DepositoRN depositoRN;
    
    public Converter getProducto() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }

                Producto t = productoRN.getProducto(value);
                return t;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Producto) value).getCodigo()+ "";
                }
            }
        };
    }
             
    
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
    
    public Converter getFormula() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }

                Formula f = formulaRN.getFormula(value);
                return f;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Formula) value).getCodigo()+ "";
                }
            }
        };
    }
    
    
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
   
    
    public Converter getUnidadMedida() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }

                UnidadMedida d = unidadDeMedidaRN.getUnidadMedida(value);
                return d;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {

                    return ((UnidadMedida) value).getCodigo()+ "";
                }
            }
        };
    }
    
    public Converter getDeposito() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }

                Deposito d = depositoRN.getDeposito(value);
                return d;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {

                    return ((Deposito) value).getCodigo()+ "";
                }
            }
        };
    }
    
    public Converter getRubro1() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }
                
                String a[] = value.split(",");
                String tipo[] = a[0].split("=");
                String codigo[] = a[1].split("=");
                
                Rubro1 t = rubro1RN.getRubro1(tipo[1], codigo[1]);
                
                System.err.println("t" + t);
                
                return t;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {

                    return ((Rubro1) value).toString()+ "";
                }
            }
        };
    }
    
    public Converter getRubro2() {
        return new Converter() {

            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null) {
                    return null;
                }
                               
                String a[] = value.split(",");
                String tipo[] = a[0].split("=");
                String codigo[] = a[1].split("=");
                
                Rubro2 t = rubro2RN.getRubro2(tipo[1], codigo[1]);
                
                System.err.println("t" + t);
                return t;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {

                    return ((Rubro2) value).toString()+ "";
                }
            }
        };
    }
   
}
