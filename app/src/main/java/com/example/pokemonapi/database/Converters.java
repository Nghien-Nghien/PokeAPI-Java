package com.example.pokemonapi.database;

import androidx.room.TypeConverter;

import com.example.pokemonapi.network.pokemoninfo.TypesResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Converters {
    @TypeConverter
    public static String listToJson(List<TypesResponse> types) {
        return new Gson().toJson(types);
    }

    @TypeConverter
    public static List<TypesResponse> jsonToList(String types) {
        return new Gson().fromJson(types, new TypeToken<ArrayList<TypesResponse>>() {}.getType());
    }
}
