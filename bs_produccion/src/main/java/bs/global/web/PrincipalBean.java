package bs.global.web;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import bs.global.util.JsfUtil;
import bs.seguridad.modelo.ItemMenuFavorito;
import bs.seguridad.modelo.ItemMenuReciente;
import bs.seguridad.modelo.ItemMenuUsuario;
import bs.seguridad.modelo.Menu;
import bs.seguridad.modelo.Usuario;
import bs.seguridad.rn.ItemMenuFavoritoRN;
import bs.seguridad.rn.ItemMenuRecienteRN;
import bs.seguridad.rn.MenuRN;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.model.menu.Submenu;

/**
 *
 * @author ctrosch
 */
@ManagedBean
@SessionScoped
public class PrincipalBean extends GenericBean implements Serializable {

    private String paginaActiva = "/seguridad/login";
    private String contextPath = "";
    private MenuModel modelo;
    private List<Menu> menuPrincipal;
    private List<Menu> menuUsuario;
    List<ItemMenuUsuario> permisosAsignados;
    private List<Menu> resultadoBusqueda;
    private List<ItemMenuReciente> recientes;
    private List<ItemMenuFavorito> favoritos;
    private Usuario usuario;

    @EJB
    private MenuRN menuRN;
    @EJB
    private ItemMenuRecienteRN itemMenuRecienteRN;
    @EJB
    private ItemMenuFavoritoRN itemMenuFavoritoRN;

    @PostConstruct
    public void init() {
        recientes = new ArrayList<ItemMenuReciente>();
    }

    public PrincipalBean() {

        modelo = new DefaultMenuModel();
        contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    }

    public void buscarMenuItem() {

        if (!txtBusqueda.isEmpty()) {

            resultadoBusqueda = menuRN.getItemsByTexto(usuario.getId(), txtBusqueda);

            if (resultadoBusqueda.isEmpty()) {
                JsfUtil.addInfoMessage("No se encontraron resultados para \"" + txtBusqueda + "\"");
            }
        } else {
            resultadoBusqueda.clear();
        }
    }

    public void agregarReciente(Menu itemMenu) {
        try {

            ItemMenuReciente ir = new ItemMenuReciente();

            ir.setCodMenu(itemMenu.getCodigo());
            ir.setIdUsuario(usuario.getId());
            ir.setUsuario(usuario);
            ir.setMenu(itemMenu);

            if (recientes == null) {
                recientes = new ArrayList<ItemMenuReciente>();
            }

            if (recientes.contains(ir)) {

                if (recientes.remove(ir)) {
                    recientes.add(0, ir);
                    reordenarRecientes();
                }
                return;
            }

            if (recientes.size() >= 15) {
                recientes.remove(recientes.size() - 1);
            }
            recientes.add(0, ir);
            reordenarRecientes();
            itemMenuRecienteRN.guardar(ir);

//            EventBusFactory ebf = EventBusFactory.getDefault();
//
//            if (ebf != null) {
//                EventBus eventBus = ebf.eventBus();
//                eventBus.publish("/reciente/"+usuario.getUsuario()+"/", usuario.getUsuario());
//            }

        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Problemas para agregar recientes");
            Logger.getLogger(PrincipalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reordenarRecientes() {

        int orden = 0;

        for (ItemMenuReciente ir : recientes) {
            try {
                ir.setOrden(orden);
                itemMenuRecienteRN.guardar(ir);
                orden++;
            } catch (Exception ex) {
                Logger.getLogger(PrincipalBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void agregarFavorito(Menu itemMenu) {

        try {
            ItemMenuFavorito i = new ItemMenuFavorito();

            i.setCodMenu(itemMenu.getCodigo());
            i.setIdUsuario(usuario.getId());
            i.setUsuario(usuario);
            i.setMenu(itemMenu);

            if (favoritos == null) {
                favoritos = new ArrayList<ItemMenuFavorito>();
            }

            if (favoritos.contains(i)) {

                if (favoritos.remove(i)) {
                    favoritos.add(0, i);
                }
                return;
            }

            if (favoritos.size() >= 15) {
                favoritos.remove(favoritos.size() - 1);
            }
            favoritos.add(0, i);
            itemMenuFavoritoRN.guardar(i);
            JsfUtil.addInfoMessage("El acceso se ha guardado en favoritos");

        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Problemas para agregar favoritos");
            Logger.getLogger(PrincipalBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarRecienteById(String idMenu) {

        Menu m = menuRN.getMenu(idMenu);
        agregarReciente(m);
    }

    public String navegar(ActionEvent ae) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(true);

        if (session.isNew()) {
            paginaActiva = "../seguridad/login.xhtml";
        }

        MenuItem itm = (MenuItem) ae.getSource();
        Menu menu = (Menu) itm.getValue();
//        System.out.println("valor de item = " + itm.getValue());
        paginaActiva = menu.getAccion();

//        System.out.println("Pagina activa: " + paginaActiva);
        //return ".."+paginaActiva+".xhtml";
        paginaActiva = "../seguridad/login.xhtml";
        return paginaActiva;
    }

    public void cargarMenu(Usuario u) {

        if (u == null) {
            JsfUtil.addWarningMessage("El usuario no existe");
            return;
        }

        usuario = u;
        permisosAsignados = menuRN.getMenuByUsuario(u);

        if (permisosAsignados == null) {
            JsfUtil.addWarningMessage("El usuario no tiene permisos asignados");
            return;
        }

        if (permisosAsignados.isEmpty()) {
            JsfUtil.addWarningMessage("El usuario no tiene permisos asignados");
            return;
        }

        menuPrincipal = menuRN.getListaByNivel(1, true);

        if (menuPrincipal == null) {
            JsfUtil.addWarningMessage("No es posible obtener el menu del sistema");
            return;
        }

        menuUsuario = new ArrayList<Menu>();
        Menu menuRaiz = new Menu();
        generarArbolMenu(u, menuPrincipal, menuRaiz);
        menuUsuario = menuRaiz.getMenuItem();
        generarMenuModel(u);

        recientes = itemMenuRecienteRN.getItemMenuRecienteByUsuario(usuario);
        favoritos = itemMenuFavoritoRN.getItemMenuFavoritoByUsuario(usuario);
    }

    private void generarArbolMenu(Usuario u, List<Menu> itemsMenu, Menu menuRaiz) {

        if (itemsMenu == null) {
            return;
        }

        //Recorremos menu principal
        for (Menu m : itemsMenu) {

            ItemMenuUsuario um = new ItemMenuUsuario(u.getId(), m.getCodigo());

//            System.err.println("Menu:" + m.getNombre()+" - " + m.getActivo()+ "Existe:" + permisosAsignados.contains(um));
            if (m.getActivo().equals("N")) {
                continue;
            }

            if (permisosAsignados.contains(um) && m.getActivo().equals("S")) {

//                System.err.println("Agregar:  " + m.getNombre()+" - " + m.getActivo());
                Menu menu = new Menu(m);
                menuRaiz.getMenuItem().add(menu);
                generarArbolMenu(u, m.getMenuItem(), menu);
            }
        }
    }

    private void generarMenuModel(Usuario usuario) {

        menuPrincipal = menuRN.getListaByNivel(1, true);

        for (Menu mp : menuPrincipal) {

            if (mp.getMenuItem() == null || mp.getMenuItem().isEmpty()) {
                continue;
            }

            if (mp.getActivo().equals("N")) {
                continue;
            }

            DefaultSubMenu subMenu = new DefaultSubMenu(mp.getNombre());
            subMenu.setId("Item" + mp.getCodigo());
            subMenu.setIcon("fa " + mp.getIcono());
            generarArbol(usuario, subMenu, mp.getMenuItem());

            if (subMenu.getElementsCount() > 0) {
                modelo.addElement(subMenu);
            }
        }
    }

    private void generarArbol(Usuario usuario, DefaultSubMenu menuRaiz, List<Menu> items) {

        if (items == null || items.isEmpty()) {
            return;
        }

        //Recorremos menu principal
        for (Menu m : items) {

            ItemMenuUsuario um = new ItemMenuUsuario(usuario.getId(), m.getCodigo());

            if (m.getActivo().equals("N")) {
                continue;
            }

            if (!permisosAsignados.contains(um)) {
                continue;
            }

            if (m.getMenuItem() == null || m.getMenuItem().isEmpty()) {

//                System.out.println("\tMenú item: " +m.getNombre());
                DefaultMenuItem item = new DefaultMenuItem(m.getNombre());
                item.setId("Item" + m.getCodigo());
                item.setUrl(m.getUrlCompleta());
                item.setTarget("_blank");
                item.setAjax(false);
                item.setIcon("fa " + m.getIcono());
                item.setOncomplete("mostrarAlerta('" + m.getCodigo() + "')");
//                item.setCommand("#{principalBean.probar}");
//                item.setUpdate(":formulario");

                menuRaiz.addElement(item);
            } else {

//                System.out.println("\tMenú: " +m.getNombre());
                DefaultSubMenu subMenu = new DefaultSubMenu(m.getNombre());
                subMenu.setId("Item" + m.getCodigo());
                subMenu.setIcon("fa " + m.getIcono());
                generarArbol(usuario, subMenu, m.getMenuItem());

                if (subMenu.getElementsCount() > 0) {
                    menuRaiz.addElement(subMenu);
                }
            }
        }
    }

//    private void generarArbol(Submenu submenu, Usuario u, List<Menu> items){
//
//        //Si el items está vacío no hace nada
//        if (items.isEmpty()){
//            return;
//        }
//
//        //Recorremos menu principal
//        for(Menu m: items){
//
//            if(!m.getActivo()){
//                continue;
//            }
//
//            ItemMenuUsuario um = new ItemMenuUsuario(u.getId(), m.getId());
//
//            //Si no tiene hijos creamos el item
//            if(m.getMenuItem().isEmpty()){
//                
//                if(u.getMenu().contains(um)){
//
////                    System.out.println("Usuario : " + u.getUsuario() + "- Item Menu: "+ m.getNombre());
//
//                    MenuItem item = new DefaultMenuItem(m.getNombre());
//
//                    item.setId("Item" + m.getId());
//                    
//                    item.setParam("url", ".."+m.getUrl()+".jsf");
//                    item.setParam("param","_blank");
//
//                    switch (m.getTipo()){
//
//                        //Registración
//                        case 1:
//                            item.setParam("icon","form16");
//                            break;
//                        //Informes
//                        case 2:
//                            item.setParam("icon","report24");
//                            break;
//                        //Configuracion
//                        case 3:
//                            item.setParam("icon","config16");
//                            break;
//                         //Impresiones
//                        case 4:
//                            item.setParam("icon","print16");
//                            break;
//
//                        default:
//                            item.setParam("icon","disk00");
//                            break;
//                    }
//
////                    if(m.getNivel()==2) item.setIcon("disk00");
////                    if(m.getNivel()==3) item.setIcon("disk00");
////                    if(m.getNivel()==4) item.setIcon("disk01");
//                    
//                    submenu.getElements().add(item);
//                }
//                
//            }else{
//
//                if(u.getMenu().contains(um)){
//                    
////                    System.out.println("Usuario : " + u.getUsuario() + "- Sub Menu: "+ m.getNombre());
//
//                    //Si tiene hijos seguimos cargando el submenu
//                    Submenu submenu1 = new DefaultSubMenu(m.getNombre());
//
//                    submenu1.setId("Item" + m.getId());                    
//                    generarArbol(submenu1, u, m.getMenuItem());
//
//                    submenu.getElements().add(submenu1);
//                    //Si el menu no tiene item, entonces no lo agregamos
//
//                    
//
//                  }
//            }            
//        }
//    }
    private void generarMenu(Submenu submenu, Menu menuItem) {

        if (menuItem.getMenuItem().isEmpty()) {

            MenuItem item = new DefaultMenuItem(menuItem.getNombre());
            item.setParam("url", ".." + menuItem.getUrl() + ".jsf");
            item.setParam("target", "_blank");

            if (menuItem.getNivel() == 3) {
                item.setParam("icon", "disk00");
            }
            if (menuItem.getNivel() == 4) {
                item.setParam("icon", "disk01");
            }
            /**
             * FacesContext context = FacesContext.getCurrentInstance();
             * MethodExpression actionListener =
             * context.getApplication().getExpressionFactory().createMethodExpression(
             * context.getELContext(), "#{navegadorMB.navegar}", null, new
             * Class[] { ActionEvent.class }); item.addActionListener(new
             * MethodExpressionActionListener(actionListener));
             *
             */
            item.setId("Item" + menuItem.getCodigo());

            submenu.getElements().add(item);

            //System.out.println("Se agrega Item: "+ menuItem.getNombre());
        } else {

            Submenu submenu1 = new DefaultSubMenu(menuItem.getNombre());
            submenu1.setId("Item" + menuItem.getCodigo());

            for (Menu item : menuItem.getMenuItem()) {

                if (item.getActivo().equals("S")) {
                    generarMenu(submenu1, item);
                }
            }

            //Si el menu no tiene item, entonces no lo agregamos
            if (submenu1.getElementsCount() != 0) {
                submenu.getElements().add(submenu1);
            }
        }
    }

    public List<Menu> getMenuPrincipal() {
        return menuPrincipal;
    }

    public void setMenuPrincipal(List<Menu> menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
    }

    public List<Menu> getResultadoBusqueda() {
        return resultadoBusqueda;
    }

    public void setResultadoBusqueda(List<Menu> resultadoBusqueda) {
        this.resultadoBusqueda = resultadoBusqueda;
    }

    public List<Menu> getMenuUsuario() {
        return menuUsuario;
    }

    public void setMenuUsuario(List<Menu> menuUsuario) {
        this.menuUsuario = menuUsuario;
    }

    public MenuModel getModelo() {
        return modelo;
    }

    public void setModelo(MenuModel modelo) {
        this.modelo = modelo;
    }

    public String getPaginaActiva() {
        return paginaActiva;
    }

    public void setPaginaActiva(String paginaActiva) {
        this.paginaActiva = paginaActiva;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ItemMenuReciente> getRecientes() {
        return recientes;
    }

    public void setRecientes(List<ItemMenuReciente> recientes) {
        this.recientes = recientes;
    }

    public List<ItemMenuFavorito> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<ItemMenuFavorito> favoritos) {
        this.favoritos = favoritos;
    }

}
