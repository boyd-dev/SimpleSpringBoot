package com.example.demo.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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
		
		//TODO 리소스 서버에서 사용자 정보 가져오기!
		//
		
		return "main.html";
    }
	
	// 로그인 페이지
	@RequestMapping(value="/oauth2Login", method = RequestMethod.GET)
    public String loginPage() {
		
		System.out.println(env.getProperty("myprops.name.age"));
		
		return "index.html";
    }
	

}
