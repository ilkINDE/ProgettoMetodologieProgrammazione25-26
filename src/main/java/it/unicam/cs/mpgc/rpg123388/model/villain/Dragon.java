package it.unicam.cs.mpgc.rpg123388.model.villain;

import it.unicam.cs.mpgc.rpg123388.model.heros.Hero;

import java.util.List;

public class Dragon extends Monster {
    public Dragon() {
        super("Drago Infernale (BOSS)", 250, 20, 200);
    }

    @Override
    public String executeTurn(List<Hero> heroes, List<Monster> allies) {
        if (heroes.isEmpty()) return "";

        StringBuilder log = new StringBuilder(this.getName() + " sputa fiamme su tutto il party! ");
        int aoeDamage = (this.getAttackPower() / 2) + 1;

        for (Hero h : heroes) {
            h.takeDamage(aoeDamage);
            log.append(h.getName()).append(" subisce ").append(aoeDamage).append(" danni. ");
        }

        return log.toString().trim();
    }
}