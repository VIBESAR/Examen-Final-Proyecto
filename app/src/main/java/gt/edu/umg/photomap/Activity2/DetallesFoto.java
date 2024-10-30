package gt.edu.umg.photomap.Activity2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import gt.edu.umg.photomap.Activity1.AdaptadorFotos;
import gt.edu.umg.photomap.Activity1.Foto;
import gt.edu.umg.photomap.R;

public class DetallesFoto extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    private TextView tvFecha, tvUbicacion;
    private ImageView imageView;
    private Button btnCamara, btnGuardar;
    private FusedLocationProviderClient fusedLocationClient;

    private Bitmap imageBitmap; // Variable de instancia para almacenar la imagen capturada
    private ArrayList<Foto> listaFotos = new ArrayList<>(); // Declaración de la lista para almacenar las fotos
    private AdaptadorFotos adaptadorFotos; // Asegúrate de definir tu adaptador personalizado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalles_foto);

        tvFecha = findViewById(R.id.tvFecha);
        tvUbicacion = findViewById(R.id.tvUbicacion);
        imageView = findViewById(R.id.imageView);
        btnCamara = findViewById(R.id.btnCamara);
        btnGuardar = findViewById(R.id.btnGuardar);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        adaptadorFotos = new AdaptadorFotos(listaFotos); // Inicializar el adaptador

        btnCamara.setOnClickListener(v -> checkCameraPermission());

        btnGuardar.setOnClickListener(v -> {
            String descripcion = ((EditText) findViewById(R.id.editTextDescripcion)).getText().toString();

            // Agrega logs para depurar
            Log.d("DetallesFoto", "Descripción: " + descripcion);
            Log.d("DetallesFoto", "Imagen Bitmap: " + (imageBitmap != null ? "No es nulo" : "Es nulo"));

            if (imageBitmap != null && !descripcion.isEmpty()) {
                Foto nuevaFoto = new Foto(imageBitmap, descripcion, tvFecha.getText().toString());
                listaFotos.add(nuevaFoto);
                adaptadorFotos.notifyDataSetChanged();
                guardarFotoEnGaleria(imageBitmap, "Foto_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()));
                Toast.makeText(this, "Foto guardada en la galería", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Faltan detalles o imagen", Toast.LENGTH_SHORT).show();
            }
        });


        obtenerUbicacion();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            checkStoragePermission();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            abrirCamara();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private void abrirCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data"); // Asigna la imagen capturada a imageBitmap
            imageView.setImageBitmap(imageBitmap);

            // Obtener la fecha y hora actuales
            String fechaActual = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
            tvFecha.setText("Fecha: " + fechaActual);
        }
    }

    private void guardarFotoEnGaleria(Bitmap bitmap, String nombreArchivo) {
        String savedImageURL = MediaStore.Images.Media.insertImage(
                getContentResolver(), bitmap, nombreArchivo, "Foto capturada desde la aplicación");
        if (savedImageURL != null) {
            Toast.makeText(this, "Foto guardada en la galería", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    String ubicacionTexto = "Ubicación: " + location.getLatitude() + ", " + location.getLongitude();
                    tvUbicacion.setText(ubicacionTexto);
                } else {
                    tvUbicacion.setText("Ubicación no disponible");
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkStoragePermission();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara();
            } else {
                Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
