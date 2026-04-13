package it.unicam.cs.mpgc.rpg123388.model.heros;

public class Thief extends Hero {
    public Thief(String name) {
        // Poca salute (60), attacco alto (20)
        super(name, 60, 20);
    }

    @Override
    protected void levelUpStats() {
        this.increaseMaxHealth(10);
        this.increaseAttackPower(6);
    }
}