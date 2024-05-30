package com.tatanstudios.astropollococina.activitys.productos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatanstudios.astropollococina.R;
import com.tatanstudios.astropollococina.adaptadores.productos.ActualizacionProductosAdapter;
import com.tatanstudios.astropollococina.network.ApiService;
import com.tatanstudios.astropollococina.network.RetrofitBuilder;
import com.tatanstudios.astropollococina.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ActualizacionProductosActivity extends AppCompatActivity {

    RecyclerView recyclerProductos;

    TextView txtToolbar;

    RelativeLayout root;

    int idCategoria = 0;

    ApiService service;
    TokenManager tokenManager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ProgressBar progressBar;

    ActualizacionProductosAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizacion_productos);

        recyclerProductos = findViewById(R.id.recyclerProductos);
        txtToolbar = findViewById(R.id.txtToolbar);
        root = findViewById(R.id.root);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        txtToolbar.setText(getString(R.string.productos));

        Intent intent = getIntent();
        if (intent != null) {
            idCategoria = intent.getIntExtra("KEY_ID", 0);
        }


        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerProductos.setLayoutManager(layoutManager);
        recyclerProductos.setHasFixedSize(true);
        adapter = new ActualizacionProductosAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerProductos.addItemDecoration(dividerItemDecoration);
        recyclerProductos.setAdapter(adapter);


        peticionServidor();
    }


    void peticionServidor(){
        progressBar.setVisibility(View.VISIBLE);

        compositeDisposable.add(
                service.verListadoDeProductosPorCategoria(idCategoria)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {
                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1){
                                            adapter = new ActualizacionProductosAdapter(getApplicationContext(), apiRespuesta.getProductos(), this);
                                            recyclerProductos.setAdapter(adapter);
                                        }else{
                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {
                                    mensajeSinConexion();
                                })
        );
    }


    // ver producto y que descripcion pidio
    public void verOpciones(int idProducto, int valorCheck){

        View vista = View.inflate(this, R.layout.dialog_activar_categoria, null);
        CheckBox checkBox = vista.findViewById(R.id.checkbox);

        if(valorCheck == 1){
            checkBox.setText(getString(R.string.activada));
            checkBox.setChecked(true);
        }else{
            checkBox.setText(R.string.desactivada);
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    checkBox.setText(getString(R.string.activada));
                }else{
                    checkBox.setText(getString(R.string.desactivada));
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.actualizar_producto));
        builder.setView(vista)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.guardar), (dialog, id) -> {

                    int valor = 0;

                    if(checkBox.isChecked()){
                        valor = 1;
                    }

                    peticionActualizarProducto(idProducto, valor);
                })
                .setNegativeButton(getString(R.string.cancelar), (dialog, id) -> dialog.cancel()).show();
    }


    void peticionActualizarProducto(int idproducto, int valor){


        progressBar.setVisibility(View.VISIBLE);

        compositeDisposable.add(
                service.actualizarEstadoProducto(idproducto, valor)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1){

                                            Toasty.success(this, getString(R.string.producto_actualizado)).show();
                                            peticionServidor();

                                        }else{
                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {
                                    mensajeSinConexion();
                                })
        );
    }


    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(this, getString(R.string.sin_conexion)).show();
    }

    @Override
    public void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if(compositeDisposable != null){
            compositeDisposable.clear();
        }
        super.onStop();
    }



}