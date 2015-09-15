package co.com.mycityview.co.com.mycityview.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by andres.ospina on 15/09/2015.
 */
public class UtilService {

    /**
     * Convierte una fecha a string segun el formato
     * @param date
     * @param formato
     * @return
     */
    public static String dateToString(Date date, String formato) {
        SimpleDateFormat dateFormat  =new SimpleDateFormat(formato, new Locale("es", "CO"));
        return dateFormat.format(date);
    }
}
