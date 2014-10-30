-- New data. Suitable for import to Postgres. Unchecked for MySQL.
-- Let Hibernate generate the tables first

INSERT INTO province VALUES (1, 58.374684000000002, 26.7191679999999998, 'lzpD6mFm44');
INSERT INTO province VALUES (2, 58.3772609999999972, 26.7272579999999991, 'n7b4rGpVbt');
INSERT INTO province VALUES (3, 59.3383340000000032, 26.3583759999999998, 'MW1BDs9stf');
INSERT INTO province VALUES (4, 58.9174780000000027, 25.6207579999999986, 'pB6RB8S0iL');
INSERT INTO province VALUES (5, 58.3743800000000022, 26.7498840000000015, 'bFTDPzokok');
INSERT INTO province VALUES (6, 58.3579039999999978, 26.6825070000000011, 'Kvukx9SCOB');

INSERT INTO home_ownership VALUES (1, '2014-10-18 13:49:28.103', '2014-10-18 13:49:28.103', 1);
INSERT INTO home_ownership VALUES (2, '2014-10-18 13:49:28.103', '2014-10-18 13:49:28.103', 1);
INSERT INTO home_ownership VALUES (3, '2014-10-18 13:49:28.103', '2014-10-18 13:49:28.103', 2);
INSERT INTO home_ownership VALUES (4, '2014-10-18 13:49:28.103', '2014-10-18 13:49:28.103', 3);
INSERT INTO home_ownership VALUES (5, '2014-10-18 13:49:28.103', '2014-10-18 13:49:28.103', 4);
INSERT INTO home_ownership VALUES (6, '2014-10-18 13:49:28.103', '2014-10-18 13:49:28.103', 5);

INSERT INTO player VALUES (1, 'mr.tk@pacific.ee', 'BPUYYOU62flwiWJe', 'Mr. TK', 1);
INSERT INTO player VALUES (2, 'doge@pacific.ee', '3myBuV7DKARaW14p', 'Doge', 2);
INSERT INTO player VALUES (3, 'biitnik@pacific.ee', 'D6Nua1BPg30CJ84q', 'Biitnik', 3);
INSERT INTO player VALUES (4, 'spinning@pacific.ee', 'Hur9Asc0i5ZdJrco', 'Spinning S-man', 4);
INSERT INTO player VALUES (5, 'johnnyzq@pacific.ee', 'UJ86IpW5xK8ZZH7t', 'JohnnyZQ', 5);
INSERT INTO player VALUES (6, 'kingjaan@pacific.ee', 'B4j1VKLaD1Hvj0ey', 'King Jaan', 6);

INSERT INTO unit VALUES (1, 4, 0, 0);
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
