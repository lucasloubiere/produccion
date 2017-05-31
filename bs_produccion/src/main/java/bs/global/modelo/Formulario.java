/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.global.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ctrosch
 */
@Entity
@Table(name = "gr_formulario")
@IdClass(FormularioPK.class)
public class Formulario implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Modulo de formulario
     */
    @Id
    @Column(name = "modfor", nullable = false, length = 2)
    private String modfor;
    /**
     * codigo de formulario
     */
    @Id
    @Basic(optional = false)
    @Column(name = "codfor", nullable = false, length = 6)
    private String codigo;
    /**
     * Descripción
     */
    @Basic(optional = false)
    @Column(name = "descrp", nullable = false, length = 60)
    private String descripcion;

    
    @Column(name = "tipnum", nullable = false, length = 1)
    private String tipoNumeracion;
    /**
     * Ultimo número grabado
     */
    @Column(name = "ULTNRO")
    private Integer ultimoNumero;
    /**
     * Ultima fecha grabada
     */
    @Basic(optional = false)
    @Column(name = "ULTFEC", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ultimaFecha;

    /*
     * Sucursal o punto de venta
     */
    @JoinColumn(name = "SUCURS", referencedColumnName = "CODIGO", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Sucursal sucursal;

    /**
     * Letra
     */
    @Basic(optional = false)
    @Column(name = "LETRID", nullable = false, length = 1)
    private String letra;
    /**
     * número máximo de items
     */
    @Column(name = "MAXITM")
    private Short cantidadMaximaItems;
    
    /**
     * Nombre del reporte
     */
    @Column(name = "RPTNAM", length = 200)
    private String nombreReporte;
    /**
     * Metodo de recuperación de fecha
     * A = Fecha Actual
     * U = Ultima fecha guardada
     */
    
    @Column(name = "RECFEC", nullable = false, length = 1)
    private String recuperacionFecha;

    
    /**
     * Observaciones
     */
    @Lob
    @Column(name = "TEXTOS", length = 2147483647)
    private String Textos;
    
    /**
     * Permite modificar numeracion
     */
    @Column(name = "MODNUM")
    private String modificaNumeracion;
    /**
     * Permite modificar fecha
     */
    @Column(name = "MODFEC")
    private String modificaFecha;
    
    /**
     * Código DGI
     */
    @Column(name = "CODDGI", length = 25)
    private String codigoDGI;


    @Embedded
    private Auditoria auditoria;


    public Formulario() {
        
        this.recuperacionFecha = "A";
        this.ultimaFecha = new Date();
        this.ultimoNumero = 0;
        this.tipoNumeracion = "A";
        this.modificaFecha = "N";
        this.modificaNumeracion = "N";
        this.auditoria = new Auditoria();
    }

    public Formulario(String modfor, String codfor) {
        this.modfor = modfor;
        this.codigo = codfor;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }
    
    public String getTextos() {
        return Textos;
    }

    public void setTextos(String Textos) {
        this.Textos = Textos;
    }

    public Short getCantidadMaximaItems() {
        return cantidadMaximaItems;
    }

    public void setCantidadMaximaItems(Short cantidadMaximaItems) {
        this.cantidadMaximaItems = cantidadMaximaItems;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getCodigoDGI() {
        return codigoDGI;
    }

    public void setCodigoDGI(String codigoDGI) {
        this.codigoDGI = codigoDGI;
    }

    public String getModfor() {
        return modfor;
    }

    public void setModfor(String modfor) {
        this.modfor = modfor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoNumeracion() {
        return tipoNumeracion;
    }

    public void setTipoNumeracion(String tipoNumeracion) {
        this.tipoNumeracion = tipoNumeracion;
    }

    public Integer getUltimoNumero() {
        return ultimoNumero;
    }

    public void setUltimoNumero(Integer ultimoNumero) {
        this.ultimoNumero = ultimoNumero;
    }

    public Date getUltimaFecha() {
        return ultimaFecha;
    }

    public void setUltimaFecha(Date ultimaFecha) {
        this.ultimaFecha = ultimaFecha;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public String getRecuperacionFecha() {
        return recuperacionFecha;
    }

    public void setRecuperacionFecha(String recuperacionFecha) {
        this.recuperacionFecha = recuperacionFecha;
    }

    public String getModificaNumeracion() {
        return modificaNumeracion;
    }

    public void setModificaNumeracion(String modificaNumeracion) {
        this.modificaNumeracion = modificaNumeracion;
    }

    public String getModificaFecha() {
        return modificaFecha;
    }

    public void setModificaFecha(String modificaFecha) {
        this.modificaFecha = modificaFecha;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }
    
    

    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Formulario other = (Formulario) obj;
        if ((this.modfor == null) ? (other.modfor != null) : !this.modfor.equals(other.modfor)) {
            return false;
        }
        if ((this.codigo == null) ? (other.codigo != null) : !this.codigo.equals(other.codigo)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + (this.modfor != null ? this.modfor.hashCode() : 0);
        hash = 11 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {        
        return "modulo=" + modfor + ", codigo=" + codigo ;
    }

}
