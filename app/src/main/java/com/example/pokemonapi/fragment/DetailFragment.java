package com.example.pokemonapi.fragment;

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

import com.bumptech.glide.Glide;
import com.example.pokemonapi.R;
import com.example.pokemonapi.adapter.TypeRecyclerViewListAdapter;
import com.example.pokemonapi.databinding.FragmentDetailBinding;
import com.example.pokemonapi.repository.Contracts;
import com.example.pokemonapi.repository.DetailPresenter;
import com.example.pokemonapi.viewmodel.DetailViewModel;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;

import org.jetbrains.annotations.NotNull;

public class DetailFragment extends Fragment implements Contracts.DetailView {

    private FragmentDetailBinding fragmentDetailBinding;
    private TypeRecyclerViewListAdapter typeRecyclerViewListAdapter;
    private DetailPresenter detailPresenter;
    private DetailViewModel detailViewModel;
    private String namePoke;
    private String imagePoke;
    private final String STATE_NAME = "Current Name";
    private final String STATE_IMAGE = "Current Image";

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailPresenter = new DetailPresenter(this);

        if (savedInstanceState == null) {
            namePoke = requireActivity().getIntent().getExtras().getString(MainFragment.EXTRA_NAME_PARAM);
            imagePoke = requireActivity().getIntent().getExtras().getString(MainFragment.EXTRA_IMAGE_PARAM);
        } else {
            namePoke = savedInstanceState.getString(STATE_NAME);
            imagePoke = savedInstanceState.getString(STATE_IMAGE);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentDetailBinding = FragmentDetailBinding.inflate(inflater, container, false);
        setUpRecyclerView();

        return fragmentDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpDataGetFromMainFragment();
        setArrowButton();
        updateDataForUI();
        handleOnBackPressed();

        if (savedInstanceState == null) {
            fetchPokemonInfo(namePoke);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_NAME, namePoke);
        outState.putString(STATE_IMAGE, imagePoke);
    }

    private void setUpRecyclerView() {
        fragmentDetailBinding.typeList.setHasFixedSize(true);
        typeRecyclerViewListAdapter = new TypeRecyclerViewListAdapter(requireActivity(), new TypeRecyclerViewListAdapter.TypeDiff());
        fragmentDetailBinding.typeList.setAdapter(typeRecyclerViewListAdapter);
    }

    private void setUpDataGetFromMainFragment() {
        fragmentDetailBinding.namePoke.setText(namePoke);
        Glide.with(this).load(imagePoke).placeholder(R.drawable.placeholder).into(fragmentDetailBinding.imagePoke);

        Glide.with(this).load(imagePoke)
                .listener(
                        GlidePalette.with(imagePoke).use(BitmapPalette.Profile.MUTED_LIGHT)
                                .intoCallBack(palette -> {
                                    if (palette != null && palette.getDominantSwatch() != null) {
                                        int rgbHexCode = palette.getDominantSwatch().getRgb();
                                        fragmentDetailBinding.cardView.setCardBackgroundColor(rgbHexCode);
                                    }
                                }).crossfade(true))
                .into(fragmentDetailBinding.imagePoke);
    }

    private void setArrowButton() {
        fragmentDetailBinding.arrow.setOnClickListener(view -> {
            detailViewModel.getLiveData().removeObservers(getViewLifecycleOwner());
            requireActivity().finish();
        });
    }

    private void fetchPokemonInfo(String namePoke) {
        detailPresenter.fetchPokemonInfo(namePoke);
    }

    private void updateDataForUI() {
        detailViewModel = new ViewModelProvider(requireActivity()).get(DetailViewModel.class);
        detailViewModel.getLiveData().observe(getViewLifecycleOwner(), pokemonInfoAPI -> {
            if (namePoke.equals(pokemonInfoAPI.name)) {
                typeRecyclerViewListAdapter.refreshTypeList(pokemonInfoAPI.types);
                fragmentDetailBinding.height.setText(pokemonInfoAPI.heightFormatted);
                fragmentDetailBinding.weight.setText(pokemonInfoAPI.weightFormatted);
                fragmentDetailBinding.progressHp.setProgress(pokemonInfoAPI.hpFormatted);
                fragmentDetailBinding.progressAtk.setProgress(pokemonInfoAPI.atkFormatted);
                fragmentDetailBinding.progressDef.setProgress(pokemonInfoAPI.defFormatted);
                fragmentDetailBinding.progressSpd.setProgress(pokemonInfoAPI.spdFormatted);
                fragmentDetailBinding.progressExp.setProgress(pokemonInfoAPI.expFormatted);
                fragmentDetailBinding.progressHp.setLabelText(pokemonInfoAPI.hpString);
                fragmentDetailBinding.progressAtk.setLabelText(pokemonInfoAPI.atkString);
                fragmentDetailBinding.progressDef.setLabelText(pokemonInfoAPI.defString);
                fragmentDetailBinding.progressSpd.setLabelText(pokemonInfoAPI.spdString);
                fragmentDetailBinding.progressExp.setLabelText(pokemonInfoAPI.expString);
            }
        });
    }

    private void handleOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                detailViewModel.getLiveData().removeObservers(getViewLifecycleOwner());
                requireActivity().finish();
            }
        });
    }

    @Override
    public void onFailure(String errorCode) {
        Toast.makeText(requireActivity(), errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastForOfflineMode() {
        Toast.makeText(requireActivity(), getResources().getString(R.string.ToastForOfflineMode), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        fragmentDetailBinding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        fragmentDetailBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        detailPresenter.getDisposableToUnsubscribe();
    }
}