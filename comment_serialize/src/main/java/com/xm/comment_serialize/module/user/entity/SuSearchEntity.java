package com.xm.comment_serialize.module.user.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_search")
public class SuSearchEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer userId;

	/**
	 * 搜索内容
	 */
	private String keyword;

	/**
	 * 是否删除(0:已删除,1未删除)
	 */
	private Integer del;

	private java.util.Date createTime;
}
