package it.unicam.cs.mpgc.rpg123388.model.heros;

public class Druid extends Hero {

    public Druid(String name) {
        super(name, 80, 18);
    }

    @Override
    protected void levelUpStats() {
        increaseMaxHealth(15);
        increaseAttackPower(7);
        System.out.println(getName() + " il Druido è salito al livello " + getLevel() + "!");
    }
}
