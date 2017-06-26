/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.global.web;



import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.util.InformeBase;
import bs.stock.modelo.Deposito;
import bs.stock.modelo.Producto;
import bs.stock.modelo.Rubro1;
import bs.stock.modelo.Rubro2;
import bs.stock.modelo.TipoProducto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Claudio
 */
@ManagedBean
@ViewScoped
public class ReporteService extends InformeBase implements Serializable {
   
    public TipoProducto tipoProducto;
    public String tipoMovimientoStock;
    public Producto producto;
    public Producto productoDesde;
    public Producto productoHasta;
    public Deposito deposito;

    private Rubro1 rubro01Desde;
    private Rubro1 rubro01Hasta;
    private Rubro2 rubro02Desde;
    private Rubro2 rubro02Hasta;

    private String incluyeImpuesto;
    
    public Date fecha;
    public Date fechaDesde;
    public Date fechaHasta;

    BigDecimal cotizacion;

    public ReporteService() {

    }

    @PostConstruct
    @Override
    public void init() {        
        fecha = new Date();
    }

    @Override
    public void validarDatos() throws ExcepcionGeneralSistema {

        String mensaje = "";
        todoOk = true;
        
        if(nombreReporte==null || nombreReporte.isEmpty()){
            mensaje = "El nombre del reporte no puede ser nulo";            
        }

        if (fechaDesde != null && fechaHasta != null && fechaHasta.before(fechaDesde)) {
            mensaje = "La fecha desde no puede ser mayor a fecha hasta";            
        }

        if (!mensaje.isEmpty()) {
            todoOk = false;
            throw new ExcepcionGeneralSistema(mensaje);
        }
    }

    @Override
    public void cargarParametros() throws ExcepcionGeneralSistema {
        
        parameters.put("TIPMOV", "");
        parameters.put("INCIMP", "N");
        parameters.put("TIPPRO", "");
        parameters.put("ARTCOD", "");
        parameters.put("ARTDES", "");
        parameters.put("ARTHAS", "");
        parameters.put("RU1DES", "0");
        parameters.put("RU1HAS", "9999");
        parameters.put("RU2DES", "0");
        parameters.put("RU2HAS", "9999");
        parameters.put("PROHAB", "");
        parameters.put("DEPOSI", "");
        parameters.put("CODLVT", "");
        parameters.put("CODLPV", "");
        parameters.put("NROCTA", "");
        parameters.put("TIPCTA", "");
        parameters.put("FECHA", "");
        parameters.put("FCHDES", "");
        parameters.put("FCHHAS", "");
        parameters.put("COTIZ", "");

        if (tipoMovimientoStock != null) {
            parameters.put("TIPMOV", tipoMovimientoStock);
        }
        
        if (incluyeImpuesto != null) {
            parameters.put("INCIMP", incluyeImpuesto);
        }

        if (tipoProducto != null) {
            parameters.put("TIPPRO", tipoProducto.getCodigo());
        }

        if (producto != null) {
            parameters.put("ARTCOD", producto.getCodigo());
        }

        if (productoDesde != null) {
            parameters.put("ARTDES", productoDesde.getCodigo());
        }else{
            parameters.put("ARTDES", "0");
        }

        if (productoHasta != null) {
            parameters.put("ARTHAS", productoHasta.getCodigo());
        }else{
            parameters.put("ARTHAS", "9999999");
        }

        if (rubro01Desde != null) {
            parameters.put("RU1DES", rubro01Desde.getCodigo());
        }

        if (rubro01Hasta != null) {
            parameters.put("RU1HAS", rubro01Hasta.getCodigo());
        }

        if (rubro02Desde != null) {
            parameters.put("RU2DES", rubro02Desde.getCodigo());
        }

        if (rubro02Hasta != null) {
            parameters.put("RU2HAS", rubro02Hasta.getCodigo());
        }

        if (fecha != null) {
            parameters.put("FECHA", fecha);
        }

        if (fechaDesde != null) {
            parameters.put("FCHDES", fechaDesde);
        }

        if (fechaHasta != null) {
            parameters.put("FCHHAS", fechaHasta);
        }

        if (cotizacion != null) {
            parameters.put("COTIZ", cotizacion);
        }
        
        pathReporte = FacesContext.getCurrentInstance().getExternalContext().getRealPath(nombreReporte+".jasper");
    }

    @Override
    public void resetParametros() {

        todoOk = false;
        muestraReporte = false;

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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(BigDecimal cotizacion) {
        this.cotizacion = cotizacion;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Producto getProductoDesde() {
        return productoDesde;
    }

    public void setProductoDesde(Producto productoDesde) {
        this.productoDesde = productoDesde;
    }

    public Producto getProductoHasta() {
        return productoHasta;
    }

    public void setProductoHasta(Producto productoHasta) {
        this.productoHasta = productoHasta;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public String getTipoMovimientoStock() {
        return tipoMovimientoStock;
    }

    public void setTipoMovimientoStock(String tipoMovimientoStock) {
        this.tipoMovimientoStock = tipoMovimientoStock;
    }

    public Rubro1 getRubro01Desde() {
        return rubro01Desde;
    }

    public void setRubro01Desde(Rubro1 rubro01Desde) {
        this.rubro01Desde = rubro01Desde;
    }

    public Rubro1 getRubro01Hasta() {
        return rubro01Hasta;
    }

    public void setRubro01Hasta(Rubro1 rubro01Hasta) {
        this.rubro01Hasta = rubro01Hasta;
    }

    public Rubro2 getRubro02Desde() {
        return rubro02Desde;
    }

    public void setRubro02Desde(Rubro2 rubro02Desde) {
        this.rubro02Desde = rubro02Desde;
    }

    public Rubro2 getRubro02Hasta() {
        return rubro02Hasta;
    }

    public void setRubro02Hasta(Rubro2 rubro02Hasta) {
        this.rubro02Hasta = rubro02Hasta;
    }

    public String getIncluyeImpuesto() {
        return incluyeImpuesto;
    }

    public void setIncluyeImpuesto(String incluyeImpuesto) {
        this.incluyeImpuesto = incluyeImpuesto;
    }
    
}
