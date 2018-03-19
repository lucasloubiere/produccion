/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.global.dao;

import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author ctrosch
 */
@Stateless
public class TemporalDAO extends BaseDAO {

    public void vaciarTabla() {
        
        try {
            
            String sQuery = "DELETE FROM Temporal";
            
            Query q = em.createQuery(sQuery);            
                        
            int u = q.executeUpdate();
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "vaciarTabla", e);            
        }       
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")


}
