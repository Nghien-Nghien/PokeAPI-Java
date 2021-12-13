package com.example.pokemonapi.model.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/";

    public Retrofit retrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(createOkHTTPClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    public APIInterface requestToApiInterface() {
        return retrofitBuilder().create(APIInterface.class);
    }

    public OkHttpClient createOkHTTPClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addNetworkInterceptor(httpLoggingInterceptor);

        return builder.build();
    }
}
