package com.example.pokemonapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.example.pokemonapi.databinding.ActivityDetailBinding;
import com.example.pokemonapi.model.pokemoninfo.PokemonInfoAPI;
import com.example.pokemonapi.model.pokemoninfo.TypesResponse;
import com.example.pokemonapi.repository.Contracts;
import com.example.pokemonapi.repository.DetailPresenter;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements Contracts.DetailView {

    public ActivityDetailBinding activityDetailBinding;
    public TypeRecyclerViewListAdapter typeRecyclerViewListAdapter;
    public DetailPresenter detailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(activityDetailBinding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        detailPresenter = new DetailPresenter(this);

        Intent intent = getIntent();

        String namePoke = intent.getStringExtra(MainActivity.EXTRA_NAME_PARAM);
        String imagePoke = intent.getStringExtra(MainActivity.EXTRA_IMAGE_PARAM);

        setUpDataGetFromIntent(namePoke, imagePoke);
        setUpRecyclerView();
        setArrowButton(activityDetailBinding.arrow);
        fetchPokemonInfo(namePoke);
    }

    public void fetchPokemonInfo(String namePoke) {
        detailPresenter.fetchPokemonInfo(namePoke);
    }

    public void setUpDataGetFromIntent(String namePoke, String imagePoke) {
        activityDetailBinding.namePoke.setText(namePoke);
        Glide.with(this).load(imagePoke).placeholder(R.drawable.placeholder).into(activityDetailBinding.imagePoke);

        Glide.with(this).load(imagePoke)
                .listener(
                        GlidePalette.with(imagePoke).use(BitmapPalette.Profile.MUTED_LIGHT)
                                .intoCallBack(new BitmapPalette.CallBack() {
                                    @Override
                                    public void onPaletteLoaded(Palette palette) {
                                        if (palette != null && palette.getDominantSwatch() != null) {
                                            int rgbHexCode = palette.getDominantSwatch().getRgb();
                                            activityDetailBinding.cardView.setCardBackgroundColor(rgbHexCode);
                                        }
                                    }
                                }).crossfade(true))
                .into(activityDetailBinding.imagePoke);
    }

    public void setUpRecyclerView() {
        activityDetailBinding.typeList.setHasFixedSize(true);
        typeRecyclerViewListAdapter = new TypeRecyclerViewListAdapter(this, new TypeRecyclerViewListAdapter.TypeDiff());
        activityDetailBinding.typeList.setAdapter(typeRecyclerViewListAdapter);
    }

    public void setArrowButton(ImageButton arrowButton) {
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onOnlineResponse(List<TypesResponse> types, String heightFormatted, String weightFormatted, Float hpFormatted, Float atkFormatted, Float defFormatted, Float spdFormatted, Float expFormatted, String hpString, String atkString, String defString, String spdString, String expString) {
        typeRecyclerViewListAdapter.refreshTypeList(types);
        activityDetailBinding.height.setText(heightFormatted);
        activityDetailBinding.weight.setText(weightFormatted);
        activityDetailBinding.progressHp.setProgress(hpFormatted);
        activityDetailBinding.progressAtk.setProgress(atkFormatted);
        activityDetailBinding.progressDef.setProgress(defFormatted);
        activityDetailBinding.progressSpd.setProgress(spdFormatted);
        activityDetailBinding.progressExp.setProgress(expFormatted);
        activityDetailBinding.progressHp.setLabelText(hpString);
        activityDetailBinding.progressAtk.setLabelText(atkString);
        activityDetailBinding.progressDef.setLabelText(defString);
        activityDetailBinding.progressSpd.setLabelText(spdString);
        activityDetailBinding.progressExp.setLabelText(expString);
    }

    @Override
    public void onOfflineResponse(PokemonInfoAPI dataOffline) {
        typeRecyclerViewListAdapter.refreshTypeList(dataOffline.types);
        activityDetailBinding.height.setText(dataOffline.heightFormatted);
        activityDetailBinding.weight.setText(dataOffline.weightFormatted);
        activityDetailBinding.progressHp.setProgress(dataOffline.hpFormatted);
        activityDetailBinding.progressAtk.setProgress(dataOffline.atkFormatted);
        activityDetailBinding.progressDef.setProgress(dataOffline.defFormatted);
        activityDetailBinding.progressSpd.setProgress(dataOffline.spdFormatted);
        activityDetailBinding.progressExp.setProgress(dataOffline.expFormatted);
        activityDetailBinding.progressHp.setLabelText(dataOffline.hpString);
        activityDetailBinding.progressAtk.setLabelText(dataOffline.atkString);
        activityDetailBinding.progressDef.setLabelText(dataOffline.defString);
        activityDetailBinding.progressSpd.setLabelText(dataOffline.spdString);
        activityDetailBinding.progressExp.setLabelText(dataOffline.expString);
    }

    @Override
    public void onFailure(String errorCode) {
        Toast.makeText(DetailActivity.this, errorCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastForOfflineMode() {
        Toast.makeText(DetailActivity.this, getResources().getString(R.string.ToastForOfflineMode), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        activityDetailBinding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        activityDetailBinding.progressBar.setVisibility(View.GONE);
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
    protected void onDestroy() {
        super.onDestroy();
        detailPresenter.getDisposableToUnsubscribe();
    }
}