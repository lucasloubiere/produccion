/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.sincronizacion.web;

import bs.global.util.JsfUtil;
import bs.sincronizacion.rn.SincronizacionRN;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lloubiere
 */
@ManagedBean
@SessionScoped
public class sincronizacionBean {

    private Date fecha;
    private Date fechaDesde;
    private Date fechaHasta;
    private String empresa;
    private String log;

    @EJB
    private SincronizacionRN sincronizacionRN;

    /**
     * Creates a new instance of TestWS
     */
    public sincronizacionBean() {
    }

    public void sincronizar() {

        String sResultado = sincronizacionRN.sincronizarMovimientosBalanza(fecha, empresa);
        fecha = sumarDiasAFecha(fecha, 1);
        JsfUtil.addInfoMessage(sResultado);
    }

    public void sincronizarEntreFechas() {

        log = "";
        fecha = fechaDesde;

        while (!fecha.after(fechaHasta)) {

            String sResultado = sincronizacionRN.sincronizarMovimientosBalanza(fecha, empresa);
            fecha = sumarDiasAFecha(fecha, 1);
            JsfUtil.addInfoMessage(sResultado);
        }
    }

    public static Date sumarDiasAFecha(Date fecha, int dias) {
        if (dias == 0) {
            return fecha;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

}
