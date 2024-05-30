package com.tatanstudios.astropollococina.adaptadores.ordenes;

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
import com.tatanstudios.astropollococina.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.astropollococina.fragmentos.ordenes.FragmentCategorias;
import com.tatanstudios.astropollococina.modelos.categorias.ModeloCategoriasList;
import com.tatanstudios.astropollococina.network.RetrofitBuilder;

import java.util.List;



public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.MyViewHolder> {

    // carga los servicios por zona

    Context context;
    List<ModeloCategoriasList> modeloTipo;
    FragmentCategorias fTipoServicio;

    RequestOptions opcionesGlide = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public CategoriasAdapter(){}

    public CategoriasAdapter(Context context, List<ModeloCategoriasList> modeloTipo, FragmentCategorias fTipoServicio){
        this.context = context;
        this.modeloTipo = modeloTipo;
        this.fTipoServicio = fTipoServicio;
    }

    @NonNull
    @Override
    public CategoriasAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_categorias, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriasAdapter.MyViewHolder holder, int position) {

        // numero de orden
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


        holder.txtNombre.setText(modeloTipo.get(position).getNombre());
        holder.txtEstado.setText(modeloTipo.get(position).getEstado());


        if(modeloTipo.get(position).getActivo() == 1){
            // letra negra
            holder.txtEstado.setTextColor(Color.parseColor("#000000"));
        }else{
            // letra roja
            holder.txtEstado.setTextColor(Color.parseColor("#C8FF0000"));
        }

        if(modeloTipo.get(position).getUsaHorario() == 1){
            holder.txtHorario.setText(modeloTipo.get(position).getHorario());
            holder.txtHorario.setVisibility(View.VISIBLE);
        }else{
            holder.txtHorario.setText("");
            holder.txtHorario.setVisibility(View.GONE);
        }


        // Redireccionamiento al fragmento correspondiente segun el servicio
        holder.setListener((view, position1) -> {
            int idcategoria = modeloTipo.get(position).getId();
            int checkActivo = modeloTipo.get(position).getActivo();
            fTipoServicio.verOpciones(idcategoria, checkActivo);
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

        TextView txtHorario;


        IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
        }


        public MyViewHolder(View itemView){
            super(itemView);

           imgCategoria = itemView.findViewById(R.id.imgCategoria);
           txtNombre = itemView.findViewById(R.id.txtCategoria);
           txtEstado = itemView.findViewById(R.id.txtCategoria3);
           txtHorario = itemView.findViewById(R.id.txtCategoria4);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
}