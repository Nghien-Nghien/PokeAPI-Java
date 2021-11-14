package com.example.pokemonapi;

import android.annotation.SuppressLint;
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
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemonapi.network.pokemonlist.ResultsResponse;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PokemonRecyclerViewListAdapter extends ListAdapter<ResultsResponse, PokemonRecyclerViewListAdapter.RecyclerViewViewHolder> {
    private final Context context;
    private final List<ResultsResponse> pokemonList = new ArrayList<>();

    public PokemonRecyclerViewListAdapter(Context context, @NonNull DiffUtil.ItemCallback<ResultsResponse> diffItemCallback) {
        super(diffItemCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pokemon_item, parent, false);
        return new RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonRecyclerViewListAdapter.RecyclerViewViewHolder holder, int position) {
        ResultsResponse item = pokemonList.get(position);

        String namePoke = item.getName();
        String imagePoke = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + item.getUrl() + ".png";

        holder.namePoke.setText(namePoke);
        Glide.with(context).load(imagePoke).placeholder(R.drawable.placeholder).error(R.drawable.error).into(holder.imagePoke);

        Glide.with(context).load(imagePoke)
                .listener(
                        GlidePalette.with(imagePoke).use(BitmapPalette.Profile.MUTED_LIGHT)
                                .intoCallBack(new BitmapPalette.CallBack() {
                                    @Override
                                    public void onPaletteLoaded(@Nullable Palette palette) {
                                        if (palette != null && palette.getDominantSwatch() != null) {
                                            int rgbHexCode = palette.getDominantSwatch().getRgb();
                                            holder.cardView.setCardBackgroundColor(rgbHexCode);
                                        }
                                    }
                                }).crossfade(true))
                .into(holder.imagePoke);

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

    public void refreshPokemonList(List<ResultsResponse> data) {
        pokemonList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public static class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView imagePoke;
        public TextView namePoke;

        public RecyclerViewViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            imagePoke = itemView.findViewById(R.id.imagePoke);
            namePoke = itemView.findViewById(R.id.namePoke);
        }
    }

    public static class PokemonDiff extends DiffUtil.ItemCallback<ResultsResponse> {

        @Override
        public boolean areItemsTheSame(@NonNull @NotNull ResultsResponse oldItem, @NonNull @NotNull ResultsResponse newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull @NotNull ResultsResponse oldItem, @NonNull @NotNull ResultsResponse newItem) {
            return oldItem == newItem;
        }
    }
}
