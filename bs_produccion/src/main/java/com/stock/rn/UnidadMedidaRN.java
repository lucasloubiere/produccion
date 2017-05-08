/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.rn;

import com.global.excepciones.ExcepcionGeneralSistema;
import com.stock.dao.UnidadMedidaDAO;
import com.stock.modelo.UnidadMedida;
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
public class UnidadMedidaRN implements Serializable {

   @EJB private UnidadMedidaDAO unidadDeMedidaDAO;

   @TransactionAttribute(TransactionAttributeType.REQUIRED)     
   public void guardar(UnidadMedida unidadDeMedida, boolean esNuevo) throws Exception{
       
       if (esNuevo){
           
           if(unidadDeMedidaDAO.getObjeto(UnidadMedida.class, unidadDeMedida.getCodigo())!=null){
               throw new ExcepcionGeneralSistema("Ya existe unidad de medida con el código"+ unidadDeMedida.getCodigo());
           }
           unidadDeMedidaDAO.crear(unidadDeMedida);
       }else{
           unidadDeMedidaDAO.editar(unidadDeMedida);
       }     
    }
   
   public void eliminar(UnidadMedida unidadDeMedida) throws Exception {
        
        unidadDeMedidaDAO.eliminar(unidadDeMedida);
        
    }
   
   
    public UnidadMedida getUnidadMedida(String cod){
        return unidadDeMedidaDAO.getUnidadMedida(cod);
    }
 
    public List<UnidadMedida> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja, int cantidadRegistros) {
        
        return unidadDeMedidaDAO.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
    }    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
