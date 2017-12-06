/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author lloubiere
 */
@Embeddable
public class CircuitoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "circom", nullable = false, length = 6)
    private String circom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "cirapl", nullable = false, length = 6)
    private String cirapl;

    public CircuitoPK() {
    }

    public CircuitoPK(String circom, String cirapl) {
        this.circom = circom;
        this.cirapl = cirapl;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (circom != null ? circom.hashCode() : 0);
        hash += (cirapl != null ? cirapl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CircuitoPK)) {
            return false;
        }
        CircuitoPK other = (CircuitoPK) object;
        if ((this.circom == null && other.circom != null) || (this.circom != null && !this.circom.equals(other.circom))) {
            return false;
        }
        if ((this.cirapl == null && other.cirapl != null) || (this.cirapl != null && !this.cirapl.equals(other.cirapl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bs.produccion.modelo.CircuitoPK[ circom=" + circom + ", cirapl=" + cirapl + " ]";
    }
    
}
