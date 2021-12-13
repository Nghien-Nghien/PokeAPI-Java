package com.example.pokemonapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pokemonapi.databinding.ActivityMainBinding;
import com.example.pokemonapi.model.network.RetrofitBuilder;
import com.example.pokemonapi.model.pokemonlist.ResultsResponse;
import com.example.pokemonapi.repository.Contracts;
import com.example.pokemonapi.repository.MainPresenter;
import com.example.pokemonapi.repository.Model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Contracts.MainView, PokemonRecyclerViewListAdapter.OnItemClickListener {

    public ActivityMainBinding activityMainBinding;
    public PokemonRecyclerViewListAdapter pokemonRecyclerViewListAdapter;
    public GridLayoutManager gridLayoutManager;
    public MainPresenter mainPresenter;
    public int visibleItemCount;
    public int totalItemCount;
    public int pastVisibleItems;
    public int THRESHOLD = 4;
    public static int offset = 0;
    public boolean loading = true;
    public static final String EXTRA_NAME_PARAM = "Name Pokemon";
    public static final String EXTRA_IMAGE_PARAM = "Image Pokemon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mainPresenter = new MainPresenter(this, new Model(new RetrofitBuilder()));

        setUpRecyclerView();
        loadMoreOnRecyclerView();
        fetchPokemonList(offset);
        pullToRefresh(offset);
    }

    public void fetchPokemonList(int offset) {
        mainPresenter.fetchPokemonList(offset);
    }

    public void setUpRecyclerView() {
        activityMainBinding.pokemonList.setHasFixedSize(true);
        pokemonRecyclerViewListAdapter = new PokemonRecyclerViewListAdapter(this, this, new PokemonRecyclerViewListAdapter.PokemonDiff());
        activityMainBinding.pokemonList.setAdapter(pokemonRecyclerViewListAdapter);

        gridLayoutManager = (GridLayoutManager) activityMainBinding.pokemonList.getLayoutManager();
    }

    public void loadMoreOnRecyclerView() {
        activityMainBinding.pokemonList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    public void pullToRefresh(int offset) {
        activityMainBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pokemonRecyclerViewListAdapter.clearAllOldData();
                fetchPokemonList(offset);
                MainActivity.offset = 0; // the offset need to be reset to 0 bcz
                                        // the loadMoreOnRecyclerView method stored the value of the offset from last time scrolled
            }
        });
    }

    @Override
    public void onOnlineResponse(List<ResultsResponse> dataOnline) {
        loading = true;
        activityMainBinding.swipeRefreshLayout.setRefreshing(false);
        pokemonRecyclerViewListAdapter.refreshPokemonList(dataOnline);
    }

    @Override
    public void onOfflineResponse(List<ResultsResponse> dataOffline) {
        loading = true;
        activityMainBinding.swipeRefreshLayout.setRefreshing(false);
        pokemonRecyclerViewListAdapter.refreshPokemonList(dataOffline);
    }

    @Override
    public void onFailure(String errorCode) {
        Toast.makeText(MainActivity.this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastForOfflineMode() {
        Toast.makeText(MainActivity.this, getResources().getString(R.string.ToastForOfflineMode), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        activityMainBinding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        activityMainBinding.progressBar.setVisibility(View.GONE);
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

    @Override
    public void onClick(String namePoke, String imagePoke) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_NAME_PARAM, namePoke);
        intent.putExtra(EXTRA_IMAGE_PARAM, imagePoke);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.getDisposableToUnsubscribe();
    }
}