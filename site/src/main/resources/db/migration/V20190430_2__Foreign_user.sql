ALTER TABLE `mail`
	DROP FOREIGN KEY `FK_mail_users`,
	DROP FOREIGN KEY `FK_mail_users_2`;
ALTER TABLE `mail`
	ADD CONSTRAINT `FK_mail_user` FOREIGN KEY (`from_user`) REFERENCES `user` (`user_id`),
	ADD CONSTRAINT `FK_mail_user_2` FOREIGN KEY (`to_user`) REFERENCES `user` (`user_id`);
ALTER TABLE `message`
	DROP FOREIGN KEY `FK_message_users`;
ALTER TABLE `message`
	ADD CONSTRAINT `FK_message_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);
ALTER TABLE `news`
	DROP INDEX `INDEX_users`,
	ADD INDEX `INDEX_user` (`user_id`) USING HASH,
	ADD CONSTRAINT `FK_news_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);
ALTER TABLE `privilege`
	DROP FOREIGN KEY `FK_privilege_users`;
ALTER TABLE `privilege`
	ADD CONSTRAINT `FK_privilege_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);
ALTER TABLE `solution`
	DROP FOREIGN KEY `FK_solution_users`;
ALTER TABLE `solution`
	ADD CONSTRAINT `FK_solution_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);
ALTER TABLE `user_problem`
	DROP FOREIGN KEY `FK_user_problem_users`;
ALTER TABLE `user_problem`
	ADD CONSTRAINT `FK_user_problem_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE CASCADE ON DELETE CASCADE;
