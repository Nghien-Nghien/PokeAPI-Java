package com.example.pokemonapi.network;

import com.example.pokemonapi.network.pokemoninfo.PokemonInfoAPI;
import com.example.pokemonapi.network.pokemonlist.PokemonListAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("pokemon?limit=20")
    Call<PokemonListAPI> fetchPokemonList(@Query("offset") int offset);

    @GET("pokemon/{name}")
    Call<PokemonInfoAPI> fetchPokemonInfo(@Path("name") String name);
}
