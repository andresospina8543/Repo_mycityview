package co.com.mycityview.model.routes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres.ospina on 09/09/2015.
 */
public class RutaDTO {
    private int idRuta;
    private String nombreRuta;
    private List<Location> listLocation;

    public int getIdRuta() {
        return idRuta;
    }

    public RutaDTO setIdRuta(int idRuta) {
        this.idRuta = idRuta;
        return this;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public RutaDTO setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
        return this;
    }

    public List<Location> getListLocation() {
        return listLocation;
    }

    public RutaDTO setListLocation(List<Location> listLocation) {
        this.listLocation = listLocation;
        return this;
    }
}
