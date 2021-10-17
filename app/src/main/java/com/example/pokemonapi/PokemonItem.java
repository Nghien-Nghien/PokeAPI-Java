package com.example.pokemonapi;

public class PokemonItem {
    private final String mNamePoke;
    private final String mImagePoke;

    public PokemonItem(String NamePoke, String ImagePoke) {
        this.mNamePoke = NamePoke;
        this.mImagePoke = ImagePoke;
    }

    public String getmNamePoke() {
        return mNamePoke;
    }

    public String getmImagePoke() {
        return mImagePoke;
    }
}
