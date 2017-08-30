/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.sincronizacion.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.sincronizacion.dao.SincronizacionDAO;
import bs.sincronizacion.modelo.Sincronizacion;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author lloubiere
 */
@Stateless
public class SincronizacionRN implements Serializable {

    @EJB
    private SincronizacionDAO sincronizacionDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardar(Sincronizacion s, boolean esNuevo) throws Exception {

        if (esNuevo) {

            if (sincronizacionDAO.getObjeto(Sincronizacion.class, s.getCodigo()) != null) {
                throw new ExcepcionGeneralSistema("Ya existe sincronizacion con el código" + s.getCodigo());
            }
            sincronizacionDAO.crear(s);
        } else {
            sincronizacionDAO.editar(s);
        }
    }

    public void eliminar(Sincronizacion s) throws Exception {

        sincronizacionDAO.eliminar(s);
    }

    public Sincronizacion getSincronizacion(String cod) {
        return sincronizacionDAO.getSincronizacion(cod);
    }

    public List<Sincronizacion> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja, int cantidadRegistros) {

        return sincronizacionDAO.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
    }
    
    @Schedule(second="*/10")
    public void tareaProgramada(){
        System.out.println("Lucas Dormilón ...Se ejecutó la tarea");
    }
    
}
