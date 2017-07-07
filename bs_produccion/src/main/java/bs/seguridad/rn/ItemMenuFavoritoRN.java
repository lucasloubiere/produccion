/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.seguridad.rn;

import bs.seguridad.dao.ItemMenuFavoritoDAO;
import bs.seguridad.modelo.ItemMenuFavorito;
import bs.seguridad.modelo.ItemMenuFavoritoPK;
import bs.seguridad.modelo.Usuario;
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
public class ItemMenuFavoritoRN{

    @EJB ItemMenuFavoritoDAO itemMenuFavoritoDAO;
    
    /**
     * No editar, solo agrega si no existe.
     * @param ir
     * @throws Exception 
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)         
    public void guardar(ItemMenuFavorito ir) throws Exception {
        
        ItemMenuFavoritoPK idPK = new ItemMenuFavoritoPK(ir.getIdUsuario(), ir.getCodMenu());
        
        if(itemMenuFavoritoDAO.getObjeto(ItemMenuFavorito.class, idPK )==null){
            itemMenuFavoritoDAO.crear(ir);
        }
    }
    
    public ItemMenuFavorito getItemMenuFavorito(int id){
        return itemMenuFavoritoDAO.getObjeto(ItemMenuFavorito.class, id);
    }
        
    public List<ItemMenuFavorito> getItemMenuFavoritoByUsuario(Usuario usuario) {

          return itemMenuFavoritoDAO.getItemMenuFavoritoByUsuario(usuario);
    }
    
}
