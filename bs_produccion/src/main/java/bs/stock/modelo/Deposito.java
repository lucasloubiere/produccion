/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.modelo;

import bs.global.modelo.Auditoria;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ide
 */
@Entity
@Table(name = "st_deposito")
@XmlRootElement
public class Deposito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "codigo", nullable = false, length = 10)
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion", nullable = false, length = 45)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "signo", nullable = false, length = 45)
    private String signo;
    @Embedded
    private Auditoria auditoria;

    @Column(name = "codref", length = 6)
    private String codigoReferencia;

    @Column(name = "codref2", length = 6)
    private String codigoReferencia2;

    @Column(name = "calcula_stock", length = 1)
    private String calculaStock;

    @Column(name = "constante")
    private BigDecimal constante;

    @Column(name = "sumando")
    private BigDecimal sumando;

    @Column(name = "divisor")
    private BigDecimal divisor;

    @JoinColumn(name = "codsec", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(optional = false)
    private Sector sector;

    public Deposito() {
        this.auditoria = new Auditoria();
        this.calculaStock = "N";
    }

    public Deposito(String codigo) {
        this.auditoria = new Auditoria();
        this.codigo = codigo;
        this.calculaStock = "N";
    }

    public Deposito(String codigo, String descripcion, String signo) {
        this.auditoria = new Auditoria();
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.signo = signo;
        this.calculaStock = "N";
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

    public String getSigno() {
        return signo;
    }

    public void setSigno(String signo) {
        this.signo = signo;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    public String getCodigoReferencia() {
        return codigoReferencia;
    }

    public void setCodigoReferencia(String codigoReferencia) {
        this.codigoReferencia = codigoReferencia;
    }

    public String getCodigoReferencia2() {
        return codigoReferencia2;
    }

    public void setCodigoReferencia2(String codigoReferencia2) {
        this.codigoReferencia2 = codigoReferencia2;
    }

    public String getCalculaStock() {
        return calculaStock;
    }

    public void setCalculaStock(String calculaStock) {
        this.calculaStock = calculaStock;
    }

    public BigDecimal getConstante() {
        return constante;
    }

    public void setConstante(BigDecimal constante) {
        this.constante = constante;
    }

    public BigDecimal getSumando() {
        return sumando;
    }

    public void setSumando(BigDecimal sumando) {
        this.sumando = sumando;
    }

    public BigDecimal getDivisor() {
        return divisor;
    }

    public void setDivisor(BigDecimal divisor) {
        this.divisor = divisor;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Deposito)) {
            return false;
        }
        Deposito other = (Deposito) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.stock.modelo.Deposito[ codigo=" + codigo + " ]";
    }

}
