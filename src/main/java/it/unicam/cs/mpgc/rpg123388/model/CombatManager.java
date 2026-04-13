package it.unicam.cs.mpgc.rpg123388.model;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;
import it.unicam.cs.mpgc.rpg123388.model.villain.Monster;

import java.util.List;
import java.util.Random;

public class CombatManager {

    private final Random random = new Random();

    public String executeTacticalTurn(List<CombatAction> playerActions, List<Hero> party, List<Monster> enemies) {
        StringBuilder log = new StringBuilder();

        // reset dei buff
        for (Hero h : party) h.resetTemporaryBuffs();

        // Esecuzione mosse alleate
        for (CombatAction action : playerActions) {
            Hero hero = action.getActor();
            if (!hero.isAlive()) continue;

            switch (action.getType()) {
                case SINGLE_ATTACK:
                    GameCharacter target = action.getTargets().get(0);
                    if (target.isAlive() && target instanceof Monster) {
                        log.append(hero.getName()).append(" si scaglia su ").append(target.getName()).append("!\n");
                        dealDamageToMonster(hero, (Monster) target, log, enemies);
                    }
                    break;

                case AOE_ATTACK:
                    log.append(hero.getName()).append(" lancia Tempesta Arcana su tutti i nemici!\n");
                    List<Monster> targetsCopy = List.copyOf(enemies);
                    for (Monster m : targetsCopy) {
                        dealDamageToMonster(hero, m, log, enemies);
                    }
                    break;

                case HEAL_PARTY:
                    log.append(hero.getName()).append(" lancia Elisir di Vita! Il party recupera HP.\n");
                    for (Hero h : party) {
                        if (h.isAlive()) {
                            h.heal(20);
                        }
                    }
                    break;

                case BUFF_DEFENSE_PARTY:
                    log.append(hero.getName()).append(" alza le Mura di Ferro! Danni ridotti per questo turno.\n");
                    for (Hero h : party) {
                        if (h.isAlive()) h.addTemporaryDamageReduction(10);
                    }
                    break;

                case BUFF_ATTACK_SINGLE:
                    GameCharacter ally = action.getTargets().get(0);
                    if (ally.isAlive() && ally instanceof Hero) {
                        log.append(hero.getName()).append(" infonde magia su ").append(ally.getName()).append(" (+15 Attacco)!\n");
                        ((Hero) ally).addTemporaryAttackBoost(15);
                    }
                    break;
            }
        }

        // Fase di controffensiva
        log.append("\n Controffensiva Nemica \n");
        for (Monster monster : enemies) {
            if (monster.isAlive() && !party.isEmpty()) {
                List<Hero> aliveHeroes = party.stream().filter(Hero::isAlive).toList();
                if (!aliveHeroes.isEmpty()) {
                    Hero target = aliveHeroes.get(random.nextInt(aliveHeroes.size()));

                    int oldHealth = target.getHealth();
                    target.takeDamage(monster.getAttackPower());
                    int damageTaken = oldHealth - target.getHealth();

                    log.append(monster.getName()).append(" attacca ").append(target.getName())
                            .append(" per ").append(damageTaken).append(" danni.\n");
                }
            }
        }

        return log.toString();
    }

    private void dealDamageToMonster(Hero hero, Monster target, StringBuilder log, List<Monster> enemies) {
        target.takeDamage(hero.getAttackPower());
        log.append(" -> ").append(target.getName()).append(" subisce ").append(hero.getAttackPower()).append(" danni.\n");

        if (!target.isAlive()) {
            log.append(" -> ").append(target.getName()).append(" è stato sconfitto!\n");
            hero.gainExperience(target.getXpReward());
            enemies.remove(target);
        }
    }
}