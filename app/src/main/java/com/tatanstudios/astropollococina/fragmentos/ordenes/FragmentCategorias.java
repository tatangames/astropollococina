package com.tatanstudios.astropollococina.fragmentos.ordenes;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tatanstudios.astropollococina.R;
import com.tatanstudios.astropollococina.activitys.principal.PrincipalActivity;
import com.tatanstudios.astropollococina.activitys.productos.ActualizacionProductosActivity;
import com.tatanstudios.astropollococina.adaptadores.ordenes.CategoriasAdapter;
import com.tatanstudios.astropollococina.network.ApiService;
import com.tatanstudios.astropollococina.network.RetrofitBuilder;
import com.tatanstudios.astropollococina.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentCategorias extends Fragment {


    ApiService service;
    TokenManager tokenManager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    CategoriasAdapter adapter;

    RecyclerView recyclerOrdenes;
    SwipeRefreshLayout refresh;
    RelativeLayout root;

    ProgressBar progressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_categorias, container, false);


        ((PrincipalActivity)getActivity()).setActionBarTitle(getString(R.string.categorias));

        recyclerOrdenes = vista.findViewById(R.id.recyclerOrdenes);
        refresh = vista.findViewById(R.id.refresh);
        root = vista.findViewById(R.id.root);


        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerOrdenes.setLayoutManager(layoutManager);
        recyclerOrdenes.setHasFixedSize(true);
        adapter = new CategoriasAdapter();
        recyclerOrdenes.setAdapter(adapter);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        root.addView(progressBar, params);

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
                service.listadoDeCategorias(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    refresh.setRefreshing(false);
                                    progressBar.setVisibility(View.GONE);


                                    if(apiRespuesta != null) {


                                        if(apiRespuesta.getSuccess() == 1){

                                            adapter = new CategoriasAdapter(getContext(), apiRespuesta.getCategorias(), this);
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



    // ver la orden y su informacion
    public void verOpciones(int idcategoria, int checkActivo){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.dialog_opciones_categoria);

        Button btn1 = bottomSheetDialog.findViewById(R.id.btnCategoria);
        Button btn2 = bottomSheetDialog.findViewById(R.id.btnProducto);

        // modificara la categoria
        btn1.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            View vista = View.inflate(getContext(), R.layout.dialog_activar_categoria, null);
            CheckBox checkBox = vista.findViewById(R.id.checkbox);

            if(checkActivo == 1){
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

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.actualizar_categoria));
            builder.setView(vista)
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.guardar), (dialog, id) -> {

                        int valor = 0;

                        if(checkBox.isChecked()){
                            valor = 1;
                        }

                        peticionActualizarCategoria(idcategoria, valor);
                    })
                    .setNegativeButton(getString(R.string.cancelar), (dialog, id) -> dialog.cancel()).show();
        });

        // VISTA PARA MOSTRAR LOS PRODUCTOS
        btn2.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            abrirVistaCateProducto(idcategoria);
        });



        bottomSheetDialog.show();
    }

    void abrirVistaCateProducto(int idcategoria){
        Intent res = new Intent(getActivity(), ActualizacionProductosActivity.class);
        res.putExtra("KEY_ID", idcategoria);
        startActivity(res);
    }

    void peticionActualizarCategoria(int idcategoria, int checkValor){


        progressBar.setVisibility(View.VISIBLE);

        refresh.setRefreshing(true);

        compositeDisposable.add(
                service.actualizarEstadoCategoria(idcategoria, checkValor)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    refresh.setRefreshing(false);
                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1){

                                            Toasty.success(getActivity(), getString(R.string.categoria_actualizada)).show();
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
