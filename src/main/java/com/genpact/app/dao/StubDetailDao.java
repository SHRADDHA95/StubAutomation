package com.genpact.app.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.genpact.app.entity.StubDetailsEntity;

@Repository

public interface StubDetailDao extends JpaRepository<StubDetailsEntity, String> {

	Optional<StubDetailsEntity> findByApiNameAndUserIdAndItpr(String apiName, String userId, String itpr);

	StubDetailsEntity findByUserIdAndItprAndApiName(String userId, String itpr, String apiName);

	StubDetailsEntity findByItprAndApiName(String Itpr, String apiName);

	List<StubDetailsEntity> findByUserId(String userId);

}
