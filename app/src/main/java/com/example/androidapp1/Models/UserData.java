package com.example.androidapp1.Models;

import android.util.Pair;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserData {

    // Объявление синглтона
    private static UserData instance;

    private UserData() {
        // Приватный конструктор, чтобы нельзя было создать объект извне.
    }
    // Метод для получения экземпляра синглтона
    public static synchronized UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }
    private int exp;
    private int gold;
    private int gems;
    private int lvl;
    private int pulls;
    private int event_pulls;
    private int pulls_to_4star;


    private int pulls_to_5star;
    // loot table where first value - loot rarity, second - status
    ArrayList<pair> loot_table;

    // inventory items
    List<InventoryItem> cones = new ArrayList<>();
    List<InventoryItem> artifacts = new ArrayList<>();
    List<InventoryItem> items = new ArrayList<>();


    private ArrayList<Character> characters;

    public ArrayList<pair> generateLootTable(ArrayList<pair> table){
        if (table != null)
            table.clear();
        Random random = new Random();
        // fill table with 3 star loot
        for (int i = 0; i < 120; i++) {
            table.add(new pair(3, 0));
        }
        // turn some 3 star lots into 4 star
        for (int i = 0; i < 12; i++) {
            int index = random.nextInt(120);
            while (table.get(index).first != 3)
                index = random.nextInt(120);
            table.set(index, new pair(4, 0));
        }
        int index = random.nextInt(120);
        while (table.get(index).first != 3)
            index = random.nextInt(120);
        // turn 1 3star lot into 5 star
        table.set(index, new pair(5, 0));
        return table;
    }
    public void update_loot_table(int value, DatabaseReference usersData){
        loot_table.set(value, new pair(loot_table.get(value).first, 1));
        usersData.child("loot_table").setValue(loot_table);
    }
    public ArrayList<pair> getLoot_table() {
        return loot_table;
    }

    public void setLoot_table (ArrayList<pair> loot_table) {
        this.loot_table = loot_table;
    }

    public int getPulls() {
        return pulls;
    }

    public void setPulls(int pulls) {
        this.pulls = pulls;
    }

    public int getEvent_pulls() {
        return event_pulls;
    }

    public void setEvent_pulls(int event_pulls) {
        this.event_pulls = event_pulls;
    }
    //private Equipment equipment;

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }


    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGems() {
        return gems;
    }

    public void setGems(int gems) {
        this.gems = gems;
    }

    public void gainExp(int exp_gained, int[] exp_table, DatabaseReference usersData)
    {
        exp += exp_gained;
        while (lvl < 50 && exp > exp_table[lvl + 1]) {
            lvl++;
        }
        usersData.child("exp").setValue(exp);
        usersData.child("lvl").setValue(lvl);
    }
    public void gainGold(int gold_gained, DatabaseReference usersData)
    {
        gold += gold_gained;
        usersData.child("gold").setValue(gold);
    }
    public void gainGems(int gems_gained, DatabaseReference usersData)
    {
        gems += gems_gained;
        usersData.child("gems").setValue(gems);
    }
    public void gainEvent_pulls(int event_pulls_gained, DatabaseReference usersData)
    {
        event_pulls += event_pulls_gained;
        usersData.child("event_pulls").setValue(event_pulls);
    }
    public void gainPulls(int pulls_gained, DatabaseReference usersData)
    {
        pulls += pulls_gained;
        usersData.child("pulls").setValue(pulls);
    }
    public void updatePulls_to_4star(int pulls_to_4star, DatabaseReference usersData)
    {
        this.pulls_to_4star = pulls_to_4star;
        usersData.child("pulls_to_4star").setValue(pulls_to_4star);
    }
    public void updatePulls_to_5star(int pulls_to_5star, DatabaseReference usersData)
    {
        this.pulls_to_5star = pulls_to_5star;
        usersData.child("pulls_to_5star").setValue(pulls_to_5star);
    }

    public int getPulls_to_4star() {
        return pulls_to_4star;
    }

    public void setPulls_to_4star(int pulls_to_4star) {
        this.pulls_to_4star = pulls_to_4star;
    }

    public int getPulls_to_5star() {
        return pulls_to_5star;
    }

    public void setPulls_to_5star(int pulls_to_5star) {
        this.pulls_to_5star = pulls_to_5star;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    }

    public Character getCharacterByName(String name){
        for (Character el: characters) {
            if (el.getName() == name)
                return el;
        }
        return null;
    }


    public List<InventoryItem> getCones() {
        return cones;
    }

    public void setCones(List<InventoryItem> cones) {
        this.cones = cones;
    }

    public List<InventoryItem> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<InventoryItem> artifacts) {
        this.artifacts = artifacts;
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }
}
