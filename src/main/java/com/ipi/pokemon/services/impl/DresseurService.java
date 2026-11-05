package com.ipi.pokemon.services.impl;

import com.ipi.pokemon.dto.DresseurDto;
import com.ipi.pokemon.entities.DresseurEntity;
import com.ipi.pokemon.entities.PokemonEntity;
import com.ipi.pokemon.exceptions.DresseurIntrouvableException;
import com.ipi.pokemon.repositories.DresseurRepository;
import com.ipi.pokemon.services.interfaces.IDresseurService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DresseurService implements IDresseurService {

    static final int NIVEAU_SANS_POKEMON = 1;
    static final int NB_TYPES_POUR_BONUS_DIVERSITE = 3;
    static final int BONUS_DIVERSITE = 20;
    static final int NIVEAU_MIN_ELITE = 50;
    static final int BONUS_ELITE = 10;

    private final DresseurRepository repository;

    public DresseurService(DresseurRepository repository) {
        this.repository = repository;
    }

    @Override
    public DresseurDto trouverDresseur(Long dresseurId) {
        DresseurEntity dresseur = chargerDresseur(dresseurId);
        List<PokemonEntity> pokemons = dresseur.getPokemons();
        return new DresseurDto(dresseur.getNom(), pokemons.size(), calculerNiveau(pokemons));
    }

    @Override
    public int calculerNiveauDresseur(Long dresseurId) {
        return calculerNiveau(chargerDresseur(dresseurId).getPokemons());
    }

    private DresseurEntity chargerDresseur(Long dresseurId) {
        return repository.findById(dresseurId)
                .orElseThrow(() -> new DresseurIntrouvableException(dresseurId));
    }

    private int calculerNiveau(List<PokemonEntity> pokemons) {
        if (pokemons.isEmpty())
            return NIVEAU_SANS_POKEMON;

        return sommeDesNiveaux(pokemons) + bonusDiversite(pokemons) + bonusElite(pokemons);
    }

    private int sommeDesNiveaux(List<PokemonEntity> pokemons) {
        return pokemons.stream().mapToInt(PokemonEntity::getNiveau).sum();
    }

    private int bonusDiversite(List<PokemonEntity> pokemons) {
        long nbTypesDistincts = pokemons.stream().map(PokemonEntity::getType).distinct().count();
        return nbTypesDistincts >= NB_TYPES_POUR_BONUS_DIVERSITE ? BONUS_DIVERSITE : 0;
    }

    private int bonusElite(List<PokemonEntity> pokemons) {
        long nbPokemonsElite = pokemons.stream().filter(p -> p.getNiveau() >= NIVEAU_MIN_ELITE).count();
        return (int) nbPokemonsElite * BONUS_ELITE;
    }
}
