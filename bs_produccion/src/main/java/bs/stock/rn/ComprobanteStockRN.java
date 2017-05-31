/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.stock.rn;


import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.modelo.ComprobantePK;
import bs.stock.dao.ComprobanteStockDAO;
import bs.stock.modelo.ComprobanteStock;
import bs.stock.modelo.MovimientoStock;
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
public class ComprobanteStockRN {
    
    @EJB ComprobanteStockDAO comprobanteDAO;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)     
    public void guardar(ComprobanteStock e, boolean esNuevo) throws Exception{
        
       if(esNuevo){
           
           if(comprobanteDAO.getComprobante(new ComprobantePK(e.getModulo(), e.getCodigo()))!=null){
               throw new ExcepcionGeneralSistema("Ya existe una entidad con el código"+e.getCodigo());
           }
            comprobanteDAO.crear(e);
            
        }else{
               
            comprobanteDAO.editar(e);
        }               
    }
   
    public MovimientoStock getMovimiento(String codFormulario, Integer numeroFormulario) {
        return comprobanteDAO.getMovimiento(codFormulario,numeroFormulario);
    }
    
    public ComprobanteStock getComprobante(ComprobantePK idPK) throws ExcepcionGeneralSistema{

        ComprobanteStock cs = comprobanteDAO.getComprobante(idPK);
        if(cs==null) throw new ExcepcionGeneralSistema("No se encontró comprobante de stock ("+idPK.getCodigo()+")");
        return cs;
    }

    public ComprobanteStock getComprobante(String modcom, String codcom) throws ExcepcionGeneralSistema{

        ComprobanteStock cs = comprobanteDAO.getComprobante(new ComprobantePK(modcom, codcom));
        if(cs==null) throw new ExcepcionGeneralSistema("No se encontró comprobante de stock ("+codcom+")");
        return cs;
    }
    
    public List<ComprobanteStock> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja){
        
        return comprobanteDAO.getListaByBusqueda(null, txtBusqueda, mostrarDebaja, 15);
    }
    
    public List<ComprobanteStock> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja,int cantMaxima){
        
        return comprobanteDAO.getListaByBusqueda(null, txtBusqueda, mostrarDebaja, cantMaxima);
    }
    
    public List<ComprobanteStock> getListaByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja){
        
        return comprobanteDAO.getListaByBusqueda(filtro, txtBusqueda, mostrarDebaja, 15);        
    }

    public List<ComprobanteStock> getListaByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja, int cantMaxima){
        
        return comprobanteDAO.getListaByBusqueda(filtro, txtBusqueda, mostrarDebaja, cantMaxima);        
    }

    public void eliminar(ComprobanteStock comprobante) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
