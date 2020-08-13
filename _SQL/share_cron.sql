/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 5.7.26 : Database - share_cron
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`share_cron` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `share_cron`;

/*Table structure for table `sc_bill_pay` */

DROP TABLE IF EXISTS `sc_bill_pay`;

CREATE TABLE `sc_bill_pay` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `open_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户标识',
  `bill_ids` longtext COLLATE utf8mb4_unicode_ci COMMENT '相关账单',
  `total_money` int(11) DEFAULT NULL COMMENT '支付总额',
  `state` int(1) DEFAULT NULL COMMENT '状态(1:待处理,2:处理成功,3:处理失败)',
  `fail_reason` text COLLATE utf8mb4_unicode_ci COMMENT '失败原因',
  `process_id` int(11) DEFAULT NULL COMMENT '处理结果id',
  `process_sn` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支付单号',
  `pay_sys_sn` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支付平台单号',
  `update_time` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sc_mgj_order_record` */

DROP TABLE IF EXISTS `sc_mgj_order_record`;

CREATE TABLE `sc_mgj_order_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '蘑菇街订单记录',
  `order_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单编号',
  `order_sub_sn` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '子订单编号',
  `state` int(1) DEFAULT NULL COMMENT '订单状态',
  `last_update` datetime NOT NULL COMMENT '最后更新时间',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sc_order_state_record` */

DROP TABLE IF EXISTS `sc_order_state_record`;

CREATE TABLE `sc_order_state_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单原始数据状态对照表',
  `order_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单号',
  `order_sub_sn` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '子订单号',
  `platform_type` int(1) DEFAULT NULL COMMENT '所属平台',
  `origin_state` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单原始状态',
  `origin_state_des` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '原始状态简介',
  `state` int(11) DEFAULT NULL COMMENT '解析后的状态(0:未支付,1:已支付,2:确认收货,3:已结算,4:结算失败)',
  `res` longtext COLLATE utf8mb4_unicode_ci COMMENT '商品原始报文，最早的记录(json格式)',
  `update_time` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sc_order_sync_history` */

DROP TABLE IF EXISTS `sc_order_sync_history`;

CREATE TABLE `sc_order_sync_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `platform_type` int(11) DEFAULT NULL COMMENT '所属平台',
  `start_time` datetime DEFAULT NULL COMMENT '查询开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '查询结束时间',
  `total` int(11) DEFAULT NULL COMMENT '数据量',
  `current_page` int(11) DEFAULT NULL COMMENT '当前页',
  `page_size` int(11) DEFAULT NULL COMMENT '页面大小',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sc_wait_pay_bill` */

DROP TABLE IF EXISTS `sc_wait_pay_bill`;

CREATE TABLE `sc_wait_pay_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `open_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bill_id` int(11) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `state` int(1) DEFAULT NULL COMMENT '处理状态(1:待发放,2:发放中,3:已发放,4:发放失败)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
