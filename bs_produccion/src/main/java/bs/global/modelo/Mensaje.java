/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.global.modelo;

import bs.seguridad.modelo.Usuario;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "gr_mensaje")
@XmlRootElement

public class Mensaje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo", nullable = false)
    private Integer codigo;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "mensaje", nullable = false, length = 65535)
    private String mensaje;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 1)
    @Column(name = "estado", length = 1)
    private String estado;
    
    @JoinColumn(name = "id_emisor", referencedColumnName = "id")
    @ManyToOne(optional = false)    
    private Usuario remitente;
    
    @JoinColumn(name = "id_receptor", referencedColumnName = "id")
    @ManyToOne(optional = false)    
    private Usuario destinatario;
    

    @Embedded
    private Auditoria auditoria;
    public Mensaje() {
        this.auditoria = new Auditoria();
        
    }

    public Mensaje(Integer codigo) {
        this.codigo = codigo;
        this.auditoria = new Auditoria();
    }

    public Mensaje(Integer codigo, String mensaje, String debaja) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.auditoria = new Auditoria();
        
    }

    public Integer getCodigo() {
        return codigo;
       
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
        
    }

    public String getMensaje() {
        return mensaje;
    }

    public void stMensaje(String mensaje) {
        this.mensaje = mensaje;
        
    }

    public Usuario getRemitente() {
        return remitente;
    }

    public void setRemitente(Usuario remitente) {
        this.remitente = remitente;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }
    
    

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
        
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
        
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
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
        if (!(object instanceof Mensaje)) {
            return false;
        }
        Mensaje other = (Mensaje) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bs.global.modelo.Mensaje[ codigo=" + codigo + " ]";
    }
    
}
