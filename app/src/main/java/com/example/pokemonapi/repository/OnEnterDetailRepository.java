package com.example.pokemonapi.repository;

import com.example.pokemonapi.network.pokemoninfo.PokemonInfoAPI;
import com.example.pokemonapi.network.pokemoninfo.TypesResponse;

import java.util.List;

public interface OnEnterDetailRepository {
    void onOnlineResponse(List<TypesResponse> types, String heightFormatted, String weightFormatted,
                          Float hpFormatted, Float atkFormatted, Float defFormatted, Float spdFormatted, Float expFormatted,
                          String hpString, String atkString, String defString, String spdString, String expString);

    void onOfflineResponse(PokemonInfoAPI dataOffline);

    void onFailure(Throwable throwable);
}
