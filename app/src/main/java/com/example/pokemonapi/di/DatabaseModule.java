package com.example.pokemonapi.di;

import android.app.Application;

import androidx.room.Room;

import com.example.pokemonapi.database.AppDatabase;
import com.example.pokemonapi.database.PokemonInfoDAO;
import com.example.pokemonapi.database.PokemonListDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private final Application application;

    public DatabaseModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, "PokemonDatabase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public PokemonListDAO providePokemonListDAO(AppDatabase appDatabase) {
        return appDatabase.pokemonListDAO();
    }

    @Provides
    @Singleton
    public PokemonInfoDAO providePokemonInfoDAO(AppDatabase appDatabase) {
        return appDatabase.pokemonInfoDAO();
    }
}
