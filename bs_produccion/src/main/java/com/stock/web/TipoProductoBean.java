/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.web;

import com.global.util.GenericBean;
import com.global.util.JsfUtil;
import com.stock.modelo.TipoProducto;
import com.stock.rn.Rubro1RN;
import com.stock.rn.Rubro2RN;
import com.stock.rn.TipoProductoRN;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author lloubiere
 */
@ManagedBean
@Stateless
public class TipoProductoBean extends GenericBean implements Serializable {

    @EJB
    private TipoProductoRN tipoProductoRN;
    @EJB
    private Rubro1RN rubro1RN;
    @EJB
    private Rubro2RN rubro2RN;

    private List<TipoProducto> lista;
    private TipoProducto tipoProducto;

    private boolean mostrarRubro1Baja;
    private boolean mostrarRubro2Baja;

    /**
     * Creates a new instance of TipoProductoBean
     */
    public TipoProductoBean() {
    }

    @PostConstruct
    public void init() {

        mostrarDebaja = false;
        txtBusqueda = "";
        nuevo();
        buscar();
    }

    public void nuevo() {

        esNuevo = true;
        buscaMovimiento = false;
        tipoProducto = new TipoProducto();
    }

    public void guardar(boolean nuevo) {

        try {
            if (tipoProducto != null) {

                tipoProductoRN.guardar(tipoProducto, esNuevo);
                esNuevo = false;
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");

                if (nuevo) {
                    nuevo();
                }
            } else {
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }

    public void habilitaDeshabilita(String habDes) {

        try {
            tipoProducto.getAuditoria().setDebaja(habDes);
            tipoProductoRN.guardar(tipoProducto, false);
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");

        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }
    }

    public void eliminar() {
        try {
            tipoProductoRN.eliminar(tipoProducto);
            nuevo();
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }
    }

    public void buscar() {
        lista = tipoProductoRN.getListaByBusqueda(txtBusqueda, mostrarDebaja);
    }

    public List<TipoProducto> complete(String query) {
        try {
            lista = tipoProductoRN.getListaByBusqueda(query, false);
            return lista;

        } catch (Exception ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<TipoProducto>();
        }
    }

    public void onSelect(SelectEvent event) {

        tipoProducto = (TipoProducto) event.getObject();
        seleccionar(tipoProducto);
    }

    public void seleccionar(TipoProducto u) {

        tipoProducto = u;
//        sincronizarRubro1();
//        sincronizarRubro2();
        esNuevo = false;
        buscaMovimiento = false;
    }

    public void imprimir() {

        if (tipoProducto == null) {
            JsfUtil.addErrorMessage("No hay entidad seleccionada, nada para imprimir");
        }
    }

//    public void nuevoRubro1() {
//
//        if (tipoProducto != null) {
//
//            if (tipoProducto.getRubro1() == null) {
//                tipoProducto.setRubro1(new ArrayList<Rubro1>());
//            }
//            rubro1Bean.setTipoProducto(tipoProducto);
//            rubro1Bean.nuevo();
//
//        } else {
//            JsfUtil.addErrorMessage("No existe un tipo de producto para asociarle rubros");
//        }
//    }

//    public void seleccionarRubro1(Rubro1 r1) {
//        if (tipoProducto != null) {
//            rubro1Bean.setTipoProducto(tipoProducto);
//            rubro1Bean.seleccionar(r1);
//        } else {
//            JsfUtil.addErrorMessage("No existe un tipo de producto para asociarle rubros");
//        }
//    }

//    public void sincronizarRubro1() {
//
//        if (tipoProducto != null) {
//            tipoProducto.setRubro1(rubro1RN.getListaByBusqueda(tipoProducto, "", mostrarRubro1Baja));
//        }
//    }
//
//    public void nuevoRubro2() {
//
//        if (tipoProducto != null) {
//
//            if (tipoProducto.getRubro2() == null) {
//                tipoProducto.setRubro2(new ArrayList<Rubro2>());
//            }
//            rubro2Bean.setTipoProducto(tipoProducto);
//            rubro2Bean.nuevo();
//        } else {
//            JsfUtil.addErrorMessage("No existe un tipo de producto para asociarle sub rubros");
//        }
//    }
//
//    public void seleccionarRubro2(Rubro2 r2) {
//        if (tipoProducto != null) {
//            rubro2Bean.setTipoProducto(tipoProducto);
//            rubro2Bean.seleccionar(r2);
//        } else {
//            JsfUtil.addErrorMessage("No existe un tipo de producto para asociarle sub rubros");
//        }
//    }

//    public void sincronizarRubro2() {
//
//        if (tipoProducto != null) {
//            tipoProducto.setRubro2(rubro2RN.getListaByBusqueda(tipoProducto, "", mostrarRubro2Baja));
//        }
//    }

    //----------------------------------------------------------------------------------- 
    public List<TipoProducto> getLista() {
        return lista;
    }

    public void setLista(List<TipoProducto> lista) {
        this.lista = lista;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

//    public Rubro1Bean getRubro1Bean() {
//        return rubro1Bean;
//    }
//
//    public void setRubro1Bean(Rubro1Bean rubro1Bean) {
//        this.rubro1Bean = rubro1Bean;
//    }
//
//    public Rubro2Bean getRubro2Bean() {
//        return rubro2Bean;
//    }
//
//    public void setRubro2Bean(Rubro2Bean rubro2Bean) {
//        this.rubro2Bean = rubro2Bean;
//    }

    public boolean isMostrarRubro1Baja() {
        return mostrarRubro1Baja;
    }

    public void setMostrarRubro1Baja(boolean mostrarRubro1Baja) {
        this.mostrarRubro1Baja = mostrarRubro1Baja;
    }

    public boolean isMostrarRubro2Baja() {
        return mostrarRubro2Baja;
    }

    public void setMostrarRubro2Baja(boolean mostrarRubro2Baja) {
        this.mostrarRubro2Baja = mostrarRubro2Baja;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
