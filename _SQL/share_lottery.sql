/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 5.7.26 : Database - share_lottery
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`share_lottery` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `share_lottery`;

/*Table structure for table `sl_prop` */

DROP TABLE IF EXISTS `sl_prop`;

CREATE TABLE `sl_prop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(1) DEFAULT NULL COMMENT '道具类型(1:时间类,2:计量类)',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '道具名称',
  `img_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '道具图片链接',
  `detail` text COLLATE utf8mb4_unicode_ci COMMENT '道具详情(富文本)',
  `des` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '道具简介',
  `spec_des` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '规格简介',
  `on_sale` int(1) DEFAULT NULL COMMENT '上下架(0:下架,1:上架)',
  `unit_type` int(1) DEFAULT NULL COMMENT '道具计量单位(1:天,2:个)',
  `max_unit` int(11) DEFAULT NULL COMMENT '最大持有数量',
  `price` int(11) DEFAULT NULL COMMENT '参考价/分',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sl_prop_spec` */

DROP TABLE IF EXISTS `sl_prop_spec`;

CREATE TABLE `sl_prop_spec` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_id` int(11) DEFAULT NULL COMMENT '所属道具id',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '规格名称',
  `type` int(1) DEFAULT NULL COMMENT '规格类型(1:购买,2:赠送)',
  `original_price` int(11) DEFAULT NULL COMMENT '原价(分)',
  `price` int(11) DEFAULT NULL COMMENT '现价(分)',
  `des` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `unit` int(11) DEFAULT NULL COMMENT '该规格下产品单元数',
  `on_sale` int(1) DEFAULT NULL COMMENT '在售(0:下架,1:在售,2:禁用)',
  `choose` int(11) DEFAULT NULL COMMENT '是否选中(0:否,1:是)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sl_user_prop_map` */

DROP TABLE IF EXISTS `sl_user_prop_map`;

CREATE TABLE `sl_user_prop_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户->道具 映射',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `prop_id` int(11) DEFAULT NULL COMMENT '道具id',
  `prop_unit_current` int(11) DEFAULT NULL COMMENT '剩余道具单元数量',
  `prop_unit_total` int(11) DEFAULT NULL COMMENT '历史总量',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sl_user_prop_spec_map` */

DROP TABLE IF EXISTS `sl_user_prop_spec_map`;

CREATE TABLE `sl_user_prop_spec_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_spec_id` int(11) DEFAULT NULL COMMENT '道具规格id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `create_time` datetime DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='AT transaction mode undo table';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
