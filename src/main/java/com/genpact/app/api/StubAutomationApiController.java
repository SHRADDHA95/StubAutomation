package com.genpact.app.api;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.genpact.app.constants.IStubAutomation;

import com.genpact.app.model.SignUpModel;
import com.genpact.app.model.StubDeleteModel;
import com.genpact.app.model.StubDetailsModel;
import com.genpact.app.service.StubAutomationService;

@RestController
@RequestMapping(value = "/")
//@CrossOrigin(origins = "http://10.101.16.21:4200")
public class StubAutomationApiController implements StubAutomationApi, IStubAutomation {

	private static final Logger LOGGER = LoggerFactory.getLogger(StubAutomationApiController.class);

	@Autowired
	StubAutomationService service;

	// USer Sign Up API
	@Override
	public ResponseEntity<String> signUp(SignUpModel signUpModel) {
		try {
			JSONObject respObj = new JSONObject();

			String resString = service.signUp(signUpModel);

			if (SUCCESS.equalsIgnoreCase(resString)) {

				return new ResponseEntity<String>(respObj.put(STATUS, resString).toString(), HttpStatus.OK);

			} else if (CONFLICT.equalsIgnoreCase(resString)) {

				return new ResponseEntity<String>(respObj.put(STATUS, resString).toString(), HttpStatus.CONFLICT);
			}

			else {
				return new ResponseEntity<String>(respObj.put(STATUS, resString).toString(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			LOGGER.error(" An Exception occured" + IN + SIGNUP + e);
			return new ResponseEntity<String>("{\"status\":\"" + INTERNAL_SERVER_ERROR + "\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Create URL API
	@Override
	public ResponseEntity<String> stubCreate(String requestJSON) throws Exception {
		LOGGER.info("request Json "+requestJSON.toString());
		JSONObject result = new JSONObject();
		try {
			String parsedResponse=StringEscapeUtils.unescapeJson(requestJSON);
			parsedResponse=parsedResponse.replace("\"{", "{").replace("\"}", "}");
			JSONObject respObj = new JSONObject(parsedResponse); 
			JSONObject jsonStub=respObj.getJSONObject("respJson");
			result = service.stubCreate(respObj);
			System.out.println(result.toString());
			if (SUCCESS.equalsIgnoreCase(result.get(STATUS).toString())) {
				return new ResponseEntity<String>(result.toString(), HttpStatus.OK);
			} else if (API_CONFLICT.equalsIgnoreCase(result.get(STATUS).toString())) {

				return new ResponseEntity<String>(result.toString(), HttpStatus.OK);
			} else {
				
				return new ResponseEntity<String>(result.toString(), HttpStatus.BAD_REQUEST);
			}
		}catch (JSONException e) {
			e.printStackTrace();
			LOGGER.error(" An Exception occured" + IN + STUB_CREATE + e);
			result.put(STATUS, FAILURE);
			result.put(URL, EMPTY);
			result.put(MSG, "Invalid Json Format");
			return new ResponseEntity<String>(result.toString(), HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (Exception e) {
			LOGGER.error(" An Exception occured" + IN + STUB_CREATE + e);
			result.put(STATUS, INTERNAL_SERVER_ERROR);
			result.put(URL, EMPTY);
			result.put(MSG, "Server Error");
			return new ResponseEntity<String>(result.toString(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@Override
	public ResponseEntity<String> signIn(SignUpModel signInModel) {
		String serviceResp = service.signIn(signInModel);
		try {
			if(serviceResp.equals("SUCCESS")) {
				JSONObject resJson= new JSONObject();
				resJson.put("status", serviceResp);
				return  new ResponseEntity<String>(resJson.put("status", serviceResp).toString(),HttpStatus.OK);	
			}else {
				return  new ResponseEntity<String>("{\"status\":\""+serviceResp+"\"}",HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e) {
			return  new ResponseEntity<String>("{\"status\":\""+serviceResp+"\"}",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Update API
	@Override
	public ResponseEntity<String> stubUpdate(String requestJSON) throws Exception {

		JSONObject result = new JSONObject();
		try {
			JSONObject respObj = new JSONObject(requestJSON);
			result = service.stubUpdate(respObj);
			System.out.println(result.toString());
			if (SUCCESS.equalsIgnoreCase(result.get(STATUS).toString())) {
				return new ResponseEntity<String>(result.toString(), HttpStatus.OK);
			} else if (CONFLICT.equalsIgnoreCase(result.get(STATUS).toString())) {

				return new ResponseEntity<String>(result.toString(), HttpStatus.CONFLICT);
			} else {
				return new ResponseEntity<String>(result.toString(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			LOGGER.error(" An Exception occured" + IN + STUB_CREATE + e);
			result.put(STATUS, INTERNAL_SERVER_ERROR);
			result.put(URL, EMPTY);
			result.put(MSG, "Server Error");
			return new ResponseEntity<String>(result.toString(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@Override
	public ResponseEntity<String> stubDelete(StubDeleteModel stubDeleteModel) {
		
		try {
			
			JSONObject respObj = new JSONObject();

			String resString = service.stubDelete(stubDeleteModel);

			if (resString.equalsIgnoreCase(SUCCESS)) {

				return new ResponseEntity<String>(respObj.put(STATUS, resString).toString(), HttpStatus.OK);

			} else if (resString.equalsIgnoreCase(NOTPRESENT)) {

				return new ResponseEntity<String>(respObj.put(STATUS, resString).toString(), HttpStatus.CONFLICT);
			}

			else {
				return new ResponseEntity<String>(respObj.put(STATUS, resString).toString(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			LOGGER.error(" An Exception occured" + IN + STUBDELETE + e);
			return new ResponseEntity<String>("{\"status\":\"" + "Internal Server Error" + "\"}",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	} 
	
	@Override
	public ResponseEntity<String> getStub(String itprName, String apiName) throws Exception{
		JSONObject result = new JSONObject();
		String resString = service.getStub(itprName, apiName);
		System.out.println(resString);
		//JSONObject respObj= new JSONObject();
		try {
			if(!resString.isEmpty()) {
				return new ResponseEntity<String>(resString, HttpStatus.OK);	
			}
			return new ResponseEntity<String>(resString, HttpStatus.CONFLICT);
		}
		catch(Exception e){
			LOGGER.error(" An Exception occured" + IN + STUB_CREATE + e);
			result.put(STATUS, INTERNAL_SERVER_ERROR);
			result.put(URL, EMPTY);
			result.put(MSG, "Server Error");
			return new ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<List<StubDetailsModel>> getStubDetails(SignUpModel userDetails) throws Exception{
		JSONObject result = new JSONObject();
		List<StubDetailsModel> resList = service.getStubDetails(userDetails);
		// JSONObject respObj= new JSONObject();
		try {
			if (null != resList && !resList.isEmpty()) {
				return new ResponseEntity<List<StubDetailsModel>>(resList, HttpStatus.OK);
			}
			return new ResponseEntity<List<StubDetailsModel>>(resList, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.error(" An Exception occured" + IN + STUB_CREATE + e);
			result.put(STATUS, INTERNAL_SERVER_ERROR);
			result.put(URL, EMPTY);
			result.put(MSG, "Server Error");
			return new ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> getStubPost(String itprName, String apiName) throws Exception{
		JSONObject result = new JSONObject();
		String resString = service.getStub(itprName, apiName);
		System.out.println(resString);
		// JSONObject respObj= new JSONObject();
		try {
			if (!resString.isEmpty()) {
				return new ResponseEntity<String>(resString, HttpStatus.OK);
			}
			return new ResponseEntity<String>(resString, HttpStatus.CONFLICT);
		} catch (Exception e) {
			LOGGER.error(" An Exception occured" + IN + STUB_CREATE + e);
			result.put(STATUS, INTERNAL_SERVER_ERROR);
			result.put(URL, EMPTY);
			result.put(MSG, "Server Error");
			return new ResponseEntity<String>(result.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
