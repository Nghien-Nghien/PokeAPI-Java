package com.example.pokemonapi;

import android.content.Intent;
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

import com.example.pokemonapi.adapters.PokemonRecyclerViewListAdapter;
import com.example.pokemonapi.databinding.FragmentMainBinding;
import com.example.pokemonapi.model.pokemonlist.ResultsResponse;
import com.example.pokemonapi.repository.Contracts;
import com.example.pokemonapi.repository.MainPresenter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainFragment extends Fragment implements Contracts.MainView, PokemonRecyclerViewListAdapter.OnItemClickListener {

    public FragmentMainBinding fragmentMainBinding;
    public PokemonRecyclerViewListAdapter pokemonRecyclerViewListAdapter;
    public GridLayoutManager gridLayoutManager;
    public MainPresenter mainPresenter;
    public int visibleItemCount;
    public int totalItemCount;
    public int pastVisibleItems;
    public int THRESHOLD = 4;
    public int offset = 0;
    public boolean loading = true;
    public static final String EXTRA_NAME_PARAM = "Name Pokemon";
    public static final String EXTRA_IMAGE_PARAM = "Image Pokemon";

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainPresenter = new MainPresenter(this);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false);
        setUpRecyclerView();

        return fragmentMainBinding.getRoot();
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
        pokemonRecyclerViewListAdapter = new PokemonRecyclerViewListAdapter(getContext(), this, new PokemonRecyclerViewListAdapter.PokemonDiff());
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
                    boolean countItemsForLoadMore = pastVisibleItems + visibleItemCount == totalItemCount - THRESHOLD;

                    if (loading && countItemsForLoadMore) {
                        loading = false;
                        offset += 20;
                        fetchPokemonList(offset);
                    }

                    if (pokemonRecyclerViewListAdapter.getItemCount() == (offset + 20) && countItemsForLoadMore) {
                        loading = true;
                    }

                    if (pokemonRecyclerViewListAdapter.getItemCount() == offset && !recyclerView.canScrollVertically(1)) {
                        offset -= 20;
                        loading = true;
                    }
                }
            }
        });
    }

    public void pullToRefresh(int offset) {
        fragmentMainBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            pokemonRecyclerViewListAdapter.clearAllOldData();
            fetchPokemonList(offset);
            this.offset = 0; // the offset need to be reset to 0 bcz
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
        fragmentMainBinding.swipeRefreshLayout.setRefreshing(false);
        pokemonRecyclerViewListAdapter.refreshPokemonList(dataOffline);
    }

    @Override
    public void onFailure(String errorCode) {
        fragmentMainBinding.swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastForOfflineMode() {
        Toast.makeText(getContext(), getResources().getString(R.string.ToastForOfflineMode), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(EXTRA_NAME_PARAM, namePoke);
        intent.putExtra(EXTRA_IMAGE_PARAM, imagePoke);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentMainBinding.pokemonList.clearOnScrollListeners();
        offset = 0;
        mainPresenter.getDisposableToUnsubscribe();
    }
}