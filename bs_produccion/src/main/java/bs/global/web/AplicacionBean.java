/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.global.web;

import bs.administracion.modelo.Empresa;
import bs.administracion.modelo.Parametro;
import bs.administracion.rn.EmpresaRN;
import bs.administracion.rn.ParametrosRN;
import bs.global.modelo.MonedaValores;
import bs.global.rn.MonedaRN;
import bs.global.util.JsfUtil;
import bs.seguridad.modelo.Usuario;
import bs.stock.modelo.ParametroStock;
import bs.stock.rn.ParametroStockRN;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 * @author Claudio
 */
@ManagedBean
@ApplicationScoped
public class AplicacionBean extends GenericBean implements Serializable {

    @EJB
    private EmpresaRN empresaRN;
    @EJB
    private MonedaRN monedaRN;
    @EJB
    private ParametrosRN parametrosRN;
    @EJB
    private ParametroStockRN parametroStockRN;

    private Empresa empresa;
    private Parametro parametro;
    private ParametroStock parametroStock;
    private List<Usuario> usuarioLogueados;
    private MonedaValores moneda;

    /**
     * Creates a new instance of Empresa
     */
    public AplicacionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            empresa = empresaRN.getEmpresa("01");
            parametro = parametrosRN.getParametro();
            parametroStock = parametroStockRN.getParametro();
            usuarioLogueados = new ArrayList<Usuario>();

            actualizarCotizacion();
            verificarCarpetasSistemas();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error en init", ex);
        }
    }

    public void verificarCarpetasSistemas() {

        if (parametro == null) {
            return;
        }

        if (parametro.getNombreCarpetaArchivos()== null) {
            parametro.setNombreCarpetaArchivos("arc_"+empresa.getRazonSocial().replaceAll(" ", ""));
        }

        //System.err.println(parametro.getPathTemporales());
        try {
            File folder = new File(parametro.getPathCarpetaArchivos());

            //Verificamos carpeta principal de la empresa, de lo contrario la creamos
            if (!folder.isDirectory()) {
                System.err.println("Crea directorio");
                folder.mkdirs();
            }

            //Verificamos carpeta de productos de la empresa, de lo contrario la creamos       
            folder = new File(parametro.getPathCarpetaProductos());
            if (!folder.isDirectory()) {
                System.err.println("Crea directorio productos");
                folder.mkdirs();
            }

            //Verificamos carpeta de productos de la empresa, de lo contrario la creamos       
            folder = new File(parametro.getPathCarpetaProcesos());
            if (!folder.isDirectory()) {
                System.err.println("Crea directorio procesos");
                folder.mkdirs();
            }

            //Verificamos carpeta de productos de la empresa, de lo contrario la creamos       
            folder = new File(parametro.getPathCarpetaTemporales());
            if (!folder.isDirectory()) {
                System.err.println("Crea directorio temporales");
                folder.mkdirs();
            }

            //Verificamos carpeta de productos de la empresa, de lo contrario la creamos       
            folder = new File(parametro.getPathCarpetaUsuarios());
            if (!folder.isDirectory()) {
                System.err.println("Crea directorio usuarios");
                folder.mkdirs();
            }
            
            //Verificamos carpeta de reportes de la empresa, de lo contrario la creamos       
            folder = new File(parametro.getPathCarpetaReportes());
            if (!folder.isDirectory()) {
                System.err.println("Crea directorio reportes");
                folder.mkdirs();
            }

        } catch (Exception e) {

            System.err.println("Problemas para verificar/crear directorios de empresa " + e);
        }
    }

    public void actualizarDatos() throws Exception {
        empresa = empresaRN.getEmpresa("01");
        parametro = parametrosRN.getParametro(empresa.getCodigo());
        parametroStock = parametroStockRN.getParametro();
        actualizarCotizacion();
    }

    public void actualizarCotizacion() {

        moneda = monedaRN.getUltimaCotizacion(parametrosRN.getParametro().getCodigoMonedaSecundaria());
        EventBusFactory ebf = EventBusFactory.getDefault();

        if (ebf != null) {
            EventBus eventBus = ebf.eventBus();
            eventBus.publish("/cotizacion", String.valueOf(moneda));
        }
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
     *
     * @param fileName
     * @param in
     * @return Path del archivo generado
     */
    public String copiarArchivo(String fileName, InputStream in) {
        try {

            String[] split = fileName.split("\\.");
            String extension = split[split.length - 1].toLowerCase();
            String archivo = split[0] + "." + extension;

            File file = new File(parametro.getPathCarpetaTemporales() + archivo);

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
            System.out.println("Error copiando archivo: " + e);
            return "";
        }
    }

    
    
    //------------------------------------------------------------------------------------------
    
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

    public List<Usuario> getUsuarioLogueados() {
        return usuarioLogueados;
    }

    public void setUsuarioLogueados(List<Usuario> usuarioLogueados) {
        this.usuarioLogueados = usuarioLogueados;
    }

    public ParametroStock getParametroStock() {
        return parametroStock;
    }

    public void setParametroStock(ParametroStock parametroStock) {
        this.parametroStock = parametroStock;
    }

    public MonedaValores getMoneda() {
        return moneda;
    }

    public void setMoneda(MonedaValores moneda) {
        this.moneda = moneda;
    }

}
