/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.global.dao;

import bs.global.modelo.Comprobante;
import bs.global.modelo.ComprobantePK;
import javax.ejb.Stateless;


/**
 *
 * @author marceloagustini
 */

@Stateless
public class ComprobanteDAO extends BaseDAO {

    public Comprobante getComprobante(ComprobantePK idPK){

        return em.find(Comprobante.class, idPK);
    }
}
