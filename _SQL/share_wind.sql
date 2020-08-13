/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 5.7.26 : Database - share_wind
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`share_wind` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `share_wind`;

/*Table structure for table `sw_api_record` */

DROP TABLE IF EXISTS `sw_api_record`;

CREATE TABLE `sw_api_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户API请求记录',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `app_type` int(11) DEFAULT NULL COMMENT 'app类型',
  `ip` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ip地址',
  `ip_addr` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ip解析',
  `ua` text COLLATE utf8mb4_unicode_ci COMMENT '浏览器ua',
  `url` text COLLATE utf8mb4_unicode_ci COMMENT '请求链接',
  `method` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求类型',
  `param` text COLLATE utf8mb4_unicode_ci COMMENT '请求参数',
  `result` longtext COLLATE utf8mb4_unicode_ci COMMENT '响应结果',
  `time` int(11) DEFAULT NULL COMMENT '执行时间(毫秒)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=635 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sw_credit_bill_bind_record` */

DROP TABLE IF EXISTS `sw_credit_bill_bind_record`;

CREATE TABLE `sw_credit_bill_bind_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '信用账单绑定记录',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `goods_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名称',
  `bill_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '绑定的订单',
  `bill_money` int(11) DEFAULT NULL COMMENT '账单金额',
  `pay_time` datetime DEFAULT NULL COMMENT '发放时间',
  `state` int(1) DEFAULT NULL COMMENT '绑定状态(0:已释放,1:绑定中)',
  `bind_scores` int(11) DEFAULT NULL COMMENT '绑定时的信用',
  `un_bind_scores` int(11) DEFAULT NULL COMMENT '解绑时的信用',
  `un_bind_reason` text COLLATE utf8mb4_unicode_ci COMMENT '解绑原因',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `EXIST` (`user_id`,`state`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sw_credit_bill_conf` */

DROP TABLE IF EXISTS `sw_credit_bill_conf`;

CREATE TABLE `sw_credit_bill_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户信用付款阶梯配置',
  `scores` int(11) DEFAULT NULL COMMENT '分数',
  `advance_count` int(11) DEFAULT NULL COMMENT '可以预支的次数',
  `max_quota` int(11) DEFAULT NULL COMMENT '最大预支的总额度(分)',
  `quota` int(11) DEFAULT NULL COMMENT '每笔限额',
  `pay_days` int(11) DEFAULT NULL COMMENT '打款间隔(天)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sw_credit_bill_pay_record` */

DROP TABLE IF EXISTS `sw_credit_bill_pay_record`;

CREATE TABLE `sw_credit_bill_pay_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账单付款风控检测记录',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `bill_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账单号',
  `bill_money` int(11) DEFAULT NULL COMMENT '账单金额',
  `check_result` int(1) DEFAULT NULL COMMENT '检测结果(0:未通过,1:通过,2:人工处理)',
  `fail_reason` text COLLATE utf8mb4_unicode_ci COMMENT '未通过原因',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sw_credit_conf` */

DROP TABLE IF EXISTS `sw_credit_conf`;

CREATE TABLE `sw_credit_conf` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '信用配置',
  `name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置名称',
  `val` text COLLATE utf8mb4_unicode_ci COMMENT '配置值',
  `des` text COLLATE utf8mb4_unicode_ci COMMENT '配置简介',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sw_credit_record` */

DROP TABLE IF EXISTS `sw_credit_record`;

CREATE TABLE `sw_credit_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户信用记录',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `type` int(1) DEFAULT NULL COMMENT '分数类型(0:新用户注册,1:每日登录,2:邀请好友,3:订单交易成功,4:恶意退单)',
  `change_scores` int(11) DEFAULT NULL COMMENT '分数变动',
  `scores` int(11) DEFAULT NULL COMMENT '改变后的分数',
  `change_reason` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '改变原因',
  `attached` text COLLATE utf8mb4_unicode_ci COMMENT '附件数据(订单sn)',
  `create_stamp` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建时间戳',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `EXIST` (`user_id`,`type`,`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sw_login_record` */

DROP TABLE IF EXISTS `sw_login_record`;

CREATE TABLE `sw_login_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户登录记录表',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `app_type` int(11) DEFAULT NULL COMMENT 'app类型',
  `ip` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录ip',
  `ip_addr` text COLLATE utf8mb4_unicode_ci COMMENT 'ip地址解析',
  `ua` text COLLATE utf8mb4_unicode_ci COMMENT '浏览器UA',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `undo_log` */

DROP TABLE IF EXISTS `undo_log`;

CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'increment id',
  `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime NOT NULL COMMENT 'create datetime',
  `log_modified` datetime NOT NULL COMMENT 'modify datetime',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1024 DEFAULT CHARSET=utf8 COMMENT='AT transaction mode undo table';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

insert  into `sw_credit_bill_conf`(`id`,`scores`,`advance_count`,`max_quota`,`quota`,`pay_days`,`create_time`) values 
(1,60,10,500,200,1,'2020-04-04 16:52:09'),
(3,80,30,1000,500,1,'2020-04-04 16:52:43'),
(5,100,50,2000,1000,1,'2020-04-04 17:03:41');

/*Data for the table `sw_credit_conf` */

insert  into `sw_credit_conf`(`id`,`name`,`val`,`des`,`create_time`) values 
(1,'default_credit_scores','60','新用户默认信用积分','2020-04-06 22:36:59'),
(2,'default_credit_unbind_delay','10','信用账单提前解绑时间（打款十天后无异常）','2020-04-16 16:13:42'),
(3,'default_credit_has_conf_desc','信用分数影响您的返现速度哦，请好好珍惜。','信用及格提示','2020-04-18 01:53:03'),
(4,'default_credit_no_conf_desc','信用过低，返现加速已停止。','信用不及格提示','2020-04-18 01:54:30'),
(5,'get_credit_method','[\"①每日登录，信用+1，当月最多奖励5次。\", \"②新增一位锁定用户，信用+1，每天最多奖励2次，每月最多奖励10次。\", \"③订单交易完成（中途没有申请售后、退款、取消订单），信用+5。\", \"④恶意退单（返现完成后申请售后、退款、取消订单），信用-20。\"]','获取信用的方法(数组)','2020-04-18 16:51:33');
