package com.fic.proyecto_desarrollo_movil.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fic.proyecto_desarrollo_movil.Modelos.Reporte;
import com.fic.proyecto_desarrollo_movil.R;

import java.util.List;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ViewHolder> {
    private List<Reporte> listaReportes;

    public ReporteAdapter(List<Reporte> listaReportes) {
        this.listaReportes = listaReportes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mis_reportes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reporte reporte = listaReportes.get(position);
        holder.txtParque.setText(reporte.getNombreParque());
        holder.txtDesc.setText(reporte.getDescripcion());
        holder.txtFecha.setText(reporte.getFecha());
    }

    @Override
    public int getItemCount() { return listaReportes.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtParque, txtDesc, txtFecha;
        public ViewHolder(View itemView) {
            super(itemView);
            txtParque = itemView.findViewById(R.id.tvParques);
            txtDesc = itemView.findViewById(R.id.txtDescripcion);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }
}