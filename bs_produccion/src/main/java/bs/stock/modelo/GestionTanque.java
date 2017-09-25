/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.modelo;

import bs.global.modelo.Auditoria;
import bs.global.modelo.Sucursal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Claudio
 */
@Entity
@Table(name = "st_gestion_tanque")
@XmlRootElement

public class GestionTanque implements Serializable {

    private List<ItemGestionTanque> items;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
   
    @JoinColumns({
        @JoinColumn(name = "modcom", referencedColumnName = "MODCOM", nullable = false),
        @JoinColumn(name = "codcom", referencedColumnName = "CODCOM", nullable = false)
    })
     @ManyToOne(fetch = FetchType.LAZY)      

    ComprobanteStock comprobante;

    @JoinColumns({
        @JoinColumn(name = "modfor", referencedColumnName = "modfor"),
        @JoinColumn(name = "codfor", referencedColumnName = "codfor")})

    @JoinColumn(name = "sucurs", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Sucursal sucursal;

    @Basic(optional = false)
    @NotNull
    @Column(name = "nrofor")
    private int numeroFormulario;

    @NotNull
    @Column(name = "fchmov")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMovimiento;

    @Lob
//    @Size(min = 1, max = 65535)
    @Column(name = "observ")
    private String observaciones;

    @Embedded
    private Auditoria auditoria;

    public GestionTanque() {

        this.auditoria = new Auditoria();
        this.items = new ArrayList<ItemGestionTanque>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public List<ItemGestionTanque> getItems() {
        return items;
    }

    public void setItems(List<ItemGestionTanque> items) {
        this.items = items;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.id;
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
        final GestionTanque other = (GestionTanque) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GestionTanque{" + "id=" + id + '}';
    }

}
