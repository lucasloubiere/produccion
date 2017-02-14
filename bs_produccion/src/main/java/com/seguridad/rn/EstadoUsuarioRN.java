/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguridad.rn;

import com.global.excepciones.ExcepcionGeneralSistema;
import com.seguridad.dao.EstadoUsuarioDAO;
import com.seguridad.modelo.EstadoUsuario;
import com.seguridad.modelo.Usuario;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author lloubiere
 */
@Stateless
public class EstadoUsuarioRN {
    
    @EJB private EstadoUsuarioDAO estadoUsuarioDAO;
    
    public void guardar(EstadoUsuario estado, boolean esNuevo) throws ExcepcionGeneralSistema {
        
        if(esNuevo){     
            if(estadoUsuarioDAO.getObjeto(EstadoUsuario.class, estado.getId())!=null){
                throw new ExcepcionGeneralSistema("El usuario "+estado.getId()+" ya existe");
            }            
            estadoUsuarioDAO.crear(estado);            
        }else{
            estadoUsuarioDAO.editar(estado);
        }        
    }    
    
    
    public List<EstadoUsuario>  getListaByBusqueda(String txtBusqueda, boolean muestraDeBaja, int cant){
        
        return estadoUsuarioDAO.getListaByBusqueda(txtBusqueda,muestraDeBaja,cant);
    }
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void eliminar(EstadoUsuario estadoUsuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public EstadoUsuario getEstado(Integer idEstado) {
        
        return estadoUsuarioDAO.getObjeto(EstadoUsuario.class, idEstado);
        
    }
}
