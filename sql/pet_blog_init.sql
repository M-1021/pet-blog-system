-- ============================================================
-- 宠物博客系统 - 完整数据库初始化脚本
-- 包含：建表 + 基础数据 + 小红书演示数据（本地封面图）
-- 使用方法：mysql -u root -p < pet_blog_init.sql
-- 图片资源：运行 setup-demo.ps1 部署 demo-assets/image/ 到 uploads/
-- ============================================================

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

-- ============================================================
-- 基础用户数据
-- ============================================================
INSERT INTO `user` (`username`, `password`, `nickname`, `role`) VALUES
('admin', '0192023a7bbd73250516f069df18b500', '系统管理员', 1),
('testuser', 'e10adc3949ba59abbe56e057f20f883e', '宠物爱好者', 0);

-- ============================================================
-- 文章分类数据
-- ============================================================
INSERT INTO `category` (`name`, `description`, `sort_order`) VALUES
('养猫指南', '关于猫咪饲养、护理、健康的知识', 1),
('养狗指南', '关于狗狗饲养、训练、健康的知识', 2),
('宠物健康', '宠物疾病预防、治疗、保健知识', 3),
('宠物饮食', '宠物营养搭配、食物选择建议', 4),
('宠物趣事', '分享与宠物相处的有趣故事', 5);

-- ============================================================
-- 小红书演示用户
-- ============================================================
INSERT INTO `user` (`username`, `password`, `nickname`, `avatar`, `role`) VALUES
('xhs_cat1', 'e10adc3949ba59abbe56e057f20f883e', '猫咪小达人', 'https://api.dicebear.com/7.x/miniavs/svg?seed=cat1', 0),
('xhs_cat2', 'e10adc3949ba59abbe56e057f20f883e', '布偶控', 'https://api.dicebear.com/7.x/miniavs/svg?seed=cat2', 0),
('xhs_dog1', 'e10adc3949ba59abbe56e057f20f883e', '遛狗日记', 'https://api.dicebear.com/7.x/miniavs/svg?seed=dog1', 0),
('xhs_dog2', 'e10adc3949ba59abbe56e057f20f883e', '柴犬茉莉', 'https://api.dicebear.com/7.x/miniavs/svg?seed=dog2', 0),
('xhs_health', 'e10adc3949ba59abbe56e057f20f883e', '宠物医生在线', 'https://api.dicebear.com/7.x/miniavs/svg?seed=health', 0);

SET @xhs_user1 = (SELECT id FROM user WHERE username='xhs_cat1' LIMIT 1);
SET @xhs_user2 = (SELECT id FROM user WHERE username='xhs_cat2' LIMIT 1);
SET @xhs_user3 = (SELECT id FROM user WHERE username='xhs_dog1' LIMIT 1);
SET @xhs_user4 = (SELECT id FROM user WHERE username='xhs_dog2' LIMIT 1);
SET @xhs_user5 = (SELECT id FROM user WHERE username='xhs_health' LIMIT 1);

-- ============================================================
-- 小红书演示文章（封面图指向 demo-assets/image/）
-- ============================================================
INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('她好像知道自己很可爱', '她好像知道自己很可爱

养猫之后才发现，原来生活中最简单的快乐就是看着猫咪打滚、伸懒腰、踩奶。它们真的是世界上最可爱的生物！#米努特矮脚猫 #我家宠物好可爱 #猫咪日常 #她好像知道自己很可爱 #和动物们贴贴的一天', '/uploads/image/cat_daily_1_00_27b46933a5.jpg', 1, @xhs_user2, 19, 4, 1084, 0, DATE_SUB(NOW(), INTERVAL 6 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('世界上最最最可爱的宝宝！！', 'This is my cattax！Her name is Molly~Nice to meet u!', '/uploads/image/cat_daily_2_00_db4c28e8e4.jpg', 1, @xhs_user1, 121, 16, 339, 0, DATE_SUB(NOW(), INTERVAL 13 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('咪：我身轻如燕！如燕！如燕！如燕！', '咪：我身轻如燕！如燕！如燕！如燕！

今天又是被猫咪治愈的一天！每次看到毛茸茸的小家伙在家里蹦来蹦去，所有的烦恼都烟消云散了。#猫咪日常 #猫咪迷惑行为 #猫咪', '/uploads/image/cat_photo_6_06_ed9a6f5f86.jpg', 1, @xhs_user2, 255, 14, 2960, 0, DATE_SUB(NOW(), INTERVAL 10 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('这就是live图的意义吧！', '这就是live图的意义吧！

分享一下我家猫咪的日常~每天下班回家打开门，看到它在门口等我，就觉得一天的疲惫都值了。#神金小猫 #猫咪日常 #live图 #这才是live图的意义 #审美积累', '/uploads/image/cat_daily_4_00_4f05e76791.jpg', 1, @xhs_user1, 23, 18, 1885, 0, DATE_SUB(NOW(), INTERVAL 7 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('可爱的小猫图片', '可爱的小猫图片

有人说，养猫的人手机里90%%都是猫咪的照片。我想说，那剩下的10%%是还没来得及拍的。#可爱小猫 #萌宠头像 #可爱猫咪 #甜甜的小猫咪 #猫咪背景图', '/uploads/image/cat_photo_5_00_b403d05707.jpg', 1, @xhs_user2, 121, 22, 938, 0, DATE_SUB(NOW(), INTERVAL 5 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('一些可爱的猫咪图片～', '一些可爱的猫咪图片～

猫咪的每一个瞬间都值得记录！无论是打哈欠时露出的小尖牙，还是阳光下眯着眼睛打盹的样子，都让人心都化了。#小猫也有人生照片 #会微笑的猫咪 #猫猫真的可以治愈一切 #这么可爱的小猫咪谁不爱呢', '/uploads/image/cat_photo_6_00_a6434161bf.jpg', 1, @xhs_user2, 103, 24, 1147, 0, DATE_SUB(NOW(), INTERVAL 9 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('💫 我宣布，这是我见过最会拿捏氛围感的布偶', '是谁家的神仙小猫咪，颜值直接杀疯了！😭 这只布偶猫真的长在了我的心巴上！蓬松的毛发像云朵一样柔软，蓝眼睛像盛满了星辰大海，自带温柔滤镜。不管是乖乖窝在沙发上，还是端庄地坐在猫抓板上，一举一动都像优雅的公主。', '/uploads/image/cat_photo_5_05_c587706d46.jpg', 1, @xhs_user2, 26, 20, 298, 0, DATE_SUB(NOW(), INTERVAL 7 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('布偶猫的美貌瞬间', '布偶猫的美貌瞬间

今天拍到了一组超满意的猫咪照片！光影、构图、表情都刚刚好，忍不住要分享给大家。#布偶猫爆毛 #布偶猫 #传统系布偶', '/uploads/image/cat_daily_4_03_1463ebf5b8.jpg', 1, @xhs_user1, 130, 4, 689, 0, DATE_SUB(NOW(), INTERVAL 4 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('过来人养猫经验！！！新手要多看看', '过来人养猫经验！！！新手要多看看

很多新手铲屎官不知道从何入手，这里整理了一份养猫必备清单和注意事项，希望对大家有帮助。#猫咪好物分享 #养猫人的幸福 #养猫经验分享 #新手养猫 #第一次养猫', '/uploads/image/cat_daily_1_01_4598ecc895.jpg', 1, @xhs_user1, 197, 17, 1812, 0, DATE_SUB(NOW(), INTERVAL 3 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('第一次养猫教程（平民版）', '养猫前：1. 建议养能在猫妈妈身边长大1-2个月的小猫，在猫妈妈身边长大的小猫是喝母乳长大的，免疫力强很多且猫妈妈会教小猫上厕所，不用担心回家后乱拉乱尿的问题。2.品种真的不重要，如果有眼缘有缘分，用爱去养 每只毛孩子都会被你养成你想要的猫。养猫后：1.【关于疫苗】3联疫苗必打，美团团购一百左右包3针，后续每年只需打一针就行。2.【关于驱虫】实在无需在宠物医院打，费用不低，网上能买药的平台买内驱外驱药只需十几块人民币。3.【关于耳螨】同上，耳螨药十几块网上都有得买。4.【关于饮食】小猫主张买贵的幼猫粮，但不需要买零食。担心小猫饮食单一可以喂些蛋黄，虾仁，鸡胸肉。5.【关于娱乐】一根逗猫棒+一支激光笔就够。', '/uploads/image/cat_photo_5_03_6bdd41f1f7.jpg', 1, @xhs_user2, 83, 11, 565, 0, DATE_SUB(NOW(), INTERVAL 30 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('今天吃 小狗版钵钵鸡🍲', '是很有仪式感的一餐呢hh 汤底是用红甜彩椒做的，没有辣味喔', '/uploads/image/dog_photo_15_12_c48b3476a0.jpg', 2, @xhs_user4, 62, 5, 1661, 0, DATE_SUB(NOW(), INTERVAL 8 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('生活在宠物友好城市的小狗', '生活在宠物友好城市的小狗

养狗的快乐就是这么简单！每天遛狗的时候，看它在草地上撒欢奔跑，自己也跟着开心起来。#狗狗日常 #小宠物大导演 #小狗城市漫游 #狗狗 #vlog #宠物友好', '/uploads/image/dog_photo_15_01_5886f28346.jpg', 2, @xhs_user4, 51, 5, 1977, 0, DATE_SUB(NOW(), INTERVAL 11 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('养狗后 经常跟小狗说的一句话是….？', '养狗后 经常跟小狗说的一句话是….？

记录一下今天和狗狗的快乐时光~它学会了一个新技能，虽然过程有点搞笑，但结果很让人骄傲！#发射小人 #我家宠物好可爱 #狗 #狗狗 #狗狗日常 #养狗经验分享 #小狗', '/uploads/image/dog_photo_15_13_091a5fc6eb.jpg', 2, @xhs_user4, 202, 7, 1280, 0, DATE_SUB(NOW(), INTERVAL 17 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('小狗日记⭕3', '今天也是爹爹的乖乖小狗~ 爸爸的狗狗 不要在我评论区里找啊，否则发生了什么我不负责的哈', '/uploads/image/dog_photo_15_14_9e6505a51e.jpg', 2, @xhs_user4, 235, 13, 1160, 0, DATE_SUB(NOW(), INTERVAL 26 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('🍁秋天雷好哇🌧ᴿᴬᴵᴺʸᴰᴬʸॱଳ͘', '🍁秋天雷好哇🌧ᴿᴬᴵᴺʸᴰᴬʸॱଳ͘

狗狗的颜值也是可以很能打的！今天这组照片拍出了杂志大片的感觉，毛发在阳光下发着光，太好看了。#马尔泰 #萌宠成长记 #可爱狗狗 #狗狗日常 #小狗给的治愈瞬间 #萌宠日常', '/uploads/image/dog_photo_15_00_88873687de.jpg', 2, @xhs_user4, 78, 10, 2043, 0, DATE_SUB(NOW(), INTERVAL 16 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('耶牌苹果垫脑（🍎长毛版）', '耶牌苹果垫脑（🍎长毛版）

狗狗的颜值也是可以很能打的！今天这组照片拍出了杂志大片的感觉，毛发在阳光下发着光，太好看了。#萨摩耶 #我家宠物好可爱 #苹果垫脑 #蓝天 #壁纸 #苹果', '/uploads/image/dog_photo_16_00_ecca74975f.jpg', 2, @xhs_user3, 230, 24, 890, 0, DATE_SUB(NOW(), INTERVAL 16 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('兜兜和kiki是最好的朋友', '兜兜和kiki是最好的朋友

分享一组狗狗的治愈系照片，每一张都能让人嘴角上扬。养狗的日子里，处处都是美好。', '/uploads/image/dog_photo_15_08_adb31682e3.jpg', 2, @xhs_user3, 222, 19, 2525, 0, DATE_SUB(NOW(), INTERVAL 5 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('夕阳照着我的小茉莉', '夕阳照着我的小茉莉

狗狗的颜值也是可以很能打的！今天这组照片拍出了杂志大片的感觉，毛发在阳光下发着光，太好看了。#柴犬 #狗狗日常 #我家宠物好可爱 #小狗日记 #柴犬日常 #live图', '/uploads/image/dog_photo_15_07_69af59e1d0.jpg', 2, @xhs_user4, 183, 16, 2190, 0, DATE_SUB(NOW(), INTERVAL 7 DAY));

INSERT INTO `article` (`title`, `content`, `cover_image`, `category_id`, `user_id`, `like_count`, `comment_count`, `view_count`, `status`, `create_time`) VALUES
('宠医在线，速回👍', '宠医在线，速回👍

作为宠物主人，了解基本的宠物健康知识非常重要。今天分享一些实用的宠物护理技巧，帮助大家更好地照顾毛孩子。#宠物医生在线问诊 #猫瘟 #宠物医生 #猫传腹 #猫咪呕吐 #新手养宠 #宠物医院 #兽医 #流浪猫救助', '/uploads/image/cat_daily_2_06_ca6aba2b7a.jpg', 3, @xhs_user5, 155, 20, 3418, 0, DATE_SUB(NOW(), INTERVAL 11 DAY));

SELECT CONCAT('初始化完成，共 ', COUNT(*), ' 篇文章') AS result FROM article WHERE deleted=0;