/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguridad.dao;

import com.global.dao.BaseDAO;
import com.seguridad.modelo.Usuario;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ide
 */
@Stateless
public class UsuarioDAO extends BaseDAO {
        
    public Usuario getUsuario(String id) {
        return getObjeto(Usuario.class, id);
    }      

    public List<Usuario> getLista() {
        return getLista(Usuario.class, true, -1, -1);
    }

    public List<Usuario> getLista(int maxResults, int firstResult) {
        return getLista(Usuario.class, false, maxResults, firstResult);
    }   
    
    public Usuario getUsuarioByEmail(String email) {
        return getObjeto(Usuario.class,"email", email);
    }
 
    public Usuario getUsuarioByNombre(String usuario) {
        return getObjeto(Usuario.class,"usuario", usuario);
    }
    
}
