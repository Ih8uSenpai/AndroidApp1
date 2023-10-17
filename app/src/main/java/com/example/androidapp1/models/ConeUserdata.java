package com.example.androidapp1.models;

import com.example.androidapp1.db_manage.ConeFirebaseManager;
import com.google.firebase.database.DatabaseReference;




public class ConeUserdata {
    private int id;
    private String name;
    private int exp;
    private int lvl;
    private boolean obtained = false;
    private Cone coneInfo;

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

    public void setObtained(boolean isObtained, String id, DatabaseReference usersData) {
        this.obtained = isObtained;
        usersData.child("cones").child(id).child("obtained").setValue(isObtained);
    }

    public void changeExp(int exp_gained, int[] cones_exp_table, DatabaseReference usersData)
    {
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

    public ConeUserdata(String name, int exp, int lvl, boolean obtained) {
        this.name = name;
        this.exp = exp;
        this.lvl = lvl;
        this.obtained = obtained;
    }
    public ConeUserdata(){}


    public Cone getConeInfo() {
        return coneInfo;
    }

    public void setConeInfo(Cone coneInfo) {
        this.coneInfo = coneInfo;
    }

    public ConeUserdata(String name, int exp, int lvl, boolean obtained, Cone coneInfo) {
        this.name = name;
        this.exp = exp;
        this.lvl = lvl;
        this.obtained = obtained;
        this.coneInfo = coneInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
