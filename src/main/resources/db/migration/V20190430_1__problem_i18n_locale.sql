ALTER TABLE `problem_i18n`
	ALTER `locale` DROP DEFAULT;
ALTER TABLE `problem_i18n`
	CHANGE COLUMN `locale` `locale` VARCHAR(10) NOT NULL AFTER `id`,
	ADD CONSTRAINT `FK_problem_i18n_locale` FOREIGN KEY (`locale`) REFERENCES `locale` (`id`);
