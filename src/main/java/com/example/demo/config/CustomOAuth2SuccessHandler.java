package com.example.demo.config;

import java.io.IOException;

import javax.servlet.ServletException;
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

import com.example.demo.utils.CookieUtils;
import com.example.demo.utils.JwtUtils;

/**
 * OAuth2의 인증 공급자로부터 인증이 성공한 후 취득한 사용자 정보를 처리하는 핸들러 
 * 이 핸들러에서 다시 JWT를 발급한다
 * @author kate
 *
 */

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	private static final Logger logger = LogManager.getLogger(CustomOAuth2SuccessHandler.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private CookieUtils cookieUtils;
	

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		if (logger.isDebugEnabled()) {
			logger.debug(authentication.getPrincipal());
		}
		
		
		String frontendAppEntryPage = env.getProperty("frontend-app.entry");
				
		String jwt = jwtUtils.generateToken((DefaultOAuth2User) authentication.getPrincipal());
		
		//TODO 고유 ID와 권한을 DB에 저장하는 것이 어떨지? 권한은 리스트형이다.
		// 
		//authentication.getAuthorities()
		
		
		// JWT 토큰을 생성하여 쿠키에 저장
		response.addCookie(cookieUtils.generateJwtHttpOnlyCookie(env.getProperty("jwt.token-name"), jwt, Integer.valueOf(env.getProperty("jwt.expire-time")).intValue()));
		
		//TODO HttpOnly=false인 쿠키가 필요하다?
		response.addCookie(cookieUtils.generateNormalCookie(env.getProperty("jwt.token-name") + "-flag", "true", Integer.valueOf(env.getProperty("jwt.expire-time")).intValue()));
		
		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect.");
	        return;
	    }
		
		getRedirectStrategy().sendRedirect(request, response, frontendAppEntryPage);
		
	}	
	
	
}
