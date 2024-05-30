package com.tatanstudios.astropollococina.modelos.categorias;

import com.google.gson.annotations.SerializedName;

public class ModeloCategoriasList {



    @SerializedName("id")
    public int id;


    @SerializedName("nombre")
    public String nombre;

    @SerializedName("activo")
    public int activo;

    @SerializedName("usa_horario")
    public int usaHorario;

    @SerializedName("horario")
    public String horario;

    @SerializedName("imagen")
    public String imagen;

    @SerializedName("estado")
    public String estado;

    public int getUsaHorario() {
        return usaHorario;
    }

    public String getEstado() {
        return estado;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getActivo() {
        return activo;
    }

    public String getHorario() {
        return horario;
    }

    public String getImagen() {
        return imagen;
    }
}
