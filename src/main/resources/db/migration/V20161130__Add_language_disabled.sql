ALTER TABLE `language`
	ADD COLUMN `disabled` BIT NOT NULL DEFAULT b'0' AFTER `description`;
