/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.modelo;

import bs.global.modelo.Auditoria;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author ide
 */
@Entity
@Table(name = "st_stock")
@IdClass(StockPK.class)
public class Stock implements Serializable {
    private static final long serialVersionUID = 1L;

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
 
    @Column(name = "STOCKS", precision = 15, scale = 2)
    private BigDecimal stocks;
    
    @Transient
    List<Stock> atributos;

    @Embedded
    private Auditoria auditoria;


    public Stock() {
        
        this.atributo1 = "";
        this.atributo2 = "";
        this.atributo3 = "";
        this.atributo4 = "";
        this.atributo5 = "";              
        this.atributo6 = "";
        this.atributo7 = "";
                
        this.auditoria = new Auditoria();
    }

    public Stock(ItemMovimientoStock i){
        
        this.artcod = i.getProducto().getCodigo();
        this.deposi = i.getDeposito().getCodigo();
        this.atributo1 = "";
        this.atributo2 = "";
        this.atributo3 = "";
        this.atributo4 = "";
        this.atributo5 = "";              
        this.atributo6 = "";
        this.atributo7 = "";
        this.stocks = i.getStocks();
        this.auditoria = new Auditoria();
        this.producto = i.getProducto();
        this.deposito = i.getDeposito();        
        this.auditoria = new Auditoria();

    }

    public Stock(String artcod, String nserie, String envase, String ndespa, String notros, String nfecha, String natrib, String nubica, String nestan, String deposi, String sector) {
        
        this.artcod = artcod;
        this.deposi = deposi;  
        this.atributo1 = "";
        this.atributo2 = "";
        this.atributo3 = "";
        this.atributo4 = "";
        this.atributo5 = "";              
        this.atributo6 = "";
        this.atributo7 = "";
        this.auditoria = new Auditoria();
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getCodigo() {
        return artcod;
    }

    public void setArtcod(String artcod) {
        this.artcod = artcod;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    public String getDeposi() {
        return deposi;
    }

    public void setDeposi(String deposi) {
        this.deposi = deposi;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
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
    
    public BigDecimal getStocks() {
        return (stocks==null?BigDecimal.ZERO:stocks);
    }

    public void setStocks(BigDecimal stocks) {
        this.stocks = stocks;
    }

    public List<Stock> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<Stock> atributos) {
        this.atributos = atributos;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.artcod != null ? this.artcod.hashCode() : 0);
        hash = 67 * hash + (this.deposi != null ? this.deposi.hashCode() : 0);
        hash = 67 * hash + (this.atributo1 != null ? this.atributo1.hashCode() : 0);
        hash = 67 * hash + (this.atributo2 != null ? this.atributo2.hashCode() : 0);
        hash = 67 * hash + (this.atributo3 != null ? this.atributo3.hashCode() : 0);
        hash = 67 * hash + (this.atributo4 != null ? this.atributo4.hashCode() : 0);
        hash = 67 * hash + (this.atributo5 != null ? this.atributo5.hashCode() : 0);
        hash = 67 * hash + (this.atributo6 != null ? this.atributo6.hashCode() : 0);
        hash = 67 * hash + (this.atributo7 != null ? this.atributo7.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Stock other = (Stock) obj;
        if ((this.artcod == null) ? (other.artcod != null) : !this.artcod.equals(other.artcod)) {
            return false;
        }
        if ((this.deposi == null) ? (other.deposi != null) : !this.deposi.equals(other.deposi)) {
            return false;
        }
        if ((this.atributo1 == null) ? (other.atributo1 != null) : !this.atributo1.equals(other.atributo1)) {
            return false;
        }
        if ((this.atributo2 == null) ? (other.atributo2 != null) : !this.atributo2.equals(other.atributo2)) {
            return false;
        }
        if ((this.atributo3 == null) ? (other.atributo3 != null) : !this.atributo3.equals(other.atributo3)) {
            return false;
        }
        if ((this.atributo4 == null) ? (other.atributo4 != null) : !this.atributo4.equals(other.atributo4)) {
            return false;
        }
        if ((this.atributo5 == null) ? (other.atributo5 != null) : !this.atributo5.equals(other.atributo5)) {
            return false;
        }
        if ((this.atributo6 == null) ? (other.atributo6 != null) : !this.atributo6.equals(other.atributo6)) {
            return false;
        }
        if ((this.atributo7 == null) ? (other.atributo7 != null) : !this.atributo7.equals(other.atributo7)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Stock{" + "artcod=" + artcod + ", deposi=" + deposi + ", atributo1=" + atributo1 + ", atributo2=" + atributo2 + ", atributo3=" + atributo3 + ", atributo4=" + atributo4 + ", atributo5=" + atributo5 + ", atributo6=" + atributo6 + ", atributo7=" + atributo7 + '}';
    }
    
}
