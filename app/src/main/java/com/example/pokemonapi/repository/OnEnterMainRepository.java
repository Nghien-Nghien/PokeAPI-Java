package com.example.pokemonapi.repository;

import com.example.pokemonapi.network.pokemonlist.ResultsResponse;

import java.util.List;

public interface OnEnterMainRepository {
    void onOnlineResponse(List<ResultsResponse> dataOnline);

    void onOfflineResponse(List<ResultsResponse> dataOffline);

    void onFailure(Throwable throwable);
}
