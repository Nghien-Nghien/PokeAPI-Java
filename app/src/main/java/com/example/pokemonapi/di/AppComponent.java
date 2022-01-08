package com.example.pokemonapi.di;

import com.example.pokemonapi.repository.DetailPresenter;
import com.example.pokemonapi.repository.MainPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, DatabaseModule.class})
public interface AppComponent {

    void injectMainPresenter(MainPresenter mainPresenter);

    void injectDetailPresenter(DetailPresenter detailPresenter);
}
