package it.unicam.cs.mpgc.rpg123388.model;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;
import it.unicam.cs.mpgc.rpg123388.model.villain.Monster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CombatManager {

    private final Random random = new Random();
    private Map<String, Integer> killCount = new HashMap<>();

    public Map<String, Integer> getKillCount() {
        return java.util.Collections.unmodifiableMap(killCount);
    }

    public String executeTacticalTurn(List<CombatAction> playerActions, List<Hero> party, List<Monster> enemies) {
        StringBuilder log = new StringBuilder();

        for (Hero h : party) h.resetTemporaryBuffs();

        for (CombatAction action : playerActions) {
            Hero hero = action.getActor();
            if (!hero.isAlive()) continue;

            switch (action.getType()) {
                case SINGLE_ATTACK:
                    GameCharacter target = action.getTargets().get(0);
                    if (target.isAlive() && target instanceof Monster) {
                        log.append(hero.getName()).append(" si scaglia su ").append(target.getName()).append("!\n");
                        dealDamageToMonster(hero, (Monster) target, log, enemies, party);
                    }
                    break;

                case AOE_ATTACK:
                    log.append(hero.getName()).append(" lancia ").append(hero.getAttackName()).append(" su tutti i nemici!\n");
                    List<Monster> targetsCopy = List.copyOf(enemies);
                    for (Monster m : targetsCopy) {
                        dealDamageToMonster(hero, m, log, enemies, party);
                    }
                    break;

                case HEAL_PARTY:
                    log.append(hero.getName()).append(" lancia ").append(hero.getBuffName()).append("! Il party recupera HP.\n");
                    for (Hero h : party) {
                        if (h.isAlive()) {
                            h.heal(hero.getBuffEffectValue());
                        }
                    }
                    break;

                case BUFF_DEFENSE_PARTY:
                    log.append(hero.getName()).append(" lancia ").append(hero.getBuffName()).append("! Danni ridotti per questo turno.\n");
                    for (Hero h : party) {
                        if (h.isAlive()) h.addTemporaryDamageReduction(hero.getBuffEffectValue());
                    }
                    break;

                case BUFF_ATTACK_SINGLE:
                    GameCharacter ally = action.getTargets().get(0);
                    if (ally.isAlive() && ally instanceof Hero) {
                        log.append(hero.getName()).append(" lancia ").append(hero.getBuffName()).append(" su ").append(ally.getName()).append("!\n");
                        ((Hero) ally).addTemporaryAttackBoost(hero.getBuffEffectValue());
                    }
                    break;
            }
        }

        log.append("\n --- Controffensiva Nemica ---\n");
        for (Monster monster : enemies) {
            if (monster.isAlive() && !party.isEmpty()) {
                List<Hero> aliveHeroes = party.stream().filter(Hero::isAlive).toList();
                if (!aliveHeroes.isEmpty()) {
                    String turnLog = monster.executeTurn(aliveHeroes, enemies);
                    if (turnLog != null && !turnLog.isEmpty()) {
                        log.append(turnLog).append("\n");
                    }
                }
            }
        }

        return log.toString();
    }

    private void dealDamageToMonster(Hero hero, Monster target, StringBuilder log, List<Monster> enemies, List<Hero> party) {
        target.takeDamage(hero.getAttackPower());
        log.append(" -> ").append(target.getName()).append(" subisce ").append(hero.getAttackPower()).append(" danni.\n");

        if (!target.isAlive()) {
            log.append(" -> ").append(target.getName()).append(" è stato sconfitto!\n");
            killCount.put(target.getName(), killCount.getOrDefault(target.getName(), 0) + 1);

            int totalXp = target.getXpReward();
            int sharedXp = totalXp / 2;

            for (Hero h : party) {
                if (h.isAlive()) {
                    if (h == hero) {
                        h.gainExperience(totalXp);
                        log.append("    [!] ").append(h.getName()).append(" riceve ").append(totalXp).append(" XP (Colpo di grazia)!\n");
                    } else {
                        h.gainExperience(sharedXp);
                        log.append("    [+] ").append(h.getName()).append(" riceve ").append(sharedXp).append(" XP (Assistenza).\n");
                    }
                }
            }
            enemies.remove(target);
        }
    }
}