package com.example.demo.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;

import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	@Autowired
	private Environment env;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		String frontendAppEntryPage = "/index.html";
		
		Cookie cookieJwt = new Cookie(env.getProperty("jwt.token-name"), "");
		cookieJwt.setPath("/");
		cookieJwt.setHttpOnly(true);
		cookieJwt.setMaxAge(0);
		response.addCookie(cookieJwt);
		
		Cookie cookie = new Cookie(env.getProperty("jwt.token-name") + "-signin", "");
		cookie.setPath("/");
		cookie.setHttpOnly(false);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		
		getRedirectStrategy().sendRedirect(request, response, frontendAppEntryPage);
		
	}

}
