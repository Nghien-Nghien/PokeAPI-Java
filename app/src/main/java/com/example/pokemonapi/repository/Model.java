package com.example.pokemonapi.repository;

import com.example.pokemonapi.model.network.RetrofitBuilder;
import com.example.pokemonapi.model.pokemoninfo.PokemonInfoAPI;
import com.example.pokemonapi.model.pokemonlist.PokemonListAPI;

import io.reactivex.rxjava3.core.Observable;

public class Model implements Contracts.Model {

    private final RetrofitBuilder retrofitBuilder;

    public Model(RetrofitBuilder retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
    }

    @Override
    public Observable<PokemonListAPI> observableFetchPokemonList(int offset) {
        return retrofitBuilder.requestToApiInterface().fetchPokemonList(offset);
    }

    @Override
    public Observable<PokemonInfoAPI> observableFetchPokemonInfo(String name) {
        return retrofitBuilder.requestToApiInterface().fetchPokemonInfo(name);
    }
}
