package it.unicam.cs.mpgc.rpg123388.view;

import it.unicam.cs.mpgc.rpg123388.model.*;
import it.unicam.cs.mpgc.rpg123388.model.heros.*;
import it.unicam.cs.mpgc.rpg123388.model.villain.*;
import it.unicam.cs.mpgc.rpg123388.persistence.ScoreManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.*;

public class HelloController {

    @FXML private VBox selectionScreen;
    @FXML private CheckBox chkWarrior, chkMage, chkDruid, chkPaladin, chkThief;
    @FXML private Button btnStart;

    @FXML private VBox gameScreen;
    @FXML private TextArea gameLog;
    @FXML private VBox partyBox, enemyBox, commandCenterBox;
    @FXML private Label roomLabel;
    @FXML private VBox gameOverScreen;
    @FXML private Label statsLabel;

    private final GameEngine gameEngine = new GameEngine();

    private Map<Hero, ComboBox<String>> heroActionSelectors = new HashMap<>();
    private Map<Hero, ComboBox<String>> heroTargetSelectors = new HashMap<>();


    @FXML
    public void onHeroSelectionChange() {
        int count = 0;
        if (chkWarrior.isSelected()) count++;
        if (chkMage.isSelected()) count++;
        if (chkDruid.isSelected()) count++;
        if (chkPaladin.isSelected()) count++;
        if (chkThief.isSelected()) count++;

        btnStart.setDisable(count != 3);
    }

    @FXML
    public void onStartGameClick() {
        List<Hero> selected = new ArrayList<>();
        if (chkWarrior.isSelected()) selected.add(new Warrior("Arthur"));
        if (chkMage.isSelected())    selected.add(new Mage("Merlino"));
        if (chkDruid.isSelected())   selected.add(new Druid("Panoramix"));
        if (chkPaladin.isSelected()) selected.add(new Paladin("Uther"));
        if (chkThief.isSelected())   selected.add(new Thief("Garrett"));

        gameEngine.startRun(selected);

        selectionScreen.setVisible(false);
        selectionScreen.setManaged(false);
        gameScreen.setVisible(true);
        gameScreen.setManaged(true);

        prossimaStanza();
    }

    private void prossimaStanza() {
        gameEngine.advanceRoom();
        int stanzaCorrente = gameEngine.getCurrentRoom();

        roomLabel.setText("Stanza: " + stanzaCorrente);
        String msg = (stanzaCorrente % 5 == 0)
                ? " ATTENZIONE: UN BOSS APPARE!"
                : "Sei entrato in una nuova stanza.";
        gameLog.appendText("\n" + msg + "\nIncontrati "
                + gameEngine.getCurrentEncounter().size() + " nemici.\n");

        updateUIStats();
    }

    @FXML
    public void onAttackButtonClick() {
        if (gameEngine.isEncounterCleared()) {
            prossimaStanza();
            return;
        }

        List<CombatAction> actions = new ArrayList<>();
        for (Hero hero : gameEngine.getParty()) {
            if (!hero.isAlive()) continue;

            String actionName = heroActionSelectors.get(hero).getValue();
            String targetName = heroTargetSelectors.get(hero).getValue();

            ActionType type = actionName.equals(hero.getBuffName()) ? hero.getBuffActionType() : hero.getAttackActionType();
            List<GameCharacter> targets = new ArrayList<>();

            if (type == ActionType.BUFF_DEFENSE_PARTY || type == ActionType.HEAL_PARTY) {
                targets.addAll(gameEngine.getParty());
            } else if (type == ActionType.AOE_ATTACK) {
                targets.addAll(gameEngine.getCurrentEncounter());
            } else if (type == ActionType.BUFF_ATTACK_SINGLE) {
                Hero selectedAlly = gameEngine.getParty().stream().filter(h -> h.getName().equals(targetName)).findFirst().orElse(hero);
                targets.add(selectedAlly);
            } else {
                Monster selectedMonster = gameEngine.getCurrentEncounter().stream()
                        .filter(m -> m.getName().equals(targetName))
                        .findFirst()
                        .orElse(gameEngine.getCurrentEncounter().get(0));
                targets.add(selectedMonster);
            }

            actions.add(new CombatAction(hero, targets, type));
        }

        gameLog.appendText("\n" + gameEngine.executeRound(actions));

        if (gameEngine.isEncounterCleared()) {
            gameLog.appendText("\nStanza ripulita! Clicca di nuovo per avanzare.\n");
        }
        if (gameEngine.isPartyDefeated()){
            mostraGameOver();
            return;
        }
        updateUIStats();
    }

    private void updateUIStats() {
        partyBox.getChildren().clear();
        enemyBox.getChildren().clear();
        commandCenterBox.getChildren().clear();

        List<String> monsterNames = gameEngine.getCurrentEncounter().stream().filter(Monster::isAlive).map(Monster::getName).toList();
        List<String> heroNames = gameEngine.getParty().stream().filter(Hero::isAlive).map(Hero::getName).toList();

        for (Monster m : gameEngine.getCurrentEncounter()) {
            if (m.isAlive()) addStatBar(enemyBox, m, "red");
        }

        for (Hero hero : gameEngine.getParty()) {
            if (!hero.isAlive()) continue;
            addStatBar(partyBox, hero, "green");

            VBox heroCommandBox = new VBox(5);
            heroCommandBox.setAlignment(javafx.geometry.Pos.CENTER);
            HBox row = new HBox(10);
            row.setAlignment(javafx.geometry.Pos.CENTER);

            ComboBox<String> actionCb = new ComboBox<>();
            actionCb.getItems().addAll(hero.getAttackName(), hero.getBuffName());

            ComboBox<String> targetCb = new ComboBox<>();
            Label personalDescLabel = new Label(hero.getAttackDescription());
            personalDescLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #555555; -fx-font-size: 11px;");

            actionCb.valueProperty().addListener((obs, oldV, newV) -> {
                personalDescLabel.setText(newV.equals(hero.getAttackName()) ? hero.getAttackDescription() : hero.getBuffDescription());
                targetCb.getItems().clear();

                if (newV.equals(hero.getAttackName())) {
                    if (hero.getAttackActionType() == ActionType.AOE_ATTACK) {
                        targetCb.setDisable(true);
                    } else {
                        targetCb.getItems().addAll(monsterNames);
                        targetCb.setDisable(false);
                    }
                } else {
                    if (hero.requiresExternalBuffTarget()) {
                        targetCb.getItems().addAll(heroNames);
                        targetCb.setDisable(false);
                    } else {
                        targetCb.setDisable(true);
                    }
                }
                if (!targetCb.getItems().isEmpty()) targetCb.getSelectionModel().selectFirst();
            });

            actionCb.getSelectionModel().selectFirst();
            heroActionSelectors.put(hero, actionCb);
            heroTargetSelectors.put(hero, targetCb);
            row.getChildren().addAll(new Label(hero.getName() + ":"), actionCb, targetCb);
            heroCommandBox.getChildren().addAll(row, personalDescLabel);
            commandCenterBox.getChildren().add(heroCommandBox);
        }
    }

    private void addStatBar(VBox box, GameCharacter c, String color) {
        VBox characterBox = new VBox(5);

        if (c instanceof Hero) {
            characterBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        } else {
            characterBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        }

        Label nameLabel;
        Label detailLabel = new Label();

        if (c instanceof Hero) {
            Hero h = (Hero) c;
            nameLabel = new Label(h.getName() + " (Lvl " + h.getLevel() + ")");
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #2c3e50;");

            detailLabel.setText("XP: " + h.getExperience() + " / " + (h.getLevel() * 100) + "  |  HP: " + h.getHealth() + " / " + h.getMaxHealth());
            detailLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d; -fx-font-weight: bold;");
        } else {
            nameLabel = new Label(c.getName());
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #c0392b;");

            detailLabel.setText("HP: " + c.getHealth() + " / " + c.getMaxHealth());
            detailLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d; -fx-font-weight: bold;");
        }

        ProgressBar hpBar = new ProgressBar((double) c.getHealth() / c.getMaxHealth());
        hpBar.setPrefWidth(240);
        hpBar.setPrefHeight(18);

        hpBar.setStyle("-fx-accent: " + color + "; -fx-control-inner-background: #ecf0f1; -fx-background-color: transparent; -fx-padding: 0;");

        characterBox.getChildren().addAll(nameLabel, hpBar, detailLabel);
        box.getChildren().add(characterBox);
    }

    private void mostraGameOver() {
        gameScreen.setVisible(false);
        gameScreen.setManaged(false);
        gameOverScreen.setVisible(true);
        gameOverScreen.setManaged(true);

        StringBuilder stats = new StringBuilder();
        stats.append(" Profondità Massima: Stanza ").append(gameEngine.getCurrentRoom()).append("\n\n");

        stats.append(" Bestiario Sconfitto:\n");
        Map<String, Integer> kills = gameEngine.getKillCount();
        if (kills.isEmpty()) {
            stats.append(" Nessuno, fallimento.\n");
        } else {
            kills.forEach((nome, quantita) ->
                    stats.append("   - ").append(nome).append(": ").append(quantita).append(" uccisi\n")
            );
        }

        stats.append("\n Stato finale degli eroi:\n");
        for (Hero h : gameEngine.getParty()) {
            stats.append("   - ").append(h.getName()).append(": Livello ").append(h.getLevel()).append("\n");
        }

        ScoreManager.saveRunStats(gameEngine.getCurrentRoom(), kills, gameEngine.getParty());
        statsLabel.setText(stats.toString());
    }

    @FXML
    public void onExitClick() {
        System.exit(0);
    }
}