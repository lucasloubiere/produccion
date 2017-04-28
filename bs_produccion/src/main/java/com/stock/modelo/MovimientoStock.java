/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.modelo;

import com.global.modelo.Auditoria;
import com.global.modelo.Formulario;
import com.global.modelo.Sucursal;
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
import javax.validation.constraints.Size;
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
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private int numeroFormulario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "observaciones")
    private String observaciones;

    @JoinColumn(name = "codsuc", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Sucursal sucursal;
    @JoinColumns({
        @JoinColumn(name = "modulo", referencedColumnName = "modulo"),
        @JoinColumn(name = "codfor", referencedColumnName = "codigo")})
    @ManyToOne(optional = false)
    private Formulario formulario;
    
    /**
     * Tipo de movimiento
     * A = Ajuste
     * I = Ingreso
     * E = Egreso
     * T = Transferencia
     */    
    @Column(name = "tipmov", length = 1,nullable = false)
    private String tipoMovimiento;
        
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
            
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento", fetch=FetchType.LAZY)
    private List<ItemProductoStock> itemsProducto;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento", fetch=FetchType.LAZY)
    private List<ItemTransferenciaStock> itemTransferencia; 
    
    @Embedded
    private Auditoria auditoria;
    
    @Transient 
    private boolean persistido;

    public MovimientoStock() {
        this.auditoria = new Auditoria();
    }

    public MovimientoStock(Integer id) {
        this.id = id;
        this.auditoria = new Auditoria();
    }

    public MovimientoStock(Integer id, int numero, Date fecha, String observaciones, String debaja) {
        this.id = id;
        this.numeroFormulario = numero;
        this.fecha = fecha;
        this.observaciones = observaciones;
        this.auditoria = new Auditoria();
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

    

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
