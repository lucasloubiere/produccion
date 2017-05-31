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
 * @author ide
 */
@Embeddable
public class Rubro1PK implements Serializable {

    private String codigo;
    private String tipoProducto;

    public Rubro1PK() {
    }

    public Rubro1PK(String tipoProducto,String codigo) {
        this.codigo = codigo;
        this.tipoProducto = tipoProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        hash += (tipoProducto != null ? tipoProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rubro1PK)) {
            return false;
        }
        Rubro1PK other = (Rubro1PK) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        if ((this.tipoProducto == null && other.tipoProducto != null) || (this.tipoProducto != null && !this.tipoProducto.equals(other.tipoProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.Rubro1PK[ codigo=" + codigo + ", tipoProducto=" + tipoProducto + " ]";
    }
    
}
