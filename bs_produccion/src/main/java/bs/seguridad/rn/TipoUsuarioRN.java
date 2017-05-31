/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.seguridad.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.seguridad.dao.TipoUsuarioDAO;
import bs.seguridad.modelo.TipoUsuario;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author lloubiere
 */
@Stateless
public class TipoUsuarioRN {
    @EJB private TipoUsuarioDAO tipoUsuarioDAO;
    
    public void guardar(TipoUsuario tipo, boolean esNuevo) throws ExcepcionGeneralSistema {
        
        if(esNuevo){     
            if(tipoUsuarioDAO.getObjeto(TipoUsuario.class, tipo.getId())!=null){
                throw new ExcepcionGeneralSistema("El usuario "+tipo.getId()+" ya existe");
            }            
            tipoUsuarioDAO.crear(tipo);            
        }else{
            tipoUsuarioDAO.editar(tipo);
        }        
    }    
    
    
    public List<TipoUsuario>  getListaByBusqueda(String txtBusqueda, boolean muestraDeBaja, int cant){
        
        return tipoUsuarioDAO.getListaByBusqueda(txtBusqueda,muestraDeBaja,cant);
    }
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void eliminar(TipoUsuario tipoUsuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public TipoUsuario getTipo(Integer idTipo) {
        return tipoUsuarioDAO.getObjeto(TipoUsuario.class, idTipo);
    }
}
