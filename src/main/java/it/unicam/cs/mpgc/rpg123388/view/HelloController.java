package it.unicam.cs.mpgc.rpg123388.view;

import it.unicam.cs.mpgc.rpg123388.model.CombatAction;
import it.unicam.cs.mpgc.rpg123388.model.CombatManager;
import it.unicam.cs.mpgc.rpg123388.model.heros.*;
import it.unicam.cs.mpgc.rpg123388.model.villain.Monster;
import it.unicam.cs.mpgc.rpg123388.model.villain.MonsterFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML private TextArea gameLog;
    @FXML private VBox partyBox;
    @FXML private VBox enemyBox;
    @FXML private ComboBox<String> targetSelector;

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
        updateUIStats();
    }

    @FXML
    public void onAttackButtonClick() {
        if (currentEncounter.isEmpty()) {
            prossimaStanza();
            return;
        }

        String selectedName = targetSelector.getValue();
        if (selectedName == null && !currentEncounter.isEmpty()) {
            gameLog.appendText("\n⚠️ Seleziona un bersaglio prima di attaccare!\n");
            return;
        }

        Monster targetSelected = currentEncounter.stream()
                .filter(m -> m.getName().equals(selectedName))
                .findFirst()
                .orElse(currentEncounter.get(0));

        List<CombatAction> actions = new ArrayList<>();

        for (Hero hero : party) {
            if (hero.isAlive() && !currentEncounter.isEmpty()) {
                if (hero instanceof Mage) {
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

    private void prossimaStanza() {
        gameLog.appendText("\nVITTORIA! Prossima stanza...\n");
        currentEncounter = monsterFactory.createEncounter(2);
        updateUIStats();
    }

    private void updateUIStats() {
        partyBox.getChildren().clear();
        enemyBox.getChildren().clear();
        targetSelector.getItems().clear();

        for (Hero hero : party) {
            if (hero.isAlive()) {
                addStatBar(partyBox, hero, "green");
            }
        }

        for (Monster monster : currentEncounter) {
            if (monster.isAlive()) {
                addStatBar(enemyBox, monster, "red");
                targetSelector.getItems().add(monster.getName());
            }
        }

        if (!targetSelector.getItems().isEmpty()) {
            targetSelector.getSelectionModel().selectFirst();
        }
    }

    private void addStatBar(VBox box, it.unicam.cs.mpgc.rpg123388.model.GameCharacter character, String color) {
        Label label = new Label(character.getName() + " (" + character.getHealth() + "/" + character.getMaxHealth() + ")");
        ProgressBar bar = new ProgressBar((double) character.getHealth() / character.getMaxHealth());
        bar.setStyle("-fx-accent: " + color + ";");
        box.getChildren().addAll(label, bar);
    }
}