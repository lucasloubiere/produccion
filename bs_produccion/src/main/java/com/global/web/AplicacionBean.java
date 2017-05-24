/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.global.web;

import com.administracion.modelo.Empresa;
import com.administracion.modelo.Parametro;
import com.administracion.rn.EmpresaRN;
import com.administracion.rn.ParametrosRN;
import com.global.util.GenericBean;
import com.global.util.JsfUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.primefaces.event.FileUploadEvent;


/** 
 * @author Claudio
 */

@ManagedBean
@ApplicationScoped
public class AplicacionBean extends GenericBean implements Serializable{
    
    @EJB private ParametrosRN parametroRN;
    @EJB private EmpresaRN empresaRN;
    

    private Empresa empresa;
    private Parametro parametro;
    
   /** Creates a new instance of Empresa */
   public AplicacionBean() {
   }

   @PostConstruct
   public void init (){     
        try {
            empresa = empresaRN.getEmpresa("01");
            parametro = parametroRN.getParametro(empresa.getCodigo());            
            
            verificarCarpetasSistemas();
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "init", ex.getCause());
        }       
   }
   
   public void verificarCarpetasSistemas(){
       
       if(parametro==null) return;
       if(parametro.getCarpetaTemporales()==null){
           parametro.setCarpetaTemporales(empresa.getRazonSocial().replaceAll(" ", ""));
       }
       
       parametro.setPathTemporales(System.getProperty("catalina.base")+ "\\docroot\\" + parametro.getCarpetaTemporales()+"\\");         
       
       //System.err.println(parametro.getPathTemporales());
       
       try {
           File folder = new File(parametro.getPathTemporales());

           //Verificamos carpeta principal de la empresa, de lo contrario la creamos
           if (!folder.isDirectory()) {
               System.err.println("Crea directorio");
               folder.mkdirs();
           }

           //Verificamos carpeta de productos de la empresa, de lo contrario la creamos       
           folder = new File(parametro.getPathTemporales() + "productos");
           if (!folder.isDirectory()) {
               System.err.println("Crea directorio productos");
               folder.mkdirs();
           }

           //Verificamos carpeta de productos de la empresa, de lo contrario la creamos       
           folder = new File(parametro.getPathTemporales() + "procesos");
           if (!folder.isDirectory()) {
               System.err.println("Crea directorio procesos");
               folder.mkdirs();
           }

           //Verificamos carpeta de productos de la empresa, de lo contrario la creamos       
           folder = new File(parametro.getPathTemporales() + "temporales");
           if (!folder.isDirectory()) {
               System.err.println("Crea directorio temporales");
               folder.mkdirs();
           }
           
           //Verificamos carpeta de productos de la empresa, de lo contrario la creamos       
           folder = new File(parametro.getPathTemporales() + "usuarios");
           if (!folder.isDirectory()) {
               System.err.println("Crea directorio usuarios");
               folder.mkdirs();
           }
           
       } catch (Exception e){
           
           System.err.println("Problemas para verificar/crear directorios de empresa " + e);
       }
   }
   
   public void actualizarDatos() throws Exception {
        empresa = empresaRN.getEmpresa("01");
        parametro = parametroRN.getParametro(empresa.getCodigo());
        
   }   
      
   public void upload(FileUploadEvent event) {  
        
        try {                     
            copiarArchivo(event.getFile().getFileName(), event.getFile().getInputstream());              
            JsfUtil.addInfoMessage("El archivo ha sido procesado con éxito");
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
   public String copiarArchivo(String fileName, InputStream in) {
       try {
           
           String[] split = fileName.split("\\.");
           String extension = split[split.length - 1].toLowerCase();               
           String archivo = split[0]+"."+extension;

           File file = new File(System.getProperty("catalina.base")+ "\\docroot\\"
                   +parametro.getCarpetaTemporales()
                   +"\\"
                   +archivo);
                      
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
           
           return file.getAbsolutePath();

        } catch (IOException e) {
            System.out.println("Error copiando archivo: "+e);
            return "";
        }
    }
      
   public Empresa getEmpresa() {
       return empresa;
   }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Parametro getParametro() {
        return parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }    
}
