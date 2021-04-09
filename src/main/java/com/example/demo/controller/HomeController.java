package com.example.demo.controller;


import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {
	
	private static final Logger logger = LogManager.getLogger(HomeController.class);
	
	@Deprecated
	@RequestMapping(value="/main.do")
    public String mainPage(HttpServletRequest req, HttpServletResponse resp, OAuth2AuthenticationToken auth, Principal principal) {
				
		if (principal != null) {
			logger.debug(principal.toString());
		}
		
		return "main.html";
    }
	
	// 로그인 페이지
	@GetMapping(value="/oauth2Login")
    public String loginPage() {
			
		return "index.html";
    }
	
		

}
