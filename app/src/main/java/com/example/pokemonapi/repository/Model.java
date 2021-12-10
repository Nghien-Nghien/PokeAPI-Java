package com.example.pokemonapi.repository;

import com.example.pokemonapi.model.network.RetrofitBuilder;
import com.example.pokemonapi.model.pokemoninfo.PokemonInfoAPI;
import com.example.pokemonapi.model.pokemonlist.PokemonListAPI;

import retrofit2.Call;

public class Model implements Contracts.Model {

    private final RetrofitBuilder retrofitBuilder;

    public Model(RetrofitBuilder retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
    }

    @Override
    public Call<PokemonListAPI> callFetchPokemonList(int offset) {
        return retrofitBuilder.requestToApiInterface().fetchPokemonList(offset);
    }

    @Override
    public Call<PokemonInfoAPI> callFetchPokemonInfo(String name) {
        return retrofitBuilder.requestToApiInterface().fetchPokemonInfo(name);
    }
}
