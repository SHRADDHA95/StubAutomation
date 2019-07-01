package com.genpact.app.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.genpact.app.model.SignUpModel;
import com.genpact.app.model.StubDeleteModel;
import com.genpact.app.model.StubDetailsModel;
public interface StubAutomationApi {

	@CrossOrigin
	@PostMapping(value = "/signUp", produces = { "application/json" }, consumes = { "application/json" })
	public ResponseEntity<String> signUp(@RequestBody SignUpModel signUpModel);

	@CrossOrigin
	@PostMapping(value="/signIn", produces= {"application/json"}, consumes= {"application/json"})
	public ResponseEntity<String> signIn(@RequestBody SignUpModel signInModel);

	@CrossOrigin
	@PostMapping(value = "/stubCreate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = { "application/json" })
	public ResponseEntity<String> stubCreate(@RequestBody String requestJSON) throws Exception;

	@CrossOrigin
	@PostMapping(value = "/stubUpdate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = { "application/json" })
	public ResponseEntity<String> stubUpdate(@RequestBody String requestJSON) throws Exception;
	
	@CrossOrigin
	@PostMapping(value="/stubDelete", produces= {"application/json"}, consumes= {"application/json"})
	public ResponseEntity<String> stubDelete(@RequestBody StubDeleteModel stubDeleteModel);
	
	@CrossOrigin
	@GetMapping(value="/getStub/{itprName}/{apiName}", produces= {"application/json"})
	public ResponseEntity<String> getStub(@PathVariable String itprName, @PathVariable String apiName) throws Exception;
	
	@CrossOrigin
	@PostMapping(value="/stubDetails", produces= {"application/json"}, consumes= {"application/json"})
	public ResponseEntity<List<StubDetailsModel>> getStubDetails(@RequestBody SignUpModel userDetails) throws Exception;

	@CrossOrigin
	@PostMapping(value="/getStub/{itprName}/{apiName}", produces= {"application/json"})
	public ResponseEntity<String> getStubPost(@PathVariable String itprName, @PathVariable String apiName) throws Exception;
}
