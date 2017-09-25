/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.modelo;

import bs.global.modelo.Auditoria;
import bs.global.modelo.Formulario;
import bs.global.modelo.Sucursal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "st_movimiento")
@XmlRootElement
public class MovimientoStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column(name = "id", nullable = false)
    private Integer id;
    /**
     * Comprobante de stock
     */
    @JoinColumns({
        @JoinColumn(name = "modcom", referencedColumnName = "MODCOM", nullable = false),
        @JoinColumn(name = "codcom", referencedColumnName = "CODCOM", nullable = false)
    })
    @ManyToOne(fetch = FetchType.LAZY)
    ComprobanteStock comprobante;

    @JoinColumns({
        @JoinColumn(name = "modfor", referencedColumnName = "modfor"),
        @JoinColumn(name = "codfor", referencedColumnName = "codfor")})
    @ManyToOne(optional = false)
    private Formulario formulario;

    @JoinColumn(name = "sucurs", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Sucursal sucursal;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nrofor")
    private int numeroFormulario;
        
    @NotNull
    @Column(name = "fchmov")
    @Temporal(TemporalType.TIMESTAMP )
    private Date fechaMovimiento;
        
    @Lob
//    @Size(min = 1, max = 65535)
    @Column(name = "observ")
    private String observaciones;

    
    /**
     * Tipo de movimiento A = Ajuste I = Ingreso E = Egreso T = Transferencia
     */
    @Column(name = "tipmov", length = 1, nullable = false)
    private String tipoMovimiento;
    
    
     @Column(name = "comprob", length = 50)
    private String comprobanteBalanza;
          
     @Column(name = "carta_porte", length = 50)
    private String cartaPorte;
     
     /**
     * Deposito ingreso
     */
    @JoinColumn(name = "deposi", referencedColumnName = "CODIGO")
    @ManyToOne(fetch = FetchType.LAZY)
    private Deposito deposito;

    /**
     * Deposito egreso
     */
    @JoinColumn(name = "deptra", referencedColumnName = "CODIGO")
    @ManyToOne(fetch = FetchType.LAZY)
    private Deposito depositoTransferencia;

    @Column(name = "isanul", length = 1)
    private String esAnulacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mrev", referencedColumnName = "ID")
    private MovimientoStock movimientoReversion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento", fetch = FetchType.LAZY)
    private List<ItemProductoStock> itemsProducto;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento", fetch = FetchType.LAZY)
    private List<ItemTransferenciaStock> itemTransferencia;

    @Embedded
    private Auditoria auditoria;

    @Transient
    private boolean persistido;
    @Transient
    private boolean noSincronizaNumeroFormulario;
    @Transient
    private boolean noValidaStockDisponible;

    
    @Transient
    private String atributo1;
    @Transient
    private String atributo2;
    @Transient
    private String atributo3;
    @Transient
    private String atributo4;
    @Transient
    private String atributo5;
    @Transient
    private String atributo6;
    @Transient
    private String atributo7;

    public MovimientoStock() {
        this.auditoria = new Auditoria();
        this.esAnulacion = "N";
        this.noSincronizaNumeroFormulario = false;
    }

    public MovimientoStock(Integer id) {
        this.id = id;
        this.auditoria = new Auditoria();
        this.esAnulacion = "N";
        this.noSincronizaNumeroFormulario = false;
    }

    public MovimientoStock(Integer id, int numero, Date fecha, String observaciones, String debaja) {
        this.id = id;
        this.numeroFormulario = numero;
        this.fechaMovimiento = fecha;
        this.observaciones = observaciones;
        this.auditoria = new Auditoria();
        this.esAnulacion = "N";
        this.noSincronizaNumeroFormulario = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNumeroFormulario() {
        return numeroFormulario;
    }

    public void setNumeroFormulario(int numeroFormulario) {
        this.numeroFormulario = numeroFormulario;
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

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public Deposito getDepositoTransferencia() {
        return depositoTransferencia;
    }

    public void setDepositoTransferencia(Deposito depositoTransferencia) {
        this.depositoTransferencia = depositoTransferencia;
    }

    public String getEsAnulacion() {
        return esAnulacion;
    }

    public void setEsAnulacion(String esAnulacion) {
        this.esAnulacion = esAnulacion;
    }

    public MovimientoStock getMovimientoReversion() {
        return movimientoReversion;
    }

    public void setMovimientoReversion(MovimientoStock movimientoReversion) {
        this.movimientoReversion = movimientoReversion;
    }

    public List<ItemProductoStock> getItemsProducto() {
        return itemsProducto;
    }

    public void setItemsProducto(List<ItemProductoStock> itemsProducto) {
        this.itemsProducto = itemsProducto;
    }

    public List<ItemTransferenciaStock> getItemTransferencia() {
        return itemTransferencia;
    }

    public void setItemTransferencia(List<ItemTransferenciaStock> itemTransferencia) {
        this.itemTransferencia = itemTransferencia;
    }

    public boolean isPersistido() {
        return persistido;
    }

    public void setPersistido(boolean persistido) {
        this.persistido = persistido;
    }

    public ComprobanteStock getComprobante() {
        return comprobante;
    }

    public void setComprobante(ComprobanteStock comprobante) {
        this.comprobante = comprobante;
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

    public String getComprobanteBalanza() {
        return comprobanteBalanza;
    }

    public void setComprobanteBalanza(String comprobanteBalanza) {
        this.comprobanteBalanza = comprobanteBalanza;
    }

    public String getCartaPorte() {
        return cartaPorte;
    }

    public void setCartaPorte(String cartaPorte) {
        this.cartaPorte = cartaPorte;
    }

    public boolean isNoSincronizaNumeroFormulario() {
        return noSincronizaNumeroFormulario;
    }

    public void setNoSincronizaNumeroFormulario(boolean noSincronizaNumeroFormulario) {
        this.noSincronizaNumeroFormulario = noSincronizaNumeroFormulario;
    }

    public boolean isNoValidaStockDisponible() {
        return noValidaStockDisponible;
    }

    public void setNoValidaStockDisponible(boolean noValidaStockDisponible) {
        this.noValidaStockDisponible = noValidaStockDisponible;
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
        if (!(object instanceof MovimientoStock)) {
            return false;
        }
        MovimientoStock other = (MovimientoStock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.Movimiento[ id=" + id + " ]";
    }

}
