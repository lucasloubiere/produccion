/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.administracion.dao;

import bs.administracion.modelo.Empresa;
import bs.global.dao.BaseDAO;
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
