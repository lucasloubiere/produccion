/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.stock.web;


import bs.stock.modelo.Deposito;
import bs.stock.modelo.Formula;
import bs.stock.modelo.Producto;
import bs.stock.modelo.Rubro01;
import bs.stock.modelo.Rubro02;
import bs.stock.modelo.TipoProducto;
import bs.stock.modelo.UnidadMedida;
import bs.stock.rn.DepositoRN;
import bs.stock.rn.FormulaRN;
import bs.stock.rn.ProductoRN;
import bs.stock.rn.Rubro1RN;
import bs.stock.rn.Rubro2RN;
import bs.stock.rn.TipoProductoRN;
import bs.stock.rn.UnidadMedidaRN;
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
                
                Rubro01 t = rubro1RN.getRubro1(tipo[1], codigo[1]);
                
                System.err.println("t" + t);
                
                return t;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {

                    return ((Rubro01) value).toString()+ "";
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
                
                Rubro02 t = rubro2RN.getRubro2(tipo[1], codigo[1]);
                
                System.err.println("t" + t);
                return t;
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {

                    return ((Rubro02) value).toString()+ "";
                }
            }
        };
    }
   
}
