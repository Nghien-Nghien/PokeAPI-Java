package com.example.pokemonapi;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;

import java.util.ArrayList;
import java.util.List;

public class PokemonRecyclerViewAdapter extends RecyclerView.Adapter<PokemonRecyclerViewAdapter.RecyclerViewViewHolder> {
    private final Context mContext;
    private final List<PokemonItem> mPokemonList = new ArrayList<>();

    public PokemonRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pokemon_item, parent, false);
        return new RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonRecyclerViewAdapter.RecyclerViewViewHolder holder, int position) {
        PokemonItem item = mPokemonList.get(position);

        String namePoke = item.getmNamePoke();
        String imagePoke = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + item.getmImagePoke() + ".png";

        holder.mNamePoke.setText(namePoke);
        Glide.with(mContext).load(imagePoke).placeholder(R.drawable.placeholder).error(R.drawable.error).into(holder.mImagePoke);

        Glide.with(mContext).load(imagePoke)
                .listener(
                        GlidePalette.with(imagePoke).use(BitmapPalette.Profile.MUTED_LIGHT)
                                .intoCallBack(new BitmapPalette.CallBack() {
                                    @Override
                                    public void onPaletteLoaded(@Nullable Palette palette) {
                                        if (palette != null && palette.getDominantSwatch() != null) {
                                            int rgbHexCode = palette.getDominantSwatch().getRgb();
                                            holder.mCardView.setCardBackgroundColor(rgbHexCode);
                                        } else {
                                            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.default_backgroundCardView));
                                        }
                                    }
                                }).crossfade(true))
                .into(holder.mImagePoke);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("NamePoke", namePoke);
                intent.putExtra("ImagePoke", imagePoke);
                view.getContext().startActivity(intent);
            }
        });
    }

    public void refreshPokemonList(List<PokemonItem> mData) {
        this.mPokemonList.addAll(mData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPokemonList.size();
    }

    public static class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public ImageView mImagePoke;
        public TextView mNamePoke;

        public RecyclerViewViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.cardView);
            mImagePoke = itemView.findViewById(R.id.imagePoke);
            mNamePoke = itemView.findViewById(R.id.namePoke);
        }
    }
}
