package it.unicam.cs.mpgc.rpg123388.model.villain;

import it.unicam.cs.mpgc.rpg123388.model.BaseCharacter;

public abstract class Monster extends BaseCharacter {
    private int xpReward;

    public Monster(String name, int maxHealth, int attackPower, int xpReward) {
        super(name, maxHealth, attackPower);
        this.xpReward = xpReward;
    }

    public int getXpReward() {
        return xpReward;
    }

    public void scaleDifficulty(int roomLevel) {
        int healthBoost = roomLevel * 3;

        int attackBoost = roomLevel / 2;

        int xpBoost = roomLevel * 2;

        this.increaseMaxHealth(healthBoost);
        this.increaseAttackPower(attackBoost);
        this.xpReward += xpBoost;
    }

}