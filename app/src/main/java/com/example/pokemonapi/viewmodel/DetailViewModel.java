package com.example.pokemonapi.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.pokemonapi.model.pokemoninfo.PokemonInfoAPI;
import com.example.pokemonapi.repository.DetailPresenter;

public class DetailViewModel extends ViewModel {

    private final LiveData<PokemonInfoAPI> liveData;

    public DetailViewModel() {
        liveData = DetailPresenter.getLiveData();
    }

    public LiveData<PokemonInfoAPI> getLiveData() {
        return liveData;
    }
}
