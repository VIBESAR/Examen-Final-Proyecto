package gt.edu.umg.photomap.Activity1;

import android.graphics.Bitmap;

public class Foto {
    private Bitmap miniatura;
    private String descripcion;
    private String fecha;

    public Foto(Bitmap miniatura, String descripcion, String fecha) {
        this.miniatura = miniatura;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Bitmap getMiniatura() {
        return miniatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }
}
