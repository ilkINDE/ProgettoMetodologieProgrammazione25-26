package it.unicam.cs.mpgc.rpg123388.model.heros;

import it.unicam.cs.mpgc.rpg123388.model.ActionType;

public class Druid extends Hero {

    public Druid(String name) {
        super(name, 80, 18);
    }

    @Override public boolean requiresExternalBuffTarget() { return false; }

    @Override
    protected void levelUpStats() {
        increaseMaxHealth(15);
        increaseAttackPower(7);
    }

    @Override public String getAttackName() { return "Radici Stritolanti"; }
    @Override public String getBuffName() { return "Elisir di Vita (Cura Party)"; }
    @Override public String getAttackDescription() { return "Danno a bersaglio singolo."; }
    @Override public String getBuffDescription() { return "Cura il party (+20 HP)."; }

    @Override public ActionType getAttackActionType() { return ActionType.SINGLE_ATTACK; }
    @Override public ActionType getBuffActionType() { return ActionType.HEAL_PARTY; }
    @Override public int getBuffEffectValue() { return 20; }
}
