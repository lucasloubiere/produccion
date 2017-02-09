/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.seguridad.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ctrosch
 */

public class ItemMenuUsuarioPK implements Serializable {
    
    private String idUsuario;    
    private int idMenu;

    public ItemMenuUsuarioPK() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.idUsuario);
        hash = 37 * hash + this.idMenu;
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
        if (this.idMenu != other.idMenu) {
            return false;
        }
        if (!Objects.equals(this.idUsuario, other.idUsuario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ItemMenuUsuarioPK{" + "idUsuario=" + idUsuario + ", idMenu=" + idMenu + '}';
    }
    
    



}
