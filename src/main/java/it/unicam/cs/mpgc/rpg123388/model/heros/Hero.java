package it.unicam.cs.mpgc.rpg123388.model.heros;

import it.unicam.cs.mpgc.rpg123388.model.GameCharacter;

public abstract class Hero implements GameCharacter {
    private final String name;
    private int health;
    private int maxHealth;
    private int attackPower;

    public Hero(String name, int health, int maxHealth, int attackPower) {
        this.name = name;
        this.health = health;
        this.maxHealth = maxHealth;
        this.attackPower = attackPower;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public int getAttackPower() {
        return attackPower;
    }

    @Override
    public void takeDamage(int damage) {
        if (damage > 0) {
            this.health -= damage;
            if (this.health < 0) {
                this.health = 0;
            }
        }
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }
}
