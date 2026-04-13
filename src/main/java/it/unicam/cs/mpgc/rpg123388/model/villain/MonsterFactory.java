package it.unicam.cs.mpgc.rpg123388.model.villain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonsterFactory {

    private final Random random = new Random();
    private int roomCounter = 0;

    public List<Monster> generateNextRoom() {
        roomCounter++;
        List<Monster> encounter = new ArrayList<>();

        if (roomCounter % 5 == 0) {
            encounter.add(new Dragon());
            encounter.add(new GoblinShaman());
            encounter.add(new GoblinShaman());
        } else {
            int numMonsters = random.nextInt(3) + 1;

            for (int i = 0; i < numMonsters; i++) {
                int roll = random.nextInt(100);

                if (roomCounter > 2 && roll < 30) {
                    encounter.add(new Orc());
                } else if (roll < 60) {
                    encounter.add(new GoblinShaman());
                } else {
                    encounter.add(new Goblin());
                }
            }
        }

        // In base alla stanza in cui si trova, il mostro viene potenziato
        for (Monster m : encounter) {
            m.scaleDifficulty(roomCounter);
        }

        return encounter;
    }

    public int getRoomCounter() {
        return roomCounter;
    }
}

