/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.produccion.modelo;

import bs.global.modelo.Auditoria;
import bs.stock.modelo.Deposito;
import bs.stock.modelo.Producto;
import bs.stock.modelo.UnidadMedida;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author "Claudio Trosch"
 */
public interface IItemMovimientoProduccion {

    String getActualizaStock();

    String getAtributo1();

    String getAtributo2();

    String getAtributo3();

    String getAtributo4();

    String getAtributo5();

    String getAtributo6();

    String getAtributo7();

    Auditoria getAuditoria();

    BigDecimal getCantidad();

    BigDecimal getCantidadNominal();

    BigDecimal getCantidadOriginal();

    BigDecimal getCantidadPendiente();

    BigDecimal getCantidadStock();

    Deposito getDeposito();

    Date getFechaEntrega();

    Date getFechaHasta();

    String getGrupo();

    Integer getId();

    Integer getIdItemOriginal();

    MovimientoProduccion getMovimiento();

    MovimientoProduccion getMovimientoOriginal();

    int getNroitm();

    String getObservaciones();

    Operario getOperario();

    BigDecimal getPendiente();

    BigDecimal getPrecio();

    BigDecimal getProduccion();

    Producto getProducto();

    Producto getProductoOriginal();

    Producto getProductoSustituto();

    UnidadMedida getUnidadMedida();

    boolean isConError();

    boolean isTodoOk();

    void setActualizaStock(String actualizaStock);

    void setAtributo1(String atributo1);

    void setAtributo2(String atributo2);

    void setAtributo3(String atributo3);

    void setAtributo4(String atributo4);

    void setAtributo5(String atributo5);

    void setAtributo6(String atributo6);

    void setAtributo7(String atributo7);

    void setAuditoria(Auditoria auditoria);

    void setCantidad(BigDecimal cantidad);

    void setCantidadNominal(BigDecimal cantidadNominal);

    void setCantidadOriginal(BigDecimal cantidadOriginal);

    void setCantidadPendiente(BigDecimal cantidadPendiente);

    void setCantidadStock(BigDecimal cantidadStock);

    void setConError(boolean conError);

    void setDeposito(Deposito deposito);

    void setFechaEntrega(Date fechaEntrega);

    void setFechaHasta(Date fechaHasta);

    void setGrupo(String grupo);

    void setId(Integer id);

    void setIdItemOriginal(Integer idItemOriginal);

    void setMovimiento(MovimientoProduccion movimiento);

    void setMovimientoOriginal(MovimientoProduccion movimientoOriginal);

    void setNroitm(int nroitm);

    void setObservaciones(String observaciones);

    void setOperario(Operario operario);

    void setPendiente(BigDecimal pendiente);

    void setPrecio(BigDecimal precio);

    void setProduccion(BigDecimal produccion);

    void setProducto(Producto producto);

    void setProductoOriginal(Producto productoOriginal);

    void setProductoSustituto(Producto productoSustituto);

    void setTodoOk(boolean todoOk);

    void setUnidadMedida(UnidadMedida unidadMedida);
    
}
