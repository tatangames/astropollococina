package com.tatanstudios.astropollococina.modelos.ordenes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloOrdenes {

    @SerializedName("success")
    public Integer success;

    @SerializedName("hayordenes")
    public int hayOrdenes;


    @SerializedName("ordenes")
    public List<ModeloOrdenesList> ordenes = null;


    @SerializedName("total")
    public String total;


    public int getHayOrdenes() {
        return hayOrdenes;
    }

    public String getTotal() {
        return total;
    }

    public Integer getSuccess() {
        return success;
    }

    public List<ModeloOrdenesList> getOrdenes() {
        return ordenes;
    }

}
