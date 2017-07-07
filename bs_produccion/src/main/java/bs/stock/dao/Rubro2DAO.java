/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.dao;

import bs.global.dao.BaseDAO;
import bs.stock.modelo.Rubro02;
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
public class Rubro2DAO extends BaseDAO {

    public Rubro02 getRubro2(String codigo) {
        return getObjeto(Rubro02.class, codigo);
    }
    
    public TipoProducto getTipoProducto(String codigo) {

        return getObjeto(TipoProducto.class, codigo);
    }
  
    
    public List<Rubro02> getListaByBusqueda(String codTipo, String txtBusqueda, boolean mostrarDeBaja,int cantMax) {
               
        
        try {            
             String sQuery = "SELECT e FROM Rubro2 e "
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
          
            return q.getResultList();            
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "getListaByBusqueda", e);
            return new ArrayList<Rubro02>();
        }  
    }
    
     public Rubro02 getRubro2ByCodigo(String codigo) {
        return getObjeto(Rubro02.class, "codigo", codigo);
    }

    public Rubro02 getRubro2ByDescripcion(String descripcion) {
        return getObjeto(Rubro02.class, "descripcion", descripcion);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
