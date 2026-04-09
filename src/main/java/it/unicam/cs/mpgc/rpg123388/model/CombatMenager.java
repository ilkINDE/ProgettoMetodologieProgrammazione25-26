package it.unicam.cs.mpgc.rpg123388.model;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;
import it.unicam.cs.mpgc.rpg123388.model.villain.Monster;

public class CombatMenager {

    public String executeTurn(Hero hero, Monster monster) {

        StringBuilder battleLog = new StringBuilder();

        monster.takeDamage(hero.getAttackPower());
        battleLog.append(hero.getName()).append(" attacca ").append(monster.getName())
                .append(" per ").append(hero.getAttackPower()).append(" danni.\n");

        if (!monster.isAlive()) {
            battleLog.append(monster.getName()).append(" è stato sconfitto!\n");


            int xpGained = monster.getXpReward();
            battleLog.append(hero.getName()).append(" guadagna ").append(xpGained).append(" XP.\n");
            hero.gainExperience(xpGained);

            return battleLog.toString();
        }

        return battleLog.toString();
    }
}

