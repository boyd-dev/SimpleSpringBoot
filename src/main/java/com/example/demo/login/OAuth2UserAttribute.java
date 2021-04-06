package com.example.demo.login;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OAuth2UserAttribute {
	
	private static final Logger logger = LogManager.getLogger(OAuth2UserAttribute.class);
	
	// 사용자 정보는 이 세가지 정보만 참조하기로 한다.
	// key 값
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String USER_EMAIL = "userEmail";
	
	
	public Map<String, Object> getOAuth2UserAttributes(String registrationId, String response) throws JsonMappingException, JsonProcessingException {
		
		Map<String, Object> attributes = new HashMap<>();
		
		if ("naver".equals(registrationId)) {
			
			//응답 형태
			//애플리케이션 등록시 제공정보 항목에 따라 차이가 있음
			//{"resultcode":"00","message":"success","response":{"id":"55XXXXXX","gender":"M","email":"foo@naver.com"}}
			
			JsonNode node = new ObjectMapper().readTree(response);
			
			attributes.put(USER_ID, node.get("response").get("id"));
			attributes.put(USER_NAME, node.get("response").get("nickname"));
			attributes.put(USER_EMAIL, node.get("response").get("email"));
			
		} else if ("google".equals(registrationId)){
			
			//응답형태
			/*
			{
			  "sub": "11XXXXXXXXXXXXXXXXXXX",
			  "name": "Foo Robert",
			  "given_name": "Foo",
			  "family_name": "Robert",
			  "picture": "https://lh6.googleusercontent.com/XXX/XXX/photo.jpg",
			  "email": "foo@gmail.com",
			  "email_verified": true,
			  "locale": "ko"
			}
            */
		
			
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> responseMap = mapper.readValue(response, new TypeReference<Map<String, String>>() {});
			
			attributes.put(USER_ID, responseMap.get("sub"));
			attributes.put(USER_NAME, responseMap.get("family_name") + " " + responseMap.get("given_name"));
			attributes.put(USER_EMAIL, responseMap.get("email"));				
		}
		
		
		if (logger.isDebugEnabled()) {
			logger.debug(attributes);
		}
		
		
		return attributes;
		
	}
	

}
