package com.example.androidapp1.models;


public class Level {
    private int exp, lvl;

    public int getLvl() {
        return lvl;
    }


    static final int[] exp_table  = new int[] {100, 200, 320, 450, 600, 850, 1200, 1600, 2200, 3000,
            3751, 4212, 4912, 5921, 7123, 8234, 9653, 10923, 12402, 14329,
            16481, 18982, 21424, 24021, 27965, 32420, 36831, 41242, 47634, 55131,
            62521, 70123, 79123, 89123, 103211, 112381, 124181, 137123, 151271, 165247,
            181427, 196148, 216471, 238131, 260812, 284881, 310161, 342518, 381271, 459211,
            561412, 725219, 920001, 1201231, 1601231, 2301231, 3101231, 4101231, 5501231, 10501231, 21101931};

    public Level(){}

    public Level(int exp, int lvl) {
        this.exp = exp;
        this.lvl = lvl;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void gainXp(int exp_gained)
    {
        exp += exp_gained;
        while (lvl < 50 && exp > exp_table[lvl + 1]){
            lvl++;
        }
    }

}
