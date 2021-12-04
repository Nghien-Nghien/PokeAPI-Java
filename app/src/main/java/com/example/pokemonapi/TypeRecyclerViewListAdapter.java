package com.example.pokemonapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapi.databinding.TypeItemBinding;
import com.example.pokemonapi.model.pokemoninfo.PokemonTypeColor;
import com.example.pokemonapi.model.pokemoninfo.TypesResponse;

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
        TypeItemBinding typeItemBinding = TypeItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new RecyclerViewViewHolder(typeItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        TypesResponse item = typeList.get(position);

        String typePoke = item.getNameType();

        holder.typeItemBinding.typePoke.setText(typePoke);
        holder.typeItemBinding.cardView.setCardBackgroundColor(context.getColor(pokemonTypeColor.getTypeColor(typePoke)));
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
        public TypeItemBinding typeItemBinding;

        public RecyclerViewViewHolder(TypeItemBinding typeItemBinding) {
            super(typeItemBinding.getRoot());
            this.typeItemBinding = typeItemBinding;
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
