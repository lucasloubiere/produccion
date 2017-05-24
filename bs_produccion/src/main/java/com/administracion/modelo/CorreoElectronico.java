package com.administracion.modelo;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.global.modelo.Auditoria;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import javax.mail.internet.InternetAddress;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author ctrosch
 */
@Entity
@Table(name = "ad_correo_electronico")
public class CorreoElectronico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)        
    @Column(name = "ID", nullable = false)
    private Integer id;
        
    //Fecha del movimiento
    @Column(name = "FCHENV", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaEnvio;    
       
   
    @Column(name = "DIRENV", length = 255)    
    private String direccionEnvio;
    
    @Column(name = "DESTIN", length = 255)    
    private String destinatarios;
    
    @Column(name = "ASUNTO", length = 255)        
    private String asunto;

    @Lob
    @Column(name = "CONTEN", length = 2147483647)
    private String contenido;
    
    @Column(name = "ENVIAD", length = 1)        
    private String enviado;
    
    @Lob
    @Column(name = "ERROREN", length = 2147483647)
    private String error;
    
    @Lob
    @Column(name = "PATHAD", length = 2147483647)
    private String pathArchivo;
        
    @Embedded
    private Auditoria auditoria;
    
    @Transient
    private InternetAddress dirFrom; 
        
    @Transient
    private File archivoAdjunto; 
 
    public CorreoElectronico() {
        
        this.fechaEnvio = new Date();
        this.enviado = "N"; 
        this.auditoria = new Auditoria();
                
    }

    public CorreoElectronico(String asunto, String destinatarios, String contenido) {
        this.asunto = asunto;
        this.destinatarios = destinatarios;
        this.contenido = contenido;
        this.fechaEnvio = new Date();
        this.enviado = "N";               
        this.auditoria = new Auditoria();
    }
    
    public CorreoElectronico(String asunto, String destinatarios, String mensaje, String archivo) {
        
        this.asunto = asunto;        
        this.destinatarios = destinatarios;
        this.contenido = mensaje;        
        this.pathArchivo = archivo;
        this.fechaEnvio = new Date();
        this.enviado = "N";
        this.auditoria = new Auditoria();
        
    }

    public CorreoElectronico(InternetAddress internetAddress, String asunto, String destinatarios, String mensaje) {
        
        this.asunto = asunto;
        this.direccionEnvio = internetAddress.getAddress();
        this.destinatarios = destinatarios;
        this.contenido = mensaje;
        this.dirFrom = internetAddress;
        this.fechaEnvio = new Date();
        this.enviado = "N";
        this.auditoria = new Auditoria();
        
    }
    
    public CorreoElectronico(InternetAddress internetAddress, String asunto, String destinatarios, String mensaje, String archivo) {
        
        this.asunto = asunto;
        this.direccionEnvio = internetAddress.getAddress();
        this.destinatarios = destinatarios;
        this.contenido = mensaje;
        this.dirFrom = internetAddress;
        this.pathArchivo = archivo;
        this.fechaEnvio = new Date();
        this.enviado = "N";
        this.auditoria = new Auditoria();
        
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(String destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getEnviado() {
        return enviado;
    }

    public void setEnviado(String enviado) {
        this.enviado = enviado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }
    
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public InternetAddress getDirFrom() {
        return dirFrom;
    }

    public void setDirFrom(InternetAddress dirFrom) {
        this.dirFrom = dirFrom;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }
    
    public String getPathArchivo() {
        return pathArchivo;
    }

    public void setPathArchivo(String pathArchivo) {
        this.pathArchivo = pathArchivo;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    public File getArchivoAdjunto() {
        return archivoAdjunto;
    }

    public void setArchivoAdjunto(File archivoAdjunto) {
        this.archivoAdjunto = archivoAdjunto;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CorreoElectronico other = (CorreoElectronico) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "CorreoElectronico{" + "id=" + id + '}';
    }
    
}
