package co.com.mycityview.model.routes;

public class Location {
	public Location(String latitud,String longitud) {
		this.latitud = Float.parseFloat(latitud);
		this.longitud = Float.parseFloat(longitud);
	}

	private float latitud;
	private float longitud;

	public float getLatitud() {
		return latitud;
	}

	public Location setLatitud(float latitud) {
		this.latitud = latitud;
		return this;
	}

	public float getLongitud() {
		return longitud;
	}

	public Location setLongitud(float longitud) {
		this.longitud = longitud;
		return this;
	}
}
