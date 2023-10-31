package com.example.androidapp1.models;

import static com.example.androidapp1.activities.HomeActivity.usersData;
import static com.example.androidapp1.service.ConeFirebaseManager.cones_data;

import com.google.firebase.database.DatabaseReference;


public class ConeUserdata {
    private int id;
    private String name;
    private int exp;
    private int lvl;
    private boolean obtained = false;
    private int character_id = -1;


    public ConeUserdata(int id, String name, int exp, int lvl) {
        this.id = id;
        this.name = name;
        this.exp = exp;
        this.lvl = lvl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public boolean isObtained() {
        return obtained;
    }

    public void setObtained(boolean isObtained, DatabaseReference usersData) {
        this.obtained = isObtained;
        usersData.child("cones").child(String.valueOf(id)).child("obtained").setValue(isObtained);
    }

    public void changeExp(int exp_gained, int[] cones_exp_table, DatabaseReference usersData) {
        exp += exp_gained;
        while (lvl < 80 && exp > cones_exp_table[lvl + 1]) {
            lvl++;
        }
        usersData.child("cones").child(String.valueOf(id)).child("exp").setValue(exp);
        usersData.child("cones").child(String.valueOf(id)).child("lvl").setValue(lvl);
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }


    public ConeUserdata() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int HpValue() {
        return coneInfo().getBase_hp() + coneInfo().getHp_growth() * (lvl - 1);
    }

    public int AtkValue() {
        return coneInfo().getBase_atk() + coneInfo().getAtk_growth() * (lvl - 1);
    }

    public int DefValue() {
        return coneInfo().getBase_def() + coneInfo().getDef_growth() * (lvl - 1);
    }

    public Cone coneInfo() {
        return cones_data.get(id);
    }

    public int getCharacter_id() {
        return character_id;
    }

    public void setCharacter_id(int character_id) {
        this.character_id = character_id;
    }

    public void changeCharacter_id(int character_id) {
        if (id != 0) {
            this.character_id = character_id;
            if (usersData != null)
                usersData.child("cones").child(String.valueOf(id)).child("character_id").setValue(character_id);
        }
    }

    @Override
    public String toString() {
        return "ConeUserdata{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", exp=" + exp +
                ", lvl=" + lvl +
                ", obtained=" + obtained +
                ", character_id=" + character_id +
                '}';
    }

}
