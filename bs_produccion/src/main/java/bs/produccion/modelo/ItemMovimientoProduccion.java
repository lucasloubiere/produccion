/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.modelo;

import bs.global.modelo.Auditoria;
import bs.stock.modelo.ComposicionFormula;
import bs.stock.modelo.Producto;
import bs.stock.modelo.UnidadMedida;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "pd_movimiento_item")
@XmlRootElement
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipitm", discriminatorType = DiscriminatorType.STRING, length = 10)
public abstract class ItemMovimientoProduccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantid", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
    @Size(max = 50)
    @Column(name = "natri1", length = 50)
    private String atributo1;
    @Size(max = 50)
    @Column(name = "natri2", length = 50)
    private String atributo2;
    @Size(max = 50)
    @Column(name = "natri3", length = 50)
    private String atributo3;
    @Size(max = 50)
    @Column(name = "natri4", length = 50)
    private String atributo4;
    @Size(max = 50)
    @Column(name = "natri5", length = 50)
    private String atributo5;
    @Size(max = 50)
    @Column(name = "natri6", length = 50)
    private String atributo6;
    @Size(max = 50)
    @Column(name = "natri7", length = 50)
    private String atributo7;
    
    @JoinColumn(name = "unimed", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(optional = false)
    private UnidadMedida unidadMedida;
    @JoinColumn(name = "artcod", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(optional = false)
    private Producto producto;
    @JoinColumns({
        @JoinColumn(name = "artcod", referencedColumnName = "artcod", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "codfor", referencedColumnName = "codfor", nullable = false)})
    @ManyToOne(optional = false)
    private ComposicionFormula composicionFormula;
    @JoinColumn(name = "codoper", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(optional = false)
    private Operario operario;
    @JoinColumn(name = "idcab", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private MovimientoProduccion movimiento;

    @Embedded
    private Auditoria auditoria;
    
    public ItemMovimientoProduccion() {
    this.auditoria = new Auditoria();
    }

    public ItemMovimientoProduccion(Integer id) {
        this.id = id;
        this.auditoria = new Auditoria();
    }

    public ItemMovimientoProduccion(Integer id, String tipitm, BigDecimal cantid, BigDecimal precio, String debaja) {
        this.id = id;        
        this.cantid = cantid;
        this.precio = precio;
        this.auditoria = new Auditoria();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getCantid() {
        return cantid;
    }

    public void setCantid(BigDecimal cantid) {
        this.cantid = cantid;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

   
    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    public String getAtributo1() {
        return atributo1;
    }

    public void setAtributo1(String atributo1) {
        this.atributo1 = atributo1;
    }

    public String getAtributo2() {
        return atributo2;
    }

    public void setAtributo2(String atributo2) {
        this.atributo2 = atributo2;
    }

    public String getAtributo3() {
        return atributo3;
    }

    public void setAtributo3(String atributo3) {
        this.atributo3 = atributo3;
    }

    public String getAtributo4() {
        return atributo4;
    }

    public void setAtributo4(String atributo4) {
        this.atributo4 = atributo4;
    }

    public String getAtributo5() {
        return atributo5;
    }

    public void setAtributo5(String atributo5) {
        this.atributo5 = atributo5;
    }

    public String getAtributo6() {
        return atributo6;
    }

    public void setAtributo6(String atributo6) {
        this.atributo6 = atributo6;
    }

    public String getAtributo7() {
        return atributo7;
    }

    public void setAtributo7(String atributo7) {
        this.atributo7 = atributo7;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Operario getOperario() {
        return operario;
    }

    public void setOperario(Operario operario) {
        this.operario = operario;
    }    

    public MovimientoProduccion getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoProduccion movimiento) {
        this.movimiento = movimiento;
    }
 
    public ComposicionFormula getComposicionFormula() {
        return composicionFormula;
    }

    public void setComposicionFormula(ComposicionFormula composicionFormula) {
        this.composicionFormula = composicionFormula;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemMovimientoProduccion)) {
            return false;
        }
        ItemMovimientoProduccion other = (ItemMovimientoProduccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bs.produccion.modelo.ItemMovimientoProduccion[ id=" + id + " ]";
    }
    
}
