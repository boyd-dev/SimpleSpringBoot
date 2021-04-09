package com.example.demo.utils;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
	
	private static final Logger logger = LogManager.getLogger(JwtUtils.class);
	
	@Autowired
	Environment env;
	
	
    //토큰 유효성 검사
    public Boolean isValidateToken(String token) {
    	try {
	        
    		final String subject = (String) getBobyFromToken(token).get("sub");
	        return !subject.isEmpty();
	        
    	} catch (Exception e) {
			return false;
		}
    }

    // 토큰 만료 검사
    public boolean isTokenExpired(String token) {
    	try {
	    	long exp = (Long) getBobyFromToken(token).get("exp");
	        final Date expiration = new Date(exp);
	        
	        return expiration.before(new Date());
	        
    	}catch (Exception e) {
			return false;
		}
    }
    
    // 토큰 발급
    public <T> String generateToken(T userDetails) {
    	
    	if (logger.isDebugEnabled()) {
    		logger.debug(userDetails);
    	}    	
    	
    	Map<String,Object> claim = new HashMap<>();
    	
    	if (userDetails instanceof DefaultOAuth2User) {
    		    		
    		claim.put("iss", env.getProperty("jwt.toekn-issuer"));  // 발급자
    		claim.put("sub",  ((DefaultOAuth2User) userDetails).getName()); // subject 인증 대상(고유 ID)
    		
    		claim.put("email", ((DefaultOAuth2User) userDetails).getAttributes().get("userEmail"));
    		claim.put("nickname", ((DefaultOAuth2User) userDetails).getAttributes().get("userName"));
    		
    	} 
    	//TODO 다른 타입의 사용자 정보의 경우는 나중에 생각해보자.
    	// else if () {}
    	
        
    	String secret = env.getProperty("jwt.secret");
    	int exp = Integer.valueOf(env.getProperty("jwt.expire-time"));
    	
        claim.put("iat", new Date(System.currentTimeMillis()));
        claim.put("exp", new Date(System.currentTimeMillis() + (1000 * exp))); // 최대값은 쿠키 만료시간을 고려?
        
        return Jwts.builder()
        			  .setClaims(claim)
        			  .signWith(SignatureAlgorithm.HS512, secret)
        			  .compact();
    }
    
    public Map<String,Object> getBobyFromToken(String token){
		String secret = env.getProperty("jwt.secret");
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    
    
    

}

