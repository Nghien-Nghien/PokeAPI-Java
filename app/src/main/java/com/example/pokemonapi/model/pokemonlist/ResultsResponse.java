package com.example.pokemonapi.model.pokemonlist;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "PokemonList")
public class ResultsResponse {
    public Integer offset;

    @SerializedName("name")
    @PrimaryKey
    @NonNull
    public String name;

    @SerializedName("url")
    public String url;

    @NonNull
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ResultsResponse(Integer offset, @NonNull String name, String url) {
        this.offset = offset;
        this.name = name;
        this.url = url;
    }
}
