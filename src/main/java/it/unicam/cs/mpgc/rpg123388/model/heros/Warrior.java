package it.unicam.cs.mpgc.rpg123388.model.heros;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;

public class Warrior extends Hero {

    public Warrior(String name) {
        super(name, 100, 15);
    }

    @Override
    protected void levelUpStats() {
        // Il guerriero quando sale di livello guadagna molta vita e un po' di attacco
        increaseMaxHealth(20);
        increaseAttackPower(5);
        System.out.println(getName() + " è salito di livello! Ora è al livello " + getLevel());
    }
}