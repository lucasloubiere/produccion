/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.global.util;


import com.global.modelo.Formulario;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author ctrosch
 * Bean genérico que contiene atributos y funcionalidades comunes a todos los bean
 * del sistema
 */

public class GenericBean implements Serializable {
    
    protected Logger log = Logger.getLogger(this.getClass().getName());

    protected Map<String,String> filtro;
    protected Map<String,String> filtroGrupo;
    protected Map<String,String> filtroDetalle;

    protected String titulo;  
    protected boolean beanIniciado = false;
    protected boolean mostrarDebaja;
    protected boolean esNuevo;
    protected boolean detalleVacio;
    protected String txtBusqueda;
    protected int cantidadRegistros;
    
    private int indexTab1;
    private int indexTab2;
    private int indexTab3;
    private int indexTab4;
    private int indexTab5;
           
    protected boolean esAnulacion;
    protected boolean seleccionaMovimiento;  
    protected boolean seleccionaTodo;
    protected boolean seleccionaPendiente;        
    
    protected String nombreArchivo;
    protected String emailEnvioComprobante;
    protected String informacionAdicional;
    protected boolean muestraReporte;
    protected boolean solicitaEmail; 
        
    // VARIABLES PARA BUSQUEDA DE COMPROBANTES
    protected boolean buscaMovimiento;
    protected Formulario formulario;
    protected Integer numeroFormularioDesde;
    protected Integer numeroFormularioHasta;
    protected Date fechaMovimientoDesde;
    protected Date fechaMovimientoHasta;

    /** Creates a new instance of GenericBean */
    public GenericBean() {
        
        filtro = new HashMap<String,String>();
        filtroGrupo = new HashMap<String,String>();
        filtroDetalle = new HashMap<String,String>();

        txtBusqueda = "";
        mostrarDebaja = false;    
        cantidadRegistros = 50;
        
        indexTab1 = 0;
        indexTab2 = 0;
        indexTab3 = 0;
        indexTab4 = 0;
        indexTab5 = 0;
        
    }
    
    public void iniciar(){
        
        
    }
    
    /**
     * Permite controlar los tabs seleccionados luego de actualizar el formulario en la vista del usuario
     * @param event 
     */    
    public void onTab1Change(TabChangeEvent event) {
        
        indexTab1 = getIndexTab(event.getTab().getId());
    }
    
    public void onTab2Change(TabChangeEvent event) {
        
        indexTab2 = getIndexTab(event.getTab().getId());
    }
    
    public void onTab3Change(TabChangeEvent event) {
        
        indexTab3 = getIndexTab(event.getTab().getId());
    }
    
    public void onTab4Change(TabChangeEvent event) {
        
        indexTab4 = getIndexTab(event.getTab().getId());
    }
    
    public void onTab5Change(TabChangeEvent event) {
        
        indexTab5 = getIndexTab(event.getTab().getId());
    }
    
    
    private int getIndexTab(String id){
        
        if(id.equals("t0")) return 0;
        if(id.equals("t1")) return 1;
        if(id.equals("t2")) return 2;
        if(id.equals("t3")) return 3;
        if(id.equals("t4")) return 4;
        if(id.equals("t5")) return 5;
        if(id.equals("t6")) return 6;
        if(id.equals("t7")) return 7;
        if(id.equals("t8")) return 8;
        if(id.equals("t9")) return 9;
        if(id.equals("t10")) return 10;        
        if(id.equals("t11")) return 11;        
        if(id.equals("t12")) return 12;        
        if(id.equals("t13")) return 13;        
        if(id.equals("t14")) return 14;        
        if(id.equals("t15")) return 15;        
        if(id.equals("t16")) return 16;        
        if(id.equals("t17")) return 17;        
        if(id.equals("t18")) return 18;        
        if(id.equals("t19")) return 19;        
        //Por defecto
        return 0;        
    }

    public Map<String, String> getFiltro() {
        return filtro;
    }

    public void setFiltro(Map<String, String> filtro) {
        this.filtro = filtro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String verDatos(){

        return "Datos";
    }

    public String verLista(){

        return "Lista";
    }

    public boolean isMostrarDebaja() {
        return mostrarDebaja;
    }

    public void setMostrarDebaja(boolean mostrarDebaja) {
        this.mostrarDebaja = mostrarDebaja;
    }

    public boolean isEsNuevo() {
        return esNuevo;
    }

    public void setEsNuevo(boolean esNuevo) {
        this.esNuevo = esNuevo;
    }

    public String getTxtBusqueda() {
        return txtBusqueda;
    }

    public void setTxtBusqueda(String txtBusqueda) {
        this.txtBusqueda = txtBusqueda;
    }

    public int getCantidadRegistros() {
        return cantidadRegistros;
    }

    public void setCantidadRegistros(int cantidadRegistros) {
        this.cantidadRegistros = cantidadRegistros;
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public Map<String, String> getFiltroGrupo() {
        return filtroGrupo;
    }

    public void setFiltroGrupo(Map<String, String> filtroGrupo) {
        this.filtroGrupo = filtroGrupo;
    }

    public Map<String, String> getFiltroDetalle() {
        return filtroDetalle;
    }

    public void setFiltroDetalle(Map<String, String> filtroDetalle) {
        this.filtroDetalle = filtroDetalle;
    }

    public boolean isBeanIniciado() {
        return beanIniciado;
    }

    public void setBeanIniciado(boolean beanIniciado) {
        this.beanIniciado = beanIniciado;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getEmailEnvioComprobante() {
        return emailEnvioComprobante;
    }

    public void setEmailEnvioComprobante(String emailEnvioComprobante) {
        this.emailEnvioComprobante = emailEnvioComprobante;
    }

    public String getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(String informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }

    public boolean isMuestraReporte() {
        return muestraReporte;
    }

    public void setMuestraReporte(boolean muestraReporte) {
        this.muestraReporte = muestraReporte;
    }

    public boolean isSolicitaEmail() {
        return solicitaEmail;
    }

    public void setSolicitaEmail(boolean solicitaEmail) {
        this.solicitaEmail = solicitaEmail;
    }

    public boolean isEsAnulacion() {
        return esAnulacion;
    }

    public void setEsAnulacion(boolean esAnulacion) {
        this.esAnulacion = esAnulacion;
    }

    public boolean isBuscaMovimiento() {
        return buscaMovimiento;
    }

    public void setBuscaMovimiento(boolean buscaMovimiento) {
        this.buscaMovimiento = buscaMovimiento;
    }

//    public Formulario getFormulario() {
//        return formulario;
//    }
//
//    public void setFormulario(Formulario formulario) {
//        this.formulario = formulario;
//    }

    public Integer getNumeroFormularioDesde() {
        return numeroFormularioDesde;
    }

    public void setNumeroFormularioDesde(Integer numeroFormularioDesde) {
        this.numeroFormularioDesde = numeroFormularioDesde;
    }

    public Integer getNumeroFormularioHasta() {
        return numeroFormularioHasta;
    }

    public void setNumeroFormularioHasta(Integer numeroFormularioHasta) {
        this.numeroFormularioHasta = numeroFormularioHasta;
    }

    public Date getFechaMovimientoDesde() {
        return fechaMovimientoDesde;
    }

    public void setFechaMovimientoDesde(Date fechaMovimientoDesde) {
        this.fechaMovimientoDesde = fechaMovimientoDesde;
    }

    public Date getFechaMovimientoHasta() {
        return fechaMovimientoHasta;
    }

    public void setFechaMovimientoHasta(Date fechaMovimientoHasta) {
        this.fechaMovimientoHasta = fechaMovimientoHasta;
    }

    public boolean isSeleccionaMovimiento() {
        return seleccionaMovimiento;
    }

    public void setSeleccionaMovimiento(boolean seleccionaMovimiento) {
        this.seleccionaMovimiento = seleccionaMovimiento;
    }

    public boolean isSeleccionaTodo() {
        return seleccionaTodo;
    }

    public void setSeleccionaTodo(boolean seleccionaTodo) {
        this.seleccionaTodo = seleccionaTodo;
    }

    public boolean isSeleccionaPendiente() {
        return seleccionaPendiente;
    }

    public void setSeleccionaPendiente(boolean seleccionaPendiente) {
        this.seleccionaPendiente = seleccionaPendiente;
    }

    public int getIndexTab1() {
        return indexTab1;
    }

    public void setIndexTab1(int indexTab1) {
        this.indexTab1 = indexTab1;
    }

    public int getIndexTab2() {
        return indexTab2;
    }

    public void setIndexTab2(int indexTab2) {
        this.indexTab2 = indexTab2;
    }

    public int getIndexTab3() {
        return indexTab3;
    }

    public void setIndexTab3(int indexTab3) {
        this.indexTab3 = indexTab3;
    }

    public int getIndexTab4() {
        return indexTab4;
    }

    public void setIndexTab4(int indexTab4) {
        this.indexTab4 = indexTab4;
    }

    public int getIndexTab5() {
        return indexTab5;
    }

    public void setIndexTab5(int indexTab5) {
        this.indexTab5 = indexTab5;
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }

    public boolean isDetalleVacio() {
        return detalleVacio;
    }

    public void setDetalleVacio(boolean detalleVacio) {
        this.detalleVacio = detalleVacio;
    }
        
}
