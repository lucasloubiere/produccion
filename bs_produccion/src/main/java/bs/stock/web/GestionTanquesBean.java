/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.web;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.util.JsfUtil;
import bs.global.web.GenericBean;
import bs.stock.modelo.Deposito;
import bs.stock.modelo.GestionTanque;
import bs.stock.modelo.ItemGestionTanque;
import bs.stock.modelo.Producto;
import bs.stock.rn.DepositoRN;
import bs.stock.rn.GestionTanqueRN;
import bs.stock.rn.MovimientoStockRN;
import bs.stock.rn.StockRN;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
    private StockRN stockRN;
    @EJB
    private DepositoRN depositoRN;
    @EJB
    private MovimientoStockRN movimientoStockRN;
    @EJB
    private GestionTanqueRN gestionTanqueRN;
    
    private GestionTanque gestionTanque;
    private ItemGestionTanque itemGestionTanque;
    private List<GestionTanque> lista;
    private List<Deposito> depositos;
    
    private List<ItemGestionTanque> resumen;
    
        
    @ManagedProperty(value = "#{productoBean}")
    protected ProductoBean productoBean;
    
    
    @PostConstruct
    private void init() {
        
        cantidadRegistros = 0;
        txtBusqueda = "";
        mostrarDebaja = false;
        resumen = new ArrayList<ItemGestionTanque>();
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
        
        filtro = new HashMap<String, String>();
        filtro.put("calculaStock = ", "'S'");
        depositos = depositoRN.getDepositoByBusqueda(filtro, txtBusqueda, true, 0);
        
    }
    
    public void onDateSelect(SelectEvent event) {
        
        obtenerDatos();        
        
    }
    
    public void obtenerDatos() {

        /**
         * Obtenemos la última gestión guardada, anterior a la fecha de la
         * actual gestión.
         */
        GestionTanque gestionAnterior = gestionTanqueRN.getUltimoRegistro();// 
        
        Calendar c = Calendar.getInstance();
        c.setTime(gestionAnterior.getFechaMovimiento());
        c.add(Calendar.MINUTE, 1);        
        gestionAnterior.setFechaMovimiento(c.getTime());
        
        gestionTanque.getItems().clear();
        
        for (Deposito deposito : depositos) {
            
            ItemGestionTanque item = new ItemGestionTanque();
            item.setDeposito(deposito);
            
            Producto producto = stockRN.getProductoByDepositoConStock(deposito);
            item.setProducto(producto);
            
            
            if (deposito != null && producto != null) {
                
                item.setDepositoConStock(true);
                
                item.setStockInicial(movimientoStockRN.getStockAFecha(producto, deposito, gestionAnterior.getFechaMovimiento()));
                
                BigDecimal transferencias = movimientoStockRN.getCantidadFromMovimiento("T", producto, deposito, gestionAnterior.getFechaMovimiento(), gestionTanque.getFechaMovimiento());
                BigDecimal ajustes = movimientoStockRN.getCantidadFromMovimiento("A", producto, deposito, gestionAnterior.getFechaMovimiento(), gestionTanque.getFechaMovimiento());
                BigDecimal ingresos = movimientoStockRN.getCantidadFromMovimiento("I", producto, deposito, gestionAnterior.getFechaMovimiento(), gestionTanque.getFechaMovimiento());
                BigDecimal egresos = movimientoStockRN.getCantidadFromMovimiento("E", producto, deposito, gestionAnterior.getFechaMovimiento(), gestionTanque.getFechaMovimiento());
                
                if (ingresos == null) {
                    ingresos = BigDecimal.ZERO;
                }
                if (egresos == null) {
                    egresos = BigDecimal.ZERO;
                }                
                if (transferencias == null) {
                    transferencias = BigDecimal.ZERO;
                }                
                if (ajustes == null) {
                    ajustes = BigDecimal.ZERO;
                }
                
                if (transferencias.compareTo(BigDecimal.ZERO) > 0) {                    
                    ingresos = ingresos.add(transferencias);                    
                }
                
                if (ajustes.compareTo(BigDecimal.ZERO) > 0) {                    
                    ingresos = ingresos.add(ajustes);                    
                }
                
                if (transferencias.compareTo(BigDecimal.ZERO) < 0) {                    
                    egresos = egresos.add(transferencias);                    
                }
                
                if (ajustes.compareTo(BigDecimal.ZERO) < 0) {                    
                    egresos = egresos.add(ajustes);                    
                }
                
                item.setIngresos(ingresos);
                item.setEgresos(egresos);                
                
                calcularStock(item);
            }
            
            item.setGestionTanque(gestionTanque);
            gestionTanque.getItems().add(item);
        }
        
        ordenarItems();
        
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
            
            filtro.put("fechaMovimiento >= ", JsfUtil.getFechaSQL(fechaMovimientoDesde));
        }
        
        if (fechaMovimientoHasta != null) {
            
            filtro.put("fechaMovimiento <= ", JsfUtil.getFechaSQL(fechaMovimientoHasta));
        }        
    }
    
    public void seleccionarMovimiento(GestionTanque mSel) {
        
        gestionTanque = mSel;        
        buscaMovimiento = false;        
        ordenarItems();
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
        
        if (i.getMedida() == null) {
            i.setMedida(BigDecimal.ZERO);
        }
        
        if (i.getMedida().compareTo(BigDecimal.ZERO) > 0){            
            i.setStockFinal((i.getMedida().multiply(i.getDeposito().getConstante()).add(i.getDeposito().getSumando())).divide(i.getDeposito().getDivisor(), 2, RoundingMode.HALF_UP));            
            i.setStockFinal(i.getStockFinal().multiply(new BigDecimal("1000")));
        } else {
            i.setStockFinal(BigDecimal.ZERO);
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
    
    public void ordenarItems() {
        
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
    
    public void procesarProducto(){
      
        if(gestionTanque!=null && productoBean.getProducto()!=null && itemGestionTanque !=null){
            
            try {
                gestionTanqueRN.asignarProducto(itemGestionTanque,productoBean.getProducto());
            } catch (ExcepcionGeneralSistema ex) {
                JsfUtil.addErrorMessage("No es posible asignar el producto al deposito " + ex);
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
