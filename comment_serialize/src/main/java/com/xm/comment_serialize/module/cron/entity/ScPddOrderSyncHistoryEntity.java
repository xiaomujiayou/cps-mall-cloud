package com.xm.comment_serialize.module.cron.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sc_pdd_order_sync_history")
public class ScPddOrderSyncHistoryEntity implements Serializable{
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
	 * 页面大小
	 */
	private Integer pageSize;

	/**
	 * 当前页
	 */
	private Integer page;

	/**
	 * 总页数
	 */
	private Integer totalPage;

	/**
	 * 数据总量
	 */
	private Integer returnCount;

	private java.util.Date createTime;
}
