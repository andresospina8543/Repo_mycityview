package co.com.mycityview;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
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
import android.widget.ImageButton;
import android.widget.Toast;
import co.com.mycityview.model.routes.RutaGoogleRest;
import co.com.mycityview.model.routes.Step;

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

public class MainActivity extends FragmentActivity {

	private GoogleMap mapa = null;
	ImageButton imageButton;


	public void addListenerOnButton() {

		imageButton = (ImageButton) findViewById(R.id.imageButton1);

		imageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				/*Toast.makeText(MainActivity.this,
						"ImageButton is clicked!", Toast.LENGTH_SHORT).show();*/

				mostrarUbicacion();
			}

		});

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addListenerOnButton();
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

		mapa.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng point) {
				Projection proj = mapa.getProjection();
				Point coord = proj.toScreenLocation(point);

				// Toast.makeText(
				// MainActivity.this,
				// "Click\n" + "Lat: " + point.latitude + "\n" + "Lng: "
				// + point.longitude + "\n" + "X: " + coord.x
				// + " - Y: " + coord.y, Toast.LENGTH_SHORT)
				// .show();

				// mapa.addMarker(new MarkerOptions()
				// .position(new LatLng(point.latitude, point.longitude))
				// .icon(BitmapDescriptorFactory
				// .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			}
		});

		// mapa.setOnMapLongClickListener(new OnMapLongClickListener() {
		// public void onMapLongClick(LatLng point) {
		// // Projection proj = mapa.getProjection();
		// // Point coord = proj.toScreenLocation(point);
		// //
		// // Toast.makeText(
		// // MainActivity.this,
		// // "Click Largo\n" + "Lat: " + point.latitude + "\n"
		// // + "Lng: " + point.longitude + "\n" + "X: "
		// // + coord.x + " - Y: " + coord.y,
		// // Toast.LENGTH_SHORT).show();
		//
		// comenzarLocalizacion();
		// }
		// });

		// mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
		// public boolean onMarkerClick(Marker marker) {
		// Toast.makeText(MainActivity.this,
		// "Marcador pulsado:\n" + marker.getTitle(),
		// Toast.LENGTH_SHORT).show();
		// return false;
		// }
		// });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_marcadores:
			mostrarMarcador(40.5, -3.5);
			break;
		case R.id.menu_lineas:
			mostrarLineas();
			break;
		case R.id.menu_location:
			mostrarUbicacion();
			break;
		case R.id.menu_ruta:
			consultarRuta();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void consultarRuta() {
		TareaRutas tarea = new TareaRutas();
		tarea.execute();

	}

	private class TareaRutas extends AsyncTask<String, Integer, Boolean> {

		RutaGoogleRest ruta;

		@Override
		protected Boolean doInBackground(String... arg0) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet del = new HttpGet(
					"http://maps.googleapis.com/maps/api/directions/json?origin=6.156965,%20-75.603936&destination=6.197671,%20-75.574358");
			del.setHeader("content-type", "application/json");
			try {
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				Gson gson = new GsonBuilder().create();
				ruta = gson.fromJson(respStr, RutaGoogleRest.class);
				// System.out.println(ruta.getStatus());
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
			}
			return null;
		}

		protected void onPostExecute(Boolean result) {
			if (ruta != null) {
				PolylineOptions lineas = new PolylineOptions();
				lineas.add(new LatLng(ruta.getRoutes().get(0).getLegs().get(0).getStart_location().getLat(), ruta.getRoutes().get(0).getLegs().get(0)
						.getStart_location().getLng()));
				for (Step step : ruta.getRoutes().get(0).getLegs().get(0).getSteps()) {
					lineas.add(new LatLng(step.getStart_location().getLat(), step.getStart_location().getLng()));
				}
				lineas.add(new LatLng(ruta.getRoutes().get(0).getLegs().get(0).getEnd_location().getLat(), ruta.getRoutes().get(0).getLegs().get(0)
						.getEnd_location().getLng()));
				lineas.width(8);
				lineas.color(Color.RED);
				mapa.addPolyline(lineas);
			}
		}

	}

	private void mostrarUbicacion() {

		comenzarLocalizacion();
	}

	private void mostrarMarcador(double lat, double lng) {
		mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Pais: Espa�a"));
	}

	private void mostrarLineas() {

		// Dibujo con Lineas

		// PolylineOptions lineas = new PolylineOptions()
		// .add(new LatLng(45.0, -12.0)).add(new LatLng(45.0, 5.0))
		// .add(new LatLng(34.5, 5.0)).add(new LatLng(34.5, -12.0))
		// .add(new LatLng(45.0, -12.0));
		//
		// lineas.width(8);
		// lineas.color(Color.RED);
		//
		// mapa.addPolyline(lineas);

		PolygonOptions rectangulo = new PolygonOptions().add(new LatLng(45.0, -12.0), new LatLng(45.0, 5.0), new LatLng(34.5, 5.0), new LatLng(34.5,
				-12.0), new LatLng(45.0, -12.0));

		rectangulo.strokeWidth(8);
		rectangulo.strokeColor(Color.RED);

		mapa.addPolygon(rectangulo);
	}

	private LocationManager locManager;
	private LocationListener locListener;

	private void comenzarLocalizacion() {

		// Obtenemos una referencia al LocationManager
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Obtenemos la �ltima posici�n conocida
		Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		// Mostramos la �ltima posici�n conocida
		mostrarPosicion(loc);

		// Nos registramos para recibir actualizaciones de la posici�n
		locListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				mostrarPosicion(location);
			}

			public void onProviderDisabled(String provider) {
				Toast.makeText(MainActivity.this, "Provider OFF", Toast.LENGTH_SHORT).show();
			}

			public void onProviderEnabled(String provider) {
				Toast.makeText(MainActivity.this, "Provider ON ", Toast.LENGTH_SHORT).show();
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
				Toast.makeText(MainActivity.this, "Provider Status: " + status, Toast.LENGTH_SHORT).show();
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

	Marker markerPosicion = null;

}
