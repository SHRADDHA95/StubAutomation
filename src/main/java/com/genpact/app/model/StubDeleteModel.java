package com.genpact.app.model;

public class StubDeleteModel {
	
	private String userId;
	private String apiName;
	private String itpr;
	
	
	
	public String getItpr() {
		return itpr;
	}

	public void setItpr(String itpr) {
		this.itpr = itpr;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}


	@Override
	public String toString() {
		return "StubDeleteModel [userId=" + this.userId + ", apiName=" + this.apiName + ", ITPRName=" + this.itpr + "]";
	}
	
	
	
	
	
}
