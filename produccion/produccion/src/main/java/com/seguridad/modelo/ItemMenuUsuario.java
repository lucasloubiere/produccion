/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.seguridad.modelo;

import java.io.Serializable;
import java.util.Objects;
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
    @Column(name = "ID_MENU", nullable = false)
    private int idMenu;

    @JoinColumn(name="ID_USUARIO",referencedColumnName="ID", nullable=false, insertable=false, updatable=false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @JoinColumn(name="ID_MENU",referencedColumnName="ID", nullable=false, insertable=false, updatable=false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    
    public ItemMenuUsuario() {

    }

    public ItemMenuUsuario(String idUsuario, int idMenu) {
        this.idUsuario = idUsuario;
        this.idMenu = idMenu;
    }

    

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemMenuUsuario other = (ItemMenuUsuario) obj;
        if (this.idUsuario != other.idUsuario) {
            return false;
        }
        if (this.idMenu != other.idMenu) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.idUsuario);
        hash = 89 * hash + this.idMenu;
        return hash;
    }
 

    

    @Override
    public String toString() {
        return "isd.seguridad.modelo.SG_UsuarioMenuPK[idUsuario=" + idUsuario + ", idMenu=" + idMenu + "]";
    }

}
