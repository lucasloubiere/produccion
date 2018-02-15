/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.produccion.modelo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

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

    @Override
    public String toString() {
        return "ItemProcesoProduccion{" + "id=" + getId() + '}';
    }
        
}
