package com.xm.comment_serialize.module.user.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_product")
public class SuProductEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 商品id
	 */
	private String goodsId;

	/**
	 * 所属平台
	 */
	private Integer platformType;

	/**
	 * 所属用户
	 */
	private Integer userId;

	/**
	 * 商品类型(1:收藏,2:浏览0.。)
	 */
	private Integer type;

	/**
	 * 是否删除(0:已删除,1:未删除)
	 */
	private Integer del;

	private java.util.Date createTime;
}
