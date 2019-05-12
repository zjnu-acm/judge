ALTER TABLE `system`
	DROP INDEX `name`,
	ADD UNIQUE INDEX `UQ_name` (`name`);
