package it.unicam.cs.mpgc.rpg123388.model.villain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe responsabile della generazione dinamica dei nemici.
 * Applica il pattern Factory per nascondere la complessità di creazione dei gruppi (Encounter).
 */

public class MonsterFactory {

    private final Random random = new Random();

    public List<Monster> createEncounter(int difficultyLevel) {
        List<Monster> encounter = new ArrayList<>();

        // Calcola quanti mostri generare: da un minimo di 1 a un massimo di 5,
        // aumentando le probabilità di averne di più se la difficoltà è alta.
        int numMonsters = Math.min(1 + random.nextInt(difficultyLevel + 1), 5);

        for (int i = 0; i < numMonsters; i++) {
            encounter.add(new Goblin());
        }

        return encounter;
    }
}