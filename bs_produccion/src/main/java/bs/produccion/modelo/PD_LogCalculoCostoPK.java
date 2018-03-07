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
// * @author ctrosch
// */
//@Embeddable
//public class PD_LogCalculoCostoPK implements Serializable {
//    @Basic(optional = false)
//    @Column(name = "PDRCTL_NROLOT", nullable = false)
//    private int pdrctlNrolot;
//    @Basic(optional = false)
//    @Column(name = "PDRCTL_TIPPRO", nullable = false, length = 6)
//    private String pdrctlTippro;
//    @Basic(optional = false)
//    @Column(name = "PDRCTL_ARTCOD", nullable = false, length = 30)
//    private String pdrctlArtcod;
//
//    public PD_LogCalculoCostoPK() {
//    }
//
//    public PD_LogCalculoCostoPK(int pdrctlNrolot, String pdrctlTippro, String pdrctlArtcod) {
//        this.pdrctlNrolot = pdrctlNrolot;
//        this.pdrctlTippro = pdrctlTippro;
//        this.pdrctlArtcod = pdrctlArtcod;
//    }
//
//    public int getPdrctlNrolot() {
//        return pdrctlNrolot;
//    }
//
//    public void setPdrctlNrolot(int pdrctlNrolot) {
//        this.pdrctlNrolot = pdrctlNrolot;
//    }
//
//    public String getPdrctlTippro() {
//        return pdrctlTippro;
//    }
//
//    public void setPdrctlTippro(String pdrctlTippro) {
//        this.pdrctlTippro = pdrctlTippro;
//    }
//
//    public String getPdrctlArtcod() {
//        return pdrctlArtcod;
//    }
//
//    public void setPdrctlArtcod(String pdrctlArtcod) {
//        this.pdrctlArtcod = pdrctlArtcod;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (int) pdrctlNrolot;
//        hash += (pdrctlTippro != null ? pdrctlTippro.hashCode() : 0);
//        hash += (pdrctlArtcod != null ? pdrctlArtcod.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof PD_LogCalculoCostoPK)) {
//            return false;
//        }
//        PD_LogCalculoCostoPK other = (PD_LogCalculoCostoPK) object;
//        if (this.pdrctlNrolot != other.pdrctlNrolot) {
//            return false;
//        }
//        if ((this.pdrctlTippro == null && other.pdrctlTippro != null) || (this.pdrctlTippro != null && !this.pdrctlTippro.equals(other.pdrctlTippro))) {
//            return false;
//        }
//        if ((this.pdrctlArtcod == null && other.pdrctlArtcod != null) || (this.pdrctlArtcod != null && !this.pdrctlArtcod.equals(other.pdrctlArtcod))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "isd.produccion.modelo.PD_LogCalculoCostoPK[pdrctlNrolot=" + pdrctlNrolot + ", pdrctlTippro=" + pdrctlTippro + ", pdrctlArtcod=" + pdrctlArtcod + "]";
//    }
//
//}
