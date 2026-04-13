package it.unicam.cs.mpgc.rpg123388.model;

public enum ActionType {
    SINGLE_ATTACK,      // Attacco a un nemico
    AOE_ATTACK,         // Attacco a tutti i nemici
    HEAL_PARTY,         // cura tutto il party
    BUFF_DEFENSE_PARTY, // Riduce i danni subiti per un turno
    BUFF_ATTACK_SINGLE  // Aumenta l'attacco di un alleato
}