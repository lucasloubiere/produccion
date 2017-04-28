/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.global.modelo;


import com.stock.modelo.Deposito;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Claudio
 */
@Entity
@Table(name = "gr_comprobante")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "MODCOM", discriminatorType = DiscriminatorType.STRING, length = 2)
@IdClass(ComprobantePK.class)
@XmlRootElement
public class Comprobante implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "MODCOM",nullable = false, length = 2)
    private String modulo;
   
    @Id
    @Column(name = "CODCOM", nullable = false, length = 6)
    private String codigo;

    @Column(name = "DESCRP", nullable = false, length = 60)
    private String descripcion;
    
    @Column(name = "TITULO", length = 60)
    private String titulo;

    @Column(name = "COPIAS")
    private Integer copias;  
    
    @Column(name = "RECFEC", length = 1)
    private String recuperacionFecha;
        
    /**
     * Deposito emisor
     */
    @JoinColumn(name = "DEPTRA", referencedColumnName = "CODIGO",nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Deposito depositoTransferencia;

     /**
     * Deposito receptor
     */
    @JoinColumn(name = "DEPOSI", referencedColumnName = "CODIGO",nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Deposito deposito;

    @Lob
    @Column(name = "TEXTOS", length = 2147483647)
    private String textos;
    
    @Column(name = "COMREV", length = 1)
    private String esComprobanteReversion;
   
    @Embedded
    private Auditoria auditoria;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprobante", fetch=FetchType.LAZY)
    private List<FormularioPorSituacionIVA> formulariosPorSituacionIVA;

    public Comprobante() {
        
        this.recuperacionFecha = "A";        
        this.esComprobanteReversion = "N";
        this.auditoria = new Auditoria();
    }

    public Comprobante(ComprobantePK idPK) {
        this.modulo = idPK.getModulo();
        this.codigo = idPK.getCodigo();
        this.auditoria = new Auditoria();
    }

    public Comprobante(ComprobantePK idPK, String Descrp) {
        this.modulo = idPK.getModulo();
        this.codigo = idPK.getCodigo();
        this.descripcion = Descrp;
        this.auditoria = new Auditoria();
    }

    public Comprobante(String modulo, String codigo) {
        this.modulo = modulo;
        this.codigo = codigo;
        this.auditoria = new Auditoria();
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getCopias() {
        return copias;
    }

    public void setCopias(Integer copias) {
        this.copias = copias;
    }

    public String getRecuperacionFecha() {
        return recuperacionFecha;
    }

    public void setRecuperacionFecha(String recuperacionFecha) {
        this.recuperacionFecha = recuperacionFecha;
    }
    
    public Deposito getDepositoTransferencia() {
        return depositoTransferencia;
    }

    public void setDepositoTransferencia(Deposito depositoTransferencia) {
        this.depositoTransferencia = depositoTransferencia;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }
    
    public String getTextos() {
        return textos;
    }

    public void setTextos(String textos) {
        this.textos = textos;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    public List<FormularioPorSituacionIVA> getFormulariosPorSituacionIVA() {
        return formulariosPorSituacionIVA;
    }

    public void setFormulariosPorSituacionIVA(List<FormularioPorSituacionIVA> formulariosPorSituacionIVA) {
        this.formulariosPorSituacionIVA = formulariosPorSituacionIVA;
    } 

    public String getEsComprobanteReversion() {
        return esComprobanteReversion;
    }

    public void setEsComprobanteReversion(String esComprobanteReversion) {
        this.esComprobanteReversion = esComprobanteReversion;
    }
       
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modulo != null ? modulo.hashCode() : 0);
        hash += (codigo != null ? codigo.hashCode() : 0);
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
        final Comprobante other = (Comprobante) obj;
        if ((this.modulo == null) ? (other.modulo != null) : !this.modulo.equals(other.modulo)) {
            return false;
        }
        if ((this.codigo == null) ? (other.codigo != null) : !this.codigo.equals(other.codigo)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        
        return "modulo=" + modulo + ", codigo=" + codigo ;
    }

    
}
