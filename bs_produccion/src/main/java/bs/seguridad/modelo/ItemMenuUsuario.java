/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.seguridad.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author ctrosch
 */
@Entity
@Table(name = "sg_itemmenuusuario")
@IdClass(ItemMenuUsuarioPK.class)
public class ItemMenuUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
       
    @Id
    @Column(name = "ID_USUARIO", nullable = false)
    private String idUsuario;
    @Id
    @Column(name = "COD_MENU", nullable = false)
    private String codMenu;

    @JoinColumn(name="ID_USUARIO",referencedColumnName="ID", nullable=false, insertable=false, updatable=false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @JoinColumn(name="ID_MENU",referencedColumnName="ID", nullable=false, insertable=false, updatable=false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    
    public ItemMenuUsuario() {

    }

    public ItemMenuUsuario(String idUsuario, String codMenu) {
        this.idUsuario = idUsuario;
        this.codMenu = codMenu;
    }

    public String getCodMenu() {
        return codMenu;
    }

    public void setCodMenu(String codMenu) {
        this.codMenu = codMenu;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.idUsuario != null ? this.idUsuario.hashCode() : 0);
        hash = 23 * hash + (this.codMenu != null ? this.codMenu.hashCode() : 0);
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
        final ItemMenuUsuario other = (ItemMenuUsuario) obj;
        if (this.codMenu != other.codMenu) {
            return false;
        }
        if ((this.idUsuario == null) ? (other.idUsuario != null) : !this.idUsuario.equals(other.idUsuario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ItemMenuUsuario{" + "idUsuario=" + idUsuario + ", codMenu=" + codMenu + '}';
    }
    

}
