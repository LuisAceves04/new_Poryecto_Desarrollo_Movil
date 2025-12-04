package com.fic.proyecto_desarrollo_movil.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fic.proyecto_desarrollo_movil.Modelos.Parque;
import com.fic.proyecto_desarrollo_movil.R;
// Importa tu nueva actividad de reporte
import com.fic.proyecto_desarrollo_movil.ui.theme.ReporteActivity;

import java.util.List;

public class ParqueAdapter extends RecyclerView.Adapter<ParqueAdapter.ViewHolder> {

    private List<Parque> lista;
    private Context context;

    public ParqueAdapter(List<Parque> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (context == null) {
            context = parent.getContext();
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parque, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Parque p = lista.get(position);
        holder.tvNombre.setText(p.getNombre());
        holder.tvDireccion.setText(p.getDireccion());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreSeleccionado = p.getNombre();

                Intent intent = new Intent(context, ReporteActivity.class);

                intent.putExtra("PARQUE_SELECCIONADO", nombreSeleccionado);

                context.startActivity(intent);

                Toast.makeText(context, "Reportando sobre: " + nombreSeleccionado, Toast.LENGTH_SHORT).show();
            }
        });
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