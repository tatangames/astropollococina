package com.tatanstudios.astropollococina.fragmentos.ordenes;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.developer.kalert.KAlertDialog;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;
import com.tatanstudios.astropollococina.R;
import com.tatanstudios.astropollococina.activitys.loginuser.LoginActivity;
import com.tatanstudios.astropollococina.activitys.ordenes.EstadoNuevaOrdenActivity;
import com.tatanstudios.astropollococina.activitys.principal.PrincipalActivity;
import com.tatanstudios.astropollococina.adaptadores.ordenes.NuevasOrdenesAdapter;
import com.tatanstudios.astropollococina.network.ApiService;
import com.tatanstudios.astropollococina.network.RetrofitBuilder;
import com.tatanstudios.astropollococina.network.TokenManager;


import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentNuevasOrdenes extends Fragment {

    ApiService service;
    TokenManager tokenManager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    NuevasOrdenesAdapter adapter;

    RecyclerView recyclerOrdenes;
    SwipeRefreshLayout refresh;
    RelativeLayout root;

    ProgressBar progressBar;

    String idOneSignal = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_nuevas_ordenes, container, false);


        ((PrincipalActivity)getActivity()).setActionBarTitle(getString(R.string.nuevas_ordenes));

        recyclerOrdenes = vista.findViewById(R.id.recyclerOrdenes);
        refresh = vista.findViewById(R.id.refresh);
        root = vista.findViewById(R.id.root);


        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerOrdenes.setLayoutManager(layoutManager);
        recyclerOrdenes.setHasFixedSize(true);
        adapter = new NuevasOrdenesAdapter();
        recyclerOrdenes.setAdapter(adapter);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);


        OSDeviceState device = OneSignal.getDeviceState();

        if(device != null){
            idOneSignal = device.getUserId();
        }


        peticionServidor();
        refresh.setOnRefreshListener(() -> peticionServidor());

        return vista;
    }

    // solicitar ordenes
    void peticionServidor(){

        progressBar.setVisibility(View.VISIBLE);

        refresh.setRefreshing(true);
        String id = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.verNuevaOrdenesAfiliado(id, idOneSignal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    refresh.setRefreshing(false);
                                    progressBar.setVisibility(View.GONE);


                                    if(apiRespuesta != null) {
                                        if(apiRespuesta.getSuccess() == 1) {
                                            usuarioInactivo();
                                        }else if(apiRespuesta.getSuccess() == 2){

                                            if(apiRespuesta.getHayOrdenes() == 0) {
                                                Toasty.info(getActivity(), getString(R.string.no_hay_ordenes)).show();
                                            }

                                            adapter = new NuevasOrdenesAdapter(getContext(), apiRespuesta.getOrdenes(), this);
                                            recyclerOrdenes.setAdapter(adapter);

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

    void usuarioInactivo(){
        new KAlertDialog(getActivity(), KAlertDialog.WARNING_TYPE)
                .setContentTextSize(20)
                .setTitleText(getString(R.string.usuario_desactivado))
                .setContentText("")
                .setConfirmText(getString(R.string.aceptar))
                .setContentTextSize(16)
                .confirmButtonColor(R.drawable.dialogo_theme_success)
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    tokenManager.deletePreferences();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .show();
    }

    // ver la orden y su informacion
    public void abrirOrdenes(int ordenid){
        Intent res = new Intent(getActivity(), EstadoNuevaOrdenActivity.class);
        res.putExtra("KEY_ORDEN", ordenid);
        someActivityResultLauncher.launch(res);
    }

    // recargar al regresar
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    peticionServidor();
                }
            });

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
