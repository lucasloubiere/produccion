/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.administracion.rn;

import com.administracion.dao.ParametroDAO;
import com.administracion.modelo.Parametro;
import com.global.excepciones.ExcepcionGeneralSistema;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Claudio
 */
@Stateless

public class ParametrosRN {

    @EJB ParametroDAO parametroDAO;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void guardar(Parametro p) throws ExcepcionGeneralSistema, Exception{
        
        if(p.getId().equals("01")){
            
            if(parametroDAO.getParametros()==null){
                parametroDAO.crear(p);
            }else{
                parametroDAO.editar(p);
            }
        }
    }

    public Parametro getParametro(String codigo) throws Exception {
                
        Parametro p = parametroDAO.getParametros();

        if (p==null){
            p= new Parametro(codigo);            
        }        
        return p;
    } 
}
