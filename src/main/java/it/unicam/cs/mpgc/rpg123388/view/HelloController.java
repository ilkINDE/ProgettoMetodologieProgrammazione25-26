package it.unicam.cs.mpgc.rpg123388.view;

import it.unicam.cs.mpgc.rpg123388.model.CombatAction;
import it.unicam.cs.mpgc.rpg123388.model.CombatManager;
import it.unicam.cs.mpgc.rpg123388.model.heros.*;
import it.unicam.cs.mpgc.rpg123388.model.villain.Monster;
import it.unicam.cs.mpgc.rpg123388.model.villain.MonsterFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloController {

    @FXML private TextArea gameLog;
    @FXML private VBox partyBox;
    @FXML private VBox enemyBox;
    @FXML private VBox commandCenterBox;

    private List<Hero> party;
    private List<Monster> currentEncounter;
    private CombatManager combatManager;
    private MonsterFactory monsterFactory;

    private Map<Hero, ComboBox<String>> heroActionSelectors;
    private Map<Hero, ComboBox<String>> heroTargetSelectors;

    @FXML
    public void initialize() {
        combatManager = new CombatManager();
        monsterFactory = new MonsterFactory();
        heroActionSelectors = new HashMap<>();
        heroTargetSelectors = new HashMap<>();

        party = new ArrayList<>();
        party.add(new Warrior("Arthur"));
        party.add(new Mage("Merlino"));
        party.add(new Druid("Panoramix"));

        currentEncounter = monsterFactory.createEncounter(1);

        gameLog.setText("Benvenuti nel Dungeon!\n");
        updateUIStats();
    }

    @FXML
    public void onAttackButtonClick() {
        if (currentEncounter.isEmpty()) {
            gameLog.appendText("\nVITTORIA! Prossima stanza in arrivo...\n");
            currentEncounter = monsterFactory.createEncounter(2);
            updateUIStats();
            return;
        }

        List<CombatAction> actions = new ArrayList<>();

        for (Hero hero : party) {
            if (hero.isAlive() && !currentEncounter.isEmpty()) {
                ComboBox<String> actionCb = heroActionSelectors.get(hero);
                ComboBox<String> targetCb = heroTargetSelectors.get(hero);

                String selectedAction = actionCb.getValue();
                String selectedTargetName = targetCb.getValue();

                boolean isAoE = "Attacco ad Area".equals(selectedAction);

                Monster targetSelected = currentEncounter.stream()
                        .filter(m -> m.getName().equals(selectedTargetName))
                        .findFirst()
                        .orElse(currentEncounter.get(0));

                if (isAoE) {
                    actions.add(new CombatAction(hero, new ArrayList<>(currentEncounter), true));
                } else {
                    actions.add(new CombatAction(hero, List.of(targetSelected), false));
                }
            }
        }

        String turnResult = combatManager.executeTacticalTurn(actions, party, currentEncounter);
        gameLog.appendText("\n--- TURNO TATTICO ---\n" + turnResult);

        updateUIStats();
    }

    private void updateUIStats() {
        partyBox.getChildren().clear();
        enemyBox.getChildren().clear();
        commandCenterBox.getChildren().clear();
        heroActionSelectors.clear();
        heroTargetSelectors.clear();

        List<String> aliveMonsterNames = new ArrayList<>();
        for (Monster monster : currentEncounter) {
            if (monster.isAlive()) {
                addStatBar(enemyBox, monster, "red");
                aliveMonsterNames.add(monster.getName());
            }
        }

        for (Hero hero : party) {
            if (hero.isAlive()) {
                addStatBar(partyBox, hero, "green");

                HBox row = new HBox(10);
                row.setAlignment(javafx.geometry.Pos.CENTER);

                Label nameLabel = new Label("Ordini per " + hero.getName() + ": ");
                nameLabel.setStyle("-fx-font-weight: bold;");

                ComboBox<String> actionCb = new ComboBox<>();
                actionCb.getItems().addAll("Attacco Singolo", "Attacco ad Area");
                actionCb.getSelectionModel().selectFirst();

                ComboBox<String> targetCb = new ComboBox<>();
                targetCb.getItems().addAll(aliveMonsterNames);
                if (!aliveMonsterNames.isEmpty()) {
                    targetCb.getSelectionModel().selectFirst();
                }

                actionCb.setOnAction(e -> {
                    targetCb.setDisable("Attacco ad Area".equals(actionCb.getValue()));
                });

                row.getChildren().addAll(nameLabel, actionCb, targetCb);
                commandCenterBox.getChildren().add(row);

                heroActionSelectors.put(hero, actionCb);
                heroTargetSelectors.put(hero, targetCb);
            }
        }
    }

    private void addStatBar(VBox box, it.unicam.cs.mpgc.rpg123388.model.GameCharacter character, String color) {
        Label label = new Label(character.getName() + " (" + character.getHealth() + "/" + character.getMaxHealth() + ")");
        ProgressBar bar = new ProgressBar((double) character.getHealth() / character.getMaxHealth());
        bar.setStyle("-fx-accent: " + color + ";");
        box.getChildren().addAll(label, bar);
    }
}