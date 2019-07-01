package com.genpact.app.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name = "STUB_DETAILS")
@Entity
public class StubDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	long id;

	@Column(name = "USER_ID")
	String userId;

	@Column(name = "ITPR")
	String itpr;

	@Column(name = "API_NAME")
	String apiName;

	@Column(name = "RESP_JSON")
	String respJson;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE_TIME")
	Date createDateTime;

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Date getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(Date updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE_TIME")
	Date updateDateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getItpr() {
		return itpr;
	}

	public void setItpr(String itpr) {
		this.itpr = itpr;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getRespJson() {
		return respJson;
	}

	public void setRespJson(String respJson) {
		this.respJson = respJson;
	}
	
}
