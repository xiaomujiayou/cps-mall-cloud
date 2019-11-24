package com.xm.comment_serialize.module.user.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_permission")
public class SuPermissionEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 权限名称
	 */
	private String name;

	/**
	 * 简介
	 */
	private String des;

	private java.util.Date createTime;
}
