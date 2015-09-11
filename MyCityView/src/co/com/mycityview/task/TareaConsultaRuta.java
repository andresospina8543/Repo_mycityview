package co.com.mycityview.task;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import co.com.mycityview.MainActivity;
import co.com.mycityview.co.com.mycityview.service.MapaService;
import co.com.mycityview.co.com.mycityview.service.RutaClienteService;
import co.com.mycityview.model.routes.RutaDTO;

/**
 * Created by andres.ospina on 10/09/2015.
 */
public class TareaConsultaRuta extends AsyncTask<String, Integer, Boolean> {

    MainActivity activity;
    RutaDTO ruta = null;
    int identificador;

    PolylineOptions polylineSelected;


    public TareaConsultaRuta(MainActivity activity, int identificador){
        this.identificador = identificador;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... arg0) {
        ruta = RutaClienteService.consultarRutaById(identificador);
        return null;
    }


    protected void onPostExecute(Boolean result) {
        if(ruta != null){
            activity.mapa.clear();
            activity.mapa.addMarker(new MarkerOptions().position(activity.markerPosicion.getPosition()));
            polylineSelected = MapaService.getPolylines(ruta.getListLocation());
            activity.mapa.addPolyline(polylineSelected);
            MapaService.addMarkeresPositionRoutes(activity.mapa,ruta);
        }
    }
}
