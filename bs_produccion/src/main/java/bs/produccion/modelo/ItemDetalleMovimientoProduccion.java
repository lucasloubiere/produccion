/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.produccion.modelo;

import bs.global.auditoria.AuditoriaListener;
import bs.global.auditoria.IAuditableEntity;
import bs.global.modelo.Auditoria;
import bs.stock.modelo.Deposito;
import bs.stock.modelo.ItemDetalleModelo;
import bs.stock.modelo.Producto;
import bs.stock.modelo.UnidadMedida;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Claudio
 */
@Entity
@Table(name = "pd_movimiento_item_detalle")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipitm", discriminatorType = DiscriminatorType.STRING, length = 1)
@EntityListeners(AuditoriaListener.class)
public abstract class ItemDetalleMovimientoProduccion implements Serializable, IAuditableEntity, ItemDetalleModelo  {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @Column(name = "nroitm", nullable = false)
    private int nroitm;    
    
    @JoinColumn(name = "artcod", referencedColumnName = "codigo", nullable = false)    
    @ManyToOne(fetch = FetchType.LAZY)
    private Producto producto;
    
    @Column(name = "descrp", length = 200)
    private String descripcion;
    
    //Deposito
    @JoinColumn(name = "deposi", referencedColumnName = "codigo", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Deposito deposito;
    
    @Column(name = "cantid", precision = 15, scale = 4)    
    private BigDecimal cantidad;
    
    @JoinColumn(name = "unimed", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadMedida unidadMedida;
    
    @Column(name = "natri1", length = 30)
    private String atributo1;
    
    @Column(name = "natri2", length = 30)
    private String atributo2;
    
    @Column(name = "natri3", length = 30)
    private String atributo3;
    
    @Column(name = "natri4", length = 30)
    private String atributo4;
    
    @Column(name = "natri5", length = 30)
    private String atributo5;
    
    @Column(name = "natri6", length = 30)
    private String atributo6;
    
    @Column(name = "natri7", length = 30)
    private String atributo7;
        
    @Embedded
    private Auditoria auditoria;

    public ItemDetalleMovimientoProduccion() {
        
        cantidad = BigDecimal.ZERO;                        
        
        this.atributo1 = "";
        this.atributo2 = "";
        this.atributo3 = "";
        this.atributo4 = "";
        this.atributo5 = "";
        this.atributo6 = "";
        this.atributo7 = "";        
        
        auditoria = new Auditoria();
    }
    
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int getNroitm() {
        return nroitm;
    }

    @Override
    public void setNroitm(int nroitm) {
        this.nroitm = nroitm;
    }
    
    @Override
    public Producto getProducto() {
        return producto;
    }

    @Override
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String getAtributo1() {
        return atributo1;
    }

    @Override
    public void setAtributo1(String atributo1) {
        this.atributo1 = atributo1;
    }

    @Override
    public String getAtributo2() {
        return atributo2;
    }

    @Override
    public void setAtributo2(String atributo2) {
        this.atributo2 = atributo2;
    }

    @Override
    public String getAtributo3() {
        return atributo3;
    }

    @Override
    public void setAtributo3(String atributo3) {
        this.atributo3 = atributo3;
    }

    @Override
    public String getAtributo4() {
        return atributo4;
    }

    @Override
    public void setAtributo4(String atributo4) {
        this.atributo4 = atributo4;
    }

    @Override
    public String getAtributo5() {
        return atributo5;
    }

    @Override
    public void setAtributo5(String atributo5) {
        this.atributo5 = atributo5;
    }

    @Override
    public String getAtributo6() {
        return atributo6;
    }

    @Override
    public void setAtributo6(String atributo6) {
        this.atributo6 = atributo6;
    }

    @Override
    public String getAtributo7() {
        return atributo7;
    }

    @Override
    public void setAtributo7(String atributo7) {
        this.atributo7 = atributo7;
    }

    @Override
    public Deposito getDeposito() {
        return deposito;
    }

    @Override
    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    @Override
    public BigDecimal getCantidad() {
        return cantidad;
    }

    @Override
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    @Override
    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    @Override
    public Auditoria getAuditoria() {
        return auditoria;
    }

    @Override
    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final ItemDetalleMovimientoProduccion other = (ItemDetalleMovimientoProduccion) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "DetalleItemMovimientoProduccion{" + "id=" + this.id  + '}';
    }
   
}
