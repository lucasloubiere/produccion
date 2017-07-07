/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.stock.modelo;

import bs.global.modelo.Auditoria;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Claudio
 */
@Entity
@Table(name = "st_parametro")
public class ParametroStock implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column(name = "ID", nullable = false)
    private Integer id;

    @JoinColumn(name = "TIPPRO", referencedColumnName = "TIPPRO")
    @ManyToOne(optional=false,fetch=FetchType.LAZY)
    private TipoProducto tipoProducto;
    
    //Unidad de medida
    @JoinColumn(name = "UNIMED", referencedColumnName = "CODIGO")
    @ManyToOne(fetch=FetchType.LAZY)
    private UnidadMedida unidadDeMedida;
    
    @Column(name = "STOCKS", length = 1)
    private String gestionaStock;
        
    @Column(name = "IMPCNT", length = 1)
    private String imprimeCantidad; 

    @Column(name = "CUENVT", length = 40)
    private String cuentaContableVenta;
        
    @Column(name = "CUENPV", length = 40)
    private String cuentaContableCompra;
    
    @Column(name = "ADATR1", length = 1)
    private String administraAtributo1;      
    @Column(name = "ADATR2", length = 1)
    private String administraAtributo2;    
    @Column(name = "ADATR3", length = 1)
    private String administraAtributo3;    
    @Column(name = "ADATR4", length = 1)
    private String administraAtributo4;    
    @Column(name = "ADATR5", length = 1)
    private String administraAtributo5;    
    @Column(name = "ADATR6", length = 1)
    private String administraAtributo6;    
    @Column(name = "ADATR7", length = 1)
    private String administraAtributo7;
    
    
    @Column(name = "DESAT1", length = 80)
    private String descripcionAtributo1;      
    @Column(name = "DESAT2", length = 80)
    private String descripcionAtributo2;    
    @Column(name = "DESAT3", length = 80)
    private String descripcionAtributo3;    
    @Column(name = "DESAT4", length = 80)
    private String descripcionAtributo4;    
    @Column(name = "DESAT5", length = 80)
    private String descripcionAtributo5;    
    @Column(name = "DESAT6", length = 80)
    private String descripcionAtributo6;    
    @Column(name = "DESAT7", length = 80)
    private String descripcionAtributo7;
    
    
    @Column(name = "DESAP1", length = 80)
    private String descripcionAplicacion1;      
    @Column(name = "DESAP2", length = 80)
    private String descripcionAplicacion2;    
    @Column(name = "DESAP3", length = 80)
    private String descripcionAplicacion3;    
    @Column(name = "DESAP4", length = 80)
    private String descripcionAplicacion4;    
    @Column(name = "DESAP5", length = 80)
    private String descripcionAplicacion5;    
    @Column(name = "DESAP6", length = 80)
    private String descripcionAplicacion6;    
    @Column(name = "DESAP7", length = 80)
    private String descripcionAplicacion7;
    @Column(name = "DESAP8", length = 80)
    private String descripcionAplicacion8;    
    @Column(name = "DESAP9", length = 80)
    private String descripcionAplicacion9;
    
    @Column(name = "DIAENT")
    private Short diasEntrega;        
    @Column(name = "KITSFC", length = 1)
    private String esKitVenta;  
    @Column(name = "IMGCHI", length = 40)
    private String imagenChica;    
    @Column(name = "IMGGRA", length = 40)
    private String imagenGrande;        
    @Column(name = "PNUEVO", length=1)
    private String productoNuevo;
    @Column(name = "PTOPED", precision = 15, scale = 4)
    private BigDecimal puntoDePedido;    
    @Column(name = "STKMIN", precision = 15, scale = 4)
    private BigDecimal stockMinimo;
    @Column(name = "STKMAX", precision = 15, scale = 4)
    private BigDecimal stockMaximo;
    @JoinColumn(name = "UNIPES", referencedColumnName = "CODIGO")
    @ManyToOne(fetch=FetchType.LAZY)
    private UnidadMedida unidadDePeso;        
        
    @JoinColumns({
        @JoinColumn(name = "RUBR01", referencedColumnName = "CODIGO", nullable = false, insertable=true, updatable=true),
        @JoinColumn(name = "TIPPRO", referencedColumnName = "TIPPRO", nullable = false, insertable=false, updatable=false)    
    })
    @ManyToOne(fetch = FetchType.LAZY)    
    private Rubro01 rubr01;    
    
    @JoinColumns({
        @JoinColumn(name = "RUBR02", referencedColumnName = "CODIGO", nullable = false, insertable=true,  updatable=true),
        @JoinColumn(name = "TIPPRO", referencedColumnName = "TIPPRO", nullable = false, insertable=false, updatable=false)    
    })
    @ManyToOne(fetch = FetchType.LAZY)    
    private Rubro02 rubr02;    
    
    @JoinColumns({
        @JoinColumn(name = "RUBR03", referencedColumnName = "CODIGO", nullable = false, insertable=true, updatable=true),
        @JoinColumn(name = "TIPPRO", referencedColumnName = "TIPPRO", nullable = false, insertable=false, updatable=false)    
    })
    @ManyToOne(fetch = FetchType.LAZY)    
    private Rubro03 rubr03;    
        
    
    @Embedded
    private Auditoria auditoria;


    public ParametroStock(){
        
        this.auditoria = new Auditoria();        
        
        this.stockMinimo = BigDecimal.ZERO;
        this.stockMaximo = BigDecimal.ZERO;
        this.puntoDePedido = BigDecimal.ZERO;
        this.diasEntrega = 0;
        
        administraAtributo1 = "N";
        administraAtributo2= "N";
        administraAtributo3= "N";
        administraAtributo4= "N";
        administraAtributo5= "N";
        administraAtributo6= "N";
        administraAtributo7= "N";             
        imprimeCantidad = "S";
                
        this.esKitVenta = "N";        
    }
  
    public ParametroStock(String artcod) {
                
        this.auditoria = new Auditoria();
        
        this.stockMinimo = BigDecimal.ZERO;
        this.stockMaximo = BigDecimal.ZERO;
        this.puntoDePedido = BigDecimal.ZERO;
        this.diasEntrega = 0;
        
        administraAtributo1 = "N";
        administraAtributo2= "N";
        administraAtributo3= "N";
        administraAtributo4= "N";
        administraAtributo5= "N";
        administraAtributo6= "N";
        administraAtributo7= "N";   
        imprimeCantidad = "S";
                
        this.esKitVenta = "N";
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public UnidadMedida getUnidadDePeso() {
        return unidadDePeso;
    }

    public void setUnidadDePeso(UnidadMedida unidadDePeso) {
        this.unidadDePeso = unidadDePeso;
    }

    public UnidadMedida getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(UnidadMedida unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(BigDecimal stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public BigDecimal getPuntoDePedido() {
        return puntoDePedido;
    }

    public void setPuntoDePedido(BigDecimal puntoDePedido) {
        this.puntoDePedido = puntoDePedido;
    }

    public Short getDiasEntrega() {
        return diasEntrega;
    }

    public void setDiasEntrega(Short diasEntrega) {
        this.diasEntrega = diasEntrega;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGestionaStock() {
        return gestionaStock;
    }

    public void setGestionaStock(String gestionaStock) {
        this.gestionaStock = gestionaStock;
    }

    public String getImprimeCantidad() {
        return imprimeCantidad;
    }

    public void setImprimeCantidad(String imprimeCantidad) {
        this.imprimeCantidad = imprimeCantidad;
    }

    public String getCuentaContableVenta() {
        return cuentaContableVenta;
    }

    public void setCuentaContableVenta(String cuentaContableVenta) {
        this.cuentaContableVenta = cuentaContableVenta;
    }

    public String getCuentaContableCompra() {
        return cuentaContableCompra;
    }

    public void setCuentaContableCompra(String cuentaContableCompra) {
        this.cuentaContableCompra = cuentaContableCompra;
    }

    public String getAdministraAtributo1() {
        return administraAtributo1;
    }

    public void setAdministraAtributo1(String administraAtributo1) {
        this.administraAtributo1 = administraAtributo1;
    }

    public String getAdministraAtributo2() {
        return administraAtributo2;
    }

    public void setAdministraAtributo2(String administraAtributo2) {
        this.administraAtributo2 = administraAtributo2;
    }

    public String getAdministraAtributo3() {
        return administraAtributo3;
    }

    public void setAdministraAtributo3(String administraAtributo3) {
        this.administraAtributo3 = administraAtributo3;
    }

    public String getAdministraAtributo4() {
        return administraAtributo4;
    }

    public void setAdministraAtributo4(String administraAtributo4) {
        this.administraAtributo4 = administraAtributo4;
    }

    public String getAdministraAtributo5() {
        return administraAtributo5;
    }

    public void setAdministraAtributo5(String administraAtributo5) {
        this.administraAtributo5 = administraAtributo5;
    }

    public String getAdministraAtributo6() {
        return administraAtributo6;
    }

    public void setAdministraAtributo6(String administraAtributo6) {
        this.administraAtributo6 = administraAtributo6;
    }

    public String getAdministraAtributo7() {
        return administraAtributo7;
    }

    public void setAdministraAtributo7(String administraAtributo7) {
        this.administraAtributo7 = administraAtributo7;
    }

    public String getDescripcionAtributo1() {
        return descripcionAtributo1;
    }

    public void setDescripcionAtributo1(String descripcionAtributo1) {
        this.descripcionAtributo1 = descripcionAtributo1;
    }

    public String getDescripcionAtributo2() {
        return descripcionAtributo2;
    }

    public void setDescripcionAtributo2(String descripcionAtributo2) {
        this.descripcionAtributo2 = descripcionAtributo2;
    }

    public String getDescripcionAtributo3() {
        return descripcionAtributo3;
    }

    public void setDescripcionAtributo3(String descripcionAtributo3) {
        this.descripcionAtributo3 = descripcionAtributo3;
    }

    public String getDescripcionAtributo4() {
        return descripcionAtributo4;
    }

    public void setDescripcionAtributo4(String descripcionAtributo4) {
        this.descripcionAtributo4 = descripcionAtributo4;
    }

    public String getDescripcionAtributo5() {
        return descripcionAtributo5;
    }

    public void setDescripcionAtributo5(String descripcionAtributo5) {
        this.descripcionAtributo5 = descripcionAtributo5;
    }

    public String getDescripcionAtributo6() {
        return descripcionAtributo6;
    }

    public void setDescripcionAtributo6(String descripcionAtributo6) {
        this.descripcionAtributo6 = descripcionAtributo6;
    }

    public String getDescripcionAtributo7() {
        return descripcionAtributo7;
    }

    public void setDescripcionAtributo7(String descripcionAtributo7) {
        this.descripcionAtributo7 = descripcionAtributo7;
    }

    public String getDescripcionAplicacion1() {
        return descripcionAplicacion1;
    }

    public void setDescripcionAplicacion1(String descripcionAplicacion1) {
        this.descripcionAplicacion1 = descripcionAplicacion1;
    }

    public String getDescripcionAplicacion2() {
        return descripcionAplicacion2;
    }

    public void setDescripcionAplicacion2(String descripcionAplicacion2) {
        this.descripcionAplicacion2 = descripcionAplicacion2;
    }

    public String getDescripcionAplicacion3() {
        return descripcionAplicacion3;
    }

    public void setDescripcionAplicacion3(String descripcionAplicacion3) {
        this.descripcionAplicacion3 = descripcionAplicacion3;
    }

    public String getDescripcionAplicacion4() {
        return descripcionAplicacion4;
    }

    public void setDescripcionAplicacion4(String descripcionAplicacion4) {
        this.descripcionAplicacion4 = descripcionAplicacion4;
    }

    public String getDescripcionAplicacion5() {
        return descripcionAplicacion5;
    }

    public void setDescripcionAplicacion5(String descripcionAplicacion5) {
        this.descripcionAplicacion5 = descripcionAplicacion5;
    }

    public String getDescripcionAplicacion6() {
        return descripcionAplicacion6;
    }

    public void setDescripcionAplicacion6(String descripcionAplicacion6) {
        this.descripcionAplicacion6 = descripcionAplicacion6;
    }

    public String getDescripcionAplicacion7() {
        return descripcionAplicacion7;
    }

    public void setDescripcionAplicacion7(String descripcionAplicacion7) {
        this.descripcionAplicacion7 = descripcionAplicacion7;
    }

    public String getDescripcionAplicacion8() {
        return descripcionAplicacion8;
    }

    public void setDescripcionAplicacion8(String descripcionAplicacion8) {
        this.descripcionAplicacion8 = descripcionAplicacion8;
    }

    public String getDescripcionAplicacion9() {
        return descripcionAplicacion9;
    }

    public void setDescripcionAplicacion9(String descripcionAplicacion9) {
        this.descripcionAplicacion9 = descripcionAplicacion9;
    }

    public String getEsKitVenta() {
        return esKitVenta;
    }

    public void setEsKitVenta(String esKitVenta) {
        this.esKitVenta = esKitVenta;
    }

    public String getImagenChica() {
        return imagenChica;
    }

    public void setImagenChica(String imagenChica) {
        this.imagenChica = imagenChica;
    }

    public String getImagenGrande() {
        return imagenGrande;
    }

    public void setImagenGrande(String imagenGrande) {
        this.imagenGrande = imagenGrande;
    }

    public String getProductoNuevo() {
        return productoNuevo;
    }

    public void setProductoNuevo(String productoNuevo) {
        this.productoNuevo = productoNuevo;
    }

    public Rubro01 getRubr01() {
        return rubr01;
    }

    public void setRubr01(Rubro01 rubr01) {
        this.rubr01 = rubr01;
    }

    public Rubro02 getRubr02() {
        return rubr02;
    }

    public void setRubr02(Rubro02 rubr02) {
        this.rubr02 = rubr02;
    }

    public Rubro03 getRubr03() {
        return rubr03;
    }

    public void setRubr03(Rubro03 rubr03) {
        this.rubr03 = rubr03;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final ParametroStock other = (ParametroStock) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Producto{" + "id=" + id  + "}";
    }

}
