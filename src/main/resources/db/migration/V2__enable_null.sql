ALTER TABLE `problem_i18n`
	CHANGE COLUMN `title` `title` LONGTEXT NULL AFTER `locale`,
	CHANGE COLUMN `description` `description` LONGTEXT NULL AFTER `title`,
	CHANGE COLUMN `input` `input` LONGTEXT NULL AFTER `description`,
	CHANGE COLUMN `output` `output` LONGTEXT NULL AFTER `input`,
	CHANGE COLUMN `hint` `hint` LONGTEXT NULL AFTER `output`,
	CHANGE COLUMN `source` `source` LONGTEXT NULL AFTER `hint`;
ALTER TABLE `contest_problem`
	ALTER `title` DROP DEFAULT;
ALTER TABLE `contest_problem`
	CHANGE COLUMN `title` `title` VARCHAR(255) NULL COLLATE 'utf8mb4_unicode_ci' AFTER `problem_id`;
