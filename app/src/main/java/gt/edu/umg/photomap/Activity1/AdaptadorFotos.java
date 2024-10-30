package gt.edu.umg.photomap.Activity1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import gt.edu.umg.photomap.R;

public class AdaptadorFotos extends RecyclerView.Adapter<AdaptadorFotos.FotoViewHolder> {

    private List<Foto> listaFotos;

    public AdaptadorFotos(List<Foto> listaFotos) {
        this.listaFotos = listaFotos;
    }

    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_foto, parent, false);
        return new FotoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        Foto foto = listaFotos.get(position);
        holder.descripcionFoto.setText(foto.getDescripcion());
        holder.fechaFoto.setText(foto.getFecha());
        holder.fotoMiniatura.setImageBitmap(foto.getMiniatura());
    }

    @Override
    public int getItemCount() {
        return listaFotos.size();
    }

    static class FotoViewHolder extends RecyclerView.ViewHolder {
        ImageView fotoMiniatura;
        TextView descripcionFoto;
        TextView fechaFoto;

        FotoViewHolder(View itemView) {
            super(itemView);
            fotoMiniatura = itemView.findViewById(R.id.fotoMiniatura);
            descripcionFoto = itemView.findViewById(R.id.descripcionFoto);
            fechaFoto = itemView.findViewById(R.id.fechaFoto);
        }
    }
}
