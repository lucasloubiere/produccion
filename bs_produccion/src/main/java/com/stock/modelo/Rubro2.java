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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ide
 */
@Entity
@Table(name = "st_rubro2")
@XmlRootElement
@IdClass(Rubro2PK.class)
public class Rubro2 implements Serializable {

    
    @Id
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "codigo", nullable = false, length = 6)
    private String codigo;
    
    @Id
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "tipoProducto", nullable = false, length = 6)
    private String tipoProducto;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "descripcion", nullable = false, length = 80)
    private String descripcion;
   
  
   
    @Embedded
    private Auditoria auditoria;


    public Rubro2() {
        this.auditoria = new Auditoria();
    }

    public Rubro2(Rubro2PK rubro2PK) {
        this.auditoria = new Auditoria();        
    }
    
    public Rubro2(String codigo, String tipoProducto) {
        this.auditoria = new Auditoria();
        this.codigo = codigo;
        this.tipoProducto = tipoProducto;
        }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
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
        int hash = 7;
        hash = 47 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        hash = 47 * hash + (this.tipoProducto != null ? this.tipoProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rubro2 other = (Rubro2) obj;
        if ((this.codigo == null) ? (other.codigo != null) : !this.codigo.equals(other.codigo)) {
            return false;
        }
        if ((this.tipoProducto == null) ? (other.tipoProducto != null) : !this.tipoProducto.equals(other.tipoProducto)) {
            return false;
        }
        return true;
    }

    
    
  

   @Override
    public String toString() {
        return "tipoProducto=" + tipoProducto+",codigo=" + codigo;
    }

}
