/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.rn;

import com.global.excepciones.ExcepcionGeneralSistema;
import com.global.dao.ModuloDAO;
import com.global.modelo.Modulo;
import com.global.modelo.Sucursal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author lloubiere
 */
@Stateless
public class ModuloRN {
    @EJB private ModuloDAO moduloDAO;
    @TransactionAttribute(TransactionAttributeType.REQUIRED)   
    
   public void guardar(Modulo e, boolean esNuevo) throws Exception{
        
       if(esNuevo){
           
           if(moduloDAO.getObjeto(Modulo.class, e.getCodigo())!=null){
               throw new ExcepcionGeneralSistema("Ya existe un Modulo con ese código "+e.getCodigo());
           }
            moduloDAO.crear(e);
            
        }else{
               
            moduloDAO.editar(e);
        }        
   }
    
   public void eliminar(Modulo modulo) throws Exception {
        
        moduloDAO.eliminar(modulo);
        
    }
  
    public List<Modulo> getListaByBusqueda(String txtBusqueda, boolean mostrarDeBaja, int cantMax) {
        
        return moduloDAO.getListaByBusqueda(txtBusqueda,mostrarDeBaja,cantMax);
    }

    public Modulo getModulo(String value) {
        
        return moduloDAO.getModulo(value);
    }

    public Sucursal getSucursal(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}


