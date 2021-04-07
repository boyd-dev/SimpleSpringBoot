package com.example.demo.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("JwtAuthenticationFilter is here!");			
		}
		
		//TODO 
		//JWT로부터 사용자 정보를 추출하여 인증 정보를 만든 후 SecurityContext에 넣는다.
		
		
		filterChain.doFilter(request, response);		
	}

}

