package com.example.pokemonapi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapi.R;
import com.example.pokemonapi.activity.DetailActivity;
import com.example.pokemonapi.adapter.PokemonRecyclerViewListAdapter;
import com.example.pokemonapi.databinding.FragmentMainBinding;
import com.example.pokemonapi.repository.Contracts;
import com.example.pokemonapi.repository.MainPresenter;
import com.example.pokemonapi.viewmodel.MainViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainFragment extends Fragment implements Contracts.MainView, PokemonRecyclerViewListAdapter.OnItemClickListener {

    private FragmentMainBinding fragmentMainBinding;
    private PokemonRecyclerViewListAdapter pokemonRecyclerViewListAdapter;
    private MainPresenter mainPresenter;
    private MainViewModel mainViewModel;
    private int offset;
    private boolean loading = true;
    private final String STATE_OFFSET = "Current Offset";
    public static final String EXTRA_NAME_PARAM = "Name Pokemon";
    public static final String EXTRA_IMAGE_PARAM = "Image Pokemon";

    public MainFragment() {
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
        pullToRefresh();
        updateDataForUI();
        handleOnBackPressed();

        if (savedInstanceState == null) {
            offset = 0;
            fetchPokemonList(offset);
        } else {
            offset = savedInstanceState.getInt(STATE_OFFSET);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_OFFSET, offset);
    }

    private void setUpRecyclerView() {
        fragmentMainBinding.pokemonList.setHasFixedSize(true);
        pokemonRecyclerViewListAdapter = new PokemonRecyclerViewListAdapter(requireActivity(), this, new PokemonRecyclerViewListAdapter.PokemonDiff());
        fragmentMainBinding.pokemonList.setAdapter(pokemonRecyclerViewListAdapter);
    }

    private void fetchPokemonList(int offset) {
        mainPresenter.fetchPokemonList(offset);
    }

    private void loadMoreOnRecyclerView() {
        fragmentMainBinding.pokemonList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // dy > 0 = check for scroll down
                    if (loading && !recyclerView.canScrollVertically(1)) {
                        loading = false;
                        offset += 20;
                        fetchPokemonList(offset);
                    }
                }
            }
        });
    }

    private void decreaseOffset(int currentOffsetValue) {
        if (pokemonRecyclerViewListAdapter.getItemCount() <= currentOffsetValue) {
            offset = pokemonRecyclerViewListAdapter.getItemCount() - 20;
            loading = true;
        }
    }

    private void pullToRefresh() {
        fragmentMainBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            pokemonRecyclerViewListAdapter.clearAllOldData();
            offset = 0; // the offset need to be reset to 0 bcz
            fetchPokemonList(offset); // the loadMoreOnRecyclerView method stored the value of the offset from last time scrolled
        });
    }

    private void updateDataForUI() {
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.getLiveData().observe(getViewLifecycleOwner(), resultsResponses -> pokemonRecyclerViewListAdapter.refreshPokemonList(resultsResponses));
    }

    private void handleOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Objects.requireNonNull(mainViewModel.getLiveData().getValue()).clear();
                requireActivity().finish();
            }
        });
    }

    @Override
    public void onFailure(String errorCode) {
        decreaseOffset(offset); // Call this method when fetchPokemonList(offset) be failed on both network & database
        fragmentMainBinding.swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(requireActivity(), errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastForOfflineMode() {
        Toast.makeText(requireActivity(), getResources().getString(R.string.ToastForOfflineMode), Toast.LENGTH_SHORT).show();
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
    public void setRefreshingForSwipeRefreshLayout() {
        loading = true;
        fragmentMainBinding.swipeRefreshLayout.setRefreshing(false);
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
        mainPresenter.getDisposableToUnsubscribe();
    }
}