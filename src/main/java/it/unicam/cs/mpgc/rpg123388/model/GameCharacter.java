package it.unicam.cs.mpgc.rpg123388.model;

public interface GameCharacter {
    String getName();
    int getHealth();
    int getMaxHealth();
    int getAttackPower();

    void takeDamage(int damage);
    boolean isAlive();
}
