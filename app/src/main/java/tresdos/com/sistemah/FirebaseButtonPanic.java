package tresdos.com.sistemah;

import java.util.Date;

/**
 * Created by Tresdos on 10/03/2017.
 */

public class FirebaseButtonPanic {
    private String fullName;
    private Date Fecha;
    private String Latitud;
    private String Longitud;
    private String Estado;

    public FirebaseButtonPanic(String nombre, String latitud, String longitud, String estado) {
        this.fullName = nombre;
//        this.Fecha = fecha;
        this.Latitud = latitud;
        this.Longitud = longitud;
        this.Estado = estado;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String nombre) {
        this.fullName = nombre;
    }

    public Date getPhoneNumber() {
        return Fecha;
    }

    public void setPhoneNumber(Date fecha) {
        this.Fecha = fecha;
    }
    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        this.Latitud = latitud;
    }
    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        this.Longitud = longitud;
    }
    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        this.Estado = estado;
    }
}
