/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.dao;

import com.global.dao.BaseDAO;
import com.stock.modelo.ItemMovimientoStock;
import com.stock.modelo.MovimientoStock;
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
public class MovimientoStockDAO extends BaseDAO {

    public void crearMovimiento(MovimientoStock m) {
        em.persist(m);
    }

    public void editarMovimiento(MovimientoStock m) {
        em.merge(m);
    }

    public void crearItemProducto(ItemMovimientoStock im) {
        em.persist(im);

    }

    public void editarItemProducto(ItemMovimientoStock im) {
        em.merge(im);
    }

    public MovimientoStock getMovimiento(Integer id) {

        return getObjeto(MovimientoStock.class, id);
    }

    public ItemMovimientoStock getItemProducto(Integer id) {
        return getObjeto(ItemMovimientoStock.class, id);
    }

    public List<MovimientoStock> getListaByBusqueda(Map<String, String> filtro, int cantMax) {
        try {
            String sQuery = "SELECT m FROM MovimientoStock m ";
            sQuery += generarStringFiltro(filtro, "m", true);
            sQuery += " ORDER BY m.fechaMovimiento DESC, m.numeroFormulario DESC";

            Query q = em.createQuery(sQuery);

            if (cantMax > 0) {
                q.setMaxResults(cantMax);
            }

            return q.getResultList();

        } catch (NoResultException e) {
            
            return new ArrayList<MovimientoStock>();
        } catch (Exception e) {
            
            System.err.println("Error al obtener movimientos de inventario");
            return new ArrayList<MovimientoStock>();
        }
    }
}
