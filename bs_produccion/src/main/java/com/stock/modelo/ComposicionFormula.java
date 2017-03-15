/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.modelo;

import com.global.modelo.Auditoria;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lloubiere
 */
@Entity
@Table(name = "st_composicion_formula")
@XmlRootElement
public class ComposicionFormula implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ComposicionFormulaPK composicionFormulaPK;
    @Column(name = "fechainicio")
    @Temporal(TemporalType.DATE)
    private Date fechainicio;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "composicionFormula")
    private List<ComposicionFormulaItem> composicionFormulaItemList;
    @JoinColumn(name = "codfor", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Formula formula;
    
    @Embedded
    private Auditoria auditoria;

    public ComposicionFormula() {
        this.auditoria = new Auditoria();
    }

    public ComposicionFormula(ComposicionFormulaPK composicionFormulaPK) {
        this.composicionFormulaPK = composicionFormulaPK;
    }

    public ComposicionFormula(ComposicionFormulaPK composicionFormulaPK, String debaja) {
        this.composicionFormulaPK = composicionFormulaPK;
      
    }

    public ComposicionFormula(String artcod, String codfor) {
        this.composicionFormulaPK = new ComposicionFormulaPK(artcod, codfor);
    }

    public ComposicionFormulaPK getComposicionFormulaPK() {
        return composicionFormulaPK;
    }

    public void setComposicionFormulaPK(ComposicionFormulaPK composicionFormulaPK) {
        this.composicionFormulaPK = composicionFormulaPK;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

 

    @XmlTransient
    public List<ComposicionFormulaItem> getComposicionFormulaItemList() {
        return composicionFormulaItemList;
    }

    public void setComposicionFormulaItemList(List<ComposicionFormulaItem> composicionFormulaItemList) {
        this.composicionFormulaItemList = composicionFormulaItemList;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (composicionFormulaPK != null ? composicionFormulaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComposicionFormula)) {
            return false;
        }
        ComposicionFormula other = (ComposicionFormula) object;
        if ((this.composicionFormulaPK == null && other.composicionFormulaPK != null) || (this.composicionFormulaPK != null && !this.composicionFormulaPK.equals(other.composicionFormulaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.ComposicionFormula[ composicionFormulaPK=" + composicionFormulaPK + " ]";
    }
    
}
