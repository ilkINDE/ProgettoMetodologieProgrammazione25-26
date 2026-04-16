package it.unicam.cs.mpgc.rpg123388.model;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;
import it.unicam.cs.mpgc.rpg123388.model.villain.Dragon;
import it.unicam.cs.mpgc.rpg123388.model.villain.GoblinShaman;
import it.unicam.cs.mpgc.rpg123388.model.villain.Monster;
import it.unicam.cs.mpgc.rpg123388.model.villain.Orc;

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
                        dealDamageToMonster(hero, (Monster) target, log, enemies,party);
                    }
                    break;

                case AOE_ATTACK:
                    log.append(hero.getName()).append(" lancia Tempesta Arcana su tutti i nemici!\n");
                    List<Monster> targetsCopy = List.copyOf(enemies);
                    for (Monster m : targetsCopy) {
                        dealDamageToMonster(hero, m, log, enemies,party);
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
        log.append("\n --- Controffensiva Nemica ---\n");
        for (Monster monster : enemies) {
            if (monster.isAlive() && !party.isEmpty()) {
                List<Hero> aliveHeroes = party.stream().filter(Hero::isAlive).toList();
                if (!aliveHeroes.isEmpty()) {
                    Hero randomHero = aliveHeroes.get(random.nextInt(aliveHeroes.size()));

                    if (monster instanceof GoblinShaman) {
                        // Lo Sciamano ha il 70% di probabilità di buffare i mostri, 30% di attaccare
                        if (random.nextInt(100) < 70) {
                            log.append(monster.getName()).append(" intona un canto! I nemici subiscono meno danni.\n");
                            for (Monster m : enemies) {
                                if (m.isAlive()) m.addTemporaryDamageReduction(5);
                            }
                        } else {
                            int oldHp = randomHero.getHealth();
                            randomHero.takeDamage(monster.getAttackPower());
                            log.append(monster.getName()).append(" lancia una maledizione su ").append(randomHero.getName())
                                    .append(" per ").append(oldHp - randomHero.getHealth()).append(" danni.\n");
                        }
                    }
                    else if (monster instanceof Orc) {
                        // L'Orco ha il 20% di probabilità di buffarsi l'attacco
                        if (random.nextInt(100) < 20) {
                            log.append(monster.getName()).append(" ruggisce di rabbia! Il suo attacco aumenta.\n");
                            monster.addTemporaryAttackBoost(15);
                        } else {
                            int oldHp = randomHero.getHealth();
                            randomHero.takeDamage(monster.getAttackPower());
                            log.append(monster.getName()).append(" colpisce con la clava ").append(randomHero.getName())
                                    .append(" per ").append(oldHp - randomHero.getHealth()).append(" danni.\n");
                        }
                    }
                    else if (monster instanceof Dragon) {
                        // Il Drago ha il 30% di probabilità di usare un attacco ad area
                        if (random.nextInt(100) < 30) {
                            log.append(monster.getName()).append(" scatena un SOFFIO DI FUOCO su tutto il party!\n");
                            for (Hero h : aliveHeroes) {
                                int oldHp = h.getHealth();
                                h.takeDamage(monster.getAttackPower() - 5); // Fa leggermente meno danni ad area
                                log.append(" -> ").append(h.getName()).append(" subisce ").append(oldHp - h.getHealth()).append(" danni.\n");
                            }
                        } else {
                            int oldHp = randomHero.getHealth();
                            randomHero.takeDamage(monster.getAttackPower());
                            log.append(monster.getName()).append(" morde ferocemente ").append(randomHero.getName())
                                    .append(" per ").append(oldHp - randomHero.getHealth()).append(" danni.\n");
                        }
                    }
                    else {
                        // Goblin base o altri mostri: attacco fisico standard
                        int oldHp = randomHero.getHealth();
                        randomHero.takeDamage(monster.getAttackPower());
                        log.append(monster.getName()).append(" attacca ").append(randomHero.getName())
                                .append(" per ").append(oldHp - randomHero.getHealth()).append(" danni.\n");
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