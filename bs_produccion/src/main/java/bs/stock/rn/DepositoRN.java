/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.stock.dao.DepositoDAO;
import bs.stock.modelo.Deposito;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author lloubiere
 */
@Stateless
public class DepositoRN {

    @EJB
    private DepositoDAO depositoDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardar(Deposito deposito, boolean esNuevo) throws Exception {

        if (esNuevo) {

            if (depositoDAO.getObjeto(Deposito.class, deposito.getCodigo()) != null) {
                throw new ExcepcionGeneralSistema("Ya existe un depósito con el código" + deposito.getCodigo());
            }
            depositoDAO.crear(deposito);

        } else {
            depositoDAO.editar(deposito);
        }
    }

    public void eliminar(Deposito deposito) throws Exception {

        depositoDAO.eliminar(deposito);

    }

    public Deposito getDeposito(String id) {
        return depositoDAO.getDeposito(id);
    }

    public Deposito getDepositoByCodigoReferencia(String codigoReferencia) {
        return depositoDAO.getDepositoByCodigoReferencia(codigoReferencia);
    }

    public List<Deposito> getLista() {
        return depositoDAO.getLista();
    }

    public List<Deposito> getLista(int maxResults, int firstResult) {
        return depositoDAO.getLista(maxResults, firstResult);
    }

    public List<Deposito> getDepositoByBusqueda(String txtBusqueda, boolean mostrarDeBaja, int cantMax) {

        return depositoDAO.getDepositoByBusqueda(txtBusqueda, mostrarDeBaja, cantMax);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
