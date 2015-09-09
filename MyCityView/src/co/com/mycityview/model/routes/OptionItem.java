package co.com.mycityview.model.routes;

/**
 * Created by ANDRESD on 23/08/2015.
 */
public class OptionItem {
    public OptionItem(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    private String name;
    private int icon;
    private RutaGoogleRest rutaGoogleRest;
    private RutaDTO rutaDTO;

    public RutaDTO getRutaDTO() {
        return rutaDTO;
    }

    public void setRutaDTO(RutaDTO rutaDTO) {
        this.rutaDTO = rutaDTO;
    }

    public RutaGoogleRest getRutaGoogleRest() {
        return rutaGoogleRest;
    }

    public void setRutaGoogleRest(RutaGoogleRest rutaGoogleRest) {
        this.rutaGoogleRest = rutaGoogleRest;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
