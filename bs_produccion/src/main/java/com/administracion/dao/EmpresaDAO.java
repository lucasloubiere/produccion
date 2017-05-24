/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.administracion.dao;

import com.administracion.modelo.Empresa;
import com.global.dao.BaseDAO;
import javax.ejb.Stateless;

/**
 *
 * @author ctrosch
 */
@Stateless
public class EmpresaDAO extends BaseDAO {

    public Empresa getEmpresa(String id) {
                
        return super.getObjeto(Empresa.class, id);
    }
}
