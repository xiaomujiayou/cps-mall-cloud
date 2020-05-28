package com.xm.comment_serialize.module.active.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.Version;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sa_config")
public class SaConfigEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 键
	 */
	private String name;

	/**
	 * 值
	 */
	private String val;

	/**
	 * 简介
	 */
	private String des;

	private java.util.Date createTime;
}
