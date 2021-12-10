package com.example.pokemonapi.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.pokemonapi.model.pokemoninfo.PokemonInfoAPI;

@Dao
public interface PokemonInfoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPokemonInfo(PokemonInfoAPI pokemonInfoAPI);

    @Query("SELECT * FROM PokemonInfo WHERE `name` = :name")
    PokemonInfoAPI getPokemonInfo(String name);

    @Query("DELETE FROM PokemonInfo")
    void deleteAll();
}
