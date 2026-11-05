package com.ipi.pokemon.repositories;

import com.ipi.pokemon.entities.DresseurEntity;
import com.ipi.pokemon.entities.PokemonEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DresseurRepositoryTest {

    private static final Long ID_SACHA = 1L;
    private static final Long ID_PIERRE = 3L;
    private static final Long ID_INEXISTANT = 999L;

    @Autowired
    private DresseurRepository repository;

    @Test
    void should_return_3_dresseurs_when_data_sql_is_loaded() {
        // Arrange
        // jeu de données chargé par data.sql

        // Act
        List<DresseurEntity> dresseurs = repository.findAll();

        // Assert
        assertEquals(3, dresseurs.size());
    }

    @Test
    void should_return_dresseur_with_his_pokemons_when_dresseur_exists() {
        // Arrange
        // Sacha possède 3 Pokémon dans data.sql

        // Act
        DresseurEntity sacha = repository.findById(ID_SACHA).orElseThrow();

        // Assert
        assertEquals("Sacha", sacha.getNom());
        assertEquals("Kanto", sacha.getRegion());
        assertEquals(3, sacha.getPokemons().size());
        assertTrue(sacha.getPokemons().stream().anyMatch(p -> p.getNom().equals("Pikachu")));
    }

    @Test
    void should_load_pokemon_type_with_accent_when_data_sql_is_utf8() {
        // Arrange
        // Pikachu est de type 'Électrik' dans data.sql

        // Act
        DresseurEntity sacha = repository.findById(ID_SACHA).orElseThrow();
        PokemonEntity pikachu = sacha.getPokemons().stream()
                .filter(p -> p.getNom().equals("Pikachu"))
                .findFirst()
                .orElseThrow();

        // Assert
        assertEquals("Électrik", pikachu.getType());
        assertEquals(55, pikachu.getNiveau());
    }

    @Test
    void should_return_empty_pokemon_list_when_dresseur_has_no_pokemon() {
        // Arrange
        // Pierre n'a aucun Pokémon dans data.sql

        // Act
        DresseurEntity pierre = repository.findById(ID_PIERRE).orElseThrow();

        // Assert
        assertEquals(0, pierre.getPokemons().size());
    }

    @Test
    void should_return_empty_when_dresseur_does_not_exist() {
        // Arrange
        // aucun dresseur avec l'id 999

        // Act
        Optional<DresseurEntity> resultat = repository.findById(ID_INEXISTANT);

        // Assert
        assertTrue(resultat.isEmpty());
    }

    @Test
    void should_persist_dresseur_when_saved() {
        // Arrange
        DresseurEntity regis = new DresseurEntity();
        regis.setNom("Régis");
        regis.setRegion("Kanto");

        // Act
        DresseurEntity sauvegarde = repository.saveAndFlush(regis);

        // Assert
        DresseurEntity relu = repository.findById(sauvegarde.getId()).orElseThrow();
        assertEquals("Régis", relu.getNom());
        assertEquals("Kanto", relu.getRegion());
        assertEquals(4, repository.count());
    }
}
