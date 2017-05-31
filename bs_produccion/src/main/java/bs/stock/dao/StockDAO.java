/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.dao;


import bs.global.dao.BaseDAO;
import bs.stock.modelo.Deposito;
import bs.stock.modelo.ItemMovimientoStock;
import bs.stock.modelo.Producto;
import bs.stock.modelo.Stock;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class StockDAO extends BaseDAO {

    @EJB
    ProductoDAO productoDAO;
    @EJB
    DepositoDAO depositoDAO;

    public BigDecimal getDisponibleProducto(Stock s){

        try {          

            Query q = (Query) em.createQuery("SELECT SUM(s.stocks) "
                    + "FROM Stock s "
                    + "WHERE s.artcod = :artcod "
                    + "AND s.deposito = :deposi "
                    + "AND s.atributo1 = :atributo1 "
                    + "AND s.atributo2 = :atributo2 "
                    + "AND s.atributo3 = :atributo3 "
                    + "AND s.atributo4 = :atributo4 "
                    + "AND s.atributo5 = :atributo5 "
                    + "AND s.atributo6 = :atributo6 "
                    + "AND s.atributo7 = :atributo7 "
                    + " " );

            q.setParameter("artcod",s.getCodigo());
            q.setParameter("deposi",s.getDeposito());            
            q.setParameter("atributo1",s.getAtributo1());
            q.setParameter("atributo2",s.getAtributo2());
            q.setParameter("atributo3",s.getAtributo3());
            q.setParameter("atributo4",s.getAtributo4());
            q.setParameter("atributo5",s.getAtributo5());
            q.setParameter("atributo6",s.getAtributo6());
            q.setParameter("atributo7",s.getAtributo7());

            return (BigDecimal) q.getSingleResult();
            
        } catch (Exception e) {
            System.out.println("No se puede obtener disponible en stock" + e.getCause());
            return BigDecimal.ZERO;
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

    public BigDecimal getStockAFecha(Producto p,Deposito d, Date fecha){

        try {

            Query q = em.createNativeQuery(" SELECT ifnull(sum(st_movimiento_item.STOCKS) ,0) "
                    + " FROM st_movimiento INNER JOIN st_movimiento_item "
                    + " ON   st_movimiento.ID = st_movimiento_item.ID_MCAB "
                    + " WHERE st_movimiento_item.ARTCOD = ?1 "
                    + " AND st_movimiento_item.DEPOSI = ?2 "
                    + " AND st_movimiento.FCHMOV < ?3 " );

            q.setParameter("1",p.getCodigo());
            q.setParameter("2",d.getCodigo());
            q.setParameter("3",fecha);
            
            return (BigDecimal) q.getSingleResult();
            
        } catch (Exception e) {            
            System.out.println("No se puede obtener stock por producto a fecha" + e.getCause());
            return BigDecimal.ZERO;
        }

    }

    public List<ItemMovimientoStock> getMovimientosEntreFechas(Producto p, Deposito d, Date fDesde, Date fHasta) {
        try {
            
            Query q = em.createQuery("SELECT i FROM ItemMovimientoStock i "
                    + "WHERE i.producto.codigo = :codigo "                    
                    + "AND i.deposito.codigo = :deposi "
                    + "AND i.movimiento.fechaMovimiento BETWEEN :fDesde AND :fHasta ");

            q.setParameter("codigo",p.getCodigo());
            q.setParameter("deposi",d.getCodigo());            
            q.setParameter("fDesde",fDesde);
            q.setParameter("fHasta",fHasta);

            return q.getResultList();
        } catch (Exception e) {    
            e.printStackTrace();
            System.out.println("No se puede obtener stock entre fechas" + e.getMessage());
            return new ArrayList<ItemMovimientoStock>();
        }
    }

}
