/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.administracion.rn;


import com.administracion.dao.EmpresaDAO;
import com.administracion.modelo.Empresa;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author ctrosch
 */
@Stateless
public class EmpresaRN{

    @EJB private EmpresaDAO empresaDAO;

    public Empresa getEmpresa(String id) {
        return empresaDAO.getEmpresa(id);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void guardar(Empresa e) throws Exception{
        
        if(e.getCodigo().equals("01")){
            
            if(empresaDAO.getEmpresa(e.getCodigo())==null){
                empresaDAO.crear(e);
            }else{
                empresaDAO.editar(e);
            }
        }
    }

}
