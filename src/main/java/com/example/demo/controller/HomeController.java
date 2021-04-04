package com.example.demo.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;


@Controller
public class HomeController {
	

	@Autowired
	private Environment env;


	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;
	
	@RequestMapping(value="/main.do")
    public String mainPage(HttpServletRequest req, HttpServletResponse resp, OAuth2AuthenticationToken auth) {
		
		OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(auth.getAuthorizedClientRegistrationId(), auth.getName());
		
		System.out.println(client.getAccessToken().getTokenValue());
		
		// 리소스 서버에서 사용자 정보 가져오기
		System.out.println(this.getUserInfo(client));
		
		
		return "main.html";
    }
	
	// 로그인 페이지
	@RequestMapping(value="/oauth2Login", method = RequestMethod.GET)
    public String loginPage() {
		
		System.out.println(env.getProperty("myprops.name.age"));		
		return "index.html";
    }
	
	private String getUserInfo(OAuth2AuthorizedClient client) {
		
		String resourceServerUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
		String accessToken = client.getAccessToken().getTokenValue();		

		String response = "";
				
		if (resourceServerUri != null && !"".equals(resourceServerUri) && accessToken != null && !"".equals(accessToken)) {
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.set("Authorization", "Bearer " + accessToken);

			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
			RestTemplate restTemplate = new RestTemplate();
			response = restTemplate.postForObject(resourceServerUri, request, String.class);

		}
		//TODO 예외발생시 처리
		//
		
		return response;
	}
	

}
