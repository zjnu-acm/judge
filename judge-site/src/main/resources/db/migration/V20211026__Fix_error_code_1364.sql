ALTER TABLE `contest_problem`
	DROP PRIMARY KEY,
	ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT FIRST,
	ADD INDEX `UQ_contest_problem` (`contest_id`, `problem_id`) USING BTREE,
	ADD PRIMARY KEY (`id`);
