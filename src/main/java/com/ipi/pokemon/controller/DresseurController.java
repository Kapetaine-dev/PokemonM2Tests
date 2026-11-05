package com.ipi.pokemon.controller;

import com.ipi.pokemon.dto.DresseurDto;
import com.ipi.pokemon.exceptions.DresseurIntrouvableException;
import com.ipi.pokemon.services.interfaces.IDresseurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("dresseurs")
public class DresseurController {

    static final String MESSAGE_DRESSEUR_INTROUVABLE = "Dresseur introuvable";

    private final IDresseurService service;

    public DresseurController(IDresseurService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ResponseEntity<DresseurDto> trouverDresseur(@PathVariable Long id) {
        return ResponseEntity.ok(service.trouverDresseur(id));
    }

    @ExceptionHandler(DresseurIntrouvableException.class)
    public ResponseEntity<String> gererDresseurIntrouvable() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MESSAGE_DRESSEUR_INTROUVABLE);
    }
}
