/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lloubiere
 */
@Entity
@Table(name = "pd_operario")
@XmlRootElement
public class Operario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "codigo", nullable = false, length = 6)
    private String codigo;
    @Size(max = 8)
    @Column(name = "legajo", length = 8)
    private String legajo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;   
    
    @JoinColumn(name = "codpues", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(optional = false)
    private OperarioPuesto puesto;

    public Operario() {
    }

    public Operario(String codigo) {
        this.codigo = codigo;
    }

    public Operario(String codigo, String nombre, String debaja) {
        this.codigo = codigo;
        this.nombre = nombre;
        
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(object instanceof Operario)) {
            return false;
        }
        Operario other = (Operario) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bs.produccion.modelo.Operario[ codigo=" + codigo + " ]";
    }
    
}
