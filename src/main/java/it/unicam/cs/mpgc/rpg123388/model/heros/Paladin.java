package it.unicam.cs.mpgc.rpg123388.model.heros;

public class Paladin extends Hero {
    public Paladin(String name) {
        // Tanta salute (120), attacco medio-basso (10)
        super(name, 120, 10);
    }

    @Override
    protected void levelUpStats() {
        this.increaseMaxHealth(25);
        this.increaseAttackPower(2);
    }
}