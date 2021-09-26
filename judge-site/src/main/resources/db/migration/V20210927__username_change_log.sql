CREATE TABLE `username_change_log` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`old` VARCHAR(50) NOT NULL,
	`new` VARCHAR(50) NOT NULL,
	`create_date` DATETIME NOT NULL DEFAULT now(),
	PRIMARY KEY (`id`)
)