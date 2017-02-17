/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.rn;

import com.global.excepciones.ExcepcionGeneralSistema;
import com.stock.dao.UnidadDeMedidaDAO;
import com.stock.modelo.UnidadDeMedida;
import java.io.Serializable;
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
public class UnidadDeMedidaRN implements Serializable {

   @EJB private UnidadDeMedidaDAO unidadDeMedidaDAO;

   @TransactionAttribute(TransactionAttributeType.REQUIRED)     
   public void guardar(UnidadDeMedida unidadDeMedida, boolean esNuevo) throws Exception{
       
       if (esNuevo){
           
           if(unidadDeMedidaDAO.getObjeto(UnidadDeMedida.class, unidadDeMedida.getCodigo())!=null){
               throw new ExcepcionGeneralSistema("Ya existe unidad de medida con el código"+ unidadDeMedida.getCodigo());
           }
           unidadDeMedidaDAO.crear(unidadDeMedida);
       }else{
           unidadDeMedidaDAO.editar(unidadDeMedida);
       }     
    }
   
   public void eliminar(UnidadDeMedida unidadDeMedida) throws Exception {
        
        unidadDeMedidaDAO.eliminar(unidadDeMedida);
        
    }
   
   
    public UnidadDeMedida getUnidadMedida(String cod){
        return unidadDeMedidaDAO.getUnidadMedida(cod);
    }
 
    public List<UnidadDeMedida> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja, int cantidadRegistros) {
        
        return unidadDeMedidaDAO.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
    }    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
