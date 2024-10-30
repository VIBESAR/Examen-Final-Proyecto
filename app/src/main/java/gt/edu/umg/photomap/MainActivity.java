package gt.edu.umg.photomap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import gt.edu.umg.photomap.Activity1.AdaptadorFotos;
import gt.edu.umg.photomap.Activity1.Foto;
import gt.edu.umg.photomap.Activity2.DetallesFoto;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorFotos adaptadorFotos;
    private List<Foto> listaFotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewGallery);
        listaFotos = new ArrayList<>();
        adaptadorFotos = new AdaptadorFotos(listaFotos);

        // Configurar el RecyclerView para mostrar la galería como una cuadrícula de 2 columnas
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adaptadorFotos);

        Button btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Botón de registro presionado");
                Intent intent = new Intent(MainActivity.this, DetallesFoto.class);
                startActivity(intent);
            }
        });
    }
}
