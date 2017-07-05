/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.seguridad.modelo;

import java.io.Serializable;

/**
 *
 * @author ctrosch
 */

public class ItemMenuUsuarioPK implements Serializable {
    
    private String idUsuario;    
    private String codMenu;

    public ItemMenuUsuarioPK() {
        
    }

    public ItemMenuUsuarioPK(String idUsuario, String codMenu) {
        this.idUsuario = idUsuario;
        this.codMenu = codMenu;
    }
    
    

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCodMenu() {
        return codMenu;
    }

    public void setCodMenu(String codMenu) {
        this.codMenu = codMenu;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.idUsuario != null ? this.idUsuario.hashCode() : 0);
        hash = 59 * hash + (this.codMenu != null ? this.codMenu.hashCode() : 0);
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
        final ItemMenuUsuarioPK other = (ItemMenuUsuarioPK) obj;
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
        return "ItemMenuUsuarioPK{" + "idUsuario=" + idUsuario + ", codMenu=" + codMenu + '}';
    }
    
    


}
