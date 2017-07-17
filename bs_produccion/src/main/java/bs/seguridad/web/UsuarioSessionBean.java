/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.seguridad.web;


import bs.administracion.modelo.CorreoElectronico;
import bs.global.util.JsfUtil;
import bs.global.web.AplicacionBean;
import bs.global.web.GenericBean;
import bs.global.web.MailFactory;
import bs.global.web.PrincipalBean;
import bs.seguridad.modelo.Usuario;
import bs.seguridad.rn.UsuarioRN;
import bs.stock.web.ProductoBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpSession;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.menu.MenuItem;

/**
 *
 * @author Caio
 */
@ManagedBean
@SessionScoped
public class UsuarioSessionBean extends GenericBean implements Serializable {

    @EJB private UsuarioRN usuarioRN;    
    @EJB private MailFactory mailFactoryBean;
    
    private String nombreUsuario;
    private String claveUsuario;
    
    boolean estaRegistrado;
    private Usuario usuario;
    private List<MenuItem> menuUsuario;
    private int cantIntentos;
    private boolean muestroMensajeExpirado;
    
    @ManagedProperty(value = "#{principalBean}")
    protected PrincipalBean principalBean;
    
    @ManagedProperty(value = "#{aplicacionBean}")
    protected AplicacionBean aplicacionBean;
    
    @PostConstruct
    public void init() {
        setUsuario(new Usuario());
        estaRegistrado = false;
        cantIntentos = 0;
    }
    
    @PreDestroy
    public void destroy(){
        
        if(usuario==null) return;
        
        if(aplicacionBean.getUsuarioLogueados().contains(usuario)){
            aplicacionBean.getUsuarioLogueados().remove(usuario);
        }
    }
    
    public void guardar(){
        
        try {
            if (usuario != null) {
                
                usuarioRN.guardar(usuario);
                esNuevo = false;
                
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");
                
            }else{
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }

    public Usuario getUsuario(){
        
        if (usuario == null) {
            usuario = new Usuario();
        }
        return usuario;
    }

    public void checkLogin() throws IOException {
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (this.usuario.getUsuario() == null) {            
            context.redirect(context.getRequestContextPath()+"/global/inicio.jsf");
        }
    }
    
    public void checkInicio() throws IOException {
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (this.usuario.getUsuario() != null) {                        
            
            if(estaRegistrado){
                context.redirect(context.getRequestContextPath()+"/global/principal.jsf");
            }
        }
    }
    
    public void oncapture(CaptureEvent captureEvent) {
        //filename = getRandomImageName();
        byte[] data = captureEvent.getData();
        
        String archivo = usuario.getUsuario()+ ".jpeg"; 
        File file = new File(aplicacionBean.getParametro().getPathCarpetaUsuarios()+archivo);
        
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(file);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            
            usuario.setImagen(archivo);           
            guardar();
        }
        catch(IOException e) {
            throw new FacesException("Errro al obtener imagen", e);
        }
    }
    
    public void upload(FileUploadEvent event) {  
        
        try {                     
            copiarArchivo(event.getFile().getFileName(), event.getFile().getInputstream());              
            JsfUtil.addInfoMessage("La imagen ha sido procesada con éxito");
        } catch (IOException e) {
            System.err.println("Error subiendo archivo " + e);
            JsfUtil.addErrorMessage("No es posible procesar el archivo");
        }
    }     
   
    /**
    * Copiar un archivo a la carpeta temporales de la empresa
    * @param fileName
    * @param in 
     * @return  Path del archivo generado
    */   
    public void copiarArchivo(String fileName, InputStream in) {
       try {
           
           String[] split = fileName.split("\\.");
           String extension = split[split.length - 1].toLowerCase();               
           String archivo = usuario.getUsuario()+"."+extension;

           File file = new File(aplicacionBean.getParametro().getPathCarpetaUsuarios()+archivo);
                      
           // write the inputStream to a FileOutputStream
           OutputStream out = new FileOutputStream(file);
                      
           int read = 0;
           byte[] bytes = new byte[1024];           
           
           while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);           
           }           
           in.close();           
           out.flush();           
           out.close();    
           
           usuario.setImagen(archivo);           
           guardar();
           
        } catch (Exception e) {
            Logger.getLogger(ProductoBean.class.getName()).log(Level.SEVERE, null,"Error cargando imagen archivo: "+ e);            
        }
    }

    //----------------------------------------------------------------------
    public String login() {
        
        
        Usuario usuAux = null;
        try {
            //usuAux = usuarioDAO.getUsuarioByEmail(usuario.getEmail());
            usuAux = usuarioRN.getUsuarioByNombre(nombreUsuario);            
            
        } catch (Exception e) {            
        }

        //Valida que exista el usuario
        if (usuAux == null) {
            JsfUtil.addErrorMessage("El usuario '" + nombreUsuario + "' no se encuentra registrado");            
            return "";
        }
        
        cantIntentos++;

        if (cantIntentos == 3) {
            
            recuperarClave();
            
//            EstadoUsuario estado = estadoDAO.getEstado(2); //Bloqueado
//            usuAux.setEstado(estado);
//            try {
//                usuarioRN.guardar(usuAux);
//            } catch (Exception ex) {
//                
//            }
            JsfUtil.addErrorMessage("Se ha enviado información de acceso a la cuenta de correo asociada al usario.");
            JsfUtil.addErrorMessage("Verifique su bandeja de entrada y vuelva a intentar");
            cantIntentos = 0;
            return "";
        }

        //Valida que no se encuentre inactivo
        if (usuAux.getEstado().getDescripcion().equals("Inactivo")) {
            JsfUtil.addErrorMessage("El usuario '" + usuAux.getUsuario() + "' se encuentra " + usuAux.getEstado().getDescripcion());
            System.out.println("Usuario inactivo");
            return "";
        }

        //Valida que no se encuentre bloqueado
        if (usuAux.getEstado().getDescripcion().equals("Bloqueado")) {
            JsfUtil.addErrorMessage("El usuario '" + usuAux.getUsuario() + "' se encuentra " + usuAux.getEstado().getDescripcion());            
            return "";
        }
        
        //Valida que no se encuentre desabilitado
        if (usuAux.getAuditoria().getDebaja().equals("S")) {
            JsfUtil.addErrorMessage("El usuario '" + usuAux.getUsuario() + "' se encuentra desabilitado");            
            return "";
        }
        
//        //Valida que no se encuentre logueado
//        if (aplicacionBean.getUsuarioLogueados().contains(usuAux)) {
//            JsfUtil.addErrorMessage("El usuario '" + usuAux.getUsuario() + "' ya se encuentra logueado en la plataforma");            
//            return "";
//        }

        //Validamos la contraseña
        if (!claveUsuario.equals(usuAux.getPassword())) {
            JsfUtil.addErrorMessage("Password incorrecto");
            getUsuario().setPassword("");
            return "";
        }

        usuario = usuAux;
        estaRegistrado = true;
        principalBean.cargarMenu(usuario);
        aplicacionBean.getUsuarioLogueados().add(usuAux);

        switch (usuario.getTipo().getId()) {
            
            //Administrador
            case 1:
                return "../global/principal.xhtml?faces-redirect=true";
            //Usuario 
            case 2:
                return "../global/principal.xhtml?faces-redirect=true";
            
            default:
                return "../global/principal.xhtml?faces-redirect=true";
        }
    }

    public String logout() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);        
        if(aplicacionBean.getUsuarioLogueados().contains(usuario)){
            aplicacionBean.getUsuarioLogueados().remove(usuario);
        }        
        setUsuario(null);
        setUsuario(new Usuario());
        estaRegistrado = false;
        session.invalidate();
        return "../seguridad/login.xhtml";
    }

    public void monitorearSesion() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        if (this.usuario.getUsuario() == null) {
            context.redirect("global/inicio.xhtml");
        }
        /**
        System.out.println("Sesión expirada " + new Date());
        invalidarSession();
        */
    }

    public void invalidarSession(){

        FacesContext context = javax.faces.context.FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        System.out.println("Usuario logout: " + usuario.getUsuario() + " Hora: " + new Date()  ) ;
        
        if(aplicacionBean.getUsuarioLogueados().contains(usuario)){
            aplicacionBean.getUsuarioLogueados().remove(usuario);
        }        
        usuario = null;
        usuario = new Usuario();
        estaRegistrado = false;
        session.invalidate();
        HttpSession sessionNew = (HttpSession) context.getExternalContext().getSession(true);
    }
    
    public String recuperarClave(){

        Usuario usuAux = null;
        
        try {            
            usuAux = usuarioRN.getUsuarioByNombre(usuario.getUsuario());            
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Problemas para buscar usuario " + e);
        }

        //Valida que exista el usuario
        if (usuAux == null) {
            JsfUtil.addErrorMessage("El usuario '" + getUsuario().getUsuario() + "' no se encuentra registrado");            
            return "";
        }
        
        usuario = usuAux;
        
        if(usuAux.getEmail()==null || usuAux.getEmail().isEmpty()){
            JsfUtil.addInfoMessage("El usuario no tiene asociada una cuenta de e-mail");
            return "";
        }
        
        //Si todo está bien comienza el proceso recupero de clave
        //renovarClave();
        
        //Enviamos el e-mail.
        informeEnvioClaveUsuario();
        
        cantIntentos = 0;
        return "../global/inicio.xhtml";
    }
    
    public void informeEnvioClaveUsuario(){

        List<String> parrafos = new ArrayList<String>();
        parrafos.add("Estimado usuario:");
        parrafos.add("De acuerdo a su solicitud, enviamos su clave de acceso.");
        
        parrafos.add("Sus datos de acceso:");
        parrafos.add("Nombre de usuario: "+usuario.getNombre());
        parrafos.add("Nueva clave: "+usuario.getPassword());
        parrafos.add("");
                
        CorreoElectronico  ce = new CorreoElectronico(
                "Solicitud de clave", 
                usuario.getEmail(),
                mailFactoryBean.generarMensaje("Solicitud de clave",parrafos));
                
        if(mailFactoryBean.enviarCorreoElectronico(ce)){
            JsfUtil.addInfoMessage("Se ha enviado un correo electrónico a la casilla de e-mail asociada al usuario ingresado","");
        }else{
            JsfUtil.addWarningMessage("Disculpe las molestias, actualmente no podemos procesar su solicitud","");
        }  
    }    
        
        
    //------------------------------------------------------------------------

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
        
    public List<MenuItem> getMenuUsuario() {
        return menuUsuario;
    }

    public void setMenuUsuario(List<MenuItem> menuUsuario) {
        this.menuUsuario = menuUsuario;
    }

    public boolean isEstaRegistrado() {
        return estaRegistrado;
    }

    public void setEstaRegistrado(boolean estaRegistrado) {
        this.estaRegistrado = estaRegistrado;
    }
    
    public boolean isMuestroMensajeExpirado() {
        return muestroMensajeExpirado;
    }

    public void setMuestroMensajeExpirado(boolean muestroMensajeExpirado) {
        this.muestroMensajeExpirado = muestroMensajeExpirado;
    }

    public PrincipalBean getPrincipalBean() {
        return principalBean;
    }

    public void setPrincipalBean(PrincipalBean principalBean) {
        this.principalBean = principalBean;
    }
    
    public AplicacionBean getAplicacionBean() {
        return aplicacionBean;
    }

    public void setAplicacionBean(AplicacionBean aplicacionBean) {
        this.aplicacionBean = aplicacionBean;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public boolean esAdministrador() {
        try {
            return estaRegistrado
                    && usuario != null
                    && usuario.getTipo().getId() == 1;
        } catch (Exception e) {
            return false;
        }
    }
    
}
