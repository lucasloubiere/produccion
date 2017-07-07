/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.seguridad.rn;

import bs.seguridad.dao.UsuarioDAO;
import bs.seguridad.modelo.Usuario;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author ctrosch
 */
@Stateless
public class UsuarioRN {

    @EJB UsuarioDAO usuarioDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)     
    public void guardar(Usuario u) throws Exception{

        if(u.getId()==null){
            usuarioDAO.crear(u);
        }else{
            usuarioDAO.editar(u);
        }
    }
    
    public Usuario getUsuario(int id){

        return usuarioDAO.getUsuario(id);
    }

    public void refreshUsuario(Usuario u){

        usuarioDAO.refreshUsuario(u);
    }

    public List<Usuario> getLista(){

        return usuarioDAO.getLista();
    }

    public void eliminar(Usuario usuario) throws Exception {

        usuarioDAO.eliminar(usuario);
    }
    
    
    public List<Usuario> getUsuarioByBusqueda(String txtBusqueda, boolean mostrarDeBaja) {
        
        return usuarioDAO.getUsuarioByBusqueda(txtBusqueda,mostrarDeBaja,15);
    }


    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public Usuario getUsuarioByNombre(String usuario) {
        
        return usuarioDAO.getUsuarioByNombre(usuario);
        
    }
 
}
