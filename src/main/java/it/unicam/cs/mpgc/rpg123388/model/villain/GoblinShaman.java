package it.unicam.cs.mpgc.rpg123388.model.villain;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;

import java.util.List;
import java.util.Random;

public class GoblinShaman extends Monster {
    public GoblinShaman() {
        super("Goblin Sciamano", 25, 4, 20);
    }

    @Override
    public String executeTurn(List<Hero> heroes, List<Monster> allies) {
        if (heroes.isEmpty()) return "";

        // Cerca eroe ferito
        Monster woundedAlly = null;
        for (Monster m : allies) {
            if (m.getHealth() < m.getMaxHealth()) {
                woundedAlly = m;
                break;
            }
        }

        if (woundedAlly != null) {
            int healAmount = this.getAttackPower();
            woundedAlly.heal(healAmount);
            return this.getName() + " lancia un incantesimo e cura " + woundedAlly.getName() + " di " + healAmount + " HP!";
        } else {
            Hero target = heroes.get(new Random().nextInt(heroes.size()));
            int damage = this.getAttackPower();
            target.takeDamage(damage);
            return this.getName() + " scaglia una maledizione su " + target.getName() + " infliggendo " + damage + " danni!";
        }
    }
}