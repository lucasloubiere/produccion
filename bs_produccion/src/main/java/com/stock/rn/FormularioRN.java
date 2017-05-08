/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.rn;

import com.global.dao.FormularioDAO;
import com.global.excepciones.ExcepcionGeneralSistema;
import com.global.modelo.Comprobante;
import com.global.modelo.Formulario;
import com.global.modelo.FormularioPK;
import com.global.modelo.FormularioPorSituacionIVA;
import com.global.modelo.Sucursal;
import com.global.rn.FormularioPorSituacionIVARN;
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
    @EJB private FormularioPorSituacionIVARN formularioPorSituacionIVARN;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)     
    public void guardar(Formulario formulario, boolean esNuevo) throws ExcepcionGeneralSistema {
        
        if(esNuevo){           
           if(formularioDAO.getFormulario(new FormularioPK(formulario.getModfor(), formulario.getCodigo()))!=null){
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

        FormularioPK idPK = new FormularioPK(formulario.getModfor(),formulario.getCodigo());

        if(formularioDAO.getFormulario(idPK)==null){
            formularioDAO.crear(formulario);
        }else{
            formularioDAO.editar(formulario);
        }
    }
     
    public Formulario getFormulario(Comprobante comprobante, Sucursal sucursal, String cndIva) throws ExcepcionGeneralSistema {

        
        if(comprobante==null){
            throw new ExcepcionGeneralSistema("No es posible obtener formulario, comprobante en blanco");
        }
        
        if(cndIva==null){
            throw new ExcepcionGeneralSistema("No es posible obtener formulario, la entidad comercial seleccionado no tienen condición de iva asignada");
        }
        
        if(sucursal==null){
            throw new ExcepcionGeneralSistema("No es posible obtener formulario, sucursal en blanco");
        }
        
        /**
         * Validamos el tipo de movimiento para buscar el formulario por situacion de iva
         * lo correcto es verificar desde donde registra para ver si se busca la situacion de iva
         */
        FormularioPorSituacionIVA fpsi;
        fpsi = formularioPorSituacionIVARN.getFormulario(comprobante.getModulo(), 
                comprobante.getCodigo(),
                sucursal.getCodigo(),
                cndIva);
        
        if(fpsi==null) throw new ExcepcionGeneralSistema("No se encontró formulario por situación de iva para comprobante "
                + "("+ comprobante.getModulo()+"-"+comprobante.getCodigo()+")"+ comprobante.getDescripcion() 
                + " Condición de IVA: " + cndIva);
        
        return  fpsi.getFormulario();
    }
    
    public void actualizarUltimoNumero(Formulario formulario) throws Exception {

        formulario.setUltimoNumero(formulario.getUltimoNumero() + 1);
        guardar(formulario);
    }

    public synchronized int tomarProximoNumero(Formulario formulario) throws ExcepcionGeneralSistema {
        try {
            FormularioPK idPK = new FormularioPK(formulario.getModfor(), formulario.getCodigo());
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