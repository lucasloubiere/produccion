/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.stock.dao.FormulaDAO;
import bs.stock.modelo.Formula;
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
public class FormulaRN {

   @EJB private FormulaDAO formulaDAO;
   
   @TransactionAttribute(TransactionAttributeType.REQUIRED)     
   public void guardar(Formula e, boolean esNuevo) throws Exception{
        
       if(esNuevo){
           
           if(formulaDAO.getObjeto(Formula.class, e.getCodigo())!=null){
               throw new ExcepcionGeneralSistema("Ya existe una fórmula con el código "+e.getCodigo());
           }
            formulaDAO.crear(e);
            
        }else{
               
            formulaDAO.editar(e);
        }        
   }
    
   public void eliminar(Formula deposito) throws Exception {
        
        formulaDAO.eliminar(deposito);
        
    }
  
    public List<Formula> getListaByBusqueda(String txtBusqueda, boolean mostrarDeBaja, int cantMax) {
        
        return formulaDAO.getListaByBusqueda(txtBusqueda,mostrarDeBaja,cantMax);
    }

    public Formula getFormula(String value) {
        
        return formulaDAO.getFormula(value);
    }
}
