package it.unicam.cs.mpgc.rpg123388.model.heros;

public class Mage extends Hero {

    public Mage(String name) {
        super(name, 60, 25);
    }

    @Override
    protected void levelUpStats() {
        increaseMaxHealth(10); // Prende poca vita
        increaseAttackPower(10); // Aumenta tantissimo l'attacco
    }

    @Override public String getAttackName() { return "Tempesta Arcana (AoE)"; }
    @Override public String getBuffName() { return "Saggezza di Avalon (Buff Attacco)"; }
    @Override public String getAttackDescription() { return "Attacco AoE magico."; }
    @Override public String getBuffDescription() { return "Attacco alleato +15."; }
}