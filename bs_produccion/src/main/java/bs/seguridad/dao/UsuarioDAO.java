/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.seguridad.dao;
import bs.global.dao.BaseDAO;
import bs.seguridad.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.Query;


/**
 *
 * @author lucas
 */
@Stateless
public class UsuarioDAO extends BaseDAO{
    
    public Usuario getUsuario(int id) {
        return getObjeto(Usuario.class, id);
    }

    public void eliminar(Usuario u) throws Exception{
        
        super.eliminar(Usuario.class,u.getId());            
    }

    public List<Usuario> getLista() {
        return getLista(Usuario.class, true, -1, -1);
    }

    public List<Usuario> getLista(int maxResults, int firstResult) {
        return getLista(Usuario.class, false, maxResults, firstResult);
    }   
    
    public Usuario getUsuarioByEmail(String email) {
        return getObjeto(Usuario.class,"email", email);
    }
 
    public Usuario getUsuarioByNombre(String usuario) {
        return getObjeto(Usuario.class,"usuario", usuario);
    }

    public void refreshUsuario(Usuario u) {
        
        em.refresh(u);
    }
    
    public List<Usuario> getUsuarioByBusqueda(String txtBusqueda, boolean mostrarDeBaja,int cantMax) {
        try {
            
            String sQuery = "SELECT e FROM Usuario e "
                    + " WHERE (e.usuario LIKE :usuario OR e.nombre LIKE :nombre OR e.email LIKE :email) "                    
                    + " ORDER BY e.usuario";
            
            Query q = em.createQuery(sQuery);            
            q.setParameter("usuario", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
            q.setParameter("nombre", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
            q.setParameter("email", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
            
            if(cantMax>0){
                q.setMaxResults(cantMax);
            }
          
            return q.getResultList();            
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "getUsuarioByBusqueda", e.getCause());
            return new ArrayList<Usuario>();
        }  
    }

}
