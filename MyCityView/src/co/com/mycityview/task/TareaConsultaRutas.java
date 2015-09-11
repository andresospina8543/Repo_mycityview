package co.com.mycityview.task;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import co.com.mycityview.MainActivity;
import co.com.mycityview.OptionListAdapter;
import co.com.mycityview.co.com.mycityview.service.RutaClienteService;
import co.com.mycityview.model.routes.OptionItem;

/**
 * Created by andres.ospina on 10/09/2015.
 */
public class TareaConsultaRutas extends AsyncTask<String, Integer, Boolean> {

    MainActivity activity;
    List<OptionItem> opItems;
    LatLng point;


    public TareaConsultaRutas(MainActivity activity, LatLng point){
        this.activity = activity;
        this.point = point;
    }

    @Override
    protected Boolean doInBackground(String... arg0) {
        opItems = RutaClienteService.consultarRutasByLocation(point);
        return null;
    }


    protected void onPostExecute(Boolean result) {
        if(opItems != null && !opItems.isEmpty()) {
            activity.spinner.setVisibility(View.VISIBLE);
            activity.items.clear();
            activity.items.addAll(opItems);
            activity.items.add(new OptionItem("Clear", 0));
            activity.spinner.setAdapter(new OptionListAdapter(activity.getApplicationContext(), activity.items));
        }else{
            Toast.makeText(activity, "No se pudo obtener el listado de rutas", Toast.LENGTH_SHORT).show();
        }
    }

}
