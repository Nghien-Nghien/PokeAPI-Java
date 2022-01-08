package com.example.pokemonapi;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapi.databinding.FragmentMainBinding;
import com.example.pokemonapi.model.pokemonlist.ResultsResponse;
import com.example.pokemonapi.repository.Contracts;
import com.example.pokemonapi.repository.MainPresenter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment implements Contracts.MainView, PokemonRecyclerViewListAdapter.OnItemClickListener {

    public FragmentMainBinding fragmentMainBinding;
    public PokemonRecyclerViewListAdapter pokemonRecyclerViewListAdapter;
    public GridLayoutManager gridLayoutManager;
    public MainPresenter mainPresenter;
    public int visibleItemCount;
    public int totalItemCount;
    public int pastVisibleItems;
    public int THRESHOLD = 4;
    public static int offset = 0;
    public boolean loading = true;
    public OnItemClickListener onItemClickListener;

    public Context context;
    public MainFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentMainBinding = FragmentMainBinding.inflate(getLayoutInflater());

        mainPresenter = new MainPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setUpRecyclerView();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadMoreOnRecyclerView();
        fetchPokemonList(offset);
        pullToRefresh(offset);
    }

    public void setUpRecyclerView() {
        fragmentMainBinding.pokemonList.setHasFixedSize(true);
        pokemonRecyclerViewListAdapter = new PokemonRecyclerViewListAdapter(context, this, new PokemonRecyclerViewListAdapter.PokemonDiff());
        fragmentMainBinding.pokemonList.setAdapter(pokemonRecyclerViewListAdapter);

        gridLayoutManager = (GridLayoutManager) fragmentMainBinding.pokemonList.getLayoutManager();
    }

    public void fetchPokemonList(int offset) {
        mainPresenter.fetchPokemonList(offset);
    }

    public void loadMoreOnRecyclerView() {
        fragmentMainBinding.pokemonList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        fragmentMainBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            pokemonRecyclerViewListAdapter.clearAllOldData();
            fetchPokemonList(offset);
            MainFragment.offset = 0; // the offset need to be reset to 0 bcz
            // the loadMoreOnRecyclerView method stored the value of the offset from last time scrolled
        });
    }

    @Override
    public void onOnlineResponse(List<ResultsResponse> dataOnline) {
        loading = true;
        fragmentMainBinding.swipeRefreshLayout.setRefreshing(false);
        pokemonRecyclerViewListAdapter.refreshPokemonList(dataOnline);
    }

    @Override
    public void onOfflineResponse(List<ResultsResponse> dataOffline) {
        loading = true;
        fragmentMainBinding.swipeRefreshLayout.setRefreshing(false);
        pokemonRecyclerViewListAdapter.refreshPokemonList(dataOffline);
    }

    @Override
    public void onFailure(String errorCode) {
        Toast.makeText(getActivity(), errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastForOfflineMode() {
        Toast.makeText(getActivity(), getResources().getString(R.string.ToastForOfflineMode), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        fragmentMainBinding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        fragmentMainBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(String namePoke, String imagePoke) {
        onItemClickListener.onClick(namePoke, imagePoke);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentMainBinding.pokemonList.clearOnScrollListeners();
        MainFragment.offset = 0;
        mainPresenter.getDisposableToUnsubscribe();
    }

    public interface OnItemClickListener {
        void onClick(String namePoke, String imagePoke);
    }
}