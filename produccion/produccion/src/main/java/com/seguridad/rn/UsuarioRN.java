/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguridad.rn;

import com.seguridad.dao.UsuarioDAO;
import com.seguridad.modelo.Usuario;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ide
 */
@Stateless
public class UsuarioRN {

    @EJB  private UsuarioDAO usuarioDAO;
    
    public Usuario getUsuarioByNombre(String nombre){
        
        return usuarioDAO.getUsuarioByNombre(nombre);
    }
    

// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
