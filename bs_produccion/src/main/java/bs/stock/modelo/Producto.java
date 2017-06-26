/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.modelo;

import bs.global.modelo.Auditoria;
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

    @JoinColumn(name = "unimed", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(optional = false)
    private UnidadMedida unidadDeMedida;
  
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
    
    @Column(name = "adatr1", length = 1)
    private String administraAtributo1;
      
    @Column(name = "adatr2", length = 1)
    private String administraAtributo2;
    //Numero de envase
    @Column(name = "adatr3", length = 1)
    private String administraAtributo3;
    //Numero otros
    @Column(name = "adatr4", length = 1)
    private String administraAtributo4;
    //Numero de atributo
    @Column(name = "adatr5", length = 1)
    private String administraAtributo5;
    //Numero de estante
    @Column(name = "adatr6", length = 1)
    private String administraAtributo6;
    
    @Column(name = "adatr7", length = 1)
    private String administraAtributo7;
    
    @Column(name = "codref", length = 20)
    private String codigoReferencia;
    
    @Embedded
    private Auditoria auditoria;

    public Producto() {
        this.auditoria = new Auditoria();
        administraAtributo1= "N";
        administraAtributo2= "N";
        administraAtributo3= "N";
        administraAtributo4= "N";
        administraAtributo5= "N";
        administraAtributo6= "N";
        administraAtributo7= "N"; 
    }

    public Producto(String codigo) {
        this.auditoria = new Auditoria();
        this.codigo = codigo;
        administraAtributo1= "N";
        administraAtributo2= "N";
        administraAtributo3= "N";
        administraAtributo4= "N";
        administraAtributo5= "N";
        administraAtributo6= "N";
        administraAtributo7= "N"; 
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

    public UnidadMedida getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(UnidadMedida unidadDeMedida) {
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

    public String getAdministraAtributo1() {
        return administraAtributo1;
    }

    public void setAdministraAtributo1(String administraAtributo1) {
        this.administraAtributo1 = administraAtributo1;
    }

    public String getAdministraAtributo2() {
        return administraAtributo2;
    }

    public void setAdministraAtributo2(String administraAtributo2) {
        this.administraAtributo2 = administraAtributo2;
    }

    public String getAdministraAtributo3() {
        return administraAtributo3;
    }

    public void setAdministraAtributo3(String administraAtributo3) {
        this.administraAtributo3 = administraAtributo3;
    }

    public String getAdministraAtributo4() {
        return administraAtributo4;
    }

    public void setAdministraAtributo4(String administraAtributo4) {
        this.administraAtributo4 = administraAtributo4;
    }

    public String getAdministraAtributo5() {
        return administraAtributo5;
    }

    public void setAdministraAtributo5(String administraAtributo5) {
        this.administraAtributo5 = administraAtributo5;
    }

    public String getAdministraAtributo6() {
        return administraAtributo6;
    }

    public void setAdministraAtributo6(String administraAtributo6) {
        this.administraAtributo6 = administraAtributo6;
    }

    public String getAdministraAtributo7() {
        return administraAtributo7;
    }

    public void setAdministraAtributo7(String administraAtributo7) {
        this.administraAtributo7 = administraAtributo7;
    }
    
     /**
     * @return the codigoReferencia
     */
    public String getCodigoReferencia() {
        return codigoReferencia;
    }

    /**
     * @param codigoReferencia the codigoReferencia to set
     */
    public void setCodigoReferencia(String codigoReferencia) {
        this.codigoReferencia = codigoReferencia;
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
