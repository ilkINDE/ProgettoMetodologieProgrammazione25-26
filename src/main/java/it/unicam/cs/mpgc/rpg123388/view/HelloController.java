package it.unicam.cs.mpgc.rpg123388.view;

import it.unicam.cs.mpgc.rpg123388.model.ActionType;
import it.unicam.cs.mpgc.rpg123388.model.CombatAction;
import it.unicam.cs.mpgc.rpg123388.model.CombatManager;
import it.unicam.cs.mpgc.rpg123388.model.GameCharacter;
import it.unicam.cs.mpgc.rpg123388.model.heros.*;
import it.unicam.cs.mpgc.rpg123388.model.villain.Monster;
import it.unicam.cs.mpgc.rpg123388.model.villain.MonsterFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.*;

public class HelloController {

    @FXML private TextArea gameLog;
    @FXML private VBox partyBox, enemyBox, commandCenterBox;

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
        party = new ArrayList<>(List.of(new Warrior("Arthur"), new Mage("Merlino"), new Druid("Panoramix")));
        currentEncounter = monsterFactory.createEncounter(1);
        updateUIStats();
    }

    private String getAbilityInfo(Hero hero, String actionName) {
        if (actionName == null) return "";
        if (hero instanceof Warrior) {
            if (actionName.contains("Fendente")) return "Attacco singolo potente. Danno fisico alto.";
            return "Grido di battaglia: Riduce i danni subiti dal party per questo turno.";
        }
        if (hero instanceof Mage) {
            if (actionName.contains("Tempesta")) return "Colpisce tutti i nemici con energia arcana.";
            return "Infonde magia in un alleato, aumentando il suo Attacco (+15) per questo turno.";
        }
        if (hero instanceof Druid) {
            if (actionName.contains("Radici")) return "Intrattiene il nemico infliggendo danni diretti.";
            return "Cura l'intero party rigenerando 20 HP a ciascuno.";
        }
        return "";
    }

    private String getHeroAttackName(Hero hero) {
        if (hero instanceof Warrior) return "Fendente Pesante";
        if (hero instanceof Mage) return "Tempesta Arcana (AoE)";
        if (hero instanceof Druid) return "Radici Stritolanti";
        return "Attacco";
    }

    private String getHeroBuffName(Hero hero) {
        if (hero instanceof Warrior) return "Mura di Ferro (Buff Party)";
        if (hero instanceof Mage) return "Saggezza di Avalon (Buff Alleato)";
        if (hero instanceof Druid) return "Elisir di Vita (Cura Party)";
        return "Buff";
    }

    @FXML
    public void onAttackButtonClick() {
        if (currentEncounter.isEmpty()) {
            currentEncounter = monsterFactory.createEncounter(2);
            updateUIStats();
            return;
        }

        List<CombatAction> actions = new ArrayList<>();
        for (Hero hero : party) {
            if (!hero.isAlive()) continue;

            String actionName = heroActionSelectors.get(hero).getValue();
            String targetName = heroTargetSelectors.get(hero).getValue();

            ActionType type;
            List<GameCharacter> targets = new ArrayList<>();

            if (actionName.equals(getHeroAttackName(hero))) {
                if (hero instanceof Warrior || hero instanceof Druid) {
                    type = ActionType.SINGLE_ATTACK;
                    Monster target = currentEncounter.stream()
                            .filter(m -> m.getName().equals(targetName)).findFirst().orElse(currentEncounter.get(0));
                    targets.add(target);
                } else {
                    type = ActionType.AOE_ATTACK;
                    targets.addAll(currentEncounter);
                }
            } else {
                if (hero instanceof Warrior) {
                    type = ActionType.BUFF_DEFENSE_PARTY;
                    targets.addAll(party);
                } else if (hero instanceof Druid) {
                    type = ActionType.HEAL_PARTY;
                    targets.addAll(party);
                } else {
                    type = ActionType.BUFF_ATTACK_SINGLE;
                    Hero targetAlly = party.stream()
                            .filter(h -> h.getName().equals(targetName)).findFirst().orElse(hero);
                    targets.add(targetAlly);
                }
            }

            actions.add(new CombatAction(hero, targets, type));
        }

        gameLog.appendText("\n" + combatManager.executeTacticalTurn(actions, party, currentEncounter));
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
            personalDescLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #555555; -fx-font-size: 12px;");

            actionCb.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null) return;

                personalDescLabel.setText(getAbilityInfo(hero, newVal));

                targetCb.getItems().clear();

                if (newVal.equals(getHeroAttackName(hero))) {
                    if (hero instanceof Mage) {
                        targetCb.setDisable(true);
                    } else {
                        targetCb.getItems().addAll(monsterNames);
                        if (!monsterNames.isEmpty()) targetCb.getSelectionModel().selectFirst();
                        targetCb.setDisable(false);
                    }
                } else {
                    if (hero instanceof Mage) {
                        targetCb.getItems().addAll(heroNames);
                        if (!heroNames.isEmpty()) targetCb.getSelectionModel().selectFirst();
                        targetCb.setDisable(false);
                    } else {
                        targetCb.setDisable(true);
                    }
                }
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
        String infoText;

        if (c instanceof Hero) {
            Hero hero = (Hero) c;
            int xpForNextLevel = hero.getLevel() * 100;
            infoText = hero.getName() + " - Lvl " + hero.getLevel() + " [XP: " + hero.getExperience() + "/" + xpForNextLevel + "]\n"
                    + "HP: " + hero.getHealth() + "/" + hero.getMaxHealth();
        } else {
            infoText = c.getName() + " (" + c.getHealth() + "/" + c.getMaxHealth() + ")";
        }

        Label l = new Label(infoText);
        l.setStyle("-fx-font-weight: bold; -fx-text-alignment: center;");

        ProgressBar b = new ProgressBar((double) c.getHealth() / c.getMaxHealth());
        b.setStyle("-fx-accent: " + color + ";");

        box.getChildren().addAll(l, b);
    }
}