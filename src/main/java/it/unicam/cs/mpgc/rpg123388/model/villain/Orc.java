package it.unicam.cs.mpgc.rpg123388.model.villain;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;

import java.util.List;

public class Orc extends Monster {
    public Orc() {
        super("Orco Guerriero", 80, 15, 45);
    }

    @Override
    public String executeTurn(List<Hero> heroes, List<Monster> allies) {
        if (heroes.isEmpty()) return "";

        // Trova l'eroe con meno vita
        Hero target = heroes.get(0);
        for (Hero h : heroes) {
            if (h.getHealth() < target.getHealth()) {
                target = h;
            }
        }

        // Attacco potenziato (x1.5)
        int damage = (int) (this.getAttackPower() * 1.5);
        target.takeDamage(damage);

        return this.getName() + " sferra una mazzata brutale su " + target.getName() + " infliggendo " + damage + " danni!";
    }
}