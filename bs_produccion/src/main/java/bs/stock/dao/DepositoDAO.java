/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.dao;

import bs.global.dao.BaseDAO;
import bs.stock.modelo.Deposito;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author lloubiere
 */
@Stateless
public class DepositoDAO extends BaseDAO {


    public List<Deposito> getLista() {
        return getLista(Deposito.class, true, -1, -1);
    }

    public List<Deposito> getLista(int maxResults, int firstResult) {
        return getLista(Deposito.class, false, maxResults, firstResult);
    }
    
    
    public Deposito getDeposito(String codigo) {
        return getObjeto(Deposito.class, codigo);
    }

    public List<Deposito> getTipoProductoByBusqueda(String txtBusqueda, boolean mostrarDeBaja, int cantMax) {

        System.err.println("txtBusqueda " + txtBusqueda);
        System.err.println("mostrarDeBaja " + mostrarDeBaja);
        System.err.println("cantMax " + cantMax);

        try {
            String sQuery = "SELECT e FROM Deposito e "
                    + " WHERE (e.codigo LIKE :codigo OR e.descripcion LIKE :descripcion) "
                    + (mostrarDeBaja ? " " : " AND e.auditoria.debaja = 'N' ")
                    + " ORDER BY e.codigo";

            Query q = em.createQuery(sQuery);
            q.setParameter("codigo", "%" + txtBusqueda.replaceAll(" ", "%") + "%");
            q.setParameter("descripcion", "%" + txtBusqueda.replaceAll(" ", "%") + "%");

            if (cantMax > 0) {
                q.setMaxResults(cantMax);
            }

            return q.getResultList();

        } catch (Exception e) {
            log.log(Level.SEVERE, "getListaByBusqueda", e.getCause());
            return new ArrayList<Deposito>();
        }
    }

    public Deposito getDepositoByCodigo(String codigo) {
        return getObjeto(Deposito.class, "codigo", codigo);
    }

    public Deposito getDepositoByDescripcion(String descripcion) {
        return getObjeto(Deposito.class, "descripcion", descripcion);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public List<Deposito> getLista(boolean mostrarDebaja) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  
   public List<Deposito> getDepositoByBusqueda(String txtBusqueda, boolean mostrarDeBaja,int cantMax) {
        try {
            
            String sQuery = "SELECT e FROM Deposito e "
                    + " WHERE (e.codigo LIKE :codigo OR e.descripcion LIKE :descripcion) "
                    + (mostrarDeBaja ? " ": " AND e.auditoria.debaja = 'N' ")
                    + " ORDER BY e.codigo";
            
            Query q = em.createQuery(sQuery);            
            q.setParameter("codigo", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
            q.setParameter("descripcion", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
            
            if(cantMax>0){
                q.setMaxResults(cantMax);
            }
          
            return q.getResultList();            
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "getDepositoByBusqueda", e.getCause());
            return new ArrayList<Deposito>();
        }  
   }
}
 
    