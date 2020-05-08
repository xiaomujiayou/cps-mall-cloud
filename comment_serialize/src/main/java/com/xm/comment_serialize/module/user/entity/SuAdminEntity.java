package com.xm.comment_serialize.module.user.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.Version;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_admin")
public class SuAdminEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 密码(MD5)
	 */
	private String password;

	/**
	 * 头像
	 */
	private String headImg;

	private java.util.Date createTime;
}
