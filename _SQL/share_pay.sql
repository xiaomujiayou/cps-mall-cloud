/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 5.7.26 : Database - share_pay
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`share_pay` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `share_pay`;

/*Table structure for table `sp_wx_ent_pay_order_in` */

DROP TABLE IF EXISTS `sp_wx_ent_pay_order_in`;

CREATE TABLE `sp_wx_ent_pay_order_in` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bill_pay_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户',
  `type` int(1) DEFAULT NULL COMMENT '记录类型(1:订单,2:活动提现,3:活动账单)',
  `bill_ids` longtext COLLATE utf8mb4_unicode_ci COMMENT '包含的订单id',
  `mch_appid` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '申请商户号的appid或商户号绑定的appid',
  `mchid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信支付分配的商户号',
  `device_info` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信支付分配的终端设备号',
  `nonce_str` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '随机字符串，不长于32位',
  `sign` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名',
  `partner_trade_no` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商户订单号',
  `openid` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'openid',
  `check_name` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'NO_CHECK：不校验真实姓名',
  `re_user_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收款用户真实姓名。',
  `amount` int(11) DEFAULT NULL COMMENT '企业付款金额，单位为分',
  `des` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业付款备注',
  `spbill_create_ip` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP',
  `return_code` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'SUCCESS/FAIL',
  `return_msg` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '返回信息，如非空，为错误原因',
  `result_code` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'SUCCESS/FAIL，注意：当状态为FAIL时，存在业务结果未明确的情况。如果状态为FAIL，请务必关注错误代码（err_code字段），通过查询查询接口确认此次付款的结果。',
  `err_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '错误码信息，注意：出现未明确的错误码时（SYSTEMERROR等），请务必用原商户订单号重试，或通过查询接口确认此次付款的结果。',
  `err_code_des` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '结果信息描述',
  `payment_no` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业付款成功，返回的微信付款单号',
  `payment_time` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业付款成功时间',
  `state` int(1) DEFAULT NULL COMMENT '状态(1:付款成功,2:付款失败)',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sp_wx_order_in` */

DROP TABLE IF EXISTS `sp_wx_order_in`;

CREATE TABLE `sp_wx_order_in` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '支付订单(统一下单数据)',
  `out_trade_no` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商户订单号',
  `openid` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户标识(trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识)',
  `su_bill_id` int(11) DEFAULT NULL COMMENT '用户所属账单',
  `appid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '小程序id',
  `mch_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信支付商户号',
  `product_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品ID(trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义。)',
  `attach` varchar(127) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '附加数据',
  `total_fee` int(16) DEFAULT NULL COMMENT '订单总金额(单位为分)',
  `device_info` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '自定义参数(设备号)',
  `nonce_str` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '随机字符串',
  `sign` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名',
  `sign_type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名类型',
  `body` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品简单描述',
  `detail` text COLLATE utf8mb4_unicode_ci COMMENT '商品详情',
  `fee_type` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '符合ISO 4217标准的三位字母代码，默认人民币：CNY',
  `spbill_create_ip` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '终端IP(支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP)',
  `time_start` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010',
  `time_expire` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id',
  `goods_tag` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单优惠标记，使用代金券或立减优惠功能时需要的参数',
  `notify_url` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。',
  `trade_type` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易类型(小程序取值如下：JSAPI)',
  `limit_pay` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '指定支付方式(上传此参数no_credit--可限制用户不能使用信用卡支付)',
  `receipt` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电子发票入口开放标识',
  `scene_info` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '场景信息',
  `state` int(1) DEFAULT NULL COMMENT '支付状态(0:待支付,1:支付成功,2:支付取消)',
  `err_msg` text COLLATE utf8mb4_unicode_ci COMMENT '错误信息',
  `req_bo` text COLLATE utf8mb4_unicode_ci COMMENT '业务对象(SuBillToPayBo)',
  `package_val` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '预支付交易会话标识',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `sp_wx_order_notify` */

DROP TABLE IF EXISTS `sp_wx_order_notify`;

CREATE TABLE `sp_wx_order_notify` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信分配的小程序ID',
  `mch_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信支付分配的商户号',
  `out_trade_no` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商户订单号',
  `total_fee` int(11) DEFAULT NULL COMMENT '订单金额',
  `attach` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '附加数据',
  `device_info` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信支付分配的终端设备号',
  `nonce_str` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '随机字符串，不长于32位',
  `sign` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名',
  `sign_type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名类型',
  `openid` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_subscribe` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否关注公众账号',
  `trade_type` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易类型',
  `bank_type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '付款银行',
  `settlement_total_fee` int(11) DEFAULT NULL COMMENT '应结订单金额',
  `fee_type` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '货币种类',
  `cash_fee` int(11) DEFAULT NULL COMMENT '现金支付金额',
  `cash_fee_type` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '现金支付货币类型',
  `coupon_fee` int(10) DEFAULT NULL COMMENT '总代金券金额',
  `coupon_count` int(10) DEFAULT NULL COMMENT '代金券使用数量',
  `transaction_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信支付订单号',
  `time_end` varchar(14) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支付完成时间',
  `result_code` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '业务结果',
  `err_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '错误代码',
  `err_code_des` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '错误代码描述',
  `return_code` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断',
  `return_msg` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '返回信息',
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
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8 COMMENT='AT transaction mode undo table';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
