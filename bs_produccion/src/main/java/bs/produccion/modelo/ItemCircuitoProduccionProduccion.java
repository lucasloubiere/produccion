package bs.produccion.modelo;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("PD")
public class ItemCircuitoProduccionProduccion extends ItemCircuitoProduccion {

    @JoinColumns({
    @JoinColumn(name = "modulo", referencedColumnName = "modcom", nullable = false,insertable=false, updatable=false),
    @JoinColumn(name = "codcom", referencedColumnName = "codcom", nullable = false,insertable=false, updatable=false)
    })
    @ManyToOne(fetch = FetchType.LAZY)
    ComprobanteProduccion comprobante;

    public ComprobanteProduccion getComprobante() {
        return comprobante;
    }

    public void setComprobante(ComprobanteProduccion comprobante) {
        this.comprobante = comprobante;
    }
}
