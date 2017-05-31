/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.stock.modelo;


import bs.global.modelo.Comprobante;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author Claudio
 *
 * Entidad que registra los componentes a producir
 *
 */
@Entity
@DiscriminatorValue("ST")
public class ComprobanteStock extends Comprobante {

    /**
     * Mascara de stock
     */
    @Column(name = "STMASCAR", length = 6)
    private String mascara;
    /*
     * Tipo de movimiento
     * A = Ajuste
     * I = Ingreso
     * E = Egreso
     * T = Transferencia
     */
    
    @Column(name = "STTIPMOV", length = 1)
    private String tipoMovimiento;


    public ComprobanteStock() {

    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
 
      
    

    
}
