package bs.produccion.modelo;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package bs.produccion.modelo;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//import javax.persistence.Column;
//import javax.persistence.EmbeddedId;
//import javax.persistence.Entity;
//import javax.persistence.Lob;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//
///**
// *
// * @author ctrosch
// */
//@Entity
//@Table(name = "pdrctl")
//public class PD_LogCalculoCosto implements Serializable {
//    private static final long serialVersionUID = 1L;
//    @EmbeddedId
//    protected PD_LogCalculoCostoPK pD_LogCalculoCostoPK;
//    @Column(name = "PDRCTL_TIPCOS")
//    private Character pdrctlTipcos;
//    @Column(name = "PDRCTL_PRECIO", precision = 20, scale = 8)
//    private BigDecimal pdrctlPrecio;
//    @Column(name = "PDRCTL_FECALT")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date pdrctlFecalt;
//    @Column(name = "PDRCTL_FECMOD")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date pdrctlFecmod;
//    @Column(name = "PDRCTL_USERID", length = 15)
//    private String pdrctlUserid;
//    @Column(name = "PDRCTL_ULTOPR")
//    private Character pdrctlUltopr;
//    @Column(name = "PDRCTL_DEBAJA")
//    private Character pdrctlDebaja;
//    @Column(name = "PDRCTL_HORMOV", length = 10)
//    private String pdrctlHormov;
//    @Column(name = "PDRCTL_MODULE", length = 10)
//    private String pdrctlModule;
//    @Column(name = "PDRCTL_OALIAS", length = 10)
//    private String pdrctlOalias;
//    @Lob
//    @Column(name = "PDRCTL_TSTAMP")
//    private byte[] pdrctlTstamp;
//    @Column(name = "PDRCTL_LOTTRA", length = 6)
//    private String pdrctlLottra;
//    @Column(name = "PDRCTL_LOTREC", length = 6)
//    private String pdrctlLotrec;
//    @Column(name = "PDRCTL_LOTORI", length = 6)
//    private String pdrctlLotori;
//    @Column(name = "PDRCTL_SYSVER", length = 10)
//    private String pdrctlSysver;
//    @Column(name = "PDRCTL_CMPVER", length = 10)
//    private String pdrctlCmpver;
//
//    public PD_LogCalculoCosto() {
//    }
//
//    public PD_LogCalculoCosto(PD_LogCalculoCostoPK pD_LogCalculoCostoPK) {
//        this.pD_LogCalculoCostoPK = pD_LogCalculoCostoPK;
//    }
//
//    public PD_LogCalculoCosto(int pdrctlNrolot, String pdrctlTippro, String pdrctlArtcod) {
//        this.pD_LogCalculoCostoPK = new PD_LogCalculoCostoPK(pdrctlNrolot, pdrctlTippro, pdrctlArtcod);
//    }
//
//    public PD_LogCalculoCostoPK getPD_LogCalculoCostoPK() {
//        return pD_LogCalculoCostoPK;
//    }
//
//    public void setPD_LogCalculoCostoPK(PD_LogCalculoCostoPK pD_LogCalculoCostoPK) {
//        this.pD_LogCalculoCostoPK = pD_LogCalculoCostoPK;
//    }
//
//    public Character getPdrctlTipcos() {
//        return pdrctlTipcos;
//    }
//
//    public void setPdrctlTipcos(Character pdrctlTipcos) {
//        this.pdrctlTipcos = pdrctlTipcos;
//    }
//
//    public BigDecimal getPdrctlPrecio() {
//        return pdrctlPrecio;
//    }
//
//    public void setPdrctlPrecio(BigDecimal pdrctlPrecio) {
//        this.pdrctlPrecio = pdrctlPrecio;
//    }
//
//    public Date getPdrctlFecalt() {
//        return pdrctlFecalt;
//    }
//
//    public void setPdrctlFecalt(Date pdrctlFecalt) {
//        this.pdrctlFecalt = pdrctlFecalt;
//    }
//
//    public Date getPdrctlFecmod() {
//        return pdrctlFecmod;
//    }
//
//    public void setPdrctlFecmod(Date pdrctlFecmod) {
//        this.pdrctlFecmod = pdrctlFecmod;
//    }
//
//    public String getPdrctlUserid() {
//        return pdrctlUserid;
//    }
//
//    public void setPdrctlUserid(String pdrctlUserid) {
//        this.pdrctlUserid = pdrctlUserid;
//    }
//
//    public Character getPdrctlUltopr() {
//        return pdrctlUltopr;
//    }
//
//    public void setPdrctlUltopr(Character pdrctlUltopr) {
//        this.pdrctlUltopr = pdrctlUltopr;
//    }
//
//    public Character getPdrctlDebaja() {
//        return pdrctlDebaja;
//    }
//
//    public void setPdrctlDebaja(Character pdrctlDebaja) {
//        this.pdrctlDebaja = pdrctlDebaja;
//    }
//
//    public String getPdrctlHormov() {
//        return pdrctlHormov;
//    }
//
//    public void setPdrctlHormov(String pdrctlHormov) {
//        this.pdrctlHormov = pdrctlHormov;
//    }
//
//    public String getPdrctlModule() {
//        return pdrctlModule;
//    }
//
//    public void setPdrctlModule(String pdrctlModule) {
//        this.pdrctlModule = pdrctlModule;
//    }
//
//    public String getPdrctlOalias() {
//        return pdrctlOalias;
//    }
//
//    public void setPdrctlOalias(String pdrctlOalias) {
//        this.pdrctlOalias = pdrctlOalias;
//    }
//
//    public byte[] getPdrctlTstamp() {
//        return pdrctlTstamp;
//    }
//
//    public void setPdrctlTstamp(byte[] pdrctlTstamp) {
//        this.pdrctlTstamp = pdrctlTstamp;
//    }
//
//    public String getPdrctlLottra() {
//        return pdrctlLottra;
//    }
//
//    public void setPdrctlLottra(String pdrctlLottra) {
//        this.pdrctlLottra = pdrctlLottra;
//    }
//
//    public String getPdrctlLotrec() {
//        return pdrctlLotrec;
//    }
//
//    public void setPdrctlLotrec(String pdrctlLotrec) {
//        this.pdrctlLotrec = pdrctlLotrec;
//    }
//
//    public String getPdrctlLotori() {
//        return pdrctlLotori;
//    }
//
//    public void setPdrctlLotori(String pdrctlLotori) {
//        this.pdrctlLotori = pdrctlLotori;
//    }
//
//    public String getPdrctlSysver() {
//        return pdrctlSysver;
//    }
//
//    public void setPdrctlSysver(String pdrctlSysver) {
//        this.pdrctlSysver = pdrctlSysver;
//    }
//
//    public String getPdrctlCmpver() {
//        return pdrctlCmpver;
//    }
//
//    public void setPdrctlCmpver(String pdrctlCmpver) {
//        this.pdrctlCmpver = pdrctlCmpver;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (pD_LogCalculoCostoPK != null ? pD_LogCalculoCostoPK.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof PD_LogCalculoCosto)) {
//            return false;
//        }
//        PD_LogCalculoCosto other = (PD_LogCalculoCosto) object;
//        if ((this.pD_LogCalculoCostoPK == null && other.pD_LogCalculoCostoPK != null) || (this.pD_LogCalculoCostoPK != null && !this.pD_LogCalculoCostoPK.equals(other.pD_LogCalculoCostoPK))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "isd.produccion.modelo.PD_LogCalculoCosto[pD_LogCalculoCostoPK=" + pD_LogCalculoCostoPK + "]";
//    }
//
//}
