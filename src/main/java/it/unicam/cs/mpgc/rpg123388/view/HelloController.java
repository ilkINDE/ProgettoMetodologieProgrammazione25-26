package it.unicam.cs.mpgc.rpg123388.view;

import it.unicam.cs.mpgc.rpg123388.model.*;
import it.unicam.cs.mpgc.rpg123388.model.heros.*;
import it.unicam.cs.mpgc.rpg123388.model.villain.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.*;

public class HelloController {

    @FXML private TextArea gameLog;
    @FXML private VBox partyBox, enemyBox, commandCenterBox;
    @FXML private Label roomLabel;

    private List<Hero> party;
    private List<Monster> currentEncounter;
    private CombatManager combatManager;
    private MonsterFactory monsterFactory;

    private Map<Hero, ComboBox<String>> heroActionSelectors = new HashMap<>();
    private Map<Hero, ComboBox<String>> heroTargetSelectors = new HashMap<>();

    @FXML
    public void initialize() {
        combatManager = new CombatManager();
        monsterFactory = new MonsterFactory();

        party = new ArrayList<>(List.of(
                new Warrior("Arthur"),
                new Mage("Merlino"),
                new Druid("Panoramix")
        ));

        prossimaStanza();
    }

    private void prossimaStanza() {
        currentEncounter = monsterFactory.generateNextRoom();
        int stanzaCorrente = monsterFactory.getRoomCounter();

        roomLabel.setText("Stanza: " + stanzaCorrente);

        String msg = (stanzaCorrente % 5 == 0) ? "!!! ATTENZIONE: UN BOSS APPARE !!!" : "Sei entrato in una nuova stanza.";
        gameLog.appendText("\n" + msg + "\nIncontrati " + currentEncounter.size() + " nemici.\n");

        updateUIStats();
    }

    @FXML
    public void onAttackButtonClick() {
        if (currentEncounter.isEmpty()) {
            prossimaStanza();
            return;
        }

        List<CombatAction> actions = new ArrayList<>();
        for (Hero hero : party) {
            if (!hero.isAlive()) continue;

            String actionName = heroActionSelectors.get(hero).getValue();
            String targetName = heroTargetSelectors.get(hero).getValue();

            ActionType type;
            List<GameCharacter> targets = new ArrayList<>();

            if (actionName.contains("Buff") || actionName.contains("Elisir") || actionName.contains("Mura")) {
                if (hero instanceof Warrior) type = ActionType.BUFF_DEFENSE_PARTY;
                else if (hero instanceof Druid) type = ActionType.HEAL_PARTY;
                else {
                    type = ActionType.BUFF_ATTACK_SINGLE;
                    targets.add(party.stream().filter(h -> h.getName().equals(targetName)).findFirst().orElse(hero));
                }
            } else {
                if (hero instanceof Mage) {
                    type = ActionType.AOE_ATTACK;
                    targets.addAll(currentEncounter);
                } else {
                    type = ActionType.SINGLE_ATTACK;
                    targets.add(currentEncounter.stream().filter(m -> m.getName().equals(targetName)).findFirst().orElse(currentEncounter.get(0)));
                }
            }
            actions.add(new CombatAction(hero, targets, type));
        }

        gameLog.appendText("\n" + combatManager.executeTacticalTurn(actions, party, currentEncounter));

        if (currentEncounter.isEmpty()) {
            gameLog.appendText("\nStanza ripulita! Clicca di nuovo per avanzare.\n");
        }

        updateUIStats();
    }

    private void updateUIStats() {
        partyBox.getChildren().clear();
        enemyBox.getChildren().clear();
        commandCenterBox.getChildren().clear();

        List<String> monsterNames = currentEncounter.stream().filter(Monster::isAlive).map(Monster::getName).toList();
        List<String> heroNames = party.stream().filter(Hero::isAlive).map(Hero::getName).toList();

        for (Monster m : currentEncounter) if (m.isAlive()) addStatBar(enemyBox, m, "red");

        for (Hero hero : party) {
            if (!hero.isAlive()) continue;
            addStatBar(partyBox, hero, "green");

            VBox heroCommandBox = new VBox(5);
            heroCommandBox.setAlignment(javafx.geometry.Pos.CENTER);
            HBox row = new HBox(10);
            row.setAlignment(javafx.geometry.Pos.CENTER);

            ComboBox<String> actionCb = new ComboBox<>();
            actionCb.getItems().addAll(getHeroAttackName(hero), getHeroBuffName(hero));

            ComboBox<String> targetCb = new ComboBox<>();
            Label personalDescLabel = new Label(getAbilityInfo(hero, getHeroAttackName(hero)));
            personalDescLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #555555; -fx-font-size: 11px;");

            actionCb.valueProperty().addListener((obs, oldV, newV) -> {
                personalDescLabel.setText(getAbilityInfo(hero, newV));
                targetCb.getItems().clear();
                if (newV.contains("Buff Alleato") || (newV.contains("Saggezza"))) {
                    targetCb.getItems().addAll(heroNames);
                    targetCb.setDisable(false);
                } else if (newV.contains("AoE") || newV.contains("Party")) {
                    targetCb.setDisable(true);
                } else {
                    targetCb.getItems().addAll(monsterNames);
                    targetCb.setDisable(false);
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
        String info;
        if (c instanceof Hero) {
            Hero h = (Hero) c;
            info = String.format("%s - Lvl %d [XP:%d/%d]\nHP:%d/%d",
                    h.getName(), h.getLevel(), h.getExperience(), h.getLevel()*100, h.getHealth(), h.getMaxHealth());
        } else info = c.getName() + " (" + c.getHealth() + "/" + c.getMaxHealth() + ")";

        Label l = new Label(info);
        l.setStyle("-fx-font-weight: bold; -fx-text-alignment: center; -fx-font-size: 11px;");
        ProgressBar b = new ProgressBar((double) c.getHealth() / c.getMaxHealth());
        b.setStyle("-fx-accent: " + color + ";");
        box.getChildren().addAll(l, b);
    }

    private String getAbilityInfo(Hero hero, String actionName) {
        if (hero instanceof Warrior) return actionName.contains("Fendente") ? "Attacco fisico potente." : "Danni subiti -10 (Party).";
        if (hero instanceof Mage) return actionName.contains("Tempesta") ? "Attacco AoE magico." : "Attacco alleato +15.";
        if (hero instanceof Druid) return actionName.contains("Radici") ? "Danno a bersaglio singolo." : "Cura il party (+20 HP).";
        return "";
    }
    private String getHeroAttackName(Hero h) { if(h instanceof Warrior) return "Fendente Pesante"; if(h instanceof Mage) return "Tempesta Arcana (AoE)"; return "Radici Stritolanti"; }
    private String getHeroBuffName(Hero h) { if(h instanceof Warrior) return "Mura di Ferro (Buff)"; if(h instanceof Mage) return "Saggezza di Avalon (Buff)"; return "Elisir di Vita (Cura)"; }
}