/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.stock.dao.TipoProductoDAO;
import bs.stock.modelo.TipoProducto;
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
public class TipoProductoRN {

    @EJB
    private TipoProductoDAO tipoProductoDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    
    
     public void guardar(TipoProducto tipo, boolean esNuevo) throws ExcepcionGeneralSistema {
        
        if(esNuevo){     
            if(tipoProductoDAO.getObjeto(TipoProducto.class, tipo.getCodigo())!=null){
                throw new ExcepcionGeneralSistema("El usuario "+tipo.getCodigo()+" ya existe");
            }            
            tipoProductoDAO.crear(tipo);            
        }else{
            tipoProductoDAO.editar(tipo);
        }        
    }    

//    public void guardar(TipoProducto tipoProducto, boolean esNuevo) throws Exception {
//
//        if (esNuevo) {
//            if (tipoProductoDAO.getObjeto(UnidadDeMedida.class, tipoProducto.getCodigo()) != null) {
//                throw new ExcepcionGeneralSistema("Ya existe unidad de medida con el código" + tipoProducto.getCodigo());
//            }
//            tipoProductoDAO.crear(tipoProducto);
//        } else {
//            tipoProductoDAO.editar(tipoProducto);
//        }
//    }

    public TipoProducto getTipoProducto(String codigo) {

        return tipoProductoDAO.getObjeto(TipoProducto.class, codigo);
    }

    public void eliminar(TipoProducto tipoProducto) throws Exception {

        tipoProductoDAO.eliminar(tipoProducto);

    }

    public List<TipoProducto> getLista(boolean mostrarDebaja) {

        return tipoProductoDAO.getListaByBusqueda("", mostrarDebaja, 0);
    }

    public List<TipoProducto> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja) {

        return tipoProductoDAO.getListaByBusqueda(txtBusqueda, mostrarDebaja, 15);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
