/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.dao;

import com.global.dao.BaseDAO;
import com.stock.modelo.UnidadMedida;
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
public class UnidadMedidaDAO extends BaseDAO{
    
    public UnidadMedida getUnidadMedida(String codigo) {
        return getObjeto(UnidadMedida.class, codigo);
    }
public List<UnidadMedida> getTipoProductoByBusqueda(String txtBusqueda, boolean mostrarDeBaja,int cantMax) {
        
        System.err.println("txtBusqueda " + txtBusqueda);
        System.err.println("mostrarDeBaja " + mostrarDeBaja);
        System.err.println("cantMax " + cantMax);                
        
        try {            
            String sQuery = "SELECT e FROM UnidadMedida e "
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
            log.log(Level.SEVERE, "getListaByBusqueda", e.getCause());
            return new ArrayList<UnidadMedida>();
        }  
    }
    
     public UnidadMedida getUnidadMedidaByCodigo(String codigo) {
        return getObjeto(UnidadMedida.class, "codigo", codigo);
    }

    public UnidadMedida getUnidadMedidaByDescripcion(String descripcion) {
        return getObjeto(UnidadMedida.class, "descripcion", descripcion);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

   public List<UnidadMedida> getListaByBusqueda(String txtBusqueda, boolean mostrarDeBaja,int cantMax) {
        try {
            
            String sQuery = "SELECT e FROM UnidadMedida e "
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
            log.log(Level.SEVERE, "getListaByBusqueda", e.getCause());
            return new ArrayList<UnidadMedida>();
        }  
    }
}
