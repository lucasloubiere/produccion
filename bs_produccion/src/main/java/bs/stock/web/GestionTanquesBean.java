/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.web;

import bs.global.util.JsfUtil;
import bs.global.web.GenericBean;
import bs.stock.modelo.Deposito;
import bs.stock.modelo.GestionTanque;
import bs.stock.modelo.ItemGestionTanque;
import bs.stock.modelo.Producto;
import bs.stock.rn.DepositoRN;
import bs.stock.rn.MovimientoStockRN;
import bs.stock.rn.StockRN;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Claudio
 */
@ManagedBean
@ViewScoped
public class GestionTanquesBean extends GenericBean {

    @EJB
    private StockRN stockRN;
    @EJB
    private DepositoRN depositoRN;
    @EJB
    private MovimientoStockRN movimientoStockRN;

    private GestionTanque gestionTanque;
    private List<GestionTanque> lista;
    private List<Deposito> depositos;

    @PostConstruct
    private void init() {

        cantidadRegistros = 0;
        txtBusqueda = "";
        mostrarDebaja = false;
        nuevo();
        buscar();

    }

    public void nuevo() {

        esNuevo = true;
        buscaMovimiento = false;
        gestionTanque = new GestionTanque();
        gestionTanque.setFechaMovimiento(new Date());

        filtro = new HashMap<String, String>();
        filtro.put("calculaStock = ", "'S'");
        depositos = depositoRN.getDepositoByBusqueda(filtro, txtBusqueda, true, 0);
    }

    public void obtenerDatos() {

        /**
         * Obtenemos la última gestión guardada, anterior a la fecha de la
         * actual gestión.
         */
        GestionTanque gestionAnterior = new GestionTanque();
        gestionAnterior.setFechaMovimiento(new Date());

        gestionTanque.getItems().clear();

        for (Deposito deposito : depositos) {

            ItemGestionTanque item = new ItemGestionTanque();
            item.setDeposito(deposito);

            Producto producto = stockRN.getProductoByDepositoConStock(deposito);
            item.setProducto(producto);

            if (deposito != null && producto != null) {

                item.setStockInicial(movimientoStockRN.getStockAFecha(producto, deposito, gestionAnterior.getFechaMovimiento()));
                item.setIngresos(movimientoStockRN.getCantidadFromMovimiento("I", producto, deposito, gestionAnterior.getFechaMovimiento(), gestionTanque.getFechaMovimiento()));
                item.setEgresos(movimientoStockRN.getCantidadFromMovimiento("E", producto, deposito, gestionAnterior.getFechaMovimiento(), gestionTanque.getFechaMovimiento()));
                

            }

            gestionTanque.getItems().add(item);
        }
    }

    public void guardar(boolean nuevo) {

        try {
            if (gestionTanque != null) {

//                gestionTanqueRN.guardar(gestionTanque, esNuevo);
//                esNuevo = false;                
//                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
                if (nuevo) {
                    nuevo();
                }
            } else {
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(DepositoBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }

    public void habilitaDeshabilita(String habDes) {

        try {
            gestionTanque.getAuditoria().setDebaja(habDes);
//            gestionTanqueRN.guardar(gestionTanque, false);            
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");

        } catch (Exception ex) {
            Logger.getLogger(DepositoBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }

    public void eliminar() {
        try {
//            gestionTanqueRN.eliminar(gestionTanque);
            nuevo();
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }
    }

    public void buscar() {
//        lista = gestionTanqueRN.getListaByBusqueda(txtBusqueda, mostrarDebaja,cantidadRegistros);
    }

    public List<GestionTanque> complete(String query) {
        try {
//            lista = gestionTanqueRN.getListaByBusqueda(query, false, cantidadRegistros);
            return lista;

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<GestionTanque>();
        }
    }

    public void onSelect(SelectEvent event) {
        gestionTanque = (GestionTanque) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }

    public void seleccionar(GestionTanque d) {

        gestionTanque = d;
        esNuevo = false;
        buscaMovimiento = false;
    }

    //--------------------------------------------------------------------------
    public List<Deposito> getDepositos() {
        return depositos;
    }

    public void setDepositos(List<Deposito> depositos) {
        this.depositos = depositos;
    }

    public GestionTanque getGestionTanque() {
        return gestionTanque;
    }

    public void setGestionTanque(GestionTanque gestionTanque) {
        this.gestionTanque = gestionTanque;
    }

    public List<GestionTanque> getLista() {
        return lista;
    }

    public void setLista(List<GestionTanque> lista) {
        this.lista = lista;
    }

}
