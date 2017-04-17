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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lloubiere
 */
@Entity
@Table(name = "st_composicion_formula_item")
@XmlRootElement
@IdClass(ComposicionFormulaItemPK.class)
public class ComposicionFormulaItem implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "artcod", nullable = false, length = 20)
    private String artcod;
    @Id
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "codfor", nullable = false, length = 6)
    private String codfor;
    @Id
    @NotNull
    @Column(name = "nroitem", nullable = false)
    private int nroitem;
    
    @JoinColumns({
        @JoinColumn(name = "artcod", referencedColumnName = "artcod", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "codfor", referencedColumnName = "codfor", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private ComposicionFormula composicionFormula;
    
    @JoinColumn(name = "artitem", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Producto producto;
    
    @Basic(optional = false)
    @NotNull(message = "La cantidad no puede estar vacía")    
    @Column(name = "cantidad", nullable = false, precision = 20, scale = 4)
    private BigDecimal cantidad;
        
    
    @Embedded
    private Auditoria auditoria;

    public ComposicionFormulaItem() {
        this.auditoria = new Auditoria();
        this.cantidad = BigDecimal.ZERO;
    }

    public int getNroitem() {
        return nroitem;
    }

    public void setNroitem(int nroitem) {
        this.nroitem = nroitem;
    }

    public String getArtcod() {
        return artcod;
    }

    public void setArtcod(String artcod) {
        this.artcod = artcod;
    }

    public String getCodfor() {
        return codfor;
    }

    public void setCodfor(String codfor) {
        this.codfor = codfor;
    }
    
    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    public ComposicionFormula getComposicionFormula() {
        return composicionFormula;
    }

    public void setComposicionFormula(ComposicionFormula composicionFormula) {
        this.composicionFormula = composicionFormula;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.nroitem;
        hash = 89 * hash + (this.artcod != null ? this.artcod.hashCode() : 0);
        hash = 89 * hash + (this.codfor != null ? this.codfor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComposicionFormulaItem other = (ComposicionFormulaItem) obj;
        if (this.nroitem != other.nroitem) {
            return false;
        }
        if ((this.artcod == null) ? (other.artcod != null) : !this.artcod.equals(other.artcod)) {
            return false;
        }
        if ((this.codfor == null) ? (other.codfor != null) : !this.codfor.equals(other.codfor)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ComposicionFormulaItem{" + "nroitem=" + nroitem + ", artcod=" + artcod + ", codfor=" + codfor + '}';
    }
    
    

    
}
