package com.tatanstudios.astropollococina.activitys.principal;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.navigation.NavigationView;
import com.tatanstudios.astropollococina.R;
import com.tatanstudios.astropollococina.activitys.loginuser.LoginActivity;
import com.tatanstudios.astropollococina.fragmentos.historial.FragmentHistorial;
import com.tatanstudios.astropollococina.fragmentos.notificacion.FragmentNotificacion;
import com.tatanstudios.astropollococina.fragmentos.ordenes.FragmentCanceladasHoy;
import com.tatanstudios.astropollococina.fragmentos.ordenes.FragmentCategorias;
import com.tatanstudios.astropollococina.fragmentos.ordenes.FragmentCompletadasHoy;
import com.tatanstudios.astropollococina.fragmentos.ordenes.FragmentNuevasOrdenes;
import com.tatanstudios.astropollococina.fragmentos.ordenes.FragmentOrdenEnPreparacion;
import com.tatanstudios.astropollococina.network.TokenManager;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView navNombre;
    TokenManager tokenManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        navNombre = headerView.findViewById(R.id.txtNavBar);

        navNombre.setText(getString(R.string.restaurante));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, new FragmentNuevasOrdenes())
                .commit();
    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_ordenes) {
            fragment = new FragmentNuevasOrdenes();
        }
        else if (id == R.id.nav_preparando) {
            fragment = new FragmentOrdenEnPreparacion();
        }
        else if (id == R.id.nav_completadas) {
            fragment = new FragmentCompletadasHoy();
        }
        else if (id == R.id.nav_canceladas) {
            fragment = new FragmentCanceladasHoy();
        }
        else if (id == R.id.nav_categorias) {
            fragment = new FragmentCategorias();
        }

        else if(id == R.id.nav_notificacion){
            fragment = new FragmentNotificacion();
        }

        else if(id == R.id.nav_historial){
            fragment = new FragmentHistorial();
        }

        else if(id == R.id.nav_cerrar){
            cerrar();
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContenedor, fragment)
                    .commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    boolean seguroCerrarSesion = true;

    void cerrar(){
        if(seguroCerrarSesion) {
            seguroCerrarSesion = false;
            KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.WARNING_TYPE);
            pDialog.setTitleText(getString(R.string.cerrar_sesion));
            pDialog.setContentText("");
            pDialog.setConfirmText(getString(R.string.si));
            pDialog.setContentTextSize(16);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.confirmButtonColor(R.drawable.dialogo_theme_success)
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();

                        tokenManager.deletePreferences();
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    });
            pDialog.cancelButtonColor(R.drawable.dialogo_theme_cancel)
                    .setContentTextSize(16)
                    .setCancelText(getString(R.string.no))
                    .setCancelClickListener(kAlertDialog -> {
                        kAlertDialog.dismissWithAnimation();
                        seguroCerrarSesion = true;
                    });
            pDialog.show();
        }
    }





}