package it.unicam.cs.mpgc.rpg123388.view;

import it.unicam.cs.mpgc.rpg123388.model.CombatAction;
import it.unicam.cs.mpgc.rpg123388.model.CombatManager;
import it.unicam.cs.mpgc.rpg123388.model.heros.Druid;
import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;
import it.unicam.cs.mpgc.rpg123388.model.heros.Mage;
import it.unicam.cs.mpgc.rpg123388.model.villain.Monster;
import it.unicam.cs.mpgc.rpg123388.model.villain.MonsterFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML private TextArea gameLog;
    @FXML private VBox partyBox;
    @FXML private VBox enemyBox;

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

        updateUIStats();
    }

    @FXML
    public void onAttackButtonClick() {
        if (currentEncounter.isEmpty()) {
            gameLog.appendText("\nVITTORIA! La stanza è già vuota. Prossimo incontro in arrivo...\n");
            currentEncounter = monsterFactory.createEncounter(2);
            gameLog.appendText("Appaiono " + currentEncounter.size() + " nuovi nemici!\n");
            updateUIStats();
            return;
        }

        List<CombatAction> actions = new ArrayList<>();

        for (Hero hero : party) {
            if (hero.isAlive() && !currentEncounter.isEmpty()) {
                boolean isMage = hero instanceof Mage;
                if (isMage) {
                    actions.add(new CombatAction(hero, new ArrayList<>(currentEncounter), true));
                } else {
                    actions.add(new CombatAction(hero, List.of(currentEncounter.get(0)), false));
                }
            }
        }

        String turnResult = combatManager.executeTacticalTurn(actions, party, currentEncounter);

        gameLog.appendText("\n--- NUOVO TURNO ---\n");
        gameLog.appendText(turnResult);

        if (currentEncounter.isEmpty()) {
            gameLog.appendText("\nVITTORIA! Stanza ripulita.\n");
        }

        boolean partyAlive = party.stream().anyMatch(Hero::isAlive);
        if (!partyAlive) {
            gameLog.appendText("\nIL PARTY È STATO SCONFITTO... GAME OVER.\n");
        }

        updateUIStats();
    }

    /**
     * Metodo helper che pulisce la grafica e ridisegna le barre della vita
     * leggendo i dati aggiornati dal model
     */
    private void updateUIStats() {
        partyBox.getChildren().clear();
        enemyBox.getChildren().clear();

        for (Hero hero : party) {
            if (hero.isAlive()) {
                Label nameLabel = new Label(hero.getName() + " (" + hero.getHealth() + "/" + hero.getMaxHealth() + ")");
                nameLabel.setStyle("-fx-font-weight: bold;");

                double hpPercentage = (double) hero.getHealth() / hero.getMaxHealth();
                ProgressBar hpBar = new ProgressBar(hpPercentage);
                hpBar.setStyle("-fx-accent: green;"); // Colore barra

                partyBox.getChildren().addAll(nameLabel, hpBar);
            }
        }

        for (Monster monster : currentEncounter) {
            if (monster.isAlive()) {
                Label nameLabel = new Label(monster.getName() + " (" + monster.getHealth() + "/" + monster.getMaxHealth() + ")");

                double hpPercentage = (double) monster.getHealth() / monster.getMaxHealth();
                ProgressBar hpBar = new ProgressBar(hpPercentage);
                hpBar.setStyle("-fx-accent: red;");

                enemyBox.getChildren().addAll(nameLabel, hpBar);
            }
        }
    }
}