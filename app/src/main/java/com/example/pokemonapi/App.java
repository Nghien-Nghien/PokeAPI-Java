package com.example.pokemonapi;

import android.app.Application;

import com.example.pokemonapi.di.AppComponent;
import com.example.pokemonapi.di.DaggerAppComponent;
import com.example.pokemonapi.di.DatabaseModule;
import com.example.pokemonapi.di.NetworkModule;

public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().networkModule(new NetworkModule()).databaseModule(new DatabaseModule(this)).build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
