package com.example.pokemonapi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pokemonapi.adapters.TypeRecyclerViewListAdapter;
import com.example.pokemonapi.databinding.FragmentDetailBinding;
import com.example.pokemonapi.model.pokemoninfo.PokemonInfoAPI;
import com.example.pokemonapi.model.pokemoninfo.TypesResponse;
import com.example.pokemonapi.repository.Contracts;
import com.example.pokemonapi.repository.DetailPresenter;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DetailFragment extends Fragment implements Contracts.DetailView {

    public FragmentDetailBinding fragmentDetailBinding;
    public TypeRecyclerViewListAdapter typeRecyclerViewListAdapter;
    public DetailPresenter detailPresenter;
    public String namePoke;
    public String imagePoke;

    public DetailFragment(String namePoke, String imagePoke) {
        this.namePoke = namePoke;
        this.imagePoke = imagePoke;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailPresenter = new DetailPresenter(this);
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
        setUpDataGetFromMainFragment(namePoke, imagePoke);
        setArrowButton(fragmentDetailBinding.arrow);
        fetchPokemonInfo(namePoke);
    }

    public void setUpRecyclerView() {
        fragmentDetailBinding.typeList.setHasFixedSize(true);
        typeRecyclerViewListAdapter = new TypeRecyclerViewListAdapter(getContext(), new TypeRecyclerViewListAdapter.TypeDiff());
        fragmentDetailBinding.typeList.setAdapter(typeRecyclerViewListAdapter);
    }

    public void setUpDataGetFromMainFragment(String namePoke, String imagePoke) {
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

    public void setArrowButton(ImageButton arrowButton) {
        arrowButton.setOnClickListener(view -> {
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }

    public void fetchPokemonInfo(String namePoke) {
        detailPresenter.fetchPokemonInfo(namePoke);
    }

    @Override
    public void onOnlineResponse(List<TypesResponse> types, String heightFormatted, String weightFormatted, Float hpFormatted, Float atkFormatted, Float defFormatted, Float spdFormatted, Float expFormatted, String hpString, String atkString, String defString, String spdString, String expString) {
        typeRecyclerViewListAdapter.refreshTypeList(types);
        fragmentDetailBinding.height.setText(heightFormatted);
        fragmentDetailBinding.weight.setText(weightFormatted);
        fragmentDetailBinding.progressHp.setProgress(hpFormatted);
        fragmentDetailBinding.progressAtk.setProgress(atkFormatted);
        fragmentDetailBinding.progressDef.setProgress(defFormatted);
        fragmentDetailBinding.progressSpd.setProgress(spdFormatted);
        fragmentDetailBinding.progressExp.setProgress(expFormatted);
        fragmentDetailBinding.progressHp.setLabelText(hpString);
        fragmentDetailBinding.progressAtk.setLabelText(atkString);
        fragmentDetailBinding.progressDef.setLabelText(defString);
        fragmentDetailBinding.progressSpd.setLabelText(spdString);
        fragmentDetailBinding.progressExp.setLabelText(expString);
    }

    @Override
    public void onOfflineResponse(PokemonInfoAPI dataOffline) {
        typeRecyclerViewListAdapter.refreshTypeList(dataOffline.types);
        fragmentDetailBinding.height.setText(dataOffline.heightFormatted);
        fragmentDetailBinding.weight.setText(dataOffline.weightFormatted);
        fragmentDetailBinding.progressHp.setProgress(dataOffline.hpFormatted);
        fragmentDetailBinding.progressAtk.setProgress(dataOffline.atkFormatted);
        fragmentDetailBinding.progressDef.setProgress(dataOffline.defFormatted);
        fragmentDetailBinding.progressSpd.setProgress(dataOffline.spdFormatted);
        fragmentDetailBinding.progressExp.setProgress(dataOffline.expFormatted);
        fragmentDetailBinding.progressHp.setLabelText(dataOffline.hpString);
        fragmentDetailBinding.progressAtk.setLabelText(dataOffline.atkString);
        fragmentDetailBinding.progressDef.setLabelText(dataOffline.defString);
        fragmentDetailBinding.progressSpd.setLabelText(dataOffline.spdString);
        fragmentDetailBinding.progressExp.setLabelText(dataOffline.expString);
    }

    @Override
    public void onFailure(String errorCode) {
        Toast.makeText(getContext(), errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastForOfflineMode() {
        Toast.makeText(getContext(), getResources().getString(R.string.ToastForOfflineMode), Toast.LENGTH_SHORT).show();
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