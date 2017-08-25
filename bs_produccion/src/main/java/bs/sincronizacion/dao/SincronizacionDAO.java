/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.sincronizacion.dao;

import bs.global.dao.BaseDAO;
import bs.sincronizacion.modelo.Sincronizacion;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author lloubiere
 */
@Stateless
public class SincronizacionDAO extends BaseDAO {

    public Sincronizacion getSincronizacion(String codigo) {
        return getObjeto(Sincronizacion.class, codigo);
    }

    public List<Sincronizacion> getListaByBusqueda(String txtBusqueda, boolean mostrarDeBaja, int cantMax) {
        try {

            String sQuery = "SELECT e FROM Sincronizacion e "
                    + " WHERE (e.codigo LIKE :codigo OR e.nombre LIKE :nombre) "
                    + (mostrarDeBaja ? " " : " AND e.auditoria.debaja = 'N' ")
                    + " ORDER BY e.codigo";

            Query q = em.createQuery(sQuery);
            q.setParameter("codigo", "%" + txtBusqueda.replaceAll(" ", "%") + "%");
            q.setParameter("nombre", "%" + txtBusqueda.replaceAll(" ", "%") + "%");

            if (cantMax > 0) {
                q.setMaxResults(cantMax);
            }

            return q.getResultList();

        } catch (Exception e) {
            log.log(Level.SEVERE, "getListaByBusqueda", e.getCause());
            return new ArrayList<Sincronizacion>();
        }
    }
}
