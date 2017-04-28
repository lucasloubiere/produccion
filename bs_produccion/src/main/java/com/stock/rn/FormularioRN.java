/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.rn;

import com.global.dao.FormularioDAO;
import com.global.excepciones.ExcepcionGeneralSistema;
import com.global.modelo.Formulario;
import com.global.modelo.FormularioPK;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author lloubiere
 */
@Stateless
public class FormularioRN {

    @EJB private FormularioDAO formularioDAO;
    

    @TransactionAttribute(TransactionAttributeType.REQUIRED)     
    public void guardar(Formulario formulario, boolean esNuevo) throws ExcepcionGeneralSistema {
        
        if(esNuevo){           
           if(formularioDAO.getFormulario(new FormularioPK(formulario.getModulo(), formulario.getCodigo()))!=null){
               throw new ExcepcionGeneralSistema("Ya existe un formulario con el código"+formulario.getCodigo());
           }
           formularioDAO.crear(formulario);            
        }else{               
            formularioDAO.editar(formulario);
        }           
    }

    public Formulario getFormulario(FormularioPK idPK){

        return formularioDAO.getFormulario(idPK);
    }
    
    public Formulario getFormulario(String modulo, String codigo){
        
        FormularioPK idPK = new FormularioPK(modulo,codigo);        
        return formularioDAO.getFormulario(idPK);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void guardar(Formulario formulario) throws Exception {

        FormularioPK idPK = new FormularioPK(formulario.getModulo(),formulario.getCodigo());

        if(formularioDAO.getFormulario(idPK)==null){
            formularioDAO.crear(formulario);
        }else{
            formularioDAO.editar(formulario);
        }
    }
     
   
    
    public void actualizarUltimoNumero(Formulario formulario) throws Exception {

        formulario.setUltimoNumero(formulario.getUltimoNumero() + 1);
        guardar(formulario);
    }

    public synchronized int tomarProximoNumero(Formulario formulario) throws ExcepcionGeneralSistema {
        try {
            FormularioPK idPK = new FormularioPK(formulario.getModulo(), formulario.getCodigo());
            Formulario f = getFormulario(idPK);
            if (f == null) {
                throw new ExcepcionGeneralSistema("No se encontró formulario " + formulario.getCodigo()+ " - " + formulario.getDescripcion());
            }
            int ultimoNumero = f.getUltimoNumero();
            ultimoNumero++;
            f.setUltimoNumero(ultimoNumero);
            guardar(f);
            return ultimoNumero;
        } catch (Exception ex) {
            Logger.getLogger(FormularioRN.class.getName()).log(Level.SEVERE,
                    "No es posible actualizar formulario ", ex);

            return 0;
        }
    }
        
    public List<Formulario> getFormularioByBusqueda(String txtBusqueda, boolean mostrarDebaja){
        
        return formularioDAO.getFormularioByBusqueda(null,txtBusqueda, mostrarDebaja, 15);
    }
    
    public List<Formulario> getFormularioByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja){
        
        return formularioDAO.getFormularioByBusqueda(filtro, txtBusqueda, mostrarDebaja, 15);        
    }

    public List<Formulario> getFormularioByBusqueda(Map<String, String> filtro, String txtBusqueda, boolean mostrarDebaja, int cantMaxima){
        
        return formularioDAO.getFormularioByBusqueda(filtro, txtBusqueda, mostrarDebaja, cantMaxima);        
    }    

    public void eliminar(Formulario formulario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
