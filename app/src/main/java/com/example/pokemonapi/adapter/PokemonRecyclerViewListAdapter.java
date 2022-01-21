package com.example.pokemonapi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemonapi.R;
import com.example.pokemonapi.databinding.PokemonItemBinding;
import com.example.pokemonapi.model.pokemonlist.ResultsResponse;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PokemonRecyclerViewListAdapter extends ListAdapter<ResultsResponse, PokemonRecyclerViewListAdapter.RecyclerViewViewHolder> {

    private final Context context;
    private final OnItemClickListener onItemClickListener;
    private final List<ResultsResponse> pokemonList = new ArrayList<>();

    public PokemonRecyclerViewListAdapter(Context context, OnItemClickListener onItemClickListener, @NonNull DiffUtil.ItemCallback<ResultsResponse> diffItemCallback) {
        super(diffItemCallback);
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PokemonItemBinding pokemonItemBinding = PokemonItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new RecyclerViewViewHolder(pokemonItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonRecyclerViewListAdapter.RecyclerViewViewHolder holder, int position) {
        ResultsResponse item = pokemonList.get(position);

        String namePoke = item.getName();
        String imagePoke = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + item.getUrl() + ".png";

        holder.pokemonItemBinding.namePoke.setText(namePoke);
        Glide.with(context).load(imagePoke).placeholder(R.drawable.placeholder).error(R.drawable.error).into(holder.pokemonItemBinding.imagePoke);

        Glide.with(context).load(imagePoke)
                .listener(
                        GlidePalette.with(imagePoke).use(BitmapPalette.Profile.MUTED_LIGHT)
                                .intoCallBack(palette -> {
                                    if (palette != null && palette.getDominantSwatch() != null) {
                                        int rgbHexCode = palette.getDominantSwatch().getRgb();
                                        holder.pokemonItemBinding.cardView.setCardBackgroundColor(rgbHexCode);
                                    }
                                }).crossfade(true))
                .into(holder.pokemonItemBinding.imagePoke);

        holder.itemView.setOnClickListener(view -> onItemClickListener.onClick(namePoke, imagePoke));
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public void refreshPokemonList(List<ResultsResponse> data) {
        pokemonList.addAll(data);
        notifyDataSetChanged();
    }

    public void clearAllOldData() {
        pokemonList.clear();
        notifyDataSetChanged();
    }

    public static class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

        private final PokemonItemBinding pokemonItemBinding;

        public RecyclerViewViewHolder(PokemonItemBinding pokemonItemBinding) {
            super(pokemonItemBinding.getRoot());
            this.pokemonItemBinding = pokemonItemBinding;
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

    public interface OnItemClickListener {
        void onClick(String namePoke, String imagePoke);
    }
}
