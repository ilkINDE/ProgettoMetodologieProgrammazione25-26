package it.unicam.cs.mpgc.rpg123388.model;

public abstract class BaseCharacter implements  GameCharacter {

    private final String name;
    private int health;
    private final int maxHealth;
    private int attackPower;

    public BaseCharacter(String name, int maxHealth, int attackPower) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackPower = attackPower;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getHealth() { return health; }

    @Override
    public int getMaxHealth() { return maxHealth; }

    @Override
    public int getAttackPower() { return attackPower; }

    @Override
    public void takeDamage(int damage) {
        if (damage > 0) {
            this.health -= damage;
            if (this.health < 0) this.health = 0;
        }
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }
}
