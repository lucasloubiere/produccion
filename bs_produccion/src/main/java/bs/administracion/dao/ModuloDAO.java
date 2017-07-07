/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.administracion.dao;

import bs.administracion.modelo.Modulo;
import bs.global.dao.BaseDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Agustin
 */
@Stateless
public class ModuloDAO extends BaseDAO {
    
    public List<Modulo> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja, int cantMax) {
            
        try {
            String sQuery = "SELECT e FROM Modulo e "
                    + "WHERE 1=1 "
                    + " AND ((e.codigo LIKE :codigo) "
                    + "  OR  (e.descripcion LIKE :descripcion) "                                      
                    + "     ) "
                    + (mostrarDebaja ? " ": " AND e.auditoria.debaja = 'N' ")
                    + "ORDER BY e.codigo ";

            Query q = em.createQuery(sQuery);
            
            q.setParameter("codigo", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
            q.setParameter("descripcion", "%"+txtBusqueda.replaceAll(" ", "%")+"%");            
                        
            if(cantMax>0){
                q.setMaxResults(cantMax);
            }
            
            return q.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            log.log(Level.SEVERE, "getListaByBusqueda", e.getMessage());
            return new ArrayList<Modulo>();
        }        
    
    }
}
