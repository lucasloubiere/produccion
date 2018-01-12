package bs.produccion.rn;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.produccion.dao.CircuitoProduccionDAO;
import bs.produccion.modelo.CircuitoProduccion;
import bs.produccion.modelo.CircuitoProduccionPK;
import bs.produccion.modelo.ComprobanteProduccion;
import bs.stock.modelo.ComprobanteStock;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Claudio
 */
@Stateless
public class CircuitoProduccionRN {
    
    @EJB private CircuitoProduccionDAO circuitoDAO;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void guardar(CircuitoProduccion c, boolean esNuevo) throws Exception{
        
        if(esNuevo){
            
            CircuitoProduccionPK idPK = new CircuitoProduccionPK(c.getCircom(),c.getCirapl());
            
            if(circuitoDAO.getObjeto(CircuitoProduccion.class, idPK)!=null){
                throw new ExcepcionGeneralSistema("Ya existe un circuito con el código "+ idPK);
            }
            circuitoDAO.crear(c);
                
        }else{
            circuitoDAO.editar(c);
        }
    }
    
    public CircuitoProduccion getCircuito(CircuitoProduccionPK idPK){
        
        return circuitoDAO.getObjeto(CircuitoProduccion.class, idPK);
        
    }   
    
    public CircuitoProduccion getCircuito(String circom, String cirapl){
        
        CircuitoProduccionPK idPK = new CircuitoProduccionPK(circom,cirapl);
        return circuitoDAO.getObjeto(CircuitoProduccion.class, idPK);
        
    }   
    
    public List<CircuitoProduccion> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja){
        
        return circuitoDAO.getListaByBusqueda(null,txtBusqueda, mostrarDebaja, 15);
    }
    
    public List<CircuitoProduccion> getListaByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja){
        
        return circuitoDAO.getListaByBusqueda(filtro, txtBusqueda, mostrarDebaja, 15);        
    }

    public List<CircuitoProduccion> getListaByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja, int cantMaxima){
        
        return circuitoDAO.getListaByBusqueda(filtro, txtBusqueda, mostrarDebaja, cantMaxima);        
    }

    public void eliminar(CircuitoProduccion codigoCircuitoProduccion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ComprobanteProduccion getComprobanteProduccion(String circom, String cirapl, String modcom, String codcom) throws ExcepcionGeneralSistema {

//        System.out.println(circuitoDAO.getComprobanteProduccion(circom, cirapl ,modcom,codcom));

        ComprobanteProduccion cf = circuitoDAO.getComprobanteProduccion(circom, cirapl ,modcom,codcom);
        if(cf==null) throw new ExcepcionGeneralSistema("El comprobante de producción ("+codcom+") no está configurado para el circuito (" +circom+" - "+cirapl+")");
        return cf;
    }

    public ComprobanteStock getComprobanteStock(String circom, String cirapl, String modcom, String codcom) throws ExcepcionGeneralSistema {

        ComprobanteStock cs = circuitoDAO.getComprobanteStock(circom, cirapl ,modcom,codcom);
        if(cs==null) throw new ExcepcionGeneralSistema("El comprobante de stock ("+codcom+") no está configurado para el circuito (" +circom+" - "+cirapl+")");
        return cs;
    }
   
    public void cargarCircuitosRelacionados(CircuitoProduccion circuito){
        
        Map<String, String> filtro = circuitoDAO.getFiltro();
        
        filtro.put("circom = ","'"+circuito.getCircom()+"'");        
        filtro.put("cirapl <> ","'"+circuito.getCircom()+"'");        
        getListaByBusqueda(filtro, "", true);
        circuito.setCircuitosRelacionados(getListaByBusqueda(filtro, "", true));
    }
    
}
