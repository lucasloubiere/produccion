/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.modelo;

import bs.global.modelo.Auditoria;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lloubiere
 */
@Entity
@Table(name = "st_composicion_formula")
@XmlRootElement
@IdClass(ComposicionFormulaPK.class)
public class ComposicionFormula implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ARTCOD", nullable = false, length = 30)
    private String artcod;
    
    @Id
    @Column(name = "codigo", nullable = false, length = 8)
    private String codfor;
        
    @JoinColumn(name = "artcod", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto;

    /**
     *  Formula
     */
    @JoinColumn(name = "codigo", referencedColumnName = "codigo", nullable = false, insertable=false, updatable=false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Formula formula;
    
    
    @Column(name = "FCHINI")
    @Temporal(TemporalType.DATE)
    private Date fechainicio;
    
        
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "composicionFormula", fetch=FetchType.LAZY)    
    private List<ItemComposicionFormulaComponente> itemsComponente;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "composicionFormula", fetch=FetchType.LAZY)    
    private List<ItemComposicionFormulaProceso> itemsProceso;
   
    
    @Embedded
    private Auditoria auditoria;

    public ComposicionFormula() {
        this.auditoria = new Auditoria();
        this.itemsComponente = new ArrayList<ItemComposicionFormulaComponente>();
        this.itemsProceso = new ArrayList<ItemComposicionFormulaProceso>();
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

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
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

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public List<ItemComposicionFormulaComponente> getItemsComponente() {
        return itemsComponente;
    }

    public void setItemsComponente(List<ItemComposicionFormulaComponente> itemsComponente) {
        this.itemsComponente = itemsComponente;
    }

    public List<ItemComposicionFormulaProceso> getItemsProceso() {
        return itemsProceso;
    }

    public void setItemsProceso(List<ItemComposicionFormulaProceso> itemsProceso) {
        this.itemsProceso = itemsProceso;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.artcod != null ? this.artcod.hashCode() : 0);
        hash = 41 * hash + (this.codfor != null ? this.codfor.hashCode() : 0);
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
        final ComposicionFormula other = (ComposicionFormula) obj;
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
        return "ComposicionFormula{" + "artcod=" + artcod + ", codfor=" + codfor + '}';
    }
    
    
}
