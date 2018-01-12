/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.produccion.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.modelo.ComprobantePK;
import bs.produccion.dao.ComprobanteProduccionDAO;
import bs.produccion.modelo.ComprobanteProduccion;
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
public class ComprobanteProduccionRN {

    @EJB private ComprobanteProduccionDAO comprobanteDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)     
    public void guardar(ComprobanteProduccion e, boolean esNuevo) throws Exception{
        
       if(esNuevo){
           
           if(comprobanteDAO.getComprobante(new ComprobantePK(e.getModulo(), e.getCodigo()))!=null){
               throw new ExcepcionGeneralSistema("Ya existe una entidad con el código"+e.getCodigo());
           }
            comprobanteDAO.crear(e);
            
        }else{
               
            comprobanteDAO.editar(e);
        }               
    }
       
    public ComprobanteProduccion getComprobante(ComprobantePK idPK) throws ExcepcionGeneralSistema{

        ComprobanteProduccion cs = comprobanteDAO.getComprobante(idPK);
        if(cs==null) throw new ExcepcionGeneralSistema("No se encontró comprobante de stock ("+idPK.getCodigo()+")");
        return cs;
    }

    public ComprobanteProduccion getComprobante(String modcom, String codcom) throws ExcepcionGeneralSistema{

        ComprobanteProduccion cs = comprobanteDAO.getComprobante(new ComprobantePK(modcom, codcom));
        if(cs==null) throw new ExcepcionGeneralSistema("No se encontró comprobante de stock ("+codcom+")");
        return cs;
    }
    
    public List<ComprobanteProduccion> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja){
        
        return comprobanteDAO.getListaByBusqueda(null, txtBusqueda, mostrarDebaja, 15);
    }
    
    public List<ComprobanteProduccion> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja,int cantMaxima){
        
        return comprobanteDAO.getListaByBusqueda(null, txtBusqueda, mostrarDebaja, cantMaxima);
    }
    
    public List<ComprobanteProduccion> getListaByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja){
        
        return comprobanteDAO.getListaByBusqueda(filtro, txtBusqueda, mostrarDebaja, 15);        
    }

    public List<ComprobanteProduccion> getListaByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja, int cantMaxima){
        
        return comprobanteDAO.getListaByBusqueda(filtro, txtBusqueda, mostrarDebaja, cantMaxima);        
    }

    public void eliminar(ComprobanteProduccion comprobante) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
}
