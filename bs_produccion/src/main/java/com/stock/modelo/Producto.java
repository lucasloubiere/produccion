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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "st_producto")
@XmlRootElement
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codigo", nullable = false, length = 20)
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "gestionaStock", nullable = false, length = 1)
    private String gestionaStock;
    @Size(max = 30)
    @Column(name = "codigoDeBarra", length = 30)
    private String codigoDeBarra;

    @JoinColumn(name = "unidadDeMedida", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(optional = false)
    private UnidadDeMedida unidadDeMedida;
  
    @JoinColumn(name = "tipoProducto", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(optional = false)
    private TipoProducto tipoProducto;
  
    @JoinColumns({
        @JoinColumn(name = "rubro1", referencedColumnName = "codigo", nullable = false),
        @JoinColumn(name = "tipoProducto", referencedColumnName = "tipoProducto", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Rubro1 rubro1;
    
    @JoinColumns({
        @JoinColumn(name = "rubro2", referencedColumnName = "codigo"),
        @JoinColumn(name = "tipoProducto", referencedColumnName = "tipoProducto", nullable = false,insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Rubro2 rubro2;
    
    @JoinColumns({
        @JoinColumn(name = "rubro3", referencedColumnName = "codigo"),
        @JoinColumn(name = "tipoProducto", referencedColumnName = "tipoProducto", nullable = false,insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Rubro3 rubro3;
    
    @Embedded
    private Auditoria auditoria;

    public Producto() {
        this.auditoria = new Auditoria();
    }

    public Producto(String codigo) {
        this.auditoria = new Auditoria();
        this.codigo = codigo;
    }

    public Producto(String codigo, String descripcion, String gestionaStock) {
        this.auditoria = new Auditoria();
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.gestionaStock = gestionaStock;
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

    public String getGestionaStock() {
        return gestionaStock;
    }

    public void setGestionaStock(String gestionaStock) {
        this.gestionaStock = gestionaStock;
    }

    public String getCodigoDeBarra() {
        return codigoDeBarra;
    }

    public void setCodigoDeBarra(String codigoDeBarra) {
        this.codigoDeBarra = codigoDeBarra;
    }

    public UnidadDeMedida getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(UnidadDeMedida unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
    
    

    public Rubro1 getRubro1() {
        return rubro1;
    }

    public void setRubro1(Rubro1 rubro1) {
        this.rubro1 = rubro1;
    }

    public Rubro2 getRubro2() {
        return rubro2;
    }

    public void setRubro2(Rubro2 rubro2) {
        this.rubro2 = rubro2;
    }

    public Rubro3 getRubro3() {
        return rubro3;
    }

    public void setRubro3(Rubro3 rubro3) {
        this.rubro3 = rubro3;
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
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.Producto[ codigo=" + codigo + " ]";
    }
    
}
