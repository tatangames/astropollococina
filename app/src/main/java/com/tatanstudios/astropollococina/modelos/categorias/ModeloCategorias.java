package com.tatanstudios.astropollococina.modelos.categorias;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.astropollococina.modelos.ordenes.ModeloOrdenesList;

import java.util.List;

public class ModeloCategorias {


    @SerializedName("success")
    public Integer success;



    @SerializedName("categorias")
    public List<ModeloCategoriasList> categorias = null;


    public Integer getSuccess() {
        return success;
    }

    public List<ModeloCategoriasList> getCategorias() {
        return categorias;
    }
}
