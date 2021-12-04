package com.example.pokemonapi.repository;

import com.example.pokemonapi.database.PokemonListDAO;
import com.example.pokemonapi.model.pokemonlist.PokemonListAPI;
import com.example.pokemonapi.model.pokemonlist.ResultsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements Contracts.MainPresenter {
    private final Contracts.MainView mainView;
    private final Contracts.Model model;
    private final PokemonListDAO pokemonListDAO;
    private List<ResultsResponse> data;

    public MainPresenter(Contracts.MainView mainView, Contracts.Model model, PokemonListDAO pokemonListDAO) {
        this.mainView = mainView;
        this.model = model;
        this.pokemonListDAO = pokemonListDAO;
    }

    @Override
    public void fetchPokemonList(int offset) {
        //pokemonListDAO.deleteAll(); // use to clear old database
        mainView.showProgressBar();
        data = new ArrayList<>();

        if (pokemonListDAO.getPokemonList(offset).isEmpty()) {

            Call<PokemonListAPI> call = model.callFetchPokemonList(offset);

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

                    mainView.hideProgressBar();
                    mainView.onOnlineResponse(data);
                    pokemonListDAO.insertPokemonList(data);
                }

                @Override
                public void onFailure(Call<PokemonListAPI> call, Throwable throwable) {
                    mainView.hideProgressBar();
                    mainView.onFailure(throwable.toString());
                }
            });
        } else {
            mainView.hideProgressBar();
            mainView.onOfflineResponse(pokemonListDAO.getPokemonList(offset));
        }
    }
}
