-- New data. Suitable for import to Postgres. Unchecked for MySQL.
-- Let Hibernate generate the tables first

INSERT INTO province VALUES (1, 26.7191679999999998, 58.374684000000002, 'uhO5DP');
INSERT INTO province VALUES (2, 26.7272579999999991, 58.3772609999999972, 'HioEFk');
INSERT INTO province VALUES (3, 26.3583759999999998, 59.3383340000000032, 'MM1Wya');
INSERT INTO province VALUES (4, 25.6207579999999986, 58.9174780000000027, 'v85ww8');
INSERT INTO province VALUES (5, 26.7498840000000015, 58.3743800000000022, '4mHKyV');
INSERT INTO province VALUES (6, 26.6825070000000011, 58.3579039999999978, 'tKgK4m');

INSERT INTO home_ownership VALUES (1, '2014-10-18 13:49:28.103', '2014-10-18 13:49:28.103', 1);
INSERT INTO home_ownership VALUES (2, '2014-10-18 13:49:28.103', '2014-10-18 13:49:28.103', 1);
INSERT INTO home_ownership VALUES (3, '2014-10-18 13:49:28.103', '2014-10-18 13:49:28.103', 2);
INSERT INTO home_ownership VALUES (4, '2014-10-18 13:49:28.103', '2014-10-18 13:49:28.103', 3);
INSERT INTO home_ownership VALUES (5, '2014-10-18 13:49:28.103', '2014-10-18 13:49:28.103', 4);
INSERT INTO home_ownership VALUES (6, '2014-10-18 13:49:28.103', '2014-10-18 13:49:28.103', 5);

INSERT INTO player VALUES (1, 'mr.tk@pacific.ee', NULL, '2014-10-18 13:49:28.103', 'HDpVys', 'Mr. TK', 1);
INSERT INTO player VALUES (2, 'doge@pacific.ee', NULL, '2014-10-18 13:49:28.103', 'mijsFj', 'Doge', 2);
INSERT INTO player VALUES (3, 'biitnik@pacific.ee', NULL, '2014-10-18 13:49:28.103', 'mS7Px5', 'Biitnik', 3);
INSERT INTO player VALUES (4, 'spinning@pacific.ee', NULL, '2014-10-18 13:49:28.103', 'v9B0zf', 'Spinning S-man', 4);
INSERT INTO player VALUES (5, 'johnnyzq@pacific.ee', NULL, '2014-10-18 13:49:28.103', 'rebKxd', 'JohnnyZQ', 5);
INSERT INTO player VALUES (6, 'kingjaan@pacific.ee', NULL, '2014-10-18 13:49:28.103', 'B84tfc', 'King Jaan', 6);

INSERT INTO unit VALUES (1, 4, 0, 0);
INSERT INTO unit VALUES (2, 2, 0, 0);
INSERT INTO unit VALUES (3, 6, 0, 0);
INSERT INTO unit VALUES (4, 3, 0, 0);
INSERT INTO unit VALUES (5, 1, 0, 0);
INSERT INTO unit VALUES (6, 9, 0, 0);
INSERT INTO unit VALUES (7, 14, 0, 0);
INSERT INTO unit VALUES (8, 5, 0, 0);

INSERT INTO home_ownership_units VALUES (5, 8);
INSERT INTO home_ownership_units VALUES (6, 7);

INSERT INTO ownership VALUES (1, '2014-10-18 13:49:28.243', '2014-10-18 13:49:28.243', 6);
INSERT INTO ownership VALUES (2, '2014-10-18 13:49:28.243', '2014-10-18 13:49:28.243', 1);
INSERT INTO ownership VALUES (3, '2014-10-18 13:49:28.243', '2014-10-18 13:49:28.243', 2);
INSERT INTO ownership VALUES (4, '2014-10-18 13:49:28.243', '2014-10-18 13:49:28.243', 3);
INSERT INTO ownership VALUES (5, '2014-10-18 13:49:28.243', '2014-10-18 13:49:28.243', 4);
INSERT INTO ownership VALUES (6, '2014-10-18 13:49:28.243', '2014-10-18 13:49:28.243', 5);

INSERT INTO ownership_units VALUES (1, 6);
INSERT INTO ownership_units VALUES (2, 1);
INSERT INTO ownership_units VALUES (3, 2);
INSERT INTO ownership_units VALUES (4, 3);
INSERT INTO ownership_units VALUES (5, 4);
INSERT INTO ownership_units VALUES (6, 5);

INSERT INTO player_owned_provinces VALUES (1, 1);
INSERT INTO player_owned_provinces VALUES (1, 2);
INSERT INTO player_owned_provinces VALUES (2, 3);
INSERT INTO player_owned_provinces VALUES (3, 4);
INSERT INTO player_owned_provinces VALUES (4, 5);
INSERT INTO player_owned_provinces VALUES (5, 6);