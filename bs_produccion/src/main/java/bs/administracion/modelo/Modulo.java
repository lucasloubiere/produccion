/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.administracion.modelo;

import bs.global.modelo.Auditoria;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Claudio
 */
@Entity
@Table(name = "gr_modulos")
public class Modulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo", length = 2)
    private String codigo;
    @Column(name = "DESCRIPCION", length = 60)
    private String descripcion;
    @Column(name = "habDes")
    @Temporal(TemporalType.DATE)
    private Date fechahabilitacionDesde;
    @Column(name = "habhAS")
    @Temporal(TemporalType.DATE)
    private Date fechahabilitacionHasta;
    
    @Embedded
    private Auditoria auditoria;

    public Modulo() {
        this.auditoria = new Auditoria();
    }

    public Modulo(String codigo) {
        this.codigo = codigo;
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

    public Date getFechahabilitacionDesde() {
        return fechahabilitacionDesde;
    }

    public void setFechahabilitacionDesde(Date fechahabilitacionDesde) {
        this.fechahabilitacionDesde = fechahabilitacionDesde;
    }

    public Date getFechahabilitacionHasta() {
        return fechahabilitacionHasta;
    }

    public void setFechahabilitacionHasta(Date fechahabilitacionHasta) {
        this.fechahabilitacionHasta = fechahabilitacionHasta;
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
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Modulo)) {
            return false;
        }
        Modulo other = (Modulo) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "isd.global.modelo.lModulos[codigo=" + codigo + "]";
    }

}
