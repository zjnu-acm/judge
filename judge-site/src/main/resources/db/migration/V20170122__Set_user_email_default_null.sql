ALTER TABLE `users`
	CHANGE COLUMN `email` `email` VARCHAR(255) NULL DEFAULT NULL AFTER `user_id`;
UPDATE `users`
	SET `email` = null where length(`email`) < 2;
