package com.example.pokemonapi.network.pokemoninfo;

import com.example.pokemonapi.R;

public class PokemonTypeColor {
    int color;

    public Integer getTypeColor(String nameType) {
        switch (nameType) {
            case "fighting":
                color = R.color.fighting;
                break;
            case "flying":
                color = R.color.flying;
                break;
            case "poison":
                color = R.color.poison;
                break;
            case "ground":
                color = R.color.ground;
                break;
            case "rock":
                color = R.color.rock;
                break;
            case "bug":
                color = R.color.bug;
                break;
            case "ghost":
                color = R.color.ghost;
                break;
            case "steel":
                color = R.color.steel;
                break;
            case "fire":
                color = R.color.fire;
                break;
            case "water":
                color = R.color.water;
                break;
            case "grass":
                color = R.color.grass;
                break;
            case "electric":
                color = R.color.electric;
                break;
            case "psychic":
                color = R.color.psychic;
                break;
            case "ice":
                color = R.color.ice;
                break;
            case "dragon":
                color = R.color.dragon;
                break;
            case "fairy":
                color = R.color.fairy;
                break;
            case "dark":
                color = R.color.dark;
                break;
            default:
                color = R.color.white;
        }
        return color;
    }
}
