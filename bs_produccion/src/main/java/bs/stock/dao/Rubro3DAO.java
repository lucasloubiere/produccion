/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.dao;

import bs.global.dao.BaseDAO;
import bs.stock.modelo.Rubro03;
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
public class Rubro3DAO extends BaseDAO {

    public Rubro03 getRubro3(String codigo) {
        return getObjeto(Rubro03.class, codigo);
    }
    
    public TipoProducto getTipoProducto(String codigo) {

        return getObjeto(TipoProducto.class, codigo);
    }
    
    public List<Rubro03> getTipoRubro1ByBusqueda(String txtBusqueda, boolean mostrarDeBaja,int cantMax) {
        
        System.err.println("txtBusqueda " + txtBusqueda);
        System.err.println("mostrarDeBaja " + mostrarDeBaja);
        System.err.println("cantMax " + cantMax);                
        
        try {            
             String sQuery = "SELECT e FROM Rubro03 e "
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
            return new ArrayList<Rubro03>();
        }  
    }
    
     public Rubro03 getRubro3ByCodigo(String codigo) {
        return getObjeto(Rubro03.class, "codigo", codigo);
    }

    public Rubro03 getRubro3ByDescripcion(String descripcion) {
        return getObjeto(Rubro03.class, "descripcion", descripcion);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
