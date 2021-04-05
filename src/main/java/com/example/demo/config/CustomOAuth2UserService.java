package com.example.demo.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.demo.controller.HomeController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService 를 참조하여 작성
 * @author song
 *
 */
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	private static final Logger logger = LogManager.getLogger(HomeController.class);
	
	private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";
	
	// 사용자 정보는 이 세가지 정보만 참조하기로 한다.
	private static final String USER_ID = "userId";
	private static final String USER_NAME = "userName";
	private static final String USER_EMAIL = "userEmail";

	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {	
		
		String clientRegistrationId = userRequest.getClientRegistration().getRegistrationId();
		String resourceServerUri = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
		String accessToken = userRequest.getAccessToken().getTokenValue();		

//		if (logger.isDebugEnabled()) {
//			logger.debug(accessToken);
//		}	
		
		OAuth2User user = null;
		
		
		//TODO 시큐리티에서 제공하는 기본 권한 형식은 ROLE_XXX
		// 인증에 성공한 사용자는 ROLE_USER 권한을 부여하기로 하자.
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		Map<String, Object> attributes = null;
		
		//사용자 정보 중 사용자 아이디로 삼을 key
		String userNameAttributeName = USER_ID;
		
				
		if (resourceServerUri != null && !"".equals(resourceServerUri) 
				&& accessToken != null && !"".equals(accessToken)) {
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.set("Authorization", "Bearer " + accessToken);

			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());			
			
			try {
								
				String response = restTemplate.postForObject(resourceServerUri, request, String.class);				
				
//				if (logger.isDebugEnabled()) {
//					logger.debug(response);
//				}
				
				// 사용자 속성을 새로 구성해본다.
				attributes = new HashMap<>();
				if ("naver".equals(clientRegistrationId)) {
					
					JsonNode node = new ObjectMapper().readTree(response);					
					
					attributes.put(USER_ID, node.get("response").get("id"));
					attributes.put(USER_NAME, ""); //TODO 네이버는 이름이 전달되지 않는다?
					attributes.put(USER_EMAIL, node.get("response").get("email"));
					
				} else if ("google".equals(clientRegistrationId)){
					
					ObjectMapper mapper = new ObjectMapper();
					Map<String, String> responseMap = mapper.readValue(response, new TypeReference<Map<String, String>>() {});
					
					attributes.put(USER_ID, responseMap.get("sub"));
					attributes.put(USER_NAME, responseMap.get("family_name") + " " + responseMap.get("given_name"));
					attributes.put(USER_EMAIL, responseMap.get("email"));				
				}				
				
				if (logger.isDebugEnabled()) {
					logger.debug(attributes.toString());
				}
				
				
			} catch (OAuth2AuthorizationException ex) {				
				OAuth2Error oauth2Error = ex.getError();
				throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
				
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			user = new DefaultOAuth2User(authorities, attributes, userNameAttributeName);
			
		} else {
			OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_INFO_URI_ERROR_CODE,
					"Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: "
							+ userRequest.getClientRegistration().getRegistrationId(),
					null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}
				
		return user;
		
	}

}
