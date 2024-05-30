package com.tatanstudios.astropollococina.adaptadores.historial;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.astropollococina.R;
import com.tatanstudios.astropollococina.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.astropollococina.fragmentos.historial.FragmentListaHistorial;
import com.tatanstudios.astropollococina.modelos.ordenes.ModeloOrdenesList;


import java.util.List;


public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.MyViewHolder> {

    // lista de pagos al servicio

    Context context;
    List<ModeloOrdenesList> modeloTipo;
    FragmentListaHistorial fTipoServicio;

    public HistorialAdapter(Context context, List<ModeloOrdenesList> modeloTipo, FragmentListaHistorial fTipoServicio){
        this.context = context;
        this.modeloTipo = modeloTipo;
        this.fTipoServicio = fTipoServicio;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_historial, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // numero de orden
        holder.txtOrdenNum.setText(String.valueOf(modeloTipo.get(position).getId()));
        // fecha de orden
        holder.txtFecha.setText(modeloTipo.get(position).getFechaOrden());
        // venta
        holder.txtVenta.setText(modeloTipo.get(position).getTotalFormat());

        holder.txtEstado.setText(modeloTipo.get(position).getEstado());

        if(modeloTipo.get(position).getEstadoCancelada() == 1){
            holder.txtEstado.setTextColor(Color.parseColor("#C8FF0000"));
        }
        else{
            holder.txtEstado.setTextColor(Color.parseColor("#000000"));
        }

        // DATOS DE CUPONES

        if(modeloTipo.get(position).getHaycupon() == 1){

            holder.txtCupon.setText(modeloTipo.get(position).getMensajeCupon());

            holder.textoCupon.setVisibility(View.VISIBLE);
            holder.txtCupon.setVisibility(View.VISIBLE);
        }else{
            holder.textoCupon.setVisibility(View.GONE);
            holder.txtCupon.setVisibility(View.GONE);
        }


        // DATOS PARA PREMIOS
        if(modeloTipo.get(position).getHayPremio() == 1){

            holder.txtPremio.setText(modeloTipo.get(position).getTextoPremio());

            holder.textoPremio.setVisibility(View.VISIBLE);
            holder.txtPremio.setVisibility(View.VISIBLE);
        }else{
            holder.textoPremio.setVisibility(View.GONE);
            holder.txtPremio.setVisibility(View.GONE);
        }




        // cliente
        holder.txtCliente.setText(modeloTipo.get(position).getCliente());
        // direccion
        holder.txtDireccion.setText(modeloTipo.get(position).getDireccion());

        if(modeloTipo.get(position).getNotaOrden() != null) {
            holder.txtNotaCliente.setText(modeloTipo.get(position).getNotaOrden());

            holder.txtNotaCliente.setVisibility(View.VISIBLE);
        }else{
            holder.txtNotaCliente.setText("");

            holder.textoNota.setVisibility(View.GONE);
            holder.txtNotaCliente.setVisibility(View.GONE);
        }


        // Redireccionamiento al fragmento correspondiente segun el servicio
        holder.setListener((view, position1) -> {
            int pos = modeloTipo.get(position).getId();
            fTipoServicio.abrirOrdenes(pos);
        });
    }

    @Override
    public int getItemCount() {
        if(modeloTipo != null){
            return modeloTipo.size();
        }else{
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView txtOrdenNum;
        TextView txtFecha;
        TextView txtVenta;
        TextView textoCupon;
        TextView txtCupon;
        TextView txtCliente;
        TextView txtDireccion;
        TextView txtNotaCliente;
        TextView textoNota;
        TextView txtEstado;


        TextView textoPremio;
        TextView txtPremio;


        IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }


        public MyViewHolder(View itemView){
            super(itemView);

            txtOrdenNum = itemView.findViewById(R.id.txtOrdenNum);
            txtFecha = itemView.findViewById(R.id.txtDireccion);
            txtVenta = itemView.findViewById(R.id.txtEntrega);
            textoCupon = itemView.findViewById(R.id.txtS4);
            txtCupon = itemView.findViewById(R.id.txtEntrega3);

            txtCliente = itemView.findViewById(R.id.txtTotal);
            txtDireccion = itemView.findViewById(R.id.txtCupon);
            txtNotaCliente = itemView.findViewById(R.id.txtNota);
            textoNota = itemView.findViewById(R.id.txtt4);
            txtEstado = itemView.findViewById(R.id.txtDireccion3);

            textoPremio = itemView.findViewById(R.id.txtS10);
            txtPremio = itemView.findViewById(R.id.txtEntrega7);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}
