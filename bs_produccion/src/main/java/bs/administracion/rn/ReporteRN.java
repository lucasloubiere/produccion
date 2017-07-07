/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.administracion.rn;


import bs.administracion.dao.ReporteDAO;
import bs.administracion.modelo.Reporte;
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
public class ReporteRN {
    
    @EJB private ReporteDAO vistaDAO;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void guardar(Reporte v, boolean esNuevo) throws Exception{
        
        if(getReporte(v.getCodigo())==null){
            vistaDAO.crear(v);
        }else{
            vistaDAO.editar(v);
        }
    }
    
    public void eliminar(Reporte visita) throws Exception {
        
        vistaDAO.eliminar(visita);        
    }
    
    public Reporte getReporte(String codigo){
        
        return vistaDAO.getObjeto(Reporte.class, codigo);        
    }   
    
    public List<Reporte> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja){
        
        return vistaDAO.getListaByBusqueda(null,txtBusqueda, mostrarDebaja, 50);
    }
    
    public List<Reporte> getListaByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja){
        
        return vistaDAO.getListaByBusqueda(filtro, txtBusqueda, mostrarDebaja, 50);        
    }

    public List<Reporte> getListaByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja, int cantMaxima){
        
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
