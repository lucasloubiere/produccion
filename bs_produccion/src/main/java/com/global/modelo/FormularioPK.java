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

    private String modulo;
    private String codigo;

    public FormularioPK() {
    }

    public FormularioPK(String modulo, String codigo) {
        this.modulo = modulo;
        this.codigo = codigo;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
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
        hash += (modulo != null ? modulo.hashCode() : 0);
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
        if ((this.modulo == null && other.modulo != null) || (this.modulo != null && !this.modulo.equals(other.modulo))) {
            return false;
        }
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.FormularioPK[ modulo=" + modulo + ", codigo=" + codigo + " ]";
    }
    
}
