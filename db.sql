DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户Id主键,IdWork生成',
  `user_name` varchar(255) DEFAULT '' COMMENT '用户名',
  `pass_word` varchar(255) DEFAULT '' COMMENT '密码',
  `is_deleted` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除,0-不删除,1-删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `id` (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';



--  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',是mysql5.6以上的版本