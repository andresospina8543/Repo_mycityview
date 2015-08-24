package co.com.mycityview.co.com.mycityview.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import co.com.mycityview.model.routes.Location;
import co.com.mycityview.model.routes.OptionItem;
import co.com.mycityview.model.routes.RutaGoogleRest;

/**
 * Created by ANDRESD on 23/08/2015.
 */
public class GoogleApiService {

    public static List<RutaGoogleRest> getRoutesFromGoogle(){
        List<RutaGoogleRest> routes = new ArrayList<RutaGoogleRest>();
        RutaGoogleRest ruta = null;
        Location[] origen =  new Location[]{new Location("6.156965","-75.603936"), new Location("6.158199","-75.609604")};
        Location[] destino =  new Location[]{new Location("6.197671","-75.574358"), new Location("6.215097","-75.576104")};
        for (int i = 0; i < origen.length; i++) {
            ruta = getRouteByOriginDestin(origen[i],destino[i]);
            if(ruta !=null){
                routes.add(ruta);
            }
        }
        return routes;
    }


    public static List<OptionItem> getRoutesItemsFromGoogle(){
        List<OptionItem> routes = new ArrayList<OptionItem>();
        RutaGoogleRest ruta = null;
        OptionItem optionItem;
        Location[] origen =  new Location[]{new Location("6.156965","-75.603936"), new Location("6.158199","-75.609604")};
        Location[] destino =  new Location[]{new Location("6.197671","-75.574358"), new Location("6.215097","-75.576104")};
        for (int i = 0; i < origen.length; i++) {
            ruta = getRouteByOriginDestin(origen[i],destino[i]);
            if(ruta !=null){
                optionItem = new OptionItem("Ruta "+i+1,i+1);
                optionItem.setRutaGoogleRest(ruta);
                routes.add(optionItem);
            }
        }
        return routes;
    }

    private static RutaGoogleRest getRouteByOriginDestin(Location origin, Location destination){
        RutaGoogleRest ruta = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del = new HttpGet(
                "http://maps.googleapis.com/maps/api/directions/json?origin="+origin.getLat()+",%20"+origin.getLng()+"&destination="+destination.getLat()+",%20"+destination.getLng());
        del.setHeader("content-type", "application/json");
        try {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());
            Gson gson = new GsonBuilder().create();
            ruta = gson.fromJson(respStr, RutaGoogleRest.class);
        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return ruta;
    }

    public static RutaGoogleRest getRouteTest(){
        RutaGoogleRest ruta = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet del = new HttpGet(
                "http://maps.googleapis.com/maps/api/directions/json?origin=6.156965,%20-75.603936&destination=6.197671,%20-75.574358");
        del.setHeader("content-type", "application/json");
        try {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());
            Gson gson = new GsonBuilder().create();
            ruta = gson.fromJson(respStr, RutaGoogleRest.class);
        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
        return ruta;
    }
}
