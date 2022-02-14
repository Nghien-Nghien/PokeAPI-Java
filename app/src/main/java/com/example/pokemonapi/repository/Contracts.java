package com.example.pokemonapi.repository;

public interface Contracts {

    interface MainPresenter {
        void fetchPokemonList(int offset);
    }

    interface DetailPresenter {
        void fetchPokemonInfo(String namePoke);
    }

    interface MainView {
        void onFailure(String errorCode);

        void toastForOfflineMode();

        void showProgressBar();

        void hideProgressBar();

        void setRefreshingForSwipeRefreshLayout();
    }

    interface DetailView {
        void onFailure(String errorCode);

        void toastForOfflineMode();

        void showProgressBar();

        void hideProgressBar();
    }
}
