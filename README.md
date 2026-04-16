# Tipologia RPG
Un Dungeon Crawler procedurale in stile *Roguelike/Endless Run*, sviluppato in Java 21 con interfaccia grafica **JavaFX**. Il progetto implementa l'architettura **MVC** e i principi **SOLID** per garantire un codice modulare, manutenibile ed estendibile.

## Panoramica del Gioco
Il giocatore gestisce un party di 3 eroi selezionati da un roster di 5 classi (Guerriero, Mago, Druido, Paladino, Ladro). L'obiettivo è sopravvivere il più a lungo possibile in un dungeon infinito. 
I nemici e i Boss (che appaiono ogni 5 stanze) scalano dinamicamente in difficoltà (HP e Attacco) a ogni livello superato, costringendo il giocatore a utilizzare combinazioni di cure, buff difensivi/offensivi e attacchi.
Al Game Over, il sistema salva un **Diario della Spedizione** permanente su file di testo (`run_history.txt`), persistendo i record e le statistiche di gioco.

## Requisiti e Avvio Rapido
In conformità con le specifiche del corso, il progetto è configurato per essere compilato ed eseguito interamente tramite **Gradle** da riga di comando.

**Requisiti di Sistema:**
- JDK 21+ installato e configurato.
- Connessione internet (solo per il primo avvio, per scaricare le dipendenze di Gradle/JavaFX).

**Istruzioni di esecuzione**
Compilazione : comando "./gradlew build"
Esecuzione: comando "./gradlew run" 

Oppure:
aprire la classe Launcher nella cartella view ed eseguire.

Dichiarazione sull'uso dell'Intelligenza Artificiale
Durante la realizzazione di questo progetto, sono stati impiegati strumenti di Intelligenza Artificiale come supporto allo sviluppo. L'IA è stata utilizzata per brainstorming sulle formule di bilanciamento del gameplay (scaling dei mostri) e come supporto al debugging della configurazione dell'ambiente di lavoro (Gradle).
Una dichiarazione dettagliata dei task specifici è disponibile nella Wiki del progetto.
