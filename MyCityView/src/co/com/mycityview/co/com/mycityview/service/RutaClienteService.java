package co.com.mycityview.co.com.mycityview.service;


import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.com.mycityview.model.routes.OptionItem;
import co.com.mycityview.model.routes.RutaDTO;
import co.com.mycityview.security.EncryptClass;


/**
 * Created by andres.ospina on 09/09/2015.
 */
public class RutaClienteService {


    private static final String HOST_BACKEND = "http://jbossews-mycityview.rhcloud.com/";
    private static final String CONTENT_TYPE = "application/json";


    /**
     * Consulta las rutas segun una coordenada seleccionada
     * @param point
     * @return
     */
    public static List<OptionItem> consultarRutasByLocation(LatLng point){
        RutaDTO ruta = null;
        OptionItem optItem = null;
        List<OptionItem> options = new ArrayList<OptionItem>();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del = new HttpGet(HOST_BACKEND+"mycityviewBE/rest/ruta/consultar?latitud="+point.latitude+"&longitud="+point.longitude);
        del.setHeader("content-type", CONTENT_TYPE);
        del.setHeader("Authorization", getAuthorization().trim());
        try {
            HttpResponse resp = httpClient.execute(del);
            if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray jsonArray = new JSONArray(respStr);
                if(jsonArray.length() != 0){
                    for (int i=0; i<jsonArray.length(); i++){
                        Gson gson = new GsonBuilder().create();
                        ruta = gson.fromJson(jsonArray.getJSONObject(i).toString(), RutaDTO.class);
                        optItem = new OptionItem(ruta.getNombreRuta(), ruta.getIdRuta());
                        optItem.setRutaDTO(ruta);
                        options.add(optItem);
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return options;
    }


    /**
     * Consulta los datos de una ruta especifica
     * @param identificador
     * @return
     */
    public static RutaDTO consultarRutaById(int identificador){
        RutaDTO ruta = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del = new HttpGet(HOST_BACKEND+"mycityviewBE/rest/ruta/"+identificador);
        del.setHeader("content-type", CONTENT_TYPE);
        del.setHeader("Authorization", getAuthorization().trim());
        try {
            HttpResponse resp = httpClient.execute(del);
            if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String respStr = EntityUtils.toString(resp.getEntity());
                Gson gson = new GsonBuilder().create();
                ruta = gson.fromJson(respStr, RutaDTO.class);
            }
        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return ruta;
    }

    private static String getAuthorization(){
        EncryptClass encrypt = new EncryptClass("encrip_mycityview");
        return encrypt.encrypt("usuario.rest:ftd*+55qx:"+UtilService.dateToString(new Date(), "dd-MM-yyyy-HH-mm"));
    }
}
