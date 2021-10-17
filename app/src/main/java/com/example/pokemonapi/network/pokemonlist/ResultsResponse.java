package com.example.pokemonapi.network.pokemonlist;

import com.google.gson.annotations.SerializedName;

public class ResultsResponse {
    @SerializedName("name")
    public String name;

    @SerializedName("url")
    public String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
