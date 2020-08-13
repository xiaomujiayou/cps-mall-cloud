/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 5.7.26 : Database - share_activite
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`share_activite` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `share_activite`;

/*Table structure for table `sa_active` */

DROP TABLE IF EXISTS `sa_active`;

CREATE TABLE `sa_active` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动表',
  `name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动名称',
  `des` text COLLATE utf8mb4_unicode_ci COMMENT '活动描述',
  `banner_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动banner图',
  `detail_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动详情地址',
  `video_study_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动演示',
  `type` int(1) DEFAULT NULL COMMENT '活动类型(1:商品优惠,2:推广奖励,3:任务奖励)',
  `state` int(1) DEFAULT NULL COMMENT '活动状态(0:下线,1:上线)',
  `user_action` text COLLATE utf8mb4_unicode_ci COMMENT '触发活动的事件(多个","号间隔)',
  `reward_type` int(1) DEFAULT NULL COMMENT '奖励类型(1:现金)',
  `money` int(11) DEFAULT NULL COMMENT '活动奖励(分,参考值)',
  `weight` int(11) DEFAULT NULL COMMENT '排序权重(倒序)',
  `days` int(11) DEFAULT NULL COMMENT '间隔天数',
  `times` int(11) DEFAULT NULL COMMENT '次数',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sa_bill` */

DROP TABLE IF EXISTS `sa_bill`;

CREATE TABLE `sa_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动账单',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `open_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ip` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `active_id` int(11) DEFAULT NULL COMMENT '所属活动',
  `type` int(1) DEFAULT NULL COMMENT '账单类型(1:现金)',
  `money` int(11) DEFAULT NULL COMMENT '奖励金额(分)',
  `attach` longtext COLLATE utf8mb4_unicode_ci COMMENT '账单生成相关数据',
  `attach_des` text COLLATE utf8mb4_unicode_ci COMMENT '相关简介',
  `state` int(1) DEFAULT NULL COMMENT '账单状态(0:待确认,1:待提现,2:审核中,3:已发放,4:发放失败,5:被风控,6:等待确认收货)',
  `fail_reason` text COLLATE utf8mb4_unicode_ci COMMENT '失败原因',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sa_cash_out_record` */

DROP TABLE IF EXISTS `sa_cash_out_record`;

CREATE TABLE `sa_cash_out_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动奖励提现记录表',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `open_id` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cash_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '提现流水号',
  `bill_ids` longtext COLLATE utf8mb4_unicode_ci COMMENT '提现的账单id(","号间隔)',
  `money` int(11) DEFAULT NULL COMMENT '总提现金额',
  `state` int(1) DEFAULT NULL COMMENT '提现状态(1:待审核,2:提现成功,3:提现失败)',
  `fail_reason` text COLLATE utf8mb4_unicode_ci COMMENT '失败原因',
  `ip` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ip地址',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sa_config` */

DROP TABLE IF EXISTS `sa_config`;

CREATE TABLE `sa_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '键',
  `val` text COLLATE utf8mb4_unicode_ci COMMENT '值',
  `des` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) ENGINE=InnoDB AUTO_INCREMENT=661 DEFAULT CHARSET=utf8 COMMENT='AT transaction mode undo table';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
