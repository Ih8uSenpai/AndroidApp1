package com.example.androidapp1.models;

import static com.example.androidapp1.activities.HomeActivity.getCurrent_user_data;
import static com.example.androidapp1.activities.HomeActivity.usersData;
import static com.example.androidapp1.utils.Constants.characters_exp_table;

import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class Character {
    private String id;
    private String name;
    private int exp, character_lvl, eidolon,
            base_hp, base_atk, base_def, crit_chance, crit_dmg,
            hp_growth, atk_growth, def_growth;
    private String ability;
    private String passive;
    private String talent;
    private boolean obtained;
    private String cone_id;

    public Character(String id, String name, int exp, int character_lvl, int eidolon, int base_hp, int base_atk, int base_def, int crit_chance, int crit_dmg, int hp_growth, int atk_growth, int def_growth, String ability, String passive, String talent, boolean obtained, String cone_id) {
        this.id = id;
        this.name = name;
        this.exp = exp;
        this.character_lvl = character_lvl;
        this.eidolon = eidolon;
        this.base_hp = base_hp;
        this.base_atk = base_atk;
        this.base_def = base_def;
        this.crit_chance = crit_chance;
        this.crit_dmg = crit_dmg;
        this.hp_growth = hp_growth;
        this.atk_growth = atk_growth;
        this.def_growth = def_growth;
        this.ability = ability;
        this.passive = passive;
        this.talent = talent;
        this.obtained = obtained;
        this.cone_id = cone_id;
    }

    public Character() {
        // Default constructor required for calls to DataSnapshot.getValue(Character.class)
    }






    // database push functions
    public void changeExp(int exp_gained, DatabaseReference usersData) {
        exp += exp_gained;
        while (character_lvl < 50 && exp > characters_exp_table[character_lvl + 1]) {
            character_lvl++;
        }
        usersData.child("characters").child(id).child("exp").setValue(exp);
        usersData.child("characters").child(id).child("character_lvl").setValue(character_lvl);
    }



    // getters and setters

    public int getCharacter_lvl() {
        return character_lvl;
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



    public int getCrit_chance() {
        return crit_chance;
    }



    public int getCrit_dmg() {
        return crit_dmg;
    }


    public boolean isObtained() {
        return obtained;
    }

    public void setObtained(boolean isObtained, DatabaseReference usersData) {
        if (!this.obtained)
            this.obtained = true;
        else if (eidolon < 6) {
            eidolon++;
        }
        usersData.child("characters").child(String.valueOf(id)).child("obtained").setValue(this.obtained);
        usersData.child("characters").child(String.valueOf(id)).child("eidolon").setValue(eidolon);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public void setDef_growth(int def_growth) {
        this.def_growth = def_growth;
    }
    public ConeUserdata equippedCone(){
        if (Objects.equals(cone_id, "null"))
            return null;
        if (getCurrent_user_data() != null)
            return getCurrent_user_data().getCones().get(Integer.parseInt(cone_id));
        return null;
    }
    public int HpValue(){
        int value = 0;
        ConeUserdata cone = equippedCone();
        if (cone != null)
            value += cone.HpValue();
        value += base_hp + hp_growth * (character_lvl - 1);
        return value;
    }
    public int AtkValue(){
        int value = 0;
        ConeUserdata cone = equippedCone();
        if (cone != null)
            value += cone.AtkValue();
        value += base_atk + atk_growth * (character_lvl - 1);
        return value;
    }
    public int DefValue(){
        int value = 0;
        ConeUserdata cone = equippedCone();
        if (cone != null)
            value += cone.DefValue();
        value += base_def + def_growth * (character_lvl - 1);
        return value;
    }

    public String getCone_id() {
        return cone_id;
    }

    public void setCone_id(String cone_id) {
        this.cone_id = cone_id;
        if (id != null)
            usersData.child("characters").child(id).child("cone_id").setValue(cone_id);
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setCharacter_lvl(int character_lvl) {
        this.character_lvl = character_lvl;
    }

    public int getEidolon() {
        return eidolon;
    }

    public void setEidolon(int eidolon) {
        this.eidolon = eidolon;
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

    public void setCrit_chance(int crit_chance) {
        this.crit_chance = crit_chance;
    }

    public void setCrit_dmg(int crit_dmg) {
        this.crit_dmg = crit_dmg;
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

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getPassive() {
        return passive;
    }

    public void setPassive(String passive) {
        this.passive = passive;
    }

    public String getTalent() {
        return talent;
    }

    public void setTalent(String talent) {
        this.talent = talent;
    }
}
