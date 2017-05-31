/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.administracion.rn;

import bs.administracion.dao.CorreoElectronicoDAO;
import bs.administracion.modelo.CorreoElectronico;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;



/**
 *
 * @author Claudio
 */
@Stateless
public class CorreoElectronicoRN {
    
    @EJB private CorreoElectronicoDAO correoElectronicoDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void guardar(CorreoElectronico ce) throws Exception{
        
        if(ce.getId() == null){            
            correoElectronicoDAO.crear(ce);
        }else{
            correoElectronicoDAO.editar(ce);
        }
    }

    public List<CorreoElectronico> getCorreosNoEnviados() {
        
        return correoElectronicoDAO.queryList(CorreoElectronico.class,"SELECT c FROM CorreoElectronico c WHERE c.enviado = 'N' ORDER BY c.fechaEnvio DESC ");
    }
}
