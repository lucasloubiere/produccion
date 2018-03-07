package bs.produccion.modelo;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package bs.produccion.modelo;
//
//import java.io.Serializable;
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Embeddable;
//
///**
// *
// * @author Claudio
// */
//@Embeddable
//public class PD_ItemSeriePK implements Serializable {
//    
//    private String modfor;
//    private String codfor;
//    private int nrofor;
//    private int nroitm;
//    private String nivexp;
//    private int itmexp;
//    private int nroitd;
//
//    public PD_ItemSeriePK() {
//    }
//
//    public PD_ItemSeriePK(String modfor, String codfor, int nrofor, int nroitm, String nivexp, int itmexp, int nroitd) {
//        this.modfor = modfor;
//        this.codfor = codfor;
//        this.nrofor = nrofor;
//        this.nroitm = nroitm;
//        this.nivexp = nivexp;
//        this.itmexp = itmexp;
//        this.nroitd = nroitd;
//    }
//
//    public String getCodfor() {
//        return codfor;
//    }
//
//    public void setCodfor(String codfor) {
//        this.codfor = codfor;
//    }
//
//    public int getItmexp() {
//        return itmexp;
//    }
//
//    public void setItmexp(int itmexp) {
//        this.itmexp = itmexp;
//    }
//
//    public String getModfor() {
//        return modfor;
//    }
//
//    public void setModfor(String modfor) {
//        this.modfor = modfor;
//    }
//
//    public String getNivexp() {
//        return nivexp;
//    }
//
//    public void setNivexp(String nivexp) {
//        this.nivexp = nivexp;
//    }
//
//    public int getNrofor() {
//        return nrofor;
//    }
//
//    public void setNrofor(int nrofor) {
//        this.nrofor = nrofor;
//    }
//
//    public int getNroitd() {
//        return nroitd;
//    }
//
//    public void setNroitd(int nroitd) {
//        this.nroitd = nroitd;
//    }
//
//    public int getNroitm() {
//        return nroitm;
//    }
//
//    public void setNroitm(int nroitm) {
//        this.nroitm = nroitm;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final PD_ItemSeriePK other = (PD_ItemSeriePK) obj;
//        if ((this.modfor == null) ? (other.modfor != null) : !this.modfor.equals(other.modfor)) {
//            return false;
//        }
//        if ((this.codfor == null) ? (other.codfor != null) : !this.codfor.equals(other.codfor)) {
//            return false;
//        }
//        if (this.nrofor != other.nrofor) {
//            return false;
//        }
//        if (this.nroitm != other.nroitm) {
//            return false;
//        }
//        if ((this.nivexp == null) ? (other.nivexp != null) : !this.nivexp.equals(other.nivexp)) {
//            return false;
//        }
//        if (this.itmexp != other.itmexp) {
//            return false;
//        }
//        if (this.nroitd != other.nroitd) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 3;
//        hash = 29 * hash + (this.modfor != null ? this.modfor.hashCode() : 0);
//        hash = 29 * hash + (this.codfor != null ? this.codfor.hashCode() : 0);
//        hash = 29 * hash + this.nrofor;
//        hash = 29 * hash + this.nroitm;
//        hash = 29 * hash + (this.nivexp != null ? this.nivexp.hashCode() : 0);
//        hash = 29 * hash + this.itmexp;
//        hash = 29 * hash + this.nroitd;
//        return hash;
//    }
//
//    @Override
//    public String toString() {
//        return "PD_ItemDetallePK{" + "modfor=" + modfor + "codfor=" + codfor + "nrofor=" + nrofor + "nroitm=" + nroitm + "nivexp=" + nivexp + "itmexp=" + itmexp + "nroitd=" + nroitd + '}';
//    }
//
//    
//}
