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
@DiscriminatorValue("C")
public class ItemDetalleItemComponenteProduccion extends ItemDetalleItemMovimientoProduccion {
   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_idet", referencedColumnName = "id", nullable = false) 
    private ItemComponenteProduccion itemComponente;

    public ItemComponenteProduccion getItemComponente() {
        return itemComponente;
    }

    public void setItemComponente(ItemComponenteProduccion itemComponente) {
        this.itemComponente = itemComponente;
    }
    
    @Override
    public String toString() {
        return "ItemDetalleComponenteProduccion{" + "id=" + getId() + '}';
    }

}
