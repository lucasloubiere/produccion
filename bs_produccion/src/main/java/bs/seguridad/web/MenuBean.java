/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.seguridad.web;

import bs.administracion.modelo.Parametro;
import bs.administracion.web.ModuloBean;
import bs.administracion.web.ReporteBean;
import bs.administracion.web.VistaBean;
import bs.global.util.JsfUtil;
import bs.global.web.GenericBean;
import bs.seguridad.modelo.Menu;
import bs.seguridad.modelo.MenuParametro;
import bs.seguridad.rn.MenuParametroRN;
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
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Claudio
 */
@ManagedBean
@ViewScoped
public class MenuBean extends GenericBean implements Serializable {

    @EJB
    private MenuParametroRN menuParametroRN;


    private Menu menu;
    private List<Menu> lista;
    private List<Menu> listaCompleta;

    private TreeNode arbol;
    private TreeNode nodoSeleccionado;
    private Parametro parametro;

    @ManagedProperty(value = "#{menuParametroBean}")
    protected MenuParametroBean menuParametroBean;

    @ManagedProperty(value = "#{moduloBean}")
    protected ModuloBean moduloBean;

    @ManagedProperty(value = "#{vistaBean}")
    protected VistaBean vistaBean;
    
    @ManagedProperty(value = "#{reporteBean}")
    protected ReporteBean reporteBean;

    /**
     * Creates a new instance of MenuBean
     */
    public MenuBean() {
    }

    @PostConstruct
    public void init() {

        titulo = "Menu Sistema";
        parametro = parametrosRN.getParametro("01");
        moduloBean.buscar();
        actualizarArbol();
    }

    public void actualizarArbol() {

        lista = menuRN.getListaByNivel(1, false);
        listaCompleta = menuRN.getLista();
        arbol = new DefaultTreeNode();
        generarArbol(arbol, lista);
    }

    private void generarArbol(TreeNode raiz, List<Menu> items) {

        if (items == null) {
            return;
        }

        if (items.isEmpty()) {
            return;
        }
        //Recorremos menu principal
        for (Menu m : items) {

            TreeNode hoja = new DefaultTreeNode(m, raiz);
            hoja.setExpanded(true);
            List<Menu> subItems = menuRN.getItemsByMenu(m);
            generarArbol(hoja, subItems);
        }
    }

    public void onNodeSelect(NodeSelectEvent event) {

        nodoSeleccionado = event.getTreeNode();
        nodoSeleccionado.setExpanded(true);
        menu = (Menu) event.getTreeNode().getData();
        sincronizarParametros();
        esNuevo = false;
    }

    public void nuevoHijo(){

        Menu mPadre = menu;
        menu = new Menu();
        menu.setNivel(mPadre.getNivel() + 1);
        menu.setOrden(mPadre.getMenuItem().size() + 1);
        menu.setMenuPrincipal(mPadre);
        menu.setModulo(mPadre.getModulo());
        menu.setOrigen(parametro.getOrigenPorDefecto());
        mPadre.getMenuItem().add(menu);

        obtenerCodigo();
        esNuevo = true;
    }

    public void nuevoParametro() {
        
        if (menu != null) {
            
            if(menu.getOrigen().equals("SIS") && !parametro.getOrigenPorDefecto().equals("SIS")){
                JsfUtil.addErrorMessage("No es posible modificar un menú de sistema, duplicar el menú y guardarlo como menu usuario.");
                return;
            }
            
            if (menu.getParametros() == null) {
                menu.setParametros(new ArrayList<MenuParametro>());
            }

            MenuParametro parametro = new MenuParametro();
            parametro.setMenu(menu);            
            sincronizarCodigoDetalle();
            
            menuParametroBean.nuevo(parametro);

        } else {

            JsfUtil.addErrorMessage("No existe un menú seleccionado al cual agregarle parámetros");
        }
    }

    public void guardar() {
        try {
            
            if(menu.getOrigen().equals("SIS") && !parametro.getOrigenPorDefecto().equals("SIS")){
                JsfUtil.addErrorMessage("No es posible modificar un menú de sistema, duplique el menú y guardelo como menu usuario.");
                return;
            }
            
            menuRN.guardar(menu);
            actualizarArbol();
            esNuevo = false;
            JsfUtil.addInfoMessage("Los datos se guardaron correctamente");

        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Error al guardar los datos");
        }
    }

    public void borrar(Menu m) {

        try {
            menu = null;
            menuRN.eliminar(m);
            actualizarArbol();
            JsfUtil.addWarningMessage("El menu fue borrado");

        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Error al borrar los datos");
        }
    }

    public void borarItemVacio() {

        for (Menu mv : menu.getMenuItem()) {

            if (mv.getNombre() == null) {
                menu.getMenuItem().remove(mv);
            }
        }
    }

    public void sincronizarParametros() {

        if (menu != null) {
            menu.setParametros(menuParametroRN.getListaByBusqueda("", menu.getCodigo(), true));
        }
    }

    public void selecionar(Menu m) {

        menu = m;
        borarItemVacio();
        esNuevo = false;

    }

    public void seleccionarMenuParametro(MenuParametro p) {

        if (menu != null) {            
            
            menuParametroBean.setMenu(menu);
            menuParametroBean.seleccionar(p);

        } else {

            JsfUtil.addErrorMessage("No existe un menú seleccionado al cual agregarle parámetros");
        }
    }

    public void duplicar() {

        if (menu == null || menu.getParametros() == null) {
            JsfUtil.addErrorMessage("No hay menú activo o la lista de parámetros es nula");
            return;
        }

        Menu menuAux = menu;
        menu = new Menu();
        menu.setNombre(menuAux.getNombre()+"(duplicado)");
        menu.setOrden(menuAux.getMenuPrincipal().getMenuItem().size() + 1);
        menu.setTipo(menuAux.getTipo());
        menu.setIcono(menuAux.getIcono());
        menu.setAccion(menuAux.getAccion());
        menu.setUrl(menuAux.getUrl());
        menu.setUpdate(menuAux.getUpdate());
        menu.setActivo(menuAux.getActivo());
        menu.setNivel(menuAux.getNivel());
        menu.setOncomplete(menuAux.getOncomplete());
        menu.setAyuda(menuAux.getAyuda());
        menu.setOrigen(parametro.getOrigenPorDefecto());
        menu.setModulo(menuAux.getModulo());
        menu.setVista(menuAux.getVista());
        menu.setMenuPrincipal(menuAux.getMenuPrincipal());
        menuAux.getMenuPrincipal().getMenuItem().add(menu);

        if (menuAux.getParametros() != null) {

            menu.setParametros(new ArrayList<MenuParametro>());

            for (MenuParametro p : menuAux.getParametros()) {

                MenuParametro pnew = new MenuParametro();
                pnew.setNombre(p.getNombre());
                pnew.setValor(p.getValor());
                pnew.setMenu(menu);
                menu.getParametros().add(pnew);
            }
        }

        obtenerCodigo();
        esNuevo = true;
    }

    /*
    De acuerdo a tipo de menú, cargamos los parámetros más comunes relacionados
     */
    public void cargarParametroPorDefecto() {

        if (menu == null || menu.getParametros() == null) {
            JsfUtil.addErrorMessage("No hay menú activo o la lista de parámetros es nula");
            return;
        }
        try {
            switch (menu.getTipo().intValue()) {
                //Registracion 
                case 1:

                    agregarParametro("NOMRPT", "Nombre reporte");
                    agregarParametro("NOMARC", "Nombre archivo");
                    sincronizarParametros();
                    break;

                //Informe
                case 2:

                    agregarParametro("CIRCOM", "Circuito inicio");
                    agregarParametro("CIRAPL", "Circuito aplicacion");
                    agregarParametro("SUCURS", "Sucursal");
                    sincronizarParametros();

                    break;

                //Impresión
                case 4:
            }
        } catch (Exception ex) {
            Logger.getLogger(MenuBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void agregarParametro(String nombre, String valor) throws Exception {

        MenuParametro p1 = new MenuParametro();
        p1.setMenu(menu);
        p1.setNombre(nombre);
        p1.setValor(valor);

        menuParametroRN.guardar(p1, true);
    }

    public void procesarVista() {

        if (vistaBean.getVista() != null) {
            menu.setVista(vistaBean.getVista());
        }
    }

    public void obtenerCodigo() {

        if (menu == null || menu.getOrigen() == null) {
            return;
        }

        String codigo = menuRN.obtenerSiguienteCogido(menu.getOrigen());
        menu.setCodigo(codigo);

        sincronizarCodigoDetalle();
    }

    public void sincronizarCodigoDetalle(){
        
        if (menu.getParametros() != null) {
            int idParametro = 1;

            for (MenuParametro p : menu.getParametros()) {
                p.setId(menu.getCodigo() + "_" + String.valueOf(idParametro));
                idParametro++;
            }
        }
    }
    
    public void procesarReporte() {

        if (reporteBean.getReporte() != null) {
            menu.setReporte(reporteBean.getReporte());
        }
    }
    
    //----------------------------------------------------------------------    
    public Menu getMenu() {

        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public TreeNode getArbol() {
        return arbol;
    }

    public void setArbol(TreeNode arbol) {
        this.arbol = arbol;
    }

    public TreeNode getNodoSeleccionado() {
        return nodoSeleccionado;
    }

    public void setNodoSeleccionado(TreeNode nodoSeleccionado) {
        this.nodoSeleccionado = nodoSeleccionado;
    }

    public List<Menu> getLista() {
        return lista;
    }

    public void setLista(List<Menu> lista) {
        this.lista = lista;
    }

    public MenuParametroBean getMenuParametroBean() {
        return menuParametroBean;
    }

    public void setMenuParametroBean(MenuParametroBean menuParametroBean) {
        this.menuParametroBean = menuParametroBean;
    }

    public ModuloBean getModuloBean() {
        return moduloBean;
    }

    public void setModuloBean(ModuloBean moduloBean) {
        this.moduloBean = moduloBean;
    }

    public VistaBean getVistaBean() {
        return vistaBean;
    }

    public void setVistaBean(VistaBean vistaBean) {
        this.vistaBean = vistaBean;
    }

    public List<Menu> getListaCompleta() {
        return listaCompleta;
    }

    public void setListaCompleta(List<Menu> listaCompleta) {
        this.listaCompleta = listaCompleta;
    }

    public Parametro getParametro() {
        return parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }

    public ReporteBean getReporteBean() {
        return reporteBean;
    }

    public void setReporteBean(ReporteBean reporteBean) {
        this.reporteBean = reporteBean;
    }
    
    
    
}
