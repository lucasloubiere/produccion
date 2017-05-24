/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.modelo;

import com.global.modelo.Auditoria;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ide
 */
@Entity
@Table(name = "st_stock")
@XmlRootElement

public class Stock implements Serializable {

    @Id
    @Column(name = "ARTCOD", nullable = false, length = 30)
    private String artcod;
    @Id
    @Column(name = "DEPOSI", nullable = false, length = 15)
    private String deposi;    
    @Id
    @Column(name = "NATRI1", nullable = false, length = 30)
    private String atributo1;
    @Id
    @Column(name = "NATRI2", nullable = false, length = 30)
    private String atributo2;
    @Id
    @Column(name = "NATRI3", nullable = false, length = 30)
    private String atributo3;
    @Id
    @Column(name = "NATRI4", nullable = false, length = 30)
    private String atributo4;
    @Id
    @Column(name = "NATRI5", nullable = false, length = 30)
    private String atributo5;
    @Id
    @Column(name = "NATRI6", nullable = false, length = 30)
    private String atributo6;
    @Id
    @Column(name = "NATRI7", nullable = false, length = 30)
    private String atributo7;    
    
    @JoinColumn(name = "ARTCOD", referencedColumnName = "CODIGO", nullable = false, insertable=false, updatable=false)    
    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    private Producto producto;
    
    @JoinColumn(name = "DEPOSI", referencedColumnName = "CODIGO", nullable = false,insertable=false, updatable=false)    
    @ManyToOne(fetch = FetchType.LAZY)
    private Deposito deposito;
 
    @Column(name = "STOCK", precision = 15, scale = 2)
    private BigDecimal stocks;
    
    
    @Embedded
    private Auditoria auditoria;
    
    public Stock() {
       this.auditoria = new Auditoria();
    }

    public String getArtcod() {
        return artcod;
    }

    public void setArtcod(String artcod) {
        this.artcod = artcod;
    }

    public String getDeposi() {
        return deposi;
    }

    public void setDeposi(String deposi) {
        this.deposi = deposi;
    }

    public String getAtributo1() {
        return atributo1;
    }

    public void setAtributo1(String atributo1) {
        this.atributo1 = atributo1;
    }

    public String getAtributo2() {
        return atributo2;
    }

    public void setAtributo2(String atributo2) {
        this.atributo2 = atributo2;
    }

    public String getAtributo3() {
        return atributo3;
    }

    public void setAtributo3(String atributo3) {
        this.atributo3 = atributo3;
    }

    public String getAtributo4() {
        return atributo4;
    }

    public void setAtributo4(String atributo4) {
        this.atributo4 = atributo4;
    }

    public String getAtributo5() {
        return atributo5;
    }

    public void setAtributo5(String atributo5) {
        this.atributo5 = atributo5;
    }

    public String getAtributo6() {
        return atributo6;
    }

    public void setAtributo6(String atributo6) {
        this.atributo6 = atributo6;
    }

    public String getAtributo7() {
        return atributo7;
    }

    public void setAtributo7(String atributo7) {
        this.atributo7 = atributo7;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public BigDecimal getStocks() {
        return stocks;
    }

    public void setStocks(BigDecimal stocks) {
        this.stocks = stocks;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }
    
}
