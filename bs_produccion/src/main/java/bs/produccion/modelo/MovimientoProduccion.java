/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.modelo;

import bs.global.modelo.Formulario;
import bs.global.modelo.Sucursal;
import bs.stock.modelo.ComprobanteStock;
import bs.stock.modelo.Deposito;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lloubiere
 */
@Entity
@Table(name = "pd_movimiento", catalog = "produccion-ds", schema = "")
@XmlRootElement

public class MovimientoProduccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "nroform")
    private Integer nroform;
    @Column(name = "fchmov")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fchmov;
    @Lob
    @Size(max = 65535)
    @Column(name = "observaciones", length = 65535)
    private String observaciones;
    @Column(name = "fchreq")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fchreq;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "DEBAJA", nullable = false, length = 1)
    private String debaja;
    @Column(name = "FECALT")
    @Temporal(TemporalType.DATE)
    private Date fecalt;
    @Column(name = "FECMOD")
    @Temporal(TemporalType.DATE)
    private Date fecmod;
    @Size(max = 1)
    @Column(name = "ULTOPR", length = 1)
    private String ultopr;
    @Size(max = 15)
    @Column(name = "USERID", length = 15)
    private String userid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idcab")
    private List<ItemMovimientoProduccion> itemMovimientoProduccionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idcab")
    private List<ItemMovimientoOperario> itemMovimientoOperarioList;
    @JoinColumn(name = "deptra", referencedColumnName = "codigo")
    @ManyToOne
    private Deposito deptra;
    @JoinColumn(name = "deposi", referencedColumnName = "codigo")
    @ManyToOne
    private Deposito deposi;
    @JoinColumn(name = "sector", referencedColumnName = "codigo")
    @ManyToOne
    private SectorProduccion sector;
    @JoinColumns({
        @JoinColumn(name = "circom", referencedColumnName = "circom"),
        @JoinColumn(name = "cirapl", referencedColumnName = "cirapl")})
    @ManyToOne
    private Circuito circuito;
    @JoinColumn(name = "sucurs", referencedColumnName = "codigo")
    @ManyToOne
    private Sucursal sucurs;
    @JoinColumns({
        @JoinColumn(name = "codform", referencedColumnName = "modfor"),
        @JoinColumn(name = "modform", referencedColumnName = "codfor")})
    @ManyToOne
    private Formulario formulario;
    @JoinColumns({
        @JoinColumn(name = "codcom", referencedColumnName = "modcom"),
        @JoinColumn(name = "modcom", referencedColumnName = "codcom")})
    @ManyToOne
    private ComprobanteStock comprobanteStock;

    public MovimientoProduccion() {
    }

    public MovimientoProduccion(Integer id) {
        this.id = id;
    }

    public MovimientoProduccion(Integer id, String debaja) {
        this.id = id;
        this.debaja = debaja;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNroform() {
        return nroform;
    }

    public void setNroform(Integer nroform) {
        this.nroform = nroform;
    }

    public Date getFchmov() {
        return fchmov;
    }

    public void setFchmov(Date fchmov) {
        this.fchmov = fchmov;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFchreq() {
        return fchreq;
    }

    public void setFchreq(Date fchreq) {
        this.fchreq = fchreq;
    }

    public String getDebaja() {
        return debaja;
    }

    public void setDebaja(String debaja) {
        this.debaja = debaja;
    }

    public Date getFecalt() {
        return fecalt;
    }

    public void setFecalt(Date fecalt) {
        this.fecalt = fecalt;
    }

    public Date getFecmod() {
        return fecmod;
    }

    public void setFecmod(Date fecmod) {
        this.fecmod = fecmod;
    }

    public String getUltopr() {
        return ultopr;
    }

    public void setUltopr(String ultopr) {
        this.ultopr = ultopr;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @XmlTransient
    public List<ItemMovimientoProduccion> getItemMovimientoProduccionList() {
        return itemMovimientoProduccionList;
    }

    public void setItemMovimientoProduccionList(List<ItemMovimientoProduccion> itemMovimientoProduccionList) {
        this.itemMovimientoProduccionList = itemMovimientoProduccionList;
    }

    @XmlTransient
    public List<ItemMovimientoOperario> getItemMovimientoOperarioList() {
        return itemMovimientoOperarioList;
    }

    public void setItemMovimientoOperarioList(List<ItemMovimientoOperario> itemMovimientoOperarioList) {
        this.itemMovimientoOperarioList = itemMovimientoOperarioList;
    }

    public Deposito getDeptra() {
        return deptra;
    }

    public void setDeptra(Deposito deptra) {
        this.deptra = deptra;
    }

    public Deposito getDeposi() {
        return deposi;
    }

    public void setDeposi(Deposito deposi) {
        this.deposi = deposi;
    }

    public SectorProduccion getSector() {
        return sector;
    }

    public void setSector(SectorProduccion sector) {
        this.sector = sector;
    }

    public Circuito getCircuito() {
        return circuito;
    }

    public void setCircuito(Circuito circuito) {
        this.circuito = circuito;
    }

    public Sucursal getSucurs() {
        return sucurs;
    }

    public void setSucurs(Sucursal sucurs) {
        this.sucurs = sucurs;
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }

    public ComprobanteStock getComprobanteStock() {
        return comprobanteStock;
    }

    public void setComprobanteStock(ComprobanteStock comprobanteStock) {
        this.comprobanteStock = comprobanteStock;
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
