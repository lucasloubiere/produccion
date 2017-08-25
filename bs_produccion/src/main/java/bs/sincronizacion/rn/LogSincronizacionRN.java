/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.sincronizacion.rn;

import bs.sincronizacion.dao.LogSincronizacionDAO;
import bs.sincronizacion.modelo.LogSincronizacion;
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
public class LogSincronizacionRN implements Serializable {

    @EJB
    private LogSincronizacionDAO logSincronizacionDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardar(LogSincronizacion s, boolean esNuevo) throws Exception {
       

        if (s.getId() == null) {                
            logSincronizacionDAO.crear(s);
        } else {
            logSincronizacionDAO.editar(s);
        }
    }

    public void eliminar(LogSincronizacion s) throws Exception {

        logSincronizacionDAO.eliminar(s);
    }

    public LogSincronizacion getLogSincronizacion(String cod) {
        return logSincronizacionDAO.getLogSincronizacion(cod);
    }

    public List<LogSincronizacion> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja, int cantidadRegistros) {

        return logSincronizacionDAO.getListaByBusqueda(txtBusqueda, mostrarDebaja, cantidadRegistros);
    }
    
}
