/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.administracion.rn;

import bs.administracion.dao.ModuloDAO;
import bs.administracion.modelo.Modulo;
import bs.global.excepciones.ExcepcionGeneralSistema;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Agustin
 */
@Stateless
public class ModuloRN {
    
    @EJB private ModuloDAO moduloDAO;
    
    public Modulo getModulo(String value) {
        return moduloDAO.getObjeto(Modulo.class, value);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardar(Modulo m, boolean esNuevo) throws Exception{
        
        if(esNuevo){
            
            if(moduloDAO.getObjeto(Modulo.class, m.getCodigo())!=null){
                throw new ExcepcionGeneralSistema("Ya existe una entidad con el código"+ m.getCodigo());
            }
            moduloDAO.crear(m);
        }else{
            moduloDAO.editar(m);
        }
    }
   
    public void eliminar(Modulo m) throws Exception {
        
        moduloDAO.eliminar(m);
    }
    
    public List<Modulo> getModuloByBusqueda(String txtBusqueda, boolean mostrarDebaja) {
        
        return moduloDAO.getListaByBusqueda(txtBusqueda, mostrarDebaja, 15);
    }

    public List<Modulo> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja, int cantidadRegistros) {
        return moduloDAO.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
    }
  
}
