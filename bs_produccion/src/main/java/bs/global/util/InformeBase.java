/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.global.util;

import bs.administracion.modelo.CorreoElectronico;
import bs.administracion.modelo.Reporte;
import bs.administracion.rn.ReporteRN;
import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.web.AplicacionBean;
import bs.global.web.GenericBean;
import bs.global.web.MailFactory;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
import javax.naming.NamingException;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.context.RequestContext;

/**
 * @author Claudio Clase para generar informes
 */
public abstract class InformeBase extends GenericBean implements Serializable {

    @EJB
    protected MailFactory mailFactoryBean;

    @EJB
    protected ReporteRN reporteRN;
    
    protected Map parameters;
    protected String codigoReporte;
    protected Reporte reporte;

    protected boolean todoOk;
    protected Integer copias;

    @ManagedProperty(value = "#{reportFactory}")
    protected ReportFactory reportFactory;

    @ManagedProperty(value = "#{aplicacionBean}")
    protected AplicacionBean aplicacionBean;

    @PostConstruct
    public void init() {

        copias = 1;

    }

    public InformeBase() {

        muestraReporte = false;
        solicitaEmail = false;
        emailEnvioComprobante = "";
        parameters = new HashMap();

    }

    public abstract void validarDatos() throws ExcepcionGeneralSistema;

    public abstract void cargarParametros() throws ExcepcionGeneralSistema;

    public abstract void resetParametros() throws ExcepcionGeneralSistema;

    public void reporteToPdf() {

        RequestContext context = RequestContext.getCurrentInstance();
        try {

            validarDatos();
            cargarParametros();

            reportFactory.exportReportToPdfFile(reporte, nombreArchivo, parameters);
            muestraReporte = true;
            todoOk = true;

        } catch (ExcepcionGeneralSistema e) {

            JsfUtil.addErrorMessage("No se puede ejecutar reporte pdf " + e);
            todoOk = false;
            muestraReporte = false;

        } catch (JRException e) {

            JsfUtil.addErrorMessage("No se puede ejecutar reporte pdf " + e);
            todoOk = false;
            muestraReporte = false;

        } catch (Exception e) {

            JsfUtil.addErrorMessage("No se puede ejecutar reporte pdf " + e);
            e.printStackTrace();
            todoOk = false;
            muestraReporte = false;

        }

        if (muestraReporte) {
            context.execute("PF('dlg_reporte').show()");
        }
    }

    public void reporteToXls() {
        try {
            validarDatos();
            cargarParametros();
            reportFactory.exportReportToXlsFile(reporte, nombreArchivo, parameters);

        } catch (ExcepcionGeneralSistema ex) {
            JsfUtil.addErrorMessage("No se puede ejecutar reporte xls " + ex);
        } catch (NamingException ex) {
            Logger.getLogger(InformeBase.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No se puede ejecutar reporte xls " + ex);
        } catch (SQLException ex) {
            Logger.getLogger(InformeBase.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No se puede ejecutar reporte xls " + ex);
        } catch (JRException ex) {
            Logger.getLogger(InformeBase.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No se puede ejecutar reporte xls " + ex);
        } catch (IOException ex) {
            Logger.getLogger(InformeBase.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("No se puede ejecutar reporte xls " + ex);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("No se puede ejecutar reporte xls " + ex);
            Logger.getLogger(InformeBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reporteToEmail(CorreoElectronico ce) throws Exception {

        if (aplicacionBean.getParametro().getEnviaNotificaciones() == 'S') {

            String pathPDF = reportFactory.getPathTemporales() + nombreArchivo + ".pdf";

            if (pathPDF == null || pathPDF.isEmpty()) {
                throw new ExcepcionGeneralSistema("No existe reporte o archivo a enviar");
            }

            ce.setPathArchivo(pathPDF);

            List<CorreoElectronico> correos = new ArrayList<CorreoElectronico>();
            correos.add(ce);

            SenderEmail se = new SenderEmail(mailFactoryBean, correos);
            se.start();

            JsfUtil.addInfoMessage("El envío fue exitoso. Destino: " + emailEnvioComprobante);

        } else {
            JsfUtil.addWarningMessage("No tiene permiso para enviar notificaciones");
        }

        solicitaEmail = false;
    }

    //----------------------------------------------------------------------------------
    public ReportFactory getReportFactory() {
        return reportFactory;
    }

    public void setReportFactory(ReportFactory reportFactory) {
        this.reportFactory = reportFactory;
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public boolean isTodoOk() {
        return todoOk;
    }

    public void setTodoOk(boolean todoOk) {
        this.todoOk = todoOk;
    }

    public Integer getCopias() {
        return copias;
    }

    public void setCopias(Integer copias) {
        this.copias = copias;
    }

    public AplicacionBean getAplicacionBean() {
        return aplicacionBean;
    }

    public void setAplicacionBean(AplicacionBean aplicacionBean) {
        this.aplicacionBean = aplicacionBean;
    }

    public MailFactory getMailFactoryBean() {
        return mailFactoryBean;
    }

    public void setMailFactoryBean(MailFactory mailFactoryBean) {
        this.mailFactoryBean = mailFactoryBean;
    }

    public String getCodigoReporte() {
        return codigoReporte;
    }

    public void setCodigoReporte(String codigoReporte) {
        this.codigoReporte = codigoReporte;
    }

}
