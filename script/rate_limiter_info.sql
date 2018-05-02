CREATE TABLE rate_limiter_info(
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
	`name` VARCHAR(200) COMMENT '令牌桶的唯一标示',
	`apps` VARCHAR(200) COMMENT '能够使用令牌桶的应用列表',
	`max_permits` INT COMMENT '令牌桶的最大令牌数',
	`rate` VARCHAR(200) COMMENT '向令牌桶中添加令牌的速率',
	`create_user` VARCHAR(100) COMMENT '创建者',
	`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_user` VARCHAR(100) COMMENT '创建者',
	`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY(id),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
