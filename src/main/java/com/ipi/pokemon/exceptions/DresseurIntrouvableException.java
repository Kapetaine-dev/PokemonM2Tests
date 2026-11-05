package com.ipi.pokemon.exceptions;

public class DresseurIntrouvableException extends RuntimeException {

    public DresseurIntrouvableException(Long dresseurId) {
        super("Dresseur introuvable : " + dresseurId);
    }
}
