CREATE TABLE `submission_detail` (
	`submission_id` BIGINT NOT NULL,
	`source` LONGTEXT NULL DEFAULT NULL,
	`compile_info` LONGTEXT NULL DEFAULT NULL,
	`detail` LONGTEXT NULL DEFAULT NULL,
	`system_info` LONGTEXT NULL DEFAULT NULL,
	PRIMARY KEY (`submission_id`)
);
ALTER TABLE `submission_detail`
	ADD CONSTRAINT `FK_submission_detail_solution` FOREIGN KEY (`submission_id`) REFERENCES `solution` (`solution_id`);
