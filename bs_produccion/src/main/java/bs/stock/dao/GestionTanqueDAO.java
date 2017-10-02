/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.dao;

import bs.global.dao.BaseDAO;
import bs.stock.modelo.GestionTanque;
import bs.stock.modelo.ItemGestionTanque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author lloubiere
 */

@Stateless
public class GestionTanqueDAO extends BaseDAO{
    
        public void crearMovimiento(GestionTanque m) {
        em.persist(m);
    }

    public void editarMovimiento(GestionTanque m) {
        em.merge(m);
    }

    public void crearItemProducto(ItemGestionTanque im) {
        em.persist(im);

    }

    public void editarItemProducto(ItemGestionTanque im) {
        em.merge(im);
    }

    public GestionTanque getMovimiento(Integer id) {

        return getObjeto(GestionTanque.class, id);
    }

    public ItemGestionTanque getItemProducto(Integer id) {
        return getObjeto(ItemGestionTanque.class, id);
    }

    public List<GestionTanque> getListaByBusqueda(Map<String, String> filtro, int cantMax) {
        try {
            String sQuery = "SELECT m FROM GestionTanque m ";
            sQuery += generarStringFiltro(filtro, "m", true);
            sQuery += " ORDER BY m.fechaMovimiento DESC, m.numeroFormulario DESC";

            Query q = em.createQuery(sQuery);

            if (cantMax > 0) {
                q.setMaxResults(cantMax);
            }

            return q.getResultList();

        } catch (NoResultException e) {

            return new ArrayList<GestionTanque>();

        } catch (Exception e) {

            System.err.println("Error al obtener movimientos de inventario " + e);
            return new ArrayList<GestionTanque>();
        }
    }

    public GestionTanque getUltimoRegistro() {        
        
        try {
            String sQuery = "SELECT m FROM GestionTanque m ";            
            sQuery += " ORDER BY m.fechaMovimiento DESC, m.numeroFormulario DESC";

            Query q = em.createQuery(sQuery);
            
            q.setMaxResults(1);
            return (GestionTanque) q.getSingleResult();

        } catch (NoResultException e) {

            return null;

        } catch (Exception e) {

            System.err.println("Error al obtener ultimo movimiento " + e);
            return null;
        }
            
    }
   
}
