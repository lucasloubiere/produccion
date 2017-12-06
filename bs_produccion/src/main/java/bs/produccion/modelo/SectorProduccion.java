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
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "pd_sector", catalog = "produccion-ds", schema = "")
@XmlRootElement

public class SectorProduccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "codigo", nullable = false, length = 6)
    private String codigo;
    @Size(max = 100)
    @Column(name = "descripcion", length = 100)
    private String descripcion;
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
    @OneToMany(mappedBy = "sector")
    private List<MovimientoProduccion> movimientoProduccionList;

    public SectorProduccion() {
    }

    public SectorProduccion(String codigo) {
        this.codigo = codigo;
    }

    public SectorProduccion(String codigo, String debaja) {
        this.codigo = codigo;
        this.debaja = debaja;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SectorProduccion)) {
            return false;
        }
        SectorProduccion other = (SectorProduccion) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bs.produccion.modelo.SectorProduccion[ codigo=" + codigo + " ]";
    }
    
}
