/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.modelo;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author lloubiere
 */
@Embeddable
public class FormularioPK implements Serializable {

    private String modfor;
    private String codigo;

    public FormularioPK() {
    }

    public FormularioPK(String modulo, String codigo) {
        this.modfor = modulo;
        this.codigo = codigo;
    }

    public String getModfor() {
        return modfor;
    }

    public void setModfor(String modfor) {
        this.modfor = modfor;
    }
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modfor != null ? modfor.hashCode() : 0);
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FormularioPK)) {
            return false;
        }
        FormularioPK other = (FormularioPK) object;
        if ((this.modfor == null && other.modfor != null) || (this.modfor != null && !this.modfor.equals(other.modfor))) {
            return false;
        }
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.FormularioPK[ modfor=" + modfor + ", codigo=" + codigo + " ]";
    }
    
}
