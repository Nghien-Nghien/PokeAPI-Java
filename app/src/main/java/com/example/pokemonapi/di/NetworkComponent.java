package com.example.pokemonapi.di;

import com.example.pokemonapi.network.APIClient;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkComponent {

    APIClient getAPIClient();
}
