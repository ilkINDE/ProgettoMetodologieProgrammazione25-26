package it.unicam.cs.mpgc.rpg123388.model;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;
import it.unicam.cs.mpgc.rpg123388.model.villain.Monster;

import java.util.List;

public class CombatAction {
        private final Hero actor;
        private final List<Monster> targets;
        private final boolean isAoE;// verifica se è o meno un attacco ad area

        public CombatAction(Hero actor, List<Monster> targets, boolean isAoE) {
            this.actor = actor;
            this.targets = targets;
            this.isAoE = isAoE;
        }

        public Hero getActor() { return actor; }
        public List<Monster> getTargets() { return targets; }
        public boolean isAoE() { return isAoE; }
}
