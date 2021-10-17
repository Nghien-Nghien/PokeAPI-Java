package com.example.pokemonapi.network.pokemoninfo;

import com.google.gson.annotations.SerializedName;

public class Type {
    @SerializedName("name")
    public String name;

    public String getName() {
        return name;
    }
}
