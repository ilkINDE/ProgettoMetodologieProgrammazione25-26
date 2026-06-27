package it.unicam.cs.mpgc.rpg123388.model.heros;

import it.unicam.cs.mpgc.rpg123388.model.ActionType;

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

    @Override public String getAttackName() { return "Martello della Giustizia"; }
    @Override public String getBuffName() { return "Luce Divina "; }
    @Override public String getAttackDescription() { return "Danno fisico contundente."; }
    @Override public String getBuffDescription() { return "Cura il party (+20 HP)."; }

    @Override public ActionType getAttackActionType() { return ActionType.SINGLE_ATTACK; }
    @Override public ActionType getBuffActionType() { return ActionType.HEAL_PARTY; }
    @Override public int getBuffEffectValue() { return 20; }
}