ALTER TABLE `system`
	CHANGE COLUMN `value` `value` TEXT NOT NULL AFTER `name`;

insert into system(name,value,description)values('page_index','Sample page index','The content of page index')
