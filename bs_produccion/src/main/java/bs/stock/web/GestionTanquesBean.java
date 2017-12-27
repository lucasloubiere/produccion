/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.web;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.util.JeeUtil;
import bs.global.util.JsfUtil;
import bs.global.web.GenericBean;
import bs.stock.modelo.GestionTanque;
import bs.stock.modelo.ItemGestionTanque;
import bs.stock.rn.GestionTanqueRN;
import bs.stock.rn.MovimientoStockRN;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Claudio
 */
@ManagedBean
@ViewScoped
public class GestionTanquesBean extends GenericBean {

    @EJB
    private MovimientoStockRN movimientoStockRN;
    @EJB
    private GestionTanqueRN gestionTanqueRN;

    private GestionTanque gestionTanque;
    private ItemGestionTanque itemGestionTanque;
    private List<GestionTanque> lista;
    
    private List<ItemGestionTanque> resumen;

    @ManagedProperty(value = "#{productoBean}")
    protected ProductoBean productoBean;

    @PostConstruct
    private void init() {

        cantidadRegistros = 0;
        txtBusqueda = "";
        mostrarDebaja = false;
        resumen = new ArrayList<ItemGestionTanque>();
        filtro = new HashMap<String, String>();
        nuevo();
    }

    public void nuevo() {

        esNuevo = true;
        buscaMovimiento = false;
        try {
            gestionTanque = gestionTanqueRN.nuevoMovimiento("ST", "GT", "0001");
        } catch (ExcepcionGeneralSistema ex) {
            Logger.getLogger(GestionTanquesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onDateSelect(SelectEvent event) {

        obtenerDatos();

    }

    public void obtenerDatos() {

        try {
            gestionTanqueRN.obtenerDatos(gestionTanque);
        } catch (ExcepcionGeneralSistema ex) {
            Logger.getLogger(GestionTanquesBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible obtener datos " + ex);
        }

    }

    public void guardar(boolean nuevo) {

        try {
            if (gestionTanque != null) {

                gestionTanqueRN.guardar(gestionTanque);
                esNuevo = false;
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
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

    public String onFlowProcess(FlowEvent event) {

        if (event.getNewStep().equals("resumen")) {
            generarResumen();
        }

        if (event.getNewStep().equals("finalizar")) {

        }

        return event.getNewStep();
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

    public void nuevaBusqueda() {

        if (gestionTanque != null && gestionTanque.getFormulario() != null) {
            formulario = gestionTanque.getFormulario();
        }
        buscaMovimiento = true;
    }

    public void buscarMovimiento() {

        if (!validarParametros()) {
            return;
        }
        cargarFiltroBusqueda();

        lista = gestionTanqueRN.getListaByBusqueda(filtro, 0);

        if (lista == null || lista.isEmpty()) {
            JsfUtil.addWarningMessage("No se han encontrado movimientos");
        }
    }

    public boolean validarParametros() {

        if (formulario == null) {
            JsfUtil.addWarningMessage("Seleccione un formulario");
            return false;
        }

        if (numeroFormularioDesde != null && numeroFormularioHasta != null) {
            if (numeroFormularioDesde > numeroFormularioHasta) {
                JsfUtil.addWarningMessage("Número de formulario desde es mayor al número de formulario hasta");
                return false;
            }
        }
        return true;
    }

    public void cargarFiltroBusqueda() {

        filtro.clear();

        if (formulario != null) {
            filtro.put("formulario.codigo = ", "'" + formulario.getCodigo() + "'");
        }

        if (numeroFormularioDesde != null) {

            filtro.put("numeroFormulario >=", String.valueOf(numeroFormularioDesde));
        }

        if (numeroFormularioHasta != null) {

            filtro.put("numeroFormulario <=", String.valueOf(numeroFormularioHasta));
        }

        if (fechaMovimientoDesde != null) {

            filtro.put("fechaMovimiento >= ", JeeUtil.getFechaSQL(fechaMovimientoDesde));
        }

        if (fechaMovimientoHasta != null) {

            filtro.put("fechaMovimiento <= ", JeeUtil.getFechaSQL(fechaMovimientoHasta));
        }
    }

    public void seleccionarMovimiento(GestionTanque mSel) {

        gestionTanque = mSel;
        buscaMovimiento = false;
        gestionTanqueRN.ordenarItems(gestionTanque);
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

        gestionTanqueRN.calcularStock(i);
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

            if (item.getProducto() == null) {
                continue;
            }

            for (ItemGestionTanque itemResumen : resumen) {

                if (item.getProducto().equals(itemResumen.getProducto())) {
                    existe = true;
                    break;
                }
                posicion++;
            }

            if (existe) {
                ItemGestionTanque itemExistente = resumen.get(posicion);
                itemExistente.setStockFinal(itemExistente.getStockFinal().add(item.getStockFinal()));
            } else {

                ItemGestionTanque itemAgregar = new ItemGestionTanque();
                itemAgregar.setProducto(item.getProducto());
                itemAgregar.setStockFinal(item.getStockFinal());
                resumen.add(itemAgregar);
            }
        }
    }

    public void resetParametros() {

//        formulario = null;
        numeroFormularioDesde = null;
        numeroFormularioHasta = null;
        fechaMovimientoDesde = null;
        fechaMovimientoHasta = null;
        muestraReporte = false;
        solicitaEmail = false;
        gestionTanque = null;

    }

    public void procesarProducto() {

        if (gestionTanque != null && productoBean.getProducto() != null && itemGestionTanque != null) {

            try {
                gestionTanqueRN.asignarProducto(itemGestionTanque, productoBean.getProducto());
            } catch (ExcepcionGeneralSistema ex) {
                JsfUtil.addErrorMessage("No es posible asignar el producto al deposito " + ex);
            }
        }
    }

//--------------------------------------------------------------------------

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

    public ProductoBean getProductoBean() {
        return productoBean;
    }

    public void setProductoBean(ProductoBean productoBean) {
        this.productoBean = productoBean;
    }

    public ItemGestionTanque getItemGestionTanque() {
        return itemGestionTanque;
    }

    public void setItemGestionTanque(ItemGestionTanque itemGestionTanque) {
        this.itemGestionTanque = itemGestionTanque;
    }

    

}
