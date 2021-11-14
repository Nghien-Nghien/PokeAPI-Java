package com.example.pokemonapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapi.network.pokemoninfo.PokemonTypeColor;
import com.example.pokemonapi.network.pokemoninfo.TypesResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TypeRecyclerViewListAdapter extends ListAdapter<TypesResponse, TypeRecyclerViewListAdapter.RecyclerViewViewHolder> {
    public PokemonTypeColor pokemonTypeColor = new PokemonTypeColor();
    private final Context context;
    private final List<TypesResponse> typeList = new ArrayList<>();

    public TypeRecyclerViewListAdapter(Context context, @NonNull DiffUtil.ItemCallback<TypesResponse> diffItemCallback) {
        super(diffItemCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.type_item, parent, false);
        return new TypeRecyclerViewListAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        TypesResponse item = typeList.get(position);

        String typePoke = item.getNameType();

        holder.typePoke.setText(typePoke);
        holder.cardView.setCardBackgroundColor(context.getColor(pokemonTypeColor.getTypeColor(typePoke)));
    }

    public void refreshTypeList(List<TypesResponse> data) {
        this.typeList.clear(); //Avoid duplicating data displayed on RecyclerView everytime do request
        this.typeList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    public static class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView typePoke;

        public RecyclerViewViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            typePoke = itemView.findViewById(R.id.typePoke);
        }
    }

    public static class TypeDiff extends DiffUtil.ItemCallback<TypesResponse> {

        @Override
        public boolean areItemsTheSame(@NonNull @NotNull TypesResponse oldItem, @NonNull @NotNull TypesResponse newItem) {
            return oldItem.getNameType().equals(newItem.getNameType());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull @NotNull TypesResponse oldItem, @NonNull @NotNull TypesResponse newItem) {
            return oldItem == newItem;
        }
    }
}
