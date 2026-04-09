package it.unicam.cs.mpgc.rpg123388.model;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;
import it.unicam.cs.mpgc.rpg123388.model.villain.Monster;

import java.util.List;
import java.util.Random;

public class CombatManager {

    private final Random random = new Random();

    /**
     * Esegue il turno basandosi sulle scelte tattiche del giocatore.
     *
     * @param playerActions La lista di azioni scelte dal giocatore.
     * @param party         Il party degli eroi.
     * @param enemies       I nemici ancora in vita.
     */

    public String executeTacticalTurn(List<CombatAction> playerActions, List<Hero> party, List<Monster> enemies) {
        StringBuilder log = new StringBuilder();

        // Gli eroi eseguono gli attacchi scelti
        for (CombatAction action : playerActions) {
            Hero hero = action.getActor();

            // Se l'eroe è morto prima di attaccare, salta il turno
            if (!hero.isAlive()) continue;

            if (action.isAoE()) {
                // Attacco ad area
                log.append(hero.getName()).append(" lancia un attacco ad area!\n");

                // Uso una copia della lista per evitare errori mentre si rimuovono mostri sconfitti
                List<Monster> targetsCopy = List.copyOf(action.getTargets());
                for (Monster target : targetsCopy) {
                    dealDamageToMonster(hero, target, log, enemies);
                }
            } else {
                // Attacco a singolo target
                Monster target = action.getTargets().get(0);
                if (target.isAlive()) {
                    log.append(hero.getName()).append(" si scaglia su ").append(target.getName()).append("!\n");
                    dealDamageToMonster(hero, target, log, enemies);
                }
            }
        }

        // fase di contrattacco
        log.append("\n Controffensiva Nemica \n");
        for (Monster monster : enemies) {
            if (monster.isAlive() && !party.isEmpty()) {
                // Prende solo gli eroi vivi
                List<Hero> aliveHeroes = party.stream().filter(Hero::isAlive).toList();
                if (!aliveHeroes.isEmpty()) {
                    Hero target = aliveHeroes.get(random.nextInt(aliveHeroes.size()));
                    target.takeDamage(monster.getAttackPower());
                    log.append(monster.getName()).append(" attacca ").append(target.getName())
                            .append(" per ").append(monster.getAttackPower()).append(" danni.\n");
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
