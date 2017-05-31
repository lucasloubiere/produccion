/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.global.rn;


import bs.global.dao.FormularioPorSituacionIVADAO;
import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.global.modelo.Comprobante;
import bs.global.modelo.Formulario;
import bs.global.modelo.FormularioPorSituacionIVA;
import bs.global.modelo.FormularioPorSituacionIVAPK;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Claudio
 */
@Stateless
public class FormularioPorSituacionIVARN {

    @EJB private FormularioPorSituacionIVADAO formularioPorSituacionIvaDAO;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void guardar(FormularioPorSituacionIVA fsi, boolean esNuevo) throws Exception{
        
        puedoGuardar(fsi,esNuevo);
        
        if(esNuevo) {
            
            fsi.setCndiva(fsi.getCondicionDeIva().getCodigo());
            fsi.setSucurs(fsi.getSucursal().getCodigo());
            
            FormularioPorSituacionIVAPK idPk = new FormularioPorSituacionIVAPK(fsi.getModcom(), fsi.getCodcom(), fsi.getSucurs(), fsi.getCndiva());
            
            if(formularioPorSituacionIvaDAO.getObjeto(FormularioPorSituacionIVA.class, idPk)!=null){
                throw new ExcepcionGeneralSistema("Ya existe un formulario asignado para esta sucural y condición de iva ");
            }
            formularioPorSituacionIvaDAO.crear(fsi);
        }else{
            formularioPorSituacionIvaDAO.editar(fsi);
        }
    }
    
    public void puedoGuardar(FormularioPorSituacionIVA fsi, boolean esNuevo) throws ExcepcionGeneralSistema{
              
        if(fsi!=null){
            
            if(fsi.getFormulario()==null){
                throw new ExcepcionGeneralSistema("El formulario no puede estar vacío");                    
            }
            
            if(fsi.getCondicionDeIva()==null){
                throw new ExcepcionGeneralSistema("La condición de iva no puede estar vacía");                    
            }
            
            if(fsi.getSucursal()==null){
                throw new ExcepcionGeneralSistema("La sucursal no puede estar vacía");                    
            }
        }  
   }
    

    public void eliminar(FormularioPorSituacionIVA formularioPorSituacionIva) throws Exception {
        
        formularioPorSituacionIvaDAO.eliminar(formularioPorSituacionIva);
    }

    public FormularioPorSituacionIVA getFormulario(String modfor, String codfor,String sucurs, String cndiva){

        FormularioPorSituacionIVAPK idPK = new FormularioPorSituacionIVAPK(modfor, codfor,sucurs, cndiva);
        return formularioPorSituacionIvaDAO.getFormulario(idPK);
    }

    public FormularioPorSituacionIVA getFormulario(FormularioPorSituacionIVAPK idPK){

        return formularioPorSituacionIvaDAO.getFormulario(idPK);
    }
    
    public List<Formulario> getFormularioByComprobante(Comprobante comprobante){

        return formularioPorSituacionIvaDAO.getFormularioByComprobante(comprobante);
    }

    public List<FormularioPorSituacionIVA> getListaByComprobante(Comprobante comp, boolean mostrarDebaja) {
        
        return formularioPorSituacionIvaDAO.getListaByBusqueda(comp.getModulo(), comp.getCodigo(),null, "", mostrarDebaja, 50);        
        
    }

    public List<FormularioPorSituacionIVA> getListaByBusqueda(Comprobante comp, String txtBusqueda, boolean mostrarDebaja) {

        return formularioPorSituacionIvaDAO.getListaByBusqueda(comp.getModulo(), comp.getCodigo(),null, txtBusqueda, mostrarDebaja, 50);        
    }
 
}
