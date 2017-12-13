/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.dao;

import bs.global.dao.BaseDAO;
import bs.produccion.modelo.ItemAplicacionProduccion;
import bs.produccion.modelo.ItemComponenteProduccion;
import bs.produccion.modelo.ItemProductoProduccion;
import javax.ejb.Stateless;


/**
 *
 * @author ctrosch
 */

@Stateless
public class ItemMovimientoProduccionDAO extends BaseDAO {
      

    public ItemProductoProduccion getItemProducto(Integer id) {
       return getObjeto(ItemProductoProduccion.class, id);
    }

    public ItemAplicacionProduccion getItemProductoAplicacion(Integer id) {
       return getObjeto(ItemAplicacionProduccion.class, id);
    }

    public ItemComponenteProduccion getItemComponente(Integer id) {
       return getObjeto(ItemComponenteProduccion.class, id);
    }

}
