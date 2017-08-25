/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.sincronizacion.dao;

import bs.global.dao.BaseDAO;
import bs.sincronizacion.modelo.LogSincronizacion;
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
public class LogSincronizacionDAO extends BaseDAO{
    
    public LogSincronizacion getLogSincronizacion(String codigo) {
        return getObjeto(LogSincronizacion.class, codigo);
    }

   public List<LogSincronizacion> getListaByBusqueda(String txtBusqueda, boolean mostrarDeBaja,int cantMax) {
        try {
            
            String sQuery = "SELECT e FROM LogSincronizacion e "
                    + " WHERE 1=1 "
                    + (mostrarDeBaja ? " ": " AND e.auditoria.debaja = 'N' ")
                    + " ORDER BY e.codigo";
            
            Query q = em.createQuery(sQuery);                        
            
            if(cantMax>0){
                q.setMaxResults(cantMax);
            }
          
            return q.getResultList();            
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "getListaByBusqueda", e.getCause());
            return new ArrayList<LogSincronizacion>();
        }  
    }
}
