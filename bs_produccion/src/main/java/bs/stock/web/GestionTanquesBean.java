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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

    private List<ItemGestionTanque> resumen;

    @PostConstruct
    private void init() {

        cantidadRegistros = 0;
        txtBusqueda = "";
        mostrarDebaja = false;
        resumen = new ArrayList<ItemGestionTanque>();
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

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -20);

        gestionAnterior.setFechaMovimiento(c.getTime());

        System.err.println("gestionAnterior.fecha " + gestionAnterior.getFechaMovimiento());

        gestionTanque.getItems().clear();

        for (Deposito deposito : depositos) {

            ItemGestionTanque item = new ItemGestionTanque() {
            };
            item.setDeposito(deposito);

            Producto producto = stockRN.getProductoByDepositoConStock(deposito);
            item.setProducto(producto);

            if (deposito != null && producto != null) {

                item.setStockInicial(movimientoStockRN.getStockAFecha(producto, deposito, gestionAnterior.getFechaMovimiento()));
                item.setIngresos(movimientoStockRN.getCantidadFromMovimiento("I", producto, deposito, gestionAnterior.getFechaMovimiento(), gestionTanque.getFechaMovimiento()));
                item.setEgresos(movimientoStockRN.getCantidadFromMovimiento("E", producto, deposito, gestionAnterior.getFechaMovimiento(), gestionTanque.getFechaMovimiento()));

                calcularStock(item);

            }
            gestionTanque.getItems().add(item);
        }

        Collections.sort(gestionTanque.getItems(), new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                //return new Integer(p1.getEdad()).compareTo(new Integer(p2.getEdad()));
                ItemGestionTanque item1 = (ItemGestionTanque) o1;
                ItemGestionTanque item2 = (ItemGestionTanque) o2;

                String cod1 = (item1.getProducto() == null ? "99999" : item1.getProducto().getCodigo());
                String cod2 = (item2.getProducto() == null ? "99999" : item2.getProducto().getCodigo());

                return (new Integer(cod1)).compareTo(new Integer(cod2));

            }
        });

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

    public void calcularStock(ItemGestionTanque i) {

        if (i.getStockInicial() == null) {
            i.setStockInicial(BigDecimal.ZERO);
        }
        if (i.getIngresos() == null) {
            i.setIngresos(BigDecimal.ZERO);
        }
        if (i.getEgresos() == null) {
            i.setEgresos(BigDecimal.ZERO);
        }

        i.setStockCalculado(i.getStockInicial().negate().add(i.getIngresos().negate()).add(i.getEgresos().negate()).add(i.getStockFinal()));
    }

    public void marcarDepositoSinMovimiento(ItemGestionTanque i) {

        if (i.getStockInicial() == null) {
            i.setStockInicial(BigDecimal.ZERO);
        }
        if (i.getIngresos() == null) {
            i.setIngresos(BigDecimal.ZERO);
        }
        if (i.getEgresos() == null) {
            i.setEgresos(BigDecimal.ZERO);
        }

        i.setStockFinal(i.getStockInicial().add(i.getIngresos()).add(i.getEgresos()));
        i.setStockCalculado(i.getStockInicial().negate().add(i.getIngresos().negate()).add(i.getEgresos().negate()).add(i.getStockFinal()));
    }

    public void generarResumen() {

        resumen.clear();
        
        for (ItemGestionTanque item : gestionTanque.getItems()) {
            
            boolean existe = false;
            int posicion = 0;

            for(ItemGestionTanque itemResumen: resumen){

                if(item.getProducto().equals(itemResumen.getProducto())){                    
                    existe=true;
                    break;                                     
                }
                posicion++;
            }            
            
            if(existe){                
                ItemGestionTanque itemExistente = resumen.get(posicion);                
                itemExistente.setStockFinal(itemExistente.getStockFinal().add(item.getStockFinal()));                
            }else{
                
                ItemGestionTanque itemAgregar = new ItemGestionTanque();
                itemAgregar.setProducto(item.getProducto());
                itemAgregar.setStockFinal(item.getStockFinal());                                       
                resumen.add(itemAgregar);
            }
        }
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

    public List<ItemGestionTanque> getResumen() {
        return resumen;
    }

    public void setResumen(List<ItemGestionTanque> resumen) {
        this.resumen = resumen;
    }
    
}
