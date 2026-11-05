package com.ipi.pokemon.services.impl;

import com.ipi.pokemon.dto.DresseurDto;
import com.ipi.pokemon.entities.DresseurEntity;
import com.ipi.pokemon.entities.PokemonEntity;
import com.ipi.pokemon.exceptions.DresseurIntrouvableException;
import com.ipi.pokemon.repositories.DresseurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DresseurServiceTest {

    private static final Long ID_DRESSEUR = 1L;

    @Mock
    private DresseurRepository repository;

    @InjectMocks
    private DresseurService service;

    @Test
    void should_return_sum_of_levels_when_no_bonus_applies() {
        // Arrange
        simulerDresseur(pokemon("Feu", 10), pokemon("Eau", 20));

        // Act
        int niveau = service.calculerNiveauDresseur(ID_DRESSEUR);

        // Assert
        assertEquals(30, niveau); // 10 + 20
    }

    @Test
    void should_return_1_when_dresseur_has_no_pokemon() {
        // Arrange
        simulerDresseur();

        // Act
        int niveau = service.calculerNiveauDresseur(ID_DRESSEUR);

        // Assert
        assertEquals(1, niveau);
    }

    @Test
    void should_apply_diversity_bonus_when_dresseur_has_3_different_types() {
        // Arrange
        simulerDresseur(pokemon("Feu", 10), pokemon("Eau", 20), pokemon("Électrik", 30));

        // Act
        int niveau = service.calculerNiveauDresseur(ID_DRESSEUR);

        // Assert
        assertEquals(80, niveau); // 10 + 20 + 30 + 20 (diversité)
    }

    @Test
    void should_not_apply_diversity_bonus_when_dresseur_has_3_pokemons_of_2_types() {
        // Arrange
        simulerDresseur(pokemon("Feu", 10), pokemon("Feu", 20), pokemon("Eau", 30));

        // Act
        int niveau = service.calculerNiveauDresseur(ID_DRESSEUR);

        // Assert
        assertEquals(60, niveau); // 10 + 20 + 30, pas de bonus
    }

    @Test
    void should_apply_elite_bonus_when_pokemon_level_is_50() {
        // Arrange
        simulerDresseur(pokemon("Feu", 50));

        // Act
        int niveau = service.calculerNiveauDresseur(ID_DRESSEUR);

        // Assert
        assertEquals(60, niveau); // 50 + 10 (élite)
    }

    @Test
    void should_not_apply_elite_bonus_when_pokemon_level_is_49() {
        // Arrange
        simulerDresseur(pokemon("Feu", 49));

        // Act
        int niveau = service.calculerNiveauDresseur(ID_DRESSEUR);

        // Assert
        assertEquals(49, niveau);
    }

    @Test
    void should_apply_elite_bonus_for_each_pokemon_when_dresseur_has_several_elite_pokemons() {
        // Arrange
        simulerDresseur(pokemon("Feu", 60), pokemon("Feu", 70));

        // Act
        int niveau = service.calculerNiveauDresseur(ID_DRESSEUR);

        // Assert
        assertEquals(150, niveau); // 60 + 70 + 2 x 10 (élite)
    }

    @Test
    void should_cumulate_all_bonuses_when_dresseur_has_3_types_and_elite_pokemons() {
        // Arrange
        simulerDresseur(pokemon("Électrik", 55), pokemon("Feu", 60), pokemon("Eau", 20));

        // Act
        int niveau = service.calculerNiveauDresseur(ID_DRESSEUR);

        // Assert
        assertEquals(175, niveau); // 55 + 60 + 20 + 20 (diversité) + 2 x 10 (élite)
    }

    @Test
    void should_throw_exception_when_calculating_level_of_unknown_dresseur() {
        // Arrange
        when(repository.findById(ID_DRESSEUR)).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(DresseurIntrouvableException.class, () -> service.calculerNiveauDresseur(ID_DRESSEUR));
    }

    @Test
    void should_return_dto_with_name_count_and_level_when_dresseur_exists() {
        // Arrange
        simulerDresseur(pokemon("Feu", 10), pokemon("Eau", 20));

        // Act
        DresseurDto dto = service.trouverDresseur(ID_DRESSEUR);

        // Assert
        assertEquals("Sacha", dto.nomDresseur());
        assertEquals(2, dto.nombrePokemons());
        assertEquals(30, dto.niveauDresseur());
    }

    @Test
    void should_throw_exception_when_searching_unknown_dresseur() {
        // Arrange
        when(repository.findById(ID_DRESSEUR)).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(DresseurIntrouvableException.class, () -> service.trouverDresseur(ID_DRESSEUR));
    }

    private void simulerDresseur(PokemonEntity... pokemons) {
        DresseurEntity dresseur = new DresseurEntity();
        dresseur.setId(ID_DRESSEUR);
        dresseur.setNom("Sacha");
        dresseur.setRegion("Kanto");
        dresseur.setPokemons(List.of(pokemons));
        when(repository.findById(ID_DRESSEUR)).thenReturn(Optional.of(dresseur));
    }

    private PokemonEntity pokemon(String type, int niveau) {
        PokemonEntity pokemon = new PokemonEntity();
        pokemon.setType(type);
        pokemon.setNiveau(niveau);
        return pokemon;
    }
}
