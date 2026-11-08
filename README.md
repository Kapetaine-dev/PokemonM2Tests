# Pokémon Management API

> **Petit mot avant de commencer :** il fallait rendre une vidéo de présentation, mais mon micro m'a lâché au moment de l'enregistrer. J'ai donc écrit ici ce que j'aurais expliqué à l'oral. Désolé pour ce contretemps !

## Le projet

Il s'agit d'un microservice Spring Boot 4 (Java 17) pour la Ligue Pokémon. Il expose `GET /dresseurs/{id}`, qui renvoie le nom du dresseur, son nombre de Pokémon et son niveau. L'essentiel du travail porte sur les tests des trois couches.

Pour lancer le build et les tests :

```bash
./mvnw clean install     # ou mvnw.cmd clean install sous Windows
```

Les 20 tests passent et le build se termine en `BUILD SUCCESS`.

## Comment c'est organisé

J'ai découpé le code en trois couches, chacune avec un seul rôle :

- **Controller** : reçoit la requête et renvoie le bon code HTTP. Il ne calcule rien.
- **Service** : contient la règle métier. Le controller ne le connaît qu'à travers son interface `IDresseurService`, ce qui me permet de le remplacer par un mock dans les tests.
- **Repository** : s'occupe de l'accès à la base avec Spring Data JPA.

Le client ne reçoit jamais l'entité directement, seulement un `DresseurDto` qui contient les trois champs demandés.

## La règle de calcul du niveau

On part de la somme des niveaux des Pokémon du dresseur. On ajoute **+20** s'il a au moins 3 types différents, et **+10** pour chaque Pokémon de niveau 50 ou plus. Un dresseur sans Pokémon est au niveau 1.

Exemple avec Sacha : Pikachu (55), Salamèche (30) et Carapuce (25), ce qui donne 110. Ses trois types différents rapportent +20, et Pikachu, au-dessus de 50, rapporte +10. Sacha est donc au **niveau 140**.

## Les tests

Tous les tests suivent le nommage `should_..._when_...` et sont découpés en `// Arrange`, `// Act` et `// Assert`.

- **Repository (6 tests)** : avec `@DataJpaTest` sur une vraie base H2, remplie par `data.sql` (Sacha a 3 Pokémon, Ondine 1, Pierre aucun). Je vérifie le chargement des données, la relation dresseur → Pokémon et la sauvegarde.
- **Service (11 tests)** : en isolation totale, sans Spring ni base. Le repository est mocké avec `when(...).thenReturn(...)`. J'ai couvert le cas normal, le cas sans Pokémon, chaque bonus, le cumul des bonus et le dresseur inconnu. J'ai aussi testé les cas limites : un niveau 49 ne donne pas de bonus alors qu'un niveau 50 en donne, et 3 Pokémon de seulement 2 types ne donnent pas le bonus diversité.
- **Controller (2 tests)** : avec `@WebMvcTest` et `MockMvc`, le service étant mocké. Je vérifie le 200 avec le bon JSON, et le 404 quand le dresseur n'existe pas.
- Un dernier test vérifie simplement que l'application démarre.

Merci pour votre lecture !
