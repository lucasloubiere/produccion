/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.produccion.dao.ItemMovimientoProduccionDAO;
import bs.produccion.modelo.ItemAplicacionProduccion;
import bs.produccion.modelo.ItemComponenteProduccion;
import bs.produccion.modelo.ItemDetalleItemComponenteProduccion;
import bs.produccion.modelo.ItemDetalleItemMovimientoProduccion;
import bs.produccion.modelo.ItemDetalleItemProductoProduccion;
import bs.produccion.modelo.ItemMovimientoProduccion;
import bs.produccion.modelo.ItemProcesoProduccion;
import bs.produccion.modelo.ItemProductoProduccion;
import bs.produccion.modelo.MovimientoProduccion;
import bs.produccion.modelo.TipoMovimientoProduccion;
import bs.produccion.vista.PendienteProduccionDetalle;
import bs.stock.modelo.ComposicionFormula;
import bs.stock.rn.ComposicionFormulaRN;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Claudio
 */
@Stateless
public class ItemMovimientoProduccionRN {

    @EJB
    protected ComposicionFormulaRN composicionFormulaRN;

    @EJB
    private ItemMovimientoProduccionDAO itemMovimientoDAO;

    public ItemProductoProduccion nuevoItemProducto(MovimientoProduccion m) {

        ItemProductoProduccion nItem = new ItemProductoProduccion();

        nItem.setNroitm(m.getItemsProducto().size() + 1);
        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(m);
        nItem.setMovimientoOriginal(m);

        return nItem;
    }

    public ItemComponenteProduccion nuevoItemComponente(MovimientoProduccion m) {

        ItemComponenteProduccion nItem = new ItemComponenteProduccion();

        nItem.setNroitm(m.getItemsComponente().size() + 1);
        nItem.setMovimiento(m);
        nItem.setMovimientoAplicacion(m);
        nItem.setMovimientoOriginal(m);

        return nItem;

    }

    public ItemProcesoProduccion nuevoItemProceso(MovimientoProduccion m) {

        ItemProcesoProduccion nItem = new ItemProcesoProduccion();

        nItem.setNroitm(m.getItemsProceso().size() + 1);
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

    public ItemDetalleItemProductoProduccion nuevoItemDetalleItemProducto(ItemProductoProduccion itemProducto) {

        ItemDetalleItemProductoProduccion itemDetalle = new ItemDetalleItemProductoProduccion();

        itemDetalle.setAtributo1(itemProducto.getAtributo1());
        itemDetalle.setAtributo2(itemProducto.getAtributo2());
        itemDetalle.setAtributo3(itemProducto.getAtributo3());
        itemDetalle.setAtributo4(itemProducto.getAtributo4());
        itemDetalle.setAtributo5(itemProducto.getAtributo5());
        itemDetalle.setAtributo6(itemProducto.getAtributo6());
        itemDetalle.setAtributo7(itemProducto.getAtributo7());

        itemDetalle.setProducto(itemProducto.getProducto());
        itemDetalle.setProductoOriginal(itemProducto.getProductoOriginal());

        itemDetalle.setCantidad(itemProducto.getCantidad());
        itemDetalle.setUnidadMedida(itemProducto.getUnidadMedida());

        itemDetalle.setItemProducto(itemProducto);
        return itemDetalle;
    }

    public ItemDetalleItemComponenteProduccion nuevoItemDetalleItemComponente(ItemComponenteProduccion itemComponente) {

        ItemDetalleItemComponenteProduccion itemDetalle = new ItemDetalleItemComponenteProduccion();

        itemDetalle.setAtributo1(itemComponente.getAtributo1());
        itemDetalle.setAtributo2(itemComponente.getAtributo2());
        itemDetalle.setAtributo3(itemComponente.getAtributo3());
        itemDetalle.setAtributo4(itemComponente.getAtributo4());
        itemDetalle.setAtributo5(itemComponente.getAtributo5());
        itemDetalle.setAtributo6(itemComponente.getAtributo6());
        itemDetalle.setAtributo7(itemComponente.getAtributo7());

        itemDetalle.setProducto(itemComponente.getProducto());
        itemDetalle.setProductoOriginal(itemComponente.getProductoOriginal());

        itemDetalle.setCantidad(itemComponente.getCantidad());
        itemDetalle.setUnidadMedida(itemComponente.getUnidadMedida());

        itemDetalle.setItemComponente(itemComponente);

        return itemDetalle;
    }

    public void agregarItemDetalleProducto(ItemProductoProduccion nItem) {

        ItemDetalleItemProductoProduccion nItemD = nuevoItemDetalleItemProducto(nItem);
        nItemD.setCantidad(BigDecimal.ZERO);
        nItem.getItemDetalle().add(nItemD);

    }

    public void agregarItemDetalleComponente(ItemComponenteProduccion nItem) {

        ItemDetalleItemComponenteProduccion nItemD = nuevoItemDetalleItemComponente(nItem);
        nItemD.setCantidad(BigDecimal.ZERO);
        nItem.getItemDetalle().add(nItemD);
    }

    public void generarItemFromItemMovimiento(MovimientoProduccion movimientoProduccion, ItemMovimientoProduccion itemMovimientoProduccion, ItemMovimientoProduccion itemNuevo) throws ExcepcionGeneralSistema {

        itemNuevo.setProducto(itemMovimientoProduccion.getProducto());
        itemNuevo.setProductoOriginal(itemMovimientoProduccion.getProducto());
        itemNuevo.setUnidadMedida(itemMovimientoProduccion.getUnidadMedida());
        itemNuevo.setOperario(itemMovimientoProduccion.getOperario());
        itemNuevo.setPrecio(itemMovimientoProduccion.getPrecio());

        itemNuevo.setAtributo1(itemMovimientoProduccion.getAtributo1());
        itemNuevo.setAtributo2(itemMovimientoProduccion.getAtributo2());
        itemNuevo.setAtributo3(itemMovimientoProduccion.getAtributo3());
        itemNuevo.setAtributo4(itemMovimientoProduccion.getAtributo4());
        itemNuevo.setAtributo5(itemMovimientoProduccion.getAtributo5());
        itemNuevo.setAtributo6(itemMovimientoProduccion.getAtributo6());
        itemNuevo.setAtributo7(itemMovimientoProduccion.getAtributo7());

        if (itemMovimientoProduccion.getProduccion() != null && itemMovimientoProduccion.getProduccion().compareTo(BigDecimal.ZERO) > 0) {
            itemNuevo.setCantidad(itemMovimientoProduccion.getProduccion());
            itemNuevo.setCantidadOriginal(itemMovimientoProduccion.getCantidad());
            itemNuevo.setCantidadStock(itemMovimientoProduccion.getProduccion());
        } else {
            itemNuevo.setCantidad(itemMovimientoProduccion.getPendiente());
            itemNuevo.setCantidadOriginal(itemMovimientoProduccion.getCantidad());
            itemNuevo.setCantidadStock(itemMovimientoProduccion.getPendiente());
        }

        itemNuevo.setActualizaStock(itemMovimientoProduccion.getActualizaStock());

        if (itemMovimientoProduccion.getComposicionFormula() != null) {
            itemNuevo.setComposicionFormula(itemMovimientoProduccion.getComposicionFormula());
        }

        //Si tiene asignado la toma de numero de serie se lo asignamos
        //Ahora toma nro de serie de hora de ruta
        if (movimientoProduccion.getTipoMovimiento().equals(TipoMovimientoProduccion.PP)) {

            SimpleDateFormat sdfAnio = new SimpleDateFormat("yy");
            SimpleDateFormat sdfSemana = new SimpleDateFormat("ww");
            SimpleDateFormat sdfDiaSemana = new SimpleDateFormat("uu");

            String nroLote = String.valueOf(itemMovimientoProduccion.getMovimiento().getNumeroFormulario()) + "-";
            nroLote = nroLote + sdfAnio.format(new Date());
            nroLote = nroLote + sdfSemana.format(new Date());
            nroLote = nroLote + sdfDiaSemana.format(new Date());

            itemNuevo.setAtributo3(nroLote);
        }

        itemNuevo.setTodoOk(true);

        //-----------------------------------------------------------
        //Verificamos si el circuito aplica a items pendietnes y si el item tiene movimiento a aplicar
        if (movimientoProduccion.getCircuito().getNoCancelaPendiente().equals("N") && itemMovimientoProduccion.getMovimientoAplicacion() != null) {

            //Genera los items aplicación
            ItemAplicacionProduccion iApl = nuevoItemAplicacion(movimientoProduccion, itemMovimientoProduccion);

            iApl.setProducto(itemMovimientoProduccion.getProducto());
            iApl.setProductoOriginal(itemMovimientoProduccion.getProducto());
            iApl.setUnidadMedida(itemMovimientoProduccion.getUnidadMedida());
            iApl.setCantidad(itemMovimientoProduccion.getProduccion().negate());
            iApl.setCantidadOriginal(itemMovimientoProduccion.getProduccion().negate());
            iApl.setCantidadStock(itemMovimientoProduccion.getProduccion());
            iApl.setActualizaStock(itemMovimientoProduccion.getActualizaStock());

            iApl.setOperario(itemMovimientoProduccion.getOperario());
            iApl.setPrecio(itemMovimientoProduccion.getPrecio());

            if (itemMovimientoProduccion.getComposicionFormula() != null) {
                iApl.setComposicionFormula(itemMovimientoProduccion.getComposicionFormula());
            }

            iApl.setTodoOk(true);
            //Agregamos el item a la lista
            movimientoProduccion.getItemsAplicacion().add(iApl);
        }
    }

    public void generarItemFromItemPendiente(MovimientoProduccion m, PendienteProduccionDetalle itemPendiente, ItemMovimientoProduccion itemNuevo) {

        itemNuevo.setProducto(itemPendiente.getProducto());
        itemNuevo.setProductoOriginal(itemPendiente.getProducto());
        itemNuevo.setUnidadMedida(itemPendiente.getUnidadMedida());

        if (itemPendiente.getCantidad() != null && itemPendiente.getCantidad().compareTo(BigDecimal.ZERO) > 0) {
            itemNuevo.setCantidad(itemPendiente.getCantidad());
            itemNuevo.setCantidadOriginal(itemPendiente.getCantidad());
            itemNuevo.setCantidadStock(itemPendiente.getCantidad());
        } else {
            itemNuevo.setCantidad(itemPendiente.getPendiente());
            itemNuevo.setCantidadOriginal(itemPendiente.getPendiente());
            itemNuevo.setCantidadStock(itemPendiente.getPendiente());
        }

        itemNuevo.setActualizaStock(itemPendiente.getStocks());

        if (itemPendiente.getFormul() != null && !itemPendiente.getFormul().isEmpty()) {
            ComposicionFormula composicionFormula = composicionFormulaRN.getComprosicionFormula(itemPendiente.getArtcod(), itemPendiente.getFormul());
            itemNuevo.setComposicionFormula(composicionFormula);
        }

        itemNuevo.setTodoOk(true);

        //-----------------------------------------------------------
        //Verificamos si el circuito aplica a items pendietnes
        if (m.getCircuito().getNoCancelaPendiente().equals("N")) {

            //Genera los items aplicación
            ItemAplicacionProduccion iApl = nuevoItemAplicacion(m, itemPendiente);

            iApl.setProducto(itemPendiente.getProducto());
            iApl.setProductoOriginal(itemPendiente.getProducto());
            iApl.setUnidadMedida(itemPendiente.getUnidadMedida());
            iApl.setCantidad(itemPendiente.getPendiente().negate());
            iApl.setCantidadOriginal(itemPendiente.getPendiente().negate());
            iApl.setCantidadStock(itemPendiente.getPendiente());
            iApl.setActualizaStock(itemPendiente.getStocks());

            if (itemPendiente.getFormul() != null && !itemPendiente.getFormul().isEmpty()) {
                ComposicionFormula composicionFormula = composicionFormulaRN.getComprosicionFormula(itemPendiente.getArtcod(), itemPendiente.getFormul());
                iApl.setComposicionFormula(composicionFormula);
            }

            iApl.setTodoOk(true);
            //Agregamos el item a la lista
            m.getItemsAplicacion().add(iApl);
        }
    }

    public boolean eliminarItemDetalleProducto(ItemProductoProduccion ip, ItemDetalleItemMovimientoProduccion nItem) throws ExcepcionGeneralSistema {

        boolean fItemBorrado = false;
        int i = 0;
        int indiceItemProducto = -1;

        //Verificamos que siempre quede 1 items detalle
        if (ip.getItemDetalle().size() == 1) {

            throw new ExcepcionGeneralSistema("No es posible eliminar item de apertura, la cantidad mínima es un item");
        }

        //Buscamos el indice del item a borrar
        for (ItemDetalleItemMovimientoProduccion id : ip.getItemDetalle()) {

            if (id.getId() == null) {
                if (id.getNroitm() == nItem.getNroitm()) {
                    indiceItemProducto = i;
                }
            } else if (id.getId().equals(nItem.getId())) {
                indiceItemProducto = i;
            }
            i++;
        }

        //Borramos los items productos
        if (indiceItemProducto >= 0) {
            ItemDetalleItemMovimientoProduccion remove = ip.getItemDetalle().remove(indiceItemProducto);

            if (remove != null) {
                fItemBorrado = true;
            }
        }
        return fItemBorrado;
    }

    public boolean eliminarItemDetalleComponente(ItemComponenteProduccion ip, ItemDetalleItemMovimientoProduccion nItem) throws ExcepcionGeneralSistema {

        boolean fItemBorrado = false;
        int i = 0;
        int indiceItemProducto = -1;

        //Verificamos que siempre quede 1 items detalle
        if (ip.getItemDetalle().size() == 1) {

            throw new ExcepcionGeneralSistema("No es posible eliminar item de apertura, la cantidad mínima es un item");
        }

        //Buscamos el indice del item a borrar
        for (ItemDetalleItemMovimientoProduccion id : ip.getItemDetalle()) {

            if (id.getId() == null) {
                if (id.getNroitm() == nItem.getNroitm()) {
                    indiceItemProducto = i;
                }
            } else if (id.getId().equals(nItem.getId())) {
                indiceItemProducto = i;
            }
            i++;
        }

        //Borramos los items productos
        if (indiceItemProducto >= 0) {
            ItemDetalleItemMovimientoProduccion remove = ip.getItemDetalle().remove(indiceItemProducto);

            if (remove != null) {
                fItemBorrado = true;
            }
        }
        return fItemBorrado;
    }

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
