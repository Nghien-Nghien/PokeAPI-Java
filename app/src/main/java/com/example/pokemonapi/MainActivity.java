package com.example.pokemonapi;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapi.network.RetrofitBuilder;
import com.example.pokemonapi.network.pokemonlist.PokemonListAPI;
import com.example.pokemonapi.network.pokemonlist.ResultsResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private PokemonRecyclerViewAdapter mPokemonRecyclerViewAdapter;
    private List<PokemonItem> mPokemonItemList;
    private RetrofitBuilder retrofitBuilder;
    public RecyclerView mRecyclerView;
    public ProgressBar progressBar;
    public int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        progressBar = findViewById(R.id.progressBar);

        mRecyclerView = findViewById(R.id.rvList);
        mRecyclerView.setHasFixedSize(true);

        mPokemonRecyclerViewAdapter = new PokemonRecyclerViewAdapter(MainActivity.this);
        mRecyclerView.setAdapter(mPokemonRecyclerViewAdapter);

        retrofitBuilder = new RetrofitBuilder();

        fetchPokemonList(offset);

        loadMoreOnRecyclerView();
    }

    public void fetchPokemonList(int offset) {
        mPokemonItemList = new ArrayList<>();

        Call<PokemonListAPI> call = retrofitBuilder.requestToApiInterface().fetchPokemonList(offset);

        //noinspection NullableProblems
        call.enqueue(new Callback<PokemonListAPI>() {
            @Override
            public void onResponse(Call<PokemonListAPI> call, Response<PokemonListAPI> response) {
                assert response.body() != null;
                List<ResultsResponse> resultsList = response.body().getResults();

                for (int i = 0; i < resultsList.size(); i++) {
                    ResultsResponse result = resultsList.get(i);

                    String namePoke = result.getName();

                    String imagePoke = result.getUrl().replaceFirst(".$", "").substring(33);

                    mPokemonItemList.add(new PokemonItem(namePoke, imagePoke));
                }

                mPokemonRecyclerViewAdapter.refreshPokemonList(mPokemonItemList);
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<PokemonListAPI> call, Throwable throwable) {
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadMoreOnRecyclerView() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) && dy != 0) {
                    showProgressBar();

                    offset += 20;
                    fetchPokemonList(offset);
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