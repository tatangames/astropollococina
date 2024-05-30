package com.tatanstudios.astropollococina.network;

import com.tatanstudios.astropollococina.modelos.categorias.ModeloCategorias;
import com.tatanstudios.astropollococina.modelos.ordenes.ModeloOrdenes;
import com.tatanstudios.astropollococina.modelos.productos.ModeloProducto;
import com.tatanstudios.astropollococina.modelos.productos.ModeloProductoList;
import com.tatanstudios.astropollococina.modelos.productos.ModeloProductosOrdenes;
import com.tatanstudios.astropollococina.modelos.usuario.AccessTokenUser;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {



    @POST("restaurante/login")
    @FormUrlEncoded
    Observable<AccessTokenUser> inicioSesion(@Field("usuario") String usuario,
                                             @Field("password") String password,
                                             @Field("idfirebase") String idfirebase);


    // LISTADO DE NUEVAS ORDENES

    @FormUrlEncoded
    @POST("restaurante/nuevas/ordenes")
    Observable<ModeloOrdenes> verNuevaOrdenesAfiliado(@Field("id") String id,
                                                      @Field("idfirebase") String idfirebase);


    // LISTADO DE PRODUCTOS DE UNA ORDEN
    @FormUrlEncoded
    @POST("restaurante/listado/producto/orden")
    Observable<ModeloProducto> verListadoDeProductosOrden(@Field("idorden") int idorden);


    @FormUrlEncoded
    @POST("restaurante/listado/productos/ordenes-individual")
    Observable<ModeloProductosOrdenes> verProductosOrdenesIndividual(
            @Field("idordendescrip") int idordendescrip);



    // INICIAR LA ORDEN
    @FormUrlEncoded
    @POST("restaurante/proceso/orden/iniciar-orden")
    Observable<AccessTokenUser> iniciarPrepararOrden(
            @Field("idorden") int idorden);



    // CANCELAR LA ORDEN AL CLIENTE
    @FormUrlEncoded
    @POST("restaurante/cancelar/orden")
    Observable<AccessTokenUser> cancelarOrden(
            @Field("idorden") int idorden, @Field("mensaje") String mensaje);


    // VER LISTADO DE ORDENES EN PREPARACION

    @FormUrlEncoded
    @POST("restaurante/preparacion/ordenes")
    Observable<ModeloOrdenes> verPreparacionOrdenesRestaurante(
            @Field("id") String id);



    // FINALIZAR LA ORDEN
    @FormUrlEncoded
    @POST("restaurante/proceso/orden/finalizar-orden")
    Observable<AccessTokenUser> finalizarOrden(
            @Field("idorden") int idorden);



    // LISTADO DE ORDENES COMPLETADAS HOY
    @FormUrlEncoded
    @POST("restaurante/completadashoy/ordenes")
    Observable<ModeloOrdenes> verCompletadasHoyOrdenes(@Field("id") String id);


    // LISTADO DE ORDENES CANCELADAS HOY
    @FormUrlEncoded
    @POST("restaurante/canceladashoy/ordenes")
    Observable<ModeloOrdenes> verCanceladasHoyOrdenes(@Field("id") String id);



    // LISTADO DE CATEGORIAS
    @FormUrlEncoded
    @POST("restaurante/listado/categorias")
    Observable<ModeloCategorias> listadoDeCategorias(@Field("id") String id);


    // ACTUALIZAR ESTADO DE LA CATEGORIA
    @FormUrlEncoded
    @POST("restaurante/actualizar/estado/categorias")
    Observable<AccessTokenUser> actualizarEstadoCategoria(@Field("idcategoria") int idcategoria,
                                                          @Field("checkvalor") int checkvalor);


    // LISTADO DE PRODUCTOS POR CATEGORIA
    @FormUrlEncoded
    @POST("restaurante/categoria/listado/productos")
    Observable<ModeloProducto> verListadoDeProductosPorCategoria(@Field("idcategoria") int idcategoria);



    // ACTUALIZAR ESTADO DE PRODUCTO
    @FormUrlEncoded
    @POST("restaurante/actualizar/estado/producto")
    Observable<AccessTokenUser> actualizarEstadoProducto(@Field("idproducto") int idproducto,
                                                          @Field("checkvalor") int checkvalor);


    // HISTORIAL DE ORDENES POR FECHA
    @FormUrlEncoded
    @POST("restaurante/historial/ordenes")
    Observable<ModeloOrdenes> informacionHistorial(@Field("id") String id,
                                                   @Field("fecha1") String fecha1,
                                                   @Field("fecha2") String fecha2);



    // enviar notificacion modo prueba
    @FormUrlEncoded
    @POST("restaurante/notificacion/modo/prueba")
    Observable<AccessTokenUser> notificacionPrueba(@Field("id") String id,
                                                   @Field("tokenid") String tokenid);


}
