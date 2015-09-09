package co.com.mycityview.co.com.mycityview.service;


import android.util.Log;


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
import java.util.List;

import co.com.mycityview.model.routes.Location;
import co.com.mycityview.model.routes.OptionItem;
import co.com.mycityview.model.routes.RutaDTO;


/**
 * Created by andres.ospina on 09/09/2015.
 */
public class RutaClienteService {


    public static List<OptionItem> consultarRutasByLocation(Location loc){
        RutaDTO ruta = null;
        OptionItem optItem = null;
        List<OptionItem> options = new ArrayList<OptionItem>();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del = new HttpGet("http://172.32.0.242:8080/mycityviewBE/rest/ruta/consultar?latitud="+loc.getLatitud()+"&longitud="+loc.getLongitud());
        del.setHeader("content-type", "application/json");
        del.setHeader("Authorization", "E666+ra+0+Wm+uiqQKVobgjBOdl5UCbYr3m2VuAJwFTzRYJyLcrCEg==");
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

    public static RutaDTO consultarRutaById(int identificador){
        RutaDTO ruta = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del = new HttpGet("http://172.32.0.242:8080/mycityviewBE/rest/ruta/"+identificador);
        del.setHeader("content-type", "application/json");
        del.setHeader("Authorization", "E666+ra+0+Wm+uiqQKVobgjBOdl5UCbYr3m2VuAJwFTzRYJyLcrCEg==");
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
}
