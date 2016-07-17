-- --------------------------------------------------------
-- Host:                         localhost
-- Server version:               10.1.13-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             9.3.0.5077
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;



-- Dumping structure for table compileinfo
CREATE TABLE IF NOT EXISTS `compileinfo` (
  `solution_id` bigint(20) NOT NULL,
  `error` longtext NOT NULL,
  PRIMARY KEY (`solution_id`) USING HASH,
  CONSTRAINT `FK_compileinfo_solution` FOREIGN KEY (`solution_id`) REFERENCES `solution` (`solution_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table contest
CREATE TABLE IF NOT EXISTS `contest` (
  `contest_id` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `description` longtext NOT NULL,
  `private` int(11) DEFAULT NULL,
  `disabled` bit(1) NOT NULL DEFAULT b'0' COMMENT 'disabled表示该比赛是否被禁用',
  PRIMARY KEY (`contest_id`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table contest_problem
CREATE TABLE IF NOT EXISTS `contest_problem` (
  `contest_id` bigint(20) NOT NULL,
  `problem_id` bigint(20) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `num` int(11) NOT NULL,
  PRIMARY KEY (`contest_id`,`problem_id`) USING HASH,
  UNIQUE KEY `UQ_contest_num` (`contest_id`,`num`),
  KEY `INDEX_problem` (`problem_id`) USING HASH,
  CONSTRAINT `FK_contest_problem_contest` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`contest_id`),
  CONSTRAINT `FK_contest_problem_problem` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`problem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='比赛的题目。';

-- Data exporting was unselected.
-- Dumping structure for table language
CREATE TABLE IF NOT EXISTS `language` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `source_extension` varchar(20) NOT NULL,
  `compile_command` longtext,
  `execute_command` longtext,
  `executable_extension` longtext NOT NULL,
  `time_factor` bigint(20) NOT NULL,
  `ext_memory` bigint(20) NOT NULL,
  `description` longtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table loginlog
CREATE TABLE IF NOT EXISTS `loginlog` (
  `id` bigint(20) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `password` varchar(40) NOT NULL,
  `ip` varchar(100) NOT NULL,
  `time` datetime NOT NULL,
  `success` bit(1) NOT NULL DEFAULT b'0' COMMENT '表示登录是否成功',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='由于历史原因2016/3/21及以前的success均为1，实际登陆是否成功未知。';

-- Data exporting was unselected.
-- Dumping structure for table mail
CREATE TABLE IF NOT EXISTS `mail` (
  `mail_id` bigint(20) NOT NULL,
  `from_user` varchar(20) NOT NULL,
  `to_user` varchar(20) NOT NULL,
  `title` varchar(200) NOT NULL DEFAULT '',
  `content` mediumtext NOT NULL,
  `new_mail` bit(1) NOT NULL DEFAULT b'1',
  `reply` bit(1) NOT NULL DEFAULT b'0',
  `in_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`mail_id`),
  KEY `INDEX_to_user` (`to_user`),
  KEY `INDEX_from_user` (`from_user`),
  CONSTRAINT `FK_mail_users` FOREIGN KEY (`from_user`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_mail_users_2` FOREIGN KEY (`to_user`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='站内信';

-- Data exporting was unselected.
-- Dumping structure for table message
CREATE TABLE IF NOT EXISTS `message` (
  `message_id` bigint(20) NOT NULL,
  `problem_id` bigint(20) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `thread_id` bigint(20) NOT NULL DEFAULT '0',
  `depth` int(11) NOT NULL DEFAULT '0',
  `orderNum` int(11) NOT NULL DEFAULT '0',
  `user_id` varchar(20) NOT NULL,
  `title` varchar(200) NOT NULL,
  `content` longtext NOT NULL,
  `in_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`message_id`),
  KEY `INDEX_user` (`user_id`) USING HASH,
  KEY `INDEX_problem` (`problem_id`) USING HASH,
  KEY `FK_message_message` (`parent_id`),
  CONSTRAINT `FK_message_message` FOREIGN KEY (`parent_id`) REFERENCES `message` (`message_id`),
  CONSTRAINT `FK_message_problem` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`problem_id`),
  CONSTRAINT `FK_message_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='讨论版';

-- Data exporting was unselected.
-- Dumping structure for table news
CREATE TABLE IF NOT EXISTS `news` (
  `news_id` bigint(20) NOT NULL,
  `user_id` varchar(20) NOT NULL,
  `title` varchar(200) NOT NULL DEFAULT '',
  `content` longtext NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `importance` tinyint(4) NOT NULL DEFAULT '0',
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`news_id`),
  KEY `INDEX_users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table persistent_logins
CREATE TABLE IF NOT EXISTS `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`) USING HASH,
  KEY `Index_username` (`username`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table privilege
CREATE TABLE IF NOT EXISTS `privilege` (
  `user_id` varchar(20) NOT NULL,
  `rightstr` enum('administrator','source_browser','news_publisher') NOT NULL DEFAULT 'news_publisher' COMMENT 'news_publisher没有用到',
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`user_id`) USING HASH,
  CONSTRAINT `FK_privilege_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table problem
CREATE TABLE IF NOT EXISTS `problem` (
  `problem_id` bigint(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` longtext NOT NULL,
  `input` longtext NOT NULL,
  `output` longtext NOT NULL,
  `sample_input` longtext NOT NULL,
  `sample_output` longtext NOT NULL,
  `hint` longtext NOT NULL,
  `source` varchar(255) NOT NULL,
  `in_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_limit` int(11) NOT NULL,
  `memory_limit` int(11) NOT NULL,
  `contest_id` bigint(20) DEFAULT NULL,
  `accepted` bigint(20) NOT NULL DEFAULT '0',
  `submit` bigint(20) NOT NULL DEFAULT '0',
  `solved` bigint(20) NOT NULL DEFAULT '0',
  `submit_user` bigint(20) NOT NULL DEFAULT '0',
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`problem_id`),
  KEY `FK_problem_contest` (`contest_id`),
  CONSTRAINT `FK_problem_contest` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`contest_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table problem_i18n
CREATE TABLE IF NOT EXISTS `problem_i18n` (
  `id` bigint(20) NOT NULL,
  `locale` enum('en','zh') NOT NULL DEFAULT 'zh',
  `title` longtext,
  `description` longtext,
  `input` longtext,
  `output` longtext,
  `hint` longtext,
  `source` longtext,
  PRIMARY KEY (`id`,`locale`) USING HASH,
  CONSTRAINT `FK_problem` FOREIGN KEY (`id`) REFERENCES `problem` (`problem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table solution
CREATE TABLE IF NOT EXISTS `solution` (
  `solution_id` bigint(20) NOT NULL,
  `problem_id` bigint(20) NOT NULL,
  `user_id` varchar(20) NOT NULL,
  `time` bigint(20) NOT NULL DEFAULT '0',
  `memory` bigint(20) NOT NULL DEFAULT '0',
  `in_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `score` smallint(6) NOT NULL DEFAULT '0',
  `language` tinyint(4) NOT NULL,
  `ip` varchar(39) NOT NULL DEFAULT '',
  `contest_id` bigint(20) DEFAULT NULL,
  `num` tinyint(4) NOT NULL DEFAULT '-1',
  `code_length` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`solution_id`),
  KEY `INDEX_score` (`score`) USING HASH,
  KEY `INDEX_user` (`user_id`) USING HASH,
  KEY `INDEX_problem` (`problem_id`) USING HASH,
  KEY `INDEX_contest` (`contest_id`) USING HASH,
  KEY `Index time_memory` (`time`,`memory`),
  CONSTRAINT `FK_solution_contest` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`contest_id`),
  CONSTRAINT `FK_solution_problem` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`problem_id`),
  CONSTRAINT `FK_solution_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table solution_details
CREATE TABLE IF NOT EXISTS `solution_details` (
  `solution_id` bigint(20) NOT NULL,
  `details` longtext NOT NULL,
  PRIMARY KEY (`solution_id`) USING HASH,
  CONSTRAINT `FK_solution_details_solution` FOREIGN KEY (`solution_id`) REFERENCES `solution` (`solution_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table source_code
CREATE TABLE IF NOT EXISTS `source_code` (
  `solution_id` bigint(20) NOT NULL,
  `source` blob NOT NULL,
  PRIMARY KEY (`solution_id`) USING HASH,
  CONSTRAINT `FK_source_code_solution` FOREIGN KEY (`solution_id`) REFERENCES `solution` (`solution_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table users
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` varchar(20) NOT NULL,
  `email` varchar(255) NOT NULL DEFAULT '',
  `ip` varchar(39) DEFAULT NULL,
  `accesstime` datetime DEFAULT NULL,
  `volume` int(11) NOT NULL DEFAULT '1',
  `language` tinyint(4) NOT NULL DEFAULT '1',
  `password` varchar(100) NOT NULL,
  `reg_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `nick` varchar(255) NOT NULL DEFAULT '',
  `school` varchar(255) NOT NULL DEFAULT '',
  `style` tinyint(4) NOT NULL DEFAULT '41',
  `vcode` varchar(50) DEFAULT NULL,
  `accepted` bigint(20) NOT NULL DEFAULT '0',
  `submit` bigint(20) NOT NULL DEFAULT '0',
  `solved` bigint(20) NOT NULL DEFAULT '0',
  `submit_problem` bigint(20) NOT NULL DEFAULT '0',
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`user_id`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.
-- Dumping structure for table user_problem
CREATE TABLE IF NOT EXISTS `user_problem` (
  `user_id` varchar(20) NOT NULL,
  `problem_id` bigint(20) NOT NULL,
  `accepted` bigint(20) NOT NULL DEFAULT '0',
  `submit` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`,`problem_id`) USING HASH,
  KEY `INDEX_PROBLEM` (`problem_id`) USING HASH,
  CONSTRAINT `FK_user_problem_problem` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`problem_id`),
  CONSTRAINT `FK_user_problem_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缓存表，记录用户提交题目的信息。';

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
