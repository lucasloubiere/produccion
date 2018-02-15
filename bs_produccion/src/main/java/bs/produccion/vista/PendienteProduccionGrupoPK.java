package bs.produccion.vista;


import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;


/**
 *
 * @author ctrosch
 */
@Embeddable
public class PendienteProduccionGrupoPK implements Serializable {
        
    private String circom;        
    private int idMapl;
    private String modfor;
    private String codfor;
    private int nrofor;
    private Date fchmov;
    private String formul;
    private String stocks;

    public PendienteProduccionGrupoPK() {
    }

    public String getCircom() {
        return circom;
    }

    public void setCircom(String circom) {
        this.circom = circom;
    }

    public int getIdMapl() {
        return idMapl;
    }

    public void setIdMapl(int idMapl) {
        this.idMapl = idMapl;
    }

    public String getCodfor() {
        return codfor;
    }

    public void setCodfor(String codfor) {
        this.codfor = codfor;
    }

    public int getNrofor() {
        return nrofor;
    }

    public void setNrofor(int nrofor) {
        this.nrofor = nrofor;
    }

    public Date getFchmov() {
        return fchmov;
    }

    public void setFchmov(Date fchmov) {
        this.fchmov = fchmov;
    }

    public String getStocks() {
        return stocks;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public String getModfor() {
        return modfor;
    }

    public void setModfor(String modfor) {
        this.modfor = modfor;
    }

    public String getFormul() {
        return formul;
    }

    public void setFormul(String formul) {
        this.formul = formul;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.circom != null ? this.circom.hashCode() : 0);
        hash = 19 * hash + this.idMapl;
        hash = 19 * hash + (this.modfor != null ? this.modfor.hashCode() : 0);
        hash = 19 * hash + (this.codfor != null ? this.codfor.hashCode() : 0);
        hash = 19 * hash + this.nrofor;
        hash = 19 * hash + (this.fchmov != null ? this.fchmov.hashCode() : 0);
        hash = 19 * hash + (this.formul != null ? this.formul.hashCode() : 0);
        hash = 19 * hash + (this.stocks != null ? this.stocks.hashCode() : 0);
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
        final PendienteProduccionGrupoPK other = (PendienteProduccionGrupoPK) obj;
        if (this.idMapl != other.idMapl) {
            return false;
        }
        if (this.nrofor != other.nrofor) {
            return false;
        }
        if ((this.circom == null) ? (other.circom != null) : !this.circom.equals(other.circom)) {
            return false;
        }
        if ((this.modfor == null) ? (other.modfor != null) : !this.modfor.equals(other.modfor)) {
            return false;
        }
        if ((this.codfor == null) ? (other.codfor != null) : !this.codfor.equals(other.codfor)) {
            return false;
        }
        if ((this.formul == null) ? (other.formul != null) : !this.formul.equals(other.formul)) {
            return false;
        }
        if ((this.stocks == null) ? (other.stocks != null) : !this.stocks.equals(other.stocks)) {
            return false;
        }
        if (this.fchmov != other.fchmov && (this.fchmov == null || !this.fchmov.equals(other.fchmov))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PendienteProduccionGrupoPK{" + "circom=" + circom + ", idMapl=" + idMapl + ", modfor=" + modfor + ", codfor=" + codfor + ", nrofor=" + nrofor + ", fchmov=" + fchmov + ", formul=" + formul + ", stocks=" + stocks + '}';
    }
    
}
