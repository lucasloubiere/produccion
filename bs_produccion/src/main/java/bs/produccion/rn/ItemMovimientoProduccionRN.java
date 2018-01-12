/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.produccion.rn;


import bs.produccion.dao.ItemMovimientoProduccionDAO;
import bs.produccion.modelo.ItemAnulacionProduccion;
import bs.produccion.modelo.ItemAplicacionProduccion;
import bs.produccion.modelo.ItemComponenteProduccion;
import bs.produccion.modelo.ItemDetalleItemMovimientoProduccion;
import bs.produccion.modelo.ItemMovimientoProduccion;
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

    public ItemAplicacionProduccion nuevoItemAplicacion(MovimientoProduccion m, PendienteProduccionDetalle ip) {

        ItemAplicacionProduccion nItem = new ItemAplicacionProduccion();
        nItem.setIdItemAplicacion(ip.getIdIapl());
        nItem.setNroitm(m.getItemsProductoAplicacion().size() + 1);        

        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(ip.getMovimientoAplicacion());
        nItem.setMovimientoOriginal(ip.getMovimientoAplicacion());        

        return nItem;
    }
    
    public ItemAplicacionProduccion nuevoItemAplicacion(MovimientoProduccion m, ItemMovimientoProduccion ip) {

        ItemAplicacionProduccion nItem = new ItemAplicacionProduccion();
        nItem.setIdItemAplicacion(ip.getId());
        nItem.setNroitm(m.getItemsProductoAplicacion().size() + 1);        

        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(ip.getMovimientoAplicacion());
        nItem.setMovimientoOriginal(ip.getMovimientoAplicacion());        

        return nItem;
    }

    public ItemAnulacionProduccion nuevoItemAnulacion(MovimientoProduccion m){

        ItemAnulacionProduccion nItem = new ItemAnulacionProduccion();

        
        nItem.setNroitm(m.getItemsProductoAnulacion().size()+1);
        nItem.setMovimiento(m);
        nItem.setMovimientoOriginal(m);

        return nItem;

    }

    public ItemDetalleItemMovimientoProduccion nuevoItemDetalle(ItemProductoProduccion i){

        ItemDetalleItemMovimientoProduccion nItem = new ItemDetalleItemMovimientoProduccion();

        nItem.setProducto(i.getProducto());
        nItem.setProductoOriginal(i.getProductoOriginal());

        nItem.setCantidad(i.getCantidad());  
        nItem.setUnidadMedida(i.getUnidadMedida());
        
        nItem.setAtributo1(i.getAtributo1());
        nItem.setAtributo2(i.getAtributo2());
        nItem.setAtributo3(i.getAtributo3());
        nItem.setAtributo4(i.getAtributo4());
        nItem.setAtributo5(i.getAtributo5());
        nItem.setAtributo6(i.getAtributo6());
        nItem.setAtributo7(i.getAtributo7());
        
        nItem.setItemProducto(i);
        i.getItemDetalle().add(nItem);

        return nItem;
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

    public ItemAplicacionProduccion getItemProductoAplicacion(Integer id) {
        return itemMovimientoDAO.getItemProductoAplicacion(id);
    }

    public ItemComponenteProduccion getItemComponente(Integer id) {
        return itemMovimientoDAO.getItemComponente(id);
    }

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
 
}
