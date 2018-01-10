/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.produccion.dao.OperarioPuestoDAO;
import bs.produccion.modelo.Operario;
import bs.produccion.modelo.OperarioPuesto;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;


/**
 *
 * @author Claudio
 */
@Stateless

public class OperarioPuestoRN {

    @EJB
    private OperarioPuestoDAO operarioPuestoDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardar(OperarioPuesto puesto, boolean esNuevo) throws Exception {

        if (esNuevo) {

            if (operarioPuestoDAO.getObjeto(Operario.class, puesto.getCodigo()) != null) {
                throw new ExcepcionGeneralSistema("Ya existe un puesto con ese código " + puesto.getCodigo());
            }
            operarioPuestoDAO.crear(puesto);
        } else {
            operarioPuestoDAO.editar(puesto);
        }
    }

    public void eliminar(Operario operario) throws Exception {

        operarioPuestoDAO.eliminar(operario);

    }

    public OperarioPuesto getOperarioPuesto(String cod) {
        return operarioPuestoDAO.getOperarioPuesto(cod);
    }

    public List<OperarioPuesto> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja, int cantidadRegistros) {

        return operarioPuestoDAO.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
    }
}
