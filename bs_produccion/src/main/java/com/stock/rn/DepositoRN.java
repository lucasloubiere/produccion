/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.rn;

import com.global.excepciones.ExcepcionGeneralSistema;
import com.stock.dao.DepositoDAO;
import com.stock.modelo.Deposito;
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
public class DepositoRN {

     @EJB private DepositoDAO depositoDAO;
     
      @TransactionAttribute(TransactionAttributeType.REQUIRED)     
   public void guardar(Deposito deposito, boolean esNuevo) throws Exception{
        
       if(esNuevo){
           
           if(depositoDAO.getObjeto(Deposito.class, deposito.getCodigo())!=null){
               throw new ExcepcionGeneralSistema("Ya existe un depósito con el código"+deposito.getCodigo());
           }
           depositoDAO.crear(deposito);
            
        }else{               
            depositoDAO.editar(deposito);
        }        
   }
    
   public void eliminar(Deposito deposito) throws Exception {
        
        depositoDAO.eliminar(deposito);
        
    }
   
   public List<Deposito> getLista(boolean mostrarDebaja) {
        
        return depositoDAO.getLista(mostrarDebaja);
        
    }

    public Deposito getDeposito(String id){
        return depositoDAO.getDeposito(id);
    }

    public List<Deposito> getLista() {
       return depositoDAO.getLista();
    }

    public List<Deposito> getLista(int maxResults, int firstResult) {
        return depositoDAO.getLista(maxResults, firstResult);
    }

    public List<Deposito> getDepositoByBusqueda(String txtBusqueda, boolean mostrarDeBaja) {
        
        return depositoDAO.getDepositoByBusqueda(txtBusqueda,mostrarDeBaja,15);
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
