-- New data. Suitable for import to Postgres. Unchecked for MySQL.
-- Let Hibernate generate the tables first

INSERT INTO province VALUES (1, -40.4195, 144.961);
INSERT INTO province VALUES (2, -40.4195, 144.963);
INSERT INTO province VALUES (3, -40.4195, 144.965);
INSERT INTO province VALUES (4, -40.4205, 144.963);
INSERT INTO province VALUES (5, -40.4215, 144.963);
INSERT INTO province VALUES (6, -40.4225, 144.963);

INSERT INTO home_ownership VALUES (0, '2014-10-18 13:49:28.103', 'lzpD6mFm44', '2014-10-18 13:49:28.103', 1);
INSERT INTO home_ownership VALUES (1, '2014-10-18 13:49:28.103', 'lzpD6mFm44', '2014-10-18 13:49:28.103', 1);
INSERT INTO home_ownership VALUES (2, '2014-10-18 13:49:28.103', 'lzpD6mFm44', '2014-10-18 13:49:28.103', 1);
INSERT INTO home_ownership VALUES (3, '2014-10-18 13:49:28.103', 'n7b4rGpVbt', '2014-10-18 13:49:28.103', 2);
INSERT INTO home_ownership VALUES (4, '2014-10-18 13:49:28.103', 'MW1BDs9stf', '2014-10-18 13:49:28.103', 3);
INSERT INTO home_ownership VALUES (5, '2014-10-18 13:49:28.103', 'pB6RB8S0iL', '2014-10-18 13:49:28.103', 4);
INSERT INTO home_ownership VALUES (6, '2014-10-18 13:49:28.103', 'Kvukx9SCOB', '2014-10-18 13:49:28.103', 5);

INSERT INTO player VALUES (0, 'bot@pacific.ee', '', 'BOT', 0);
INSERT INTO player VALUES (1, 'mr.tk@pacific.ee', 'BPUYYOU62flwiWJe', 'Mr. TK', 1);
INSERT INTO player VALUES (2, 'doge@pacific.ee', '3myBuV7DKARaW14p', 'Doge', 2);
INSERT INTO player VALUES (3, 'biitnik@pacific.ee', 'D6Nua1BPg30CJ84q', 'Biitnik', 3);
INSERT INTO player VALUES (4, 'spinning@pacific.ee', 'Hur9Asc0i5ZdJrco', 'Spinning S-man', 4);
INSERT INTO player VALUES (5, 'johnnyzq@pacific.ee', 'UJ86IpW5xK8ZZH7t', 'JohnnyZQ', 5);
INSERT INTO player VALUES (6, 'kingjaan@pacific.ee', 'B4j1VKLaD1Hvj0ey', 'King Jaan', 6);

--INSERT INTO unit VALUES (1, 4, 0, 0);
INSERT INTO unit VALUES (2, 2, 0, 0);
INSERT INTO unit VALUES (3, 6, 0, 0);
INSERT INTO unit VALUES (4, 3, 0, 0);
INSERT INTO unit VALUES (5, 1, 0, 0);
INSERT INTO unit VALUES (6, 9, 0, 0);
INSERT INTO unit VALUES (7, 10, 0, 0);
INSERT INTO unit VALUES (8, 10, 0, 0);
INSERT INTO unit VALUES (9, 10, 0, 0);
INSERT INTO unit VALUES (10, 10, 0, 0);
INSERT INTO unit VALUES (11, 10, 0, 0);
INSERT INTO unit VALUES (12, 0, 0, 0);

INSERT INTO home_ownership_units VALUES (1, 7);
INSERT INTO home_ownership_units VALUES (2, 8);
INSERT INTO home_ownership_units VALUES (3, 9);
INSERT INTO home_ownership_units VALUES (4, 10);
INSERT INTO home_ownership_units VALUES (5, 11);
INSERT INTO home_ownership_units VALUES (6, 12);

INSERT INTO ownership VALUES (1, '2014-10-18 13:49:28.243', 'Kvukx9SCOB', '2014-10-18 13:49:28.243', 6);
--INSERT INTO ownership VALUES (2, '2014-10-18 13:49:28.243', 'lzpD6mFm44', '2014-10-18 13:49:28.243', 1);
INSERT INTO ownership VALUES (3, '2014-10-18 13:49:28.243', 'n7b4rGpVbt', '2014-10-18 13:49:28.243', 2);
INSERT INTO ownership VALUES (4, '2014-10-18 13:49:28.243', 'MW1BDs9stf', '2014-10-18 13:49:28.243', 3);
INSERT INTO ownership VALUES (5, '2014-10-18 13:49:28.243', 'pB6RB8S0iL', '2014-10-18 13:49:28.243', 4);
INSERT INTO ownership VALUES (6, '2014-10-18 13:49:28.243', 'bFTDPzokok', '2014-10-18 13:49:28.243', 5);

INSERT INTO ownership_units VALUES (1, 6);
--INSERT INTO ownership_units VALUES (2, 1);
INSERT INTO ownership_units VALUES (3, 2);
INSERT INTO ownership_units VALUES (4, 3);
INSERT INTO ownership_units VALUES (5, 4);
INSERT INTO ownership_units VALUES (6, 5);

INSERT INTO player_owned_provinces VALUES (1, 1);
--INSERT INTO player_owned_provinces VALUES (1, 2);
INSERT INTO player_owned_provinces VALUES (2, 3);
INSERT INTO player_owned_provinces VALUES (3, 4);
INSERT INTO player_owned_provinces VALUES (4, 5);
INSERT INTO player_owned_provinces VALUES (5, 6);

INSERT INTO battle_history VALUES (1, 11, 21, true, '2014-10-18 13:49:28.243', 9, 9, 1, 2, 6);
INSERT INTO battle_history VALUES (2, 3, 7, false, '2014-10-19 13:49:28.243', 5, 5, 2, 1, 6);
