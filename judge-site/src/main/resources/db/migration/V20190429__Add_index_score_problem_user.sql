ALTER TABLE `solution`
	ADD INDEX `INDEX_spu` (`score`, `problem_id`, `user_id`) USING HASH;
