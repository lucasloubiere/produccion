/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.dao;

import bs.global.dao.BaseDAO;
import bs.stock.modelo.Rubro01;
import bs.stock.modelo.TipoProducto;
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
public class Rubro1DAO extends BaseDAO {

    public Rubro01 getRubro1(String codigo) {
        return getObjeto(Rubro01.class, codigo);
    }
    
    public TipoProducto getTipoProducto(String codigo) {

        return getObjeto(TipoProducto.class, codigo);
    }
    
    public List<Rubro01> getListaByBusqueda(String codTipo, String txtBusqueda, boolean mostrarDeBaja,int cantMax) {
                       
        
        try {            
             String sQuery = "SELECT e FROM Rubro01 e "
                    + " WHERE (e.codigo LIKE :codigo OR e.descripcion LIKE :descripcion) "
                    + (codTipo==null ? " ": " AND e.tipoProducto = :tipoProducto ")
                    + (mostrarDeBaja ? " ": " AND e.auditoria.debaja = 'N' ")
                    + " ORDER BY e.tipoProducto, e.codigo";
            
             Query q = em.createQuery(sQuery);            
            q.setParameter("codigo", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
            q.setParameter("descripcion", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
            
            if(codTipo!=null){
                q.setParameter("tipoProducto", codTipo);
            }
            
            if(cantMax>0){
                q.setMaxResults(cantMax);
            }
            
           
          
            return q.getResultList();            
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "getListaByBusqueda", e);
            return new ArrayList<Rubro01>();
        }  
    }
    
     public Rubro01 getRubro1ByCodigo(String codigo) {
        return getObjeto(Rubro01.class, "codigo", codigo);
    }

    public Rubro01 getRubro1ByDescripcion(String descripcion) {
        return getObjeto(Rubro01.class, "descripcion", descripcion);
    }

    
}
