package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class RestHomeController {
	
	private static final Logger logger = LogManager.getLogger(RestHomeController.class);
	
	//API 호출 테스트
	@RequestMapping(value="/api/hello")
    public ResponseEntity<String> test(HttpServletResponse resp, Principal principal) throws IOException {
		
		String nickname = "Annonymous";
		
		if (!principal.getName().isEmpty()) {
			OAuth2User user = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getDetails();
			nickname = user.getAttribute("nickname").toString();
			
			if (logger.isDebugEnabled()) {
				logger.debug(nickname);
			}		
		}				
				
		return ResponseEntity.ok(nickname);
    }

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exception(Exception e) {		
		return new ResponseEntity<String>("/api/hello/ Failed!", HttpStatus.OK);
	}

}
