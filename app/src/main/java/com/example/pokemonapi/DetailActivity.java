package com.example.pokemonapi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemonapi.network.RetrofitBuilder;
import com.example.pokemonapi.network.pokemoninfo.PokemonInfoAPI;
import com.example.pokemonapi.network.pokemoninfo.TypesResponse;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;
import com.google.android.material.card.MaterialCardView;
import com.skydoves.progressview.ProgressView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private RetrofitBuilder retrofitBuilder;
    public PokemonInfoAPI basePerformance;
    private TypeRecyclerViewAdapter mTypeRecyclerViewAdapter;
    private List<TypeItem> mTypeItemList;

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
    RecyclerView mRecyclerView;

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
        mRecyclerView = findViewById(R.id.typesView);

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

        retrofitBuilder = new RetrofitBuilder();
        basePerformance = new PokemonInfoAPI();

        mTypeRecyclerViewAdapter = new TypeRecyclerViewAdapter(DetailActivity.this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mTypeRecyclerViewAdapter);

        setArrowButton(arrowButton);

        fetchPokemonInfo(namePoke);
    }

    public void setArrowButton(ImageButton arrowButton) {
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void fetchPokemonInfo(String namePoke) {
        mTypeItemList = new ArrayList<>();

        Call<PokemonInfoAPI> call = retrofitBuilder.requestToApiInterface().fetchPokemonInfo(namePoke);

        //noinspection NullableProblems
        call.enqueue(new Callback<PokemonInfoAPI>() {
            @Override
            public void onResponse(Call<PokemonInfoAPI> call, Response<PokemonInfoAPI> response) {
                assert response.body() != null;

                //Get these info: weight, height
                PokemonInfoAPI baseInfo = response.body();

                @SuppressLint("DefaultLocale") String weightPoke = String.format("%.1f KG", (float) baseInfo.getWeight() / 10);
                @SuppressLint("DefaultLocale") String heightPoke = String.format("%.1f M", (float) baseInfo.getHeight() / 10);

                weight.setText(weightPoke);
                height.setText(heightPoke);

                //Get Base Performance
                progress_hp.setProgress((float) basePerformance.hp);
                progress_hp.setLabelText(basePerformance.getHPString());
                progress_atk.setProgress((float) basePerformance.atk);
                progress_atk.setLabelText(basePerformance.getATKString());
                progress_def.setProgress((float) basePerformance.def);
                progress_def.setLabelText(basePerformance.getDEFString());
                progress_spd.setProgress((float) basePerformance.spd);
                progress_spd.setLabelText(basePerformance.getSPDString());
                progress_exp.setProgress((float) basePerformance.exp);
                progress_exp.setLabelText(basePerformance.getEXPString());

                //Get name of types Pokemon and Color Types
                List<TypesResponse> typesList = response.body().getTypes();

                for (int i = 0; i < typesList.size(); i++) {
                    TypesResponse type = typesList.get(i);

                    String nameType = type.getType().getName();

                    mTypeItemList.add(new TypeItem(nameType));
                }

                mTypeRecyclerViewAdapter.refreshTypeList(mTypeItemList);
            }

            @Override
            public void onFailure(Call<PokemonInfoAPI> call, Throwable throwable) {
                Toast.makeText(DetailActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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