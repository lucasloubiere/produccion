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
                    + " AND s.stocks <> 0 "                    
                    + "ORDER BY s.artcod " );

            q.setParameter("codigo",codDep);
            
            return (Producto) q.getSingleResult();
            
        } catch (NoResultException e) {                        
            return null;  
            
        } catch (NonUniqueResultException e) {     
            
            System.out.println("Deposito de producto único, con más de un producto "+ codDep);
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

    public List<Stock> getSumStockByProductoRefineria() {
        
        try {
                        
            Query q = em.createNativeQuery(" SELECT ARTCOD, DEPOSI, '' AS NATRI1,'' AS NATRI2,'' AS NATRI3,'' AS NATRI4,'' AS NATRI5,'' AS NATRI6,'' AS NATRI7, SUM(s.STOCKS) AS STOCKS " +
                                           " FROM st_stock s inner join st_producto p on s.artcod = p.codigo" +
                    " inner join st_deposito d on s.deposi = d.codigo" +
                    " where d.codsec = 'REF' " +
                    " GROUP BY ARTCOD ", Stock.class);

            return q.getResultList();
            
        } catch (Exception e) {            
            System.out.println("getSumStockByProductoRefineria" + e );
            return new ArrayList<Stock>();
        }
    }
    
    public void recalcularStock() {
        
        try {
            Query q1 = em.createNativeQuery("DELETE FROM st_stock");
            
            q1.executeUpdate();
            
            Query q2 = em.createNativeQuery("INSERT INTO `st_stock` (`artcod`, `deposi`, `natri1`, `natri2`, `natri3`, `natri4`, `natri5`, `natri6`, `natri7`, "
                    + " `stocks`, `DEBAJA`, `FECALT`, `FECMOD`, `ULTOPR`) "
                    + " SELECT i.artcod, i.deposi,i.natri1,i.natri2,i.natri3,i.natri4,i.natri5,i.natri6,i.natri7, SUM(i.stock) as stocks,'N' as DEBAJA,CURDATE() AS FECALT, "
                    + " CURDATE() AS FECMOD,'A' AS ULTOPR"
                    + " FROM st_movimiento_item i inner JOIN st_movimiento m on m.id = i.idCab"
                    + " GROUP BY i.artcod, i.deposi,"
                    + " i.natri1,i.natri2,i.natri3,i.natri4,i.natri5,i.natri6,i.natri7");
            
            q2.executeUpdate();
        } catch (Exception e) {            
            
            System.err.println("recalcularStock " + e);
        }        
    }

    

}
