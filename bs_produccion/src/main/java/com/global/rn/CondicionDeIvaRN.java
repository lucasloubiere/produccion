/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.global.rn;

import com.global.dao.CondicionDeIvaDAO;
import com.global.excepciones.ExcepcionGeneralSistema;
import com.global.modelo.CondicionDeIva;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * @author Agustin
 */
@Stateless
public class CondicionDeIvaRN {
    
    @EJB private CondicionDeIvaDAO condicionDeIvaDAO;
    
    public CondicionDeIva getCondicionDeIva(String codigo){
        
        return condicionDeIvaDAO.getObjeto(CondicionDeIva.class, codigo);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)     
    public void guardar(CondicionDeIva c, boolean esNuevo) throws Exception{
        
        if (esNuevo){
            if (condicionDeIvaDAO.getObjeto(CondicionDeIva.class, c.getCodigo())!=null){
                throw new ExcepcionGeneralSistema("Ya existe condición de IVA con el código"+ c.getCodigo());
            }
            condicionDeIvaDAO.crear(c);
        }else{
            condicionDeIvaDAO.editar(c);
        }
        
    }
    
    public List<CondicionDeIva> getListaByBusqueda(String txtBusqueda,  boolean mostrarDebaja) {
        
        return condicionDeIvaDAO.getListaByBusqueda(null, txtBusqueda, mostrarDebaja, 15);
        
    }
    
    public void eliminar (CondicionDeIva c) throws Exception {
         
        condicionDeIvaDAO.eliminar(c);
    }
}
