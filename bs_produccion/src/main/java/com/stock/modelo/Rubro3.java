/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.modelo;

import com.global.modelo.Auditoria;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ide
 */
@Entity
@Table(name = "st_rubro3")
@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "Rubro3.findAll", query = "SELECT r FROM Rubro3 r"),
//    @NamedQuery(name = "Rubro3.findByCodigo", query = "SELECT r FROM Rubro3 r WHERE r.rubro3PK.codigo = :codigo"),
//    @NamedQuery(name = "Rubro3.findByTipoProducto", query = "SELECT r FROM Rubro3 r WHERE r.rubro3PK.tipoProducto = :tipoProducto"),
//    @NamedQuery(name = "Rubro3.findByDescripcion", query = "SELECT r FROM Rubro3 r WHERE r.descripcion = :descripcion")})
public class Rubro3 implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected Rubro3PK rubro3PK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "descripcion", nullable = false, length = 80)
    private String descripcion;
    @JoinColumn(name = "tipoProducto", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TipoProducto tipoProducto1;
    @Embedded
    private Auditoria auditoria;


    public Rubro3() {
        this.auditoria = new Auditoria();
    }

    public Rubro3(Rubro3PK rubro3PK) {
        this.rubro3PK = rubro3PK;
        this.auditoria = new Auditoria();
    }

    public Rubro3(Rubro3PK rubro3PK, String descripcion) {
        this.auditoria = new Auditoria();
        this.rubro3PK = rubro3PK;
        this.descripcion = descripcion;
    }

    public Rubro3(String codigo, String tipoProducto) {
        this.auditoria = new Auditoria();
        this.rubro3PK = new Rubro3PK(codigo, tipoProducto);
    }

    public Rubro3PK getRubro3PK() {
        return rubro3PK;
    }

    public void setRubro3PK(Rubro3PK rubro3PK) {
        this.rubro3PK = rubro3PK;
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

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rubro3PK != null ? rubro3PK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rubro3)) {
            return false;
        }
        Rubro3 other = (Rubro3) object;
        if ((this.rubro3PK == null && other.rubro3PK != null) || (this.rubro3PK != null && !this.rubro3PK.equals(other.rubro3PK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.Rubro3[ rubro3PK=" + rubro3PK + " ]";
    }
    
}
