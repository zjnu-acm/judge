ALTER TABLE `contest_problem`
	DROP INDEX `UQ_contest_num`,
	ADD UNIQUE INDEX `UQ_contest_num` (`contest_id`, `num`) USING HASH;
ALTER TABLE `language`
	DROP PRIMARY KEY,
	ADD PRIMARY KEY (`id`) USING HASH;
ALTER TABLE `mail`
	DROP INDEX `INDEX_to_user`,
	ADD INDEX `INDEX_to_user` (`to_user`) USING HASH,
	DROP INDEX `INDEX_from_user`,
	ADD INDEX `INDEX_from_user` (`from_user`) USING HASH;
ALTER TABLE `message`
	DROP INDEX `FK_message_message`,
	ADD INDEX `FK_message_message` (`parent_id`) USING HASH;
ALTER TABLE `problem`
	DROP INDEX `FK_problem_contest`,
	ADD INDEX `FK_problem_contest` (`contest_id`) USING HASH;
