/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.global.rn;

import bs.administracion.rn.ParametrosRN;
import bs.global.dao.MonedaValoresDAO;
import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.modelo.MonedaValores;
import bs.global.modelo.MonedaValoresPK;
import java.math.BigDecimal;
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
public class MonedaValoresRN {

    @EJB
    private MonedaValoresDAO monedaValoresDAO;
    @EJB
    protected ParametrosRN parametrosRN;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardar(MonedaValores mv, boolean esNuevo) throws Exception {

        if (esNuevo) {

            MonedaValoresPK idPk = new MonedaValoresPK(mv.getCodcof(), mv.getFechaAlta());

            if (monedaValoresDAO.getObjeto(MonedaValores.class, idPk) != null) {
                throw new ExcepcionGeneralSistema("Ya extiste una cotización con la misma fecha " + mv.getFecha());
            }
            monedaValoresDAO.crear(mv);
        } else {
            monedaValoresDAO.editar(mv);
        }
    }

    public MonedaValores getMonedaValores(String codcof, String fecalt) {

        return monedaValoresDAO.getMoneda(codcof, fecalt);
    }

    public List<MonedaValores> getLista() {
        return monedaValoresDAO.getListaByBusqueda("", "", false, 15);
    }

    public BigDecimal getCotizacionDia(String codCoeficiente) {

        if (codCoeficiente.equals(parametrosRN.getParametro().getCodigoMonedaPrimaria())) {
            return BigDecimal.ONE;
        } else {
            return monedaValoresDAO.getCotizacionDia(codCoeficiente).getCotizacion().setScale(2);
        }

    }

    public void eliminar(MonedaValores mv) throws Exception {
        monedaValoresDAO.eliminar(mv);
    }

}
