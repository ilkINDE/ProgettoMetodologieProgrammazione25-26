package it.unicam.cs.mpgc.rpg123388.model.heros;

import it.unicam.cs.mpgc.rpg123388.model.ActionType;

public class Mage extends Hero {

    public Mage(String name) {
        super(name, 60, 25);
    }

    @Override public boolean requiresExternalBuffTarget() { return true; }

    @Override
    protected void levelUpStats() {
        increaseMaxHealth(10); // Prende poca vita
        increaseAttackPower(10); // Aumenta tantissimo l'attacco
    }

    @Override public String getAttackName() { return "Tempesta Arcana (AoE)"; }
    @Override public String getBuffName() { return "Saggezza di Avalon (Buff Attacco)"; }
    @Override public String getAttackDescription() { return "Attacco AoE magico."; }
    @Override public String getBuffDescription() { return "Attacco alleato +15."; }

    @Override public ActionType getAttackActionType() { return ActionType.AOE_ATTACK; }
    @Override public ActionType getBuffActionType() { return ActionType.BUFF_ATTACK_SINGLE; }
    @Override public int getBuffEffectValue() { return 15; }
}