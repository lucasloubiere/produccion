/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.produccion.dao.DepartamentoProduccionDAO;
import bs.produccion.modelo.DepartamentoProduccion;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;


/**
 *
 * @author lloubiere
 *
 */
@Stateless

public class DepartamentoProduccionRN {

    @EJB
    private DepartamentoProduccionDAO departamentoProduccionDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardar(DepartamentoProduccion departamentoProduccion, boolean esNuevo) throws Exception {

        if (esNuevo) {

            if (departamentoProduccionDAO.getObjeto(DepartamentoProduccion.class, departamentoProduccion.getCodigo()) != null) {
                throw new ExcepcionGeneralSistema("Ya existe un operario con ese código " + departamentoProduccion.getCodigo());
            }
            departamentoProduccionDAO.crear(departamentoProduccion);
        } else {
            departamentoProduccionDAO.editar(departamentoProduccion);
        }
    }

    public void eliminar(DepartamentoProduccion departamentoProduccion) throws Exception {

        departamentoProduccionDAO.eliminar(departamentoProduccion);

    }

    public DepartamentoProduccion getDepartamentoProduccion(String cod) {
        return departamentoProduccionDAO.getDepartamentoProduccion(cod);
    }

    public List<DepartamentoProduccion> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja, int cantidadRegistros) {

        return departamentoProduccionDAO.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
    }
}
