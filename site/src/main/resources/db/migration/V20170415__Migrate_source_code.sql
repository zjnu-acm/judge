CREATE TABLE IF NOT EXISTS `submission_source` (
	`solution_id` BIGINT(20) NOT NULL,
	`source` LONGTEXT NOT NULL,
	PRIMARY KEY (`solution_id`) USING HASH,
	CONSTRAINT `FK_submission_source_solution` FOREIGN KEY (`solution_id`) REFERENCES `solution` (`solution_id`)
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;
INSERT IGNORE INTO submission_source(solution_id,source) SELECT solution_id,uncompress(source) FROM source_code;
