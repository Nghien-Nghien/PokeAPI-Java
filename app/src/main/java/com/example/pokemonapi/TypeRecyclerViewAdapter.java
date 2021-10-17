package com.example.pokemonapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonapi.network.pokemoninfo.PokemonTypeColor;

import java.util.ArrayList;
import java.util.List;

public class TypeRecyclerViewAdapter extends RecyclerView.Adapter<TypeRecyclerViewAdapter.RecyclerViewViewHolder> {
    public PokemonTypeColor pokemonTypeColor = new PokemonTypeColor();
    private final Context mContext;
    private final List<TypeItem> mTypeList = new ArrayList<>();

    public TypeRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.type_item, parent, false);
        return new TypeRecyclerViewAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        TypeItem item = mTypeList.get(position);

        String typePoke = item.getmTypePoke();

        holder.mTypePoke.setText(typePoke);
        holder.mCardView.setCardBackgroundColor(mContext.getColor(pokemonTypeColor.getTypeColor(typePoke)));
    }

    public void refreshTypeList(List<TypeItem> mData) {
        this.mTypeList.clear(); //Avoid duplicating data displayed on RecyclerView everytime do request
        this.mTypeList.addAll(mData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTypeList.size();
    }

    public static class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTypePoke;

        public RecyclerViewViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.cardView);
            mTypePoke = itemView.findViewById(R.id.typePoke);
        }
    }
}
