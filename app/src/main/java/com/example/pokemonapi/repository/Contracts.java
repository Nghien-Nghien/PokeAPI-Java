package com.example.pokemonapi.repository;

import com.example.pokemonapi.model.pokemoninfo.PokemonInfoAPI;
import com.example.pokemonapi.model.pokemoninfo.TypesResponse;
import com.example.pokemonapi.model.pokemonlist.ResultsResponse;

import java.util.List;

public interface Contracts {

    interface MainPresenter {
        void fetchPokemonList(int offset);
    }

    interface DetailPresenter {
        void fetchPokemonInfo(String namePoke);
    }

    interface MainView {
        void onOnlineResponse(List<ResultsResponse> dataOnline);

        void onOfflineResponse(List<ResultsResponse> dataOffline);

        void onFailure(String errorCode);

        void toastForOfflineMode();

        void showProgressBar();

        void hideProgressBar();
    }

    interface DetailView {
        void onOnlineResponse(List<TypesResponse> types, String heightFormatted, String weightFormatted,
                              Float hpFormatted, Float atkFormatted, Float defFormatted, Float spdFormatted, Float expFormatted,
                              String hpString, String atkString, String defString, String spdString, String expString);

        void onOfflineResponse(PokemonInfoAPI dataOffline);

        void onFailure(String errorCode);

        void toastForOfflineMode();

        void showProgressBar();

        void hideProgressBar();
    }
}
