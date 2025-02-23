package com.tatanstudios.astropollococina.network;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static final String BASE_URL = "http://165.227.196.71/api/";
    public static final String urlImagenes = "http://165.227.196.71/storage/imagenes/";

    //private static final String BASE_URL = "http://192.168.1.29:8080/api/";
    //public static final String urlImagenes = "http://192.168.1.29:8080/storage/imagenes/";

    private final static OkHttpClient client = buildClient();
    private final static Retrofit retrofit = buildRetrofit();

    // peticiones sin token
    private static OkHttpClient buildClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();

                    Request.Builder builder1 = request.newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Connection", "close");

                    request = builder1.build();

                    return  chain.proceed(request);
                });

        return builder.build();
    }

    private static Retrofit buildRetrofit(){

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static <T> T createServiceNoAuth(Class<T> service){
        return retrofit.create(service);
    }

    // peticiones con token
    public static <T> T createServiceWithAuth(Class<T> service, TokenManager tokenManager){

        OkHttpClient newClient = client.newBuilder().addInterceptor(chain -> {

            Request request = chain.request();

            Request.Builder builder = request.newBuilder();

            if(tokenManager.getToken().getToken() != null){
                builder.addHeader("Authorization", "Bearer " + tokenManager.getToken().getToken());
            }

            request = builder.build();
            return chain.proceed(request);
        }).build();

        Retrofit newRetrofit = retrofit.newBuilder().client(newClient).build();
        return newRetrofit.create(service);
    }

}
