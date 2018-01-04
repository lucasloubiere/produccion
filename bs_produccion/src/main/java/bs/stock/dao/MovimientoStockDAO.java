/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.dao;

import bs.global.dao.BaseDAO;
import bs.stock.modelo.Deposito;
import bs.stock.modelo.ItemMovimientoStock;
import bs.stock.modelo.MovimientoStock;
import bs.stock.modelo.Producto;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

            System.err.println("Error al obtener movimientos de inventario " + e);
            return new ArrayList<MovimientoStock>();
        }
    }

    public BigDecimal getStockAFecha(Producto p, Deposito d, Date fecha) {

        try {
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

            Query q = em.createNativeQuery(" SELECT ifnull(sum(st_movimiento_item.STOCK) ,0) "
                    + " FROM st_movimiento INNER JOIN st_movimiento_item "
                    + " ON   st_movimiento.ID = st_movimiento_item.IDCAB "
                    + " WHERE st_movimiento_item.ARTCOD = ?1 "
                    + " AND st_movimiento_item.DEPOSI = ?2 "
                    + " AND st_movimiento.FCHMOV < ?3 ");
            
            q.setParameter("1", p.getCodigo());
            q.setParameter("2", d.getCodigo());
            q.setParameter("3", sdf.format(fecha));

            return (BigDecimal) q.getSingleResult();

        } catch (NoResultException nre) {

            return BigDecimal.ZERO;

        } catch (Exception e) {
            System.out.println("No se puede obtener stock por producto a fecha " + e);
            return BigDecimal.ZERO;
        }

    }

    public List<ItemMovimientoStock> getMovimientosEntreFechas(Producto p, Deposito d, Date fDesde, Date fHasta) {
        try {

            Query q = em.createQuery("SELECT i FROM ItemMovimientoStock i "
                    + "WHERE i.producto.codigo = :codigo "
                    + "AND i.deposito.codigo = :deposi "
                    + "AND i.movimiento.fechaMovimiento BETWEEN :fDesde AND :fHasta "
                    + "ORDER BY i.movimiento.fechaMovimiento ");

            q.setParameter("codigo", p.getCodigo());
            q.setParameter("deposi", d.getCodigo());
            q.setParameter("fDesde", fDesde);
            q.setParameter("fHasta", fHasta);

            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No se puede obtener stock entre fechas" + e.getMessage());
            return new ArrayList<ItemMovimientoStock>();
        }
    }

    public BigDecimal getCantidadFromMovimiento(String tipoMovimiento, Producto producto, Deposito deposito, Date fechaMovimientoDesde, Date fechaMovimientoHasta) {

        try {

            Query q = em.createQuery("SELECT sum(i.stocks) FROM ItemMovimientoStock i "
                    + "WHERE i.producto.codigo = :codigo "
                    + "AND i.deposito.codigo = :deposi "
                    + "AND i.movimiento.tipoMovimiento = :tipoMovimiento "
                    + "AND i.movimiento.fechaMovimiento BETWEEN :fDesde AND :fHasta ");

            q.setParameter("tipoMovimiento", tipoMovimiento);
            q.setParameter("codigo", producto.getCodigo());
            q.setParameter("deposi", deposito.getCodigo());
            q.setParameter("fDesde", fechaMovimientoDesde);
            q.setParameter("fHasta", fechaMovimientoHasta);

            return (BigDecimal) q.getSingleResult();

        } catch (NoResultException nre) {
            nre.printStackTrace();
            return BigDecimal.ZERO;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getCantidadFromMovimiento" + e);
            return BigDecimal.ZERO;

        }

    }

    public void recalcularStock() {
        
        try {
            Query q1 = em.createNativeQuery("DELETE FROM st_stock");
            
            q1.executeUpdate();
            
            Query q2 = em.createNativeQuery("INSERT INTO `st_stock` (`artcod`, `deposi`, `natri1`, `natri2`, `natri3`, `natri4`, `natri5`, `natri6`, `natri7`, "
                    + " `stocks`, `DEBAJA`, `FECALT`, `FECMOD`, `ULTOPR`) "
                    + " SELECT i.artcod, i.deposi,i.natri1,i.natri2,i.natri3,i.natri4,i.natri5,i.natri6,i.natri7, SUM(i.stock) as stocks,'N' as DEBAJA,CURDATE() AS FECALT, "
                    + " CURDATE() AS FECMOD,'A' AS ULTOPR"
                    + " FROM st_movimiento_item i inner JOIN st_movimiento m on m.id = i.idcab"
                    + " GROUP BY i.artcod, i.deposi,"
                    + " i.natri1,i.natri2,i.natri3,i.natri4,i.natri5,i.natri6,i.natri7");
            
            q2.executeUpdate();
        } catch (Exception e) {            
            
            System.err.println("recalcularStock " + e);
        }
        
    }
}
