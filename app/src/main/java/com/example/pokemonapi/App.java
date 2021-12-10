package com.example.pokemonapi;

import android.app.Application;

import com.example.pokemonapi.database.DatabaseBuilder;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseBuilder.getINSTANCE().setContext(this);
    }
}
