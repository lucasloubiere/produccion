/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.produccion.dao.OperarioDAO;
import bs.produccion.modelo.Operario;
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

public class OperarioRN {

    @EJB
    private OperarioDAO operarioDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardar(Operario operario, boolean esNuevo) throws Exception {

        if (esNuevo) {

            if (operarioDAO.getObjeto(Operario.class, operario.getCodigo()) != null) {
                throw new ExcepcionGeneralSistema("Ya existe un operario con ese código " + operario.getCodigo());
            }
            operarioDAO.crear(operario);
        } else {
            operarioDAO.editar(operario);
        }
    }

    public void eliminar(Operario operario) throws Exception {

        operarioDAO.eliminar(operario);

    }

    public Operario getOperario(String cod) {
        return operarioDAO.getOperario(cod);
    }

    public List<Operario> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja, int cantidadRegistros) {

        return operarioDAO.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
    }
}
