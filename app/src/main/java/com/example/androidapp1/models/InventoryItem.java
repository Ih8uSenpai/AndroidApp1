package com.example.androidapp1.models;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class InventoryItem {
    private int ImageResource;
    private String name;
    private String description;
    private String function;
    private int rarity;



    private boolean is_obtained = false;
    private int amount;

    public InventoryItem(int imageResource, String name, String description) {
        ImageResource = imageResource;
        this.name = name;
        this.description = description;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }





    public int getImageResource() {
        return ImageResource;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFunction() {
        return function;
    }

    public boolean getIs_obtained() {
        return is_obtained;
    }

    public void setIs_obtained(boolean is_obtained, String id, DatabaseReference usersData) {
        this.is_obtained = is_obtained;
        usersData.child("cones").child(id).child("is_obtained").setValue(is_obtained);
    }

    public void setImageResource(int imageResource) {
        ImageResource = imageResource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setIs_obtained(boolean is_obtained) {
        this.is_obtained = is_obtained;
    }

    public boolean isIs_obtained() {
        return is_obtained;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
