/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.dao;

import bs.global.dao.BaseDAO;
import bs.produccion.modelo.ItemAplicacionProduccion;
import bs.produccion.modelo.ItemProductoProduccion;
import bs.produccion.modelo.MovimientoProduccion;
import bs.produccion.vista.PendienteProduccionDetalle;
import bs.produccion.vista.PendienteProduccionGrupo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;


/**
 *
 * @author Claudio
 */

@Stateless
public class ProduccionDAO extends BaseDAO {
    
    public MovimientoProduccion editar(MovimientoProduccion m){
        return (MovimientoProduccion) super.editar(m);
    }
     
    public MovimientoProduccion getMovimiento(Integer id) {
        return getObjeto(MovimientoProduccion.class, id);
    }
        
    public List<MovimientoProduccion> getListaByBusqueda(Map<String,String> filtro,boolean soloPendientes, int cantMax) {
        
        try {
            
            String sQuery = "SELECT m FROM MovimientoProduccion m ";
                   if(soloPendientes){
                       sQuery += "WHERE EXISTS(SELECT p FROM PendienteProduccionDetalle p WHERE p.codfor = m.formulario.codigo AND p.nrofor = m.numeroFormulario) ";
                   }
                   sQuery += generarStringFiltro(filtro,"m", !soloPendientes);
                   sQuery +=" ORDER BY m.fechaMovimiento DESC, m.numeroFormulario DESC";

            Query q = em.createQuery(sQuery); 
            
            if(cantMax>0){
                q.setMaxResults(cantMax);
            } 
            
            return q.getResultList();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al obtener movimientos de producción");
            return new ArrayList<MovimientoProduccion>();
        }
    }
       
    public MovimientoProduccion getMovimientoProduccionById(Integer id){
        try {
            return em.find(MovimientoProduccion.class, id);
        } catch (Exception e) {
            System.out.println("No se encontró movimiento: "+ id);
            return null;
        }
    }

    public MovimientoProduccion getMovimiento(String codFormulario, Integer numeroFormulario) {
        
        try {
            String sQuery = "SELECT m FROM MovimientoProduccion m "
                    + "WHERE m.formulario.codigo = :codFormulario "
                    + "AND m.numeroFormulario = :numeroFormulario";
          
            Query q = em.createQuery(sQuery);            
            
            q.setParameter("codFormulario", codFormulario);
            q.setParameter("numeroFormulario", numeroFormulario);
            
            q.setMaxResults(1);
            return (MovimientoProduccion) q.getSingleResult();
        }catch(NoResultException nre){
            return null;
        
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al obtener movimientos de producción");
            return null;
        }
        
    }

    public PendienteProduccionGrupo getPendienteGrupo(Map<String, String> filtroGrupo) {
        
        String sQuery = "SELECT p FROM PendienteProduccionGrupo p ";
               sQuery += generarStringFiltro(filtroGrupo,"p", true);

        sincronizacionTemporal("pd_pendiente_grupo");
        return queryObject(PendienteProduccionGrupo.class, sQuery);
        
    }
    
    public List<PendienteProduccionGrupo> getPendientesGrupo(Map<String, String> filtroGrupo) {
        
        String sQuery = "SELECT p FROM PendienteProduccionGrupo p ";
               sQuery += generarStringFiltro(filtroGrupo,"p", true);

        sincronizacionTemporal("pd_pendiente_grupo");
        return queryList(PendienteProduccionGrupo.class, sQuery);
        
    }

    public List<PendienteProduccionDetalle> getPendientesDetalle(Map<String, String> filtroDetalle){
        
        try {
                    
            String sQuery = "SELECT i FROM PendienteProduccionDetalle i ";               
            sQuery += generarStringFiltro(filtroDetalle,"i", true);
            
            sincronizacionTemporal("pd_pendiente_detalle");
            Query q = em.createQuery(sQuery);            
            
            q.setMaxResults(300);
            
            return q.getResultList();
        } catch (NoResultException e) {            
            return new ArrayList<PendienteProduccionDetalle>();
        } catch (Exception e) {            
            System.err.println("Error al obtener pendientes de compras detalle " + e);
            return new ArrayList<PendienteProduccionDetalle>();
        }
        
    }
    
    public PendienteProduccionDetalle getPendienteDetalle(Map<String, String> filtroDetalle){
        
        try {
                    
            String sQuery = "SELECT i FROM PendienteProduccionDetalle i ";               
            sQuery += generarStringFiltro(filtroDetalle,"i", true);
            
            sincronizacionTemporal("pd_pendiente_detalle");
            Query q = em.createQuery(sQuery);            
            
            q.setMaxResults(1);
            
            return (PendienteProduccionDetalle) q.getSingleResult();
        } catch (NoResultException e) {                        
            return null;
        } catch (Exception e) {            
            System.err.println("Error al obtener pendientes de compras detalle " + e);
            return null;
        }
        
    }

    public ItemAplicacionProduccion getItemAplicacion(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
    public List<ItemAplicacionProduccion> getAplicacionesByItem(Integer idItem){
        
        try {
            String sQuery = "SELECT i FROM ItemAplicacionProduccion i "
                    + "WHERE i.idItemAplicacion = :idItem "
                    + "AND i.id <> i.idItemAplicacion "
                    + "AND i.auditoria.debaja = 'N' "
                    + "ORDER BY i.producto.codigo ";
          
            Query q = em.createQuery(sQuery);            
            q.setParameter("idItem", idItem);
            
            return q.getResultList();
            
            
        } catch (Exception e) {            
            System.err.println("Error al obtener ItemAplicacionProduccion");
            return null;
        }
    }

    public ItemProductoProduccion getItemProductoByItemAplicacion(Integer idMovimiento, Integer nroItem, String artcod) {
        
        try {
            String sQuery = "SELECT i FROM ItemProductoProduccion i "
                    + "WHERE i.movimiento.id = :idMovimiento "
                    + "AND i.nroitm =:nroItem "
                    + "AND i.producto.codigo =:artcod "                    
                    + "AND i.id = i.idItemAplicacion "
                    + "AND i.auditoria.debaja = 'N' "
                    + "ORDER BY i.producto.codigo ";
          
            Query q = em.createQuery(sQuery);            
            q.setParameter("idMovimiento", idMovimiento);
            q.setParameter("nroItem", nroItem);
            q.setParameter("artcod", artcod);
            
            q.setMaxResults(1);
            
            return (ItemProductoProduccion) q.getSingleResult();
            
        }catch(NoResultException nre) {
            return null; 
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al obtener ItemAplicacionProduccion de compra");
            return null;
        }       
        
    }
}
