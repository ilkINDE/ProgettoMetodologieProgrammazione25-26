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
        return killCount;
    }

    public String executeTacticalTurn(Monster monster, List<Hero> heroes, List<Monster> allies) {
        return monster.executeTurn(heroes, allies);
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
                        // Chi ha ucciso riceve il massimo della exp
                        h.gainExperience(totalXp);
                        log.append("    [!] ").append(h.getName()).append(" riceve ").append(totalXp).append(" XP (Colpo di grazia)!\n");
                    } else {
                        // Gli altri ricevono metà exp
                        h.gainExperience(sharedXp);
                        log.append("    [+] ").append(h.getName()).append(" riceve ").append(sharedXp).append(" XP (Assistenza).\n");
                    }
                }
            }
            enemies.remove(target);
        }
    }
}