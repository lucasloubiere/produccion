package bs.produccion.modelo;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package bs.produccion.modelo;
//
//import isd.inventario.modelo.ST_Producto;
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.IdClass;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinColumns;
//import javax.persistence.Lob;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//
///**
// *
// * @author Claudio
// */
//@Entity
//@Table(name = "pdrmvs")
//@IdClass(PD_ItemSeriePK.class)
//public class ItemProductoProduccionSerie implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @Column(name = "PDRMVS_MODFOR", nullable = false, length = 2)
//    private String modfor;
//    @Id
//    @Column(name = "PDRMVS_CODFOR", nullable = false, length = 6)
//    private String codfor;
//    @Id
//    @Column(name = "PDRMVS_NROFOR", nullable = false)
//    private int nrofor;
//    @Id
//    @Column(name = "PDRMVS_NROITM", nullable = false)
//    private int nroitm;
//    @Id
//    @Column(name = "PDRMVS_NIVEXP", nullable = false, length = 2)
//    private String nivexp;
//    @Id
//    @Column(name = "PDRMVS_ITMEXP", nullable = false)
//    private int itmexp;
//    @Id
//    @Column(name = "PDRMVS_NROITD", nullable = false)
//    private int nroitd;
//
//    
//    @Column(name = "PDRMVS_MODAPL", length = 2)
//    private String modapl;
//    
//    @Column(name = "PDRMVS_CODAPL", length = 6)
//    private String codapl;
//    
//    @Column(name = "PDRMVS_NROAPL")
//    private Integer nroapl;
//    
//    @Column(name = "PDRMVS_ITMAPL")
//    private Integer itmapl;
//    
//    @Column(name = "PDRMVS_NIVAPL", length = 2)
//    private String nivapl;
//    
//    @Column(name = "PDRMVS_EXPAPL")
//    private Integer expapl;
//
//    @JoinColumns({
//    @JoinColumn(name = "PDRMVS_MODFOR", referencedColumnName = "PDRMVI_MODFOR", nullable = false, insertable=false, updatable=false),
//    @JoinColumn(name = "PDRMVS_CODFOR", referencedColumnName = "PDRMVI_CODFOR", nullable = false, insertable=false, updatable=false),
//    @JoinColumn(name = "PDRMVS_NROFOR", referencedColumnName = "PDRMVI_NROFOR", nullable = false, insertable=false, updatable=false),
//    @JoinColumn(name = "PDRMVS_NROITM", referencedColumnName = "PDRMVI_NROITM", nullable = false, insertable=false, updatable=false),
//    @JoinColumn(name = "PDRMVS_NIVEXP", referencedColumnName = "PDRMVI_NROFOR", nullable = false, insertable=false, updatable=false),
//    @JoinColumn(name = "PDRMVS_ITMEXP", referencedColumnName = "PDRMVI_ITMEXP", nullable = false, insertable=false, updatable=false)
//    })
//    @ManyToOne
//    private PD_ItemProducto itemProducto;
//
//
//    @JoinColumns({
//    @JoinColumn(name = "PDRMVS_TIPPRO", referencedColumnName = "STMPDH_TIPPRO", nullable = false),
//    @JoinColumn(name = "PDRMVS_ARTCOD", referencedColumnName = "STMPDH_ARTCOD", nullable = false)
//    })
//    @ManyToOne(optional=false)
//    private ST_Producto producto;
//
//    @JoinColumns({
//    @JoinColumn(name = "PDRMVS_TIPORI", referencedColumnName = "STMPDH_TIPPRO", nullable = false),
//    @JoinColumn(name = "PDRMVS_ARTORI", referencedColumnName = "STMPDH_ARTCOD", nullable = false)
//    })
//    @ManyToOne(optional=false)
//    private ST_Producto productoOriginal;
//
//
//    @Column(name = "PDRMVS_NSERIE", length = 30)
//    private String nserie;
//    @Column(name = "PDRMVS_NDESPA", length = 30)
//    private String ndespa;
//    @Column(name = "PDRMVS_ENVASE", length = 30)
//    private String envase;
//    @Column(name = "PDRMVS_NOTROS", length = 30)
//    private String notros;
//    @Column(name = "PDRMVS_NFECHA", length = 30)
//    private String nfecha;
//    @Column(name = "PDRMVS_NATRIB", length = 30)
//    private String natrib;
//    @Column(name = "PDRMVS_NESTAN", length = 30)
//    private String nestan;
//    @Column(name = "PDRMVS_NUBICA", length = 30)
//    private String nubica;
//    @Column(name = "PDRMVS_DEPOSI", length = 15)
//    private String deposi;
//    @Column(name = "PDRMVS_SECTOR", length = 15)
//    private String sector;
//    @Column(name = "PDRMVS_SUCURS", length = 6)
//    private String sucurs;
//    @Column(name = "PDRMVS_CANTID", precision = 18, scale = 4)
//    private BigDecimal cantid;
//    @Column(name = "PDRMVS_CANTST", precision = 18, scale = 4)
//    private BigDecimal cantst;
//    @Column(name = "PDRMVS_CNTSEC", precision = 18, scale = 4)
//    private BigDecimal cntsec;
//
//    @Column(name = "PDRMVS_UNIMED", length = 6)
//    private String unimed;
//    @Column(name = "PDRMVS_PRECIO", precision = 20, scale = 6)
//    private BigDecimal precio;
//
//    @Column(name = "PDRMVS_MODCIE", length = 2)
//    private String modcie;
//    @Column(name = "PDRMVS_CODCIE", length = 6)
//    private String codcie;
//    @Column(name = "PDRMVS_NROCIE")
//    private Integer nrocie;
//    @Column(name = "PDRMVS_ITMCIE")
//    private Integer itmcie;
//    
//    @Lob
//    @Column(name = "PDRMVS_TEXTOS", length = 65535)
//    private String textos;
//
//    @Column(name = "PDRMVS_FCHENT")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date fchent;
//    @Column(name = "PDRMVS_FCHHAS")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date fchhas;
//
//
//
//    @Column(name = "PDRMVS_FECALT")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date fecalt;
//    @Column(name = "PDRMVS_FECMOD")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date fecmod;
//    @Column(name = "PDRMVS_USERID", length = 15)
//    private String userid;
//    @Column(name = "PDRMVS_ULTOPR")
//    private Character ultopr;
//    @Column(name = "PDRMVS_DEBAJA")
//    private Character debaja;
//    @Column(name = "PDRMVS_HORMOV", length = 10)
//    private String hormov;
//    @Column(name = "PDRMVS_MODULE", length = 10)
//    private String module;
//    @Column(name = "PDRMVS_OALIAS", length = 10)
//    private String oalias;
//    @Column(name = "PDRMVS_TSTAMP")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date tstamp;
//
//
//    public ItemProductoProduccionSerie() {
//        cantid = BigDecimal.ZERO;
//        cantst = BigDecimal.ZERO;
//        cntsec = BigDecimal.ZERO;
//        fecalt = new Date();
//        fecmod = new Date();
//        ultopr = 'A';
//    }
//
//    public ItemProductoProduccionSerie(String modfor, String codfor, int nrofor, int nroitm, String nivexp, int itmexp, int nroitd) {
//        this.modfor = modfor;
//        this.codfor = codfor;
//        this.nrofor = nrofor;
//        this.nroitm = nroitm;
//        this.nivexp = nivexp;
//        this.itmexp = itmexp;
//        this.nroitd = nroitd;
//
//        cantid = BigDecimal.ZERO;
//        cantst = BigDecimal.ZERO;
//        cntsec = BigDecimal.ZERO;
//        fecalt = new Date();
//        fecmod = new Date();
//        ultopr = 'A';
//    }
//
//
//    public String getCodfor() {
//        return codfor;
//    }
//
//    public void setCodfor(String codfor) {
//        this.codfor = codfor;
//    }
//
//    public PD_ItemProducto getItemProducto() {
//        return itemProducto;
//    }
//
//    public void setItemProducto(PD_ItemProducto itemProducto) {
//        this.itemProducto = itemProducto;
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
//    public ST_Producto getProducto() {
//        return producto;
//    }
//
//    public void setProducto(ST_Producto producto) {
//        this.producto = producto;
//    }
//
//    public ST_Producto getProductoOriginal() {
//        return productoOriginal;
//    }
//
//    public void setProductoOriginal(ST_Producto productoOriginal) {
//        this.productoOriginal = productoOriginal;
//    }
//
//    public BigDecimal getCantid() {
//        return cantid;
//    }
//
//    public void setCantid(BigDecimal cantid) {
//        this.cantid = cantid;
//    }
//
//    public BigDecimal getCantst() {
//        return cantst;
//    }
//
//    public void setCantst(BigDecimal cantst) {
//        this.cantst = cantst;
//    }
//
//    public BigDecimal getCntsec() {
//        return cntsec;
//    }
//
//    public void setCntsec(BigDecimal cntsec) {
//        this.cntsec = cntsec;
//    }
//
//    public String getCodapl() {
//        return codapl;
//    }
//
//    public void setCodapl(String codapl) {
//        this.codapl = codapl;
//    }
//
//    public String getCodcie() {
//        return codcie;
//    }
//
//    public void setCodcie(String codcie) {
//        this.codcie = codcie;
//    }
//
//    public Character getDebaja() {
//        return debaja;
//    }
//
//    public void setDebaja(Character debaja) {
//        this.debaja = debaja;
//    }
//
//    public String getDeposi() {
//        return deposi;
//    }
//
//    public void setDeposi(String deposi) {
//        this.deposi = deposi;
//    }
//
//    public String getEnvase() {
//        return envase;
//    }
//
//    public void setEnvase(String envase) {
//        this.envase = envase;
//    }
//
//    public Integer getExpapl() {
//        return expapl;
//    }
//
//    public void setExpapl(Integer expapl) {
//        this.expapl = expapl;
//    }
//
//    public Date getFchent() {
//        return fchent;
//    }
//
//    public void setFchent(Date fchent) {
//        this.fchent = fchent;
//    }
//
//    public Date getFchhas() {
//        return fchhas;
//    }
//
//    public void setFchhas(Date fchhas) {
//        this.fchhas = fchhas;
//    }
//
//    public Date getFecalt() {
//        return fecalt;
//    }
//
//    public void setFecalt(Date fecalt) {
//        this.fecalt = fecalt;
//    }
//
//    public Date getFecmod() {
//        return fecmod;
//    }
//
//    public void setFecmod(Date fecmod) {
//        this.fecmod = fecmod;
//    }
//
//    public String getHormov() {
//        return hormov;
//    }
//
//    public void setHormov(String hormov) {
//        this.hormov = hormov;
//    }
//
//    public Integer getItmapl() {
//        return itmapl;
//    }
//
//    public void setItmapl(Integer itmapl) {
//        this.itmapl = itmapl;
//    }
//
//    public Integer getItmcie() {
//        return itmcie;
//    }
//
//    public void setItmcie(Integer itmcie) {
//        this.itmcie = itmcie;
//    }
//
//    public String getModapl() {
//        return modapl;
//    }
//
//    public void setModapl(String modapl) {
//        this.modapl = modapl;
//    }
//
//    public String getModcie() {
//        return modcie;
//    }
//
//    public void setModcie(String modcie) {
//        this.modcie = modcie;
//    }
//
//    public String getModule() {
//        return module;
//    }
//
//    public void setModule(String module) {
//        this.module = module;
//    }
//
//    public String getNatrib() {
//        return natrib;
//    }
//
//    public void setNatrib(String natrib) {
//        this.natrib = natrib;
//    }
//
//    public String getNdespa() {
//        return ndespa;
//    }
//
//    public void setNdespa(String ndespa) {
//        this.ndespa = ndespa;
//    }
//
//    public String getNestan() {
//        return nestan;
//    }
//
//    public void setNestan(String nestan) {
//        this.nestan = nestan;
//    }
//
//    public String getNfecha() {
//        return nfecha;
//    }
//
//    public void setNfecha(String nfecha) {
//        this.nfecha = nfecha;
//    }
//
//    public String getNivapl() {
//        return nivapl;
//    }
//
//    public void setNivapl(String nivapl) {
//        this.nivapl = nivapl;
//    }
//
//    public String getNotros() {
//        return notros;
//    }
//
//    public void setNotros(String notros) {
//        this.notros = notros;
//    }
//
//    public Integer getNroapl() {
//        return nroapl;
//    }
//
//    public void setNroapl(Integer nroapl) {
//        this.nroapl = nroapl;
//    }
//
//    public Integer getNrocie() {
//        return nrocie;
//    }
//
//    public void setNrocie(Integer nrocie) {
//        this.nrocie = nrocie;
//    }
//
//    public String getNserie() {
//        return nserie;
//    }
//
//    public void setNserie(String nserie) {
//        this.nserie = nserie;
//    }
//
//    public String getNubica() {
//        return nubica;
//    }
//
//    public void setNubica(String nubica) {
//        this.nubica = nubica;
//    }
//
//    public String getOalias() {
//        return oalias;
//    }
//
//    public void setOalias(String oalias) {
//        this.oalias = oalias;
//    }
//
//    public BigDecimal getPrecio() {
//        return precio;
//    }
//
//    public void setPrecio(BigDecimal precio) {
//        this.precio = precio;
//    }
//
//    public String getSector() {
//        return sector;
//    }
//
//    public void setSector(String sector) {
//        this.sector = sector;
//    }
//
//    public String getSucurs() {
//        return sucurs;
//    }
//
//    public void setSucurs(String sucurs) {
//        this.sucurs = sucurs;
//    }
//
//    public String getTextos() {
//        return textos;
//    }
//
//    public void setTextos(String textos) {
//        this.textos = textos;
//    }
//
//    public Date getTstamp() {
//        return tstamp;
//    }
//
//    public void setTstamp(Date tstamp) {
//        this.tstamp = tstamp;
//    }
//
//    public Character getUltopr() {
//        return ultopr;
//    }
//
//    public void setUltopr(Character ultopr) {
//        this.ultopr = ultopr;
//    }
//
//    public String getUnimed() {
//        return unimed;
//    }
//
//    public void setUnimed(String unimed) {
//        this.unimed = unimed;
//    }
//
//    public String getUserid() {
//        return userid;
//    }
//
//    public void setUserid(String userid) {
//        this.userid = userid;
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
//        final ItemProductoProduccionSerie other = (ItemProductoProduccionSerie) obj;
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
//        int hash = 7;
//        hash = 53 * hash + (this.modfor != null ? this.modfor.hashCode() : 0);
//        hash = 53 * hash + (this.codfor != null ? this.codfor.hashCode() : 0);
//        hash = 53 * hash + this.nrofor;
//        hash = 53 * hash + this.nroitm;
//        hash = 53 * hash + (this.nivexp != null ? this.nivexp.hashCode() : 0);
//        hash = 53 * hash + this.itmexp;
//        hash = 53 * hash + this.nroitd;
//        return hash;
//    }
//
//    @Override
//    public String toString() {
//        return "PD_ItemSerie{" + "modfor=" + modfor + "codfor=" + codfor + "nrofor=" + nrofor + "nroitm=" + nroitm + "nivexp=" + nivexp + "itmexp=" + itmexp + "nroitd=" + nroitd + '}';
//    }
//
//    
//
//   
//}
