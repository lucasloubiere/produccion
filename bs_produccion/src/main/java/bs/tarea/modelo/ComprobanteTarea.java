/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.tarea.modelo;

import bs.global.modelo.Comprobante;
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
@DiscriminatorValue("TA")
public class ComprobanteTarea extends Comprobante {

    
}
