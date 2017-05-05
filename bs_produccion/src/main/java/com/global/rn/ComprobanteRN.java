/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.global.rn;

import com.global.dao.ComprobanteDAO;
import com.global.modelo.Comprobante;
import com.global.modelo.ComprobantePK;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Claudio
 */
@Stateless
public class ComprobanteRN {

    @EJB private ComprobanteDAO comprobanteDAO;

    public Comprobante getComprobante(ComprobantePK idPK){

        return comprobanteDAO.getComprobante(idPK);
    }

    public Comprobante getComprobante(String modcom, String codcom){

        return comprobanteDAO.getComprobante(new ComprobantePK(modcom, codcom));
    }



    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
 
}
