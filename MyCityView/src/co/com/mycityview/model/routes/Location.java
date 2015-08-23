package co.com.mycityview.model.routes;

public class Location {
	public Location(String lat,String lng) {
		this.lng = Float.parseFloat(lng);
		this.lat = Float.parseFloat(lat);
	}

	private float lat;
	private float lng;
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLng() {
		return lng;
	}
	public void setLng(float lng) {
		this.lng = lng;
	}
	
}
