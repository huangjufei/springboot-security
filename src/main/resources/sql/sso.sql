

1,创建表时,字段不要预先想得太多字段,只要核心字段;可以考虑关联子表来对付以后的业务增加
2,字段的长度尽量的小,不一定每个表的相同字段的长度硬要一致如description这样的字段
3,先建主表,在建中间表,尽量让字段名和类中的字段名一致(我目前完全一致)
4,COMMENT注释一定清楚,不要怕长
5,utf8_bin 类型区别大小写
6,我在建表初期就没打算使用自增主键和其它表关联,所以我决定删除自增主键
7,不能为null的字段添加NOT NULL,可以为null的添加DEFAULT NULL
8,时间类型选择datetime
9,中间表我直接使用联合主键,联合索引的顺序是权重排列
10,中间的创建人,创建时间,修改人,修改时间最后还是被我删了
11,是否删除标识字段,直接上char(1)


sso_system -- (必须)系统表
sso_system_role --(必须)系统和角色中间表(一个系统下有多个角色,一个角色不能属于多个系统)
sso_role --(必须)角色表
sso_user_role --(必须)角色和用户中间表 (角色和用户是多对多的的关系)
sso_user --(必须)用户表
sso_role_permission --(必须)角色和资源中间表(一个角色对应多个资源,一个资源也可以属于多个角色)
sso_permission --(必须)资源表
sso_user_detail -- 用户详情表(一个用户详情目前只设计了一对一的关系)


CREATE DATABASE `sso` CHARACTER SET 'utf8' COLLATE 'utf8_bin'; --创建数据库需要单独先执行后再执行下面的


-- 系统表(必须)
DROP TABLE IF EXISTS `sso_system`;
CREATE TABLE `sso_system` (
  `systemCode` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '唯一关联键',
  `systemName` varchar(16) COLLATE utf8_bin NOT NULL COMMENT '名称',
  `address` varchar(16) DEFAULT NULL COMMENT 'ip',
  
  `status` char(1) NOT NULL COMMENT '是否删除',
  `description` varchar(256) DEFAULT NULL COMMENT '描述', 
  
  `createBy` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '创建人',
  `createDate` datetime NOT NULL COMMENT '创建时间',
  `updateBy` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '修改人',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`systemCode`),
  UNIQUE KEY `unique_name` (`systemName`) USING BTREE COMMENT '唯一名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统表';


-- 角色表(必须)
DROP TABLE IF EXISTS `sso_role`;
CREATE TABLE `sso_role` (
  `roleCode` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '唯一关联键',
  `roleName` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '角色表',
  
  `status` char(1) NOT NULL COMMENT '是否删除',
  `description` varchar(32) DEFAULT NULL COMMENT '描述', 
  
  `createBy` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '创建人',
  `createDate` datetime NOT NULL COMMENT '创建时间',
  `updateBy` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '修改人',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`roleCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色表';

-- 用户表(必须)
DROP TABLE IF EXISTS `sso_user`;
CREATE TABLE `sso_user` (
  `userCode` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '唯一关联键',	
  `username` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '用户名', -- 手机号或唯一的值
  `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '密码',
  `fullname` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '全名',
  `salt` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '盐',
  
  `status` char(1) NOT NULL COMMENT '是否删除',
  `description` varchar(64) DEFAULT NULL COMMENT '描述', 
  
  `createBy` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '创建人',
  `createDate` datetime NOT NULL COMMENT '创建时间',
  `updateBy` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '修改人',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`userCode`),
  UNIQUE KEY `unique_name` (`username`) USING BTREE COMMENT '唯一名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户表';


-- 资源表(必须)
DROP TABLE IF EXISTS `sso_permission`;
CREATE TABLE `sso_permission` (
  `permissionCode` varchar(32) NOT NULL COMMENT '权限标识符',
  `url` varchar(128) DEFAULT NULL COMMENT '请求地址',
  `status` char(1) NOT NULL COMMENT '是否删除',
  `description` varchar(128) DEFAULT NULL COMMENT '描述', 
  
  `createBy` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '创建人',
  `createDate` datetime NOT NULL COMMENT '创建时间',
  `updateBy` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '修改人',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`permissionCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='资源表';



-- 中间表 系统和角色(必须)
DROP TABLE IF EXISTS `sso_system_role`;
CREATE TABLE `sso_system_role` (
  `systemCode` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '系统表code',
  `roleCode` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '角色表的code',
  
  `status` char(1) NOT NULL COMMENT '是否删除',
  `description` varchar(64) DEFAULT NULL COMMENT '描述', 
  PRIMARY KEY (`systemCode`,roleCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='中间表 系统和角色,系统和角色中间表(一个系统下有多个角色,一个角色不能属于多个系统)';


-- 中间表 角色和用户(必须)
DROP TABLE IF EXISTS `sso_role_user`;
CREATE TABLE `sso_role_user` (
  `userCode` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '用户code',
  `roleCode` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '角色表的code',
  
  `status` char(1) NOT NULL COMMENT '是否删除',
  `description` varchar(64) DEFAULT NULL COMMENT '描述', 
  PRIMARY KEY (`userCode`,roleCode),
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='中间表 角色和用户,(角色和用户是多对多的的关系)';


-- 中间表 角色和资源(必须)
DROP TABLE IF EXISTS `sso_role_permission`;
CREATE TABLE `sso_role_permission` (
  `roleCode` varchar(128) NOT NULL COMMENT '角色code',
  `permissionCode` varchar(32) NOT NULL COMMENT '资源code',
  `permissionAdd` char(1) NOT NULL COMMENT '是否有添加',
  `permissionDelete` char(1) NOT NULL COMMENT '是否有删除',
  `permissionUpdate` char(1) NOT NULL COMMENT '是否有修改',
  `permissionSelect` char(1) NOT NULL COMMENT '是否有查询',
  `status` char(1) NOT NULL COMMENT '是否删除',
  `description` varchar(128) DEFAULT NULL COMMENT '描述', 
  PRIMARY KEY (`roleCode`,`permissionCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='中间表 角色和资源表,(一个角色对应多个资源,一个资源也可以属于多个角色';


-- 用户详情表
DROP TABLE IF EXISTS `sso_user_detail`;
CREATE TABLE `sso_user_detail` (
  `userCode` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '唯一关联键',	
  `username` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '用户名', -- 手机号或唯一的值
  `mobile` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '移动电话',
  `phone` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '座机',
  `qq` varchar(16) COLLATE utf8_bin NOT NULL COMMENT 'qq号码',
  `address` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '住址',
  
  `status` char(1) NOT NULL COMMENT '是否删除',
  `description` varchar(64) DEFAULT NULL COMMENT '描述',  
  `createBy` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '创建人',
  `createDate` datetime NOT NULL COMMENT '创建时间',
  `updateBy` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '修改人',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`userCode`),
  UNIQUE KEY `unique_name` (`username`) USING BTREE COMMENT '唯一名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户详情表';