package com.ipi.pokemon.services.interfaces;

import com.ipi.pokemon.dto.DresseurDto;

public interface IDresseurService {

    DresseurDto trouverDresseur(Long dresseurId);

    int calculerNiveauDresseur(Long dresseurId);
}
