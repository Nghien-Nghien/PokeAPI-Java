package com.example.pokemonapi.database;

import android.app.Application;

import androidx.room.Room;

public class DatabaseBuilder {

    private static DatabaseBuilder INSTANCE;
    private Application context;

    private DatabaseBuilder() {
    }

    public void setContext(Application context) {
        this.context = context;
    }

    public static DatabaseBuilder getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseBuilder();
        }

        return INSTANCE;
    }

    public AppDatabase databaseBuilder() {
        return Room.databaseBuilder(context, AppDatabase.class, "PokemonDatabase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
