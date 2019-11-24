package com.xm.comment_serialize.module.user.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_user_bill")
public class SuUserBillEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 所属用户id
	 */
	private Integer userId;

	/**
	 * 账单来源(1:拼多多)
	 */
	private Integer fro;

	/**
	 * 账单类型(1:order)
	 */
	private Integer type;

	/**
	 * 账单绑定参数
	 */
	private String parameters;

	/**
	 * 是否删除(0:已删除,1:未删除)
	 */
	private Integer del;

	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
}
