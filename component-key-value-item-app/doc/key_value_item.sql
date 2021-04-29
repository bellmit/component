/*
 Navicat MySQL Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : 127.0.0.1:3306
 Source Schema         : mybatisplus

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 23/04/2021 18:24:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for key_value_item
-- ----------------------------
DROP TABLE IF EXISTS `key_value_item`;
CREATE TABLE `key_value_item`
(
    `id`           int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `item_name`    varchar(100)     NOT NULL DEFAULT '' COMMENT '项名称',
    `item_key`     varchar(50)      NOT NULL DEFAULT '' COMMENT '项key',
    `item_value`   text             NULL COMMENT '项值',
    `memo`         varchar(511)     NULL     DEFAULT NULL COMMENT '备注',
    `creator`      varchar(100)     NULL     DEFAULT NULL COMMENT '创建人',
    `modifier`     varchar(100)     NULL     DEFAULT NULL COMMENT '修改人',
    `deleted`      int(1)           NOT NULL DEFAULT 0 COMMENT '是否删除：0未删除，1已删除',
    `created_time` datetime(0)      NULL     DEFAULT NULL COMMENT '更新时间',
    `updated_time` datetime(0)      NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_name_key` (`item_name`, `item_key`) USING BTREE COMMENT '名称和键组成的唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT = '键值项'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of key_value_item
-- ----------------------------
INSERT INTO `key_value_item`
VALUES (1, 'defaultPoster', 'url', 'http://cdn.lyloou.com/a.jpg', '描述1', NULL, 'me', 0, '2021-04-16 17:49:26',
        '2021-04-16 19:28:12');
INSERT INTO `key_value_item`
VALUES (4, 'defaultPoster', 'image', 'http://cdn.lyloou.com/a.jpg', '描述', NULL, 'me', 0, '2021-04-16 19:10:35',
        '2021-04-16 19:10:35');

SET FOREIGN_KEY_CHECKS = 1;
