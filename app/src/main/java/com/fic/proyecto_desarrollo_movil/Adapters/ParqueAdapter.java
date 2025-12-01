package com.fic.proyecto_desarrollo_movil.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fic.proyecto_desarrollo_movil.Modelos.Parque;
import com.fic.proyecto_desarrollo_movil.R;

import java.util.List;

public class ParqueAdapter extends RecyclerView.Adapter<ParqueAdapter.ViewHolder> {

    private List<Parque> lista;

    public ParqueAdapter(List<Parque> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parque, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Parque p = lista.get(position);
        holder.tvNombre.setText(p.getNombre());
        holder.tvDireccion.setText(p.getDireccion());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDireccion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvParques);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
        }
    }
}