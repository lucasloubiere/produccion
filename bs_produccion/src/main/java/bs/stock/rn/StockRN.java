/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.rn;

import bs.stock.dao.StockDAO;
import bs.stock.modelo.Deposito;
import bs.stock.modelo.ItemMovimientoStock;
import bs.stock.modelo.Producto;
import bs.stock.modelo.Stock;
import bs.stock.modelo.StockPK;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author lloubiere
 */
@Stateless
public class StockRN implements Serializable {
    
     @EJB private StockDAO stockDAO;

    public synchronized void guardar(Stock stock) throws Exception{
        
        Stock stockAux = getStock(stock);        
        if(stockAux==null){
            stockDAO.crear(stock);
        }else{
            stockAux.setStocks(stockAux.getStocks().add(stock.getStocks()));
            stockDAO.editar(stockAux);
        }
    }
    
    public Stock getStock(Stock stock){

        StockPK idPK = new StockPK(stock.getCodigo(), stock.getDeposi(),stock.getAtributo1(), stock.getAtributo2(), stock.getAtributo3(), stock.getAtributo4(), stock.getAtributo5(), stock.getAtributo6(), stock.getAtributo7());
        return stockDAO.getObjeto(Stock.class, idPK);
    }

public boolean isProductoDisponible(Stock s){

        BigDecimal disp = stockDAO.getStockDisponibleByProducto(s);
        BigDecimal cant = s.getStocks().add(BigDecimal.ZERO);
        
        s.setStockDisponible(disp);

        if(disp==null){
            disp = BigDecimal.ZERO;
            System.out.println("Producto sin stock en "+ s.getDeposi());
            return false;
        }

        System.err.println("Producto:" + s + "Cantidad: " + cant  + " Disp:" + disp );

        if(cant.compareTo(BigDecimal.ZERO)>0){
            System.err.println("El stock positivo: " + cant);
            cant = cant.negate();
        }

//        System.err.println("Stock resultante: " +disp.add(cant));

        //Si el stock resultante es menor a cero no pasa
        if(disp.add(cant).compareTo(BigDecimal.ZERO)==-1){
            return false;
        }

        return true;
    }

    public Stock getStock(StockPK idPK){

        return stockDAO.getObjeto(Stock.class, idPK);
    }

    public List<Stock> getStockByProducto(Producto p) {

        return stockDAO.getStockByProducto(p.getCodigo());
    }
    
    public List<Stock> getStockByProducto(Producto p, boolean conAtributos) {

        if(p==null){
            return new ArrayList<Stock>();
        }
        
        List<Stock> resultado = stockDAO.getStockByProductoSinAtributos(p.getCodigo());            
        
        if(conAtributos){
            
            for(Stock s: resultado){             
                s.setAtributos(stockDAO.getStockByProductoDeposito(s.getCodigo(), s.getDeposi()));
            }
        }
        
        return resultado; 
    }
    
   
    /**
     * 
     * @param d deposito
     * @param conAtributos
     * @return Lista de stock con o sin atributos. 
     */
    public List<Stock> getStockByDeposito(Deposito d, boolean conAtributos) {
        
        if(d==null){
            return new ArrayList<Stock>();
        }
        
        List<Stock> resultado = stockDAO.getStockByDepositoSinAtributos(d.getCodigo());            
        
        if(conAtributos){
            
            for(Stock s: resultado){
             
                s.setAtributos(stockDAO.getStockByProductoDeposito(s.getCodigo(), s.getDeposi()));
            }
        }
        
        return resultado;        
    }

    public List<Stock> getStockByDeposito(Deposito d) {

        if(d==null){
            return new ArrayList<Stock>();
        }
        
        return stockDAO.getStockByDeposito(d.getCodigo());
    }

    public List<Stock> getStockByProductoDeposito(Producto p,Deposito d){
        
        String artcod = "";
        String deposi = "";
        
        if(p!=null){
            artcod = p.getCodigo();
        }
        
        if(d!=null){
            deposi = d.getCodigo();
        }
        
        
        return stockDAO.getStockByProductoDeposito(artcod ,deposi);
    }

    public BigDecimal getStockAFecha(Producto p,Deposito d, Date fecha){
        return stockDAO.getStockAFecha(p,d,fecha);
    }

    public List<ItemMovimientoStock> getMovimientosEntreFechas(Producto p,Deposito d, Date fDesde,Date fHasta){

        return stockDAO.getMovimientosEntreFechas(p,d,fDesde,fHasta);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
