package com.example.pokemonapi.network.pokemoninfo;

import com.google.gson.annotations.SerializedName;

public class TypesResponse {
    @SerializedName("type")
    public Type type;

    public Type getType() {
        return type;
    }
}
