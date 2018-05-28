package bs.produccion.vista;

import bs.global.modelo.Formulario;
import bs.produccion.modelo.MovimientoProduccion;
import bs.produccion.modelo.Planta;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
 * @author Claudio
 */
@Entity
@Table(name = "pd_pendiente_grupo")
@XmlRootElement
@IdClass(PendienteProduccionGrupoPK.class)
public class PendienteProduccionGrupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Size(min = 1, max = 6)
    @Column(nullable = false, length = 6)
    private String circom;
    @Id
    @NotNull
    @Column(name = "id_mapl", nullable = false)
    private int idMapl;
    @Id
    @Size(max = 8)
    @Column(length = 8)
    private String modfor;
    @Id
    @Size(max = 8)
    @Column(length = 8)
    private String codfor;
    @Id
    @NotNull
    @Column(nullable = false)
    private int nrofor;
    @Id
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fchmov;
    @Id
    @NotNull
    @Column(nullable = false)
    private String formul;
    @Id
    @NotNull
    @Size(min = 1, max = 1)
    @Column(nullable = false, length = 1)
    private String stocks;
    @Id
    @NotNull
    @Size(min = 1, max = 1)
    @Column(nullable = false, length = 1)    
    private String tipitm;
    
    @Id
    @NotNull
    @Column(nullable = false)
    private String grupo;

    @Size(max = 50)
    @Column(length = 50)
    private String observ;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 10, scale = 2)
    private BigDecimal pendiente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mapl", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private MovimientoProduccion movimientoAplicacion;

    @JoinColumns({
        @JoinColumn(name = "modfor", referencedColumnName = "modfor", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "codfor", referencedColumnName = "codfor", nullable = false, insertable = false, updatable = false)
    })
    @ManyToOne(fetch = FetchType.LAZY)
    private Formulario formulario;

    @JoinColumn(name = "codpla", referencedColumnName = "codigo", nullable = false, insertable = false, updatable = false)
    @ManyToOne
    private Planta planta;

    @Column(name = "descrp", length = 60)
    private String descripcion;

    public PendienteProduccionGrupo() {

    }

    public String getCircom() {
        return circom;
    }

    public void setCircom(String circom) {
        this.circom = circom;
    }

    public int getIdMapl() {
        return idMapl;
    }

    public void setIdMapl(int idMapl) {
        this.idMapl = idMapl;
    }

    public String getCodfor() {
        return codfor;
    }

    public void setCodfor(String codfor) {
        this.codfor = codfor;
    }

    public int getNrofor() {
        return nrofor;
    }

    public void setNrofor(int nrofor) {
        this.nrofor = nrofor;
    }

    public Date getFchmov() {
        return fchmov;
    }

    public void setFchmov(Date fchmov) {
        this.fchmov = fchmov;
    }

    public String getStocks() {
        return stocks;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public String getObserv() {
        return observ;
    }

    public void setObserv(String observ) {
        this.observ = observ;
    }

    public BigDecimal getPendiente() {
        return pendiente;
    }

    public void setPendiente(BigDecimal pendiente) {
        this.pendiente = pendiente;
    }

    public MovimientoProduccion getMovimientoAplicacion() {
        return movimientoAplicacion;
    }

    public void setMovimientoAplicacion(MovimientoProduccion movimientoAplicacion) {
        this.movimientoAplicacion = movimientoAplicacion;
    }

    public String getModfor() {
        return modfor;
    }

    public void setModfor(String modfor) {
        this.modfor = modfor;
    }

    public String getFormul() {
        return formul;
    }

    public void setFormul(String formul) {
        this.formul = formul;
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getTipitm() {
        return tipitm;
    }

    public void setTipitm(String tipitm) {
        this.tipitm = tipitm;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.circom != null ? this.circom.hashCode() : 0);
        hash = 73 * hash + this.idMapl;
        hash = 73 * hash + (this.modfor != null ? this.modfor.hashCode() : 0);
        hash = 73 * hash + (this.codfor != null ? this.codfor.hashCode() : 0);
        hash = 73 * hash + this.nrofor;
        hash = 73 * hash + (this.fchmov != null ? this.fchmov.hashCode() : 0);
        hash = 73 * hash + (this.formul != null ? this.formul.hashCode() : 0);
        hash = 73 * hash + (this.stocks != null ? this.stocks.hashCode() : 0);
        hash = 73 * hash + (this.tipitm != null ? this.tipitm.hashCode() : 0);
        hash = 73 * hash + (this.grupo != null ? this.grupo.hashCode() : 0);
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
        final PendienteProduccionGrupo other = (PendienteProduccionGrupo) obj;
        if (this.idMapl != other.idMapl) {
            return false;
        }
        if (this.nrofor != other.nrofor) {
            return false;
        }
        if ((this.circom == null) ? (other.circom != null) : !this.circom.equals(other.circom)) {
            return false;
        }
        if ((this.modfor == null) ? (other.modfor != null) : !this.modfor.equals(other.modfor)) {
            return false;
        }
        if ((this.codfor == null) ? (other.codfor != null) : !this.codfor.equals(other.codfor)) {
            return false;
        }
        if ((this.formul == null) ? (other.formul != null) : !this.formul.equals(other.formul)) {
            return false;
        }
        if ((this.stocks == null) ? (other.stocks != null) : !this.stocks.equals(other.stocks)) {
            return false;
        }
        if ((this.grupo == null) ? (other.grupo != null) : !this.grupo.equals(other.grupo)) {
            return false;
        }
        if (this.tipitm != other.tipitm && (this.tipitm == null || !this.tipitm.equals(other.tipitm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PendienteProduccionGrupo{" + "circom=" + circom + ", idMapl=" + idMapl + ", modfor=" + modfor + ", codfor=" + codfor + ", nrofor=" + nrofor + ", fchmov=" + fchmov + ", formul=" + formul + ", stocks=" + stocks + ", tipitm=" + tipitm + ", grupo=" + grupo + '}';
    }
    
}
