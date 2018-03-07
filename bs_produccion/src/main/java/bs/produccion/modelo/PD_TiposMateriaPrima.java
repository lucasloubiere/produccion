package bs.produccion.modelo;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package bs.produccion.modelo;
//
//import java.io.Serializable;
//import java.util.Date;
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
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
//@Table(name = "pdtmap")
//public class PD_TiposMateriaPrima implements Serializable {
//    private static final long serialVersionUID = 1L;
//    @Id
//    @Basic(optional = false)
//    @Column(name = "PDTMAP_TIPMAP", nullable = false, length = 6)
//    private String pdtmapTipmap;
//    @Column(name = "PDTMAP_DESCRP", length = 60)
//    private String pdtmapDescrp;
//    @Column(name = "PDTMAP_FECALT")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date pdtmapFecalt;
//    @Column(name = "PDTMAP_FECMOD")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date pdtmapFecmod;
//    @Column(name = "PDTMAP_USERID", length = 15)
//    private String pdtmapUserid;
//    @Column(name = "PDTMAP_ULTOPR")
//    private Character pdtmapUltopr;
//    @Column(name = "PDTMAP_DEBAJA")
//    private Character pdtmapDebaja;
//    @Column(name = "PDTMAP_HORMOV", length = 10)
//    private String pdtmapHormov;
//    @Column(name = "PDTMAP_MODULE", length = 10)
//    private String pdtmapModule;
//    @Column(name = "PDTMAP_OALIAS", length = 10)
//    private String pdtmapOalias;
//    @Lob
//    @Column(name = "PDTMAP_TSTAMP")
//    private byte[] pdtmapTstamp;
//    @Column(name = "PDTMAP_LOTTRA", length = 6)
//    private String pdtmapLottra;
//    @Column(name = "PDTMAP_LOTREC", length = 6)
//    private String pdtmapLotrec;
//    @Column(name = "PDTMAP_LOTORI", length = 6)
//    private String pdtmapLotori;
//    @Column(name = "PDTMAP_SYSVER", length = 10)
//    private String pdtmapSysver;
//    @Column(name = "PDTMAP_CMPVER", length = 10)
//    private String pdtmapCmpver;
//
//    public PD_TiposMateriaPrima() {
//    }
//
//    public PD_TiposMateriaPrima(String pdtmapTipmap) {
//        this.pdtmapTipmap = pdtmapTipmap;
//    }
//
//    public String getPdtmapTipmap() {
//        return pdtmapTipmap;
//    }
//
//    public void setPdtmapTipmap(String pdtmapTipmap) {
//        this.pdtmapTipmap = pdtmapTipmap;
//    }
//
//    public String getPdtmapDescrp() {
//        return pdtmapDescrp;
//    }
//
//    public void setPdtmapDescrp(String pdtmapDescrp) {
//        this.pdtmapDescrp = pdtmapDescrp;
//    }
//
//    public Date getPdtmapFecalt() {
//        return pdtmapFecalt;
//    }
//
//    public void setPdtmapFecalt(Date pdtmapFecalt) {
//        this.pdtmapFecalt = pdtmapFecalt;
//    }
//
//    public Date getPdtmapFecmod() {
//        return pdtmapFecmod;
//    }
//
//    public void setPdtmapFecmod(Date pdtmapFecmod) {
//        this.pdtmapFecmod = pdtmapFecmod;
//    }
//
//    public String getPdtmapUserid() {
//        return pdtmapUserid;
//    }
//
//    public void setPdtmapUserid(String pdtmapUserid) {
//        this.pdtmapUserid = pdtmapUserid;
//    }
//
//    public Character getPdtmapUltopr() {
//        return pdtmapUltopr;
//    }
//
//    public void setPdtmapUltopr(Character pdtmapUltopr) {
//        this.pdtmapUltopr = pdtmapUltopr;
//    }
//
//    public Character getPdtmapDebaja() {
//        return pdtmapDebaja;
//    }
//
//    public void setPdtmapDebaja(Character pdtmapDebaja) {
//        this.pdtmapDebaja = pdtmapDebaja;
//    }
//
//    public String getPdtmapHormov() {
//        return pdtmapHormov;
//    }
//
//    public void setPdtmapHormov(String pdtmapHormov) {
//        this.pdtmapHormov = pdtmapHormov;
//    }
//
//    public String getPdtmapModule() {
//        return pdtmapModule;
//    }
//
//    public void setPdtmapModule(String pdtmapModule) {
//        this.pdtmapModule = pdtmapModule;
//    }
//
//    public String getPdtmapOalias() {
//        return pdtmapOalias;
//    }
//
//    public void setPdtmapOalias(String pdtmapOalias) {
//        this.pdtmapOalias = pdtmapOalias;
//    }
//
//    public byte[] getPdtmapTstamp() {
//        return pdtmapTstamp;
//    }
//
//    public void setPdtmapTstamp(byte[] pdtmapTstamp) {
//        this.pdtmapTstamp = pdtmapTstamp;
//    }
//
//    public String getPdtmapLottra() {
//        return pdtmapLottra;
//    }
//
//    public void setPdtmapLottra(String pdtmapLottra) {
//        this.pdtmapLottra = pdtmapLottra;
//    }
//
//    public String getPdtmapLotrec() {
//        return pdtmapLotrec;
//    }
//
//    public void setPdtmapLotrec(String pdtmapLotrec) {
//        this.pdtmapLotrec = pdtmapLotrec;
//    }
//
//    public String getPdtmapLotori() {
//        return pdtmapLotori;
//    }
//
//    public void setPdtmapLotori(String pdtmapLotori) {
//        this.pdtmapLotori = pdtmapLotori;
//    }
//
//    public String getPdtmapSysver() {
//        return pdtmapSysver;
//    }
//
//    public void setPdtmapSysver(String pdtmapSysver) {
//        this.pdtmapSysver = pdtmapSysver;
//    }
//
//    public String getPdtmapCmpver() {
//        return pdtmapCmpver;
//    }
//
//    public void setPdtmapCmpver(String pdtmapCmpver) {
//        this.pdtmapCmpver = pdtmapCmpver;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (pdtmapTipmap != null ? pdtmapTipmap.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof PD_TiposMateriaPrima)) {
//            return false;
//        }
//        PD_TiposMateriaPrima other = (PD_TiposMateriaPrima) object;
//        if ((this.pdtmapTipmap == null && other.pdtmapTipmap != null) || (this.pdtmapTipmap != null && !this.pdtmapTipmap.equals(other.pdtmapTipmap))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "isd.produccion.modelo.PD_TiposMateriaPrima[pdtmapTipmap=" + pdtmapTipmap + "]";
//    }
//
//}
