/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.stock.dao.Rubro1DAO;
import bs.stock.modelo.Rubro01;
import bs.stock.modelo.Rubro1PK;
import java.io.Serializable;
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

public class Rubro1RN implements Serializable {

    @EJB
    Rubro1DAO rubroDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardar(Rubro01 rubro1, boolean esNuevo) throws ExcepcionGeneralSistema {

        if (esNuevo) {

            Rubro1PK idPK = new Rubro1PK( rubro1.getCodigo(),rubro1.getTipoProducto());

            if (rubroDAO.getObjeto(Rubro01.class, idPK) != null) {
                throw new ExcepcionGeneralSistema("Ya existe un rubro con el código" + rubro1.getCodigo());
            }
            rubroDAO.crear(rubro1);
        } else {
            rubroDAO.editar(rubro1);
        }
    }

    public Rubro01 getRubro1(Rubro1PK idPK) {

        return rubroDAO.getObjeto(Rubro01.class, idPK);
    }

    public Rubro01 getRubro1(String tipo,String codigo) {

        Rubro1PK idPK = new Rubro1PK(tipo,codigo);
        return rubroDAO.getObjeto(Rubro01.class, idPK);
    }

    public void eliminar(Rubro01 rubro1) throws Exception {
        rubroDAO.eliminar(rubro1);
    }

    public List<Rubro01> getLista(boolean mostrarDebaja) {

        return rubroDAO.getListaByBusqueda(null, "", mostrarDebaja, 50);
    }

    public List<Rubro01> getListaByBusqueda(String codTipo, String txtBusqueda, boolean mostrarDebaja, int cantRegistros) {

        if (codTipo == null) {
            return rubroDAO.getListaByBusqueda(null, txtBusqueda, mostrarDebaja, cantRegistros);
        } else {
            return rubroDAO.getListaByBusqueda(codTipo, txtBusqueda, mostrarDebaja, cantRegistros);
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
