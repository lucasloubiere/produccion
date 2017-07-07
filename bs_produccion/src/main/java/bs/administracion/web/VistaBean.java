/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.administracion.web;

import bs.administracion.modelo.Parametro;
import bs.administracion.modelo.Vista;
import bs.administracion.modelo.VistaDetalle;
import bs.administracion.rn.ParametrosRN;
import bs.administracion.rn.VistaRN;
import bs.global.util.JsfUtil;
import bs.global.web.GenericBean;
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
public class VistaBean extends GenericBean implements Serializable {

    private Vista vista;
    private VistaDetalle vistaDetalle;
    private List<Vista> lista;
    private Parametro parametro;

    private boolean seleccionaTodoVisible;
    private boolean seleccionaTodoSoloLectura;
    private boolean seleccionaTodoRequerido;

    @EJB
    private VistaRN vistaRN;
    @EJB
    private ParametrosRN parametrosRN;

    @ManagedProperty(value = "#{moduloBean}")
    private ModuloBean moduloBean;

    public VistaBean() {

    }

    @PostConstruct
    public void init() {
        txtBusqueda = "";
        parametro = parametrosRN.getParametro("01");
        mostrarDebaja = false;
        nuevo();
        buscar();
    }

    public void nuevo() {

        esNuevo = true;
        buscaMovimiento = false;
        vista = new Vista();
        vista.setOrigen(parametro.getOrigenPorDefecto());
        vistaDetalle = new VistaDetalle();
        obtenerCodigo();
    }

    public void guardar(boolean nuevo) {

        try {
            if (vista != null) {

                if (vista.getOrigen().equals("SIS") && !parametro.getOrigenPorDefecto().equals("SIS")) {
                    JsfUtil.addErrorMessage("No es posible modificar un menú de sistema, duplicar el menú y guardarlo como menu usuario.");
                    return;
                }

                vistaRN.guardar(vista, esNuevo);
                esNuevo = false;
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");

                if (nuevo) {
                    nuevo();
                }
            } else {
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(VistaBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }

    public void agregarItem() {

        vistaDetalle.setVista(vista);
        vista.getDetalle().add(vistaDetalle);
        vistaDetalle = new VistaDetalle();
    }

    public void eliminarItem() {

    }

    public void duplicar() {

        if (vista == null || vista.getDetalle() == null) {
            JsfUtil.addErrorMessage("No hay vista activa o la lista de parámetros es nula");
            return;
        }

        Vista vistaAux = vista;
        vista = new Vista();
        vista.setNombre(vistaAux.getNombre());
        vista.setPath(vistaAux.getPath());
        vista.setOrigen(parametro.getOrigenPorDefecto());
        vista.setModulo(vistaAux.getModulo());

        if (vistaAux.getDetalle() != null) {

            vista.setDetalle(new ArrayList<VistaDetalle>());

            for (VistaDetalle d : vistaAux.getDetalle()) {

                VistaDetalle dnew = new VistaDetalle();
                //codigo;
                dnew.setNombre(d.getNombre());
                dnew.setTipo(d.getTipo());
                dnew.setCampo(d.getCampo());
                dnew.setVisible(d.isVisible());
                dnew.setSoloLectura(d.isSoloLectura());
                dnew.setRequerido(d.isRequerido());
                dnew.setOrigen(d.getOrigen());
                dnew.setVista(vista);
                vista.getDetalle().add(dnew);
            }
        }

        obtenerCodigo();
        esNuevo = true;
    }

    public void habilitaDeshabilita(String habDes) {

        try {
            vista.getAuditoria().setDebaja(habDes);
            vistaRN.guardar(vista, false);
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");

        } catch (Exception ex) {
            Logger.getLogger(VistaBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }

    public void eliminar() {
        try {
            vistaRN.eliminar(vista);
            nuevo();
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }
    }

    public void buscar() {

        if (moduloBean.getModulo() != null && moduloBean.getModulo().getCodigo() != null) {
            filtro.clear();
            filtro.put("modulo.codigo = ", "'" + moduloBean.getModulo().getCodigo() + "'");
        }

        lista = vistaRN.getListaByBusqueda(filtro, txtBusqueda, mostrarDebaja);
    }

    public List<Vista> complete(String query) {
        try {
            lista = vistaRN.getListaByBusqueda(query, false);
            return lista;

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Vista>();
        }
    }

    public void onSelect(SelectEvent event) {
        vista = (Vista) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }

    public void seleccionar(Vista d) {

        vista = d;
        esNuevo = false;
        buscaMovimiento = false;
    }

    public void imprimir() {

        if (vista == null) {
            JsfUtil.addErrorMessage("No hay entidad seleccionada, nada para imprimir");
        }
    }

    public void seleccionarTodo(String campo) {

        for (VistaDetalle i : vista.getDetalle()) {

            if (campo.equals("visible")) {
                i.setVisible(seleccionaTodoVisible);
            }

            if (campo.equals("soloLectura")) {
                i.setVisible(seleccionaTodoSoloLectura);
            }

            if (campo.equals("requerido")) {
                i.setVisible(seleccionaTodoRequerido);
            }
        }
    }

    public void obtenerCodigo() {

        if (vista == null || vista.getOrigen() == null) {
            return;
        }

        String codigo = vistaRN.obtenerSiguienteCogido(vista.getOrigen());
        vista.setCodigo(codigo);

        if (vista.getDetalle() != null) {

            int idDetalle = 1;

            for (VistaDetalle d : vista.getDetalle()) {
                d.setCodigo(vista.getCodigo() + "_" + String.valueOf(idDetalle));
                idDetalle++;
            }
        }
    }

    @Override
    public Vista getVista() {
        return vista;
    }

    @Override
    public void setVista(Vista vista) {
        this.vista = vista;
    }

    public List<Vista> getLista() {
        return lista;
    }

    public void setLista(List<Vista> lista) {
        this.lista = lista;
    }

    public boolean isSeleccionaTodoVisible() {
        return seleccionaTodoVisible;
    }

    public void setSeleccionaTodoVisible(boolean seleccionaTodoVisible) {
        this.seleccionaTodoVisible = seleccionaTodoVisible;
    }

    public boolean isSeleccionaTodoSoloLectura() {
        return seleccionaTodoSoloLectura;
    }

    public void setSeleccionaTodoSoloLectura(boolean seleccionaTodoSoloLectura) {
        this.seleccionaTodoSoloLectura = seleccionaTodoSoloLectura;
    }

    public boolean isSeleccionaTodoRequerido() {
        return seleccionaTodoRequerido;
    }

    public void setSeleccionaTodoRequerido(boolean seleccionaTodoRequerido) {
        this.seleccionaTodoRequerido = seleccionaTodoRequerido;
    }

    public VistaDetalle getVistaDetalle() {
        return vistaDetalle;
    }

    public void setVistaDetalle(VistaDetalle vistaDetalle) {
        this.vistaDetalle = vistaDetalle;
    }

    public Parametro getParametro() {
        return parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }

    public ModuloBean getModuloBean() {
        return moduloBean;
    }

    public void setModuloBean(ModuloBean moduloBean) {
        this.moduloBean = moduloBean;
    }

}
