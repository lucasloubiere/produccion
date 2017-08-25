/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.sincronizacion.ws;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lloubiere
 */
@XmlRootElement
public class MovimientoBalanza {
    
    private Integer nroIngreso;    
    private String nroComprobante;    
    private String operacion;    
    private Integer netoNeto;    
    private String productoCodigo;    
    private String productoDescripcion;    
    private Integer plataformaId;    
    private String plataformaDescripcion;

    public MovimientoBalanza() {
    }
    
    public Integer getNroIngreso() {
        return nroIngreso;
    }

    public void setNroIngreso(Integer nroIngreso) {
        this.nroIngreso = nroIngreso;
    }

    public String getNroComprobante() {
        return nroComprobante;
    }

    public void setNroComprobante(String nroComprobante) {
        this.nroComprobante = nroComprobante;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Integer getNetoNeto() {
        return netoNeto;
    }

    public void setNetoNeto(Integer netoNeto) {
        this.netoNeto = netoNeto;
    }

    public String getProductoCodigo() {
        return productoCodigo;
    }

    public void setProductoCodigo(String productoCodigo) {
        this.productoCodigo = productoCodigo;
    }

    public String getProductoDescripcion() {
        return productoDescripcion;
    }

    public void setProductoDescripcion(String productoDescripcion) {
        this.productoDescripcion = productoDescripcion;
    }

    public Integer getPlataformaId() {
        return plataformaId;
    }

    public void setPlataformaId(Integer plataformaId) {
        this.plataformaId = plataformaId;
    }

    public String getPlataformaDescripcion() {
        return plataformaDescripcion;
    }

    public void setPlataformaDescripcion(String plataformaDescripcion) {
        this.plataformaDescripcion = plataformaDescripcion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.nroIngreso != null ? this.nroIngreso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MovimientoBalanza other = (MovimientoBalanza) obj;
        if (this.nroIngreso != other.nroIngreso && (this.nroIngreso == null || !this.nroIngreso.equals(other.nroIngreso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Datos{" + "nroIngreso=" + nroIngreso + ", productoDescripcion=" + productoDescripcion + '}';
    }
    
}
