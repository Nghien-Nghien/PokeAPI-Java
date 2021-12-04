package com.example.pokemonapi.repository;

import android.annotation.SuppressLint;

import com.example.pokemonapi.database.PokemonInfoDAO;
import com.example.pokemonapi.model.pokemoninfo.PokemonInfoAPI;
import com.example.pokemonapi.model.pokemoninfo.TypesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPresenter implements Contracts.DetailPresenter {
    private final Contracts.DetailView detailView;
    private final Contracts.Model model;
    private final PokemonInfoDAO pokemonInfoDAO;
    private final PokemonInfoAPI basePerformance;
    private List<TypesResponse> typesData;

    public DetailPresenter(Contracts.DetailView detailView, Contracts.Model model, PokemonInfoDAO pokemonInfoDAO, PokemonInfoAPI basePerformance) {
        this.detailView = detailView;
        this.model = model;
        this.pokemonInfoDAO = pokemonInfoDAO;
        this.basePerformance = basePerformance;
    }

    @Override
    public void fetchPokemonInfo(String namePoke) {
        //pokemonInfoDAO.deleteAll(); // use to clear old database
        detailView.showProgressBar();
        typesData = new ArrayList<>();

        if (pokemonInfoDAO.getPokemonInfo(namePoke) == null) {
            Call<PokemonInfoAPI> call = model.callFetchPokemonInfo(namePoke);

            //noinspection NullableProblems
            call.enqueue(new Callback<PokemonInfoAPI>() {
                @Override
                public void onResponse(Call<PokemonInfoAPI> call, Response<PokemonInfoAPI> response) {
                    assert response.body() != null;

                    //Get these info: weight, height
                    PokemonInfoAPI baseInfo = response.body();

                    @SuppressLint("DefaultLocale") String heightFormatted = String.format("%.1f M", (float) baseInfo.getHeight() / 10);
                    @SuppressLint("DefaultLocale") String weightFormatted = String.format("%.1f KG", (float) baseInfo.getWeight() / 10);

                    //Get Base Performance
                    Float hpFormatted = (float) basePerformance.hp;
                    Float atkFormatted = (float) basePerformance.atk;
                    Float defFormatted = (float) basePerformance.def;
                    Float spdFormatted = (float) basePerformance.spd;
                    Float expFormatted = (float) basePerformance.exp;

                    String hpString = basePerformance.hpString;
                    String atkString = basePerformance.atkString;
                    String defString = basePerformance.defString;
                    String spdString = basePerformance.spdString;
                    String expString = basePerformance.expString;

                    //Get name of types Pokemon and Color Types
                    List<TypesResponse> typesList = response.body().getTypes();
                    for (int i = 0; i < typesList.size(); i++) {
                        TypesResponse type = typesList.get(i);

                        String nameType = type.getType().getName();

                        typesData.add(new TypesResponse(nameType));
                    }

                    detailView.hideProgressBar();
                    detailView.onOnlineResponse(typesData, heightFormatted, weightFormatted,
                            hpFormatted, atkFormatted, defFormatted, spdFormatted, expFormatted,
                            hpString, atkString, defString, spdString, expString);
                    pokemonInfoDAO.insertPokemonInfo(new PokemonInfoAPI(namePoke, typesData, heightFormatted, weightFormatted,
                            hpFormatted, atkFormatted, defFormatted, spdFormatted, expFormatted,
                            hpString, atkString, defString, spdString, expString));
                }

                @Override
                public void onFailure(Call<PokemonInfoAPI> call, Throwable throwable) {
                    detailView.hideProgressBar();
                    detailView.onFailure(throwable.toString());
                }
            });
        } else {
            detailView.hideProgressBar();
            detailView.onOfflineResponse(pokemonInfoDAO.getPokemonInfo(namePoke));
        }
    }
}
