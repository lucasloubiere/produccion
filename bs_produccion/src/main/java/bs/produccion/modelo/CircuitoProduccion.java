/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.modelo;

import bs.global.modelo.Auditoria;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
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
@Table(name = "pd_circuito")
@IdClass(CircuitoProduccionPK.class)
@XmlRootElement
public class CircuitoProduccion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "CIRCOM", nullable = false, length = 6)
    private String circom;
    @Id
    @Column(name = "CIRAPL", nullable = false, length = 6)
    private String cirapl;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "actust", nullable = false, length = 1)
    private String actualizaStock;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "actupd", nullable = false, length = 1)
    private String actualizaProduccion;
    
    @JoinColumn(name = "circom", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CodigoCircuitoProduccion circuitoComienzo;
    
    @JoinColumn(name = "cirapl", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CodigoCircuitoProduccion circuitoAplicacion;
    
    @Embedded
    private Auditoria auditoria;

    public CircuitoProduccion() {
        this.auditoria = new Auditoria();
    }

    public CircuitoProduccion(String circom, String cirapl) {
        this.circom = circom;
        this.cirapl = cirapl;
        this.auditoria = new Auditoria();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActualizaStock() {
        return actualizaStock;
    }

    public void setActualizaStock(String actualizaStock) {
        this.actualizaStock = actualizaStock;
    }

    public String getActualizaProduccion() {
        return actualizaProduccion;
    }

    public void setActualizaProduccion(String actualizaProduccion) {
        this.actualizaProduccion = actualizaProduccion;
    }

    public String getCircom() {
        return circom;
    }

    public void setCircom(String circom) {
        this.circom = circom;
    }

    public String getCirapl() {
        return cirapl;
    }

    public void setCirapl(String cirapl) {
        this.cirapl = cirapl;
    }

    public CodigoCircuitoProduccion getCircuitoComienzo() {
        return circuitoComienzo;
    }

    public void setCircuitoComienzo(CodigoCircuitoProduccion circuitoComienzo) {
        this.circuitoComienzo = circuitoComienzo;
    }


    public CodigoCircuitoProduccion getCircuitoAplicacion() {
        return circuitoAplicacion;
    }

    public void setCircuitoAplicacion(CodigoCircuitoProduccion circuitoAplicacion) {
        this.circuitoAplicacion = circuitoAplicacion;
    }

  
    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.circom != null ? this.circom.hashCode() : 0);
        hash = 17 * hash + (this.cirapl != null ? this.cirapl.hashCode() : 0);
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
        final CircuitoProduccion other = (CircuitoProduccion) obj;
        if ((this.circom == null) ? (other.circom != null) : !this.circom.equals(other.circom)) {
            return false;
        }
        if ((this.cirapl == null) ? (other.cirapl != null) : !this.cirapl.equals(other.cirapl)) {
            return false;
        }
        return true;
    }

    
}
