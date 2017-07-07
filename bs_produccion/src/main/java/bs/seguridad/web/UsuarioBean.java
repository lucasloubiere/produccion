/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.seguridad.web;

import bs.global.util.JsfUtil;
import bs.global.web.GenericBean;
import bs.seguridad.modelo.Usuario;
import bs.seguridad.rn.EstadoUsuarioRN;
import bs.seguridad.rn.UsuarioRN;
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
 * @author Pablo
 */
@ManagedBean
@ViewScoped
public class UsuarioBean extends GenericBean implements Serializable {

    private Usuario usuario;
    private List<Usuario> lista;

    @EJB
    private UsuarioRN usuarioRN;
    @EJB
    private EstadoUsuarioRN estadoUsuarioRN;

    @ManagedProperty(value = "#{usuarioSessionBean}")
    protected UsuarioSessionBean usuarioSessionBean;

    public UsuarioBean() {

    }

    @PostConstruct
    public void init() {
        txtBusqueda = "";
        mostrarDebaja = false;
        nuevo();
        buscar();
    }

    public void iniciarVariables() {

        try {
            if (!beanIniciado) {

                beanIniciado = true;
            }
        } catch (Exception ex) {
//            e.printStackTrace();
            JsfUtil.addErrorMessage("Error al iniciar el bean " + ex);
        }
    }

    public void nuevo() {

        if (usuarioSessionBean.estaRegistrado
                && usuarioSessionBean.getUsuario() != null
                && usuarioSessionBean.getUsuario().getTipo().getId() != 1) {

            JsfUtil.addWarningMessage("", "Solo el administrador puede agregar usuarios");
        }
        esNuevo = true;
        buscaMovimiento = false;
        usuario = new Usuario();
    }

    public void guardar(boolean nuevo) {

        try {
            if (usuario != null) {

                if (usuarioSessionBean.estaRegistrado
                        && usuarioSessionBean.getUsuario() != null
                        && usuarioSessionBean.getUsuario().getTipo().getId() != 1
                        && esNuevo) {
                    JsfUtil.addWarningMessage("", "Solo el administrador puede agregar usuarios");
                    return;
                }

                usuarioRN.guardar(usuario);
                esNuevo = false;
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");

                if (nuevo) {
                    nuevo();
                }
            } else {
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }

    public void habilitaDeshabilita(String habDes) {

        try {

            if (habDes.equals("S")) {
                usuario.setEstado(estadoUsuarioRN.getEstado(1));
            } else {
                usuario.setEstado(estadoUsuarioRN.getEstado(9));
            }

            usuarioRN.guardar(usuario);
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");

        } catch (Exception ex) {
            Logger.getLogger(UsuarioBean.class
                    .getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }

    public void eliminar() {
        try {
            usuarioRN.eliminar(usuario);
            nuevo();
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }
    }

    public void buscar() {
        lista = usuarioRN.getUsuarioByBusqueda(txtBusqueda, mostrarDebaja);
    }

    public List<Usuario> complete(String query) {
        try {
            lista = usuarioRN.getUsuarioByBusqueda(query, false);
            return lista;

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Usuario>();
        }
    }

    public void onSelect(SelectEvent event) {
        usuario = (Usuario) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }

    public void seleccionar(Usuario d) {

        usuario = d;
        esNuevo = false;
        buscaMovimiento = false;
    }

    public void imprimir() {

        if (usuario == null) {
            JsfUtil.addErrorMessage("No hay entidad seleccionada, nada para imprimir");
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getLista() {
        return lista;
    }

    public void setLista(List<Usuario> lista) {
        this.lista = lista;
    }

    public UsuarioSessionBean getUsuarioSessionBean() {
        return usuarioSessionBean;
    }

    public void setUsuarioSessionBean(UsuarioSessionBean usuarioSessionBean) {
        this.usuarioSessionBean = usuarioSessionBean;
    }

}
