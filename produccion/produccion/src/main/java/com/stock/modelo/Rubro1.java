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
@Table(name = "st_rubro1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rubro1.findAll", query = "SELECT r FROM Rubro1 r"),
    @NamedQuery(name = "Rubro1.findByCodigo", query = "SELECT r FROM Rubro1 r WHERE r.rubro1PK.codigo = :codigo"),
    @NamedQuery(name = "Rubro1.findByTipoProducto", query = "SELECT r FROM Rubro1 r WHERE r.rubro1PK.tipoProducto = :tipoProducto"),
    @NamedQuery(name = "Rubro1.findByDescripcion", query = "SELECT r FROM Rubro1 r WHERE r.descripcion = :descripcion")})
public class Rubro1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected Rubro1PK rubro1PK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "descripcion", nullable = false, length = 80)
    private String descripcion;

    @JoinColumn(name = "tipoProducto", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TipoProducto tipoProducto1;

    public Rubro1() {
    }

    public Rubro1(Rubro1PK rubro1PK) {
        this.rubro1PK = rubro1PK;
    }

    public Rubro1(Rubro1PK rubro1PK, String descripcion) {
        this.rubro1PK = rubro1PK;
        this.descripcion = descripcion;
    }

    public Rubro1(String codigo, String tipoProducto) {
        this.rubro1PK = new Rubro1PK(codigo, tipoProducto);
    }

    public Rubro1PK getRubro1PK() {
        return rubro1PK;
    }

    public void setRubro1PK(Rubro1PK rubro1PK) {
        this.rubro1PK = rubro1PK;
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
        hash += (rubro1PK != null ? rubro1PK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rubro1)) {
            return false;
        }
        Rubro1 other = (Rubro1) object;
        if ((this.rubro1PK == null && other.rubro1PK != null) || (this.rubro1PK != null && !this.rubro1PK.equals(other.rubro1PK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.Rubro1[ rubro1PK=" + rubro1PK + " ]";
    }
    
}
