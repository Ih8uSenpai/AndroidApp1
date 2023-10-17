package com.example.androidapp1.models;

import java.util.ArrayList;



public class Cone{
    private int ImageResource;
    private String name;
    private String description;
    private String ability;
    private int rarity;
    private int base_hp;
    private int base_atk;
    private int base_def;
    private int hp_growth;
    private int atk_growth;
    private int def_growth;

    public int getImageResource() {
        return ImageResource;
    }

    public void setImageResource(int imageResource) {
        ImageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public int getBase_hp() {
        return base_hp;
    }

    public void setBase_hp(int base_hp) {
        this.base_hp = base_hp;
    }

    public int getBase_atk() {
        return base_atk;
    }

    public void setBase_atk(int base_atk) {
        this.base_atk = base_atk;
    }

    public int getBase_def() {
        return base_def;
    }

    public void setBase_def(int base_def) {
        this.base_def = base_def;
    }

    public int getHp_growth() {
        return hp_growth;
    }

    public void setHp_growth(int hp_growth) {
        this.hp_growth = hp_growth;
    }

    public int getAtk_growth() {
        return atk_growth;
    }

    public void setAtk_growth(int atk_growth) {
        this.atk_growth = atk_growth;
    }

    public int getDef_growth() {
        return def_growth;
    }

    public void setDef_growth(int def_growth) {
        this.def_growth = def_growth;
    }
}
