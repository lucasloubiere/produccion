/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.web.informe;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.modelo.Temporal;
import bs.global.rn.TemporalRN;
import bs.global.util.InformeBase;
import bs.global.util.JsfUtil;
import bs.stock.modelo.Deposito;
import bs.stock.rn.DepositoRN;
import bs.stock.rn.ProductoRN;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Claudio
 */
@ManagedBean
@ViewScoped
public class StockDepositoFechaBean extends InformeBase implements Serializable {

    @EJB
    ProductoRN productoRN;
    @EJB
    DepositoRN depositoRN;
    @EJB
    TemporalRN temporalRN;

    private Date fechaHoraHasta;
    private Deposito deposito;
    private List<Deposito> lista;

    /**
     * Creates a new instance of ImpresionComprobanteFacturacionBean
     */
    public StockDepositoFechaBean() {
    }

    @PostConstruct
    public void init() {

        fechaHoraHasta = new Date();

    }

    @Override
    public void validarDatos() throws ExcepcionGeneralSistema {

        String mensaje = "";
        todoOk = true;

        if (fechaHoraHasta == null) {
            mensaje = "Fecha hasta no puede estar en blanco";
        }

        if (!mensaje.isEmpty()) {
            todoOk = false;
            throw new ExcepcionGeneralSistema(mensaje);
        }
    }

    @Override
    public void cargarParametros() throws ExcepcionGeneralSistema {

        parameters.put("FCHHHAS", JsfUtil.getTimeStampSQL(fechaHoraHasta));

        if (deposito != null) {
            parameters.put("DEPOSI", deposito.getCodigo());
        } else {
            parameters.put("DEPOSI", "");
        }

        if (lista == null || lista.isEmpty()) {
            guardarDepositosSeleccionados(depositoRN.getLista());
        }else{
            guardarDepositosSeleccionados(lista);
        }
        
        nombreArchivo = "ST_STOCK_DEPOSITO_FECHA";
        reporte = reporteRN.getReporte(codigoReporte);
        //pathReporte = "stock/informe/ST_STOCK_DEPOSITO_FECHA.jasper";
        
        
//        temporalRN.vaciarTabla();
    }
    
    public String getFiltroIn(List<Deposito> lista){
        
        String sIn = "";
        
        for(Deposito d:lista){
            
            if(sIn.isEmpty()){
                sIn += "'"+d.getCodigo()+"'";
            }else{
                sIn += ",'"+d.getCodigo()+"'";
            }            
        }
        
        return sIn;
    }
    

    @Override
    public void resetParametros() {

        fechaHoraHasta = new Date();
        deposito = null;
        todoOk = false;
        muestraReporte = false;

    }
    
    public void guardarDepositosSeleccionados(List<Deposito> seleccion){
        
        temporalRN.vaciarTabla();
        
        for(Deposito d:seleccion){
            
            try {
                Temporal t = new Temporal(d.getCodigo());
                temporalRN.guardar(t);
            } catch (Exception ex) {
                Logger.getLogger(StockDepositoFechaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

    public Date getFechaHoraHasta() {
        return fechaHoraHasta;
    }

    public void setFechaHoraHasta(Date fechaHoraHasta) {
        this.fechaHoraHasta = fechaHoraHasta;
    }
    
    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public List<Deposito> getLista() {
        return lista;
    }

    public void setLista(List<Deposito> lista) {
        this.lista = lista;
    }

}
