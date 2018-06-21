/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.produccion.modelo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Claudio
 *
 * Entidad que registra los componentes a producir
 *
 */
@Entity
@DiscriminatorValue("P")
public class ItemDetalleProductoProduccion extends ItemDetalleMovimientoProduccion {
    
        
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_idet", referencedColumnName = "id", nullable = false) 
    private ItemProductoProduccion itemProducto;

    public ItemProductoProduccion getItemProducto() {
        return itemProducto;
    }

    public void setItemProducto(ItemProductoProduccion itemProducto) {
        this.itemProducto = itemProducto;
    }
        
    @Override
    public String toString() {
        return "ItemDetalleProductoProduccion{" + "id=" + getId() + '}';
    }

}
