/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.web;

import bs.global.modelo.Formulario;
import bs.global.rn.FormularioPorSituacionIVARN;
import bs.global.rn.SucursalRN;
import bs.global.util.JsfUtil;
import bs.global.web.GenericBean;
import bs.produccion.modelo.CircuitoProduccion;
import bs.produccion.modelo.ItemProductoProduccion;
import bs.produccion.modelo.MovimientoProduccion;
import bs.produccion.rn.CircuitoProduccionRN;
import bs.produccion.rn.DepartamentoProduccionRN;
import bs.produccion.rn.ProduccionRN;
import bs.stock.rn.ComposicionFormulaRN;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Claudio
 */
@ManagedBean
@ViewScoped
public class ControlProduccionBean extends GenericBean implements Serializable {

    @EJB
    private ProduccionRN produccionRN;    
    @EJB
    private ComposicionFormulaRN composicionFormulaRN;
    @EJB
    private DepartamentoProduccionRN departamentoRN;
    @EJB
    private CircuitoProduccionRN circuitoRN;
    @EJB
    private SucursalRN sucursalRN;

    protected @EJB
    FormularioPorSituacionIVARN formularioPorSituacionIVARN;

    protected MovimientoProduccion m;
    protected ItemProductoProduccion itemProducto;
    protected List<MovimientoProduccion> movimientos;
    protected CircuitoProduccion circuito;

    protected String CIRCOM = "";
    protected String CIRAPL = "";

    protected String SUCPD = "";
    protected String MODPD = "";
    protected String CODPD = "";

    protected String SUCST = "";
    protected String MODST = "";
    protected String CODST = "";

    protected String SUCPR = "";
    protected String MODPR = "";
    protected String CODPR = "";

    protected String SUCVC = "";
    protected String MODVC = "";
    protected String CODVC = "";

    //Datos para generar movimientos aplicados
    protected List<String[]> ordenesControl;
    protected List<String[]> produccionProgramada;
    protected List<String[]> ingresoProduccion;
    protected List<String[]> detalleProduccion;
    protected List<String[]> produccionAnulada;
    
    private String[] modoIngresoSeleccionado;


    /**
     * Creates a new instance of RequerimientoProduccionBean
     */
    /**
     * Creates a new instance of OrdenProduccionBean
     */
    public ControlProduccionBean() {

        
        titulo = "Control de producción";
        muestraReporte = false;
        numeroFormularioDesde = 1;
    }

    public void iniciarVariables() {

        try {
            if (!beanIniciado) {
                formulario = new Formulario();
                iniciarMovimiento();
                beanIniciado = true;
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Error al iniciar el bean " + ex);
        }
    }

    public void iniciarMovimiento() {

        super.iniciar();

        try {

            muestraReporte = false;
            solicitaEmail = false;
            buscaMovimiento = false;

            nombreArchivo = "";
            esAnulacion = false;

            numeroFormularioDesde = null;
            numeroFormularioHasta = null;
            fechaMovimientoDesde = null;
            fechaMovimientoHasta = null;
            
            ordenesControl = new ArrayList<String[]>();
            
            String[] o1 = new String[4];
            o1[0] = "03/05/2018";
            o1[1] = "1";
            o1[2] = "QUIROZ, Aldo L.";
            o1[3] = "S";
            
            String[] o2 = new String[4];
            o2[0] = "03/05/2018";
            o2[1] = "2";
            o2[2] = "MICELLI, Claudio.";
            o2[3] = "S";
            
            String[] o3 = new String[4];
            o3[0] = "03/05/2018";
            o3[1] = "3";
            o3[2] = "BERLANDA, Eliano";
            o3[3] = "S";
            
            ordenesControl.add(o1);
            ordenesControl.add(o2);
            ordenesControl.add(o3);
            
            produccionProgramada = new ArrayList<String[]>();
            
            String[] p1 = new String[5];
            p1[0] = "649";
            p1[1] = "BOTELLA 15 X 900 CC VICENTIN";
            p1[2] = "4212";
            p1[3] = "78";
            p1[4] = "17790843000298";
            
            String[] p2 = new String[5];
            p2[0] = "616";
            p2[1] = "BOTELLA 6 X 3 LTS VICENTIN";
            p2[2] = "1500";
            p2[3] = "50";
            p2[4] = "17790843000175";
            
            String[] p3 = new String[5];
            p3[0] = "FR0608";
            p3[1] = "BIDON 4 X 4.5 LTS EL DESPENSERO";
            p3[2] = "1500";
            p3[3] = "60";
            p3[4] = "17790064104201";
            
            String[] p4 = new String[5];
            p4[0] = "604";
            p4[1] = "BOTELLA 12 X 1.5 LTS VICENTIN";
            p4[2] = "3000";
            p4[3] = "60";
            p4[4] = "17790843000168";
            
            produccionProgramada.add(p1);
            produccionProgramada.add(p2);
            produccionProgramada.add(p3);
            produccionProgramada.add(p4);
            
            
            ingresoProduccion = new ArrayList<String[]>();
            detalleProduccion = new ArrayList<String[]>();
            
            String[] d01 = crearDetalleProduccion("649", "BOTELLA 15 x 900 CC \"VICENTIN\"", "78", "00532", "12:20");
            String[] d02 = crearDetalleProduccion("649", "BOTELLA 15 x 900 CC \"VICENTIN\"", "78", "00533", "12:21");
            String[] d03 = crearDetalleProduccion("649", "BOTELLA 15 x 900 CC \"VICENTIN\"", "78", "00534", "12:22");
            String[] d04 = crearDetalleProduccion("616", "BOTELLA 6 x 3 LTS \"VICENTIN\"", "50", "00791", "12:25");
            String[] d05 = crearDetalleProduccion("616", "BOTELLA 6 x 3 LTS \"VICENTIN\"", "50", "00792", "12:26");
            String[] d06 = crearDetalleProduccion("649", "BOTELLA 15 x 900 CC \"VICENTIN\"", "78", "00535", "12:27");
            String[] d07 = crearDetalleProduccion("649", "BOTELLA 15 x 900 CC \"VICENTIN\"", "78", "00536", "12:28");
            String[] d08 = crearDetalleProduccion("649", "BOTELLA 15 x 900 CC \"VICENTIN\"", "78", "00537", "12:29");
            String[] d09 = crearDetalleProduccion("616", "BOTELLA 6 x 3 LTS \"VICENTIN\"", "50", "00793", "12:30");
            String[] d10 = crearDetalleProduccion("616", "BOTELLA 6 x 3 LTS \"VICENTIN\"", "50", "00794", "12:33");
            String[] d11 = crearDetalleProduccion("616", "BOTELLA 6 x 3 LTS \"VICENTIN\"", "50", "00752", "12:33");
            
            detalleProduccion.add(d01);
            detalleProduccion.add(d02);
            detalleProduccion.add(d03);
            detalleProduccion.add(d04);
            detalleProduccion.add(d05);
            detalleProduccion.add(d06);
            detalleProduccion.add(d07);
            detalleProduccion.add(d08);
            detalleProduccion.add(d09);
            detalleProduccion.add(d10);
            detalleProduccion.add(d11);            
            
            produccionAnulada = new ArrayList<String[]>();
            
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public String[] crearDetalleProduccion(String codigo, String descripcion, String cajas, String lote, String hora){
        
        String[] o = new String[5];
            o[0] = codigo;
            o[1] = descripcion;
            o[2] = cajas;
            o[3] = lote;
            o[4] = hora;
            
        return o;        
    }



    //--------------------------------------------------------------------------
    public MovimientoProduccion getM() {
        return m;
    }

    public void setM(MovimientoProduccion m) {
        this.m = m;
    }

    public CircuitoProduccion getCircuito() {
        return circuito;
    }

    public void setCircuito(CircuitoProduccion circuito) {
        this.circuito = circuito;
    }

    public String getCIRCOM() {
        return CIRCOM;
    }

    public void setCIRCOM(String CIRCOM) {
        this.CIRCOM = CIRCOM;
    }

    public String getCIRAPL() {
        return CIRAPL;
    }

    public void setCIRAPL(String CIRAPL) {
        this.CIRAPL = CIRAPL;
    }

    public String getSUCPD() {
        return SUCPD;
    }

    public void setSUCPD(String SUCPD) {
        this.SUCPD = SUCPD;
    }

    public String getMODPD() {
        return MODPD;
    }

    public void setMODPD(String MODPD) {
        this.MODPD = MODPD;
    }

    public String getCODPD() {
        return CODPD;
    }

    public void setCODPD(String CODPD) {
        this.CODPD = CODPD;
    }

    public String getSUCST() {
        return SUCST;
    }

    public void setSUCST(String SUCST) {
        this.SUCST = SUCST;
    }

    public String getMODST() {
        return MODST;
    }

    public void setMODST(String MODST) {
        this.MODST = MODST;
    }

    public String getCODST() {
        return CODST;
    }

    public void setCODST(String CODST) {
        this.CODST = CODST;
    }

    public List<MovimientoProduccion> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoProduccion> movimientos) {
        this.movimientos = movimientos;
    }

    public String getSUCPR() {
        return SUCPR;
    }

    public void setSUCPR(String SUCPR) {
        this.SUCPR = SUCPR;
    }

    public String getMODPR() {
        return MODPR;
    }

    public void setMODPR(String MODPR) {
        this.MODPR = MODPR;
    }

    public String getCODPR() {
        return CODPR;
    }

    public void setCODPR(String CODPR) {
        this.CODPR = CODPR;
    }

    public String getSUCVC() {
        return SUCVC;
    }

    public void setSUCVC(String SUCVC) {
        this.SUCVC = SUCVC;
    }

    public String getMODVC() {
        return MODVC;
    }

    public void setMODVC(String MODVC) {
        this.MODVC = MODVC;
    }

    public String getCODVC() {
        return CODVC;
    }

    public void setCODVC(String CODVC) {
        this.CODVC = CODVC;
    }

    public ItemProductoProduccion getItemProducto() {
        return itemProducto;
    }

    public void setItemProducto(ItemProductoProduccion itemProducto) {
        this.itemProducto = itemProducto;
    }

    public List<String[]> getOrdenesControl() {
        return ordenesControl;
    }

    public void setOrdenesControl(List<String[]> ordenesControl) {
        this.ordenesControl = ordenesControl;
    }

    public List<String[]> getProduccionProgramada() {
        return produccionProgramada;
    }

    public void setProduccionProgramada(List<String[]> produccionProgramada) {
        this.produccionProgramada = produccionProgramada;
    }

    public List<String[]> getIngresoProduccion() {
        return ingresoProduccion;
    }

    public void setIngresoProduccion(List<String[]> ingresoProduccion) {
        this.ingresoProduccion = ingresoProduccion;
    }

    public List<String[]> getDetalleProduccion() {
        return detalleProduccion;
    }

    public void setDetalleProduccion(List<String[]> detalleProduccion) {
        this.detalleProduccion = detalleProduccion;
    }

    public List<String[]> getProduccionAnulada() {
        return produccionAnulada;
    }

    public void setProduccionAnulada(List<String[]> produccionAnulada) {
        this.produccionAnulada = produccionAnulada;
    }

    public String[] getModoIngresoSeleccionado() {
        return modoIngresoSeleccionado;
    }

    public void setModoIngresoSeleccionado(String[] modoIngresoSeleccionado) {
        this.modoIngresoSeleccionado = modoIngresoSeleccionado;
    }
    
    
    
    
    
}
