/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.global.util;


import bs.administracion.modelo.Parametro;
import bs.global.web.AplicacionBean;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.util.ByteArrayDataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;


/**
 *
 * @author ctrosch
 */

@ManagedBean
@SessionScoped
public class ReportFactory implements Serializable {
    
    @ManagedProperty(value = "#{aplicacionBean}")
    protected AplicacionBean aplicacionBean;
    
    private Context ctx;
    private javax.sql.DataSource ds;
    private Connection conexion;
    private String pathTemporales;
        
//    @ManagedProperty(value = "#{empresaBean}")
//    protected EmpresaBean empresaBean;
    
    public ReportFactory() throws NamingException, SQLException {
//        ctx = new InitialContext();
//        ds = (javax.sql.DataSource) ctx.lookup("bs-erp");
//        conexion = ds.getConnection();
//        conexion.setAutoCommit(true);
//        context = FacesContext.getCurrentInstance();
    }
    
    @PostConstruct
    public void init(){
        try {
            Parametro p = aplicacionBean.getParametro();
                        
            ctx = new InitialContext();
            
            System.err.println((p.getDataSource()==null?"bs-erp":p.getDataSource()));
            
            ds = (javax.sql.DataSource) ctx.lookup((p.getDataSource()==null?"bs-erp":p.getDataSource()));
            conexion = ds.getConnection();
            conexion.setAutoCommit(true);   
            pathTemporales = System.getProperty("catalina.base")+ "\\docroot\\"+p.getCarpetaTemporales()+"\\";
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "No es posible iniciar ReportFactory", ex);        
        }
        
    }

    public void verReportePDF(String pathReport,String nombreSalida,Map parameters) throws NamingException, SQLException, JRException, IOException {
        
        cargarDatosEmpresa(parameters);
        
        JasperPrint print = JasperFillManager.fillReport(pathReport, parameters, conexion);
        byte[] bytes = JasperExportManager.exportReportToPdf(print);
        
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition","inline;filename="+nombreSalida+".pdf");        
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.setContentType("application/pdf");
        FacesContext.getCurrentInstance().responseComplete();
        
    }

    public void descargarReportePDF(JasperReport jasperReport,String nombreSalida,Map parameters) throws NamingException, SQLException, JRException, IOException {

        cargarDatosEmpresa(parameters);
        
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conexion);
        byte[] bytes = JasperExportManager.exportReportToPdf(print);

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition","attachment;filename="+nombreSalida+".pdf");
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.setContentType("application/pdf");
        FacesContext.getCurrentInstance().responseComplete();

    }
    
    public void exportReportToXlsFile(String pathReport,String nombreSalida,Map parameters)  throws NamingException, SQLException, JRException, IOException, Exception {

        generarExcel(pathReport, nombreSalida, parameters);        
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        
        FileInputStream entrada = new FileInputStream(pathTemporales+ nombreSalida +".xls");
        byte[] lectura = new byte[entrada.available()];
        entrada.read(lectura);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment; filename="+nombreSalida+".xls");
        response.setContentLength(lectura.length);
        response.getOutputStream().write(lectura);
        response.getOutputStream().flush();
        response.getOutputStream().close();
        entrada.close();        
        FacesContext.getCurrentInstance().responseComplete();
    }
    
    
    public void exportReportToPdfFile(String pathReport,String nombreSalida,Map parameters) throws JRException{

        cargarDatosEmpresa(parameters);        
        JasperPrint print = JasperFillManager.fillReport(pathReport, parameters, conexion);
        JasperExportManager.exportReportToPdfFile(print, pathTemporales+ nombreSalida +".pdf");                      
    }
    
    public String exportReportToHtmlFile(String reportName,String nombreSalida,Map parameters) throws NamingException, SQLException, JRException, IOException {

        String pathReport = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") ;

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream(reportName));

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conexion);

        JasperExportManager.exportReportToHtmlFile(print, pathReport+"pedidoweb.html");

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        String linea="";
        String contenido = "";
        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File (pathReport+"pedidoweb.html");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);
            // Lectura del fichero

            while((linea=br.readLine())!=null) contenido+=linea+"\n";

            //System.err.println(contenido);
            return contenido;

        }catch(Exception e){
            e.printStackTrace();

        }finally{
         // En el finally cerramos el fichero, para asegurarnos
         // que se cierra tanto si todo va bien como si salta
         // una excepcion.
         try{
            if( null != fr ){
               fr.close();
            }

            return "";
         }catch (Exception e2){

            e2.printStackTrace();
            return "";
         }
      }
    }
    
    public String exportReportToPdfFileGetPath(String pathReport,String nombreSalida,Map parameters) throws JRException{

        exportReportToPdfFile(pathReport, nombreSalida, parameters);
        return pathTemporales+ nombreSalida +".pdf";
    }
    
    private void generarExcel(String pathReport,String nombreSalida,Map parameters)  throws NamingException, SQLException, JRException, IOException, Exception {
               
        File sourceFile = new File(pathReport);
        JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
        
        String query =  jasperReport.getQuery().getText();
        
        Iterator<Map.Entry<String, Object>> entries;
        entries = parameters.entrySet().iterator();
        while (entries.hasNext()) {
          Map.Entry<String, Object> entry = entries.next();
          String key = entry.getKey();
          Object value = entry.getValue();
          String parametro = "";
         
          if (value.getClass() == Integer.class || value.getClass() == Long.class
              ||value.getClass() == Float.class || value.getClass() == Double.class) {
          
              parametro = String.valueOf(value);              
          
          } else if (value.getClass() == java.util.Date.class || value.getClass() == java.sql.Date.class) {
              
              parametro = JsfUtil.getFechaSQL((java.util.Date) value);
              
          } else {
              parametro = "'"+value+"'";
          }
          
          query = query.replaceAll("\\$P\\{"+key+"\\}", parametro );
          
        }
                    
        query = query.replaceAll("EMPRESA.\\*", "' '" );
        query = query.replaceAll("ad_empresa.\\*", "' '" );
        
        
        
        System.err.println("query" + query);
        
        PreparedStatement stmt = conexion.prepareStatement(query);
        
        ResultSet resultSet = stmt.executeQuery();
        
        ExcelFactory resultSetToExcel = new ExcelFactory(resultSet, nombreSalida);
    
        resultSetToExcel.generate(new File(pathTemporales+ nombreSalida +".xls"));
        
    }
       
    public DataSource getArchivoAdjuntoPDF(String reportName,String nombreSalida,Map parameters) throws JRException{

        JasperPrint jasperPrint = JasperFillManager.fillReport( this.getClass().getClassLoader().getResourceAsStream(reportName),parameters ,conexion);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
        DataSource adjunto =  new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
        return adjunto;

    }
    
    public void cargarDatosEmpresa(Map p) {

//        Empresa e = empresaBean.getEmpresa();
//
//        p.put("EMP_NOMBRE", e.getRazonSocial());
//        p.put("EMP_DIRECION", e.getDireccion());
//        p.put("EMP_PROVINCIA", e.getProvincia());
//        p.put("EMP_PAIS", e.getPais());
//        p.put("EMP_TELEFONO", e.getTelefono());
//        p.put("EMP_FAX", e.getFax());
//        p.put("EMP_EMAIL", e.getEmail());
//        p.put("EMP_CUIT", e.getCuit());
//        p.put("EMP_IB", e.getNroIB());
//        p.put("EMP_INIACT", e.getInicioActividades());
//        p.put("EMP_IVA", e.getCondicionIVA());
//        p.put("EMP_CIUDAD", e.getCiudad());
        p.put("LOGO", FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/image/logo.png"));        
    }

//    public void exportReportToXlsFile(String pathReport,String nombreSalida,Map parameters) throws JRException{
//        
//        cargarDatosEmpresa(parameters);        
//        JasperPrint print = JasperFillManager.fillReport(pathReport, parameters, conexion);        
//        
//        JRXlsExporter exportador = new JRXlsExporter();
//        exportador.setParameter(JRExporterParameter.JASPER_PRINT, print);
//        exportador.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,pathTemporales+ nombreSalida +".xls");
//        exportador.setParameter(JRExporterParameter.IGNORE_PAGE_MARGINS, true);
//        exportador.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);
//        exportador.setParameter(JRXlsAbstractExporterParameter.IS_IGNORE_CELL_BORDER, false);
//        exportador.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,true);
//        exportador.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,true);
//        exportador.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,true);
//        exportador.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,true);
//        
//        try {
//            exportador.exportReport();            
//        } catch (JRException e) {
//            e.printStackTrace();
//        }        
//    }
    
    
    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public javax.sql.DataSource getDs() {
        return ds;
    }

    public void setDs(javax.sql.DataSource ds) {
        this.ds = ds;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    public String getPathTemporales() {
        return pathTemporales;
    }

    public void setPathTemporales(String pathTemporales) {
        this.pathTemporales = pathTemporales;
    }

    public AplicacionBean getAplicacionBean() {
        return aplicacionBean;
    }

    public void setAplicacionBean(AplicacionBean aplicacionBean) {
        this.aplicacionBean = aplicacionBean;
    }
    
}
