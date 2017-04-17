/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.rn;

import com.global.excepciones.ExcepcionGeneralSistema;
import com.stock.dao.ProductoDAO;
import com.stock.modelo.Producto;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author lloubiere
 */
@Stateless
@Path("/producto/")
public class ProductoRN implements Serializable {

    @EJB private ProductoDAO productoDAO;
    Map<String,String> filtro = new HashMap<String,String>();

    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)         
    public void guardar(Producto p, boolean esNuevo) throws Exception {
        
        if(esNuevo){            
            if(productoDAO.getObjeto(Producto.class, p.getCodigo() )!=null){
               throw new ExcepcionGeneralSistema("Ya existe un producto con el código "+ p.getCodigo());                
            }
            
            productoDAO.crear(p);
        }else{
            productoDAO.editar(p);
        }        
    }
    
    public void eliminar(Producto p){        
        productoDAO.eliminar(Producto.class, p.getCodigo());        
    }
    
    public Producto getProducto(String codigo){
    
        return productoDAO.getProducto(codigo);        
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    @Path("lista")
    public List<Producto> getListaByBusqueda(){
        return productoDAO.getListaByBusqueda(null, "", "", false, 15);
    }
    
    public List<Producto> getListaByBusqueda(String codTipo , String txtBusqueda, boolean mostrarDebaja){
        
        return productoDAO.getListaByBusqueda(null, codTipo, txtBusqueda, mostrarDebaja, 15);
    }
    
    public List<Producto> getListaByBusqueda(String codTipo , String txtBusqueda, boolean mostrarDebaja,int cantMaxima){
        
        return productoDAO.getListaByBusqueda(null, codTipo, txtBusqueda, mostrarDebaja, cantMaxima);
    }
    
    public List<Producto> getListaByBusqueda(Map<String, String> filtro, String codTipo, String txtBusqueda, boolean mostrarDebaja){
        
        return productoDAO.getListaByBusqueda(filtro, codTipo, txtBusqueda, mostrarDebaja, 15);        
    }

    public List<Producto> getListaByBusqueda(Map<String, String> filtro,String codTipo, String txtBusqueda, boolean mostrarDebaja, int cantMaxima){
        
        return productoDAO.getListaByBusqueda(filtro,codTipo, txtBusqueda, mostrarDebaja, cantMaxima);        
    }
    
    public String getProximoCodigo(String tippro, String rub01,String rub02){
        
        int nroProducto = productoDAO.getProximoCodigoProducto(tippro, rub01, rub02);
        
        String codigo = "0000"+ String.valueOf(nroProducto);
        
        return tippro+rub01+codigo.substring(codigo.length()-4, codigo.length());
        
    }

    public int getCantidadRegistros() {
        
        return productoDAO.getCantidadRegistros();
    }
}
