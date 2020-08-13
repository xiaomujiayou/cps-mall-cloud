/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 5.7.26 : Database - share_user
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`share_user` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `share_user`;

/*Table structure for table `su_admin` */

DROP TABLE IF EXISTS `su_admin`;

CREATE TABLE `su_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '后台管理员',
  `user_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码(MD5)',
  `head_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT 'https://mall-share.oss-cn-shanghai.aliyuncs.com/comment/img/manage-head-img.jpg' COMMENT '头像',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_bill` */

DROP TABLE IF EXISTS `su_bill`;

CREATE TABLE `su_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户账单',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `bill_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账单流水',
  `from_user_id` int(11) DEFAULT NULL COMMENT '账单来源用户，因为哪个用户而产生的订单(订单类型为2/3时)',
  `money` int(11) DEFAULT NULL COMMENT '账单金额(分)',
  `type` int(1) DEFAULT NULL COMMENT '账单类型(1:普通自购,2:代理收益,3:分享自购,4:分享收益,5:购买道具)',
  `attach` int(11) DEFAULT NULL COMMENT '相关id(账单类型为[1,2,3,4]为orderId,5:道具规格id)',
  `promotion_rate` int(11) DEFAULT NULL COMMENT '佣金比例(千分比 账单类型为[1,2,3,4])',
  `state` int(1) DEFAULT NULL COMMENT '账单状态(1:等待确认,2:准备发放,3:已发放,4:已失效,5:已处理,6:待支付,7:已支付)',
  `income` int(1) DEFAULT NULL COMMENT '收支类型(1:收入账单,2:支出)',
  `des` text COLLATE utf8mb4_unicode_ci COMMENT '账单简介(微信支付body)',
  `fail_reason` text COLLATE utf8mb4_unicode_ci COMMENT '账单失效原因',
  `credit_state` int(1) DEFAULT NULL COMMENT '信用支付状态(0:非信用支付,1:达到信用支付条件,2:信用下降解除绑定)',
  `pay_time` datetime DEFAULT NULL COMMENT '打款时间',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_config` */

DROP TABLE IF EXISTS `su_config`;

CREATE TABLE `su_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户配置(会覆盖系统配置)',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置类型',
  `val` text COLLATE utf8mb4_unicode_ci COMMENT '配置值',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_feedback` */

DROP TABLE IF EXISTS `su_feedback`;

CREATE TABLE `su_feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户反馈',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `des` text COLLATE utf8mb4_unicode_ci COMMENT '问题描述',
  `images` text COLLATE utf8mb4_unicode_ci COMMENT '相关图片',
  `type` int(1) DEFAULT '1' COMMENT '意见类型(1:默认)',
  `state` int(1) DEFAULT '1' COMMENT '状态(1:待处理,2:已处理)',
  `accept` int(1) DEFAULT '0' COMMENT '是否采纳(0:默认,1:未采纳,2:已采纳)',
  `reply` text COLLATE utf8mb4_unicode_ci COMMENT '回复',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_menu_tips` */

DROP TABLE IF EXISTS `su_menu_tips`;

CREATE TABLE `su_menu_tips` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户菜单按钮提示',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户id',
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单id',
  `hot` int(1) DEFAULT NULL COMMENT '是否显示小红点(0:否,1:是)',
  `num` int(11) DEFAULT NULL COMMENT '显示数字(少于四位数)',
  `update_time` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_order` */

DROP TABLE IF EXISTS `su_order`;

CREATE TABLE `su_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `order_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单编号',
  `order_sub_sn` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '子订单号',
  `product_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品id',
  `product_name` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名称',
  `img_url` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品简介',
  `platform_type` int(1) DEFAULT NULL COMMENT '所属平台',
  `state` int(1) DEFAULT NULL COMMENT '订单状态(-1:无效订单,0:未支付,1:已支付,2:确认收货,3:已结算,4:结算失败)',
  `fail_reason` text COLLATE utf8mb4_unicode_ci COMMENT '审核失败原因',
  `p_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '推广位id',
  `original_price` int(11) DEFAULT NULL COMMENT '单品原始价格(分)',
  `quantity` int(1) DEFAULT NULL COMMENT '购买数量',
  `amount` int(11) DEFAULT NULL COMMENT '实际支付金额(分)',
  `promotion_rate` int(11) DEFAULT NULL COMMENT '佣金比例(千分比)',
  `promotion_amount` int(11) DEFAULT NULL COMMENT '佣金(分)',
  `type` int(1) DEFAULT NULL COMMENT '订单类型(0:领券页面,1:红包页,2:领券页,3:题页)',
  `form_type` int(1) DEFAULT NULL COMMENT '来源类型(1:普通自购,2:分享自购)',
  `share_user_id` int(11) DEFAULT NULL COMMENT '分享订单分享的用户id',
  `from_app` int(1) DEFAULT NULL COMMENT '订单来源(1:微信小程序)',
  `custom_parameters` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '自定义参数(userId:购买用户,shareUserId:分享的用户,fromApp:app来源)',
  `cart` text COLLATE utf8mb4_unicode_ci COMMENT '购物车所有商品ID(,号分割)',
  `order_modify_at` datetime DEFAULT NULL COMMENT '最后更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`order_sub_sn`,`platform_type`),
  KEY `EXIST` (`order_sub_sn`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_permission` */

DROP TABLE IF EXISTS `su_permission`;

CREATE TABLE `su_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限名称',
  `des` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_pid` */

DROP TABLE IF EXISTS `su_pid`;

CREATE TABLE `su_pid` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pdd` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拼多多pid',
  `jd` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '京东',
  `mgj` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '蘑菇街',
  `tb` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '淘宝',
  `wph` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '唯品会',
  `state` int(1) DEFAULT '1' COMMENT '状态(0:不完善,1:可用,2:已被使用)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9781 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_product` */

DROP TABLE IF EXISTS `su_product`;

CREATE TABLE `su_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `app_type` int(1) DEFAULT NULL,
  `goods_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品id',
  `platform_type` int(1) DEFAULT NULL COMMENT '所属平台',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `is_collect` int(1) DEFAULT NULL COMMENT '是否被搜藏(0:否,1:是)',
  `share_user_id` int(11) DEFAULT NULL COMMENT '分享用户id',
  `coupon_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '优惠券id(淘宝用)',
  `goods_info` text COLLATE utf8mb4_unicode_ci COMMENT '商品信息快照(json格式)',
  `del` int(1) DEFAULT NULL COMMENT '是否删除(0:已删除,1:未删除)',
  `collect_time` datetime DEFAULT NULL COMMENT '搜藏时间',
  `update_time` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`goods_id`,`platform_type`,`user_id`),
  KEY `EXIST` (`goods_id`,`platform_type`,`user_id`),
  KEY `QUEUE` (`user_id`,`is_collect`,`del`,`collect_time`,`update_time`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_role` */

DROP TABLE IF EXISTS `su_role`;

CREATE TABLE `su_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色名称',
  `des` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_role_permission_map` */

DROP TABLE IF EXISTS `su_role_permission_map`;

CREATE TABLE `su_role_permission_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `permission_id` int(11) DEFAULT NULL COMMENT '权限id',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_search` */

DROP TABLE IF EXISTS `su_search`;

CREATE TABLE `su_search` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户搜索历史记录',
  `user_id` int(11) DEFAULT NULL,
  `keyword` text COLLATE utf8mb4_unicode_ci COMMENT '搜索内容',
  `del` int(1) DEFAULT NULL COMMENT '是否删除(0:已删除,1未删除)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_share` */

DROP TABLE IF EXISTS `su_share`;

CREATE TABLE `su_share` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户分享记录',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `goods_id` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品id',
  `platform_type` int(1) DEFAULT NULL COMMENT '所属平台',
  `watch` int(11) DEFAULT '0' COMMENT '商品浏览次数',
  `sell` int(11) DEFAULT '0' COMMENT '商品下单次数',
  `will_make_money` int(11) DEFAULT '0' COMMENT '预估收入(分)',
  `del` int(1) DEFAULT NULL COMMENT '删除(0:删除,1:未删除)',
  `goods_info` text COLLATE utf8mb4_unicode_ci COMMENT '商品信息快照(json)',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`user_id`,`goods_id`,`platform_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_summary` */

DROP TABLE IF EXISTS `su_summary`;

CREATE TABLE `su_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户数据汇总',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `profit_today` int(11) DEFAULT NULL COMMENT '今日收益(分)',
  `profit_today_last_update` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '今日收益最后一次更新日期',
  `profit_history` int(11) DEFAULT NULL COMMENT '历史收益',
  `profit_wait` int(11) DEFAULT NULL COMMENT '等待确认',
  `profit_ready` int(11) DEFAULT NULL COMMENT '准备发放',
  `profit_cash` int(11) DEFAULT NULL COMMENT '已返',
  `total_coupon` int(11) DEFAULT NULL COMMENT '使用优惠劵总量',
  `total_buy` int(11) DEFAULT NULL COMMENT '自购成交额',
  `total_share` int(11) DEFAULT NULL COMMENT '分享成交额',
  `total_proxy_user` int(11) DEFAULT NULL COMMENT '锁定用户总量',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`user_id`),
  KEY `EXIST` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_user` */

DROP TABLE IF EXISTS `su_user`;

CREATE TABLE `su_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL COMMENT '父用户id',
  `nickname` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信昵称',
  `head_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT 'https://mall-share.oss-cn-shanghai.aliyuncs.com/comment/img/head-img%20.png' COMMENT '头像',
  `sex` int(1) DEFAULT '0' COMMENT '性别(0:未知,1:男,2:女)',
  `open_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tel` varchar(13) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `state` int(1) DEFAULT '1' COMMENT '账号状态(0:封禁,1:正常)',
  `reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '封禁理由',
  `city` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `province` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省',
  `country` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '国家',
  `language` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '语言',
  `pid` int(11) DEFAULT NULL COMMENT '推广位id',
  `from_whare` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户来源',
  `register_ip` varchar(18) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '注册ip',
  `current_ip` varchar(18) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '当前IP',
  `last_login` datetime NOT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `su_user_role_map` */

DROP TABLE IF EXISTS `su_user_role_map`;

CREATE TABLE `su_user_role_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `creator` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者',
  `reason` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建原因',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) ENGINE=InnoDB AUTO_INCREMENT=4780 DEFAULT CHARSET=utf8 COMMENT='AT transaction mode undo table';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
