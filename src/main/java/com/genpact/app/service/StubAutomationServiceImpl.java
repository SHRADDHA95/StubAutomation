package com.genpact.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.genpact.app.constants.IStubAutomation;
import com.genpact.app.dao.StubAutomationDao;
import com.genpact.app.dao.StubDetailDao;
import com.genpact.app.entity.StubDetailsEntity;
import com.genpact.app.entity.UserAuthEntity;
import com.genpact.app.model.SignUpModel;
import com.genpact.app.model.StubDeleteModel;
import com.genpact.app.model.StubDetailsModel;

@Service
public class StubAutomationServiceImpl implements StubAutomationService, IStubAutomation {

	private static final Logger LOGGER = LoggerFactory.getLogger(StubAutomationServiceImpl.class);

	@Autowired
	StubAutomationDao stubAutomationDao;

	@Autowired
	StubDetailDao stubDetailDao;

	@Value("${stubauto.context}")
	private String hostName;

	// Registers new user
	@Override
	public String signUp(SignUpModel signUpModel) throws Exception {
		String STATUS = "FAILURE";
		try {

			UserAuthEntity entity = new UserAuthEntity();
			UserAuthEntity savedEntity;
			entity.setUserId(signUpModel.getUserId());
			entity.setPassword(signUpModel.getPassword());
			entity.setFirstName(signUpModel.getFirstName());
			entity.setLastName(signUpModel.getLastName());
			entity.setEmailId(signUpModel.getEmailId());

			if (stubAutomationDao.findById(signUpModel.getUserId()).isPresent()) {

				STATUS = CONFLICT;
				return STATUS;

			} else {
				savedEntity = stubAutomationDao.save(entity);

			}

			if (savedEntity != null) {
				STATUS = SUCCESS;
				LOGGER.info("User created sucessfully " + "user id:" + savedEntity.getUserId());
			}

		} catch (Exception e) {
			STATUS = FAILURE;
			throw new Exception(e);
			// LOGGER.error(e.getMessage());

		}
		return STATUS;

	}

	@Override
	public String signIn(SignUpModel signInModel) {
		String result ="FAILURE";
		try {
			if(stubAutomationDao.findById(signInModel.getUserId()).isPresent()) {
				UserAuthEntity userEntity = stubAutomationDao.findById(signInModel.getUserId()).get();
				if(userEntity.getPassword().equals(signInModel.getPassword())) {
					result = "SUCCESS";
				}
				LOGGER.info("User present" + userEntity);
			}
			
		}
		catch(Exception e) {
			LOGGER.info(e.toString());
		}
		return result;
	}

	// returns API url for the requested JSON
	@Override
	public JSONObject stubCreate(JSONObject reJsonObject) throws Exception {
		JSONObject responseJSON = new JSONObject();
		StubDetailsEntity savedEntity;
		try {

			String apiName = reJsonObject.get("apiName").toString();
			String itpr = reJsonObject.get("itpr").toString();
			String respJson = reJsonObject.getJSONObject("respJson").toString();
			String userId = reJsonObject.get("userId").toString();
			String url = "";

			if (!apiName.isEmpty() && !itpr.isEmpty() && !respJson.isEmpty() && !userId.isEmpty()) {
				StubDetailsEntity stubDetailEntity = new StubDetailsEntity();
				stubDetailEntity.setUserId(userId);
				stubDetailEntity.setApiName(apiName);
				stubDetailEntity.setItpr(itpr);
				stubDetailEntity.setRespJson(respJson);
				stubDetailEntity.setCreateDateTime(new Date());
				if (stubDetailDao.findByApiNameAndUserIdAndItpr(apiName, userId, itpr).isPresent()) {

					responseJSON.put(STATUS, API_CONFLICT);
					responseJSON.put(URL, EMPTY);
					responseJSON.put(MSG, "Requested API Already Exists");

				} else {
					savedEntity = stubDetailDao.save(stubDetailEntity);
					if (savedEntity != null) {
						url = hostName + BACKSLASH + itpr + BACKSLASH + apiName;
						responseJSON.put(STATUS, SUCCESS);
						responseJSON.put(URL, url);
						responseJSON.put(MSG, "Request Processed Successfully");

					}

				}

			} else {

				responseJSON.put(STATUS, FAILURE);
				responseJSON.put(URL, EMPTY);
				responseJSON.put(MSG, "Invalid Request Parameters");
				// LOGGER.error("");
			}

		} catch (Exception e) {
			responseJSON.put(STATUS, INTERNAL_SERVER_ERROR);
			responseJSON.put(URL, EMPTY);
			responseJSON.put(MSG, "Server Error");
			throw new Exception(e);
		}
		return responseJSON;
	}

	// Updates existing API JSON
	@Override
	public JSONObject stubUpdate(JSONObject reJsonObject) throws Exception {
		JSONObject responseJSON = new JSONObject();
		StubDetailsEntity savedEntity;
		try {

			String apiName = reJsonObject.get("apiName").toString();
			String itpr = reJsonObject.get("itpr").toString();
			String respJson = reJsonObject.get("respJson").toString();
			String userId = reJsonObject.get("userId").toString();
			String url = "";

			if (!apiName.isEmpty() && !itpr.isEmpty() && !respJson.isEmpty() && !userId.isEmpty()) {
				Optional<StubDetailsEntity> optional = stubDetailDao.findByApiNameAndUserIdAndItpr(apiName, userId,
						itpr);
				if (!optional.isPresent()) {
					responseJSON.put(STATUS, API_CONFLICT);
					responseJSON.put(URL, EMPTY);
					responseJSON.put(MSG, UPDATE_ERROR_MSG);

				} else {
					StubDetailsEntity stubDetailEntity = optional.get();
					stubDetailEntity.setRespJson(respJson);
					stubDetailEntity.setUpdateDateTime(new Date());
					savedEntity = stubDetailDao.save(stubDetailEntity);
					if (savedEntity != null) {
						url = hostName + BACKSLASH + itpr + BACKSLASH + apiName;
						responseJSON.put(STATUS, SUCCESS);
						responseJSON.put(URL, url);
						responseJSON.put(MSG, "API Updated Successfully");

					}

				}

			} else {

				responseJSON.put(STATUS, FAILURE);
				responseJSON.put(URL, EMPTY);
				responseJSON.put(MSG, "Invalid Request Parameters");
				// LOGGER.error("");
			}

		} catch (Exception e) {
			responseJSON.put(STATUS, INTERNAL_SERVER_ERROR);
			responseJSON.put(URL, EMPTY);
			responseJSON.put(MSG, "Server Error");
			throw new Exception(e);
		}
		return responseJSON;
	}

	@Override
	public String stubDelete(StubDeleteModel stubDeleteModel) {
		String STATUS = "FAILURE";
		try {

			StubDetailsEntity savedEntity;

			savedEntity = stubDetailDao.findByUserIdAndItprAndApiName(stubDeleteModel.getUserId(),stubDeleteModel.getItpr(), stubDeleteModel.getApiName());

			if(null!=savedEntity) 
			{				
				stubDetailDao.delete(savedEntity);
				STATUS = SUCCESS;
			}else 
			{

				STATUS = NOTPRESENT;
			}

		} catch (Exception e) {
			e.printStackTrace();
			STATUS = FAILURE;
		}
		return STATUS;
	}

	@Override
	public String getStub(String itprName, String apiName) {
		StubDetailsEntity s = new StubDetailsEntity();
		String respJson = "";
		try {
			if(stubDetailDao.findByItprAndApiName(itprName, apiName) != null ) {
				s =  stubDetailDao.findByItprAndApiName(itprName, apiName);
				respJson = s.getRespJson();
				System.out.println("respJson="+respJson);
			}
		}
		catch(Exception e) {
			LOGGER.info(e.toString());
		}
		return respJson;
	}

	@Override
	public List<StubDetailsModel> getStubDetails(SignUpModel userDetails) {
		List<StubDetailsModel> stubList = new ArrayList<StubDetailsModel>();
		StubDetailsModel stubDetailsModel=null;
		try {
			List<StubDetailsEntity> entityList=stubDetailDao.findByUserId(userDetails.getUserId());
			if(entityList != null ) {
				for(StubDetailsEntity stubDetailEntity:entityList) {
					stubDetailsModel= new StubDetailsModel();
					stubDetailsModel.setApiName(stubDetailEntity.getApiName());
					if(null!=stubDetailEntity.getCreateDateTime())
						stubDetailsModel.setCreateDate(stubDetailEntity.getCreateDateTime().toString());
					stubDetailsModel.setItpr(stubDetailEntity.getItpr());
					//stubDetailsModel.setResJson(stubDetailEntity.getRespJson());
					if(null!=stubDetailEntity.getUpdateDateTime())
					stubDetailsModel.setUpdateDate(stubDetailEntity.getUpdateDateTime().toString());
					stubDetailsModel.setUserId(stubDetailEntity.getUserId());
					stubDetailsModel.setUrl(hostName + BACKSLASH + stubDetailEntity.getItpr() + BACKSLASH + stubDetailEntity.getApiName());					
					stubList.add(stubDetailsModel);
				}
			}
		}
		catch(Exception e) {
			LOGGER.info(e.toString());
		}
		return stubList;
	}

	
}
