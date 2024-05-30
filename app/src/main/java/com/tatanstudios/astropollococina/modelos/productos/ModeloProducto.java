package com.tatanstudios.astropollococina.modelos.productos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModeloProducto {
    @SerializedName("success")
    public Integer success;
    @SerializedName("productos")
    public ArrayList<ModeloProductoList> productos = null;

    public Integer getSuccess() {
        return success;
    }

    public ArrayList<ModeloProductoList> getProductos() {
        return productos;
    }

}
