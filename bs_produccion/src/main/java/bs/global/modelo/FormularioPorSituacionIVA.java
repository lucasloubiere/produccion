/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.global.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author ctrosch
 */
@Entity
@Table(name = "gr_formulario_iva")
@IdClass(FormularioPorSituacionIVAPK.class)
public class FormularioPorSituacionIVA implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "modcom", nullable = false, length = 2)
    private String modcom;
    @Id
    @Column(name = "codcom", nullable = false, length = 6)
    private String codcom;
    @Id
    @Column(name = "cndiva", nullable = false, length = 6)
    private String cndiva;
    @Id
    @Column(name = "sucurs", nullable = false, length = 6)
    private String sucurs;

     /**
     * Comprobante 
     */
    @JoinColumns({
    @JoinColumn(name = "modcom", referencedColumnName = "modcom", nullable = false,insertable=false, updatable=false),
    @JoinColumn(name = "codcom", referencedColumnName = "codcom", nullable = false,insertable=false, updatable=false)
    })
    @ManyToOne(fetch = FetchType.LAZY)
    Comprobante comprobante;

    @JoinColumn(name = "sucurs", referencedColumnName = "codigo", nullable = false, insertable=false, updatable=false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Sucursal sucursal;

    @JoinColumn(name = "cndiva", referencedColumnName = "codigo", nullable = false, insertable=false, updatable=false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CondicionDeIva condicionDeIva;
    
    /**
     * Formulario
     */
    @JoinColumns({
    @JoinColumn(name = "modfor", referencedColumnName = "modfor", nullable = false),
    @JoinColumn(name = "codfor", referencedColumnName = "codfor", nullable = false)
    })
    @ManyToOne(fetch = FetchType.LAZY)
    Formulario formulario;
    
    @Column(name = "CLADGI", length = 6)
    private String codigoDGI;
    /**
     * Nombre del reporte
     */
    @Column(name = "RPTNAM", length = 200)
    private String nombreReporte;
    
    @Embedded
    private Auditoria auditoria;


    public FormularioPorSituacionIVA() {
        auditoria = new Auditoria();
    }

    public FormularioPorSituacionIVA(String modcom, String codcom, String cndiva) {
        this.modcom = modcom;
        this.codcom = codcom;
        this.cndiva = cndiva;
        
        auditoria = new Auditoria();
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public String getSucurs() {
        return sucurs;
    }

    public void setSucurs(String sucurs) {
        this.sucurs = sucurs;
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
    
    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    public String getCndiva() {
        return cndiva;
    }

    public void setCndiva(String cndiva) {
        this.cndiva = cndiva;
    }

    public String getCodcom() {
        return codcom;
    }

    public CondicionDeIva getCondicionDeIva() {
        return condicionDeIva;
    }

    public void setCondicionDeIva(CondicionDeIva condicionDeIva) {
        this.condicionDeIva = condicionDeIva;
    }

    public String getCodigoDGI() {
        return codigoDGI;
    }

    public void setCodigoDGI(String codigoDGI) {
        this.codigoDGI = codigoDGI;
    }
    
    public void setCodcom(String codcom) {
        this.codcom = codcom;
    }
    public String getModcom() {
        return modcom;
    }

    public void setModcom(String modcom) {
        this.modcom = modcom;
    }
    
    
    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.modcom != null ? this.modcom.hashCode() : 0);
        hash = 97 * hash + (this.codcom != null ? this.codcom.hashCode() : 0);
        hash = 97 * hash + (this.cndiva != null ? this.cndiva.hashCode() : 0);
        hash = 97 * hash + (this.sucurs != null ? this.sucurs.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FormularioPorSituacionIVA other = (FormularioPorSituacionIVA) obj;
        if ((this.modcom == null) ? (other.modcom != null) : !this.modcom.equals(other.modcom)) {
            return false;
        }
        if ((this.codcom == null) ? (other.codcom != null) : !this.codcom.equals(other.codcom)) {
            return false;
        }
        if ((this.cndiva == null) ? (other.cndiva != null) : !this.cndiva.equals(other.cndiva)) {
            return false;
        }
        if ((this.sucurs == null) ? (other.sucurs != null) : !this.sucurs.equals(other.sucurs)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FormularioPorSituacionIVA{" + "modcom=" + modcom + ", codcom=" + codcom + ", cndiva=" + cndiva + ", sucurs=" + sucurs + '}';
    }
    
}
