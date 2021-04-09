package com.example.demo.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService 를 참조하여 작성
 * @author kate
 *
 */
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	private static final Logger logger = LogManager.getLogger(CustomOAuth2UserService.class);
	
	private static final String MISSING_USER_INFO_ERROR_CODE = "missing_redirect_uri_access_code";

	@Autowired
	private OAuth2UserAttribute oauth2UserAttribute;
	
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
		
		//사용자 정보 중 사용자 아이디로 삼을 key
		String userNameAttributeName = OAuth2UserAttribute.USER_ID;
		

		Map<String, Object> attributes = null;
				
		if (resourceServerUri != null && !"".equals(resourceServerUri) 
				&& accessToken != null && !"".equals(accessToken)) {
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.set("Authorization", "Bearer " + accessToken);

			// 헤더에만 접근 코드를 넣어서 전송하므로 파라미터로 넘길 값은 없다.
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());			
			
			try {
				
				// 리소스 서버에게 사용자 정보 요청
				String response = restTemplate.postForObject(resourceServerUri, request, String.class);				
				
				if (logger.isDebugEnabled()) {
					logger.debug(response);
				}
				
				//TODO 권한 정보도 넘겨야 할듯?
				attributes = oauth2UserAttribute.getOAuth2UserAttributes(clientRegistrationId, response);
				
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
			OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_INFO_ERROR_CODE,
					"Missing required redirect uri or access token for Client Registration: "
							+ userRequest.getClientRegistration().getRegistrationId(),
					null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}
				
		return user;
		
	}

}
