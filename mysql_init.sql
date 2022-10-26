DROP DATABASE IF EXISTS bookmanager;
CREATE DATABASE bookmanager;
USE bookmanager;

-- ----------------------------
-- Table structure for m_book
-- ----------------------------
DROP TABLE IF EXISTS `m_book`;
CREATE TABLE `m_book` (
  `book_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `author` VARCHAR(20) DEFAULT NULL,
  `photo` VARCHAR(200) DEFAULT NULL,
  `description` VARCHAR(200) DEFAULT NULL,

  `status` BOOL DEFAULT FALSE,
  `user_id` BIGINT(20) DEFAULT NULL,
  `deadline` VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (`book_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of m_book
-- ----------------------------
INSERT INTO `m_book` (`title`, `author`, `photo`, `description`)
VALUES('龙族', '江南', 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2060592172,4119553351&fm=26&gp=0.jpg', '龙族和混血之间的故事');

INSERT INTO `m_book` (`title`, `author`, `photo`, `description`)
VALUES('十宗罪', '蜘蛛', '十宗罪.jpg', '恐怖凶杀案');

INSERT INTO `m_book` (`title`, `author`, `photo`, `description`)
VALUES('人间失格', '太宰治', '人间失格.jpg', '主角不断寻求自杀的方式');

INSERT INTO `m_book` (`title`, `author`, `photo`, `description` )
VALUES('福尔摩斯探案集', '阿瑟·柯南·道尔', '福尔摩斯探案集.jpg', '侦探福尔摩斯解密叙述');

INSERT INTO `m_book` (`title`, `author`, `photo`, `description`)
VALUES('哈利波特与魔法石', 'J.K. Rowling', '哈利波特.jpg', '魔法的世界');

-- test
SELECT * FROM `m_book`;


-- ----------------------------
-- Table structure for m_user
-- ----------------------------
DROP TABLE IF EXISTS `m_user`;
CREATE TABLE `m_user` (
  `user_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) DEFAULT NULL,
  `password` VARCHAR(100) DEFAULT NULL,
  `admine` BOOL DEFAULT FALSE,
  `credit` INT DEFAULT 100,
  PRIMARY KEY (`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- ----------------------------
-- Records of m_user
-- ----------------------------
INSERT INTO `m_user` (`username`, `password`, `admine`)
VALUES('Yao', 'yy123456', TRUE);

INSERT INTO `m_user` (`username`, `password`, `admine`)
VALUES('Kise', 'cf123456', TRUE);

INSERT INTO `m_user` (`username`, `password`, `admine`)
VALUES('Alen', 'ed123456', TRUE);

INSERT INTO `m_user` (`username`, `password`, `admine`)
VALUES('root', 'ro123456', TRUE);

INSERT INTO `m_user` (`username`, `password`, `admine`)
VALUES('moyan', 'my123456', FALSE);

INSERT INTO `m_user` (`username`, `password`, `admine`)
VALUES('user1', 'u1123456', FALSE);

INSERT INTO `m_user` (`username`, `password`, `admine`)
VALUES('user2', 'u2123456', FALSE);

INSERT INTO `m_user` (`username`, `password`, `admine`)
VALUES('user3', 'u3123456', FALSE);

INSERT INTO `m_user` (`username`, `password`, `admine`)
VALUES('conan', 'co123456', FALSE);

INSERT INTO `m_user` (`username`, `password`, `admine`)
VALUES('Ling', 'li123456', FALSE);

-- test
SELECT * FROM `m_user`;


-- ----------------------------
-- Table structure for m_inform
-- ----------------------------
DROP TABLE IF EXISTS `m_inform`;
CREATE TABLE `m_inform` (
  `inform_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `content` LONGTEXT,
  `user_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`inform_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- ----------------------------
-- Records of m_inform
-- ----------------------------
INSERT INTO `m_inform` (`title`, `content`, `user_id`)
VALUES('Hello!!', '欢迎使用我们开发的图书管理系统！', 2);

INSERT INTO `m_inform` (`title`, `content`, `user_id`)
VALUES('Borrow!!', '可以通过使用图书管理系统进行图书借还！', 1);

INSERT INTO `m_inform` (`title`, `content`, `user_id`)
VALUES('Attention!!', '我校图书馆每周周三下午闭馆！', 1);

INSERT INTO `m_inform` (`title`, `content`, `user_id`)
VALUES('Quention!!', '有任何问题可以到借阅处前台询问工作人员！', 3);

INSERT INTO `m_inform` (`title`, `content`, `user_id`)
VALUES('Reflect!!', '可以通过反馈中心对该图书管理系统进行问题反馈，我们会第一时间给您答复！', 4);

-- test
SELECT * FROM `m_inform`;

-- ----------------------------
-- Table structure for m_problem
-- ----------------------------
DROP TABLE IF EXISTS `m_problem`;
CREATE TABLE `m_problem` (
  `problem_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `content` LONGTEXT,
  `user_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`problem_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- ----------------------------
-- Records of m_problem
-- ----------------------------
INSERT INTO `m_problem` (`title`, `content`, `user_id`)
VALUES('图书欠缺', '希望添置软件工程习题集', 5);

INSERT INTO `m_problem` (`title`, `content`, `user_id`)
VALUES('图书欠缺', '希望再购进哈利波特英文版全套', 6);

INSERT INTO `m_problem` (`title`, `content`, `user_id`)
VALUES('系统问题', '希望增加图书二维码扫描借阅', 7);

INSERT INTO `m_problem` (`title`, `content`, `user_id`)
VALUES('图书破烂', '希望可将旧图书更换', 9);

-- test
SELECT * FROM `m_problem`;


-- ----------------------------
-- Table structure for m_reply
-- ----------------------------
DROP TABLE IF EXISTS `m_reply`;
CREATE TABLE `m_reply` (
  `reply_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `content` LONGTEXT,
  `user_id` BIGINT(20) NOT NULL,
  `problem_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`reply_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- ----------------------------
-- Records of m_reply
-- ----------------------------
INSERT INTO `m_reply` (`content`, `user_id`, `problem_id`)
VALUES('感谢您的反馈，我们将会添加相应的图书采购，如有需要，您可以向我们提供相应的图书参考', 2, 1);

INSERT INTO `m_reply` (`content`, `user_id`, `problem_id`)
VALUES('感谢您的反馈，我们将会添加相应的图书采购', 1, 2);

INSERT INTO `m_reply` (`content`, `user_id`, `problem_id`)
VALUES('感谢您的反馈，我们将会尽力地实现该功能', 3, 3);

INSERT INTO `m_reply` (`content`, `user_id`, `problem_id`)
VALUES('感谢您的反馈，我们将会更换相应的图书，您也可以向我们提供相应的需要更换的图书', 2, 4);
-- test
SELECT * FROM `m_reply`;


-- ----------------------------
-- Table structure for m_tag
-- ----------------------------
DROP TABLE IF EXISTS `m_tag`;
CREATE TABLE `m_tag` (
  `tag_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(20),
  `book_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`tag_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- ----------------------------
-- Records of m_tag
-- ----------------------------
INSERT INTO `m_tag` (`title`, `book_id`)
VALUES('玄幻', 1);

INSERT INTO `m_tag` (`title`, `book_id`)
VALUES('恐怖', 2);

INSERT INTO `m_tag` (`title`, `book_id`)
VALUES('悬疑', 2);

INSERT INTO `m_tag` (`title`, `book_id`)
VALUES('人性', 3);

INSERT INTO `m_tag` (`title`, `book_id`)
VALUES('致郁', 3);

INSERT INTO `m_tag` (`title`, `book_id`)
VALUES('案件', 4);

INSERT INTO `m_tag` (`title`, `book_id`)
VALUES('悬疑', 4);

INSERT INTO `m_tag` (`title`, `book_id`)
VALUES('冒险', 5);

INSERT INTO `m_tag` (`title`, `book_id`)
VALUES('魔法', 5);

INSERT INTO `m_tag` (`title`, `book_id`)
VALUES('玄幻', 5);

-- test
SELECT * FROM `m_tag`;


-- ----------------------------
-- Table structure for m_history
-- ----------------------------
DROP TABLE IF EXISTS `m_history`;
CREATE TABLE `m_history` (
  `history_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `book_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`history_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- ----------------------------
-- Records of m_history
-- ----------------------------
INSERT INTO `m_history` (`user_id`, `book_id`)
VALUES(1, 1);

INSERT INTO `m_history` (`user_id`, `book_id`)
VALUES(1, 2);
-- test
SELECT * FROM `m_history`;
