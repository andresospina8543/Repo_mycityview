package co.com.mycityview.model.routes;

import java.util.List;

public class Leg {
	
	private Location end_location;
	private Location start_location;
	private List<Step> steps;
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
	public List<Step> getSteps() {
		return steps;
	}
	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

}
