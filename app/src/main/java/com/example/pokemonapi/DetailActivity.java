package com.example.pokemonapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemonapi.database.DatabaseBuilder;
import com.example.pokemonapi.database.PokemonInfoDAO;
import com.example.pokemonapi.network.RetrofitBuilder;
import com.example.pokemonapi.network.pokemoninfo.PokemonInfoAPI;
import com.example.pokemonapi.network.pokemoninfo.TypesResponse;
import com.example.pokemonapi.repository.DetailRepository;
import com.example.pokemonapi.repository.OnEnterDetailRepository;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;
import com.google.android.material.card.MaterialCardView;
import com.skydoves.progressview.ProgressView;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    public TypeRecyclerViewListAdapter typeRecyclerViewListAdapter;
    public RecyclerView recyclerView;
    public ProgressBar progressBar;
    public DetailRepository detailRepository;

    MaterialCardView cardView;
    ImageButton arrowButton;
    ImageView imagePoke;
    TextView namePoke;
    TextView weight;
    TextView height;
    ProgressView progress_hp;
    ProgressView progress_atk;
    ProgressView progress_def;
    ProgressView progress_spd;
    ProgressView progress_exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        cardView = findViewById(R.id.cardView);
        arrowButton = findViewById(R.id.arrow);
        imagePoke = findViewById(R.id.imagePoke);
        namePoke = findViewById(R.id.namePoke);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        progress_hp = findViewById(R.id.progress_hp);
        progress_atk = findViewById(R.id.progress_atk);
        progress_def = findViewById(R.id.progress_def);
        progress_spd = findViewById(R.id.progress_spd);
        progress_exp = findViewById(R.id.progress_exp);

        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.typeList);
        recyclerView.setHasFixedSize(true);

        typeRecyclerViewListAdapter = new TypeRecyclerViewListAdapter(this, new TypeRecyclerViewListAdapter.TypeDiff());
        recyclerView.setAdapter(typeRecyclerViewListAdapter);

        Intent intent = getIntent();

        String namePoke = intent.getStringExtra("NamePoke");
        String imagePoke = intent.getStringExtra("ImagePoke");

        this.namePoke.setText(namePoke);
        Glide.with(this).load(imagePoke).placeholder(R.drawable.placeholder).into(this.imagePoke);

        Glide.with(this).load(imagePoke)
                .listener(
                        GlidePalette.with(imagePoke).use(BitmapPalette.Profile.MUTED_LIGHT)
                                .intoCallBack(new BitmapPalette.CallBack() {
                                    @Override
                                    public void onPaletteLoaded(Palette palette) {
                                        if (palette != null && palette.getDominantSwatch() != null) {
                                            int rgbHexCode = palette.getDominantSwatch().getRgb();
                                            cardView.setCardBackgroundColor(rgbHexCode);
                                        } else {
                                            cardView.setCardBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.default_backgroundCardView));
                                        }
                                    }
                                }).crossfade(true))
                .into(this.imagePoke);

        RetrofitBuilder retrofitBuilder = new RetrofitBuilder();
        PokemonInfoDAO pokemonInfoDAO = new DatabaseBuilder(this).databaseBuilder().pokemonInfoDAO();
        PokemonInfoAPI basePerformance = new PokemonInfoAPI();
        detailRepository = new DetailRepository(retrofitBuilder, pokemonInfoDAO, basePerformance);

        detailRepository.setListener(new OnEnterDetailRepository() {
            @Override
            public void onOnlineResponse(List<TypesResponse> types, String heightFormatted, String weightFormatted, Float hpFormatted, Float atkFormatted, Float defFormatted, Float spdFormatted, Float expFormatted, String hpString, String atkString, String defString, String spdString, String expString) {
                typeRecyclerViewListAdapter.refreshTypeList(types);
                height.setText(heightFormatted);
                weight.setText(weightFormatted);
                progress_hp.setProgress(hpFormatted);
                progress_atk.setProgress(atkFormatted);
                progress_def.setProgress(defFormatted);
                progress_spd.setProgress(spdFormatted);
                progress_exp.setProgress(expFormatted);
                progress_hp.setLabelText(hpString);
                progress_atk.setLabelText(atkString);
                progress_def.setLabelText(defString);
                progress_spd.setLabelText(spdString);
                progress_exp.setLabelText(expString);
                hideProgressBar();
            }

            @Override
            public void onOfflineResponse(PokemonInfoAPI dataOffline) {
                typeRecyclerViewListAdapter.refreshTypeList(dataOffline.types);
                height.setText(dataOffline.heightFormatted);
                weight.setText(dataOffline.weightFormatted);
                progress_hp.setProgress(dataOffline.hpFormatted);
                progress_atk.setProgress(dataOffline.atkFormatted);
                progress_def.setProgress(dataOffline.defFormatted);
                progress_spd.setProgress(dataOffline.spdFormatted);
                progress_exp.setProgress(dataOffline.expFormatted);
                progress_hp.setLabelText(dataOffline.hpString);
                progress_atk.setLabelText(dataOffline.atkString);
                progress_def.setLabelText(dataOffline.defString);
                progress_spd.setLabelText(dataOffline.spdString);
                progress_exp.setLabelText(dataOffline.expString);
                hideProgressBar();
            }

            @Override
            public void onFailure(Throwable throwable) {
                hideProgressBar();
                Toast.makeText(DetailActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        setArrowButton(arrowButton);
        fetchPokemonInfo(namePoke);
    }

    public void fetchPokemonInfo(String namePoke) {
        showProgressBar();
        detailRepository.fetchPokemonInfo(namePoke);
    }

    public void setArrowButton(ImageButton arrowButton) {
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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