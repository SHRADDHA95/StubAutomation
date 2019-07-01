package com.genpact.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.genpact.app.entity.StubDetailsEntity;
import com.genpact.app.entity.UserAuthEntity;

@Repository
public interface StubAutomationDao extends JpaRepository<UserAuthEntity, String>{
	

	
}
