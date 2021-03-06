/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.modelo;

import bs.global.modelo.Auditoria;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lloubiere
 */

@Entity
@Table(name = "st_movimiento_item")
@XmlRootElement
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPITM", discriminatorType = DiscriminatorType.STRING, length = 10)
public abstract class ItemMovimientoStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "idcab", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MovimientoStock movimiento;
    
    @JoinColumn(name = "artcod", referencedColumnName = "codigo")
    @ManyToOne
    private Producto producto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    
    @Column(name = "cantid")
    private BigDecimal cantidad;
        
    @Basic(optional = false)
    @NotNull
    @Column(name = "stock")
    private BigDecimal stocks;

    @JoinColumn(name = "unimed", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadMedida unidadMedida;

    @JoinColumn(name = "deposi", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Deposito deposito;

    //@Size(min = 1, max = 50)
    @Column(name = "natri1")
    private String atributo1;
    
    //@Size(min = 1, max = 50)
    @Column(name = "natri2")
    private String atributo2;
    
    //@Size(min = 1, max = 50)
    @Column(name = "natri3")
    private String atributo3;
    
//    @Size(min = 1, max = 50)
    @Column(name = "natri4")
    private String atributo4;
    
    //@Size(min = 1, max = 50)
    @Column(name = "natri5")
    private String atributo5;
    
    //@Size(min = 1, max = 50)
    @Column(name = "natri6")
    private String atributo6;
    
    //@Size(min = 1, max = 50)
    @Column(name = "natri7")
    private String atributo7;
    
    @Column(name = "fchmov")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMovimiento;
    
    @Column(name = "PRECIO", precision = 15, scale = 4)
    private BigDecimal precio;
    @Column(name = "PRESEC", precision = 15, scale = 4)
    private BigDecimal precioSecundario;
    
    @Lob
    @Column(name = "OBSERV", length = 65535)
    private String observaciones;

    @Embedded
    private Auditoria auditoria;
    
    @Transient
    private BigDecimal saldoInicial;

    @Transient
    private boolean todoOk;

    public ItemMovimientoStock() {
        
//        this.cantidad = BigDecimal.ZERO;
        this.auditoria = new Auditoria();
    }

    public ItemMovimientoStock(Integer id) {
        
        this.id = id;
//        this.cantidad = BigDecimal.ZERO;
        this.auditoria = new Auditoria();
    }

    public ItemMovimientoStock(Integer id, int idcab, String artcod, BigDecimal cantidad, String natri1, String natri2, String natri3) {
        
        this.id = id;
//        this.cantidad = cantidad;
        this.atributo1 = natri1;
        this.atributo2 = natri2;
        this.atributo3 = natri3;        
        this.auditoria = new Auditoria();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MovimientoStock getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoStock movimiento) {
        this.movimiento = movimiento;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
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

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
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

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }
    
    public boolean isTodoOk() {
        return todoOk;
    }

    public void setTodoOk(boolean todoOk) {
        this.todoOk = todoOk;
    }

    public BigDecimal getStocks() {
        return stocks;
    }

    public void setStocks(BigDecimal stocks) {
        this.stocks = stocks;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }
    
    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getPrecioSecundario() {
        return precioSecundario;
    }

    public void setPrecioSecundario(BigDecimal precioSecundario) {
        this.precioSecundario = precioSecundario;
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
        if (!(object instanceof ItemMovimientoStock)) {
            return false;
        }
        ItemMovimientoStock other = (ItemMovimientoStock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.MovimientoItem[ id=" + id + " ]";
    }

}
