package com.tatanstudios.astropollococina.network;

import android.content.SharedPreferences;

import com.tatanstudios.astropollococina.modelos.usuario.AccessTokenUser;


public class TokenManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences prefs) {
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences prefs) {
        if (INSTANCE == null) {
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }





    // GUARDAR CLIENTE ID DEL CLIENTE
    public void guardarClienteID(AccessTokenUser token) {
        editor.putString("ID", token.getId()).commit();
    }


    public void deletePreferences(){
        editor.remove("ID").commit();
    }






    public AccessTokenUser getToken(){
        AccessTokenUser token = new AccessTokenUser();

        token.setId(prefs.getString("ID", null));

        return token;
    }

}



