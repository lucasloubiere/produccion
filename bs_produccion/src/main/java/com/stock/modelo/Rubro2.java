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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "st_rubro2")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rubro2.findAll", query = "SELECT r FROM Rubro2 r"),
    @NamedQuery(name = "Rubro2.findByCodigo", query = "SELECT r FROM Rubro2 r WHERE r.rubro2PK.codigo = :codigo"),
    @NamedQuery(name = "Rubro2.findByTipoProducto", query = "SELECT r FROM Rubro2 r WHERE r.rubro2PK.tipoProducto = :tipoProducto"),
    @NamedQuery(name = "Rubro2.findByDescripcion", query = "SELECT r FROM Rubro2 r WHERE r.descripcion = :descripcion")})
public class Rubro2 implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected Rubro2PK rubro2PK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "descripcion", nullable = false, length = 80)
    private String descripcion;
    @JoinColumn(name = "tipoProducto", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TipoProducto tipoProducto1;


    public Rubro2() {
    }

    public Rubro2(Rubro2PK rubro2PK) {
        this.rubro2PK = rubro2PK;
    }

    public Rubro2(Rubro2PK rubro2PK, String descripcion) {
        this.rubro2PK = rubro2PK;
        this.descripcion = descripcion;
    }

    public Rubro2(String codigo, String tipoProducto) {
        this.rubro2PK = new Rubro2PK(codigo, tipoProducto);
    }

    public Rubro2PK getRubro2PK() {
        return rubro2PK;
    }

    public void setRubro2PK(Rubro2PK rubro2PK) {
        this.rubro2PK = rubro2PK;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoProducto getTipoProducto1() {
        return tipoProducto1;
    }

    public void setTipoProducto1(TipoProducto tipoProducto1) {
        this.tipoProducto1 = tipoProducto1;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rubro2PK != null ? rubro2PK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rubro2)) {
            return false;
        }
        Rubro2 other = (Rubro2) object;
        if ((this.rubro2PK == null && other.rubro2PK != null) || (this.rubro2PK != null && !this.rubro2PK.equals(other.rubro2PK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.Rubro2[ rubro2PK=" + rubro2PK + " ]";
    }
    
}
