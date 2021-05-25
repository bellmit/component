CREATE TABLE `scenario_item`
(
    `id`              int(11) unsigned               NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `scenario_type`   varchar(100)                   NOT NULL DEFAULT '' COMMENT '场景类型（如：活动）',
    `scenario_id`     varchar(50)                    NOT NULL DEFAULT '' COMMENT '场景id（如活动ID为：1）',
    `item_type`       varchar(100)                   NOT NULL DEFAULT '' COMMENT '项类型（如：抽奖活动，签到活动）',
    `item_key`        varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '项key（如：默认抽奖次数、文字规则、转盘图片）',
    `item_value`      text CHARACTER SET utf8 COMMENT '项值（如：1次、图片url）',
    `item_value_type` varchar(100)                            DEFAULT '' COMMENT '项值类型（Integer、String）',
    `description`     varchar(511)                            DEFAULT NULL COMMENT '描述',
    `required`        tinyint(1)                     NOT NULL DEFAULT '0' COMMENT '是否必须（可以根据这个做验证）',
    `creator`         varchar(100)                            DEFAULT NULL COMMENT '创建人',
    `modifier`        varchar(100)                            DEFAULT NULL COMMENT '修改人',
    `created_time`    datetime                                DEFAULT NULL COMMENT '更新时间',
    `updated_time`    datetime                                DEFAULT NULL COMMENT '更新时间',
    `deleted`         tinyint(1)                     NOT NULL DEFAULT '0' COMMENT '是否删除：0未删除，1已删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_type_id_type_key` (`scenario_type`, `scenario_id`, `item_type`, `item_key`),
    KEY `idx_scenario_type_id` (`scenario_type`, `scenario_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='通用场景值配置';