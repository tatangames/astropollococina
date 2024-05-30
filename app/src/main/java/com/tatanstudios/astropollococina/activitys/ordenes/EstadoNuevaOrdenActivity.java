package com.tatanstudios.astropollococina.activitys.ordenes;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.tatanstudios.astropollococina.R;
import com.tatanstudios.astropollococina.adaptadores.ordenes.ProductoNuevaOrdenAdapter;
import com.tatanstudios.astropollococina.network.ApiService;
import com.tatanstudios.astropollococina.network.RetrofitBuilder;
import com.tatanstudios.astropollococina.network.TokenManager;


import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EstadoNuevaOrdenActivity extends AppCompatActivity {


    RecyclerView recyclerProductos;

    TextView txtToolbar;

    RelativeLayout root;

    ConstraintLayout vista;


    Button btnIniciar;

    Button btnCancelar;

    TextView txtNumeroOrden;


    int idOrden = 0;

    ApiService service;
    TokenManager tokenManager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ProgressBar progressBar;

    ProductoNuevaOrdenAdapter adapter;

    String mensaje = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_nueva_orden);

        root = findViewById(R.id.root);
        vista = findViewById(R.id.vista);
        txtToolbar = findViewById(R.id.txtToolbar);
        recyclerProductos = findViewById(R.id.recyclerProductos);
        btnIniciar = findViewById(R.id.btnIniciar);
        btnCancelar = findViewById(R.id.btnCancelar);
        txtNumeroOrden = findViewById(R.id.txtnumorden);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        txtToolbar.setText(getString(R.string.productos));

        Intent intent = getIntent();
        if (intent != null) {
            idOrden = intent.getIntExtra("KEY_ORDEN", 0);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerProductos.setLayoutManager(layoutManager);
        recyclerProductos.setHasFixedSize(true);
        adapter = new ProductoNuevaOrdenAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerProductos.addItemDecoration(dividerItemDecoration);
        recyclerProductos.setAdapter(adapter);

        btnCancelar.setOnClickListener(v -> cancelarOrden());
        btnIniciar.setOnClickListener(v ->aceparOrden());


        txtNumeroOrden.setText("Orden #" + idOrden);

        peticionServidor();
    }


    void aceparOrden(){
        new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE)
                .setContentTextSize(20)
                .setTitleText(getString(R.string.iniciar_orden))
                .setConfirmText(getString(R.string.si))
                .setContentTextSize(16)
                .confirmButtonColor(R.drawable.dialogo_theme_success)
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    peticionIniciarOrden();
                })
                .cancelButtonColor(R.drawable.dialogo_theme_cancel)
                .setContentTextSize(16)
                .setCancelText(getString(R.string.cancelar))
                .setCancelClickListener(kAlertDialog -> kAlertDialog.dismissWithAnimation())
                .show();
    }


    void peticionIniciarOrden(){

        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(
                service.iniciarPrepararOrden(idOrden)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1){

                                            // ORDEN CANCELADA
                                            alertaOrdenCancelada(apiRespuesta.getTitulo(), apiRespuesta.getMensaje());
                                        }
                                        else if(apiRespuesta.getSuccess() == 2){

                                            // ORDEN INICIADA
                                            alertaOrdenIniciada(apiRespuesta.getTitulo(), apiRespuesta.getMensaje());
                                        }
                                        else{
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

    boolean boolCancelarOrden = true;

    void cancelarOrden(){

        if(boolCancelarOrden){
            boolCancelarOrden = false;

            final EditText edtMensaje = new EditText(this);
            edtMensaje.setFilters(new InputFilter[]{new InputFilter.LengthFilter(300)});
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.cancelacion))
                    .setMessage(getString(R.string.nota_para_el_cliente))
                    .setView(edtMensaje)
                    .setPositiveButton(getString(R.string.enviar), (dialog1, which) -> {
                        String dato = (edtMensaje.getText().toString());

                        if(TextUtils.isEmpty(dato)){
                            Toasty.info(this, getString(R.string.nota_cancelacion_es_requerida)).show();
                            return;
                        }

                        if(dato.length() > 300) {
                            Toasty.info(this, "Máximo 300 caracteres, agrego(" + mensaje.length() + ")").show();
                            return;
                        }

                        mensaje = dato;

                        peticionCancelar();
                    })
                    .setNegativeButton(getString(R.string.cancelar), null)
                    .create();

            dialog.setOnDismissListener(dialogInterface -> {
                dialog.dismiss();
                boolCancelarOrden = true;
            });

            dialog.show();
        }
    }


    void peticionCancelar(){

        cerrarTeclado();

        new KAlertDialog(this, KAlertDialog.WARNING_TYPE)
                .setContentTextSize(20)
                .setTitleText(getString(R.string.cancelar_orden))
                .setConfirmText(getString(R.string.si))
                .setContentTextSize(16)
                .confirmButtonColor(R.drawable.dialogo_theme_success)
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    enviarCancelamiento();
                })
                .cancelButtonColor(R.drawable.dialogo_theme_cancel)
                .setContentTextSize(16)
                .setCancelText(getString(R.string.no))
                .setCancelClickListener(kAlertDialog -> kAlertDialog.dismissWithAnimation())
                .show();
    }

    void cerrarTeclado() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    void enviarCancelamiento(){

        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(
                service.cancelarOrden(idOrden, mensaje)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {
                                        if(apiRespuesta.getSuccess() == 1) {

                                            alertaOrdenIniciada(apiRespuesta.getTitulo(), apiRespuesta.getMensaje());

                                        }


                                        else{
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


    void alertaOrdenIniciada(String titulo, String mensaje){

        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(titulo);
        pDialog.setContentText(mensaje);
        pDialog.setConfirmText(getString(R.string.aceptar));
        pDialog.setContentTextSize(16);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.confirmButtonColor(R.drawable.dialogo_theme_success)
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    onBackPressed();
                });
        pDialog.show();
    }

    void alertaOrdenCancelada(String titulo, String mensaje){

        KAlertDialog pDialog = new KAlertDialog(this, KAlertDialog.WARNING_TYPE);
        pDialog.setTitleText(titulo);
        pDialog.setContentText(mensaje);
        pDialog.setConfirmText(getString(R.string.aceptar));
        pDialog.setContentTextSize(16);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.confirmButtonColor(R.drawable.dialogo_theme_success)
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    onBackPressed();
                });
        pDialog.show();
    }


    void peticionServidor(){
        progressBar.setVisibility(View.VISIBLE);

        compositeDisposable.add(
                service.verListadoDeProductosOrden(idOrden)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {
                                    progressBar.setVisibility(View.GONE);
                                    vista.setVisibility(View.VISIBLE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1){
                                            adapter = new ProductoNuevaOrdenAdapter(getApplicationContext(), apiRespuesta.getProductos(), this);
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
    public void verProductoIndividual(int idOrdenDescrip){
        Intent res = new Intent(this, VistaProductoInvidualOrdenActivity.class);
        res.putExtra("KEY_ORDEN", idOrdenDescrip);
        startActivity(res);
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

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        super.onBackPressed();
    }


}