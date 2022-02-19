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
import com.example.pokemonapi.repository.MainRepository;
import com.example.pokemonapi.viewmodel.MainViewModel;

import org.jetbrains.annotations.NotNull;

public class MainFragment extends Fragment implements PokemonRecyclerViewListAdapter.OnItemClickListener {

    private FragmentMainBinding fragmentMainBinding;
    private PokemonRecyclerViewListAdapter pokemonRecyclerViewListAdapter;
    private MainRepository mainRepository;
    private MainViewModel mainViewModel;
    private int offset;
    private boolean checking0, checking1, checking2;
    private boolean loading = true;
    private final String STATE_OFFSET = "Current Offset";
    public static final String EXTRA_NAME_PARAM = "Name Pokemon";
    public static final String EXTRA_IMAGE_PARAM = "Image Pokemon";

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainRepository = new MainRepository();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false);
        setUpRecyclerView();
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        return fragmentMainBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMoreOnRecyclerView();
        pullToRefresh();
        updateDataForUI();
        updateProgressBarForUI();
        updateSwipeRefreshLayoutForUI();
        updateToastForUI();
        handleOnBackPressed();

        if (savedInstanceState == null) {
            checking0 = true;
            checking1 = true;
            checking2 = true;
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
        mainRepository.fetchPokemonList(offset);
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

    private void pullToRefresh() {
        fragmentMainBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            pokemonRecyclerViewListAdapter.clearAllOldData();
            offset = 0; // the offset need to be reset to 0 bcz
            fetchPokemonList(offset); // the loadMoreOnRecyclerView method stored the value of the offset from last time scrolled
        });
    }

    private void updateDataForUI() {
        mainViewModel.getPokemonListLiveData().observe(getViewLifecycleOwner(), pokemonList -> {
            if (pokemonList != null) {
                pokemonRecyclerViewListAdapter.refreshPokemonList(pokemonList);
            }
        });
    }

    private void updateProgressBarForUI() {
        mainViewModel.getProgressBarLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if (checking0 && aBoolean != null) {
                if (aBoolean) {
                    fragmentMainBinding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    fragmentMainBinding.progressBar.setVisibility(View.GONE);
                }
            } else {
                checking0 = true;
            }
        });
    }

    private void updateSwipeRefreshLayoutForUI() {
        mainViewModel.getSwipeRefreshLayoutLiveData().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null) {
                if (checking1 && aBoolean) {
                    loading = true;
                    fragmentMainBinding.swipeRefreshLayout.setRefreshing(false);
                } else {
                    checking1 = true;
                }
            }
        });
    }

    private void updateToastForUI() {
        mainViewModel.getToastLiveData().observe(getViewLifecycleOwner(), string -> {
            if (string != null) {
                if (checking2) {
                    if (string.isEmpty()) {
                        Toast.makeText(requireActivity(), getResources().getString(R.string.ToastForOfflineMode), Toast.LENGTH_SHORT).show();
                    } else {
                        offset -= 20;
                        loading = true;
                        fragmentMainBinding.swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(requireActivity(), string, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    checking2 = true;
                }
            }
        });
    }

    private void handleOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mainRepository.resetValuesLiveData();
                requireActivity().finish();
            }
        });
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
        mainRepository.getDisposableToUnsubscribe();
    }
}