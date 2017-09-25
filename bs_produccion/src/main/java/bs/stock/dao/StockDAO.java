/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.dao;


import bs.global.dao.BaseDAO;
import bs.stock.modelo.Producto;
import bs.stock.modelo.Stock;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

@Stateless
public class StockDAO extends BaseDAO {

    @EJB
    ProductoDAO productoDAO;
    @EJB
    DepositoDAO depositoDAO;

    public BigDecimal getStockDisponibleByProducto(Stock s){

        try {          

            Query q = (Query) em.createQuery("SELECT SUM(s.stocks) "
                    + "FROM Stock s "
                    + "WHERE s.artcod = :artcod "
                    + "AND s.deposi = :deposi "
                    + "AND s.atributo1 = :atributo1 "
                    + "AND s.atributo2 = :atributo2 "
                    + "AND s.atributo3 = :atributo3 "
                    + "AND s.atributo4 = :atributo4 "
                    + "AND s.atributo5 = :atributo5 "
                    + "AND s.atributo6 = :atributo6 "
                    + "AND s.atributo7 = :atributo7 "
                    + " " );

            q.setParameter("artcod",s.getCodigo());
            q.setParameter("deposi",s.getDeposi());            
            q.setParameter("atributo1",s.getAtributo1());
            q.setParameter("atributo2",s.getAtributo2());
            q.setParameter("atributo3",s.getAtributo3());
            q.setParameter("atributo4",s.getAtributo4());
            q.setParameter("atributo5",s.getAtributo5());
            q.setParameter("atributo6",s.getAtributo6());
            q.setParameter("atributo7",s.getAtributo7());

            return (BigDecimal) q.getSingleResult();
            
        } catch (NoResultException e) {
            
            return BigDecimal.ZERO;
            
        } catch (Exception e) {
            System.out.println("No se puede obtener disponible en stock" + e);
            return null;
        }
    }
    
    //Por el momento solo utlizamos la entidad stock, es lo mismo 
    //que esta consulta
//    public List<StockProductoDeposito> verStockPorDeposito(String codDep) {
//        try {
//
//            String sQuery = "Select" +
//                    "  st_stock.ARTCOD,st_stock.DEPOSI,  st_stock.NSERIE,  st_stock.NDESPA,  st_stock.NESTAN,  st_stock.NATRIB," +
//                    "  st_stock.NFECHA,  st_stock.NUBICA,  st_stock.ENVASE,  st_stock.NOTROS,  Sum(st_stock.STOCKS) As Sum_STOCKS," +
//                    "  st_producto.DESCRP,  st_deposito.DESCRP As DESCRP1" +
//                    "  From  st_stock Inner Join  st_producto On st_stock.ARTCOD = st_producto.CODIGO Inner Join" +
//                    "  st_deposito On st_deposito.CODIGO = st_stock.DEPOSI "
//                    + " WHERE st_stock.DEPOSI =:?1 " +
//                    "Group By" +
//                    "  st_stock.ARTCOD, st_stock.DEPOSI, st_stock.NSERIE, st_stock.NDESPA," +
//                    "  st_stock.NESTAN, st_stock.NATRIB, st_stock.NFECHA, st_stock.NUBICA," +
//                    "  st_stock.ENVASE, st_stock.NOTROS, st_producto.DESCRP, st_deposito.DESCRP";
//
//            Query q = em.createNativeQuery(sQuery, StockProductoDeposito.class);
//
//            q.setParameter(1, codDep);
//            
//            return q.getResultList();
//        } catch (Exception e) {
//            System.out.println("No se puede obtener stock por producto " + e.getMessage());
//            return new ArrayList<StockProductoDeposito>();
//        }
//    }

        //Por el momento solo utlizamos la entidad stock, es lo mismo 
//    //que esta consulta
//    public List<StockProductoDeposito> verStockPorProducto(String artCod) {
//        try {
//
//            String sQuery = "Select" +
//                    "  st_stock.ARTCOD,st_stock.DEPOSI,  st_stock.NSERIE,  st_stock.NDESPA,  st_stock.NESTAN,  st_stock.NATRIB," +
//                    "  st_stock.NFECHA,  st_stock.NUBICA,  st_stock.ENVASE,  st_stock.NOTROS,  Sum(st_stock.STOCKS) As Sum_STOCKS," +
//                    "  st_producto.DESCRP,  st_deposito.DESCRP As DESCRP1" +
//                    "  From  st_stock Inner Join  st_producto On st_stock.ARTCOD = st_producto.CODIGO Inner Join" +
//                    "  st_deposito On st_deposito.CODIGO = st_stock.DEPOSI "
//                    + " WHERE st_stock.ARTCOD =:?1 " +
//                    "Group By" +
//                    "  st_stock.ARTCOD, st_stock.DEPOSI, st_stock.NSERIE, st_stock.NDESPA," +
//                    "  st_stock.NESTAN, st_stock.NATRIB, st_stock.NFECHA, st_stock.NUBICA," +
//                    "  st_stock.ENVASE, st_stock.NOTROS, st_producto.DESCRP, st_deposito.DESCRP";
//
//            Query q = em.createNativeQuery(sQuery, StockProductoDeposito.class);
//
//            q.setParameter(1, artCod);
//            
//            return q.getResultList();
//        } catch (Exception e) {
//            System.out.println("No se puede obtener stock por producto " + e.getMessage());
//            return new ArrayList<StockProductoDeposito>();
//        }
//    }
    
    public List<Stock> getStockByDeposito(String codDep){
        try {
            Query q = em.createQuery("SELECT s FROM Stock s "
                    + "WHERE s.deposi = :deposi "                    
                    + "ORDER BY s.artcod ");

            q.setParameter("deposi",codDep);
            
            return q.getResultList();
            
        } catch (Exception e) {            
            System.out.println("getStockByDeposito" + e );
            return new ArrayList<Stock>();
        }
    }
    
    public List<Stock> getStockByDepositoSinAtributos(String codDep){
        try {
                        
            Query q = em.createNativeQuery("SELECT ARTCOD, DEPOSI, '' AS NATRI1,'' AS NATRI2,'' AS NATRI3,'' AS NATRI4,'' AS NATRI5,'' AS NATRI6,'' AS NATRI7, SUM(STOCKS) AS STOCKS "
                    + " FROM st_stock " 
                    + " WHERE DEPOSI = '"+codDep+"' "
                    + " GROUP BY ARTCOD,DEPOSI " 
                    + " ORDER BY ARTCOD ", Stock.class);

            return q.getResultList();
            
        } catch (Exception e) {            
            System.out.println("getStockByDeposito" + e );
            return new ArrayList<Stock>();
        }
    }
        
    public Producto getProductoByDepositoConStock(String codDep){
        try {
                        
            Query q = em.createQuery("SELECT DISTINCT s.producto FROM Stock s "
                    + "WHERE s.deposi = :codigo "                    
                    + "ORDER BY s.artcod " );

            q.setParameter("codigo",codDep);
            
            return (Producto) q.getSingleResult();
            
        } catch (NoResultException e) {            
            System.out.println("Deposito de producto único, con más de un producto "+ codDep);
            return null;  
            
        } catch (NonUniqueResultException e) {            
            return null;  
            
        } catch (Exception e) {            
            e.printStackTrace();
            System.out.println("getProductoByDepositoConStock" + e );
            return null;
        }
    }
    
    
    public List<Stock> getStockByProductoSinAtributos(String artcod){
        try {
                        
            Query q = em.createNativeQuery("SELECT ARTCOD, DEPOSI, '' AS NATRI1,'' AS NATRI2,'' AS NATRI3,'' AS NATRI4,'' AS NATRI5,'' AS NATRI6,'' AS NATRI7, SUM(STOCKS) AS STOCKS "
                    + " FROM st_stock " 
                    + " WHERE ARTCOD = '"+artcod+"' "
                    + " GROUP BY ARTCOD,DEPOSI " 
                    + " ORDER BY ARTCOD ", Stock.class);

            return q.getResultList();
            
        } catch (Exception e) {            
            System.out.println("getStockByDeposito" + e );
            return new ArrayList<Stock>();
        }
    }

    
    public List<Stock> getStockByProducto(String artCod){
        try {

            Query q = em.createQuery("SELECT s FROM Stock s "
                    + "WHERE s.artcod = :codigo "                    
                    + "ORDER BY s.deposi " );

            q.setParameter("codigo",artCod);
            
            return q.getResultList();
            
        } catch (Exception e) {            
            System.out.println("getStockByProducto" + e.getCause());
            return new ArrayList<Stock>();
        }
    }
    
    public List<Stock> getStockByProductoDeposito(String artcod,String deposi){
        try {

            Query q = em.createQuery(" SELECT s FROM Stock s "
                    + " WHERE s.artcod = :codigo "                    
                    + " AND s.deposi = :deposito "
                    + " AND s.stocks <> 0 " );

            q.setParameter("codigo",artcod);
            q.setParameter("deposito",deposi);            

            return q.getResultList();
            
        } catch (Exception e) {            
            System.out.println("No se puede obtener stock por producto y deposito" + e);
            return new ArrayList<Stock>();
        }
    }

    

}
