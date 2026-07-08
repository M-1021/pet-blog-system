-- ============================================================
-- 宠物博客系统 - 完整数据库初始化脚本（合并版）
-- 合并了 init.sql + alter_add_view_count.sql
-- 数据库：MySQL 5.7+ / 8.0
-- 编码：UTF-8mb4
-- 使用方法：mysql -u root -p < pet_blog_init.sql
-- ============================================================

-- ============================================================
-- 宠物博客系统 - 数据库初始化脚本
-- 数据库：MySQL 5.7+ / 8.0
-- 编码：UTF-8mb4
-- ============================================================

-- 创建数据库（如不存在）
CREATE DATABASE IF NOT EXISTS pet_blog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pet_blog;

-- ============================================================
-- 1. 用户表
-- ============================================================
DROP TABLE IF EXISTS `report`;
DROP TABLE IF EXISTS `reply`;
DROP TABLE IF EXISTS `post`;
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `article_like`;
DROP TABLE IF EXISTS `article`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `username`    VARCHAR(50)  NOT NULL,
    `password`    VARCHAR(100) NOT NULL,
    `nickname`    VARCHAR(50)  DEFAULT NULL,
    `avatar`      VARCHAR(255) DEFAULT NULL,
    `email`       VARCHAR(100) DEFAULT NULL,
    `phone`       VARCHAR(20)  DEFAULT NULL,
    `bio`         VARCHAR(500) DEFAULT NULL,
    `role`        TINYINT      NOT NULL DEFAULT 0 COMMENT '0-普通用户 1-管理员',
    `deleted`     TINYINT      NOT NULL DEFAULT 0,
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 2. 文章分类表
-- ============================================================
CREATE TABLE `category` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(50)  NOT NULL,
    `description` VARCHAR(200) DEFAULT NULL,
    `sort_order`  INT          NOT NULL DEFAULT 0,
    `deleted`     TINYINT      NOT NULL DEFAULT 0,
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 3. 宠物知识文章表
-- ============================================================
CREATE TABLE `article` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `title`         VARCHAR(200) NOT NULL,
    `content`       TEXT         NOT NULL,
    `cover_image`   VARCHAR(255) DEFAULT NULL,
    `category_id`   BIGINT       DEFAULT NULL,
    `user_id`       BIGINT       NOT NULL,
    `like_count`    INT          NOT NULL DEFAULT 0,
    `comment_count` INT          NOT NULL DEFAULT 0,
    `view_count`    INT          NOT NULL DEFAULT 0 COMMENT '浏览量',
    `status`        TINYINT      NOT NULL DEFAULT 0 COMMENT '0-正常 1-违规下架',
    `deleted`       TINYINT      NOT NULL DEFAULT 0,
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 4. 文章评论表
-- ============================================================
CREATE TABLE `comment` (
    `id`          BIGINT        NOT NULL AUTO_INCREMENT,
    `content`     VARCHAR(1000) NOT NULL,
    `article_id`  BIGINT        NOT NULL,
    `user_id`     BIGINT        NOT NULL,
    `parent_id`   BIGINT        DEFAULT NULL COMMENT '父评论ID，NULL为顶级评论',
    `deleted`     TINYINT       NOT NULL DEFAULT 0,
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 5. 文章点赞表（同一用户只能点赞一次）
-- ============================================================
CREATE TABLE `article_like` (
    `id`          BIGINT   NOT NULL AUTO_INCREMENT,
    `article_id`  BIGINT   NOT NULL,
    `user_id`     BIGINT   NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_article_user` (`article_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 6. 在线交流帖子表
-- ============================================================
CREATE TABLE `post` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `title`       VARCHAR(200) NOT NULL,
    `content`     TEXT         NOT NULL,
    `user_id`     BIGINT       NOT NULL,
    `reply_count` INT          NOT NULL DEFAULT 0,
    `deleted`     TINYINT      NOT NULL DEFAULT 0,
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 7. 帖子回复表
-- ============================================================
CREATE TABLE `reply` (
    `id`          BIGINT        NOT NULL AUTO_INCREMENT,
    `content`     VARCHAR(1000) NOT NULL,
    `post_id`     BIGINT        NOT NULL,
    `user_id`     BIGINT        NOT NULL,
    `deleted`     TINYINT       NOT NULL DEFAULT 0,
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_post_id` (`post_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 8. 举报表
-- ============================================================
CREATE TABLE `report` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `article_id`  BIGINT       NOT NULL,
    `user_id`     BIGINT       NOT NULL,
    `reason`      VARCHAR(500) NOT NULL,
    `status`      TINYINT      NOT NULL DEFAULT 0 COMMENT '0-待处理 1-已处理',
    `result`      VARCHAR(500) DEFAULT NULL COMMENT '处理结果',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `handle_time` DATETIME     DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 初始测试数据
-- ============================================================

-- 管理员账号 admin / admin123
INSERT INTO `user` (`username`, `password`, `nickname`, `role`) VALUES
('admin', '0192023a7bbd73250516f069df18b500', '系统管理员', 1);

-- 测试用户 testuser / 123456
INSERT INTO `user` (`username`, `password`, `nickname`, `role`) VALUES
('testuser', 'e10adc3949ba59abbe56e057f20f883e', '宠物爱好者', 0);

-- 文章分类
INSERT INTO `category` (`name`, `description`, `sort_order`) VALUES
('养猫指南', '关于猫咪饲养、护理、健康的知识', 1),
('养狗指南', '关于狗狗饲养、训练、健康的知识', 2),
('宠物健康', '宠物疾病预防、治疗、保健知识', 3),
('宠物饮食', '宠物营养搭配、食物选择建议', 4),
('宠物趣事', '分享与宠物相处的有趣故事', 5);

-- 测试文章
INSERT INTO `article` (`title`, `content`, `category_id`, `user_id`, `like_count`, `view_count`) VALUES
('新手养猫必看：猫咪到家第一周该做什么', '当你迎来一只新猫咪时，第一周非常关键。首先要为猫咪准备一个安静的房间，放置好猫砂盆、水碗和食物碗。让猫咪自己探索新环境，不要强迫互动。', 1, 1, 15, 120),
('狗狗日常训练的五个基础指令', '训练狗狗是每个铲屎官的必修课。坐下（Sit）、趴下（Down）、等待（Stay）、过来（Come）、松口（Drop it）是最基础的五个指令。', 2, 2, 23, 256),
('夏季宠物防暑降温小妙招', '夏天到了，宠物也需要防暑降温。确保宠物随时有充足的清洁饮水，避免在中午最热的时候遛狗。', 3, 1, 8, 89);