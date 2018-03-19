/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.global.rn;

import bs.administracion.rn.ParametrosRN;
import bs.global.dao.MonedaDAO;
import bs.global.dao.TemporalDAO;
import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.modelo.Moneda;
import bs.global.modelo.MonedaValores;
import bs.global.modelo.Temporal;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author ctrosch
 */
@Stateless
public class TemporalRN {

    @EJB
    private TemporalDAO temporalDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardar(Temporal temporal) throws Exception {

        temporalDAO.crear(temporal);

    }
    
    public void vaciarTabla(){
        
        temporalDAO.vaciarTabla();
    }

    public void guardarLista() {

    }
}
