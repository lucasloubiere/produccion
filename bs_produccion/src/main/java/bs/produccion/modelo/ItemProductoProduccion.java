/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.modelo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 *
 * @author Claudio
 *
 * Entidad que registra los componentes a producir
 *
 */
@Entity
@DiscriminatorValue("P")
public class ItemProductoProduccion extends ItemMovimientoProduccion {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemProducto", fetch = FetchType.LAZY)
    private List<ItemDetalleItemProductoProduccion> itemDetalle;
    
    public ItemProductoProduccion() {
        super();
        this.itemDetalle = new ArrayList<ItemDetalleItemProductoProduccion>();
    }

    public List<ItemDetalleItemProductoProduccion> getItemDetalle() {
        return itemDetalle;
    }

    public void setItemDetalle(List<ItemDetalleItemProductoProduccion> itemDetalle) {
        this.itemDetalle = itemDetalle;
    }

    @Override
    public String toString() {
        return "ItemProductoProduccion{" + "id=" + getId() + '}';
    }
}
