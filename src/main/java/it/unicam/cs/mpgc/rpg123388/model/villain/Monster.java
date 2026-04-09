package it.unicam.cs.mpgc.rpg123388.model.villain;

import it.unicam.cs.mpgc.rpg123388.model.BaseCharacter;

public abstract class Monster extends BaseCharacter {
    private final int xpReward;

    public Monster(String name, int maxHealth, int attackPower, int xpReward) {
        super(name, maxHealth, attackPower);
        this.xpReward = xpReward;
    }

    public int getXpReward() {
        return xpReward;
    }

}