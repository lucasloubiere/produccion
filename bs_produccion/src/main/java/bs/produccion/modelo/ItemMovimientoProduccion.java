/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.modelo;

import bs.global.auditoria.AuditoriaListener;
import bs.global.auditoria.IAuditableEntity;
import bs.global.modelo.Auditoria;
import bs.stock.modelo.ComposicionFormula;
import bs.stock.modelo.Deposito;
import bs.stock.modelo.Formula;
import bs.stock.modelo.Producto;
import bs.stock.modelo.UnidadMedida;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author ctrosch
 */
@Entity
@Table(name = "pd_movimiento_item")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipitm", discriminatorType = DiscriminatorType.STRING, length = 1)
@EntityListeners(AuditoriaListener.class)
public abstract class ItemMovimientoProduccion implements Serializable, IAuditableEntity, IItemMovimientoProduccion {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nroitm")
    private int nroitm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mcab", referencedColumnName = "id", nullable = false)
    private MovimientoProduccion movimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mori", referencedColumnName = "id", nullable = false)
    private MovimientoProduccion movimientoOriginal;

    @Column(name = "itmori")
    private Integer idItemOriginal;

    @JoinColumn(name = "artcod", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Producto producto;

    @JoinColumn(name = "artori", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Producto productoOriginal;

    @JoinColumn(name = "artsus", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Producto productoSustituto;

    @JoinColumn(name = "deposi", referencedColumnName = "codigo", nullable = true)
    @ManyToOne(optional = false)
    private Deposito deposito;

    @JoinColumn(name = "codfor", referencedColumnName = "codigo", nullable = true)
    @ManyToOne(optional = false)
    private Formula formula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "artcod", referencedColumnName = "artcod", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "codfor", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    })
    private ComposicionFormula composicionFormula;

    @JoinColumn(name = "operar", referencedColumnName = "codigo", nullable = false)
    @ManyToOne
    private Operario operario;

    @Column(name = "cantid", precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "cantst", precision = 10, scale = 2)
    private BigDecimal cantidadStock;

    @Column(name = "cntori", precision = 10, scale = 2)
    private BigDecimal cantidadOriginal;

    @Column(name = "cntpen", precision = 10, scale = 2)
    private BigDecimal cantidadPendiente;

    @Column(name = "cntnom", precision = 10, scale = 2)
    private BigDecimal cantidadNominal;

    @Column(name = "grupo", length = 20)
    private String grupo;

    @ManyToOne
    @JoinColumn(name = "unimed", referencedColumnName = "codigo", nullable = false)
    private UnidadMedida unidadMedida;

    @Column(name = "precio", precision = 4, scale = 6)
    private BigDecimal precio;

    @Column(name = "horini")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaInicio;

    @Column(name = "horfin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaFin;

    @Column(name = "fchent")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;

    @Column(name = "fchhas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;

    @Lob
    @Column(name = "observ", length = 2147483647)
    private String observaciones;

    @Column(name = "stocks", length = 1)
    private String actualizaStock;

    @Embedded
    private Auditoria auditoria;

    @Transient
    private BigDecimal pendiente;

    @Transient
    private BigDecimal produccion;

    @Transient
    private boolean conError;

    @Transient
    private boolean todoOk;

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

    public ItemMovimientoProduccion() {

        cantidad = BigDecimal.ZERO;
        cantidadStock = BigDecimal.ZERO;
        cantidadOriginal = BigDecimal.ZERO;
        pendiente = BigDecimal.ZERO;
        produccion = BigDecimal.ZERO;
        precio = BigDecimal.ZERO;
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
    public MovimientoProduccion getMovimiento() {
        return movimiento;
    }

    @Override
    public void setMovimiento(MovimientoProduccion movimiento) {
        this.movimiento = movimiento;
    }

    @Override
    public MovimientoProduccion getMovimientoOriginal() {
        return movimientoOriginal;
    }

    @Override
    public void setMovimientoOriginal(MovimientoProduccion movimientoOriginal) {
        this.movimientoOriginal = movimientoOriginal;
    }

    @Override
    public Integer getIdItemOriginal() {
        return idItemOriginal;
    }

    @Override
    public void setIdItemOriginal(Integer idItemOriginal) {
        this.idItemOriginal = idItemOriginal;
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
    public Producto getProductoOriginal() {
        return productoOriginal;
    }

    @Override
    public void setProductoOriginal(Producto productoOriginal) {
        this.productoOriginal = productoOriginal;
    }

    @Override
    public Producto getProductoSustituto() {
        return productoSustituto;
    }

    @Override
    public void setProductoSustituto(Producto productoSustituto) {
        this.productoSustituto = productoSustituto;
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
    public Operario getOperario() {
        return operario;
    }

    @Override
    public void setOperario(Operario operario) {
        this.operario = operario;
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
    public BigDecimal getCantidadStock() {
        return cantidadStock;
    }

    @Override
    public void setCantidadStock(BigDecimal cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    @Override
    public BigDecimal getCantidadOriginal() {
        return cantidadOriginal;
    }

    @Override
    public void setCantidadOriginal(BigDecimal cantidadOriginal) {
        this.cantidadOriginal = cantidadOriginal;
    }

    @Override
    public BigDecimal getCantidadPendiente() {
        return cantidadPendiente;
    }

    @Override
    public void setCantidadPendiente(BigDecimal cantidadPendiente) {
        this.cantidadPendiente = cantidadPendiente;
    }

    @Override
    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    @Override
    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    @Override
    public BigDecimal getPrecio() {
        return precio;
    }

    @Override
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    @Override
    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    @Override
    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    @Override
    public Date getFechaHasta() {
        return fechaHasta;
    }

    @Override
    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    @Override
    public String getObservaciones() {
        return observaciones;
    }

    @Override
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String getActualizaStock() {
        return actualizaStock;
    }

    @Override
    public void setActualizaStock(String actualizaStock) {
        this.actualizaStock = actualizaStock;
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
    public boolean isConError() {
        return conError;
    }

    @Override
    public void setConError(boolean conError) {
        this.conError = conError;
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
    public boolean isTodoOk() {
        return todoOk;
    }

    @Override
    public void setTodoOk(boolean todoOk) {
        this.todoOk = todoOk;
    }

    @Override
    public BigDecimal getPendiente() {
        return pendiente;
    }

    @Override
    public void setPendiente(BigDecimal pendiente) {
        this.pendiente = pendiente;
    }

    @Override
    public BigDecimal getProduccion() {
        return produccion;
    }

    @Override
    public void setProduccion(BigDecimal produccion) {
        this.produccion = produccion;
    }

    @Override
    public String getGrupo() {
        return grupo;
    }

    @Override
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    @Override
    public BigDecimal getCantidadNominal() {
        return cantidadNominal;
    }

    @Override
    public void setCantidadNominal(BigDecimal cantidadNominal) {
        this.cantidadNominal = cantidadNominal;
    }

    public ComposicionFormula getComposicionFormula() {
        return composicionFormula;
    }

    public void setComposicionFormula(ComposicionFormula composicionFormula) {
        this.composicionFormula = composicionFormula;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final ItemMovimientoProduccion other = (ItemMovimientoProduccion) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ItemMovimientoProduccion{" + "id=" + id + '}';
    }

}
