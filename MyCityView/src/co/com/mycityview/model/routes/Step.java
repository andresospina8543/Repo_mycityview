package co.com.mycityview.model.routes;

public class Step {
	private Location end_location;
	private Location start_location;
	private GooglePolyline polyline;

	public GooglePolyline getPolyline() {
		return polyline;
	}

	public void setPolyline(GooglePolyline polyline) {
		this.polyline = polyline;
	}

	public Location getEnd_location() {
		return end_location;
	}
	public void setEnd_location(Location end_location) {
		this.end_location = end_location;
	}
	public Location getStart_location() {
		return start_location;
	}
	public void setStart_location(Location start_location) {
		this.start_location = start_location;
	}
	
}
