-- Dresseurs (id générés : 1 = Sacha, 2 = Ondine, 3 = Pierre)
INSERT INTO Dresseurs (nom, region) VALUES ('Sacha', 'Kanto');
INSERT INTO Dresseurs (nom, region) VALUES ('Ondine', 'Kanto');
INSERT INTO Dresseurs (nom, region) VALUES ('Pierre', 'Kanto');

-- Sacha : 3 Pokémon de 3 types distincts, dont 1 élite
INSERT INTO Pokemons (nom, type, niveau, id_dresseur) VALUES ('Pikachu', 'Électrik', 55, 1);
INSERT INTO Pokemons (nom, type, niveau, id_dresseur) VALUES ('Salamèche', 'Feu', 30, 1);
INSERT INTO Pokemons (nom, type, niveau, id_dresseur) VALUES ('Carapuce', 'Eau', 25, 1);

-- Ondine : 1 Pokémon
INSERT INTO Pokemons (nom, type, niveau, id_dresseur) VALUES ('Staross', 'Eau', 40, 2);

-- Pierre : aucun Pokémon
