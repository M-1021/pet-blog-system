USE pet_blog;
ALTER TABLE `article` ADD COLUMN `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览量' AFTER `comment_count`;