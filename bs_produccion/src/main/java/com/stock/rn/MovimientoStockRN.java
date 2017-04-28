/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stock.rn;
///**
// *
// * @author lloubiere
// */
//
//@Stateless
//
//public class MovimientoStockRN  {
//
//   
//    @EJB private MovimientoStockDAO inventarioDAO;
////    @EJB private StockRN stockRN;   
//    @EJB private FormularioRN formularioRN;    
//    @EJB private SucursalRN sucursalRN;
//    
//    @TransactionAttribute(TransactionAttributeType.REQUIRED)
//    public synchronized  void guardar(MovimientoStock m) throws Exception {
//
////        borrarItemsNoValidos(m);
////        generarItemTransferencia(m);
////        asignarDepositoItems(m);
////        asignarCantidadStock(m);
////        
////        //Validamos que se pueda guardar el comprobante
////        controlComprobante(m, false);       
////        generarStock(m);
//        
//        Integer ultimoNumero = formularioRN.tomarProximoNumero(m.getFormulario());        
//        m.setNumeroFormulario(ultimoNumero);
//        
//        inventarioDAO.crear(m);
//        m.setPersistido(true);        
//    }
//
//    /*
//     * Se utiliza para generar movimientos desde el modulo de stock
//     * este metodo incremenda el nro de formulario
//     */
////    public MovimientoStock nuevoMovimiento(String MODST, String CODST, String SUCURS) throws ExcepcionGeneralSistema{
////
////        ComprobanteStock comprobante = comprobanteDAO.getComprobante(MODST,CODST);
////        Sucursal sucursal = sucursalRN.getSucursal(SUCURS);
////        
////        if(comprobante==null) throw new ExcepcionGeneralSistema("No se encontró comprobante de stock " + MODST + "-"+ CODST);
////        if(sucursal==null) throw new ExcepcionGeneralSistema("No se encontró sucursal " + SUCURS);        
////                
////        //Buscamos el formulario correspondiente
////        Formulario formulario = formularioRN.getFormulario(comprobante,sucursal,"X"); 
////        
////        if(formulario==null) throw new ExcepcionGeneralSistema("No se encontró formulario de stock para el comprobante ("+CODST+") "
////                + "para la sucursal ("+SUCURS+") "
////                + "con la condición de iva (X) ");
////        
////        MovimientoStock m = crearMovimiento(comprobante,formulario,sucursal);
////        
////        return m;
////    }
//
//    /**
//     * Se utiliza para generar movimientos de stock automáticos desde otros módulos
//     * @param comprobante
//     * @param formulario
//     * @param sucursal
//     * @return
//     * @throws ExcepcionGeneralSistema
//     */
//    public MovimientoStock nuevoMovimientoAutomatico(ComprobanteStock comprobante, Formulario formulario, Sucursal sucursal) throws ExcepcionGeneralSistema{
//
////        return crearMovimiento(comprobante, formulario, sucursal);
////    }
//
//    /**
//     * Se utiliza para generar movimientos de stock automáticos
//     * @param comprobante objeto comprobante a generar
//     * @param formulario obejeto formulario a generar
//     * @return movimiento de stock
//     * @throws ExcepcionGeneralSistema
//     */
////    private MovimientoStock crearMovimiento(ComprobanteStock comprobante, Formulario formulario,Sucursal sucursal) throws ExcepcionGeneralSistema{
////        
////        if(sucursal==null){
////            throw new ExcepcionGeneralSistema("La sucursal no puede ser nula");
////        }        
////        
////        if(comprobante==null){
////            throw new ExcepcionGeneralSistema("El comprobante de stock no puede ser nulo");
////        }        
////        
////        if(comprobante.getTipoMovimiento()==null){
////            throw new ExcepcionGeneralSistema("El comprobante no tiene definido el tipo de movimiento de inventario");
////        }
////        
////        if(formulario==null){
////            throw new ExcepcionGeneralSistema("El formulario de stock no puede ser nulo");
////        }        
////        
////        MovimientoStock m = new MovimientoStock();        
////        Moneda moneda = monedaRN.getMoneda("USD");
////        BigDecimal cotizacion  = monedaRN.getCotizacionDia("USD");
////
////        m.setPersistido(false);
////        m.setFormulario(formulario);
////        m.setComprobante(comprobante);
////        m.setNumeroFormulario(formulario.getUltimoNumero()+1);        
////        m.setFechaMovimiento(new Date());
////        m.setSucursal(sucursal);
////        m.setTipoMovimiento(comprobante.getTipoMovimiento());
////        m.setMonedaSecundaria(moneda);
////        m.setMonedaRegistracion(monedaRN.getMoneda("ARS"));
////        m.setCotizacion(cotizacion);
////
////        if(comprobante.getDeposito()!=null ){
////            m.setDeposito(comprobante.getDeposito());            
////        }
////
////        if(comprobante.getDepositoTransferencia()!=null ){
////            m.setDepositoTransferencia(comprobante.getDepositoTransferencia());            
////        }
////        
////        //Generamos el item producto vacío
////        m.setItemsProducto(new ArrayList<ItemProductoStock>());
////        m.getItemsProducto().add(nuevoItemProducto(m));        
////        
////        return m;
////    }
//
////    public ItemProductoStock nuevoItemProducto(MovimientoStock m){
////
////        return (ItemProductoStock) nuevoItemMovimiento(TipoItemMovimiento.P, m );
////
////    }
////
////     public ItemTransferenciaStock nuevoItemTransferencia(MovimientoStock m){
////
////         return (ItemTransferenciaStock) nuevoItemMovimiento(TipoItemMovimiento.T, m );
////
////    }
////
////    private ItemMovimientoStock nuevoItemMovimiento(TipoItemMovimiento ti,  MovimientoStock m){
////
////        ItemMovimientoStock nItem;
////
////        if(ti.equals(TipoItemMovimiento.P)){
////            nItem = new ItemProductoStock();
////        }else{
////            nItem = new ItemTransferenciaStock();
////        }        
////        
////        nItem.setNroitm(m.getItemsProducto().size()+1);
////        nItem.setSucursal(m.getSucursal().getCodigo());
////        nItem.setFechaMovimiento(m.getFechaMovimiento());
////        nItem.setMonedaSecundaria(m.getMonedaSecundaria());
////        nItem.setCotizacion(m.getCotizacion());
////        
////        nItem.setAtributo1(m.getAtributo1()!=null?m.getAtributo1():"");
////        nItem.setAtributo2(m.getAtributo2()!=null?m.getAtributo2():"");
////        nItem.setAtributo3(m.getAtributo3()!=null?m.getAtributo3():"");
////        nItem.setAtributo4(m.getAtributo4()!=null?m.getAtributo4():"");
////        nItem.setAtributo5(m.getAtributo5()!=null?m.getAtributo5():"");
////        nItem.setAtributo6(m.getAtributo6()!=null?m.getAtributo6():"");
////        nItem.setAtributo7(m.getAtributo7()!=null?m.getAtributo7():"");
////              
////        nItem.setMovimiento(m);
////
////        return nItem;
////    }
//
//    
//    /**
//     * Validaciones previas a guardar el movimiento
//     * @param m Movimiento de stock
//     * @param permiteVacio permite guardar comprobante vacío
//     * @throws bs.global.excepciones.ExcepcionGeneralSistema
//     */
//    
//    
////    public void controlComprobante(MovimientoStock m, boolean permiteVacio) throws ExcepcionGeneralSistema {
//
////        if(m.getId()!=null){
////            throw  new ExcepcionGeneralSistema("No es posible modificar un comprobante de stock");                        
////        }
////
////        if(!permiteVacio && m.getItemsProducto().isEmpty()){
////            throw new ExcepcionGeneralSistema("El detalle está vacío, no es posible guardar el comprobante de stock");
////        }   
////        
////        //Verificamos que el deposito ingreso siempre esté cargado
////        if(m.getDeposito()==null){
////            throw new ExcepcionGeneralSistema("El depósito no puede ser nulo");
////        }
////
////        //Si es transferencia el deposito de egreso tiene que estar cargado
////        if(m.getTipoMovimiento().equals("T")){
////
////            if(m.getDepositoTransferencia()==null){
////                throw new ExcepcionGeneralSistema("El depósito para transferencia no puede ser nulo");
////            }
////            
////            if(m.getDeposito().equals(m.getDepositoTransferencia())){
////                throw new ExcepcionGeneralSistema("El depósito de egreso y de ingreso no pueden ser iguales");
////            }
////        }         
//////        
//////        if(m.getFechaMovimiento()==null){
//////            throw new ExcepcionGeneralSistema("La fecha no puede estar en blanco");
//////        }
////        
////        if(m.getSucursal()==null){
////            throw new ExcepcionGeneralSistema("La sucursal no puede estar en blanco");
////        }
////        
//////        controlItemsProducto(m);
////        
////    }
//    
////    public void puedoAgregarItem(MovimientoStock m,  ItemMovimientoStock nItem) throws ExcepcionGeneralSistema{   
//        
//        if(nItem==null){
//            throw new ExcepcionGeneralSistema("No se ha creado el item");            
//        }
//        
//        if(m.getDeposito()== null){
//            throw new ExcepcionGeneralSistema("Seleccione el depósito");                
//        }
//        
//        if(nItem.getProducto()==null){
//            throw new ExcepcionGeneralSistema("Seleccione un producto para agregar al comprobante");            
//        }
//        
//        if(m.getTipoMovimiento().equals("I")
//                ||m.getTipoMovimiento().equals("E")
//                ||m.getTipoMovimiento().equals("T")){
//            
//            if(nItem.getCantidad()==null || nItem.getCantidad().compareTo(BigDecimal.ZERO)<=0){
//                throw new ExcepcionGeneralSistema("Ingrese un valor de cantidad mayor a 0 para " + nItem.getProducto().getDescripcion() );                
//            }              
//        }
//        
//        //Si es ajuste solamente validamos que sea distinto de cero
//        if(m.getTipoMovimiento().equals("A")){
//          
//            if(nItem.getCantidad()==null || nItem.getCantidad().compareTo(BigDecimal.ZERO)==0){
//                throw new ExcepcionGeneralSistema("Ingrese un valor de cantidad distinto a 0 para "+ nItem.getProducto().getDescripcion());                
//            }            
//        }
//        
//        if(m.getTipoMovimiento().equals("T")){
//            
//            if(m.getDepositoTransferencia() == null){
//                throw new ExcepcionGeneralSistema("Seleccione el depósito de egreso");                
//            }            
//        }
//         
//        //Control de atributos de stock
////        if(nItem.getProducto()!=null 
////                && nItem.getProducto().getAdministraAtributo1().equals("S")
////                && nItem.getAtributo1().isEmpty()){
////            
////                throw new ExcepcionGeneralSistema("Ingrese el atributo 1 para el producto "+ nItem.getProducto().getDescripcion());                          
////        }  
////        
////        if(nItem.getProducto()!=null 
////                && nItem.getProducto().getAdministraAtributo2().equals("S")
////                && nItem.getAtributo2().isEmpty()){
////            
////                throw new ExcepcionGeneralSistema("Ingrese el atributo 2 para el producto "+ nItem.getProducto().getDescripcion());                          
////        }  
////        
////        if(nItem.getProducto()!=null 
////                && nItem.getProducto().getAdministraAtributo3().equals("S")
////                && nItem.getAtributo3().isEmpty()){
////            
////                throw new ExcepcionGeneralSistema("Ingrese el atributo 3 para el producto "+ nItem.getProducto().getDescripcion());                          
////        }  
////        
////        if(nItem.getProducto()!=null 
////                && nItem.getProducto().getAdministraAtributo4().equals("S")
////                && nItem.getAtributo4().isEmpty()){
////            
////                throw new ExcepcionGeneralSistema("Ingrese el atributo 4 para el producto "+ nItem.getProducto().getDescripcion());                          
////        }  
////        
////        if(nItem.getProducto()!=null 
////                && nItem.getProducto().getAdministraAtributo5().equals("S")
////                && nItem.getAtributo5().isEmpty()){
////            
////                throw new ExcepcionGeneralSistema("Ingrese el atributo 5 para el producto "+ nItem.getProducto().getDescripcion());                          
////        }  
////        
////        if(nItem.getProducto()!=null 
////                && nItem.getProducto().getAdministraAtributo6().equals("S")
////                && nItem.getAtributo6().isEmpty()){
////            
////                throw new ExcepcionGeneralSistema("Ingrese el atributo 6 para el producto "+ nItem.getProducto().getDescripcion());                          
////        }  
////        
////        if(nItem.getProducto()!=null 
////                && nItem.getProducto().getAdministraAtributo7().equals("S")
////                && nItem.getAtributo7().isEmpty()){
////            
////                throw new ExcepcionGeneralSistema("Ingrese el atributo 7 para el producto "+ nItem.getProducto().getDescripcion());                          
////        }  
////        
////        for(ItemProductoStock ip: m.getItemsProducto()){
////
////            if(ip.getProducto()!=null 
////                    && ip.getProducto().equals(nItem.getProducto())
////                    && ip.getAtributo1().equals(nItem.getAtributo1())
////                    && ip.getAtributo2().equals(nItem.getAtributo2())
////                    && ip.getAtributo3().equals(nItem.getAtributo3())
////                    && ip.getAtributo4().equals(nItem.getAtributo4())
////                    && ip.getAtributo5().equals(nItem.getAtributo5())
////                    && ip.getAtributo6().equals(nItem.getAtributo6())
////                    && ip.getAtributo7().equals(nItem.getAtributo7())                    
////                    && ip.isTodoOk()){
////                throw new ExcepcionGeneralSistema("El producto "+nItem.getProducto().getDescripcion()+" ya existe");                  
////            }
//            
////            if(!ip.getProducto().getGestionaStock().equals("S")){
////                throw new ExcepcionGeneralSistema("El producto "+nItem.getProducto().getDescripcion()+" no gestiona stock");                   
////            }
//        }        
//    }
//
////    public void controlItemsProducto(MovimientoStock m) throws ExcepcionGeneralSistema{
////
////
////        for(ItemProductoStock i: m.getItemsProducto()){
////
////            if(i.getCantidad()==null || i.getCantidad().equals(BigDecimal.ZERO)){
////
////                throw new ExcepcionGeneralSistema("Ingrese una valor válido para la cantidad en "+i.getProducto().getDescripcion());
////            }
////
////            // Controlamos el ingreso de los atributos de stock
////            if(i.getProducto()!=null 
////                    && i.getProducto().getAdministraAtributo1().equals("S")
////                    && i.getAtributo1().isEmpty()){
////
////                    throw new ExcepcionGeneralSistema("Ingrese el atributo 1 para el producto "+ i.getProducto().getDescripcion());                          
////            }  
////
////            if(i.getProducto()!=null 
////                    && i.getProducto().getAdministraAtributo2().equals("S")
////                    && i.getAtributo2().isEmpty()){
////
////                    throw new ExcepcionGeneralSistema("Ingrese el atributo 2 para el producto "+ i.getProducto().getDescripcion());                          
////            }  
////
////            if(i.getProducto()!=null 
////                    && i.getProducto().getAdministraAtributo3().equals("S")
////                    && i.getAtributo3().isEmpty()){
////
////                    throw new ExcepcionGeneralSistema("Ingrese el atributo 3 para el producto "+ i.getProducto().getDescripcion());                          
////            }  
////
////            if(i.getProducto()!=null 
////                    && i.getProducto().getAdministraAtributo4().equals("S")
////                    && i.getAtributo4().isEmpty()){
////
////                    throw new ExcepcionGeneralSistema("Ingrese el atributo 4 para el producto "+ i.getProducto().getDescripcion());                          
////            }  
////
////            if(i.getProducto()!=null 
////                    && i.getProducto().getAdministraAtributo5().equals("S")
////                    && i.getAtributo5().isEmpty()){
////
////                    throw new ExcepcionGeneralSistema("Ingrese el atributo 5 para el producto "+ i.getProducto().getDescripcion());                          
////            }  
////
////            if(i.getProducto()!=null 
////                    && i.getProducto().getAdministraAtributo6().equals("S")
////                    && i.getAtributo6().isEmpty()){
////
////                    throw new ExcepcionGeneralSistema("Ingrese el atributo 6 para el producto "+ i.getProducto().getDescripcion());                          
////            }  
////
////            if(i.getProducto()!=null 
////                    && i.getProducto().getAdministraAtributo7().equals("S")
////                    && i.getAtributo7().isEmpty()){
////
////                    throw new ExcepcionGeneralSistema("Ingrese el atributo 7 para el producto "+ i.getProducto().getDescripcion());                          
////            }  
////            
////            if(m.getTipoMovimiento().equals("E")){
////                
////                Stock s = stockRN.nuevoStock(i);
////                //Es un egreso de stock por lo tanto convertimos la cantidad a negativo
////                s.setStocks(s.getStocks().negate());  
////                
////                if(!stockRN.isProductoDisponible(s)){
////    
////                    String mensaje = "Stock suficiente para "
////                            + i.getProducto().getDescripcion() + " | Deposito " + i.getDeposito().getCodigo()
////                            + (i.getProducto().getAdministraAtributo1().equals("S")? "| Atributo1 " + i.getAtributo1() :"")
////                            + (i.getProducto().getAdministraAtributo2().equals("S")? "| Atributo2 " + i.getAtributo2():"")
////                            + (i.getProducto().getAdministraAtributo3().equals("S")? "| Atributo3 " + i.getAtributo3():"")
////                            + (i.getProducto().getAdministraAtributo4().equals("S")? "| Atributo4 " + i.getAtributo4():"")
////                            + (i.getProducto().getAdministraAtributo5().equals("S")? "| Atributo5 " + i.getAtributo5():"")
////                            + (i.getProducto().getAdministraAtributo6().equals("S")? "| Atributo6 " + i.getAtributo6():"")
////                            + (i.getProducto().getAdministraAtributo7().equals("S")? "| Atributo7 " + i.getAtributo7():"");
////
////                    throw new ExcepcionGeneralSistema(mensaje);                            
////
////                }                
////            }
////        }
////
////        if(m.getItemTransferencia()!=null){
////
////            for(ItemTransferenciaStock i: m.getItemTransferencia()){
////
////                Stock s = stockRN.nuevoStock(i);
////                if(!stockRN.isProductoDisponible(s)){
////                     throw new ExcepcionGeneralSistema("No existe stock suficiente para el producto "
////                            + "" + i.getProducto().getDescripcion() + " en el deposito " + i.getDeposito().getCodigo());
////                }                
////            }
////        }
////    }
//
//    /**
//     * Generamos los items de transferencia para registrar la salida del stock.
//     * @param m Movimiento de stock
//     */
////    private void generarItemTransferencia(MovimientoStock m) throws ExcepcionGeneralSistema{
////
////        //Verificamos que se un movimiento de tipo transferencia
////        if(m.getTipoMovimiento().equals("T")){
////            //Generamos la lista vacía
////            m.setItemTransferencia(new ArrayList<ItemTransferenciaStock>());           
////        }else{
////            return;
////        }        
////
////        if(m.getItemsProducto()!=null){
////            
////            for(ItemProductoStock i: m.getItemsProducto()){
////
////                if(i.getProducto()!=null){
////                    ItemTransferenciaStock t = nuevoItemTransferencia(m);
////                    t.setProducto(i.getProducto());  
////                    t.setUnidadMedida(i.getUnidadMedida());                    
////                    t.setPrecio(i.getPrecio());                    
////                    t.setAtributo1(i.getAtributo1());
////                    t.setAtributo2(i.getAtributo2());
////                    t.setAtributo3(i.getAtributo3());
////                    t.setAtributo4(i.getAtributo4());
////                    t.setAtributo5(i.getAtributo5());
////                    t.setAtributo6(i.getAtributo6());
////                    t.setAtributo7(i.getAtributo7());
////                    
////                    if(i.getCantidad()==null){
////                        throw new ExcepcionGeneralSistema("Cantidad en blanco");
////                    }
////
////                    t.setCantidad(i.getCantidad());
////                    t.setStocks(i.getCantidad().negate());
////                    m.getItemTransferencia().add(t);
////                }
////            }
////        }
////    }
////
////
////    /**
////     * Generar los objetos stock donde se almacena el stock de los productos
////     * por deposito, fecha, etc
////     * @param m Movimiento de stock
////     * @throws Exception
////     */
////    private synchronized void generarStock(MovimientoStock m) throws Exception{
////
////        for(ItemProductoStock i: m.getItemsProducto()){
////           
////            Stock nStock = stockRN.nuevoStock(i);
////            stockRN.guardar(nStock);
////        }
////
////        if(m.getItemTransferencia()!=null){
////
////            for(ItemTransferenciaStock i: m.getItemTransferencia()){
////
////                Stock nStock = stockRN.nuevoStock(i);
////                stockRN.guardar(nStock);
////            }
////        }
////    }
////
////    private void asignarDepositoItems(MovimientoStock m) {
////
////        if(m.getItemsProducto()!=null){
////            
////            for(ItemMovimientoStock i: m.getItemsProducto()){
////                
////                i.setDeposito(m.getDeposito());                                    
////            }
////        }
////        
////        //Aplicamos el deposito a los items de transferencia
////        if(m.getTipoMovimiento().equals("T")){
////
////            if(m.getItemTransferencia()!=null){
////                for(ItemMovimientoStock i: m.getItemTransferencia()){
////                    i.setDeposito(m.getDepositoTransferencia());
////                }
////            }
////        }
////    }
////    
////    private void asignarCantidadStock(MovimientoStock m) {
////
////        if(m.getItemsProducto()==null) return;
////                    
////        for(ItemMovimientoStock i: m.getItemsProducto()){
////                
////            //Si es un egreso actualizamos el stock en negativo
////            if(m.getTipoMovimiento().equals("E")){
////                i.setStocks(i.getCantidad().negate());                
////            }else{
////                i.setStocks(i.getCantidad());                   
////            } 
////        }
////        
////        if(m.getItemTransferencia()!=null){
////            for(ItemMovimientoStock i: m.getItemTransferencia()){
////                i.setStocks(i.getCantidad().negate());
////            }
////        }
////    }
////    
////    
////    /**
////     * Borramos de la lista los items que no son válidos para guardar y que
////     * pudieran generar errores
////     * @param m Movimiento de Stock
////     */
////    private void borrarItemsNoValidos(MovimientoStock m) {
////
////        if(m.getItemsProducto()==null){
////            return;
////        }
////        
////        String indiceBorrar[] = new String[m.getItemsProducto().size()];
////        
////        //Seteamos los valores en -1
////        for(int i=0;i<indiceBorrar.length;i++){
////            indiceBorrar[i] = "N";
////        }
////        
////        for(int i = 0 ; i < m.getItemsProducto().size(); i++ ){ 
////                
////            ItemProductoStock im = m.getItemsProducto().get(i);
////            
////            if(im.getProducto()==null){
////                indiceBorrar[i] = "S" ;                
////                continue;
////            }
////            
////            if(!im.isTodoOk()){
////                indiceBorrar[i] = "S" ;                
////            }
////        }
////        
////        for(int i=0;i<indiceBorrar.length;i++){            
////            if(indiceBorrar[i].equals("S")){
////                ItemProductoStock remove = m.getItemsProducto().remove(i);                
////            }            
////        }            
////        
////    }
////
////    /**
////     * Eliminar un item de un movimiento
////     * @param m movimiento del cual se eliminará el item
////     * @param nItem item a eliminar
////     * @return éxito si la eliminación fue exitosa
////     */
////    public boolean eliminarItemProducto(MovimientoStock m,  ItemMovimientoStock nItem){
////
////        if(m.getId()!=null){
////            return false;
////        }
////        
////        boolean fItemBorrado = false;
////        int i = 0;
////        int indiceItemProducto = -1;
////        
////        for(ItemProductoStock ip: m.getItemsProducto()){
////
////            if(ip.getProducto()!=null){
////
////                if(ip.getProducto().equals(nItem.getProducto()) && ip.getCantidad().equals(nItem.getCantidad())){
////                    indiceItemProducto = i;
////                }
////            }
////            i++;
////        }
////
////        //Borramos los items productos
////        if(indiceItemProducto>=0){
////            ItemProductoStock remove = m.getItemsProducto().remove(indiceItemProducto);
////            if(remove!=null){
////                fItemBorrado = true;
////            }
////        }
////
////
////        return fItemBorrado;
////    }   
////
////    public MovimientoStock generarComprobante(MovimientoFacturacion mf) throws ExcepcionGeneralSistema, Exception {
////        
////        Formulario formulario = formularioRN.getFormulario(mf.getComprobanteStock(),mf.getSucursal(),"X"); 
////        
////        MovimientoStock ms = crearMovimiento(mf.getComprobanteStock(),formulario,mf.getSucursal());
////        
////        ms.setFechaMovimiento(mf.getFechaMovimiento()); 
////        ms.setMonedaSecundaria(mf.getMonedaSecundaria());
////        ms.setMonedaRegistracion(mf.getMonedaRegistracion());
////        ms.setCotizacion(mf.getCotizacion());                
////        ms.setEntidadComercial(mf.getCliente());
////        
////        ms.getItemsProducto().clear();
//                
//        for(ItemProductoFacturacion ipf: mf.getItemsProducto()){
//                        
//            if(ipf.getProducto()!=null 
//                    && ipf.getProducto().getTipoProducto().getGestionaStock().equals("S")
//                    && ipf.getProducto().getGestionaStock().equals("S")){
//                                
//                ItemProductoStock ips = nuevoItemProducto(ms);
//                ips.setProducto(ipf.getProducto());
//                ips.setObservaciones(ipf.getObservaciones());
//                               
//                ips.setAtributo1(ipf.getAtributo1()!=null? ipf.getAtributo1():"");
//                ips.setAtributo2(ipf.getAtributo2()!=null? ipf.getAtributo2():"");
//                ips.setAtributo3(ipf.getAtributo3()!=null? ipf.getAtributo3():"");
//                ips.setAtributo4(ipf.getAtributo4()!=null? ipf.getAtributo4():"");
//                ips.setAtributo5(ipf.getAtributo5()!=null? ipf.getAtributo5():"");
//                ips.setAtributo6(ipf.getAtributo6()!=null? ipf.getAtributo6():"");
//                ips.setAtributo7(ipf.getAtributo7()!=null? ipf.getAtributo7():"");
//                
//                ips.setCantidad(ipf.getCantidad());
//                ips.setUnidadMedida(ipf.getProducto().getUnidadDeMedida());                        
//                ips.setPrecio(ipf.getProducto().getPrecioReposicion());
//                ips.setTodoOk(true);
//                
//                ips.setMovimiento(ms);
//                ms.getItemsProducto().add(ips);
//            }
//        } 
//        
//        borrarItemsNoValidos(ms);
//        generarItemTransferencia(ms);
//        asignarDepositoItems(ms);   
//        asignarCantidadStock(ms);
//        controlComprobante(ms,true);       
//        generarStock(ms);
//        
////        if(ms.getItemsProducto().isEmpty()){
////            ms = null;
////        }   
//        
//        return ms;
//        
//    }
//    
//    /**
//     * 
//     * @param mc Movimiento de compras a partir del cual se genera el movimiento de stock
//     * @return Movimiento de stock generado
//     * @throws ExcepcionGeneralSistema
//     * @throws Exception 
//     */
//    public MovimientoStock generarComprobante(MovimientoCompra mc) throws ExcepcionGeneralSistema, Exception {
//        
//        Formulario formulario = formularioRN.getFormulario(mc.getComprobanteStock(),mc.getSucursal(),"X"); 
//        
//        MovimientoStock ms = crearMovimiento(mc.getComprobanteStock(),formulario,mc.getSucursal());
//        
//        ms.setFechaMovimiento(mc.getFechaMovimiento()); 
//        ms.setMonedaSecundaria(mc.getMonedaSecundaria());
//        ms.setMonedaRegistracion(mc.getMonedaRegistracion());
//        
//        ms.setCotizacion(mc.getCotizacion());                
//        ms.setEntidadComercial(mc.getProveedor());
//        
//        ms.getItemsProducto().clear();
//                
//        for(ItemProductoCompra ipc: mc.getItemsProducto()){
//                        
//            if(ipc.getProducto()!=null 
//                    && ipc.getProducto().getTipoProducto().getGestionaStock().equals("S")
//                    && ipc.getProducto().getGestionaStock().equals("S")){
//                                
//                for(DetalleItemMovimientoCompra idc:ipc.getItemsDetalle()){
//                    
//                    ItemProductoStock ips = nuevoItemProducto(ms);
//                    ips.setProducto(idc.getProducto());
//                    ips.setObservaciones(ipc.getObservaciones());                             
//                    
//                    ips.setAtributo1(ipc.getAtributo1()!=null? idc.getAtributo1():"");
//                    ips.setAtributo2(ipc.getAtributo2()!=null? idc.getAtributo2():"");
//                    ips.setAtributo3(ipc.getAtributo3()!=null? idc.getAtributo3():"");
//                    ips.setAtributo4(ipc.getAtributo4()!=null? idc.getAtributo4():"");
//                    ips.setAtributo5(ipc.getAtributo5()!=null? idc.getAtributo5():"");
//                    ips.setAtributo6(ipc.getAtributo6()!=null? idc.getAtributo6():"");
//                    ips.setAtributo7(ipc.getAtributo7()!=null? idc.getAtributo7():"");
//                    
//                    ips.setCantidad(idc.getCantidad());
//                    ips.setUnidadMedida(idc.getUnidadMedida());                        
//                    ips.setPrecio(idc.getProducto().getPrecioReposicion());
//                    ips.setTodoOk(true);
//
//                    ips.setMovimiento(ms);
//                    ms.getItemsProducto().add(ips);
//                }
//            }
//        } 
//        
//        borrarItemsNoValidos(ms);
//        generarItemTransferencia(ms);
//        asignarDepositoItems(ms);  
//        asignarCantidadStock(ms);
//        controlComprobante(ms,true);       
//        generarStock(ms);
//        
//        if(ms.getItemsProducto().isEmpty()){
//            ms = null;
//        }   
//        
//        return ms;
//        
//    }
//
//    public List<MovimientoStock> getListaByBusqueda(Map<String, String> filtro, int cantidadRegistros) {
//        
//        return inventarioDAO.getListaByBusqueda(filtro, cantidadRegistros);  
//    }

//}

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
//}
