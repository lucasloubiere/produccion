/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.administracion.modelo;

import java.io.File;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author lloubiere
 *
 */
@Entity
@Table(name = "ad_parametro")
public class Parametro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", length = 2)
    private String id;

    /**
     * CONFIGURACION CORREO *
     */
    @Column(name = "SMHOST", length = 45)
    private String smtpHost;
    @Column(name = "SMSTLS", length = 45)
    private String smtpStarttls;
    @Column(name = "SMPUER", length = 4)
    private String smtpPuerto;
    @Column(name = "SMNCUE", length = 45)
    private String smtpNombreCuenta;
    @Column(name = "SMUSUA", length = 45)
    private String smtpUsuario;
    @Column(name = "SMPASS", length = 40)
    private String smtpPassword;
    @Column(name = "SMAUTH", length = 5)
    private String smtpAuth;
    @Column(name = "SMDIEN", length = 60)
    private String smtpDireccionEnvio;
    @Column(name = "SMNOEN", length = 60)
    private String smtpNombreEnvio;
    @Column(name = "SMSMTP", length = 10)
    private String smtpTipo;

    //Email recepcion de consultas 1
    @Column(name = "EMCONS", length = 60)
    private String emailRecepcionConsulta;
    @Column(name = "ENVEML")
    private char enviaNotificaciones;
    @Lob
    @Column(name = "PLANOT", length = 2147483647)
    private String plantillaNotificaciones;

    /**
     * FIN CONFIGURACION CORREO *
     */
    @Lob
    @Column(name = "PATHUP", length = 2147483647)
    private String pathUpload;

    @Column(name = "SISMNT", length = 1)
    private String sistemaEnMantenimiento;
    @Column(name = "SISMEN", length = 255)
    private String mensajeMantenimiento;

    @Column(name = "ORIDEF", length = 1)
    private String origenPorDefecto;

    @Column(name = "PATHII", length = 200)
    private String pathImagenInicio;

    @Column(name = "PATHPP", length = 200)
    private String pathImagenesPrincipal;

    @Column(name = "PATHPR", length = 200)
    private String pathImagenesProductos;

    @Column(name = "PATHIM", length = 200)
    private String pathImagenesDefault;

    @Column(name = "PATHEX", length = 200)
    private String pathExterno;

    @Column(name = "CARTMP", length = 200)
    private String nombreCarpetaArchivos;
    //private String carpetaTemporales;

    @Column(name = "MODPRU", length = 1)
    private String modoPrueba;

    @Column(name = "MODPNO", length = 255)
    private String recepcionNotificacionModoPrueba;

    @Column(name = "GOGANA", length = 255)
    private String codigoGoogleAnalitycs;

    @Column(name = "CHAT", length = 255)
    private String chat;

    @Column(name = "FONDO", length = 255)
    private String fondo;

    @Column(name = "TEMA", length = 255)
    private String tema;

    @Column(name = "DATASOURCE", length = 255)
    private String dataSource;

    @Column(name = "MODDAS", length = 1)
    //B - Básico || E - Extendido
    private String modoDashboard;

    @Column(name = "ZOOM", length = 5)
    private String zoom;
    
    @Column(name = "MONPRI", length = 5)
    private String codigoMonedaPrimaria;
    
    @Column(name = "MONSEC", length = 5)
    private String codigoMonedaSecundaria;

    @Transient
    private String pathTemporales;


    public Parametro() {
    }

    public Parametro(String id) {
        this.id = id;
    }

    public String getEmailRecepcionConsulta() {
        return emailRecepcionConsulta;
    }

    public void setEmailRecepcionConsulta(String emailRecepcionConsulta) {
        this.emailRecepcionConsulta = emailRecepcionConsulta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(String smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpNombreCuenta() {
        return smtpNombreCuenta;
    }

    public void setSmtpNombreCuenta(String smtpNombreCuenta) {
        this.smtpNombreCuenta = smtpNombreCuenta;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public String getSmtpPuerto() {
        return smtpPuerto;
    }

    public void setSmtpPuerto(String smtpPuerto) {
        this.smtpPuerto = smtpPuerto;
    }

    public String getSmtpStarttls() {
        return smtpStarttls;
    }

    public void setSmtpStarttls(String smtpStarttls) {
        this.smtpStarttls = smtpStarttls;
    }

    public String getSmtpUsuario() {
        return smtpUsuario;
    }

    public void setSmtpUsuario(String smtpUsuario) {
        this.smtpUsuario = smtpUsuario;
    }

    public String getMensajeMantenimiento() {
        return mensajeMantenimiento;
    }

    public void setMensajeMantenimiento(String mensajeMantenimiento) {
        this.mensajeMantenimiento = mensajeMantenimiento;
    }

    public String getSistemaEnMantenimiento() {
        if (sistemaEnMantenimiento == null) {

            sistemaEnMantenimiento = "S";
        }

        return sistemaEnMantenimiento;
    }

    public void setSistemaEnMantenimiento(String sistemaEnMantenimiento) {
        this.sistemaEnMantenimiento = sistemaEnMantenimiento;
    }

    public char getEnviaNotificaciones() {
        return enviaNotificaciones;
    }

    public void setEnviaNotificaciones(char enviaNotificaciones) {
        this.enviaNotificaciones = enviaNotificaciones;
    }

    public String getPathImagenesPrincipal() {
        return pathImagenesPrincipal;
    }

    public void setPathImagenesPrincipal(String pathImagenesPrincipal) {
        this.pathImagenesPrincipal = pathImagenesPrincipal;
    }

    public String getPathImagenesProductos() {
        return pathImagenesProductos;
    }

    public void setPathImagenesProductos(String pathImagenesProductos) {
        this.pathImagenesProductos = pathImagenesProductos;
    }

    public String getModoPrueba() {
        return modoPrueba;
    }

    public void setModoPrueba(String modoPrueba) {
        this.modoPrueba = modoPrueba;
    }

    public String getRecepcionNotificacionModoPrueba() {
        return recepcionNotificacionModoPrueba;
    }

    public void setRecepcionNotificacionModoPrueba(String recepcionNotificacionModoPrueba) {
        this.recepcionNotificacionModoPrueba = recepcionNotificacionModoPrueba;
    }

    public String getSmtpDireccionEnvio() {
        return smtpDireccionEnvio;
    }

    public void setSmtpDireccionEnvio(String smtpDireccionEnvio) {
        this.smtpDireccionEnvio = smtpDireccionEnvio;
    }

    public String getSmtpNombreEnvio() {
        return smtpNombreEnvio;
    }

    public void setSmtpNombreEnvio(String smtpNombreEnvio) {
        this.smtpNombreEnvio = smtpNombreEnvio;
    }

    public String getSmtpTipo() {
        return smtpTipo;
    }

    public void setSmtpTipo(String smtpTipo) {
        this.smtpTipo = smtpTipo;
    }

    public String getPlantillaNotificaciones() {
        return plantillaNotificaciones;
    }

    public void setPlantillaNotificaciones(String plantillaNotificaciones) {
        this.plantillaNotificaciones = plantillaNotificaciones;
    }

    public String getPathImagenesDefault() {
        return pathImagenesDefault;
    }

    public void setPathImagenesDefault(String pathImagenesDefault) {
        this.pathImagenesDefault = pathImagenesDefault;
    }

    public String getPathUpload() {
        return pathUpload;
    }

    public void setPathUpload(String pathUpload) {
        this.pathUpload = pathUpload;
    }

    public String getCodigoGoogleAnalitycs() {
        return codigoGoogleAnalitycs;
    }

    public void setCodigoGoogleAnalitycs(String codigoGoogleAnalitycs) {
        this.codigoGoogleAnalitycs = codigoGoogleAnalitycs;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getFondo() {
        return fondo;
    }

    public void setFondo(String fondo) {
        this.fondo = fondo;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getPathCarpetaArchivos() {

        pathTemporales = System.getProperty("catalina.base") +File.separator+"docroot"+File.separator + nombreCarpetaArchivos + File.separator;
        return pathTemporales;
    }

    public String getPathCarpetaReportes() {

        return System.getProperty("catalina.base") +File.separator+"docroot"+File.separator + nombreCarpetaArchivos +File.separator+"reportes"+File.separator;       
    }

    public String getPathCarpetaProductos() {

        return System.getProperty("catalina.base") +File.separator+"docroot"+File.separator + nombreCarpetaArchivos +File.separator+"productos"+File.separator;       
    }

    public String getPathCarpetaProcesos() {

        return System.getProperty("catalina.base") +File.separator+"docroot"+File.separator + nombreCarpetaArchivos +File.separator+"procesos"+File.separator;

    }

    public String getPathCarpetaTemporales() {

        return System.getProperty("catalina.base") +File.separator+"docroot"+File.separator + nombreCarpetaArchivos +File.separator+"temporales"+File.separator;

    }

    public String getPathCarpetaUsuarios() {

        return System.getProperty("catalina.base") +File.separator+"docroot"+File.separator+ nombreCarpetaArchivos +File.separator+"usuarios"+File.separator;

    }

    public String getNombreCarpetaArchivos() {
        return nombreCarpetaArchivos;
    }

    public void setNombreCarpetaArchivos(String nombreCarpetaArchivos) {
        this.nombreCarpetaArchivos = nombreCarpetaArchivos;
    }

    public void setPathTemporales(String pathTemporales) {
        this.pathTemporales = pathTemporales;
    }

    public String getModoDashboard() {
        return modoDashboard;
    }

    public void setModoDashboard(String modoDashboard) {
        this.modoDashboard = modoDashboard;
    }

    public String getPathExterno() {
        return pathExterno;
    }

    public void setPathExterno(String pathExterno) {
        this.pathExterno = pathExterno;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getPathImagenInicio() {

        return pathImagenInicio;
    }

    public void setPathImagenInicio(String pathImagenInicio) {
        this.pathImagenInicio = pathImagenInicio;
    }

    public String getOrigenPorDefecto() {
        return origenPorDefecto;
    }

    public void setOrigenPorDefecto(String origenPorDefecto) {
        this.origenPorDefecto = origenPorDefecto;
    }

    public String getCodigoMonedaPrimaria() {
        return codigoMonedaPrimaria;
    }

    public void setCodigoMonedaPrimaria(String codigoMonedaPrimaria) {
        this.codigoMonedaPrimaria = codigoMonedaPrimaria;
    }

    public String getCodigoMonedaSecundaria() {
        return codigoMonedaSecundaria;
    }

    public void setCodigoMonedaSecundaria(String codigoMonedaSecundaria) {
        this.codigoMonedaSecundaria = codigoMonedaSecundaria;
    }
    
    public String getCssPathImagenInicio() {
        if (pathImagenInicio != null && !pathImagenInicio.isEmpty()) {
            return "background: url('" + pathImagenInicio + "');\n";
        } else {
            return "";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Parametro other = (Parametro) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "tv.global.modelo.Parametro[usrprmttvid=" + id + "]";
    }

}
