package co.com.mycityview.model.routes;

import java.util.List;

public class RutaGoogleRest {

	private List<Ruta> routes;
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Ruta> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Ruta> routes) {
		this.routes = routes;
	}
}
