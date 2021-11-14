package com.example.pokemonapi.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseBuilder {
    private final Context context;

    public DatabaseBuilder(Context context) {
        this.context = context;
    }

    public AppDatabase databaseBuilder() {
        return Room.databaseBuilder(context, AppDatabase.class, "PokemonDatabase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
