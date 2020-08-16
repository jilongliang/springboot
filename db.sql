DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户Id主键,IdWork生成',
  `user_name` varchar(255) DEFAULT '' COMMENT '用户名',
  `pass_word` varchar(255) DEFAULT '' COMMENT '密码',
  `del_flag` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除,0-不删除,1-删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `id` (`id`)USING BTREE
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


-- 创建表字段不建议用is开头，在我Kotlin+Springboot+MyBatisPlus2.x整合也提到此问题，
-- 故此整合MyBatisPlus3.x版本,把表的is_deleted字段修改成del_flag,阿里开发手册也提到此问题.
-- Kotlin+SpringBoot+MyBatisPlus完美搭建最简洁最酷的前后端分离框架
-- https://www.jianshu.com/p/0acd593fd11e