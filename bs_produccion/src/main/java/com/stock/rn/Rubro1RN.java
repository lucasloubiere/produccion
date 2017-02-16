/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.rn;

import com.global.excepciones.ExcepcionGeneralSistema;
import com.stock.dao.Rubro1DAO;
import com.stock.modelo.Rubro1;
import com.stock.modelo.Rubro1PK;
import com.stock.modelo.TipoProducto;
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
    public void guardar(Rubro1 rubro1, boolean esNuevo) throws ExcepcionGeneralSistema {

        if (esNuevo) {

            Rubro1PK idPK = new Rubro1PK( rubro1.getCodigo(),rubro1.getTipoProducto());

            if (rubroDAO.getObjeto(Rubro1.class, idPK) != null) {
                throw new ExcepcionGeneralSistema("Ya existe un rubro con el código" + rubro1.getCodigo());
            }
            rubroDAO.crear(rubro1);
        } else {
            rubroDAO.editar(rubro1);
        }
    }

    public Rubro1 getRubro1(Rubro1PK idPK) {

        return rubroDAO.getObjeto(Rubro1.class, idPK);
    }

    public Rubro1 getRubro1(String codigo,String tipo ) {

        Rubro1PK idPK = new Rubro1PK(codigo,tipo);
        return rubroDAO.getObjeto(Rubro1.class, idPK);
    }

    public void eliminar(Rubro1 rubro1) throws Exception {
        rubroDAO.eliminar(rubro1);
    }

    public List<Rubro1> getLista(boolean mostrarDebaja) {

        return rubroDAO.getListaByBusqueda(null, "", mostrarDebaja, 50);
    }

    public List<Rubro1> getListaByBusqueda(String codTipo, String txtBusqueda, boolean mostrarDebaja, int cantRegistros) {

        if (codTipo == null) {
            return rubroDAO.getListaByBusqueda(null, txtBusqueda, mostrarDebaja, cantRegistros);
        } else {
            return rubroDAO.getListaByBusqueda(codTipo, txtBusqueda, mostrarDebaja, cantRegistros);
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
