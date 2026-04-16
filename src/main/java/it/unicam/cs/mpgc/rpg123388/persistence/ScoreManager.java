package it.unicam.cs.mpgc.rpg123388.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScoreManager {
    private static final String FILE_PATH = "run_history.txt";

    public static void saveRunStats(String statsText) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            String record = "SPEDIZIONE DEL " + timestamp + " \n"
                    + statsText;

            Files.write(Paths.get(FILE_PATH), record.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.err.println("Errore nel salvataggio del record: " + e.getMessage());
        }
    }
}