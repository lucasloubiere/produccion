/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.web;

import bs.global.util.JsfUtil;
import bs.global.web.GenericBean;
import bs.stock.modelo.Sector;
import bs.stock.rn.SectorRN;
import java.util.ArrayList;
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
 * @author lloubiere
 */
@ManagedBean
@ViewScoped
public class SectorBean extends GenericBean {

    private Sector sector;
    private List<Sector> lista;
    @EJB
    private SectorRN sectorRN;

    /**
     * Creates a new instance of UnidadDeMedidaBean
     */
    public SectorBean() {

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
        sector = new Sector();
    }

    public void guardar(boolean nuevo) {

        try {
            if (sector != null) {
                sectorRN.guardar(sector, esNuevo);
                esNuevo = false;
                buscar(); 
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
                if (nuevo){
                    nuevo();
                }
            } else {
                JsfUtil.addErrorMessage("No hay datos para guardar");
            }
        } catch (Exception exception) {
        }
    }

    public void habilitaDeshabilita(String habDes) {
        try {
            sector.getAuditoria().setDebaja(habDes);
            sectorRN.guardar(sector, false);
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");

        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }
    }

    public void eliminar() {

        try {
            sectorRN.eliminar(sector);
            nuevo();
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);

        }
    }

    public void buscar() {
        
         lista = sectorRN.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
    
    }

    public List<Sector> complete(String query) {
        try {
            lista = sectorRN.getListaByBusqueda(query, false, cantidadRegistros);    
            return lista;
            
        } catch (Exception ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Sector>();
        }
    }

    
    public void onSelect(SelectEvent event) {
        sector = (Sector) event.getObject(); 
        esNuevo = false;
        buscaMovimiento = false;
        
    }

    public void seleccionar(Sector u) {
        sector = u;
        esNuevo = false;
        buscaMovimiento = false;

    }

    public Sector getUnidadDeMedida() {
        return sector;
    }

    public void setUnidadDeMedida(Sector sector) {
        this.sector = sector;
    }

    public List<Sector> getLista() {
        return lista;
    }

    public void setLista(List<Sector> lista) {
        this.lista = lista;
    }

}
