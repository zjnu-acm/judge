ALTER TABLE `message`
	ALTER `orderNum` DROP DEFAULT;
ALTER TABLE `message`
	CHANGE COLUMN `orderNum` `order_num` INT NOT NULL AFTER `depth`;
