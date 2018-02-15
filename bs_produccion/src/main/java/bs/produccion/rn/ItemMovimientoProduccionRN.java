/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.produccion.rn;


import bs.produccion.dao.ItemMovimientoProduccionDAO;
import bs.produccion.modelo.ItemAplicacionProduccion;
import bs.produccion.modelo.ItemComponenteProduccion;
import bs.produccion.modelo.ItemDetalleItemMovimientoProduccion;
import bs.produccion.modelo.ItemMovimientoProduccion;
import bs.produccion.modelo.ItemProcesoProduccion;
import bs.produccion.modelo.ItemProductoProduccion;
import bs.produccion.modelo.MovimientoProduccion;
import bs.produccion.vista.PendienteProduccionDetalle;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Claudio
 */
@Stateless
public class ItemMovimientoProduccionRN {
    
    @EJB private ItemMovimientoProduccionDAO itemMovimientoDAO;

    public ItemProductoProduccion nuevoItemProducto(MovimientoProduccion m){

        ItemProductoProduccion nItem = new ItemProductoProduccion();
        
        nItem.setNroitm(m.getItemsProducto().size()+1);        
        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(m);        
        nItem.setMovimientoOriginal(m);
        
        return nItem;
    }
    
    public ItemComponenteProduccion nuevoItemComponente(MovimientoProduccion m){

        ItemComponenteProduccion nItem = new ItemComponenteProduccion();

        nItem.setNroitm(m.getItemsComponente().size()+1);
        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(m);        
        nItem.setMovimientoOriginal(m);

        return nItem;

    }
    
    public ItemProcesoProduccion nuevoItemProceso(MovimientoProduccion m){

        ItemProcesoProduccion nItem = new ItemProcesoProduccion();

        nItem.setNroitm(m.getItemsProceso().size()+1);
        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(m);
        nItem.setMovimientoOriginal(m);

        return nItem;
    }
    
    public ItemAplicacionProduccion nuevoItemAplicacion(MovimientoProduccion m, PendienteProduccionDetalle ip) {

        ItemAplicacionProduccion nItem = new ItemAplicacionProduccion();
        nItem.setIdItemAplicacion(ip.getIdIapl());
        nItem.setNroitm(m.getItemsAplicacion().size() + 1);        

        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(ip.getMovimientoAplicacion());
        nItem.setMovimientoOriginal(ip.getMovimientoAplicacion());        

        return nItem;
    }
    
    public ItemAplicacionProduccion nuevoItemAplicacion(MovimientoProduccion m, ItemMovimientoProduccion ip) {

        ItemAplicacionProduccion nItem = new ItemAplicacionProduccion();
        nItem.setIdItemAplicacion(ip.getId());
        nItem.setNroitm(m.getItemsAplicacion().size() + 1);        

        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(ip.getMovimientoAplicacion());
        nItem.setMovimientoOriginal(ip.getMovimientoAplicacion());        

        return nItem;
    }

    public ItemDetalleItemMovimientoProduccion nuevoItemDetalle(ItemProductoProduccion itemProducto){

        ItemDetalleItemMovimientoProduccion itemDetalle = new ItemDetalleItemMovimientoProduccion();

        itemDetalle.setAtributo1(itemProducto.getAtributo1());
        itemDetalle.setAtributo2(itemProducto.getAtributo2());
        itemDetalle.setAtributo3(itemProducto.getAtributo3());
        itemDetalle.setAtributo4(itemProducto.getAtributo4());
        itemDetalle.setAtributo5(itemProducto.getAtributo5());
        itemDetalle.setAtributo6(itemProducto.getAtributo6());
        itemDetalle.setAtributo7(itemProducto.getAtributo7());

        itemDetalle.setProducto(itemProducto.getProducto());
        itemDetalle.setProductoOriginal(itemProducto.getProductoOriginal());

        itemDetalle .setCantidad(itemProducto.getCantidad());
        itemDetalle.setUnidadMedida(itemProducto.getUnidadMedida());
        
        itemDetalle.setItemProducto(itemProducto);
        
        return itemDetalle;
    }

//    public ItemProductoProduccionSerie nuevoItemSerie(PD_ItemProducto i){
//
//        ItemProductoProduccionSerie nItem = new ItemProductoProduccionSerie();
//
//        nItem.setModfor(i.getModfor());
//        nItem.setCodfor(i.getCodfor());
//        nItem.setNrofor(i.getNrofor());
//        nItem.setNroitm(i.getNroitm());
//        nItem.setNivexp("1");
//        nItem.setItmexp(i.getItmexp());
//        nItem.setNroitd(i.getItemSerie().size());
//
//        //Datos para aplicación
//        nItem.setModapl(i.getModfor());
//        nItem.setCodapl(i.getCodfor());
//        nItem.setNroapl(i.getNrofor());
//        nItem.setItmapl(i.getNroitm());
//        nItem.setNivapl("1");
//        nItem.setExpapl(i.getItmexp());
//
//        nItem.setProducto(i.getProducto());
//        nItem.setProductoOriginal(i.getProductoOriginal());
//
//        nItem.setItemProducto(i);
//
//        return nItem;
//    }

    public ItemProductoProduccion getItemProducto(Integer id) {
        return itemMovimientoDAO.getItemProducto(id);
    }

    public ItemAplicacionProduccion getItemAplicacion(Integer id) {
        return itemMovimientoDAO.getItemProductoAplicacion(id);
    }

    public ItemComponenteProduccion getItemComponente(Integer id) {
        return itemMovimientoDAO.getItemComponente(id);
    }

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
 
}
