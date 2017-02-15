/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ide
 */
@Entity
@Table(name = "st_unidaddemedida")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadDeMedida.findAll", query = "SELECT u FROM UnidadDeMedida u"),
    @NamedQuery(name = "UnidadDeMedida.findByCodigo", query = "SELECT u FROM UnidadDeMedida u WHERE u.codigo = :codigo"),
    @NamedQuery(name = "UnidadDeMedida.findByDescripcion", query = "SELECT u FROM UnidadDeMedida u WHERE u.descripcion = :descripcion"),
    @NamedQuery(name = "UnidadDeMedida.findByFormato", query = "SELECT u FROM UnidadDeMedida u WHERE u.formato = :formato")})
public class UnidadDeMedida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "codigo", nullable = false, length = 6)
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion", nullable = false, length = 45)
    private String descripcion;
    @Size(max = 45)
    @Column(name = "Formato", length = 45)
    private String formato;


    public UnidadDeMedida() {
    }

    public UnidadDeMedida(String codigo) {
        this.codigo = codigo;
    }

    public UnidadDeMedida(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
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

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
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
        if (!(object instanceof UnidadDeMedida)) {
            return false;
        }
        UnidadDeMedida other = (UnidadDeMedida) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.UnidadDeMedida[ codigo=" + codigo + " ]";
    }
    
}