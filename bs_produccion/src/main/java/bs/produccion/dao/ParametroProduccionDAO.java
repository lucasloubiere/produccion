/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.produccion.dao;

import bs.global.dao.BaseDAO;
import bs.produccion.modelo.ParametroProduccion;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Claudio
 */
@Stateless

public class ParametroProduccionDAO extends BaseDAO {

    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void guardar(ParametroProduccion parametros)throws Exception{
        em.persist(parametros);
    }
    
    public void editar(ParametroProduccion parametro)throws Exception{
        em.merge(parametro);
    }

    public ParametroProduccion getParametros(){

        return  em.find(ParametroProduccion.class, "01");

    }
    
}
