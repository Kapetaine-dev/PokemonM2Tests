# Pokémon Management API

Microservice Spring Boot 4 (Java 17) de gestion des dresseurs Pokémon, avec une suite de tests sur les trois couches.

## Lancer le build et les tests

```bash
./mvnw clean install        # Linux / macOS / Git Bash
mvnw.cmd clean install      # Windows
```

## Endpoint

`GET /dresseurs/{id}`

| Cas                 | Statut          | Corps                                                         |
|---------------------|-----------------|---------------------------------------------------------------|
| Le dresseur existe  | `200 OK`        | `{"nomDresseur":"Sacha","nombrePokemons":3,"niveauDresseur":140}` |
| Dresseur inconnu    | `404 Not Found` | `Dresseur introuvable`                                        |

## Règle métier : `DresseurService.calculerNiveauDresseur`

- Base : somme des niveaux des Pokémon
- +20 si le dresseur possède au moins 3 types distincts
- +10 par Pokémon de niveau ≥ 50
- Aucun Pokémon : niveau = 1

## Tests

| Couche     | Classe                   | Outils                                    |
|------------|--------------------------|-------------------------------------------|
| Repository | `DresseurRepositoryTest` | `@DataJpaTest`, H2, `src/test/resources/data.sql` |
| Service    | `DresseurServiceTest`    | Mockito (`@Mock`, `when(...).thenReturn(...)`) |
| Controller | `DresseurControllerTest` | `@WebMvcTest`, `MockMvc`, `@MockitoBean`  |

Nommage : `should_[comportement]_when_[condition]()`. Chaque test est structuré en `// Arrange`, `// Act`, `// Assert`.

## Choix techniques

- **Relation `@ManyToOne` côté Pokémon** : un Pokémon appartient à un et un seul dresseur, un dresseur possède plusieurs Pokémon. `@OneToOne` imposerait une contrainte d'unicité sur `id_dresseur` (un seul Pokémon par dresseur).
- **`404 Not Found` pour un dresseur inexistant** : le sujet mentionne 201, mais 201 signifie « Created » en HTTP ; 404 est le code standard pour une ressource absente.
