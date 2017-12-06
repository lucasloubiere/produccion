/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lloubiere
 */
@Entity
@Table(name = "pd_operario_puesto", catalog = "produccion-ds", schema = "")
@XmlRootElement

public class OperarioPuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "codigo", nullable = false, length = 6)
    private String codigo;
    @Size(max = 100)
    @Column(name = "descripcion", length = 100)
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "valorhora", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorhora;
    
    public OperarioPuesto() {
    }

    public OperarioPuesto(String codigo) {
        this.codigo = codigo;
    }

    public OperarioPuesto(String codigo, BigDecimal valorhora, String debaja) {
        this.codigo = codigo;
        this.valorhora = valorhora;
        
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

    public BigDecimal getValorhora() {
        return valorhora;
    }

    public void setValorhora(BigDecimal valorhora) {
        this.valorhora = valorhora;
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
        if (!(object instanceof OperarioPuesto)) {
            return false;
        }
        OperarioPuesto other = (OperarioPuesto) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bs.produccion.modelo.OperarioPuesto[ codigo=" + codigo + " ]";
    }
    
}
