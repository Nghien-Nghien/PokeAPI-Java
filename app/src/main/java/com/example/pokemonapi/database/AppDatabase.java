package com.example.pokemonapi.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.pokemonapi.model.pokemoninfo.PokemonInfoAPI;
import com.example.pokemonapi.model.pokemonlist.ResultsResponse;

@Database(entities = {ResultsResponse.class, PokemonInfoAPI.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract PokemonListDAO pokemonListDAO();

    public abstract PokemonInfoDAO pokemonInfoDAO();
}
