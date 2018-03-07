/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.modelo;

import bs.global.auditoria.AuditoriaListener;
import bs.global.auditoria.IAuditableEntity;
import bs.global.modelo.Auditoria;
import bs.global.modelo.Comprobante;
import bs.global.modelo.Formulario;
import bs.global.modelo.Moneda;
import bs.global.modelo.Sucursal;
import bs.stock.modelo.ComprobanteStock;
import bs.stock.modelo.Deposito;
import bs.stock.modelo.MovimientoStock;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author ctrosch
 */
@Entity
@Table(name = "pd_movimiento")
@EntityListeners(AuditoriaListener.class)
public class MovimientoProduccion implements Serializable, IAuditableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "estado", length = 1)
    private String estado;

    @JoinColumns({
        @JoinColumn(name = "circom", referencedColumnName = "circom", nullable = false),
        @JoinColumn(name = "cirapl", referencedColumnName = "cirapl", nullable = false)
    })
    @ManyToOne(fetch = FetchType.LAZY)
    private CircuitoProduccion circuito;

    //Comprobante de compra
    @JoinColumns({
        @JoinColumn(name = "modcom", referencedColumnName = "modcom", nullable = false),
        @JoinColumn(name = "codcom", referencedColumnName = "codcom", nullable = false)
    })
    @ManyToOne(fetch = FetchType.LAZY)
    private Comprobante comprobante;

    @JoinColumns({
        @JoinColumn(name = "modfor", referencedColumnName = "modfor", nullable = false),
        @JoinColumn(name = "codfor", referencedColumnName = "codfor", nullable = false)
    })
    @ManyToOne(fetch = FetchType.LAZY)
    private Formulario formulario;

    @Column(name = "nrofor", nullable = false)
    private int numeroFormulario;

    //Sucursal o punto de venta
    @JoinColumn(name = "sucurs", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Sucursal sucursal;

    //Fecha de movimiento
    @Basic(optional = false)
    @Column(name = "fchmov", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaMovimiento;

    //Fecha requerida
    @Column(name = "fchreq", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRequerida;

    //Fecha inicio
    @Column(name = "fchini", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;

    //Fecha inicio
    @Column(name = "fchfin", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;

    //Moneda de registracion
    @JoinColumn(name = "monreg", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Moneda monedaRegistracion;

    //Moneda secundaria
    @JoinColumn(name = "monsec", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Moneda monedaSecundaria;

    @Column(name = "COTIZA", precision = 15, scale = 4)
    private BigDecimal cotizacion;

    @JoinColumn(name = "deposi", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Deposito deposito;

    @JoinColumn(name = "deptra", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Deposito depositoTransferencia;

    //Departamento
    @JoinColumn(name = "deppro", referencedColumnName = "codigo", nullable = false)
    @ManyToOne
    private DepartamentoProduccion departamento;
    
    @JoinColumn(name = "codpla", referencedColumnName = "codigo", nullable = false)
    @ManyToOne
    private Planta planta;

    //Tipo de comprobante
    @Enumerated(EnumType.STRING)
    @Column(name = "tipmov", length = 2)
    private TipoMovimientoProduccion tipoMovimiento;

    //Tipo de comprobante
//    @Enumerated(EnumType.STRING)
    @Column(name = "priori", length = 20)
    private String prioridad;

    //Solicitante
    @Column(name = "solici", length = 40)
    private String solicitante;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_mst", referencedColumnName = "id")
    private MovimientoStock movimientoStock;

    @Lob
    @Column(name = "observ", length = 2147483647)
    private String observaciones;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento", fetch = FetchType.LAZY)
    private List<ItemProductoProduccion> itemsProducto;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento", fetch = FetchType.LAZY)
    private List<ItemComponenteProduccion> itemsComponente;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento", fetch = FetchType.LAZY)
    private List<ItemProcesoProduccion> itemsProceso;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento", fetch = FetchType.LAZY)
    private List<ItemAplicacionProduccion> itemsAplicacion;

    @Embedded
    private Auditoria auditoria;

    @Transient
    private Sucursal sucursalStock;

    @Transient
    private boolean persistido;
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

    @Transient
    private ComprobanteStock comprobanteStock;

    @Transient
    private MovimientoProduccion parteProduccion;
    @Transient
    private MovimientoProduccion valeConsumo;
    @Transient
    private MovimientoProduccion parteProceso;

    @Transient
    private boolean noSincronizaNumeroFormulario;

    public MovimientoProduccion() {
        
        estado = "1";

        fechaMovimiento = new Date();
        fechaRequerida = new Date();

        itemsProducto = new ArrayList<ItemProductoProduccion>();
        itemsComponente = new ArrayList<ItemComponenteProduccion>();
        itemsProceso = new ArrayList<ItemProcesoProduccion>();
        itemsAplicacion = new ArrayList<ItemAplicacionProduccion>();

//        itemsProductoAplicacion = new ArrayList<ItemAplicacionProduccion>();
//        itemsEstructuraAplicacion = new ArrayList<ItemEstructuraProduccionAplicacion>();
//        itemsComponenteAplicacion = new ArrayList<ItemMateriaPrimaAplicacionProduccion>();
        this.auditoria = new Auditoria();
    }

    public MovimientoProduccion(Formulario formulario) {
        
        estado = "1";

        fechaMovimiento = new Date();
        fechaRequerida = new Date();

        this.formulario = formulario;

        itemsProducto = new ArrayList<ItemProductoProduccion>();
        itemsComponente = new ArrayList<ItemComponenteProduccion>();
        itemsProceso = new ArrayList<ItemProcesoProduccion>();
        itemsAplicacion = new ArrayList<ItemAplicacionProduccion>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CircuitoProduccion getCircuito() {
        return circuito;
    }

    public void setCircuito(CircuitoProduccion circuito) {
        this.circuito = circuito;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }

    public int getNumeroFormulario() {
        return numeroFormulario;
    }

    public void setNumeroFormulario(int numeroFormulario) {
        this.numeroFormulario = numeroFormulario;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public Date getFechaRequerida() {
        return fechaRequerida;
    }

    public void setFechaRequerida(Date fechaRequerida) {
        this.fechaRequerida = fechaRequerida;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public DepartamentoProduccion getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoProduccion departamento) {
        this.departamento = departamento;
    }

    public TipoMovimientoProduccion getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimientoProduccion tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    public boolean isPersistido() {
        return persistido;
    }

    public void setPersistido(boolean persistido) {
        this.persistido = persistido;
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

    public Deposito getDepositoTransferencia() {
        return depositoTransferencia;
    }

    public void setDepositoTransferencia(Deposito depositoTransferencia) {
        this.depositoTransferencia = depositoTransferencia;
    }

    public MovimientoProduccion getParteProduccion() {
        return parteProduccion;
    }

    public void setParteProduccion(MovimientoProduccion parteProduccion) {
        this.parteProduccion = parteProduccion;
    }

    public MovimientoProduccion getValeConsumo() {
        return valeConsumo;
    }

    public void setValeConsumo(MovimientoProduccion valeConsumo) {
        this.valeConsumo = valeConsumo;
    }

    public MovimientoProduccion getParteProceso() {
        return parteProceso;
    }

    public void setParteProceso(MovimientoProduccion parteProceso) {
        this.parteProceso = parteProceso;
    }

    public MovimientoStock getMovimientoStock() {
        return movimientoStock;
    }

    public void setMovimientoStock(MovimientoStock movimientoStock) {
        this.movimientoStock = movimientoStock;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<ItemProductoProduccion> getItemsProducto() {
        return itemsProducto;
    }

    public void setItemsProducto(List<ItemProductoProduccion> itemsProducto) {
        this.itemsProducto = itemsProducto;
    }

    public List<ItemComponenteProduccion> getItemsComponente() {
        return itemsComponente;
    }

    public void setItemsComponente(List<ItemComponenteProduccion> itemsComponente) {
        this.itemsComponente = itemsComponente;
    }

    public ComprobanteStock getComprobanteStock() {
        return comprobanteStock;
    }

    public void setComprobanteStock(ComprobanteStock comprobanteStock) {
        this.comprobanteStock = comprobanteStock;
    }

    public Sucursal getSucursalStock() {
        return sucursalStock;
    }

    public void setSucursalStock(Sucursal sucursalStock) {
        this.sucursalStock = sucursalStock;
    }

    public Moneda getMonedaRegistracion() {
        return monedaRegistracion;
    }

    public void setMonedaRegistracion(Moneda monedaRegistracion) {
        this.monedaRegistracion = monedaRegistracion;
    }

    public Moneda getMonedaSecundaria() {
        return monedaSecundaria;
    }

    public void setMonedaSecundaria(Moneda monedaSecundaria) {
        this.monedaSecundaria = monedaSecundaria;
    }

    public BigDecimal getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(BigDecimal cotizacion) {
        this.cotizacion = cotizacion;
    }

    public List<ItemProcesoProduccion> getItemsProceso() {
        return itemsProceso;
    }

    public void setItemsProceso(List<ItemProcesoProduccion> itemsProceso) {
        this.itemsProceso = itemsProceso;
    }

    public List<ItemAplicacionProduccion> getItemsAplicacion() {
        return itemsAplicacion;
    }

    public void setItemsAplicacion(List<ItemAplicacionProduccion> itemsAplicacion) {
        this.itemsAplicacion = itemsAplicacion;
    }

    public boolean isNoSincronizaNumeroFormulario() {
        return noSincronizaNumeroFormulario;
    }

    public void setNoSincronizaNumeroFormulario(boolean noSincronizaNumeroFormulario) {
        this.noSincronizaNumeroFormulario = noSincronizaNumeroFormulario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final MovimientoProduccion other = (MovimientoProduccion) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MovimientoProduccion{" + "id=" + id + '}';
    }

}
