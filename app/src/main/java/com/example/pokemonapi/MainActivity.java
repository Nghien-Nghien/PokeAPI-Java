package com.example.pokemonapi;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapi.database.DatabaseBuilder;
import com.example.pokemonapi.database.PokemonListDAO;
import com.example.pokemonapi.network.RetrofitBuilder;
import com.example.pokemonapi.network.pokemonlist.ResultsResponse;
import com.example.pokemonapi.repository.MainRepository;
import com.example.pokemonapi.repository.OnEnterMainRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public PokemonRecyclerViewListAdapter pokemonRecyclerViewListAdapter;
    public RecyclerView recyclerView;
    public GridLayoutManager gridLayoutManager;
    public ProgressBar progressBar;
    public MainRepository mainRepository;
    public int visibleItemCount;
    public int totalItemCount;
    public int pastVisibleItems;
    public int THRESHOLD = 4;
    public int offset = 0;
    public boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.pokemonList);
        recyclerView.setHasFixedSize(true);

        pokemonRecyclerViewListAdapter = new PokemonRecyclerViewListAdapter(this, new PokemonRecyclerViewListAdapter.PokemonDiff());
        recyclerView.setAdapter(pokemonRecyclerViewListAdapter);

        gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

        RetrofitBuilder retrofitBuilder = new RetrofitBuilder();
        PokemonListDAO pokemonListDAO = new DatabaseBuilder(this).databaseBuilder().pokemonListDAO();
        mainRepository = new MainRepository(retrofitBuilder, pokemonListDAO);

        mainRepository.setListener(new OnEnterMainRepository() {
            @Override
            public void onOnlineResponse(List<ResultsResponse> dataOnline) {
                loading = true;
                pokemonRecyclerViewListAdapter.refreshPokemonList(dataOnline);
                hideProgressBar();
            }

            @Override
            public void onOfflineResponse(List<ResultsResponse> dataOffline) {
                loading = true;
                pokemonRecyclerViewListAdapter.refreshPokemonList(dataOffline);
                hideProgressBar();
            }

            @Override
            public void onFailure(Throwable throwable) {
                loading = true;
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        });

        fetchPokemonList(offset);
        loadMoreOnRecyclerView();
    }

    public void fetchPokemonList(int offset) {
        showProgressBar();
        mainRepository.fetchPokemonList(offset);
    }

    public void loadMoreOnRecyclerView() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading && (pastVisibleItems + visibleItemCount == totalItemCount - THRESHOLD)) {
                        loading = false;
                        offset += 20;
                        fetchPokemonList(offset);
                    }
                }
            }
        });
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //The IMMERSIVE_STICKY use to hide Navigation Bar after short time don't touch on it
        }
    }
}