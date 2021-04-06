package com.example.demo.controller;


import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	
	private static final Logger logger = LogManager.getLogger(HomeController.class);
	
	@Autowired
	private Environment env;
	
//	@Autowired
//	private OAuth2AuthorizedClientService authorizedClientService;
	
	@RequestMapping(value="/main.do")
    public String mainPage(HttpServletRequest req, HttpServletResponse resp, OAuth2AuthenticationToken auth, Principal principal) {
		
		//OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(auth.getAuthorizedClientRegistrationId(), auth.getName());
	    		
		if (logger.isDebugEnabled()) {
			//logger.debug(client.getRefreshToken().getTokenValue());
			
			// 액세스 토큰으로 사용자 정보를 다시 조회할 필요가 없을 것 같다. 시큐리티에서 Principal 객체로 전달해주고 있다!
			if (principal != null) {
				logger.debug(principal.toString());
			}
		}
			
		
		return "main.html";
    }
	
	// 로그인 페이지
	@RequestMapping(value="/oauth2Login", method = RequestMethod.GET)
    public String loginPage() {
		
		System.out.println(env.getProperty("myprops.name.age"));	
		return "index.html";
    }
	
		

}
