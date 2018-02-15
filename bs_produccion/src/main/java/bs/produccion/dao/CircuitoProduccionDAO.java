/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.dao;


import bs.global.dao.BaseDAO;
import bs.produccion.modelo.CircuitoProduccion;
import bs.produccion.modelo.CircuitoProduccionPK;
import bs.produccion.modelo.ComprobanteProduccion;
import bs.stock.modelo.ComprobanteStock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.Query;


/**
 *
 * @author ctrosch
 */

@Stateless
public class CircuitoProduccionDAO extends BaseDAO {
    
    public CircuitoProduccion getCircuito(CircuitoProduccionPK idPK){
        return em.find(CircuitoProduccion.class, idPK);
    }

    public ComprobanteProduccion getComprobanteProduccion(String circom, String cirapl, String modcom, String codcom) {

        try{
            
            String sQuery = "SELECT i.comprobante FROM ItemCircuitoProduccionProduccion i "
                    + "WHERE i.cirapl = '"+cirapl+"' "
                    + "AND i.circom = '"+circom+"' "
                    + "AND i.modulo = '"+modcom+"' "
                    + "AND i.codcom = '"+codcom+"' ";
            return queryObject(ComprobanteProduccion.class, sQuery);

        }catch(Exception e){
            return null;
        }
       
    }

    public ComprobanteStock getComprobanteStock(String circom, String cirapl, String modcom, String codcom) {
        try{

            String sQuery = "SELECT i.comprobante FROM ItemCircuitoProduccionStock i "
                    + "WHERE i.cirapl = '"+cirapl+"' "
                    + "AND i.circom = '"+circom+"' "
                    + "AND i.modulo = '"+modcom+"' "
                    + "AND i.codcom = '"+codcom+"' ";
            return queryObject(ComprobanteStock.class, sQuery);

        }catch(Exception e){
            System.err.println("Error getComprobanteStock " + e );
            return null;
        }
    }
    
    public List<CircuitoProduccion> getListaByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja, int cantMax){
        
        try {
            
            String sQuery = "SELECT e FROM CircuitoProduccion e "
                    + " WHERE (e.circom LIKE :circom "
                    + "  OR e.cirapl LIKE :cirapl"
                    + "  OR e.descripcion LIKE :descripcion"
                    + "       ) "
                    + (mostrarDebaja ? " ": " AND e.auditoria.debaja = 'N' ")
                    + generarStringFiltro(filtro,"e", false)
                    + " ORDER BY e.circom, e.cirapl ";
            
            Query q = em.createQuery(sQuery);            
            q.setParameter("circom"     ,"%"+txtBusqueda.replaceAll(" ", "%")+"%");
            q.setParameter("cirapl"     ,"%"+txtBusqueda.replaceAll(" ", "%")+"%");
            q.setParameter("descripcion","%"+txtBusqueda.replaceAll(" ", "%")+"%");
            
            if(cantMax>0){
                q.setMaxResults(cantMax);
            }    
                        
            return q.getResultList();            
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "getListaByBusqueda", e);
            return new ArrayList<CircuitoProduccion>();
        }        
    }

}
