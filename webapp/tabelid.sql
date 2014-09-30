CREATE TABLE `test`.`user` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ID_UNIQUE` (`id` ASC),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC));

  
  CREATE TABLE `test`.`treasure` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `latitude` FLOAT NOT NULL,
  `longitude` FLOAT NOT NULL,
  `picture_link` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ID_UNIQUE` (`id` ASC));

  
  CREATE TABLE `test`.`trek` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `treasure_id` INT UNSIGNED NOT NULL,
  `user_id` INT UNSIGNED NOT NULL,
  `start_time` TIMESTAMP NOT NULL,
  `end_time` TIMESTAMP NULL,
  `difference` INT UNSIGNED NULL,
  `score` INT UNSIGNED NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ID_UNIQUE` (`id` ASC),
  INDEX `fk_trek_treasure_idx` (`treasure_id` ASC),
  INDEX `fk_trek_user_idx` (`user_id` ASC),
  CONSTRAINT `fk_trek_treasure`
    FOREIGN KEY (`treasure_id`)
    REFERENCES `test`.`treasure` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_trek_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `test`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

	
	INSERT INTO `user` (`id`,`username`,`email`) VALUES (1,'sad','ahven');
	INSERT INTO `user` (`id`,`username`,`email`) VALUES (2,'sadafafs','ahasdaff');
	INSERT INTO `treasure` (`id`,`latitude`,`longitude`,`picture_link`) VALUES (1,55.34,24.12,'http://codepump2.herokuapp.com');
	INSERT INTO `trek` (`id`,`treasure_id`,`user_id`,`start_time`,`end_time`,`difference`,`score`) VALUES (1,1,1,'2014-09-29 23:06:05','2014-09-29 23:10:16',0,0);

