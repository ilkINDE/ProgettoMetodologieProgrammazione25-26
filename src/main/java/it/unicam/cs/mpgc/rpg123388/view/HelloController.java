package it.unicam.cs.mpgc.rpg123388.view;

import it.unicam.cs.mpgc.rpg123388.model.*;
import it.unicam.cs.mpgc.rpg123388.model.heros.*;
import it.unicam.cs.mpgc.rpg123388.model.villain.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private TextArea battleLogArea;

    private CombatManager combatManager;
    private List<Hero> party;
    private List<Monster> enemies;

    @FXML
    public void initialize() {
        combatManager = new CombatManager();
        party = new ArrayList<>();
        party.add(new Warrior("Arthur"));
        party.add(new Mage("Merlino"));
        party.add(new Druid("Panoramix"));

        MonsterFactory factory = new MonsterFactory();
        enemies = factory.createEncounter(3);

        battleLogArea.setText("Benvenuti nel Dungeon!\nUn gruppo di " + enemies.size() + " nemici appare!\n");
    }

    @FXML
    protected void onNextTurnClick() {
        if (enemies.isEmpty()) {
            battleLogArea.appendText("\nHai già sconfitto tutti i nemici! Vittoria!\n");
            return;
        }

        List<CombatAction> playerOrders = new ArrayList<>();
        playerOrders.add(new CombatAction(party.get(1), enemies, true)); // Merlino fa AoE

        if (!enemies.isEmpty()) playerOrders.add(new CombatAction(party.get(0), List.of(enemies.get(0)), false));
        if (enemies.size() > 1) playerOrders.add(new CombatAction(party.get(2), List.of(enemies.get(1)), false));

        String log = combatManager.executeTacticalTurn(playerOrders, party, enemies);

        battleLogArea.appendText("\n--- NUOVO TURNO ---\n");
        battleLogArea.appendText(log);

        if (enemies.isEmpty()) {
            battleLogArea.appendText("\nVITTORIA! Stanza ripulita.\n");
        }
    }
}