/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.global.web;

import bs.stock.modelo.Stock;
import bs.stock.rn.StockRN;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author lloubiere
 */
@ManagedBean
@ApplicationScoped
public class DashboardBean {
    
    @EJB 
    private StockRN stockRN;
    
    List<Stock> stocks;

    /**
     * Creates a new instance of DashboardBean
     */
    public DashboardBean() {
        
    }
    
    @PostConstruct
    public void iniciar(){
        actualizarResumenTanques();
    }
    
    public void actualizarResumenTanques(){
        
        Map<String, String> filtro = new LinkedHashMap<String, String>();
        filtro.clear();
        filtro.put("deposito.calculaStock IN ", "('M','F')");
        filtro.put("deposito.sector.codigo = ", "'REF'");
        
        stocks = stockRN.getSumStockByProductoRefineria();
        
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
    
}
