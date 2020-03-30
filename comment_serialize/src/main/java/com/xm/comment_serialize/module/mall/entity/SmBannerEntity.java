package com.xm.comment_serialize.module.mall.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sm_banner")
public class SmBannerEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * app类型
	 */
	private Integer appType;

	private Integer platformType;

	/**
	 * 标题
	 */
	private String name;

	/**
	 * 包含的信息
	 */
	private String url;

	/**
	 * 图片路径
	 */
	private String img;

	/**
	 * 目标(1:普通url跳转,2:小程序跳转,3:唤醒其他小程序,4:唤醒app,5:客服消息)
	 */
	private Integer target;

	/**
	 * 类型(1:首页轮播图,2:首页滑动列表)
	 */
	private Integer type;

	/**
	 * 是否可用(0:不可用,1：可用)
	 */
	private Integer disable;

	/**
	 * 排序(从大到小)
	 */
	private Integer sort;

	private java.util.Date createTime;
}
