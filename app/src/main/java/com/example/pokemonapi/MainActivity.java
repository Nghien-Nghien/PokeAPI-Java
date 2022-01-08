package com.example.pokemonapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokemonapi.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MainFragment.OnItemClickListener {

    public ActivityMainBinding activityMainBinding;
    public static final String EXTRA_NAME_PARAM = "Name Pokemon";
    public static final String EXTRA_IMAGE_PARAM = "Image Pokemon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setUpFragment(savedInstanceState);
    }

    public void setUpFragment(Bundle savedInstanceState) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainerView, new MainFragment(this))
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

    @Override
    public void onClick(String namePoke, String imagePoke) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_NAME_PARAM, namePoke);
        intent.putExtra(EXTRA_IMAGE_PARAM, imagePoke);
        startActivity(intent);
    }
}