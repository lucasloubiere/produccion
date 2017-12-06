/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lloubiere
 */
@Entity
@Table(name = "pd_circuito", catalog = "produccion-ds", schema = "")
@XmlRootElement

public class Circuito implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CircuitoPK circuitoPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "actust", nullable = false, length = 1)
    private String actust;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "actupd", nullable = false, length = 1)
    private String actupd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "DEBAJA", nullable = false, length = 1)
    private String debaja;
    @Column(name = "FECALT")
    @Temporal(TemporalType.DATE)
    private Date fecalt;
    @Column(name = "FECMOD")
    @Temporal(TemporalType.DATE)
    private Date fecmod;
    @Size(max = 1)
    @Column(name = "ULTOPR", length = 1)
    private String ultopr;
    @Size(max = 15)
    @Column(name = "USERID", length = 15)
    private String userid;
    @OneToMany(mappedBy = "circuito")
    private List<MovimientoProduccion> movimientoProduccionList;
    @JoinColumn(name = "cirapl", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CodigoCircuitoProduccion codigoCircuitoProduccion;
    @JoinColumn(name = "circom", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CodigoCircuitoProduccion codigoCircuitoProduccion1;

    public Circuito() {
    }

    public Circuito(CircuitoPK circuitoPK) {
        this.circuitoPK = circuitoPK;
    }

    public Circuito(CircuitoPK circuitoPK, String descripcion, String actust, String actupd, String debaja) {
        this.circuitoPK = circuitoPK;
        this.descripcion = descripcion;
        this.actust = actust;
        this.actupd = actupd;
        this.debaja = debaja;
    }

    public Circuito(String circom, String cirapl) {
        this.circuitoPK = new CircuitoPK(circom, cirapl);
    }

    public CircuitoPK getCircuitoPK() {
        return circuitoPK;
    }

    public void setCircuitoPK(CircuitoPK circuitoPK) {
        this.circuitoPK = circuitoPK;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActust() {
        return actust;
    }

    public void setActust(String actust) {
        this.actust = actust;
    }

    public String getActupd() {
        return actupd;
    }

    public void setActupd(String actupd) {
        this.actupd = actupd;
    }

    public String getDebaja() {
        return debaja;
    }

    public void setDebaja(String debaja) {
        this.debaja = debaja;
    }

    public Date getFecalt() {
        return fecalt;
    }

    public void setFecalt(Date fecalt) {
        this.fecalt = fecalt;
    }

    public Date getFecmod() {
        return fecmod;
    }

    public void setFecmod(Date fecmod) {
        this.fecmod = fecmod;
    }

    public String getUltopr() {
        return ultopr;
    }

    public void setUltopr(String ultopr) {
        this.ultopr = ultopr;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @XmlTransient
    public List<MovimientoProduccion> getMovimientoProduccionList() {
        return movimientoProduccionList;
    }

    public void setMovimientoProduccionList(List<MovimientoProduccion> movimientoProduccionList) {
        this.movimientoProduccionList = movimientoProduccionList;
    }

    public CodigoCircuitoProduccion getCodigoCircuitoProduccion() {
        return codigoCircuitoProduccion;
    }

    public void setCodigoCircuitoProduccion(CodigoCircuitoProduccion codigoCircuitoProduccion) {
        this.codigoCircuitoProduccion = codigoCircuitoProduccion;
    }

    public CodigoCircuitoProduccion getCodigoCircuitoProduccion1() {
        return codigoCircuitoProduccion1;
    }

    public void setCodigoCircuitoProduccion1(CodigoCircuitoProduccion codigoCircuitoProduccion1) {
        this.codigoCircuitoProduccion1 = codigoCircuitoProduccion1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (circuitoPK != null ? circuitoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Circuito)) {
            return false;
        }
        Circuito other = (Circuito) object;
        if ((this.circuitoPK == null && other.circuitoPK != null) || (this.circuitoPK != null && !this.circuitoPK.equals(other.circuitoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bs.produccion.modelo.Circuito[ circuitoPK=" + circuitoPK + " ]";
    }
    
}
