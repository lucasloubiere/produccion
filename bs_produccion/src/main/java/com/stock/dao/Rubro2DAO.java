/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.dao;

import com.global.dao.BaseDAO;
import com.stock.modelo.Rubro2;
import com.stock.modelo.TipoProducto;
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
public class Rubro2DAO extends BaseDAO {

    public Rubro2 getRubro2(String codigo) {
        return getObjeto(Rubro2.class, codigo);
    }
    
    public TipoProducto getTipoProducto(String codigo) {

        return getObjeto(TipoProducto.class, codigo);
    }
  
    
    public List<Rubro2> getTipoRubro1ByBusqueda(String txtBusqueda, boolean mostrarDeBaja,int cantMax) {
        
        System.err.println("txtBusqueda " + txtBusqueda);
        System.err.println("mostrarDeBaja " + mostrarDeBaja);
        System.err.println("cantMax " + cantMax);                
        
        try {            
             String sQuery = "SELECT e FROM Rubro2 e "
                    + " WHERE (e.codigo LIKE :codigo OR e.descripcion LIKE :descripcion) "
               //     + (codigo==null ? " ": " AND e.tipoProducto.codigo = :tipoProducto ")
                    + (mostrarDeBaja ? " ": " AND e.auditoria.debaja = 'N' ")
                    + " ORDER BY e.tippro, e.codigo";
            
            Query q = em.createQuery(sQuery);            
            q.setParameter("codigo", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
            q.setParameter("descripcion", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
            
            if(cantMax>0){
                q.setMaxResults(cantMax);
            }
          
            return q.getResultList();            
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "getListaByBusqueda", e.getCause());
            return new ArrayList<Rubro2>();
        }  
    }
    
     public Rubro2 getRubro2ByCodigo(String codigo) {
        return getObjeto(Rubro2.class, "codigo", codigo);
    }

    public Rubro2 getRubro2ByDescripcion(String descripcion) {
        return getObjeto(Rubro2.class, "descripcion", descripcion);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
