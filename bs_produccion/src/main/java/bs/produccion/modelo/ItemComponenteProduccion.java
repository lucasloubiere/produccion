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
@DiscriminatorValue("C")
public class ItemComponenteProduccion extends ItemMovimientoProduccion {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemComponente", fetch = FetchType.LAZY)
    private List<ItemDetalleItemComponenteProduccion> itemDetalle;
    
    
    public ItemComponenteProduccion() {
        super();
        this.itemDetalle = new ArrayList<ItemDetalleItemComponenteProduccion>();
    }

    public List<ItemDetalleItemComponenteProduccion> getItemDetalle() {
        return itemDetalle;
    }

    public void setItemDetalle(List<ItemDetalleItemComponenteProduccion> itemDetalle) {
        this.itemDetalle = itemDetalle;
    }
    
    @Override
    public String toString() {
        return "ItemComponenteProduccion{" + "id=" + getId() + '}';
    }

}
