/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.administracion.modelo;

import bs.global.modelo.Auditoria;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author "Claudio Trosch"
 */
@Entity
@Table(name = "ad_vista_detalle")
@XmlRootElement
public class VistaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "codigo", nullable = false)
    private String codigo;
    
    @Size(max = 1)
    @Column(name = "TIPO", length = 1)
    private String tipo;
    
    @Size(max = 100)
    @Column(name = "NOMBRE", length = 100)
    private String nombre;
    @Size(max = 100)
    @Column(name = "CAMPO", length = 100)
    private String campo;    
    @Column(name = "VISIBLE")
    private boolean visible;    
    @Column(name = "SOLLEC")
    private boolean soloLectura;
    @Column(name = "REQUERIDO")
    private boolean requerido;
    
    @Size(min = 3, max = 3)
    @Column(name = "ORIGEN", nullable = false, length = 3)
    private String origen;
    
    @JoinColumn(name = "COD_VISTA", referencedColumnName = "codigo")
    @ManyToOne(fetch = FetchType.EAGER)
    private Vista vista;
    
    @Embedded
    private Auditoria auditoria;

    public VistaDetalle() {
        
        this.auditoria = new Auditoria();
        this.origen = "USR";
    }

    public VistaDetalle(String codigo) {
        this.codigo = codigo;
        this.auditoria = new Auditoria();
        this.origen = "USR";
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public boolean isRequerido() {
        return requerido;
    }

    public void setRequerido(boolean requerido) {
        this.requerido = requerido;
    }
    
    public Vista getVista() {
        return vista;
    }

    public void setVista(Vista vista) {
        this.vista = vista;
    }
    
    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
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
        final VistaDetalle other = (VistaDetalle) obj;
        if ((this.codigo == null) ? (other.codigo != null) : !this.codigo.equals(other.codigo)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "bs.administracion.modelo.VistaDetalle[ codigo=" + codigo + " ]";
    }
    
}
