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

    @Override public String getAttackName() { return "Pugnalata alle Spalle"; }
    @Override public String getBuffName() { return "Concentrazione "; }
    @Override public String getAttackDescription() { return "Danno letale su singolo bersaglio."; }
    @Override public String getBuffDescription() { return "Concentrazione Letale: +15 Attacco Personale."; }
}