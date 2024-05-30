package com.tatanstudios.astropollococina.fragmentos.notificacion;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.POWER_SERVICE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.developer.kalert.KAlertDialog;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;
import com.tatanstudios.astropollococina.R;
import com.tatanstudios.astropollococina.activitys.principal.PrincipalActivity;
import com.tatanstudios.astropollococina.network.ApiService;
import com.tatanstudios.astropollococina.network.RetrofitBuilder;
import com.tatanstudios.astropollococina.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentNotificacion extends Fragment {


    Button btnEnviar, btnSegundoPlano;

    ApiService service;
    TokenManager tokenManager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RelativeLayout root;

    ProgressBar progressBar;

    String idOneSignal = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_notificacion, container, false);

        ((PrincipalActivity) getActivity()).setActionBarTitle(getString(R.string.notificacion));



        root = vista.findViewById(R.id.root);
        btnEnviar = vista.findViewById(R.id.btnEnviar);
        btnSegundoPlano = vista.findViewById(R.id.btnSegundoPlano);


        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);


        progressBar.setVisibility(View.GONE);

        OSDeviceState device = OneSignal.getDeviceState();

        if(device != null){
            idOneSignal = device.getUserId();
        }

        btnEnviar.setOnClickListener( v-> enviarNotificacion());
        btnSegundoPlano.setOnClickListener( v-> verificarModo());

        return vista;
    }

    void enviarNotificacion(){

        progressBar.setVisibility(View.VISIBLE);

        String id = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.notificacionPrueba(id, idOneSignal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {
                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1){
                                            Toasty.info(getActivity(), "Enviado").show();
                                        }else{
                                            Toasty.info(getActivity(), "Error al enviar").show();
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


    void verificarModo(){

        String packageName = getActivity().getPackageName();
        PowerManager pm = (PowerManager) getActivity().getSystemService(POWER_SERVICE);

        // Comprueba si la aplicación no está en la lista blanca de optimización de batería
        if (pm.isIgnoringBatteryOptimizations(packageName)) {

            // La aplicación está en modo "No Restringido"
            // Realiza aquí las acciones que deseas cuando está en "No Restringido"

            new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE)
                    .setContentTextSize(20)
                    .setTitleText("Activado")
                    .setContentText("Las notificaciones estan activadas en Segundo Plano")
                    .setConfirmText("Aceptar")
                    .setContentTextSize(16)
                    .confirmButtonColor(R.drawable.dialogo_theme_success)
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();

                    })
                    .show();

        }else{
            // La aplicación no está en modo "No Restringido"
            // Realiza aquí las acciones que deseas cuando no está en "No Restringido"

            // Crea un intent para abrir la configuración de optimización de batería
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }

    }

    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.info(getActivity(), getString(R.string.sin_conexion)).show();
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
