/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.seguridad.web;

import bs.global.util.JsfUtil;
import bs.global.web.GenericBean;
import bs.seguridad.modelo.ItemMenuUsuario;
import bs.seguridad.modelo.Menu;
import bs.seguridad.modelo.Usuario;
import bs.seguridad.rn.MenuRN;
import bs.seguridad.rn.UsuarioRN;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Pablo
 */
@ManagedBean
@ViewScoped
public class PermisosBean extends GenericBean implements Serializable {

    @EJB
    private UsuarioRN usuarioRN;
    @EJB
    private MenuRN menuRN;

    private Usuario usuario;
    List<ItemMenuUsuario> permisosAsignados;
    private List<Usuario> lista;
    private TreeNode menu;
    private TreeNode nodoSeleccionado;

    @ManagedProperty(value = "#{usuarioSessionBean}")
    protected UsuarioSessionBean usuarioSessionBean;

    /**
     * Creates a new instance of UsuarioMB
     */
    public PermisosBean() {

    }

    @PostConstruct
    public void init() {
        lista = usuarioRN.getLista();
        menu = new DefaultTreeNode();

    }

    public void guardar() {

    }

    public void seleccionar(Usuario u) {
        usuario = u;
        //Actualizamos la lista de permisos asignados
        permisosAsignados = menuRN.getMenuByUsuario(usuario);
        //Generamos un nuevo arbol
        menu = new DefaultTreeNode();
        //Generamos el arbol para el usuario
        generarArbol(menu, menuRN.getListaByNivel(1, false));
    }

    public void cargarMenu() {

        menu = new DefaultTreeNode();
        generarArbol(menu, menuRN.getListaByNivel(1, false));
    }

    private void generarArbol(TreeNode raiz, List<Menu> items) {

        if (items.isEmpty()) {
            return;
        }

        if (usuario == null) {
            return;
        }

        //Recorremos menu principal
        for (Menu m : items) {

            ItemMenuUsuario um = new ItemMenuUsuario(usuario.getId(), m.getCodigo());
            TreeNode hoja = null;

            if (permisosAsignados.contains(um)) {
                hoja = new DefaultTreeNode("habilitado", m, raiz);
                hoja.setExpanded(true);
            } else {
                hoja = new DefaultTreeNode("bloqueado", m, raiz);
                hoja.setExpanded(true);
            }

            generarArbol(hoja, m.getMenuItem());
        }
    }

    public void habilitarMenu() {
        try {
            Menu m = (Menu) nodoSeleccionado.getData();

            if (!usuarioSessionBean.esAdministrador() 
                    && (m.getModulo().getCodigo().equals("AS") 
                    || m.getModulo().getCodigo().equals("SG")
                    || m.getModulo().getCodigo().equals("WS"))) {
                JsfUtil.addWarningMessage("", "Solo el administrador puede habilitar este permiso");
                return;
            }

            menuRN.HabilitarMenu(usuario, m);
            permisosAsignados = menuRN.getMenuByUsuario(usuario);
            cargarMenu();

            JsfUtil.addInfoMessage("El menú fue habilitado");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("No es posible la habilitación");
        }
    }

    public void bloquearMenu() {
        try {
            Menu m = (Menu) nodoSeleccionado.getData();
            
            if (!usuarioSessionBean.esAdministrador() 
                    && (m.getModulo().getCodigo().equals("AS") 
                    || m.getModulo().getCodigo().equals("SG")
                    || m.getModulo().getCodigo().equals("WS"))) {
                JsfUtil.addWarningMessage("", "Solo el administrador puede bloquear este permiso");
                return;
            }

            menuRN.BloquearMenu(usuario, m);
            permisosAsignados = menuRN.getMenuByUsuario(usuario);
            cargarMenu();

            JsfUtil.addInfoMessage("El menú fue bloqueado");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("No es posible el bloqueo");
        }
    }

    public void eliminar() {

    }

    public List<Usuario> getLista() {
        return lista;
    }

    public void setLista(List<Usuario> lista) {
        this.lista = lista;
    }

    public TreeNode getMenu() {
        return menu;
    }

    public void setMenu(TreeNode menu) {
        this.menu = menu;
    }

    public TreeNode getNodoSeleccionado() {
        return nodoSeleccionado;
    }

    public void setNodoSeleccionado(TreeNode nodoSeleccionado) {
        this.nodoSeleccionado = nodoSeleccionado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ItemMenuUsuario> getPermisosAsignados() {
        return permisosAsignados;
    }

    public void setPermisosAsignados(List<ItemMenuUsuario> permisosAsignados) {
        this.permisosAsignados = permisosAsignados;
    }

    public UsuarioSessionBean getUsuarioSessionBean() {
        return usuarioSessionBean;
    }

    public void setUsuarioSessionBean(UsuarioSessionBean usuarioSessionBean) {
        this.usuarioSessionBean = usuarioSessionBean;
    }

}
