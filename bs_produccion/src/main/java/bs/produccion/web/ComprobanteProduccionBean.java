package bs.produccion.web;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package bs.produccion.web;
//
//import isd.general.util.ABMEntidades;
//
//import isd.general.web.GenericBean;
//import isd.inventario.rn.SectorRN;
//import bs.produccion.modelo.ComprobanteProduccion;
//import bs.produccion.modelo.Operario;
//import bs.produccion.rn.PDComprobantesRN;
//import bs.produccion.rn.PDOperarioRN;
//import java.util.HashMap;
//import java.util.List;
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
//
///**
// *
// * @author Claudio
// */
//@ManagedBean
//@ViewScoped
//public class ComprobanteProduccionBean extends GenericBean implements ABMEntidades<ComprobanteProduccion> {
//
//    @EJB private PDComprobantesRN comprobanteRN;
//    @EJB protected SectorRN sectorRN;
//
//    private ComprobanteProduccion comprobante;
//    private List<ComprobanteProduccion> lista;
//
//
//    /** Creates a new instance of PD_operarioBean */
//    public ComprobanteProduccionBean() {
//    }
//
//    @PostConstruct
//    public void init(){
//
//        try {
//
//           filtro = new HashMap<String, String>();
//           filtro.put("auditoria.debaja =", "'N'");
//           lista = comprobanteRN.getLista(filtro);
//           titulo = "Comprobantes de Producción";
//           nuevo();
//           
//        } catch (Exception e) {
//            e.printStackTrace();
//            JsfUtil.addErrorMessage("Error al iniciar el bean " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void nuevo() {
//        comprobante = new ComprobanteProduccion();
//        nuevo = true;
//    }
//
//    @Override
//    public void guardar(boolean nuevo) {
//        try {
//            comprobanteRN.guardar(comprobante);
//            lista = comprobanteRN.getLista(filtro);
//            nuevo = false;
//            JsfUtil.addInfoMessage("Los datos han sido guardados correctamente");
//
//            if(nuevo){
//                nuevo();
//            }
//        } catch (Exception ex) {
//            JsfUtil.addErrorMessage("No es posible guardar los datos");
//        }
//    }
//   
//    @Override
//    public void eliminar(ComprobanteProduccion o) {
//
//        JsfUtil.addErrorMessage("No es posible borrar los datos");
////
////
////        try {
////            comprobanteRN.eliminar(o);
////            lista = comprobanteRN.getLista(filtro);
////            nuevo();
////            JsfUtil.addWarningMessage("Los datos han sido borrados");
////        } catch (Exception ex) {
////            JsfUtil.addErrorMessage("No es posible borrar los datos");
////        }
//        
//    }
//
//    @Override
//    public void deBaja() {
//        comprobante.getAuditoria().setDebaja("S");
//        guardar(false);
//        nuevo = false;
//    }
//
//    @Override
//    public void seleccionar(ComprobanteProduccion entidad) {
//
//        comprobante = entidad;
//        nuevo = false;
//        
//    }
//
//    @Override
//    public void imprimirLista(){
//
//    }
//
//    @Override
//    public void imprimirEntidad(){
//        
//    }
//
//    public void limipiarSector(String tipoDeposito){
//
//        //Por ahora dejamos siempre los sectores cero por defecto
//        try {
//            if (tipoDeposito.equals("I") && comprobante.getDepositoIngreso() != null) {
//
//                comprobante.setSectorIngreso(sectorRN.getSector(comprobante.getDepositoIngreso().getDeposi(), "0"));
//            }
//
//            if (tipoDeposito.equals("E") && comprobante.getDepositoEgreso() != null) {
//
//                comprobante.setSectorEgreso(sectorRN.getSector(comprobante.getDepositoEgreso().getDeposi(), "0"));
//            }
//        } catch (Exception e) {
//
//            JsfUtil.addErrorMessage("No es posible limpiar el sector del deposito");
//        }
//    }
//
//    public ComprobanteProduccion getComprobante() {
//        return comprobante;
//    }
//
//    public void setComprobante(ComprobanteProduccion comprobante) {
//        this.comprobante = comprobante;
//    }
//
//    public List<ComprobanteProduccion> getLista() {
//        return lista;
//    }
//
//    public void setLista(List<ComprobanteProduccion> lista) {
//        this.lista = lista;
//    }
//
//    @Override
//    public String verEntidad(ComprobanteProduccion entidad) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    
//}
