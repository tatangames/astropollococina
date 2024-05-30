package com.tatanstudios.astropollococina.adaptadores.ordenes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.astropollococina.R;
import com.tatanstudios.astropollococina.activitys.ordenes.EstadoFinalizarOrdenActivity;
import com.tatanstudios.astropollococina.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.astropollococina.modelos.productos.ModeloProductoList;
import com.tatanstudios.astropollococina.network.RetrofitBuilder;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductoFinalizarOrdenAdapter extends RecyclerView.Adapter<ProductoFinalizarOrdenAdapter.MyViewHolder>  {

    Context context;
    public ArrayList<ModeloProductoList> modeloTipo;
    EstadoFinalizarOrdenActivity estadoFinalizarOrdenActivity;

    RequestOptions opcionesGlide = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public ProductoFinalizarOrdenAdapter(){}

    public ProductoFinalizarOrdenAdapter(Context context, ArrayList<ModeloProductoList>  modeloTipo, EstadoFinalizarOrdenActivity estadoFinalizarOrdenActivity){
        this.context = context;
        this.modeloTipo = modeloTipo;
        this.estadoFinalizarOrdenActivity = estadoFinalizarOrdenActivity;
    }

    @NonNull
    @Override
    public ProductoFinalizarOrdenAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_producto_nueva_orden, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoFinalizarOrdenAdapter.MyViewHolder holder, int position) {

        if(modeloTipo.get(position).getUtilizaImagen() == 1){
            if(modeloTipo.get(position).getImagen() != null){
                Glide.with(context)
                        .load(RetrofitBuilder.urlImagenes + modeloTipo.get(position).getImagen())
                        .apply(opcionesGlide)
                        .into(holder.imgProducto);
            }else{
                Glide.with(context)
                        .load(R.drawable.camaradefecto)
                        .apply(opcionesGlide)
                        .into(holder.imgProducto);
            }

        }else{
            Glide.with(context)
                    .load(R.drawable.camaradefecto)
                    .apply(opcionesGlide)
                    .into(holder.imgProducto);
        }

        // nombre completo
        holder.txtNombre.setText(modeloTipo.get(position).getNombreproducto());

        // mostrar toda la nota del producto
        if(modeloTipo.get(position).getNota() != null){
            holder.txtNota.setText(modeloTipo.get(position).getNota());
            holder.txtNota.setTextColor(Color.RED);
        }else{
            holder.txtNota.setText("");
            holder.txtNota.setTextColor(Color.BLACK);
        }

        holder.txtPrecio.setText(modeloTipo.get(position).getMultiplicado());
        holder.txtCantidad.setText(modeloTipo.get(position).getCantidad() + "x");

        // buscar menu de este servicio
        holder.setListener((view, position1) -> {
            int id = modeloTipo.get(position).getId(); // id de ordnes_descripcion

            estadoFinalizarOrdenActivity.verProductoIndividual(id);
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

        CircleImageView imgProducto;

        TextView txtNombre;
        TextView txtCantidad;
        TextView txtPrecio;
        TextView txtNota;

        IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }


        public MyViewHolder(View itemView){
            super(itemView);

            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtNombre = itemView.findViewById(R.id.txt);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            txtNota = itemView.findViewById(R.id.txt5);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }


}