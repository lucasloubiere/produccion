/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.modelo;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author lloubiere
 */


@Embeddable
public class ComposicionFormulaPK implements Serializable {

    private String artcod;
    private String codfor;

    public ComposicionFormulaPK() {
    }

    public ComposicionFormulaPK(String artcod, String codfor) {
        this.artcod = artcod;
        this.codfor = codfor;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (artcod != null ? artcod.hashCode() : 0);
        hash += (codfor != null ? codfor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComposicionFormulaPK)) {
            return false;
        }
        ComposicionFormulaPK other = (ComposicionFormulaPK) object;
        if ((this.artcod == null && other.artcod != null) || (this.artcod != null && !this.artcod.equals(other.artcod))) {
            return false;
        }
        if ((this.codfor == null && other.codfor != null) || (this.codfor != null && !this.codfor.equals(other.codfor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.ComposicionFormulaPK[ artcod=" + artcod + ", codfor=" + codfor + " ]";
    }
    
}
