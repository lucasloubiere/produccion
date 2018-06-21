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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

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
    private List<ItemDetalleProductoProduccion> itemDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_IAPL", referencedColumnName = "id")
    private ItemProductoProduccion itemAplicado;

    @OneToMany(mappedBy = "itemAplicado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemProductoProduccion> itemsAplicacion;
    
    @Transient
    private List<ItemDetalleProductoProduccion> itemDetalleTemporal;

    public ItemProductoProduccion() {
        super();
        this.itemDetalle = new ArrayList<ItemDetalleProductoProduccion>();
    }

    public List<ItemDetalleProductoProduccion> getItemDetalle() {
        return itemDetalle;
    }

    public void setItemDetalle(List<ItemDetalleProductoProduccion> itemDetalle) {
        this.itemDetalle = itemDetalle;
    }

    public ItemProductoProduccion getItemAplicado() {
        return itemAplicado;
    }

    public void setItemAplicado(ItemProductoProduccion itemAplicado) {
        this.itemAplicado = itemAplicado;
    }

    public List<ItemProductoProduccion> getItemsAplicacion() {
        return itemsAplicacion;
    }

    public void setItemsAplicacion(List<ItemProductoProduccion> itemsAplicacion) {
        this.itemsAplicacion = itemsAplicacion;
    }

    public List<ItemDetalleProductoProduccion> getItemDetalleTemporal() {
        return itemDetalleTemporal;
    }

    public void setItemDetalleTemporal(List<ItemDetalleProductoProduccion> itemDetalleTemporal) {
        this.itemDetalleTemporal = itemDetalleTemporal;
    }
    
    @Override
    public String toString() {
        return "ItemProductoProduccion{" + "id=" + getId() + '}';
    }
}
