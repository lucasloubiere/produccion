/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.produccion.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.produccion.dao.ParametroProduccionDAO;
import bs.produccion.modelo.ParametroProduccion;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Claudio
 */
@Stateless

public class ParametroProduccionRN {

    @EJB ParametroProduccionDAO parametroDAO;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void guardar(ParametroProduccion p) throws ExcepcionGeneralSistema, Exception{
        
        if(p.getId().equals("01")){
            
            if(parametroDAO.getParametros()==null){
                parametroDAO.crear(p);
            }else{
                parametroDAO.editar(p);
            }
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)  
    public ParametroProduccion getParametro() throws Exception {
                
        ParametroProduccion p = parametroDAO.getParametros();

        if (p==null){            
            p= new ParametroProduccion("01");            
            parametroDAO.crear(p);
        }        
        return p;
    } 
}
