/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.modelo;

import bs.stock.modelo.ComposicionFormula;
import bs.stock.modelo.Producto;
import bs.stock.modelo.UnidadMedida;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lloubiere
 */
@Entity
@Table(name = "pd_movimiento_item", catalog = "produccion-ds", schema = "")
@XmlRootElement

public class ItemMovimientoProduccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "tipitm", nullable = false, length = 1)
    private String tipitm;
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
    private String natri1;
    @Size(max = 50)
    @Column(name = "natri2", length = 50)
    private String natri2;
    @Size(max = 50)
    @Column(name = "natri3", length = 50)
    private String natri3;
    @Size(max = 50)
    @Column(name = "natri4", length = 50)
    private String natri4;
    @Size(max = 50)
    @Column(name = "natri5", length = 50)
    private String natri5;
    @Size(max = 50)
    @Column(name = "natri6", length = 50)
    private String natri6;
    @Size(max = 50)
    @Column(name = "natri7", length = 50)
    private String natri7;
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
    @JoinColumn(name = "unimed", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(optional = false)
    private UnidadMedida unimed;
    @JoinColumn(name = "artcod", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(optional = false)
    private Producto artcod;
    @JoinColumns({
        @JoinColumn(name = "artcod", referencedColumnName = "artcod", nullable = false),
        @JoinColumn(name = "codfor", referencedColumnName = "codfor", nullable = false)})
    @ManyToOne(optional = false)
    private ComposicionFormula composicionFormula;
    @JoinColumn(name = "codoper", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(optional = false)
    private Operario codoper;
    @JoinColumn(name = "idcab", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private MovimientoProduccion idcab;

    public ItemMovimientoProduccion() {
    }

    public ItemMovimientoProduccion(Integer id) {
        this.id = id;
    }

    public ItemMovimientoProduccion(Integer id, String tipitm, BigDecimal cantid, BigDecimal precio, String debaja) {
        this.id = id;
        this.tipitm = tipitm;
        this.cantid = cantid;
        this.precio = precio;
        this.debaja = debaja;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipitm() {
        return tipitm;
    }

    public void setTipitm(String tipitm) {
        this.tipitm = tipitm;
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

    public String getNatri1() {
        return natri1;
    }

    public void setNatri1(String natri1) {
        this.natri1 = natri1;
    }

    public String getNatri2() {
        return natri2;
    }

    public void setNatri2(String natri2) {
        this.natri2 = natri2;
    }

    public String getNatri3() {
        return natri3;
    }

    public void setNatri3(String natri3) {
        this.natri3 = natri3;
    }

    public String getNatri4() {
        return natri4;
    }

    public void setNatri4(String natri4) {
        this.natri4 = natri4;
    }

    public String getNatri5() {
        return natri5;
    }

    public void setNatri5(String natri5) {
        this.natri5 = natri5;
    }

    public String getNatri6() {
        return natri6;
    }

    public void setNatri6(String natri6) {
        this.natri6 = natri6;
    }

    public String getNatri7() {
        return natri7;
    }

    public void setNatri7(String natri7) {
        this.natri7 = natri7;
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

    public UnidadMedida getUnimed() {
        return unimed;
    }

    public void setUnimed(UnidadMedida unimed) {
        this.unimed = unimed;
    }

    public Producto getArtcod() {
        return artcod;
    }

    public void setArtcod(Producto artcod) {
        this.artcod = artcod;
    }

    public ComposicionFormula getComposicionFormula() {
        return composicionFormula;
    }

    public void setComposicionFormula(ComposicionFormula composicionFormula) {
        this.composicionFormula = composicionFormula;
    }

    public Operario getCodoper() {
        return codoper;
    }

    public void setCodoper(Operario codoper) {
        this.codoper = codoper;
    }

    public MovimientoProduccion getIdcab() {
        return idcab;
    }

    public void setIdcab(MovimientoProduccion idcab) {
        this.idcab = idcab;
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
