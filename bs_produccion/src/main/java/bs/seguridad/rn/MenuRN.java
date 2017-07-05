/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.seguridad.rn;

import bs.seguridad.dao.MenuDAO;
import bs.seguridad.modelo.ItemMenuUsuario;
import bs.seguridad.modelo.ItemMenuUsuarioPK;
import bs.seguridad.modelo.Menu;
import bs.seguridad.modelo.Usuario;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Claudio
 */
@Stateless
public class MenuRN {
    @EJB
    private MenuDAO menuDAO;


    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void guardar(Menu m) throws Exception {
        menuDAO.guardar(m);
    }

    public Menu getMenu(String id){
        return menuDAO.getMenu(id);
    }

    public List<Menu> getLista() {

        return menuDAO.getLista();
    }

    public List<Menu> getLista(int maxResults, int firstResult) {

        return menuDAO.getLista(maxResults, firstResult);
    }

    public List<Menu> getListaByNivel(Integer nivel,boolean  soloActivos){

        return menuDAO.getListaByNivel(nivel, soloActivos);
    }

    public Menu getMenuByNombre(String value) {

        return menuDAO.getMenuByNombre(value);
    }

    public void eliminar(Menu m) throws Exception {
        menuDAO.borrar(m);
    }

    public Menu getMenuByUsuario(Menu m, Usuario usuario) {

        return menuDAO.getMenuByUsuario(m,usuario);
    }   

    public List<ItemMenuUsuario> getMenuByUsuario(Usuario usuario) {

        return menuDAO.getMenuByUsuario(usuario);
    }

    public void HabilitarMenu(Usuario usuario, Menu m) throws Exception {

        ItemMenuUsuario us = new ItemMenuUsuario(usuario.getUsuario(), m.getCodigo());

        //Si el menú no se encuentra activado, entonces los habilitamos
        if(menuDAO.getUsuarioMenu(us)==null){
            menuDAO.guardar(us);
        }

        //Habilitamos recursivamente los items hijos del menú a habilitar
        if(!m.getMenuItem().isEmpty()){

            for(Menu mi:m.getMenuItem()){
                HabilitarMenu(usuario, mi);
            }
        }

        //Habilitamos hacia atras, los menues principales
        habilitarMenuPrincipal(usuario, m);
    }

    public void habilitarMenuPrincipal(Usuario usuario,Menu m) throws Exception{

        if(m.getMenuPrincipal()!=null){

            ItemMenuUsuario us = new ItemMenuUsuario(usuario.getUsuario(), m.getMenuPrincipal().getCodigo());

            //Si el menú no se encuentra activado, entonces los habilitamos
            if(menuDAO.getUsuarioMenu(us)==null){
                menuDAO.guardar(us);
            }
            habilitarMenuPrincipal(usuario, m.getMenuPrincipal());
        }
    }

    public void BloquearMenu(Usuario usuario, Menu m) throws Exception {

        ItemMenuUsuario us = new ItemMenuUsuario(usuario.getUsuario(), m.getCodigo());

        //Si el menú no se encuentra activado, entonces los habilitamos
        if(menuDAO.getUsuarioMenu(us)!=null){
            ItemMenuUsuarioPK idPK = new ItemMenuUsuarioPK(us.getIdUsuario(), us.getCodMenu());
            menuDAO.eliminar(ItemMenuUsuario.class,idPK);
        }

        //Habilitamos recursivamente los items hijos del menú a habilitar
        if(!m.getMenuItem().isEmpty()){

            for(Menu mi:m.getMenuItem()){
                BloquearMenu(usuario, mi);
            }
        }
    }

    public List<Menu> getItemsByMenu(Menu m) {

        return menuDAO.getItemsByMenu(m);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public List<Menu> getItemsByTexto(int idUsuario,String txtBusqueda) {
        
        return menuDAO.getItemsByTexto(idUsuario,txtBusqueda);
    }
    
    public String obtenerSiguienteCogido(String origen) {
        
        String codigo = menuDAO.obtenerSiguienteCodigo(origen);
        
        if(codigo==null || codigo.isEmpty()){
            codigo = origen.toUpperCase()+"_00001";
        }
        
        return codigo;
        
    }
 
}
