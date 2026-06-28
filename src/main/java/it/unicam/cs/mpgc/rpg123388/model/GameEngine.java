package it.unicam.cs.mpgc.rpg123388.model;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;
import it.unicam.cs.mpgc.rpg123388.model.villain.Monster;
import it.unicam.cs.mpgc.rpg123388.model.villain.MonsterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GameEngine {

    private final CombatManager combatManager = new CombatManager();
    private final MonsterFactory monsterFactory = new MonsterFactory();
    private List<Hero> party = new ArrayList<>();
    private List<Monster> currentEncounter = new ArrayList<>();

    public void startRun(List<Hero> selectedHeroes) {
        this.party = new ArrayList<>(selectedHeroes);
    }

    public String executeRound(List<CombatAction> actions) {
        return combatManager.executeTacticalTurn(actions, party, currentEncounter);
    }

    public void advanceRoom() {
        currentEncounter = monsterFactory.generateNextRoom();
    }

    public int getCurrentRoom() {
        return monsterFactory.getRoomCounter();
    }

    public boolean isEncounterCleared() {
        return currentEncounter.isEmpty();
    }

    public boolean isPartyDefeated() {
        return party.stream().noneMatch(Hero::isAlive);
    }

    public List<Hero> getParty() { return party; }
    public List<Monster> getCurrentEncounter() { return currentEncounter; }
    public Map<String, Integer> getKillCount() { return combatManager.getKillCount(); }
}