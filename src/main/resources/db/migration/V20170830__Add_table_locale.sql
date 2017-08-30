CREATE TABLE `locale` (
  `_id` int(11) NOT NULL AUTO_INCREMENT,
  `id` varchar(10) NOT NULL,
  `name` varchar(20) NOT NULL,
  `description` varchar(10) NOT NULL DEFAULT '',
  `disabled` bit(1) NOT NULL DEFAULT b'0',
  `created_time` datetime NOT NULL DEFAULT current_timestamp(),
  `modified_time` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`_id`),
  UNIQUE KEY `UQ_ID` (`id`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4
