package com.example.pokemonapi.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.pokemonapi.model.pokemonlist.ResultsResponse;
import com.example.pokemonapi.repository.MainRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final LiveData<List<ResultsResponse>> pokemonListLiveData;

    private final LiveData<Boolean> progressBarLiveData;

    private final LiveData<Boolean> swipeRefreshLayoutLiveData;

    private final LiveData<String> toastLiveData;

    public MainViewModel() {
        pokemonListLiveData = MainRepository.getPokemonListLiveData();
        progressBarLiveData = MainRepository.getProgressBarLiveData();
        swipeRefreshLayoutLiveData = MainRepository.getSwipeRefreshLayoutLiveData();
        toastLiveData = MainRepository.getToastLiveData();
    }

    public LiveData<List<ResultsResponse>> getPokemonListLiveData() {
        return pokemonListLiveData;
    }

    public LiveData<Boolean> getProgressBarLiveData() {
        return progressBarLiveData;
    }

    public LiveData<Boolean> getSwipeRefreshLayoutLiveData() {
        return swipeRefreshLayoutLiveData;
    }

    public LiveData<String> getToastLiveData() {
        return toastLiveData;
    }
}
