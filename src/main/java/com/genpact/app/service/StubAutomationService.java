package com.genpact.app.service;

import com.genpact.app.model.SignUpModel;
import com.genpact.app.model.StubDeleteModel;
import com.genpact.app.model.StubDetailsModel;

import java.util.List;

import org.json.JSONObject;

public interface StubAutomationService {

	String signUp(SignUpModel signUpModel) throws Exception;

	String signIn(SignUpModel signInModel);

	JSONObject stubCreate(JSONObject reJsonObject) throws Exception;

	JSONObject stubUpdate(JSONObject reJsonObject) throws Exception;
	
	String stubDelete(StubDeleteModel signInModel);
	
	String getStub(String itprName, String apiName);
	
	List<StubDetailsModel> getStubDetails(SignUpModel userDetails);

}
