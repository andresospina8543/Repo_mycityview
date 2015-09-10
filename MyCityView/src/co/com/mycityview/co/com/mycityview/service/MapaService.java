package co.com.mycityview.co.com.mycityview.service;


import android.graphics.Color;
import android.graphics.drawable.Drawable;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.ArrayList;
import java.util.List;

import co.com.mycityview.R;
import co.com.mycityview.model.routes.Location;
import co.com.mycityview.model.routes.OptionItem;
import co.com.mycityview.model.routes.RutaDTO;
import co.com.mycityview.model.routes.RutaGoogleRest;
import co.com.mycityview.model.routes.Step;

/**
 * Created by ANDRESD on 22/08/2015.
 */
public class MapaService {

    /**
     * Obtiene el PolylineOptions a partir de una lista de coordenadas
     * @param ruta
     * @return
     */
    public static PolylineOptions getPolylines(List<Location> ruta){
        PolylineOptions lineas = new PolylineOptions();
        if (ruta != null) {
            for (Location loc : ruta) {
                lineas.add(new LatLng(loc.getLatitud(),loc.getLongitud()));
            }
            lineas.width(8);
            lineas.color(Color.RED);
        }
        return lineas;
    }

    /**
     * Obtiene la lista de coordenadas decodificadas
     * @param encodedPath
     * @return
     */
    public static List<LatLng> decode(final String encodedPath) {
        int len = encodedPath.length();

        // For speed we preallocate to an upper bound on the final length, then
        // truncate the array before returning.
        final List<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;
    }

    /**
     * Adiciona los markers inicio y  fin de una ruta en el mapa
     * @param mapa
     * @param ruta
     */
    public static void addMarkeresPositionRoutes(GoogleMap mapa, RutaDTO ruta) {
        Location startLoc = ruta.getListLocation().get(0);
        Location endLoc = ruta.getListLocation().get(ruta.getListLocation().size()-1);
        mapa.addMarker(new MarkerOptions().position(new LatLng(startLoc.getLatitud(), startLoc.getLongitud())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(endLoc.getLatitud(), endLoc.getLongitud())).icon(BitmapDescriptorFactory.fromResource(R.drawable.meta01)));
    }
}
