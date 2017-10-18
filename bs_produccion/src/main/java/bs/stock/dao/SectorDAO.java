/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.dao;

import bs.global.dao.BaseDAO;
import bs.stock.modelo.Sector;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author lloubiere
 */
@Stateless
public class SectorDAO extends BaseDAO {

    public List<Sector> getLista() {
        return getLista(Sector.class, true, -1, -1);
    }

    public List<Sector> getLista(int maxResults, int firstResult) {
        return getLista(Sector.class, false, maxResults, firstResult);
    }

    public Sector getSector(String codigo) {
        return getObjeto(Sector.class, codigo);
    }

    public Sector getSectorByCodigo(String codigo) {
        return getObjeto(Sector.class, "codigo", codigo);
    }

    public Sector getSectorByDescripcion(String descripcion) {
        return getObjeto(Sector.class, "descripcion", descripcion);
    }

    
    public List<Sector> getListaByBusqueda(Map<String, String> filtro,String txtBusqueda, boolean mostrarDeBaja, int cantMax) {
        try {

            String sQuery = "SELECT e FROM Sector e "
                    + " WHERE (e.codigo LIKE :codigo OR e.descripcion LIKE :descripcion) "
                    + (mostrarDeBaja ? " " : " AND e.auditoria.debaja = 'N' ")
                    + generarStringFiltro(filtro,"e", false)
                    + " ORDER BY e.codigo";

            Query q = em.createQuery(sQuery);
            q.setParameter("codigo", "%" + txtBusqueda.replaceAll(" ", "%") + "%");
            q.setParameter("descripcion", "%" + txtBusqueda.replaceAll(" ", "%") + "%");

            if (cantMax > 0) {
                q.setMaxResults(cantMax);
            }

            return q.getResultList();

        } catch (Exception e) {
            log.log(Level.SEVERE, "getSectorByBusqueda", e.getCause());
            return new ArrayList<Sector>();
        }
    }

}
