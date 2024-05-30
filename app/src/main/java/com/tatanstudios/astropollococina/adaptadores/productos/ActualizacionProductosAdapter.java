package com.tatanstudios.astropollococina.adaptadores.productos;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.astropollococina.R;
import com.tatanstudios.astropollococina.activitys.productos.ActualizacionProductosActivity;
import com.tatanstudios.astropollococina.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.astropollococina.modelos.productos.ModeloProductoList;
import com.tatanstudios.astropollococina.network.RetrofitBuilder;

import java.util.List;


public class ActualizacionProductosAdapter extends RecyclerView.Adapter<ActualizacionProductosAdapter.MyViewHolder> {

    // carga los servicios por zona

    Context context;
    List<ModeloProductoList> modeloTipo;
    ActualizacionProductosActivity fTipoServicio;

    RequestOptions opcionesGlide = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public ActualizacionProductosAdapter(){}

    public ActualizacionProductosAdapter(Context context, List<ModeloProductoList> modeloTipo, ActualizacionProductosActivity fTipoServicio){
        this.context = context;
        this.modeloTipo = modeloTipo;
        this.fTipoServicio = fTipoServicio;
    }

    @NonNull
    @Override
    public ActualizacionProductosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_actualizacion_productos, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActualizacionProductosAdapter.MyViewHolder holder, int position) {


        // numero de orden

        if(modeloTipo.get(position).getUtilizaImagen() == 1){
            if(!modeloTipo.get(position).getImagen().isEmpty()){
                Glide.with(context)
                        .load(RetrofitBuilder.urlImagenes + modeloTipo.get(position).getImagen())
                        .apply(opcionesGlide)
                        .into(holder.imgCategoria);
            }else{
                Glide.with(context)
                        .load(R.drawable.camaradefecto)
                        .apply(opcionesGlide)
                        .into(holder.imgCategoria);
            }
        }else{
            Glide.with(context)
                    .load(R.drawable.camaradefecto)
                    .apply(opcionesGlide)
                    .into(holder.imgCategoria);
        }





        holder.txtNombre.setText(modeloTipo.get(position).getNombre());
        holder.txtEstado.setText(modeloTipo.get(position).getEstado());


        if(modeloTipo.get(position).getActivo() == 1){
            // letra negra
            holder.txtEstado.setTextColor(Color.parseColor("#000000"));
        }else{
            // letra roja
            holder.txtEstado.setTextColor(Color.parseColor("#C8FF0000"));
        }


        // Redireccionamiento al fragmento correspondiente segun el servicio
        holder.setListener((view, position1) -> {
            int idProducto = modeloTipo.get(position).getId();
            int checkActivo = modeloTipo.get(position).getActivo();
            fTipoServicio.verOpciones(idProducto, checkActivo);
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

        ImageView imgCategoria;

        TextView txtNombre;

        TextView txtEstado;

        IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(View itemView){
            super(itemView);

            imgCategoria = itemView.findViewById(R.id.imgCategoria);
            txtNombre = itemView.findViewById(R.id.txtCategoria);
            txtEstado = itemView.findViewById(R.id.txtCategoria3);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}