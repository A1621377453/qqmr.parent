package com.qingqingmr.qqmr.domain.entity;

import java.util.Date;

/**
 * 短信模板
 * 
 * @author liujingjing
 * @datetime 2018年7月17日 下午6:43:23
 */
public class TemplateNotice {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 添加时间
	 */
	private Date time;

	/**
	 * 模板类型:1-短信模板；2-消息模板；3-邮件模板
	 */
	private Integer type;

	/**
	 * 应用场景
	 */
	private Integer scene;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}


	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getScene() {
		return scene;
	}

	public void setScene(Integer scene) {
		this.scene = scene;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}
}