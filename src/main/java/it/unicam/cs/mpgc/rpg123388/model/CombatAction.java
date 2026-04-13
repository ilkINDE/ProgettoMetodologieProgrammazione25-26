package it.unicam.cs.mpgc.rpg123388.model;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;
import java.util.List;

public class CombatAction {
    private final Hero actor;
    private final List<? extends GameCharacter> targets;
    private final ActionType type;

    public CombatAction(Hero actor, List<? extends GameCharacter> targets, ActionType type) {
        this.actor = actor;
        this.targets = targets;
        this.type = type;
    }

    public Hero getActor() { return actor; }
    public List<? extends GameCharacter> getTargets() { return targets; }
    public ActionType getType() { return type; }
}