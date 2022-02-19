package com.example.pokemonapi.di;

import com.example.pokemonapi.repository.DetailRepository;
import com.example.pokemonapi.repository.MainRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, DatabaseModule.class})
public interface AppComponent {

    void injectMainRepository(MainRepository mainRepository);

    void injectDetailRepository(DetailRepository detailRepository);
}
