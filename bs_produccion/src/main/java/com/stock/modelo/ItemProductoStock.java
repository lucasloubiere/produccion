/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stock.modelo;

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
@DiscriminatorValue("I")
public class ItemProductoStock extends ItemMovimientoStock {
    
    
}
