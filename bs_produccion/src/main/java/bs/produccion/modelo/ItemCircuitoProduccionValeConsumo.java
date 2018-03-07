package bs.produccion.modelo;


import bs.stock.modelo.ComprobanteStock;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;


/**
 *
 * @author Claudio
 */
@Entity
@DiscriminatorValue("VC")
public class ItemCircuitoProduccionValeConsumo extends ItemCircuitoProduccion {

    @JoinColumns({
    @JoinColumn(name = "modulo", referencedColumnName = "modcom", nullable = false,insertable=false, updatable=false),
    @JoinColumn(name = "codcom", referencedColumnName = "codcom", nullable = false,insertable=false, updatable=false)
    })
    @ManyToOne(fetch = FetchType.LAZY)
    ComprobanteStock comprobante;

    public ComprobanteStock getComprobante() {
        return comprobante;
    }

    public void setComprobante(ComprobanteStock comprobante) {
        this.comprobante = comprobante;
    }
}