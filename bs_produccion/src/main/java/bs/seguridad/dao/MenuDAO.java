/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.seguridad.dao;

import bs.global.dao.BaseDAO;
import bs.seguridad.modelo.ItemMenuUsuario;
import bs.seguridad.modelo.ItemMenuUsuarioPK;
import bs.seguridad.modelo.Menu;
import bs.seguridad.modelo.Usuario;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Claudio
 */
@Stateless
public class MenuDAO extends BaseDAO {

    
    public void guardar(Menu m) throws Exception {
        if(getMenu(m.getCodigo())==null){
            crear(m);
        }else{
            editar(m);
        }
    }

    public void borrar(Menu m) throws Exception {

        eliminar(getObjeto(Menu.class, m.getCodigo()));

    }
    
    public void guardar(ItemMenuUsuario us) throws Exception  {

        crear(us);
    }

    public Menu getMenu(String id){
        return getObjeto(Menu.class, id);
    }

    public List<Menu> getLista() {
        return getLista(Menu.class, true, -1, -1);
    }

    public List<Menu> getLista(int maxResults, int firstResult) {
        return getLista(Menu.class, false, maxResults, firstResult);
    }

    public List<Menu> getListaByNivel(Integer nivel, boolean  activos){
        
        String sQuery = "SELECT m FROM Menu m "
                +" WHERE m.nivel = "+nivel 
                + (activos ? " AND m.activo = 'S'" : "" )
                +" ORDER BY m.orden";
        
        return queryList(Menu.class,sQuery);
    }

    public Menu getMenuByNombre(String value) {
        return getObjeto(Menu.class,"nombre",value);

    }

    public Menu getMenuByUsuario(Menu m, Usuario usuario) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<ItemMenuUsuario> getMenuByUsuario(Usuario usuario) {

        try {
            String sQuery = "SELECT m FROM ItemMenuUsuario m "
                    + " WHERE m.idUsuario = :idUsuario "
                    + " AND m.menu.activo = :activo "
                    + " ORDER BY m.menu.orden ";
            
            Query q = (Query) em.createQuery(sQuery);

            q.setParameter("idUsuario", usuario.getUsuario());
            q.setParameter("activo", "S");

            return q.getResultList();
        } catch (Exception e) {
            System.out.println("No se puede obtener lista de menu por usuario " + e.getMessage());
            return null;
        }        
    }

    public ItemMenuUsuario getUsuarioMenu(ItemMenuUsuario us) {

        ItemMenuUsuarioPK idPK = new ItemMenuUsuarioPK(us.getIdUsuario(), us.getCodMenu());

        return em.find(ItemMenuUsuario.class, idPK);


    }

    public List<Menu> getItemsByMenu(Menu m) {

        try {
            String sQuery = "SELECT m FROM Menu m "
                    + " WHERE m.menuPrincipal.codigo = :codPpal"
                    + " ORDER BY m.orden ";
            Query q = (Query) em.createQuery(sQuery);
            q.setParameter("codPpal", m.getCodigo());
            return q.getResultList();

        } catch (Exception e) {
            System.out.println("No se puede obtener lista de items por menú " + e.getMessage());
            return null;
        }        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public List<Menu> getItemsByTexto(int idUsuario,String txtBusqueda) {
        try {
            
            String sQuery = "SELECT DISTINCT m FROM Menu m "
                    + " WHERE"
                    + "     (m.nombre LIKE '%"+txtBusqueda.replaceAll(" ", "%")+"%') "
                    + " AND (m.url <> '' OR m.url IS NOT NULL) "
                    + " AND (m.nivel > 1 )"
                    + " AND (EXISTS(SELECT i "
                    + "            FROM ItemMenuUsuario i "
                    + "            WHERE i.codMenu = m.codigo "
                    + "            AND i.idUsuario = "+idUsuario+" ))"
                    + " ORDER BY m.modulo.codigo, m.nombre ";
            Query q = (Query) em.createQuery(sQuery);
            
            return q.getResultList();

        } catch (Exception e) {
            System.out.println("No se puede obtener lista de items " + e.getMessage());
            return null;
        }     
    }
    
    public String obtenerSiguienteCodigo(String origen) {
        
        try {
            String sQuery = " SELECT CONCAT(ORIGEN,'_',RIGHT(CONCAT('00000',IFNULL(max(RIGHT(codigo,5)) + 1,1)),5)) from sg_menu where sg_menu.ORIGEN = '"+origen.toUpperCase()+"' ";

            Query q = em.createNativeQuery(sQuery);
            
            return ((String) q.getSingleResult());

        } catch (Exception e) {
            e.printStackTrace();
            log.log(Level.SEVERE, "obtenerSiguienteCodigo", e.getMessage());
            return origen.toUpperCase()+"_000000";
        }  
    }
 
}
