package com.example.pokemonapi.di;

import com.example.pokemonapi.repository.DetailPresenter;
import com.example.pokemonapi.repository.MainPresenter;

import dagger.Component;

@Component(modules = {RepositoryModule.class})
public interface RepositoryComponent {

    void injectMainPresenter(MainPresenter mainPresenter);

    void injectDetailPresenter(DetailPresenter detailPresenter);
}
