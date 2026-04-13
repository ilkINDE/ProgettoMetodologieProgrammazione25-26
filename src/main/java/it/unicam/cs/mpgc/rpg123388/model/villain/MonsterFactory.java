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
    private int roomCounter = 0; // Per tenere traccia delle stanze completate

    public List<Monster> generateNextRoom() {
        roomCounter++;
        List<Monster> encounter = new ArrayList<>();

        // Ogni 5 stanze spawna il Boss
        if (roomCounter % 5 == 0) {
            encounter.add(new Dragon());
            // Magari con un paio di sciamani come scorta
            encounter.add(new GoblinShaman());
            encounter.add(new GoblinShaman());
            return encounter;
        }

        int numMonsters = random.nextInt(3) + 1;

        for (int i = 0; i < numMonsters; i++) {
            int roll = random.nextInt(100);

            // Più si va avanti, più è probabile trovare Orchi forti
            if (roomCounter > 2 && roll < 30) {
                encounter.add(new Orc());
            } else if (roll < 60) {
                encounter.add(new GoblinShaman());
            } else {
                encounter.add(new Goblin());
            }
        }

        return encounter;
    }

    public int getRoomCounter() {
        return roomCounter;
    }
}

