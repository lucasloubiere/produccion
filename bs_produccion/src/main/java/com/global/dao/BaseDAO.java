/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.global.dao;

/**
 *
 * @author ctrosch
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class BaseDAO implements Serializable {

    protected Logger log = Logger.getLogger(this.getClass().getName());

    protected EntityManager em;
    protected Map<String, String> filtro;

    public EntityManager getEm() {
        return this.em;
    }

    @PersistenceContext(unitName = "PU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void crear(Object objeto) {
        em.persist(objeto);
        em.flush();
    }

//    public void editar(Object objeto) {        
//        em.merge(objeto);
//        em.flush();
//    }
    public Object editar(Object objeto) {
        Object objetoNew = em.merge(objeto);
        em.flush();
        return objetoNew;
    }

    public void refresh(Object objeto) {

        em.refresh(objeto);

    }

    /**
     * Eliminar objeto con clave primaria Integer
     *
     * @param entityClass Clase del objeto a eliminar
     * @param id Valor de la clave primaria
     */
    public void eliminar(Class entityClass, Integer id) {

        em.remove(getEm().find(entityClass, id));
    }

    /**
     * Eliminar objeto pasando el objeto persistente
     *
     * @param objeto Objeto a eliminar
     *
     */
    public void eliminar(Object objeto) throws Exception {

        em.remove(objeto);
        em.flush();
    }

    /**
     * Eliminar objeto con clave primaria Short
     *
     * @param entityClass Clase del objeto a eliminar
     * @param id Valor de la clave primaria
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void eliminar(Class entityClass, Short id) {
        try {
            em.remove(getEm().find(entityClass, id));
        } catch (Exception e) {
            System.out.println("No se puede eliminar: " + entityClass.getName());
        }
    }

    /**
     * Eliminar objeto con clave primaria String
     *
     * @param entityClass Clase del objeto a eliminar
     * @param cod
     * @param id Valor de la clave primaria
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void eliminar(Class entityClass, String cod) {
        try {
            em.remove(getEm().find(entityClass, cod));
        } catch (Exception e) {
            System.out.println("No se puede eliminar: " + entityClass.getName());
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /**
     * Eliminar objeto con clave primaria object
     *
     * @param entityClass Clase del objeto a eliminar
     * @param id Valor de la clave primaria
     */
    public void eliminar(Class entityClass, Object id) throws Exception {
        try {
            em.remove(getEm().find(entityClass, id));
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("No se puede eliminar: " + entityClass.getName());

        }
    }

    public <T extends Object> T getObjeto(Class<T> entityClass, Integer id) {
        try {

            return (T) em.find(entityClass, id);
        } catch (NoResultException nre) {

            return null;

        } catch (Exception e) {
            System.out.println("No se puede obtener objeto: " + entityClass.getSimpleName() + " - " + e.getMessage());
            return null;
        }
    }

    public <T extends Object> T getObjeto(Class<T> entityClass, Object id) {
        try {
            return (T) em.find(entityClass, id);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("No se puede obtener objeto: " + entityClass.getSimpleName() + " - " + e.getMessage());
            return null;
        }
    }

    public <T extends Object> T getObjeto(Class<T> entityClass, String cod) {
        try {

            return (T) em.find(entityClass, cod);
        } catch (Exception e) {
            System.out.println("No se puede obtener objeto: " + entityClass.getSimpleName() + " - " + e.getMessage());
            return null;
        }
    }

    public <T extends Object> T getObjeto(Class<T> entityClass, BigDecimal cod) {
        try {
            return (T) em.find(entityClass, cod);
        } catch (Exception e) {
            System.out.println("No se puede obtener objeto: " + entityClass.getSimpleName() + " - " + e.getMessage());
            return null;
        }
    }

    public <T extends Object> T getObjeto(Class<T> entityClass, Short id) {
        try {
            return (T) em.find(entityClass, id);
        } catch (Exception e) {
            System.out.println("No se puede obtener objeto: " + entityClass.getSimpleName() + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Se obtiene una entidad de acuerdo a un tipo de valor específico para un
     * campo determinado
     *
     * @param <T>
     * @param entityClass: Clase de objeto a obtener
     * @param nombreCampo: Nombre del campo por el cual se buscarán
     * coincidencias
     * @param valorCampo: Valor del campo a obtener
     * @return un objeto del tipo entityClass
     */
    public <T extends Object> T getObjeto(Class<T> entityClass, String nombreCampo, String valorCampo) {

        try {
            return (T) em.createQuery("SELECT o FROM " + entityClass.getSimpleName() + " o WHERE o." + nombreCampo + " ='" + valorCampo + "'").getSingleResult();
        } catch (Exception e) {
            System.err.println("No se puede obtener objeto " + entityClass.getSimpleName() + " - " + e.getMessage());
            System.err.println("Verificar parametros - Clase - nombre del campo - valor buscado");
            e.printStackTrace();
            return null;
        }
    }

    public <T extends Object> T getObjeto(Class<T> entityClass, Map<String, String> filtro) {

        try {
            String sQuery = "SELECT o FROM " + entityClass.getSimpleName() + " o ";

            //Si el filtro no está vacio lo aplicamos
            if (filtro.isEmpty()) {

                sQuery = sQuery + "WHERE ";

                Iterator it = filtro.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry ent = (Map.Entry) it.next();
                    sQuery = sQuery + " o." + ent.getKey() + " ='" + ent.getValue() + "' ";
                }
            }

            return (T) em.createQuery(sQuery).getSingleResult();

        } catch (Exception e) {
            System.err.println("No se puede obtener objeto " + entityClass.getSimpleName() + " - " + e.getCause());
            System.err.println("Verificar parametros - Clase - nombre del campo - valor buscado");
            return null;
        }
    }

    /**
     * Consulta todos los registro de una entidad particular
     *
     * @param <T>
     * @param entityClass: Clase de la entidad a consultar
     * @param all: indica si se devuelven todos los registro
     * @param maxResults: cantidad de registros a devolver
     * @param maxResults: posición en la lista a partir del cual comienza a
     * devolver datos
     * @return lista de Entidad del tipo entityClass
     */
    public List getLista(Class entityClass, boolean all, int maxResults, int firstResult) {
        try {
            Query q = (Query) getEm().createQuery("SELECT o FROM " + entityClass.getSimpleName() + " o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } catch (Exception e) {

            log.log(Level.SEVERE, "getLista", "No se puede obtener la lista de " + entityClass.getName());
            log.log(Level.SEVERE, "getLista", e);
            return null;
        }
    }

    protected List getLista(Class entityClass, boolean all, int maxResults, int firstResult, String campoOrdena) {
        try {
            Query q = (Query) getEm().createQuery("SELECT o FROM " + entityClass.getSimpleName() + " o ORDER BY o." + campoOrdena);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } catch (Exception e) {

            log.log(Level.SEVERE, "getLista", "No se puede obtener la lista de " + entityClass.getName());
            log.log(Level.SEVERE, "getLista", e);
            return null;
        }
    }

    /**
     * Consulta todos los registro de una entidad particular de acuerdo a un
     * filtro determinado
     *
     * @param <T>
     * @param entityClass: Clase de la entidad a consultar
     * @param all: indica si se devuelven todos los registro
     * @param maxResults: cantidad de registros a devolver
     * @param maxResults: posición en la lista a partir del cual comienza a
     * devolver datos
     * @param filtro aplicado a la consulta
     * @return lista de Entidad del tipo entityClass
     */
    /**
     * Consulta todos los registro de una entidad particular de acuerdo a un
     * filtro determinado
     *
     * @param <T>
     * @param entityClass : Clase de la entidad a consultar
     * @param all : indica si se devuelven todos los registro
     * @param maxResults : cantidad de registros a devolver
     * @param firstResult
     * @param filtro aplicado a la consulta
     * @return lista de Entidad del tipo entityClass
     */
    public <T extends Object> T getLista(Class entityClass, boolean all, int maxResults, int firstResult, Map<String, String> filtro) {

        try {
            String sQuery = "SELECT o FROM " + entityClass.getSimpleName() + " o "
                    + generarStringFiltro(filtro, "o", true);

            Query q = em.createQuery(sQuery);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }

            return (T) q.getSingleResult();

        } catch (Exception e) {

            log.log(Level.SEVERE, "getLista", "No se puede obtener entidad " + entityClass.getSimpleName());
            log.log(Level.SEVERE, "getLista", e);
            return null;
        }
    }

    /**
     * Consulta todos los registro de una entidad particular de acuerdo a un
     * filtro determinado
     *
     * @param <T>
     * @param entityClass: Clase de la entidad a consultar
     * @param all: indica si se devuelven todos los registro
     * @param maxResults: cantidad de registros a devolver
     * @param firstResults: posición en la lista a partir del cual comienza a
     * devolver datos
     * @param campoOrdena : nombre del campo por el cual queremos ordenar los
     * datos
     * @param ordena: indica si se ordena o no
     * @param filtro aplicado a la consulta
     * @return lista de Entidad del tipo entityClass
     */
    public List getLista(Class entityClass, boolean all, int maxResults, int firstResult,
            String campoOrdena, boolean ordena,
            Map<String, String> filtro) {

        try {
            String sQuery = "SELECT o FROM " + entityClass.getSimpleName() + " o ";
            sQuery.concat(generarStringFiltro(filtro, "o", true));

            if (ordena) {
                sQuery.concat(" ORDER BY " + campoOrdena);
            }

            Query q = em.createQuery(sQuery);

            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }

            return q.getResultList();

        } catch (Exception e) {
            System.err.println("No se puede obtener objeto " + entityClass.getSimpleName() + " - " + e.getCause());
            System.err.println("Verificar parametros - Clase - nombre del campo - valor buscado");
            return null;
        }
    }

    /**
     * Consulta en la cual se devuelve un solo objeto
     *
     * @param <T>
     * @param entityClass: Clase de la entidad devuelta
     * @param sQuery: consulta a ejecutar
     * @return Entidad del tipo entityClass
     */
    public <T extends Object> T queryObject(Class<T> entityClass, String sQuery) {
        try {
            return (T) em.createQuery(sQuery).getSingleResult();
        } catch (Exception e) {
            System.err.print("No se ejecuto queryObject: " + sQuery + e.getMessage());
            return null;
        }
    }

    /**
     * Consulta en la cual se devuelven mas de un objeto
     *
     * @param entityClass : Clase de la entidad contenida en la lista devuelta
     * @param sQuery : Consulta a ejecutar
     * @return lista de Entidad del tipo entityClass
     */
    public List queryList(Class entityClass, String sQuery) {
        try {
            return em.createQuery(sQuery).getResultList();
        } catch (Exception e) {
            System.err.print("No se ejecuto queryList: " + sQuery);
            System.err.print("ERROR: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Consulta de actualización * @param sQuery : Consulta a ejecutar
     * @return void
     */
    public void queryUpdate(String query) {
        try {
            getEm().createQuery(query).executeUpdate();
        } catch (Exception e) {
            System.err.print(e.getMessage() + "No ejecutó consulta: " + query);
        }
    }

    /**
     * Consulta la cantidad de registros de una entidad
     *
     * @param entityClass : Tipo de objetos contenidos en la lista devuelta
     *
     * @return Cantidad de registros
     */
    public long getCantidadRegistros(Class entityClass) {
        return (Long) em.createQuery("SELECT COUNT(o) FROM " + entityClass.getSimpleName() + " o").getSingleResult();
    }

    /**
     * Genera un string con la estructura JPQL para utilizarlo en una consulta
     *
     * @param filtro a aplicar a la consulta
     * @return
     */
    /**
     * Genera un string con la estructura JPQL para utilizarlo en una consulta
     *
     * @param filtro a aplicar a la consulta
     * @return
     */
    protected String generarStringFiltro(Map<String, String> filtro, String aliasEntidad, boolean cWhere) {

        if (filtro == null) {
            return "";
        }

        String sFiltro = "";
        //Si el filtro no está vacio lo aplicamos
        if (!filtro.isEmpty()) {

            Iterator it = filtro.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry ent = (Map.Entry) it.next();

                if (cWhere) {
                    sFiltro += " WHERE ( " + aliasEntidad + "." + ent.getKey() + ent.getValue() + ") ";
                    cWhere = false;
                } else {
                    sFiltro += " AND ( " + aliasEntidad + "." + ent.getKey() + ent.getValue() + ") ";
                }
            }
        }

        return sFiltro;

        /**
         * //Si el filtro no está vacio lo aplicamos if (!filtro.isEmpty()){
         *
         * //Agrego WHERE al string? if(cWhere) sFiltro = sFiltro + " WHERE ";
         * //Si no agrego el WHERE, tengo que agregar el AND al principio //Ya
         * que viene de otra consulta con WHERE else cAND = true; Iterator it =
         * filtro.entrySet().iterator(); while(it.hasNext()){
         *
         * Map.Entry ent = (Map.Entry)it.next();
         *
         * if (cAND) sFiltro = sFiltro + " AND " ; sFiltro = sFiltro + "( o."+
         * ent.getKey() +" LIKE '%" + ent.getValue() + "%' )"; cAND = true; } }
         */
    }

    protected void sincronizacionTemporal(String vista) {

        //Solución que encontré para que me sincronice los datos
        String nQuery = "select * from " + vista + " limit 1 ";
        List<Object[]> resultado = new ArrayList<Object[]>();
        resultado = em.createNativeQuery(nQuery).getResultList();
    }

    public Map<String, String> getFiltro() {
        return new LinkedHashMap<String, String>();
    }

}
