INSERT INTO `system` (`id`, `name`, `value`, `description`) VALUES
	(1, 'working_path', 'C:\\temp', 'Judge working directory'),
	(2, 'data_files_path', 'C:\\data', 'Judge data files directory'),
	(3, 'upload_path', 'C:\\uploads', 'Files upload directory'),
	(4, 'delete_temp_file', 'true', 'Flag to detect if delete judging files. For debugging purpose only. Set true when when in production.'),
	(5, 'admin_mail', '', 'Mail of administrator'),
	(6, 'resetpassword_title', '在线评测系统找回密码', 'Title of the mail to reset password');
