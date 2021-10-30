package com.example.pokemonapi.network;

import android.os.SystemClock;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .client(createOkHTTPClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public APIInterface requestToApiInterface() {
        return retrofit().create(APIInterface.class);
    }

    public OkHttpClient createOkHTTPClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addNetworkInterceptor(httpLoggingInterceptor);

        Interceptor delayInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                SystemClock.sleep(2000);
                return chain.proceed(chain.request());
            }
        };

        //builder.addNetworkInterceptor(delayInterceptor);

        OkHttpClient client = builder.build();

        return client;
    }
}
