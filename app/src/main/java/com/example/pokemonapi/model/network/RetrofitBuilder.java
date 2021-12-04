package com.example.pokemonapi.model.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public Retrofit retrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .client(createOkHTTPClient())
                .addConverterFactory(GsonConverterFactory.create())
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
