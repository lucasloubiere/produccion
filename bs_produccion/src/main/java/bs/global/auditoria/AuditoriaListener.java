/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bs.global.auditoria;


import bs.global.modelo.Auditoria;
import bs.global.util.JsfUtil;
import bs.seguridad.web.UsuarioSessionBean;
import java.util.Date;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 *
 * @author Claudio
 * Si la clase no implementa la interfaz IAuditableEntity, no actualiza datos
 */
public class AuditoriaListener {
    
    public static UsuarioSessionBean getUsuarioSessionBean() {
        return (UsuarioSessionBean) JsfUtil.getObjeto("usuarioSessionBean", UsuarioSessionBean.class);
    }
    
    @PrePersist
    public void onPrePersist(Object o) {
        if (o instanceof IAuditableEntity) {

//            System.err.println("onPrePersist");

            IAuditableEntity e = (IAuditableEntity) o;
            if (e.getAuditoria() == null) {
                e.setAuditoria(new Auditoria());
            }
            
            e.getAuditoria().setFechaAlta(new Date());
            e.getAuditoria().setFechaModificacion(new Date());
            e.getAuditoria().setUltimaOperacion("A");                        
            
            
//            System.err.println("usuarioSessionBean " + getUsuarioSessionBean());                
//            e.getAuditoria().setUsuario(getUsuarioSessionBean().getUsuario().getUsuario());                
            
            
//            Empresa empresa = empresaRN.getEmpresa("01");
//            
//            if(empresa!=null){
//                e.getAuditoria().setCodigoEmpresa(empresa.getCodigo());                
//            }
        }
    }

    @PreUpdate
    public void onPreUpdate(Object o) {

        if (o instanceof IAuditableEntity) {

            IAuditableEntity e = (IAuditableEntity) o;
            if (e.getAuditoria() == null) {
                  e.setAuditoria(new Auditoria());
            }

            e.getAuditoria().setFechaModificacion(new Date());
            e.getAuditoria().setUltimaOperacion("M");
            
//            System.err.println("usuarioSessionBean " + getUsuarioSessionBean());                
//            e.getAuditoria().setUsuario(getUsuarioSessionBean().getUsuario().getUsuario());                

//            EmpresaBean eb = (EmpresaBean) JsfUtil.getManagedBean("empresaBean");
//            e.getAuditoria().setCodigoEmpresa(eb.getEmpresa().getCodigo());   
        }     
        
    }
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
 
}
