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
@DiscriminatorValue("H")
public class ItemHorarioProduccion extends ItemMovimientoProduccion {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_iapl", referencedColumnName = "id")
    private ItemHorarioProduccion itemAplicado;

    @OneToMany(mappedBy = "itemAplicado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemHorarioProduccion> itemsAplicacion;

    public ItemHorarioProduccion getItemAplicado() {
        return itemAplicado;
    }

    public void setItemAplicado(ItemHorarioProduccion itemAplicado) {
        this.itemAplicado = itemAplicado;
    }

    public List<ItemHorarioProduccion> getItemsAplicacion() {
        return itemsAplicacion;
    }

    public void setItemsAplicacion(List<ItemHorarioProduccion> itemsAplicacion) {
        this.itemsAplicacion = itemsAplicacion;
    }
    
}
