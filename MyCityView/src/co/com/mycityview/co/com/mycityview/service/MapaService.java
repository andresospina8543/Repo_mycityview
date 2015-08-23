package co.com.mycityview.co.com.mycityview.service;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.ArrayList;
import java.util.List;

import co.com.mycityview.model.routes.RutaGoogleRest;
import co.com.mycityview.model.routes.Step;

/**
 * Created by ANDRESD on 22/08/2015.
 */
public class MapaService {

    public static PolylineOptions getPolylinesFromRoute(RutaGoogleRest ruta){
            PolylineOptions lineas = new PolylineOptions();
            if (ruta != null) {

                lineas.add(new LatLng(ruta.getRoutes().get(0).getLegs().get(0).getStart_location().getLat(), ruta.getRoutes().get(0).getLegs().get(0)
                        .getStart_location().getLng()));
                for (Step step : ruta.getRoutes().get(0).getLegs().get(0).getSteps()) {
                    for(LatLng latLng: decode(step.getPolyline().getPoints())){
                        lineas.add(latLng);
                    }
                }
                lineas.add(new LatLng(ruta.getRoutes().get(0).getLegs().get(0).getEnd_location().getLat(), ruta.getRoutes().get(0).getLegs().get(0)
                        .getEnd_location().getLng()));
                lineas.width(8);
                lineas.color(Color.RED);
            }
        return lineas;
    }

    public static PolylineOptions getPolylinesFromRoute2(RutaGoogleRest ruta){
        PolylineOptions lineas = new PolylineOptions();
        if (ruta != null) {
            lineas.add(new LatLng(ruta.getRoutes().get(0).getLegs().get(0).getStart_location().getLat(), ruta.getRoutes().get(0).getLegs().get(0)
                    .getStart_location().getLng()));
            for (Step step : ruta.getRoutes().get(0).getLegs().get(0).getSteps()) {
                lineas.add(new LatLng(step.getStart_location().getLat(), step.getStart_location().getLng()));
            }
            lineas.add(new LatLng(ruta.getRoutes().get(0).getLegs().get(0).getEnd_location().getLat(), ruta.getRoutes().get(0).getLegs().get(0)
                    .getEnd_location().getLng()));
            lineas.width(8);
            lineas.color(Color.RED);
        }
        return lineas;
    }


    private static ArrayList<LatLng> decodePoly(String encoded) {


        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((int) (((double) lat /1E5)* 1E6), (int) (((double) lng/1E5   * 1E6)));
            poly.add(p);
        }


        return poly;
    }


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

}
