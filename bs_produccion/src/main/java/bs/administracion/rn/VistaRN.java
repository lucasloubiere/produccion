/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.administracion.rn;


import bs.administracion.dao.VistaDAO;
import bs.administracion.modelo.Vista;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Agustin
 */
@Stateless
public class VistaRN {
    
    @EJB private VistaDAO vistaDAO;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void guardar(Vista v, boolean esNuevo) throws Exception{
        
        if(getVista(v.getCodigo())==null){
            vistaDAO.crear(v);
        }else{
            vistaDAO.editar(v);
        }
    }
    
    public void eliminar(Vista visita) throws Exception {
        
        vistaDAO.eliminar(visita);        
    }
    
    public Vista getVista(String codigo){
        
        return vistaDAO.getObjeto(Vista.class, codigo);        
    }   
    
    public List<Vista> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja){
        
        return vistaDAO.getListaByBusqueda(null,txtBusqueda, mostrarDebaja, 50);
    }
    
    public List<Vista> getListaByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja){
        
        return vistaDAO.getListaByBusqueda(filtro, txtBusqueda, mostrarDebaja, 50);        
    }

    public List<Vista> getListaByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja, int cantMaxima){
        
        return vistaDAO.getListaByBusqueda(filtro, txtBusqueda, mostrarDebaja, cantMaxima);        
    }

    public String obtenerSiguienteCogido(String origen) {
        
        String codigo = vistaDAO.obtenerSiguienteCodigo(origen);
        
        if(codigo==null || codigo.isEmpty()){
            codigo = origen.toUpperCase()+"_00001";
        }
        
        return codigo;        
    }
    
}
