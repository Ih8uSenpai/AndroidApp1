package com.example.androidapp1.Models;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class InventoryItem {
    private int ImageResource;
    private String name;
    private String description;
    private String function;
    private ArrayList<Integer> stats;



    private boolean is_obtained = false;

    public InventoryItem(int imageResource, String name, String description) {
        ImageResource = imageResource;
        this.name = name;
        this.description = description;
    }


    public InventoryItem() {}

    public InventoryItem(int imageResource, String name, String description, String function, ArrayList<Integer> stats) {
        ImageResource = imageResource;
        this.name = name;
        this.description = description;
        this.function = function;
        this.stats = stats;
    }

    public InventoryItem(int imageResource, String name, String description, String function) {
        ImageResource = imageResource;
        this.name = name;
        this.description = description;
        this.function = function;
    }

    public InventoryItem(String name, String description) {
        this.name = name;
        this.description = description;
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

    public ArrayList<Integer> getStats() {
        return stats;
    }
    public boolean getIs_obtained() {
        return is_obtained;
    }

    public void setIs_obtained(boolean is_obtained, String id, DatabaseReference usersData) {
        this.is_obtained = is_obtained;
        usersData.child("cones").child(id).child("is_obtained").setValue(is_obtained);
    }

}
