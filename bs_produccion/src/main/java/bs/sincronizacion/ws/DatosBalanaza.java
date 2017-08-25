/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.sincronizacion.ws;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lloubiere
 */
@XmlRootElement
public class DatosBalanaza {
    
    private String error;        
    private List<MovimientoBalanza> data;

    public DatosBalanaza() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<MovimientoBalanza> getData() {
        return data;
    }

    public void setData(List<MovimientoBalanza> data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return "DatosBalanaza{" + "error=" + error + ", data=" + data + '}';
    }
    
}
