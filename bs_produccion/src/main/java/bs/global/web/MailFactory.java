package bs.global.web;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import bs.administracion.modelo.CorreoElectronico;
import bs.administracion.modelo.Parametro;
import bs.administracion.rn.CorreoElectronicoRN;
import bs.administracion.rn.ParametrosRN;
import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.util.JsfUtil;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 *
 * @author ctrosch
 */

@Stateless
public class MailFactory implements Serializable{

    private Properties props = new Properties();
    private Session session;
    private Transport transport;

    Parametro p;

    @EJB private ParametrosRN parametrosRN;
    @EJB private CorreoElectronicoRN correoElectronicoRN;
   
    @PostConstruct
    private void iniciar(){
        try {           

            p = parametrosRN.getParametro("01");
            props.setProperty("mail.smtp.host", p.getSmtpHost());
            
            if(p.getSmtpStarttls().equals("true")) props.setProperty("mail.smtp.starttls.enable", p.getSmtpStarttls());
            
            props.setProperty("mail.smtp.port", p.getSmtpPuerto());
            props.setProperty("mail.smtp.user", p.getSmtpUsuario());
            props.setProperty("mail.smtp.auth", p.getSmtpAuth());
            
        } catch (Exception e){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "MailFactory ERROR", e);            
            props = new Properties();
        }
    }
    
    public synchronized boolean enviarCorreoElectronico(CorreoElectronico ce){
        
        try {
            List<CorreoElectronico> lce = new ArrayList<CorreoElectronico>();
            lce.add(ce);
            enviarCorreosElectronicos(lce);
        } catch (Exception e) {
            return false;
        }        
        return true;        
    }

    public synchronized void enviarCorreosElectronicos(List<CorreoElectronico> correos) {

        if(correos==null || correos.isEmpty()) return;
        if(props.isEmpty())  return;            
        
        iniciar();
        
        try {            
            // Preparamos la sesion
            session = Session.getDefaultInstance(props); 
//            session.setDebug(true);            
            transport = session.getTransport(p.getSmtpTipo());                        
            
            for(CorreoElectronico ce:correos){
                
                if(ce==null) continue;
                
                try {                
                    //Si estamos en modo prueba siempre cambiamo el destinatario del correo
                    if(p.getModoPrueba().equals("S")){

                        String destinatario = (p.getRecepcionNotificacionModoPrueba()==null?"":p.getRecepcionNotificacionModoPrueba());
                        ce.setDestinatarios(destinatario);
                    }  

                    InternetAddress[] direccionesTo  = getDirecciones(ce.getDestinatarios());                
                    InternetAddress[] direccionesBcc = getDirecciones(p.getSmtpDireccionEnvio());                
                    
                    InternetAddress direccionFrom = null;                    
                    try {
                        direccionFrom = new InternetAddress(p.getSmtpDireccionEnvio(),p.getSmtpNombreEnvio());
                        ce.setDireccionEnvio(direccionFrom.getAddress());
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al generar dirFrom", ex);
                    }

                    if (direccionesTo==null) continue;   
                    
                    if(ce.getAsunto()==null)    ce.setAsunto("Sin asunto");
                    if(ce.getContenido()==null) ce.setContenido("Sin contenido");

                    MimeMessage message = new MimeMessage(session);

                    if(ce.getDirFrom()==null){
                        message.setFrom(direccionFrom);
                    }else{
                        message.setFrom(ce.getDirFrom());
                    }

                    MimeMultipart multiparte = new MimeMultipart("related");
                    
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(ce.getContenido(), "text/html");
                    multiparte.addBodyPart(messageBodyPart);   
                    
                    if(ce.getPathArchivo()!=null){                        
                        if(!ce.getPathArchivo().isEmpty()){
                            BodyPart adjunto = new MimeBodyPart();
                            adjunto.setDataHandler(new DataHandler(new FileDataSource(ce.getPathArchivo())));                            
                            adjunto.setFileName(adjunto.getDataHandler().getName());
                            multiparte.addBodyPart(adjunto);
                        }                        
                    }                    
                    
                    message.setRecipients(Message.RecipientType.TO, direccionesTo);
                    message.setRecipients(Message.RecipientType.BCC, direccionesBcc);
                    message.setSubject(ce.getAsunto());
                    message.setContent(multiparte);
                                    
                    if (!transport.isConnected()) {
                        transport.connect(p.getSmtpHost(), p.getSmtpUsuario(), p.getSmtpPassword());                        
                    }
                    
                    transport.sendMessage(message, message.getAllRecipients());                    
                    guardarCorreoElectronico(ce, "S");                    
                } catch (MessagingException messagingException){
                    ce.setError(messagingException.getMessage());
                    guardarCorreoElectronico(ce, "N");
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al enviar correos electrónicos", messagingException);
                }
                
            } // Fin for
            
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al enviar correos electrónicos", ex);
        } finally {
            
            if(transport !=null){
                try {
                    transport.close();
                } catch (MessagingException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al enviar correos electrónicos", ex);
                }
            }
        }
    }
    
    public String generarMensaje(String titulo,List<String> contenido) {

        String mensaje = "";
        for (String sContenido:contenido) {

            mensaje += "<p> ";
            mensaje +="    <span style=\"font-family: 'trebuchet ms', geneva, sans-serif;\">";
            mensaje += sContenido;
            mensaje +="    </span>";
            mensaje +="</p> ";
        }
       
        return p.getPlantillaNotificaciones().replace("sTitulo", titulo).replace("sContenido", mensaje);        
    }

    public String validarDestinatario(String email) throws ExcepcionGeneralSistema {
        
            //Quitamos los espacios en blanco
            email = email.trim();
            
            String destinatario;
            
            if (p.getModoPrueba().equals("S")) {
                System.err.println("Modo prueba, cambia destinatario");
                destinatario = (p.getRecepcionNotificacionModoPrueba() == null ? "" : p.getRecepcionNotificacionModoPrueba());
            } else {
                destinatario = (email == null ? "" : email);
            }
            
            if (destinatario.isEmpty()) {
               throw  new ExcepcionGeneralSistema("Cuenta de correo inválida");                         
            }
            
            String[] direcciones = destinatario.split(";");  
            
            for(String s: direcciones){
                
                s = s.trim();
                
                if(!JsfUtil.validateEmail(s)){
                    throw  new ExcepcionGeneralSistema("Cuenta de correo inválida:" + s);      
                }
            }
            
            return destinatario;        
    }

    private InternetAddress[] getDirecciones(String direccionesInternet) {
        try {
            
            
            if(direccionesInternet==null) return null;
            if(direccionesInternet.isEmpty()) return null;
            
            String[] direcciones = direccionesInternet.trim().split(";");                
            if(direcciones==null || direcciones.length<1) return null;
            
            int cantDir = 0;
            for (int i = 0; i < direcciones.length; i++){
                if(direcciones[i] !=null){
                    if(!direcciones[i].trim().isEmpty()){
                        cantDir++;
                    }
                }
            }                       
            
            InternetAddress[] direccionesTo = new InternetAddress[cantDir];            
            cantDir = 0;

            for (int i = 0; i < direcciones.length; i++){
                if(direcciones[i] !=null){
                    if(!direcciones[i].isEmpty()){
                            direccionesTo[cantDir] = new InternetAddress(direcciones[cantDir].trim());
                            cantDir++;
                        }
                    }
                }
            
            return direccionesTo;                    
                    
        } catch (AddressException e) {            
            System.err.print("Error al enviar correos - Dirección inválida " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        
    }

    public void envioPruebaHTML(){        
        
        iniciar();
        CorreoElectronico ce = new CorreoElectronico("Prueba de envío", p.getRecepcionNotificacionModoPrueba(), "Esto es una prueba del funcionamiento de las notificaciones");
        List<CorreoElectronico> correos = new  ArrayList<CorreoElectronico>();  
        correos.add(ce);
        enviarCorreosElectronicos(correos);
       
    }

    private void guardarCorreoElectronico(CorreoElectronico correo, String enviado) {
        
        if(correo==null) return;
        
        try {
            correo.setEnviado(enviado);
            correoElectronicoRN.guardar(correo);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No guardó correo electrónico", ex);
        }                
    }

}

