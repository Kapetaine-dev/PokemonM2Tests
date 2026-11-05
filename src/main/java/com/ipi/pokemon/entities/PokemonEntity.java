package com.ipi.pokemon.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Pokemons")
public class PokemonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "niveau", nullable = false)
    private int niveau;

    // Un Pokémon appartient à un et un seul dresseur (plusieurs Pokémon -> un dresseur)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dresseur")
    private DresseurEntity dresseur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public DresseurEntity getDresseur() {
        return dresseur;
    }

    public void setDresseur(DresseurEntity dresseur) {
        this.dresseur = dresseur;
    }
}
