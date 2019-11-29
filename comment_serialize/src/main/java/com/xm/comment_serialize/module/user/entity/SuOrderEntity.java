package com.xm.comment_serialize.module.user.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_order")
public class SuOrderEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属用户
	 */
	private Integer userId;

	/**
	 * 订单编号
	 */
	private String orderSn;

	/**
	 * 商品id
	 */
	private String productId;

	/**
	 * 商品名称
	 */
	private String productName;

	/**
	 * 商品简介
	 */
	private String imgUrl;

	/**
	 * 所属平台
	 */
	private Integer platformType;

	/**
	 * 订单状态(-1 未支付; 0-已支付；1-已成团；2-确认收货；3-审核成功；4-审核失败（不可提现）；5-已经结算；8-非多多进宝商品（无佣金订单）;10-已处罚)
	 */
	private Integer state;

	/**
	 * 审核失败原因
	 */
	private String failReason;

	/**
	 * 推广位id
	 */
	private String pId;

	/**
	 * 单品原始价格(分)
	 */
	private Integer originalPrice;

	/**
	 * 购买数量
	 */
	private Integer quantity;

	/**
	 * 实际支付金额(分)
	 */
	private Integer amount;

	/**
	 * 佣金比例(千分比)
	 */
	private Integer promotionRate;

	/**
	 * 佣金(分)
	 */
	private Integer promotionAmount;

	/**
	 * 订单类型(0:领券页面,1:红包页,2:领券页,3:题页)
	 */
	private Integer type;

	/**
	 * 自定义参数(userId:购买用户,fromUser:分享的用户,appType:app来源)
	 */
	private String customParameters;

	/**
	 * 最后更新时间
	 */
	private java.util.Date orderModifyAt;

	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
}
