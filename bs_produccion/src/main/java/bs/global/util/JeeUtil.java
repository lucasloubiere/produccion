package bs.global.util;



import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JeeUtil implements Serializable{
    
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
        
    public static String digitoVerificador(String codigo){
        int nPar   = 0;
        int nImpar = 0;
        int nTotal = 0;
        
        char[] c = codigo.toCharArray();
        
        for(int i=0;i<(c.length-1) ;i++){
            
            int valor = Integer.parseInt(String.valueOf(c[i]));
            
            if (valor % 2 == 0 ){                
                nPar = nPar + valor;
            }else{                
                nImpar = nImpar + valor;
            }                
        }
        
        nImpar = nImpar*3;        
        nTotal = nPar + nImpar;
        
        int digito = 10 - (nTotal % 10);
        
        if (digito == 10) {
            digito = 0;
        }
        
        return String.valueOf(digito);
    }
    
    public static String getCadenaAlfanumAleatoria (int longitud){
        String cadenaAleatoria = "";
        long milis = new java.util.GregorianCalendar().getTimeInMillis();
        Random r = new Random(milis);
        int i = 0;
        while ( i <= longitud){
            char c = (char)r.nextInt(255);
            if ( (c >= '0' && c <='9') || (c >='A' && c <='Z') ){
                cadenaAleatoria += c;
                i ++;
            }
        }
        return cadenaAleatoria;
    }
    
    
    public static String generarEnLetras(BigDecimal importe) {
        
//        NumeroToLetras nl = new NumeroToLetras();        
//        String enLetrasEntero = nl.convertirLetras(importe);
                
//        return " "+ enLetrasEntero;        
        return "";
    }
    
    
 
    /**
     * Validate given email with regular expression.
     * 
     * @param email
     *            email for validation
     * @return true valid email, otherwise false
     */
    public static boolean validateEmail(String email) {
 
        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
 
        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
 
    }

    public static String getFechaSQL(Date fechaMovimientoDesde) {
        
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");        
        return "'"+sdf.format(fechaMovimientoDesde)+"'";
    
    }
    
    public static String getFechaWS(Date fechaMovimientoDesde) {
        
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("ddMMyyyy");        
        return sdf.format(fechaMovimientoDesde);
    
    }
    
    public static Date getFechaYHora(Date fechaMovimiento, String sHora) {
        
        try {
            
            if(sHora==null || sHora.isEmpty()){
                sHora = "00:00";
            }
            
            DateFormat sdFecha = new java.text.SimpleDateFormat("yyyy-MM-dd");
            
            String sFecha = sdFecha.format(fechaMovimiento);            
            String sFechaFinal = sFecha +" "+ sHora;
            
            DateFormat sdHora = new java.text.SimpleDateFormat("yyyy-MM-dd kk:mm");            
            return sdHora.parse(sFechaFinal);
                    
        } catch (ParseException ex) {
            
            return new Date();
        }
    
    }    
    
    public static Map<String,String> getMeses(){
    
        Map<String,String>  meses = new HashMap<String,String>();
        
        meses.put("1", "Enero");
        meses.put("2", "Enero");
        meses.put("3", "Enero");
        meses.put("4", "Enero");
        meses.put("5", "Enero");
        meses.put("6", "Enero");
        meses.put("7", "Enero");
        meses.put("8", "Enero");
        meses.put("9", "Enero");
        meses.put("10", "Enero");
        meses.put("11", "Enero");
        meses.put("12", "Enero");
        
        return meses;
        
    }
    
    
    public static Map<String,String> getAnios(){
        
        Map<String,String>  anios = new HashMap<String,String>();
        
        Calendar c = new GregorianCalendar();
        
        for(int i=2010;i<=c.get(Calendar.YEAR);i++){
            anios.put(String.valueOf(i), String.valueOf(i));
        }
        
        return anios;        
    }
    
    
    
}
