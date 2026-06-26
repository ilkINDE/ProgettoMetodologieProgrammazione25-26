package it.unicam.cs.mpgc.rpg123388.model.villain;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;

import java.util.List;

public class Goblin extends Monster{
    public Goblin() {
        super("Goblin Soldato", 30, 6, 10);
    }

    @Override
    public String executeTurn(List<Hero> heroes, List<Monster> allies) {
        if (heroes.isEmpty()) return "";

        // Sceglie un bersaglio a caso
        Hero target = heroes.get(new java.util.Random().nextInt(heroes.size()));

        int damage = this.getAttackPower();
        target.takeDamage(damage);

        return this.getName() + " attacca rapidamente " + target.getName() + " infliggendo " + damage + " danni.";
    }
}
