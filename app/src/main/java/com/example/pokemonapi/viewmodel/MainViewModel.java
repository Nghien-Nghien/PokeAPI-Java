package com.example.pokemonapi.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.pokemonapi.model.pokemonlist.ResultsResponse;
import com.example.pokemonapi.repository.MainPresenter;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final LiveData<List<ResultsResponse>> liveData;

    public MainViewModel() {
        liveData = MainPresenter.getLiveData();
    }

    public LiveData<List<ResultsResponse>> getLiveData() {
        return liveData;
    }
}
