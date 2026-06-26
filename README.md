# Endless Dungeon RPG
Un Dungeon Crawler procedurale in stile *Roguelike/Endless Run*, sviluppato in Java 21 con interfaccia grafica **JavaFX**. Il progetto implementa rigorosamente l'architettura **MVC** e i principi **SOLID** per garantire un codice modulare, manutenibile e predisposto a future estensioni.

## Caratteristiche Tecniche Principali
* **Architettura e SOLID (OCP):** Il sistema di combattimento è completamente disaccoppiato dalle logiche specifiche delle singole entità. L'intelligenza artificiale dei nemici e le mosse degli eroi sfruttano il polimorfismo, garantendo il pieno rispetto dell'Open/Closed Principle.
* **Persistenza e Validazione (DOM e XSD):** I salvataggi non sono semplici serializzazioni piatte, ma veri e propri alberi gerarchici manipolati in memoria tramite API JAXP (Parser DOM). Ogni scrittura su file (`run_history.xml`) è preceduta da una severa validazione formale a runtime contro uno schema XML (`run_history.xsd`).
* **UI Dinamica:** L'interfaccia JavaFX è progettata per calcolare dinamicamente le proprie dimensioni (Size-To-Scene) bloccando i resize distruttivi e incapsulando i testi in elementi a scorrimento, garantendo usabilità su ogni display.

## Panoramica del Gioco
Il giocatore gestisce un party di 3 eroi selezionati da un roster di 5 classi (Guerriero, Mago, Druido, Paladino, Ladro). L'obiettivo è sopravvivere il più a lungo possibile in un dungeon infinito.
I nemici e i Boss (che appaiono ogni 5 stanze) scalano dinamicamente in difficoltà (HP e Attacco) a ogni livello superato, costringendo il giocatore a utilizzare combinazioni tattiche di cure, buff difensivi/offensivi e attacchi ad area.
Al Game Over, il sistema storicizza i dati della spedizione nel documento XML, salvando i livelli raggiunti dal party, la profondità del dungeon e il bestiario delle uccisioni.

## Requisiti e Avvio Rapido
In conformità con le specifiche del corso, il progetto è configurato per essere scaricato, compilato ed eseguito interamente tramite **Gradle** da riga di comando.

**Requisiti di Sistema:**
- JDK 21+ installato

**Istruzioni di Esecuzione (Terminale):**
1. Compilazione: `./gradlew build` (o `.\gradlew build` su Windows PowerShell)
2. Esecuzione: `./gradlew run` (o `.\gradlew run` su Windows PowerShell)

**Avvio alternativo da IDE:**
Aprire il progetto tramite l'IDE, attendere la sincronizzazione di Gradle ed eseguire la classe `Launcher` situata nel package `view`.

---

### Dichiarazione sull'uso dell'Intelligenza Artificiale
Durante la realizzazione di questo progetto, sono stati impiegati strumenti di Intelligenza Artificiale come supporto allo sviluppo. L'IA è stata utilizzata per brainstorming sulle formule di bilanciamento del gameplay (scaling dei mostri) e come supporto al debugging della configurazione dell'ambiente di lavoro (Gradle).
Una dichiarazione dettagliata dei task specifici è disponibile nella Wiki del progetto.