package com.example.pokemonapi.repository;

import com.example.pokemonapi.database.PokemonListDAO;
import com.example.pokemonapi.network.RetrofitBuilder;
import com.example.pokemonapi.network.pokemonlist.PokemonListAPI;
import com.example.pokemonapi.network.pokemonlist.ResultsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {
    private final RetrofitBuilder retrofitBuilder;
    private final PokemonListDAO pokemonListDAO;
    private List<ResultsResponse> data;
    private OnEnterMainRepository listener;

    public MainRepository(RetrofitBuilder retrofitBuilder, PokemonListDAO pokemonListDAO) {
        this.retrofitBuilder = retrofitBuilder;
        this.pokemonListDAO = pokemonListDAO;
    }

    public void fetchPokemonList(int offset) {
        //pokemonListDAO.deleteAll(); // use to clear old database
        data = new ArrayList<>();

        if (pokemonListDAO.getPokemonList(offset).isEmpty()) {

            Call<PokemonListAPI> call = retrofitBuilder.requestToApiInterface().fetchPokemonList(offset);

            //noinspection NullableProblems
            call.enqueue(new Callback<PokemonListAPI>() {
                @Override
                public void onResponse(Call<PokemonListAPI> call, Response<PokemonListAPI> response) {
                    assert response.body() != null;
                    List<ResultsResponse> resultsList = response.body().getResults();

                    for (int i = 0; i < resultsList.size(); i++) {
                        ResultsResponse result = resultsList.get(i);

                        String namePoke = result.getName();

                        String urlPoke = result.getUrl().replaceFirst(".$", "").substring(33);

                        data.add(new ResultsResponse(offset, namePoke, urlPoke));
                    }

                    listener.onOnlineResponse(data);
                    pokemonListDAO.insertPokemonList(data);
                }

                @Override
                public void onFailure(Call<PokemonListAPI> call, Throwable throwable) {
                    listener.onFailure(throwable);
                }
            });
        } else {
            listener.onOfflineResponse(pokemonListDAO.getPokemonList(offset));
        }
    }

    public void setListener(OnEnterMainRepository listener) {
        this.listener = listener;
    }
}
