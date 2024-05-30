package com.tatanstudios.astropollococina.modelos.productos;

import com.google.gson.annotations.SerializedName;

public class ModeloProductoList {

    @SerializedName("id")
    public Integer id;

    @SerializedName("cantidad")
    public int cantidad;

    @SerializedName("nota")
    public String nota;

    @SerializedName("precio")
    public String precio;

    @SerializedName("nombreproducto")
    public String nombreproducto;

    @SerializedName("utiliza_imagen")
    public int utilizaImagen;

    @SerializedName("imagen")
    public String imagen;

    @SerializedName("descripcion")
    public String descripcion;

    @SerializedName("multiplicado")
    public String multiplicado;

    @SerializedName("activo")
    public int activo;

    @SerializedName("estado")
    public String estado;

    @SerializedName("nombre")
    public String nombre;


    public String getNombre() {
        return nombre;
    }

    public int getActivo() {
        return activo;
    }

    public String getEstado() {
        return estado;
    }

    public Integer getId() {
        return id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getNota() {
        return nota;
    }

    public String getPrecio() {
        return precio;
    }

    public String getNombreproducto() {
        return nombreproducto;
    }

    public int getUtilizaImagen() {
        return utilizaImagen;
    }

    public String getImagen() {
        return imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getMultiplicado() {
        return multiplicado;
    }
}
