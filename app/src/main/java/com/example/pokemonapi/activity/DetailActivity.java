package com.example.pokemonapi.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokemonapi.R;
import com.example.pokemonapi.databinding.ActivityDetailBinding;
import com.example.pokemonapi.fragment.DetailFragment;
import com.example.pokemonapi.fragment.MainFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(activityDetailBinding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        String namePoke = getIntent().getExtras().getString(MainFragment.EXTRA_NAME_PARAM);
        String imagePoke = getIntent().getExtras().getString(MainFragment.EXTRA_IMAGE_PARAM);

        setUpDetailFragment(namePoke, imagePoke);
    }

    private void setUpDetailFragment(String namePoke, String imagePoke) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, new DetailFragment(namePoke, imagePoke))
                .setReorderingAllowed(true)
                .commit();
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