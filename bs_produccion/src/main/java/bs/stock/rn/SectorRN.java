/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.rn;

import bs.global.excepciones.ExcepcionGeneralSistema;
import bs.stock.dao.SectorDAO;
import bs.stock.modelo.Sector;
import java.io.Serializable;
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
public class SectorRN implements Serializable {

   @EJB private SectorDAO sectorDAO;

   @TransactionAttribute(TransactionAttributeType.REQUIRED)     
   public void guardar(Sector sector, boolean esNuevo) throws Exception{
       
       if (esNuevo){
           
           if(sectorDAO.getObjeto(Sector.class, sector.getCodigo())!=null){
               throw new ExcepcionGeneralSistema("Ya existe sector con el código"+ sector.getCodigo());
           }
           sectorDAO.crear(sector);
       }else{
           sectorDAO.editar(sector);
       }     
    }
   
   public void eliminar(Sector sector) throws Exception {
        
        sectorDAO.eliminar(sector);
        
    }
   
   
    public Sector getSector(String cod){
        return sectorDAO.getSector(cod);
    }
 
    public List<Sector> getListaByBusqueda(String txtBusqueda, boolean mostrarDebaja, int cantidadRegistros) {
        
        return sectorDAO.getListaByBusqueda(null,txtBusqueda, mostrarDebaja, cantidadRegistros);
    }    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
