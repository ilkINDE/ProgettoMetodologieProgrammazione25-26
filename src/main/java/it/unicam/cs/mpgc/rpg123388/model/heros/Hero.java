package it.unicam.cs.mpgc.rpg123388.model.heros;

import it.unicam.cs.mpgc.rpg123388.model.BaseCharacter;

public abstract class Hero extends BaseCharacter {

    private int level;
    private int experience;

    public Hero(String name, int maxHealth, int attackPower) {
        super(name, maxHealth, attackPower);
        this.level = 1;
        this.experience = 0;
    }

    public int getLevel() { return level; }

    public int getExperience() { return experience; }

    public void gainExperience(int xp) {
        if (xp > 0) {
            this.experience += xp;
            if (this.experience >= 100 * this.level) {
                this.experience -= (100 * this.level);
                this.level++;
                levelUpStats();
            }
        }
    }

    // metodo per decidere come far salire le statistiche
    protected abstract void levelUpStats();
}
