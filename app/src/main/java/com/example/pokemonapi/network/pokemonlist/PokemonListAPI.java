package com.example.pokemonapi.network.pokemonlist;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PokemonListAPI {
    @SerializedName("results")
    public ArrayList<ResultsResponse> results;

    public ArrayList<ResultsResponse> getResults() {
        return results;
    }
}
