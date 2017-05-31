/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.stock.dao;

import bs.global.dao.BaseDAO;
import bs.global.modelo.ComprobantePK;
import bs.stock.modelo.ComprobanteStock;
import bs.stock.modelo.MovimientoStock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Claudio
 */
@Stateless
public class ComprobanteStockDAO extends BaseDAO {

    public ComprobanteStock getComprobante(String MODCJ, String CODCJ) {
        ComprobantePK idPk = new ComprobantePK(MODCJ, CODCJ);
        return getComprobante(idPk);
    }
 
    
    public ComprobanteStock getComprobante(ComprobantePK idPK){

        return em.find(ComprobanteStock.class, idPK);
    }

    public void eliminar(ComprobanteStock o) throws Exception{

        super.eliminar(ComprobanteStock.class, o.getCodigo());
    }

    public List<ComprobanteStock> getLista() {
        return getLista(ComprobanteStock.class, true , -1, -1);
    }

    public List<ComprobanteStock> getLista(Map<String, String> filtro) {

        String sQuery = "SELECT i FROM ComprobanteStock i ";
              sQuery += generarStringFiltro(filtro,"i", true);

        return queryList(ComprobanteStock.class, sQuery);
    }

    
    public List<ComprobanteStock> getListaByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja, int cantMax){
        
        try {            
            String sQuery = "SELECT e FROM ComprobanteStock e "
                    + " WHERE (e.codigo LIKE :codigo OR e.descripcion LIKE :descripcion) "
                    + (mostrarDebaja ? " ": " AND e.auditoria.debaja = 'N' ")                    
                    + generarStringFiltro(filtro,"e", false)
                    + " ORDER BY e.codigo ";
            
            Query q = em.createQuery(sQuery);            
            q.setParameter("codigo", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
            q.setParameter("descripcion", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
           
            if(cantMax>0){
                q.setMaxResults(cantMax);
            }    
                        
            return q.getResultList();            
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "getListaByBusqueda", e);
            return new ArrayList<ComprobanteStock>();
        }        
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public MovimientoStock getMovimiento(String codFormulario, Integer numeroFormulario) {
        try {
            String sQuery = "SELECT m FROM MovimientoStock m "
                    + "WHERE m.formulario.codigo = :codFormulario "
                    + "AND m.numeroFormulario = :numeroFormulario";
          
            Query q = em.createQuery(sQuery);            
            
            q.setParameter("codFormulario", codFormulario);
            q.setParameter("numeroFormulario", numeroFormulario);
            
            q.setMaxResults(1);
            return (MovimientoStock) q.getSingleResult();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al obtener movimientos de stock");
            return null;
        }
    }
 
}
