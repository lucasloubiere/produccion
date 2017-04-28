/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.rn;

import com.global.excepciones.ExcepcionGeneralSistema;
import com.global.dao.SucursalDAO;
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
public class SucursalRN {
    @EJB private SucursalDAO sucursalDAO;
    @TransactionAttribute(TransactionAttributeType.REQUIRED)   
    
   public void guardar(Sucursal e, boolean esNuevo) throws Exception{
        
       if(esNuevo){
           
           if(sucursalDAO.getObjeto(Sucursal.class, e.getCodigo())!=null){
               throw new ExcepcionGeneralSistema("Ya existe un Sucursal con ese código "+e.getCodigo());
           }
            sucursalDAO.crear(e);
            
        }else{
               
            sucursalDAO.editar(e);
        }        
   }
    
   public void eliminar(Sucursal sucursal) throws Exception {
        
        sucursalDAO.eliminar(sucursal);
        
    }
  
    public List<Sucursal> getListaByBusqueda(String txtBusqueda, boolean mostrarDeBaja, int cantMax) {
        
        return sucursalDAO.getListaByBusqueda(txtBusqueda,mostrarDeBaja,cantMax);
    }

    public Sucursal getSucursal(String value) {
        
        return sucursalDAO.getSucursal(value);
    }
    
}
