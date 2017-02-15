/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguridad.dao;

import com.global.dao.BaseDAO;
import com.seguridad.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author ide
 */
@Stateless
public class UsuarioDAO extends BaseDAO {

    public Usuario getUsuario(String id) {
        return getObjeto(Usuario.class, id);
    }

    public List<Usuario> getUsuarioByBusqueda(String txtBusqueda, boolean mostrarDeBaja, int cantMax) {

        System.err.println("txtBusqueda " + txtBusqueda);
        System.err.println("mostrarDeBaja " + mostrarDeBaja);
        System.err.println("cantMax " + cantMax);

        try {
            String sQuery = "SELECT e FROM Usuario e "
                    + " WHERE (e.usuario LIKE :usuario OR e.nombre LIKE :nombre OR e.email LIKE :email) "
                    + (mostrarDeBaja ? " " : " AND e.auditoria.debaja = 'N' ")
                    + " ORDER BY e.usuario";

            Query q = em.createQuery(sQuery);
            q.setParameter("usuario", "%" + txtBusqueda.replaceAll(" ", "%") + "%");
            q.setParameter("nombre", "%" + txtBusqueda.replaceAll(" ", "%") + "%");
            q.setParameter("email", "%" + txtBusqueda.replaceAll(" ", "%") + "%");

            if (cantMax > 0) {
                q.setMaxResults(cantMax);
            }

            return q.getResultList();

        } catch (Exception e) {
            log.log(Level.SEVERE, "getUsuarioByBusqueda", e.getCause());
            return new ArrayList<Usuario>();
        }
    }

    public Usuario getUsuarioByEmail(String email) {
        return getObjeto(Usuario.class, "email", email);
    }

    public Usuario getUsuarioByNombre(String usuario) {
        return getObjeto(Usuario.class, "usuario", usuario);
    }

}
