package com.example.pokemonapi.repository;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pokemonapi.App;
import com.example.pokemonapi.database.PokemonInfoDAO;
import com.example.pokemonapi.model.pokemoninfo.PokemonInfoAPI;
import com.example.pokemonapi.model.pokemoninfo.TypesResponse;
import com.example.pokemonapi.network.APIClient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailPresenter implements Contracts.DetailPresenter {

    @Inject
    APIClient apiClient;
    @Inject
    PokemonInfoDAO pokemonInfoDAO;
    private final Contracts.DetailView detailView;
    private final List<TypesResponse> typesData = new ArrayList<>();
    private final static MutableLiveData<PokemonInfoAPI> mutableLiveData = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DetailPresenter(Contracts.DetailView detailView) {
        this.detailView = detailView;
        getInjection();
    }

    @Override
    public void fetchPokemonInfo(String namePoke) {
        //pokemonInfoDAO.deleteAll(); // use to clear old database
        detailView.showProgressBar();

        Observable<PokemonInfoAPI> pokemonInfoAPIObservable = apiClient.observableFetchPokemonInfo(namePoke);
        DisposableObserver<PokemonInfoAPI> pokemonInfoAPIObserver = getPokemonInfoAPIObserver(namePoke);

        Disposable disposableFetchData = pokemonInfoAPIObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(pokemonInfoAPIObserver);

        compositeDisposable.add(disposableFetchData);
    }

    private DisposableObserver<PokemonInfoAPI> getPokemonInfoAPIObserver(String namePoke) {
        return new DisposableObserver<PokemonInfoAPI>() {
            @Override
            public void onNext(@NonNull PokemonInfoAPI pokemonInfoAPI) {
                onResponseSuccess(pokemonInfoAPI, namePoke);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                onResponseFail(e, namePoke);
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private void onResponseSuccess(PokemonInfoAPI pokemonInfoAPI, String namePoke) {
        //Get these info: weight, height
        @SuppressLint("DefaultLocale") String heightFormatted = String.format("%.1f M", (float) pokemonInfoAPI.getHeight() / 10);
        @SuppressLint("DefaultLocale") String weightFormatted = String.format("%.1f KG", (float) pokemonInfoAPI.getWeight() / 10);

        //Get Base Performance
        Float hpFormatted = (float) pokemonInfoAPI.hp;
        Float atkFormatted = (float) pokemonInfoAPI.atk;
        Float defFormatted = (float) pokemonInfoAPI.def;
        Float spdFormatted = (float) pokemonInfoAPI.spd;
        Float expFormatted = (float) pokemonInfoAPI.exp;

        String hpString = pokemonInfoAPI.hpString;
        String atkString = pokemonInfoAPI.atkString;
        String defString = pokemonInfoAPI.defString;
        String spdString = pokemonInfoAPI.spdString;
        String expString = pokemonInfoAPI.expString;

        //Get name of types Pokemon and Color Types
        List<TypesResponse> typesList = pokemonInfoAPI.getTypes();

        for (int i = 0; i < typesList.size(); i++) {
            TypesResponse type = typesList.get(i);

            String nameType = type.getType().getName();

            typesData.add(new TypesResponse(nameType));
        }

        PokemonInfoAPI pokemonInfo = new PokemonInfoAPI(namePoke, typesData, heightFormatted, weightFormatted,
                hpFormatted, atkFormatted, defFormatted, spdFormatted, expFormatted,
                hpString, atkString, defString, spdString, expString);

        detailView.hideProgressBar();
        mutableLiveData.setValue(pokemonInfo);
        onInsertPokemonInfoIntoDatabase(pokemonInfo);
    }

    private void onResponseFail(Throwable e, String namePoke) {
        detailView.hideProgressBar();

        if (pokemonInfoDAO.getPokemonInfo(namePoke) == null) {
            detailView.onFailure(e.toString());
        } else {
            mutableLiveData.setValue(pokemonInfoDAO.getPokemonInfo(namePoke));
            detailView.toastForOfflineMode();
        }
    }

    private void onInsertPokemonInfoIntoDatabase(PokemonInfoAPI pokemonInfo) {
        Observable<PokemonInfoAPI> observable = Observable.just(pokemonInfo);

        Disposable disposableInsertData = observable
                .doOnNext(pokemonInfoAPI -> pokemonInfoDAO.insertPokemonInfo(pokemonInfoAPI))
                .subscribeOn(Schedulers.io())
                .subscribe();

        compositeDisposable.add(disposableInsertData);
    }

    private void getInjection() {
        App.getAppComponent().injectDetailPresenter(this);
    }

    public static LiveData<PokemonInfoAPI> getLiveData() {
        return mutableLiveData;
    }

    public void getDisposableToUnsubscribe() {
        compositeDisposable.dispose();
    }
}
