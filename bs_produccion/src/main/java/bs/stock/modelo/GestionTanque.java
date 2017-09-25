/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bs.stock.modelo;

import bs.global.modelo.Auditoria;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Embedded;

/**
 *
 * @author Claudio
 */
public class GestionTanque {
    
    private int id;
    private Date fechaMovimiento;
    
    private List<ItemGestionTanque> items;
    
    @Embedded
    private Auditoria auditoria;

    public GestionTanque() {
        
        this.auditoria = new Auditoria();
        this.items = new ArrayList<ItemGestionTanque>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public List<ItemGestionTanque> getItems() {
        return items;
    }

    public void setItems(List<ItemGestionTanque> items) {
        this.items = items;
    }
    
    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.id;
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
        final GestionTanque other = (GestionTanque) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GestionTanque{" + "id=" + id + '}';
    }
    
}
