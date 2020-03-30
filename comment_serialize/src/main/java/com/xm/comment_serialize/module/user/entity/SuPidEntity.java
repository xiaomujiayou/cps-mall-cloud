package com.xm.comment_serialize.module.user.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "su_pid")
public class SuPidEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 拼多多pid
	 */
	private String pdd;

	/**
	 * 京东
	 */
	private String jd;

	/**
	 * 蘑菇街
	 */
	private String mgj;

	/**
	 * 淘宝
	 */
	private String tb;

	/**
	 * 状态(0:不完善,1:可用,2:已被使用)
	 */
	private Integer state;

	private java.util.Date createTime;
}
