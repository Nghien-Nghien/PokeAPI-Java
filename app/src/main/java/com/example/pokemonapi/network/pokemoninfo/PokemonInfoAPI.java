package com.example.pokemonapi.network.pokemoninfo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.SplittableRandom;

public class PokemonInfoAPI {
    final int maxHP = 300;
    final int maxATK = 300;
    final int maxDEF = 300;
    final int maxSPD = 300;
    final int maxEXP = 1000;

    @SerializedName("types")
    public ArrayList<TypesResponse> types;

    @SerializedName("height")
    public Integer height;

    @SerializedName("weight")
    public Integer weight;

    public ArrayList<TypesResponse> getTypes() {
        return types;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer hp = new SplittableRandom().nextInt(100, maxHP);
    public Integer atk = new SplittableRandom().nextInt(100, maxATK);
    public Integer def = new SplittableRandom().nextInt(100, maxDEF);
    public Integer spd = new SplittableRandom().nextInt(100, maxSPD);
    public Integer exp = new SplittableRandom().nextInt(300, maxEXP);

    public String getHPString() {
        return hp + "/" + maxHP;
    }

    public String getATKString() {
        return atk + "/" + maxATK;
    }

    public String getDEFString() {
        return def + "/" + maxDEF;
    }

    public String getSPDString() {
        return spd + "/" + maxSPD;
    }

    public String getEXPString() {
        return exp + "/" + maxEXP;
    }
}
