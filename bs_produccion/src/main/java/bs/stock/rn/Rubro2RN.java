/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.stock.dao.Rubro2DAO;
import bs.stock.modelo.Rubro2;
import bs.stock.modelo.Rubro2PK;
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

public class Rubro2RN implements Serializable {

    @EJB
    Rubro2DAO rubroDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardar(Rubro2 rubro2, boolean esNuevo) throws ExcepcionGeneralSistema {

        if (esNuevo) {

            Rubro2PK idPK = new Rubro2PK( rubro2.getCodigo(),rubro2.getTipoProducto());

            if (rubroDAO.getObjeto(Rubro2.class, idPK) != null) {
                throw new ExcepcionGeneralSistema("Ya existe un rubro con el código" + rubro2.getCodigo());
            }
            rubroDAO.crear(rubro2);
        } else {
            rubroDAO.editar(rubro2);
        }
    }

    public Rubro2 getRubro2(Rubro2PK idPK) {

        return rubroDAO.getObjeto(Rubro2.class, idPK);
    }

    public Rubro2 getRubro2(String tipo,String codigo) {

        Rubro2PK idPK = new Rubro2PK(tipo,codigo);
        return rubroDAO.getObjeto(Rubro2.class, idPK);
    }

    public void eliminar(Rubro2 rubro2) throws Exception {
        rubroDAO.eliminar(rubro2);
    }

    public List<Rubro2> getLista(boolean mostrarDebaja) {

        return rubroDAO.getListaByBusqueda(null, "", mostrarDebaja, 50);
    }

    public List<Rubro2> getListaByBusqueda(String codTipo, String txtBusqueda, boolean mostrarDebaja, int cantRegistros) {

        if (codTipo == null) {
            return rubroDAO.getListaByBusqueda(null, txtBusqueda, mostrarDebaja, cantRegistros);
        } else {
            return rubroDAO.getListaByBusqueda(codTipo, txtBusqueda, mostrarDebaja, cantRegistros);
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
