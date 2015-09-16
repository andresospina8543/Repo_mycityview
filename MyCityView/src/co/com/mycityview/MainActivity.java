package co.com.mycityview;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import co.com.mycityview.co.com.mycityview.service.GoogleApiService;
import co.com.mycityview.co.com.mycityview.service.MapaService;
import co.com.mycityview.co.com.mycityview.service.RutaClienteService;
import co.com.mycityview.model.routes.OptionItem;
import co.com.mycityview.model.routes.RutaDTO;
import co.com.mycityview.model.routes.RutaGoogleRest;
import co.com.mycityview.model.routes.Step;
import co.com.mycityview.task.TareaConsultaRuta;
import co.com.mycityview.task.TareaConsultaRutas;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

	public GoogleMap mapa = null;
	ImageButton imageButton;
	public Spinner spinner;
	public List<OptionItem> items;
	private LocationManager locManager;
	private LocationListener locListener;
	private Location actualLocation;
	public Marker markerPosicion = null;
	private ProgressDialog mProgressDialog;

	public void addListenerOnButton() {
		imageButton = (ImageButton) findViewById(R.id.imageButton1);
		imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				comenzarLocalizacion();
			}
		});

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addListenerOnButton();
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

		items = new ArrayList<OptionItem>();

		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

				if (((OptionItem) adapterView.getItemAtPosition(position)).getIcon() != 0) {
					TareaConsultaRuta consultaRuta = new TareaConsultaRuta(MainActivity.this, ((OptionItem) adapterView.getItemAtPosition(position)).getRutaDTO().getIdRuta());
					consultaRuta.execute();
					mostrarPosicion(actualLocation);
				} else {
					mapa.clear();
					mostrarPosicion(actualLocation);
					spinner.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
				//nada
			}
		});
		spinner.setVisibility(View.INVISIBLE);

		mapa.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
			public void onMapLongClick(LatLng point) {
				actualLocation = new Location("click");
				actualLocation.setLatitude(point.latitude);
				actualLocation.setLongitude(point.longitude);
				TareaConsultaRutas tareaConsultaRutas = new TareaConsultaRutas(MainActivity.this, point);
				tareaConsultaRutas.execute();
				mostrarPosicion(actualLocation);
			}
		});
	}

	private void comenzarLocalizacion() {

		// Obtenemos una referencia al LocationManager
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Obtenemos la ultima posicion conocida
		Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		// Mostramos la ultima posicion conocida
		mostrarPosicion(loc);

		// Nos registramos para recibir actualizaciones de la posicion
		locListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				//mostrarPosicion(location);
			}

			public void onProviderDisabled(String provider) {
				//Toast.makeText(MainActivity.this, "Provider OFF", Toast.LENGTH_SHORT).show();
			}

			public void onProviderEnabled(String provider) {
				//Toast.makeText(MainActivity.this, "Provider ON ", Toast.LENGTH_SHORT).show();
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
				//Toast.makeText(MainActivity.this, "Provider Status: " + status, Toast.LENGTH_SHORT).show();
			}
		};

		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locListener);
	}

	private void mostrarPosicion(Location loc) {
		if (loc != null) {
			if (markerPosicion != null) {
				markerPosicion.remove();
			}

			markerPosicion = mapa.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude())).title(
					"Precision: " + loc.getAccuracy()));
			mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(),loc.getLongitude()), 14.0f));

		} else {
			Toast.makeText(MainActivity.this, "Sin datos", Toast.LENGTH_SHORT).show();
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.salir:
				setResult(RESULT_OK);
				finish();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void showProgressDialog () {

		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			//mProgressDialog.setMessage(messageToShow);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			mProgressDialog.setContentView(R.layout.progressbar);
		}else{
			if(!mProgressDialog.isShowing()){
				mProgressDialog.show();
			}
		}

	}
	public void dismissProgress () {

		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}


}
