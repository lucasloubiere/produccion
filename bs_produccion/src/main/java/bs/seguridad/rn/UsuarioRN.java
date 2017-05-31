/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.seguridad.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.seguridad.dao.UsuarioDAO;
import bs.seguridad.modelo.Usuario;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ide
 */
@Stateless
public class UsuarioRN {

    @EJB  private UsuarioDAO usuarioDAO;
    
    public void guardar(Usuario usuario, boolean esNuevo) throws ExcepcionGeneralSistema {
        
        if(esNuevo){     
            if(usuarioDAO.getObjeto(Usuario.class, usuario.getUsuario())!=null){
                throw new ExcepcionGeneralSistema("El usuario "+usuario.getUsuario()+" ya existe");
            }            
            usuarioDAO.crear(usuario);            
        }else{
            usuarioDAO.editar(usuario);
        }        
    }    
    
    
    
    public Usuario getUsuarioByNombre(String nombre){
        
        return usuarioDAO.getUsuarioByNombre(nombre);
    }
    public List<Usuario> getLista(String txtBusqueda, boolean mostrarDeBaja, int cantidad){
                
        return usuarioDAO.getUsuarioByBusqueda(txtBusqueda, mostrarDeBaja, cantidad);
    }
            
            

// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void eliminar(Usuario usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
