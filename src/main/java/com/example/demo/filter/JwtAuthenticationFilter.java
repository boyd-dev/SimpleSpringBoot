package com.example.demo.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.utils.CookieUtils;
import com.example.demo.utils.JwtUtils;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);
	
	// API 호출은 전부 JWT를 확인한다.
	private RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/**");


	@Resource
	private Environment env;
	
	@Autowired
	private CookieUtils cookieUtils;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// permitAll이라고 해서 필터가 아예 적용되지 않도록 하는 것이 아니다. 필터는 항상 통과한다.
		// 
		if (requestMatcher.matches(request)) {
			
			String jwt = cookieUtils.getcookieValue(request, env.getProperty("jwt.token-name"));
				
			if (logger.isDebugEnabled()) {
				logger.debug(jwt);		
			}
				
			if (jwt.isEmpty()) {
				
				if (logger.isDebugEnabled()) {
					logger.debug("JWT is empty");		
				}
				
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);				
					
			} else {
					
				// JWT가 있다면 유효한지 검사
				if (!jwtUtils.isValidateToken(jwt)) {
					if (logger.isDebugEnabled()) {
						logger.debug("JWT is invalid");		
					}
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);				
				}			
				
				// JWT가 만료되었는지 확인
				//TODO 쿠키가 만료되는 경우는 아예 JWT가 없어졌을 것이므로 이 조건이 필요가 있을까?
				if (jwtUtils.isTokenExpired(jwt)) {
					if (logger.isDebugEnabled()) {
						logger.debug("JWT is expired");		
					}
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);					
				}
					
				Map<String, Object> attributes = jwtUtils.getBobyFromToken(jwt);
				
				if (logger.isDebugEnabled()) {
					logger.debug("JWT::" + attributes);					
				}
				
				
				// JWT로부터 사용자 정보를 추출하여 인증 정보를 만든 후 SecurityContext에 넣는다.
				// 결과적으로 처음 인증 공급자로부터 받은 정보를 JWT에 넣었고 쿠키를 통해 다시 받으면 
				// 그것을 OAuth2User로 다시 복원해서 시큐리티의 인증정보에 넣어야 시큐리티의 필터들을 통과할 수 있다. 

				// JWT를 만들 때 사용자 고유 ID로 삼았던 필드명과 맞춘다.
				String userNameAttributeName = "sub";
				
				//TODO
				// 권한은 JWT에 저장하지 않기로 했는데?
				List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
				
				OAuth2User userDetails = new DefaultOAuth2User(authorities, attributes, userNameAttributeName);				
				OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(userDetails, authorities, userNameAttributeName);
                
				authentication.setDetails(userDetails);
                SecurityContextHolder.getContext().setAuthentication(authentication); 
			}
			
	     }		
		
		filterChain.doFilter(request, response);		
	}

}

