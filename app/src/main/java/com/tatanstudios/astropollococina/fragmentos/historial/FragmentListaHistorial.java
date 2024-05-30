package com.tatanstudios.astropollococina.fragmentos.historial;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.astropollococina.R;
import com.tatanstudios.astropollococina.activitys.ordenes.EstadoProductosListadoActivity;
import com.tatanstudios.astropollococina.adaptadores.historial.HistorialAdapter;
import com.tatanstudios.astropollococina.network.ApiService;
import com.tatanstudios.astropollococina.network.RetrofitBuilder;
import com.tatanstudios.astropollococina.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentListaHistorial extends Fragment {

    // muestra lista de ordenes
    String fecha1 = "";
    String fecha2 = "";

    ApiService service;
    TokenManager tokenManager;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    HistorialAdapter historialAdapter;

    RecyclerView recyclerServicio;
    RelativeLayout root;

    ProgressBar progressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_historial_busqueda, container, false);


        recyclerServicio = vista.findViewById(R.id.recyclerServicio);
        root = vista.findViewById(R.id.root);


        Bundle bundle = getArguments();
        if (bundle != null) {
            fecha1 = bundle.getString("FECHA1");
            fecha2 = bundle.getString("FECHA2");
        }

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerServicio.setLayoutManager(layoutManager);
        recyclerServicio.setHasFixedSize(true);
        recyclerServicio.setAdapter(historialAdapter);

        peticionServidor();

        return vista;
    }

    void peticionServidor(){

        progressBar.setVisibility(View.VISIBLE);

        String id = tokenManager.getToken().getId();
        compositeDisposable.add(
                service.informacionHistorial(id, fecha1, fecha2)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null){
                                        if(apiRespuesta.getSuccess() == 1) {

                                            if(apiRespuesta.getOrdenes().size() > 0){

                                                historialAdapter = new HistorialAdapter(getContext(), apiRespuesta.getOrdenes(), this);
                                                recyclerServicio.setAdapter(historialAdapter);
                                            }else{
                                                Toasty.info(getActivity(), getString(R.string.no_hay_registros)).show();
                                            }

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



    public void abrirOrdenes(int ordenid) {
        Intent res = new Intent(getActivity(), EstadoProductosListadoActivity.class);
        res.putExtra("KEY_ORDEN", ordenid);
        startActivity(res);
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
