package com.ipi.pokemon.controller;

import com.ipi.pokemon.dto.DresseurDto;
import com.ipi.pokemon.exceptions.DresseurIntrouvableException;
import com.ipi.pokemon.services.interfaces.IDresseurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DresseurController.class)
class DresseurControllerTest {

    private static final Long ID_DRESSEUR = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IDresseurService service;

    @Test
    void should_return_200_with_dresseur_dto_when_dresseur_exists() throws Exception {
        // Arrange
        when(service.trouverDresseur(ID_DRESSEUR)).thenReturn(new DresseurDto("Sacha", 3, 140));

        // Act
        // Assert
        mockMvc.perform(get("/dresseurs/{id}", ID_DRESSEUR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomDresseur").value("Sacha"))
                .andExpect(jsonPath("$.nombrePokemons").value(3))
                .andExpect(jsonPath("$.niveauDresseur").value(140));
    }

    @Test
    void should_return_404_when_dresseur_does_not_exist() throws Exception {
        // Arrange
        when(service.trouverDresseur(ID_DRESSEUR)).thenThrow(new DresseurIntrouvableException(ID_DRESSEUR));

        // Act
        // Assert
        mockMvc.perform(get("/dresseurs/{id}", ID_DRESSEUR))
                .andExpect(status().isNotFound())
                .andExpect(content().string(DresseurController.MESSAGE_DRESSEUR_INTROUVABLE));
    }
}
