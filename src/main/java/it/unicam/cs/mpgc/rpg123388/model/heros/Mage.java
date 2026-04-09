package it.unicam.cs.mpgc.rpg123388.model.heros;

public class Mage extends Hero {

    public Mage(String name) {
        super(name, 60, 25);
    }

    @Override
    protected void levelUpStats() {
        increaseMaxHealth(10); // Prende poca vita
        increaseAttackPower(10); // Aumenta tantissimo l'attacco
        System.out.println(getName() + " il Mago è salito al livello " + getLevel() + "!");
    }
}