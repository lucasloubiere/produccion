/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.administracion.web;

import bs.administracion.modelo.Parametro;
import bs.administracion.modelo.Reporte;
import bs.administracion.rn.ParametrosRN;
import bs.administracion.rn.ReporteRN;
import bs.global.util.JsfUtil;
import bs.global.web.GenericBean;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Agustin
 */
@ManagedBean
@ViewScoped
public class ReporteBean extends GenericBean implements Serializable {

    private Reporte reporte;
    private List<Reporte> lista;
    private Parametro parametro;

    private boolean seleccionaTodoVisible;
    private boolean seleccionaTodoSoloLectura;
    private boolean seleccionaTodoRequerido;

    @EJB
    private ReporteRN reporteRN;
    @EJB
    private ParametrosRN parametrosRN;
    
    @ManagedProperty(value = "#{moduloBean}")
    private ModuloBean moduloBean; 
        

    public ReporteBean() {

    }

    @PostConstruct
    public void init() {
        txtBusqueda = "";
        parametro = parametrosRN.getParametro("01");
        mostrarDebaja = false;
        nuevo();
        buscar();
    }

    public void nuevo() {

        esNuevo = true;
        buscaMovimiento = false;
        reporte = new Reporte();
        reporte.setOrigen(parametro.getOrigenPorDefecto());
        obtenerCodigo();
    }

    public void guardar(boolean nuevo) {

        try {
            if (reporte != null) {
                
                if (reporte.getOrigen().equals("SIS") && !parametro.getOrigenPorDefecto().equals("SIS")) {
                    JsfUtil.addErrorMessage("No es posible modificar un menú de sistema, duplicar el menú y guardarlo como menu usuario.");
                    return;
                }

                reporteRN.guardar(reporte, esNuevo);
                esNuevo = false;
                JsfUtil.addInfoMessage("Los datos se guardaron correctamente");

                if (nuevo) {
                    nuevo();
                }
            } else {
                JsfUtil.addInfoMessage("No hay datos para guardar");
            }
        } catch (Exception ex) {
            Logger.getLogger(ReporteBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible guardar los datos " + ex);
        }
    }

    public void duplicar() {

        if (reporte == null) {
            JsfUtil.addErrorMessage("No hay reporte activo o la lista de parámetros es nula");
            return;
        }

        Reporte reporteAux = reporte;
        reporte = new Reporte();
        reporte.setNombre(reporteAux.getNombre());
        reporte.setPath(reporteAux.getPath());
        reporte.setOrigen(parametro.getOrigenPorDefecto());
        reporte.setModulo(reporteAux.getModulo());

        obtenerCodigo();
        esNuevo = true;
    }

    public void habilitaDeshabilita(String habDes) {

        try {
            reporte.getAuditoria().setDebaja(habDes);
            reporteRN.guardar(reporte, false);
            JsfUtil.addInfoMessage("Los datos se actualizaron correctamente");

        } catch (Exception ex) {
            Logger.getLogger(ReporteBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible actualizar los datos " + ex);
        }
    }

    public void eliminar() {
        try {
            reporteRN.eliminar(reporte);
            nuevo();
            JsfUtil.addInfoMessage("Los datos fueron borrados");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No es posible borrar los datos " + ex);
        }
    }

    public void buscar() {
        
        if(moduloBean.getModulo()!=null && moduloBean.getModulo().getCodigo()!=null){
            filtro.clear();
            filtro.put("modulo.codigo = ", "'"+moduloBean.getModulo().getCodigo()+"'");
        }
        
        lista = reporteRN.getListaByBusqueda(filtro,txtBusqueda, mostrarDebaja);
    }

    public List<Reporte> complete(String query) {
        try {
            lista = reporteRN.getListaByBusqueda(query, false);
            return lista;

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Reporte>();
        }
    }

    public void onSelect(SelectEvent event) {
        reporte = (Reporte) event.getObject();
        esNuevo = false;
        buscaMovimiento = false;
    }

    public void seleccionar(Reporte d) {

        reporte = d;
        esNuevo = false;
        buscaMovimiento = false;
    }

    public void imprimir() {

        if (reporte == null) {
            JsfUtil.addErrorMessage("No hay entidad seleccionada, nada para imprimir");
        }
    }

    public void obtenerCodigo() {

        if (reporte == null || reporte.getOrigen() == null) {
            return;
        }

        String codigo = reporteRN.obtenerSiguienteCogido(reporte.getOrigen());
        reporte.setCodigo(codigo);

    }

    public void upload(FileUploadEvent event) {
        
        if(reporte.getModulo()==null){
            JsfUtil.addErrorMessage("Primero debe seleccionar el módulo");
        }

        try {
            copiarArchivo(event.getFile().getFileName(), event.getFile().getInputstream());
//            JsfUtil.addInfoMessage("El reporte ha sido procesado con éxito");
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
     */
    public void copiarArchivo(String fileName, InputStream in) {
        try {

            //String[] split = fileName.split("\\.");
            //String extension = split[split.length - 1].toLowerCase();
            String archivo = reporte.getModulo().getCodigo()                    
                    + (reporte.getGrupo()==null || reporte.getGrupo().isEmpty()?"":File.separator+reporte.getGrupo())
                    +  File.separator + fileName;
            File folder = new File(parametro.getPathCarpetaReportes() 
                    + reporte.getModulo().getCodigo()
                    + (reporte.getGrupo()==null || reporte.getGrupo().isEmpty()?"":File.separator+reporte.getGrupo()));

            //Verificamos carpeta 
            if (!folder.isDirectory()) {
                folder.mkdirs();
            }

            File file = new File(parametro.getPathCarpetaReportes() + archivo);

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
            
            reporte.setPath(archivo);

        } catch (Exception e) {
            System.err.println("error " + e);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, "Error cargando archivo: " + e);
        }
    }

    //--------------------------------------------------------------------------
    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    public List<Reporte> getLista() {
        return lista;
    }

    public void setLista(List<Reporte> lista) {
        this.lista = lista;
    }

    public Parametro getParametro() {
        return parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }

    public boolean isSeleccionaTodoVisible() {
        return seleccionaTodoVisible;
    }

    public void setSeleccionaTodoVisible(boolean seleccionaTodoVisible) {
        this.seleccionaTodoVisible = seleccionaTodoVisible;
    }

    public boolean isSeleccionaTodoSoloLectura() {
        return seleccionaTodoSoloLectura;
    }

    public void setSeleccionaTodoSoloLectura(boolean seleccionaTodoSoloLectura) {
        this.seleccionaTodoSoloLectura = seleccionaTodoSoloLectura;
    }

    public boolean isSeleccionaTodoRequerido() {
        return seleccionaTodoRequerido;
    }

    public void setSeleccionaTodoRequerido(boolean seleccionaTodoRequerido) {
        this.seleccionaTodoRequerido = seleccionaTodoRequerido;
    }

    public ModuloBean getModuloBean() {
        return moduloBean;
    }

    public void setModuloBean(ModuloBean moduloBean) {
        this.moduloBean = moduloBean;
    }
    
}
