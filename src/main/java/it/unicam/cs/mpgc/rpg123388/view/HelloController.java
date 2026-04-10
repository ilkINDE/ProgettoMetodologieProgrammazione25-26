package it.unicam.cs.mpgc.rpg123388.view;

import it.unicam.cs.mpgc.rpg123388.model.CombatAction;
import it.unicam.cs.mpgc.rpg123388.model.CombatManager;
import it.unicam.cs.mpgc.rpg123388.model.heros.Druid;
import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;
import it.unicam.cs.mpgc.rpg123388.model.heros.Mage;
import it.unicam.cs.mpgc.rpg123388.model.villain.Monster;
import it.unicam.cs.mpgc.rpg123388.model.villain.MonsterFactory;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private TextArea gameLog;

    private List<Hero> party;
    private List<Monster> currentEncounter;
    private CombatManager combatManager;
    private MonsterFactory monsterFactory;

    @FXML
    public void initialize() {
        combatManager = new CombatManager();
        monsterFactory = new MonsterFactory();

        party = new ArrayList<>();
        party.add(new Mage("Merlino"));
        party.add(new Druid("Panoramix"));

        currentEncounter = monsterFactory.createEncounter(1);

        gameLog.setText("Benvenuti nel Dungeon!\n");
        gameLog.appendText("Un gruppo di " + currentEncounter.size() + " nemici appare!\n");
    }

    @FXML
    public void onAttackButtonClick() {
        if (currentEncounter.isEmpty()) {
            gameLog.appendText("\nVITTORIA! La stanza è già vuota. Prossimo incontro in arrivo...\n");
            currentEncounter = monsterFactory.createEncounter(2);
            gameLog.appendText("Appaiono " + currentEncounter.size() + " nuovi nemici!\n");
            return;
        }

        List<CombatAction> actions = new ArrayList<>();

        for (Hero hero : party) {
            if (hero.isAlive() && !currentEncounter.isEmpty()) {
                // Esempio di scelta tattica:
                // Se è un Mago (Merlino), fa attacco AoE (ad area)
                // Se è un Druido (Panoramix), attacca il primo bersaglio
                boolean isMage = hero instanceof Mage;

                if (isMage) {
                    actions.add(new CombatAction(hero, new ArrayList<>(currentEncounter), true));
                } else {
                    actions.add(new CombatAction(hero, List.of(currentEncounter.get(0)), false));
                }
            }
        }

        // Eseguiamo il turno tramite il CombatManager e otteniamo il log
        String turnResult = combatManager.executeTacticalTurn(actions, party, currentEncounter);

        // Stampiamo i risultati reali nella View
        gameLog.appendText("\n--- NUOVO TURNO ---\n");
        gameLog.appendText(turnResult);

        if (currentEncounter.isEmpty()) {
            gameLog.appendText("\nVITTORIA! Stanza ripulita.\n");
        }

        // Controlliamo se il party è ancora vivo
        boolean partyAlive = party.stream().anyMatch(Hero::isAlive);
        if (!partyAlive) {
            gameLog.appendText("\nIL PARTY È STATO SCONFITTO... GAME OVER.\n");
        }
    }
}