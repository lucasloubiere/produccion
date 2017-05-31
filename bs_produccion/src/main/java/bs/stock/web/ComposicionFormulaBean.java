/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.web;

import bs.global.web.GenericBean;
import bs.global.util.JsfUtil;
import bs.stock.modelo.ComposicionFormula;
import bs.stock.modelo.ComposicionFormulaItem;
import bs.stock.rn.ComposicionFormulaRN;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Agustin
 */
@ManagedBean
@ViewScoped
public class ComposicionFormulaBean extends GenericBean implements Serializable {

    private ComposicionFormula composicionFormula;
    private ComposicionFormulaItem composicionFormulaItem;
    private List<ComposicionFormula> lista;

    @ManagedProperty(value = "#{productoBean}")
    protected ProductoBean productoBean;

    @EJB
    private ComposicionFormulaRN composicionFormulaRN;

    public ComposicionFormulaBean() {

    }

    @PostConstruct
    public void init() {

        txtBusqueda = "";
        mostrarDebaja = false;
        nuevo();
        buscar();
    }

    public void nuevo() {

        esNuevo = true;
        buscaMovimiento = false;
        composicionFormula = new ComposicionFormula();

    }

    public void guardar(boolean nuevo) {

        try {
            if (composicionFormula != null) {

                composicionFormulaRN.guardar(composicionFormula, esNuevo);
                esNuevo = false;
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");

                if (nuevo) {
                    nuevo();
                }
            } else {
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(ComposicionFormulaBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }

    public void habilitaDeshabilita(String habDes) {

        try {
            composicionFormula.getAuditoria().setDebaja(habDes);
            composicionFormulaRN.guardar(composicionFormula, false);
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");

        } catch (Exception ex) {
            Logger.getLogger(ComposicionFormulaBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }

    public void eliminar() {
        try {
            composicionFormulaRN.eliminar(composicionFormula);
            nuevo();
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }
    }

    public void buscar() {

        String artcod = "";
        String codfor = "";

//        if (composicionFormula.getProducto() != null) {
//            artcod = composicionFormula.getProducto().getCodigo();
//        }
//        if (composicionFormula.getFormula() != null) {
//            codfor = composicionFormula.getFormula().getCodigo();
//        }
        lista = composicionFormulaRN.getListaByBusqueda(artcod, codfor, txtBusqueda, mostrarDebaja, cantidadRegistros);
    }

    public List<ComposicionFormula> complete(String query) {
        try {

            String artcod = "";
            String codfor = "";

//            if (composicionFormula.getProducto() != null) {
//                artcod = composicionFormula.getProducto().getCodigo();
//            }
//            if (composicionFormula.getFormula() != null) {
//                codfor = composicionFormula.getFormula().getCodigo();
//            }
            lista = composicionFormulaRN.getListaByBusqueda(artcod, codfor, query, false, cantidadRegistros);
            return lista;

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<ComposicionFormula>();
        }
    }

    public void onSelect(SelectEvent event) {
        composicionFormula = (ComposicionFormula) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }

    public void seleccionar(ComposicionFormula d) {

        composicionFormula = d;
        esNuevo = false;
        buscaMovimiento = false;
    }

    public void imprimir() {

        if (composicionFormula == null) {
            JsfUtil.addErrorMessage("No hay entidad seleccionada, nada para imprimir");
        }
    }

    public void procesarProducto() {

        if (productoBean.getProducto() != null) {
            composicionFormula.setArtcod(productoBean.getProducto().getCodigo());
            composicionFormula.setProducto(productoBean.getProducto());
        }
    }

    public void procesarFormula() {

        if (composicionFormula.getFormula() != null) {
            composicionFormula.setCodfor(composicionFormula.getFormula().getCodigo());
        }
    }

    public void agregarItem() {

        if (composicionFormula.getFormula() == null) {
            JsfUtil.addErrorMessage("Seleccione una formula antes de agregar un item");
            return;
        }

        if (composicionFormula.getProducto() == null) {
            JsfUtil.addErrorMessage("Seleccione el producto principal antes de agregar un item");
            return;
        }

        composicionFormulaItem = new ComposicionFormulaItem();
        composicionFormulaItem.setNroitem(composicionFormula.getItemsComposicion().size() + 1);
        composicionFormulaItem.setArtcod(composicionFormula.getArtcod());
        composicionFormulaItem.setCodfor(composicionFormula.getCodfor());
        composicionFormulaItem.setComposicionFormula(composicionFormula);
        composicionFormula.getItemsComposicion().add(composicionFormulaItem);

    }

    public void eliminarItem(ComposicionFormulaItem ci) {

        try {
            if (composicionFormula.getItemsComposicion() == null) {
                return;
            }

            composicionFormula.getItemsComposicion().remove(ci);

            if (!esNuevo) {
                composicionFormulaRN.eliminarItem(ci);
            }   
            
            buscar();            
            //composicionFormula = composicionFormulaRN.getComprosicionFormula(composicionFormula.getArtcod(), composicionFormula.getCodfor());

            if (ci.getProducto() == null) {
                JsfUtil.addWarningMessage("Se ha elininado el item");
            } else {
                JsfUtil.addWarningMessage("Se ha elininado el producto " + ci.getProducto().getDescripcion());
            }

        } catch (Exception exception) {

            JsfUtil.addErrorMessage("No es posible eliminar el item");
            composicionFormula.getItemsComposicion().add(ci);
        }

    }

    public void seleccionarItem() {

    }

    //-----------------------------------------------------------------------------------
    public ComposicionFormula getComposicionFormula() {
        return composicionFormula;
    }

    public void setComposicionFormula(ComposicionFormula composicionFormula) {
        this.composicionFormula = composicionFormula;
    }

    public List<ComposicionFormula> getLista() {
        return lista;
    }

    public void setLista(List<ComposicionFormula> lista) {
        this.lista = lista;
    }

    public ProductoBean getProductoBean() {
        return productoBean;
    }

    public void setProductoBean(ProductoBean productoBean) {
        this.productoBean = productoBean;
    }

    public ComposicionFormulaItem getComposicionFormulaItem() {
        return composicionFormulaItem;
    }

    public void setComposicionFormulaItem(ComposicionFormulaItem composicionFormulaItem) {
        this.composicionFormulaItem = composicionFormulaItem;
    }
}
