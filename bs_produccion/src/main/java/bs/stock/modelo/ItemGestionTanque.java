/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.modelo;

import bs.global.modelo.Auditoria;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 *
 * @author Claudio
 */
public class ItemGestionTanque {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "idcab", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MovimientoStock movimiento;
        
    @JoinColumn(name = "deposi", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Deposito deposito;
    
    @JoinColumn(name = "artcod", referencedColumnName = "codigo")
    @ManyToOne
    private Producto producto;
    
    @Column(name = "stock_inicial")
    private BigDecimal stockInicial;
    @Column(name = "ingresos")
    private BigDecimal ingresos;
    @Column(name = "egresos")
    private BigDecimal egresos;
    @Column(name = "stock_final")
    private BigDecimal stockFinal;
    @Column(name = "stock_calculado")
    private BigDecimal stockCalculado;
    
    @JoinColumn(name = "unimed", referencedColumnName = "codigo", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadMedida unidadMedida;


    //@Size(min = 1, max = 50)
    @Column(name = "natri1")
    private String atributo1;
    
    //@Size(min = 1, max = 50)
    @Column(name = "natri2")
    private String atributo2;
    
    //@Size(min = 1, max = 50)
    @Column(name = "natri3")
    private String atributo3;
    
//    @Size(min = 1, max = 50)
    @Column(name = "natri4")
    private String atributo4;
    
    //@Size(min = 1, max = 50)
    @Column(name = "natri5")
    private String atributo5;
    
    //@Size(min = 1, max = 50)
    @Column(name = "natri6")
    private String atributo6;
    
    //@Size(min = 1, max = 50)
    @Column(name = "natri7")
    private String atributo7;

    @Embedded
    private Auditoria auditoria;
        
    @Transient
    private boolean todoOk;
    
    public ItemGestionTanque() {

        this.stockFinal     = BigDecimal.ZERO;
        this.ingresos       = BigDecimal.ZERO;
        this.egresos        = BigDecimal.ZERO;
        this.stockFinal     = BigDecimal.ZERO;
        this.stockCalculado = BigDecimal.ZERO;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public BigDecimal getStockInicial() {
        return stockInicial;
    }

    public void setStockInicial(BigDecimal stockInicial) {
        this.stockInicial = stockInicial;
    }

    public BigDecimal getIngresos() {
        return ingresos;
    }

    public void setIngresos(BigDecimal ingresos) {
        this.ingresos = ingresos;
    }

    public BigDecimal getEgresos() {
        return egresos;
    }

    public void setEgresos(BigDecimal egresos) {
        this.egresos = egresos;
    }

    public BigDecimal getStockFinal() {
        return stockFinal;
    }

    public void setStockFinal(BigDecimal stockFinal) {
        this.stockFinal = stockFinal;
    }

    public BigDecimal getStockCalculado() {
        return stockCalculado;
    }

    public void setStockCalculado(BigDecimal stockCalculado) {
        this.stockCalculado = stockCalculado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MovimientoStock getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoStock movimiento) {
        this.movimiento = movimiento;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getAtributo1() {
        return atributo1;
    }

    public void setAtributo1(String atributo1) {
        this.atributo1 = atributo1;
    }

    public String getAtributo2() {
        return atributo2;
    }

    public void setAtributo2(String atributo2) {
        this.atributo2 = atributo2;
    }

    public String getAtributo3() {
        return atributo3;
    }

    public void setAtributo3(String atributo3) {
        this.atributo3 = atributo3;
    }

    public String getAtributo4() {
        return atributo4;
    }

    public void setAtributo4(String atributo4) {
        this.atributo4 = atributo4;
    }

    public String getAtributo5() {
        return atributo5;
    }

    public void setAtributo5(String atributo5) {
        this.atributo5 = atributo5;
    }

    public String getAtributo6() {
        return atributo6;
    }

    public void setAtributo6(String atributo6) {
        this.atributo6 = atributo6;
    }

    public String getAtributo7() {
        return atributo7;
    }

    public void setAtributo7(String atributo7) {
        this.atributo7 = atributo7;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    public boolean isTodoOk() {
        return todoOk;
    }

    public void setTodoOk(boolean todoOk) {
        this.todoOk = todoOk;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemGestionTanque other = (ItemGestionTanque) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ItemGestionTanque{" + "id=" + id + '}';
    }
    
}
