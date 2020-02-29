package com.xm.comment_serialize.module.mall.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sm_help")
public class SmHelpEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 页面多个","号间隔
	 */
	private String url;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 富文本内容
	 */
	private String content;

	/**
	 * 状态(0:不可用,1:可用)
	 */
	private Integer state;

	private java.util.Date createTime;
}
