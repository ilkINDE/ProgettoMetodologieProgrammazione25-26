package it.unicam.cs.mpgc.rpg123388.model.heros;

import it.unicam.cs.mpgc.rpg123388.model.ActionType;
import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;

public class Warrior extends Hero {

    public Warrior(String name) {
        super(name, 100, 15);
    }

    @Override public boolean requiresExternalBuffTarget() { return false; }

    @Override
    protected void levelUpStats() {
        // Il guerriero quando sale di livello guadagna molta vita e un po' di attacco
        increaseMaxHealth(20);
        increaseAttackPower(5);
    }

    @Override public String getAttackName() { return "Fendente Pesante"; }
    @Override public String getBuffName() { return "Mura di Ferro"; }
    @Override public String getAttackDescription() { return "Attacco fisico potente."; }
    @Override public String getBuffDescription() { return "Danni subiti -10 (Party)."; }

    @Override public ActionType getAttackActionType() { return ActionType.SINGLE_ATTACK; }
    @Override public ActionType getBuffActionType() { return ActionType.BUFF_DEFENSE_PARTY; }
    @Override public int getBuffEffectValue() { return 10; }
}