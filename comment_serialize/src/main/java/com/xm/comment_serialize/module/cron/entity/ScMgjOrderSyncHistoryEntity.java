package com.xm.comment_serialize.module.cron.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sc_mgj_order_sync_history")
public class ScMgjOrderSyncHistoryEntity implements Serializable{
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 开始时间
	 */
	private java.util.Date startUpdateTime;

	/**
	 * 结束时间
	 */
	private java.util.Date endUpdateTime;

	/**
	 * 数据总量
	 */
	private Integer returnCount;

	private java.util.Date createTime;
}
