/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.produccion.modelo;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Claudio
 *
 * Entidad que registra los componentes a producir
 *
 */
@Entity
@DiscriminatorValue("R")
public class ItemProcesoProduccion extends ItemMovimientoProduccion {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_IAPL", referencedColumnName = "id")
    private ItemProcesoProduccion itemAplicado;

    @OneToMany(mappedBy = "itemAplicado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemProcesoProduccion> itemsAplicacion;

    public ItemProcesoProduccion getItemAplicado() {
        return itemAplicado;
    }

    public void setItemAplicado(ItemProcesoProduccion itemAplicado) {
        this.itemAplicado = itemAplicado;
    }

    public List<ItemProcesoProduccion> getItemsAplicacion() {
        return itemsAplicacion;
    }

    public void setItemsAplicacion(List<ItemProcesoProduccion> itemsAplicacion) {
        this.itemsAplicacion = itemsAplicacion;
    }
    
    @Override
    public String toString() {
        return "ItemProcesoProduccion{" + "id=" + getId() + '}';
    }
        
}
