/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguridad.dao;

import com.global.dao.BaseDAO;
import com.seguridad.modelo.EstadoUsuario;
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
public class EstadoUsuarioDAO extends BaseDAO{
    
    public List<EstadoUsuario> getListaByBusqueda(String txtBusqueda, boolean mostrarDeBaja,int cantMax) {
        try {
            
            String sQuery = "SELECT e FROM EstadoUsuario e "
                    + " WHERE (e.descripcion LIKE :descripcion) "
                    + (mostrarDeBaja ? " ": " AND e.auditoria.debaja = 'N' ")
                    + " ORDER BY e.descripcion";
            
            Query q = em.createQuery(sQuery);            
            q.setParameter("descripcion", "%"+txtBusqueda.replaceAll(" ", "%")+"%");            
            
            if(cantMax>0){
                q.setMaxResults(cantMax);
            }
          
            return q.getResultList();            
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "getUsuarioByBusqueda", e.getCause());
            return new ArrayList<EstadoUsuario>();
        }  
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
