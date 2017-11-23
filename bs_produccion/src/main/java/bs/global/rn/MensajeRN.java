/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.global.rn;

import bs.global.dao.MensajeDAO;
import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.modelo.Mensaje;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author lloubiere
 */
@Stateless
public class MensajeRN {
    
 @EJB
 private MensajeDAO mensajeDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    
    
     public void guardar(Mensaje mensaje) throws ExcepcionGeneralSistema {
                
        if(mensaje.getCodigo()==null){
            mensajeDAO.crear(mensaje);            
        }else{
            mensajeDAO.editar(mensaje);
        }        
    }
     
   public List<Mensaje> getListaByBusqueda(String txtBusqueda, Map<String, String> filtro, boolean mostrarDebaja, int cantMax) {
       
        return mensajeDAO.getListaByBusqueda(filtro, txtBusqueda, mostrarDebaja, cantMax);
    }
   
     

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
