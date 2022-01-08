package com.example.pokemonapi.repository;

import com.example.pokemonapi.App;
import com.example.pokemonapi.database.PokemonListDAO;
import com.example.pokemonapi.model.pokemonlist.PokemonListAPI;
import com.example.pokemonapi.model.pokemonlist.ResultsResponse;
import com.example.pokemonapi.network.APIClient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainPresenter implements Contracts.MainPresenter {

    private final Contracts.MainView mainView;
    @Inject
    APIClient apiClient;
    @Inject
    PokemonListDAO pokemonListDAO;
    private List<ResultsResponse> data;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainPresenter(Contracts.MainView mainView) {
        this.mainView = mainView;
        getInjection();
    }

    @Override
    public void fetchPokemonList(int offset) {
        //pokemonListDAO.deleteAll(); // use to clear old database
        mainView.showProgressBar();
        data = new ArrayList<>();

        Observable<PokemonListAPI> pokemonListAPIObservable = apiClient.observableFetchPokemonList(offset);
        DisposableObserver<PokemonListAPI> pokemonListAPIObserver = getPokemonListAPIObserver(offset);

        pokemonListAPIObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pokemonListAPIObserver);

        compositeDisposable.add(pokemonListAPIObserver);
    }

    private DisposableObserver<PokemonListAPI> getPokemonListAPIObserver(int offset) {
        return new DisposableObserver<PokemonListAPI>() {
            @Override
            public void onNext(@NonNull PokemonListAPI pokemonListAPI) {
                onResponseSuccess(pokemonListAPI, offset);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                onResponseFail(e, offset);
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private void onResponseSuccess(PokemonListAPI pokemonListAPI, int offset) {
        List<ResultsResponse> resultsList = pokemonListAPI.getResults();

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

    private void onResponseFail(Throwable e, int offset) {
        mainView.hideProgressBar();
        mainView.onFailure(e.toString());

        if (pokemonListDAO.getPokemonList(offset) != null) {
            mainView.onOfflineResponse(pokemonListDAO.getPokemonList(offset));
            mainView.toastForOfflineMode();
        }
    }

    public void getDisposableToUnsubscribe() {
        compositeDisposable.dispose();
    }

    private void getInjection() {
        App.getAppComponent().injectMainPresenter(this);
    }
}
