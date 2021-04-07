package com.example.demo.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.utils.JwtUtils;

/**
 * OAuth2의 인증 공급자로부터 인증이 성공한 후 취득한 사용자 정보를 처리하는 핸들러 
 * 이 핸들러에서 다시 JWT를 발급한다
 * @author song
 *
 */

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	private static final Logger logger = LogManager.getLogger(CustomOAuth2SuccessHandler.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		if (logger.isDebugEnabled()) {
			logger.debug(authentication.getPrincipal());
		}
		
		// 프론트엔드 애플리케이션은 어차피 SPA
		String frontendAppEntryPage = "/index.html";
				
		String jwt = jwtUtils.generateToken((DefaultOAuth2User) authentication.getPrincipal());
		
		// JWT 토큰을 생성하여 쿠키에 저장
		Cookie cookieJwt = new Cookie(env.getProperty("jwt.token-name"), jwt);
		cookieJwt.setPath("/");
		cookieJwt.setHttpOnly(true);
		cookieJwt.setMaxAge(60); //sec
		response.addCookie(cookieJwt);
		
		// TODO HttpOnly=false인 쿠키가 필요한데?
		Cookie cookie = new Cookie(env.getProperty("jwt.token-name") + "-signin", "true");
		cookie.setPath("/");
		cookie.setHttpOnly(false);
		cookie.setMaxAge(60);
		response.addCookie(cookie);
		
		
		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect.");
	        return;
	    }
		
		getRedirectStrategy().sendRedirect(request, response, frontendAppEntryPage);
		
	}	
	
	
}
