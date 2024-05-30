package com.tatanstudios.astropollococina.modelos.ordenes;

import com.google.gson.annotations.SerializedName;

public class ModeloOrdenesList {


    @SerializedName("id")
    public int id;


    @SerializedName("nota_orden")
    public String notaOrden;


    @SerializedName("cliente")
    public String cliente;

    @SerializedName("direccion")
    public String direccion;

    @SerializedName("telefono")
    public String telefono;

    @SerializedName("referencia")
    public String referencia;

    @SerializedName("haycupon")
    public int haycupon;

    @SerializedName("totalformat")
    public String totalFormat;

    @SerializedName("mensaje_cupon")
    public String mensajeCupon;

    @SerializedName("fecha_orden")
    public String fechaOrden;

    @SerializedName("fecha_preparada")
    public String fechaPreparada;

    @SerializedName("fecha_cancelada")
    public String fechaCancelada;

    @SerializedName("estado_cancelada")
    public int estadoCancelada;

    @SerializedName("estado")
    public String estado;


    @SerializedName("haypremio")
    public int hayPremio;

    @SerializedName("textopremio")
    public String textoPremio;


    public int getHayPremio() {
        return hayPremio;
    }

    public String getTextoPremio() {
        return textoPremio;
    }

    public String getReferencia() {
        return referencia;
    }

    public int getEstadoCancelada() {
        return estadoCancelada;
    }

    public String getEstado() {
        return estado;
    }

    public String getFechaCancelada() {
        return fechaCancelada;
    }

    public String getFechaPreparada() {
        return fechaPreparada;
    }

    public int getId() {
        return id;
    }

    public String getNotaOrden() {
        return notaOrden;
    }

    public String getCliente() {
        return cliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getHaycupon() {
        return haycupon;
    }

    public String getTotalFormat() {
        return totalFormat;
    }

    public String getMensajeCupon() {
        return mensajeCupon;
    }

    public String getFechaOrden() {
        return fechaOrden;
    }
}
