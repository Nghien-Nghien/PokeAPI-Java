package com.example.pokemonapi.di;

import com.example.pokemonapi.network.APIClient;
import com.example.pokemonapi.repository.DetailPresenter;
import com.example.pokemonapi.repository.MainPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    public MainPresenter provideMainPresenter(APIClient apiClient) {
        return new MainPresenter(apiClient);
    }

    @Provides
    public DetailPresenter provideDetailPresenter(APIClient apiClient) {
        return new DetailPresenter(apiClient);
    }
}
