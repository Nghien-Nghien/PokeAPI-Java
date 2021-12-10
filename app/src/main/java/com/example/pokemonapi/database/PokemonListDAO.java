package com.example.pokemonapi.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.pokemonapi.model.pokemonlist.ResultsResponse;

import java.util.List;

@Dao
public interface PokemonListDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPokemonList(List<ResultsResponse> pokemonList);

    @Query("SELECT * FROM PokemonList WHERE `offset` = :offset")
    List<ResultsResponse> getPokemonList(int offset);

    @Query("DELETE FROM PokemonList")
    void deleteAll();
}
