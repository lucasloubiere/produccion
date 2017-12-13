/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.produccion.dao;


import bs.global.dao.BaseDAO;
import bs.global.modelo.ComprobantePK;
import bs.produccion.modelo.ComprobanteProduccion;
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
public class ComprobanteProduccionDAO extends BaseDAO {

    public ComprobanteProduccion getComprobante(ComprobantePK idPK){

        return em.find(ComprobanteProduccion.class, idPK);
    }

    public void eliminar(ComprobanteProduccion o) throws Exception{

        super.eliminar(ComprobanteProduccion.class, o.getCodigo());
    }

    public List<ComprobanteProduccion> getLista() {
        return getLista(ComprobanteProduccion.class, true , -1, -1);
    }

    public List<ComprobanteProduccion> getLista(Map<String, String> filtro) {

        String sQuery = "SELECT i FROM ComprobanteProduccion i ";
              sQuery += generarStringFiltro(filtro,"i", true);

        return queryList(ComprobanteProduccion.class, sQuery);
    }

    public List<ComprobanteProduccion> getListaByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja, int cantMax){
        
        try {
            
            String sQuery = "SELECT e FROM ComprobanteProduccion e "
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
            return new ArrayList<ComprobanteProduccion>();
        }        
    }
 
}
