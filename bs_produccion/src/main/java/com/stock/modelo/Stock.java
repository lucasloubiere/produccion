/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.modelo;

import com.global.modelo.Auditoria;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ide
 */
@Entity
@Table(name = "st_stock")
@XmlRootElement

public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected StockPK stockPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "stock", nullable = false, precision = 20, scale = 2)
    private BigDecimal stock;
    @JoinColumn(name = "deposi", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Deposito deposito;
    @JoinColumn(name = "artcod", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto;
    @JoinColumn(name = "unidadDeMedida", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(optional = false)
    private UnidadMedida unidadDeMedida;
    @Embedded
    private Auditoria auditoria;
    
    public Stock() {
       this.auditoria = new Auditoria();
    }

    public Stock(StockPK stockPK) {
       this.auditoria = new Auditoria();
        this.stockPK = stockPK;
    }

    public Stock(StockPK stockPK, BigDecimal stock) {
        this.auditoria = new Auditoria();
        this.stockPK = stockPK;
        this.stock = stock;
    }

    public Stock(String codigoProducto, String codigoDeposito) {
        this.auditoria = new Auditoria();
        this.stockPK = new StockPK(codigoProducto, codigoDeposito);
    }

    public StockPK getStockPK() {
        
        return stockPK;
    }

    public void setStockPK(StockPK stockPK) {
        this.stockPK = stockPK;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public UnidadMedida getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(UnidadMedida unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockPK != null ? stockPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stock)) {
            return false;
        }
        Stock other = (Stock) object;
        if ((this.stockPK == null && other.stockPK != null) || (this.stockPK != null && !this.stockPK.equals(other.stockPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.Stock[ stockPK=" + stockPK + " ]";
    }
    
}
