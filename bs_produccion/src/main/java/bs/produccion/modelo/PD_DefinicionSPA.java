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
//@Table(name = "pdtdsh")
//public class PD_DefinicionSPA implements Serializable {
//    private static final long serialVersionUID = 1L;
//    @Id
//    @Basic(optional = false)
//    @Column(name = "PDTDSH_CODIGO", nullable = false, length = 6)
//    private String pdtdshCodigo;
//    @Basic(optional = false)
//    @Column(name = "PDTDSH_DESCRP", nullable = false, length = 60)
//    private String pdtdshDescrp;
//    @Basic(optional = false)
//    @Column(name = "PDTDSH_REPDET", nullable = false, length = 60)
//    private String pdtdshRepdet;
//    @Column(name = "PDTDSH_FECALT")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date pdtdshFecalt;
//    @Column(name = "PDTDSH_FECMOD")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date pdtdshFecmod;
//    @Column(name = "PDTDSH_USERID", length = 15)
//    private String pdtdshUserid;
//    @Column(name = "PDTDSH_ULTOPR")
//    private Character pdtdshUltopr;
//    @Column(name = "PDTDSH_DEBAJA")
//    private Character pdtdshDebaja;
//    @Column(name = "PDTDSH_HORMOV", length = 10)
//    private String pdtdshHormov;
//    @Column(name = "PDTDSH_MODULE", length = 10)
//    private String pdtdshModule;
//    @Column(name = "PDTDSH_OALIAS", length = 10)
//    private String pdtdshOalias;
//    @Lob
//    @Column(name = "PDTDSH_TSTAMP")
//    private byte[] pdtdshTstamp;
//    @Column(name = "PDTDSH_LOTTRA", length = 6)
//    private String pdtdshLottra;
//    @Column(name = "PDTDSH_LOTREC", length = 6)
//    private String pdtdshLotrec;
//    @Column(name = "PDTDSH_LOTORI", length = 6)
//    private String pdtdshLotori;
//    @Column(name = "PDTDSH_SYSVER", length = 10)
//    private String pdtdshSysver;
//    @Column(name = "PDTDSH_CMPVER", length = 10)
//    private String pdtdshCmpver;
//
//    public PD_DefinicionSPA() {
//    }
//
//    public PD_DefinicionSPA(String pdtdshCodigo) {
//        this.pdtdshCodigo = pdtdshCodigo;
//    }
//
//    public PD_DefinicionSPA(String pdtdshCodigo, String pdtdshDescrp, String pdtdshRepdet) {
//        this.pdtdshCodigo = pdtdshCodigo;
//        this.pdtdshDescrp = pdtdshDescrp;
//        this.pdtdshRepdet = pdtdshRepdet;
//    }
//
//    public String getPdtdshCodigo() {
//        return pdtdshCodigo;
//    }
//
//    public void setPdtdshCodigo(String pdtdshCodigo) {
//        this.pdtdshCodigo = pdtdshCodigo;
//    }
//
//    public String getPdtdshDescrp() {
//        return pdtdshDescrp;
//    }
//
//    public void setPdtdshDescrp(String pdtdshDescrp) {
//        this.pdtdshDescrp = pdtdshDescrp;
//    }
//
//    public String getPdtdshRepdet() {
//        return pdtdshRepdet;
//    }
//
//    public void setPdtdshRepdet(String pdtdshRepdet) {
//        this.pdtdshRepdet = pdtdshRepdet;
//    }
//
//    public Date getPdtdshFecalt() {
//        return pdtdshFecalt;
//    }
//
//    public void setPdtdshFecalt(Date pdtdshFecalt) {
//        this.pdtdshFecalt = pdtdshFecalt;
//    }
//
//    public Date getPdtdshFecmod() {
//        return pdtdshFecmod;
//    }
//
//    public void setPdtdshFecmod(Date pdtdshFecmod) {
//        this.pdtdshFecmod = pdtdshFecmod;
//    }
//
//    public String getPdtdshUserid() {
//        return pdtdshUserid;
//    }
//
//    public void setPdtdshUserid(String pdtdshUserid) {
//        this.pdtdshUserid = pdtdshUserid;
//    }
//
//    public Character getPdtdshUltopr() {
//        return pdtdshUltopr;
//    }
//
//    public void setPdtdshUltopr(Character pdtdshUltopr) {
//        this.pdtdshUltopr = pdtdshUltopr;
//    }
//
//    public Character getPdtdshDebaja() {
//        return pdtdshDebaja;
//    }
//
//    public void setPdtdshDebaja(Character pdtdshDebaja) {
//        this.pdtdshDebaja = pdtdshDebaja;
//    }
//
//    public String getPdtdshHormov() {
//        return pdtdshHormov;
//    }
//
//    public void setPdtdshHormov(String pdtdshHormov) {
//        this.pdtdshHormov = pdtdshHormov;
//    }
//
//    public String getPdtdshModule() {
//        return pdtdshModule;
//    }
//
//    public void setPdtdshModule(String pdtdshModule) {
//        this.pdtdshModule = pdtdshModule;
//    }
//
//    public String getPdtdshOalias() {
//        return pdtdshOalias;
//    }
//
//    public void setPdtdshOalias(String pdtdshOalias) {
//        this.pdtdshOalias = pdtdshOalias;
//    }
//
//    public byte[] getPdtdshTstamp() {
//        return pdtdshTstamp;
//    }
//
//    public void setPdtdshTstamp(byte[] pdtdshTstamp) {
//        this.pdtdshTstamp = pdtdshTstamp;
//    }
//
//    public String getPdtdshLottra() {
//        return pdtdshLottra;
//    }
//
//    public void setPdtdshLottra(String pdtdshLottra) {
//        this.pdtdshLottra = pdtdshLottra;
//    }
//
//    public String getPdtdshLotrec() {
//        return pdtdshLotrec;
//    }
//
//    public void setPdtdshLotrec(String pdtdshLotrec) {
//        this.pdtdshLotrec = pdtdshLotrec;
//    }
//
//    public String getPdtdshLotori() {
//        return pdtdshLotori;
//    }
//
//    public void setPdtdshLotori(String pdtdshLotori) {
//        this.pdtdshLotori = pdtdshLotori;
//    }
//
//    public String getPdtdshSysver() {
//        return pdtdshSysver;
//    }
//
//    public void setPdtdshSysver(String pdtdshSysver) {
//        this.pdtdshSysver = pdtdshSysver;
//    }
//
//    public String getPdtdshCmpver() {
//        return pdtdshCmpver;
//    }
//
//    public void setPdtdshCmpver(String pdtdshCmpver) {
//        this.pdtdshCmpver = pdtdshCmpver;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (pdtdshCodigo != null ? pdtdshCodigo.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof PD_DefinicionSPA)) {
//            return false;
//        }
//        PD_DefinicionSPA other = (PD_DefinicionSPA) object;
//        if ((this.pdtdshCodigo == null && other.pdtdshCodigo != null) || (this.pdtdshCodigo != null && !this.pdtdshCodigo.equals(other.pdtdshCodigo))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "isd.produccion.modelo.PD_DefinicionSPA[pdtdshCodigo=" + pdtdshCodigo + "]";
//    }
//
//}
