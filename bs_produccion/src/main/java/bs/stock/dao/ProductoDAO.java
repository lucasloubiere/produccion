/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.dao;

import bs.global.dao.BaseDAO;
import bs.stock.modelo.Producto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author lloubiere
 */
@Stateless
public class ProductoDAO extends BaseDAO implements Serializable{
    
    public Producto getProducto(String id) {
        return getObjeto(Producto.class, id);
    }

    public Producto getProducto(Producto p){

        return getProducto(p.getCodigo());
    }
    
    public List<Producto> getListaByBusqueda(Map<String, String> filtro, String codTipo, String txtBusqueda, boolean mostrarDebaja, int cantMax){
        
        try {            
            String sQuery = "SELECT e FROM Producto e "
                    + " WHERE (e.codigo LIKE :codigo OR e.descripcion LIKE :descripcion) "
                    + (mostrarDebaja ? " ": " AND e.auditoria.debaja = 'N' ")
                    + (codTipo==null || codTipo.isEmpty() ? " ": " AND e.tipoProducto.codigo = :tipoProducto ")
                    + generarStringFiltro(filtro,"e", false)
                    + " ORDER BY e.descripcion ";
            
            Query q = em.createQuery(sQuery);            
            q.setParameter("codigo", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
            q.setParameter("descripcion", "%"+txtBusqueda.replaceAll(" ", "%")+"%");
           
            if(codTipo!=null && !codTipo.isEmpty()){
                q.setParameter("tipoProducto", codTipo);
            }
            
            if(cantMax>0){
                q.setMaxResults(cantMax);
            }    
            
            return q.getResultList();            
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "getProductoByBusqueda", e);
            return new ArrayList<Producto>();
        }        
    }
    
    public int getProximoCodigoProducto(String tippro, String rub01,String rub02){
        
        try {
            String sQuery = "select IFNULL(CAST(RIGHT(MAX(st_producto.CODIGO),4) AS SIGNED),0) +1  "
                    + " from st_producto "
                    + " WHERE "
                    + " st_producto.tipoProducto = '"+tippro+"' "
            + " and st_producto.rubro1 = '"+rub01+"' " ;
//            + " and st_producto.RUBR02 = '"+rub02+"' ";

            Query q = em.createNativeQuery(sQuery);
            
            return ((Long) q.getSingleResult()).intValue();

        } catch (Exception e) {
            e.printStackTrace();
            log.log(Level.SEVERE, "getProximoCodigoProducto", e.getMessage());
            return 0;
        }  
    }

    public int getCantidadRegistros() {
        
        try {            
            String sQuery = "SELECT COUNT(e) FROM Producto e WHERE e.auditoria.debaja = 'N' ";                    
            
            Query q = em.createQuery(sQuery);            
                        
            return ((Long)q.getSingleResult()).intValue();
            
        } catch (Exception e) {
            log.log(Level.SEVERE, "getProductoByBusqueda", e);
            return 0;
        }
        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
