package it.unicam.cs.mpgc.rpg123388.model;

public abstract class BaseCharacter implements GameCharacter {

    private String name;
    private int health;
    private int maxHealth;
    private int attackPower;

    private int temporaryAttackBoost = 0;
    private int temporaryDamageReduction = 0;

    public BaseCharacter(String name, int maxHealth, int attackPower) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackPower = attackPower;
    }

    @Override public String getName() { return name; }
    @Override public int getHealth() { return health; }
    @Override public int getMaxHealth() { return maxHealth; }

    @Override public int getAttackPower() { return attackPower + temporaryAttackBoost; }

    @Override
    public void takeDamage(int damage) {
        int actualDamage = damage - temporaryDamageReduction;
        if (actualDamage < 0) actualDamage = 0;

        if (actualDamage > 0) {
            this.health -= actualDamage;
            if (this.health < 0) this.health = 0;
        }
    }

    public void heal(int amount) {
        if (amount > 0 && isAlive()) {
            this.health += amount;
            if (this.health > this.maxHealth) this.health = this.maxHealth;
        }
    }

    public void addTemporaryAttackBoost(int boost) { this.temporaryAttackBoost += boost; }
    public void addTemporaryDamageReduction(int damageReduction) { this.temporaryDamageReduction += damageReduction; }

    public void resetTemporaryBuffs() {
        this.temporaryAttackBoost = 0;
        this.temporaryDamageReduction = 0;
    }

    @Override
    public boolean isAlive() { return this.health > 0; }

    protected void increaseMaxHealth(int amount) {
        if (amount > 0) {
            this.maxHealth += amount;
            this.health += amount;
        }
    }
    protected void increaseAttackPower(int amount) {
        if (amount > 0) this.attackPower += amount;
    }
}