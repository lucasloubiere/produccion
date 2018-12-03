/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.stock.rn;


import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.stock.dao.ComposicionFormulaDAO;
import bs.stock.modelo.ComposicionFormula;
import bs.stock.modelo.ComposicionFormulaPK;
import bs.stock.modelo.ItemComposicionFormulaComponente;
import bs.stock.modelo.ItemComposicionFormulaPK;
import bs.stock.modelo.ItemComposicionFormulaProceso;
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
public class ComposicionFormulaRN {

    @EJB private ComposicionFormulaDAO composicionFormulaDAO;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)   
    public ComposicionFormula guardar(ComposicionFormula e, boolean esNuevo)throws ExcepcionGeneralSistema {
        
        borrarItemNoValidos(e);
        
        if(esNuevo){           
           if(composicionFormulaDAO.getComposicionFormula(new ComposicionFormulaPK(e.getArtcod(), e.getCodfor()))!=null){
               throw new ExcepcionGeneralSistema("Ya existe una entidad con el código "+e.getArtcod()+"-"+e.getCodfor());
           }
           composicionFormulaDAO.crear(e);            
        }else{               
           e = (ComposicionFormula) composicionFormulaDAO.editar(e);
        }          
        
        return e;
    }
    
    private void borrarItemNoValidos(ComposicionFormula c){
        
        
//        if(c.getItemsComponentes()== null || c.getItemsComponentes().isEmpty()){
//            return;            
//        }
//        
//        String indiceBorrar[] = new String[c.getItemsComponentes().size()];
//        
//        //Seteamos los valores en -1
//        for(int i=0;i<indiceBorrar.length;i++){
//            indiceBorrar[i] = "N";
//        }
//              
//        for(int i = 0 ; i < c.getItemsComponentes().size(); i++ ){ 
//                
//            ComposicionFormulaItem ic = c.getItemsComponentes().get(i);
//            
//            if(ic.getProducto()==null){
//                indiceBorrar[i] = "S" ;                
//                continue;
//            }            
//        }
//        
//        for(int i=0;i<indiceBorrar.length;i++){
//            
//            if(indiceBorrar[i].equals("S")){
//                System.err.println("borra item");
//                ComposicionFormulaItem remove = c.getItemsComponentes().remove(i);                
//            }            
//        }    
    }

    public ComposicionFormula getComposicionFormula(ComposicionFormulaPK idPK){
        return composicionFormulaDAO.getComposicionFormula(idPK);
    }

    public ComposicionFormula getComprosicionFormula(String artcod,String formul){        

        ComposicionFormulaPK idPK = new ComposicionFormulaPK(artcod,formul);
        return composicionFormulaDAO.getComposicionFormula(idPK);
        
    }
    
    public void eliminar(ComposicionFormula composicionFormula) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<ComposicionFormula> getListaByBusqueda(String artcod, String codfor, String txtBusqueda, boolean mostrarDebaja, int cantidadRegistros) {
        return composicionFormulaDAO.getListaByBusqueda(artcod,codfor,txtBusqueda, mostrarDebaja, cantidadRegistros);
    }

    public void eliminarItemComponente(ItemComposicionFormulaComponente ci) throws Exception {
        
        if(ci==null){
            return;
        }
        
        if(ci.getArtcod()==null || ci.getArtcod().isEmpty()){
            return;
        }
        
        if(ci.getCodigo()==null || ci.getCodigo().isEmpty()){
            return;
        }
        
        ItemComposicionFormulaPK idPk = new ItemComposicionFormulaPK(ci.getArtcod(), ci.getCodigo(), ci.getNroitm());
        composicionFormulaDAO.eliminar(composicionFormulaDAO.getObjeto(ItemComposicionFormulaComponente.class, idPk));
        
    }
    
    public void eliminarItemProceso(ItemComposicionFormulaProceso ci) throws Exception {
        
        if(ci==null){
            return;
        }
        
        if(ci.getArtcod()==null || ci.getArtcod().isEmpty()){
            return;
        }
        
        if(ci.getCodigo()==null || ci.getCodigo().isEmpty()){
            return;
        }
        
        ItemComposicionFormulaPK idPk = new ItemComposicionFormulaPK(ci.getArtcod(), ci.getCodigo(), ci.getNroitm());
        composicionFormulaDAO.eliminar(composicionFormulaDAO.getObjeto(ItemComposicionFormulaProceso.class, idPk));
        
    }
 
}
