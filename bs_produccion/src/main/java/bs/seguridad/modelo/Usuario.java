/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.seguridad.modelo;


import bs.global.modelo.Auditoria;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 *
 * @author Lloubiere
 */
@Entity
@Table(name = "sg_usuario")
@Cacheable(true) 
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id    
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @Column(name = "USUARIO", length = 15)
    private String usuario;    
    @Column(name = "PASSWORD", length = 15)
    private String password;
    @Column(name = "NOMBRE", length = 80)
    private String nombre;    
    @Column(name = "EMAIL", length = 80)
    private String email;
    
    @Column(name = "IMAGEN", length = 80)
    private String imagen;
        
    @JoinColumn(name = "ID_ESTADO", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private EstadoUsuario estado;
    @JoinColumn(name = "ID_TIPO", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoUsuario tipo;
    
    @Column(name = "ID_ENTIDAD")
    private int idEntidad;    
    
    @Embedded
    private Auditoria auditoria;
    

    /**
    @JoinTable(name = "sec_usuario_menu", joinColumns = {
        @JoinColumn(name = "ID_USUARIO", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_MENU", referencedColumnName = "id")})
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE},fetch=FetchType.EAGER)
   
    private Set<Menu> menu = new HashSet<Menu>();
     * */
//
//    @Transient
//    private List<ItemMenuUsuario> menu;



    public Usuario() {
        
        this.auditoria = new Auditoria();
    }

    public Usuario(Integer id) {
        this.id = id;
        this.auditoria = new Auditoria();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "isd.empresa.modelo.Usuario[id=" + id + "]";
    }

    public int getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(int idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }
    
    
    
}
