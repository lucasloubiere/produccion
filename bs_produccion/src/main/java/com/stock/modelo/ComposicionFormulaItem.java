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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lloubiere
 */
@Entity
@Table(name = "st_composicion_formula_item")
@XmlRootElement

public class ComposicionFormulaItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ComposicionFormulaItemPK composicionFormulaItemPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad", nullable = false, precision = 20, scale = 4)
    private BigDecimal cantidad;
    @Embedded
    private Auditoria auditoria;
    
    @JoinColumns({
        @JoinColumn(name = "artcod", referencedColumnName = "artcod", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "codfor", referencedColumnName = "codfor", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private ComposicionFormula composicionFormula;

    public ComposicionFormulaItem() {
        this.auditoria = new Auditoria();
    }

    public ComposicionFormulaItem(ComposicionFormulaItemPK composicionFormulaItemPK) {
        this.composicionFormulaItemPK = composicionFormulaItemPK;
    }

    public ComposicionFormulaItem(ComposicionFormulaItemPK composicionFormulaItemPK, BigDecimal cantidad, String debaja) {
        this.composicionFormulaItemPK = composicionFormulaItemPK;
        this.cantidad = cantidad;
        
    }

    public ComposicionFormulaItem(int nroitem, String artcod, String codfor) {
        this.composicionFormulaItemPK = new ComposicionFormulaItemPK(nroitem, artcod, codfor);
    }

    public ComposicionFormulaItemPK getComposicionFormulaItemPK() {
        return composicionFormulaItemPK;
    }

    public void setComposicionFormulaItemPK(ComposicionFormulaItemPK composicionFormulaItemPK) {
        this.composicionFormulaItemPK = composicionFormulaItemPK;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (composicionFormulaItemPK != null ? composicionFormulaItemPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComposicionFormulaItem)) {
            return false;
        }
        ComposicionFormulaItem other = (ComposicionFormulaItem) object;
        if ((this.composicionFormulaItemPK == null && other.composicionFormulaItemPK != null) || (this.composicionFormulaItemPK != null && !this.composicionFormulaItemPK.equals(other.composicionFormulaItemPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.ComposicionFormulaItem[ composicionFormulaItemPK=" + composicionFormulaItemPK + " ]";
    }
    
}
