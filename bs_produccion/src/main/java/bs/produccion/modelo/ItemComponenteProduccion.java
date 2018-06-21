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
@DiscriminatorValue("C")
public class ItemComponenteProduccion extends ItemMovimientoProduccion {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemComponente", fetch = FetchType.LAZY)
    private List<ItemDetalleComponenteProduccion> itemDetalle;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_IAPL", referencedColumnName = "id")
    private ItemComponenteProduccion itemAplicado;

    @OneToMany(mappedBy = "itemAplicado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemComponenteProduccion> itemsAplicacion;
    
    @Transient
    private List<ItemDetalleComponenteProduccion> itemDetalleTemporal;
    
    
    public ItemComponenteProduccion() {
        super();
        this.itemDetalle = new ArrayList<ItemDetalleComponenteProduccion>();
    }

    public List<ItemDetalleComponenteProduccion> getItemDetalle() {
        return itemDetalle;
    }

    public void setItemDetalle(List<ItemDetalleComponenteProduccion> itemDetalle) {
        this.itemDetalle = itemDetalle;
    }

    public ItemComponenteProduccion getItemAplicado() {
        return itemAplicado;
    }

    public void setItemAplicado(ItemComponenteProduccion itemAplicado) {
        this.itemAplicado = itemAplicado;
    }

    public List<ItemComponenteProduccion> getItemsAplicacion() {
        return itemsAplicacion;
    }

    public void setItemsAplicacion(List<ItemComponenteProduccion> itemsAplicacion) {
        this.itemsAplicacion = itemsAplicacion;
    }

    public List<ItemDetalleComponenteProduccion> getItemDetalleTemporal() {
        return itemDetalleTemporal;
    }

    public void setItemDetalleTemporal(List<ItemDetalleComponenteProduccion> itemDetalleTemporal) {
        this.itemDetalleTemporal = itemDetalleTemporal;
    }
       
    
    @Override
    public String toString() {
        return "ItemComponenteProduccion{" + "id=" + getId() + '}';
    }

}
