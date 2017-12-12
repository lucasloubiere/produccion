/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.modelo;

import bs.global.modelo.Auditoria;
import bs.global.modelo.Comprobante;
import bs.global.modelo.Formulario;
import bs.global.modelo.Sucursal;
import bs.stock.modelo.Deposito;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lloubiere
 */
@Entity
@Table(name = "pd_movimiento")
@XmlRootElement

public class MovimientoProduccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "nroform")
    private Integer numeroformulario;
    @Column(name = "fchmov")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fchaMovimiento;
    @Lob
    @Size(max = 65535)
    @Column(name = "observaciones", length = 65535)
    private String observaciones;
    @Column(name = "fchreq")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fchaRequerida;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento")
    private List<ItemProductoProduccion> itemsProducto;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento")
    private List<ItemProcesoProduccion> itemsProceso;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento")
    private List<ItemComponenteProduccion> itemsComponente;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimiento")
    private List<ItemMovimientoOperario> itemsOperario;

    @JoinColumn(name = "deptra", referencedColumnName = "codigo")
    @ManyToOne
    private Deposito depositoTransferencia;
    @JoinColumn(name = "deposi", referencedColumnName = "codigo")
    @ManyToOne
    private Deposito deposito;
    @JoinColumn(name = "sector", referencedColumnName = "codigo")
    @ManyToOne
    private SectorProduccion sector;
    @JoinColumns({
        @JoinColumn(name = "circom", referencedColumnName = "circom"),
        @JoinColumn(name = "cirapl", referencedColumnName = "cirapl")})
    @ManyToOne
    private CircuitoProduccion circuito;
    @JoinColumn(name = "sucurs", referencedColumnName = "codigo")
    @ManyToOne
    private Sucursal sucursal;
    @JoinColumns({
        @JoinColumn(name = "codform", referencedColumnName = "modfor"),
        @JoinColumn(name = "modform", referencedColumnName = "codfor")})
    @ManyToOne
    private Formulario formulario;
    @JoinColumns({
        @JoinColumn(name = "codcom", referencedColumnName = "modcom"),
        @JoinColumn(name = "modcom", referencedColumnName = "codcom")})
    @ManyToOne
    private Comprobante comprobante;

    @Embedded
    private Auditoria auditoria;

    public MovimientoProduccion() {
        this.auditoria = new Auditoria();
        this.itemsComponente = new ArrayList<ItemComponenteProduccion>();
        this.itemsOperario = new ArrayList<ItemMovimientoOperario>();
        this.itemsProceso = new ArrayList<ItemProcesoProduccion>();
        this.itemsProducto = new ArrayList<ItemProductoProduccion>();
    }

    public MovimientoProduccion(Integer id) {
        this.itemsComponente = new ArrayList<ItemComponenteProduccion>();
        this.itemsOperario = new ArrayList<ItemMovimientoOperario>();
        this.itemsProceso = new ArrayList<ItemProcesoProduccion>();
        this.itemsProducto = new ArrayList<ItemProductoProduccion>();
        this.auditoria = new Auditoria();
        this.id = id;
    }

    
    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public SectorProduccion getSector() {
        return sector;
    }

    public void setSector(SectorProduccion sector) {
        this.sector = sector;
    }

    public CircuitoProduccion getCircuito() {
        return circuito;
    }

    public void setCircuito(CircuitoProduccion circuito) {
        this.circuito = circuito;
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
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
        if (!(object instanceof MovimientoProduccion)) {
            return false;
        }
        MovimientoProduccion other = (MovimientoProduccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bs.produccion.modelo.MovimientoProduccion[ id=" + id + " ]";
    }

}
