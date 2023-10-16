package com.example.androidapp1.models;

import com.google.firebase.database.DatabaseReference;

public class Character {
    private String name;
    private int exp, character_lvl, hp, attack, defense, crit_chance, crit_dmg, eidolon;
    private String ability;
    private String passive;
    private String talent;
    private boolean is_obtained;

    public Character() {
        // Default constructor required for calls to DataSnapshot.getValue(Character.class)
    }

    public Character(String name, int character_lvl, int hp, int attack, int defense, int crit_chance, int crit_dmg, String ability, String passive, String talent, boolean is_obtained) {
        this.name = name;
        this.character_lvl = character_lvl;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.crit_chance = crit_chance;
        this.crit_dmg = crit_dmg;
        this.ability = ability;
        this.passive = passive;
        this.talent = talent;
        this.is_obtained = is_obtained;
    }


    // database push functions
    private void changeExp(int exp_gained, int[] characters_exp_table, DatabaseReference usersData)
    {
        exp += exp_gained;
        while (character_lvl < 50 && exp > characters_exp_table[character_lvl + 1]) {
            character_lvl++;
        }
        usersData.child("characters").child(name).child("exp").setValue(exp);
        usersData.child("characters").child(name).child("character_lvl").setValue(character_lvl);
    }
    private void obtain(DatabaseReference usersData){
        if (!is_obtained)
            is_obtained = true;
        else if (eidolon < 6) {
            eidolon++;
        }
        usersData.child("characters").child(name).child("is_obtained").setValue(is_obtained);
        usersData.child("characters").child(name).child("eidolon").setValue(eidolon);
    }
    private void changeHp(int hp, DatabaseReference usersData){
        this.hp = hp;
        usersData.child("characters").child(name).child("hp").setValue(hp);
    }
    private void changeAttack(int attack, DatabaseReference usersData){
        this.attack = attack;
        usersData.child("characters").child(name).child("attack").setValue(attack);
    }
    private void changeDefense(int defense, DatabaseReference usersData){
        this.defense = defense;
        usersData.child("characters").child(name).child("defense").setValue(defense);
    }
    private void changeCrit_chance(int crit_chance, DatabaseReference usersData){
        this.crit_chance = crit_chance;
        usersData.child("characters").child(name).child("crit_chance").setValue(crit_chance);
    }
    private void changeCrit_dmg(int crit_dmg, DatabaseReference usersData){
        this.crit_dmg = crit_dmg;
        usersData.child("characters").child(name).child("crit_dmg").setValue(crit_dmg);
    }



    // getters and setters

    public int getCharacter_lvl() {
        return character_lvl;
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


    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getCrit_chance() {
        return crit_chance;
    }

    public void setCrit_chance(int crit_chance) {
        this.crit_chance = crit_chance;
    }

    public int getCrit_dmg() {
        return crit_dmg;
    }

    public void setCrit_dmg(int crit_dmg) {
        this.crit_dmg = crit_dmg;
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

    public boolean getIs_obtained() {
        return is_obtained;
    }

    public void setIs_obtained(boolean is_obtained) {
        this.is_obtained = is_obtained;
    }
}
