package com.example.demo.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
	
	
	public Cookie generateCookie(String name, String value, String path, boolean httpOnly, int maxAge, String domain) {
		
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(path);
		cookie.setHttpOnly(httpOnly);
		cookie.setMaxAge(maxAge); //sec
		if (domain != null) {
			cookie.setDomain(domain);
		}		
		
		return cookie;		
	}
	
    public Cookie generateJwtHttpOnlyCookie(String name, String value, int maxAge) {		
    	Cookie cookieJwt = generateCookie(name, value, "/", true, maxAge, null);
		return cookieJwt;		
	}
    
    public Cookie generateNormalCookie(String name, String value, int maxAge) {		
    	Cookie cookieJwt = generateCookie(name, value, "/", false, maxAge, null);
		return cookieJwt;		
	}
    
    public Cookie generateRemoveJwtCookie(String name, String value) {		
    	Cookie cookieJwt = generateCookie(name, "", "/", true, 0, null);
		return cookieJwt;
	}
    
    
    public String getcookieValue(HttpServletRequest request, String key) {
		
    	String value = "";
		 
		if (request.getCookies() != null ) {
			
			for (Cookie cookie : request.getCookies()){
				if (cookie.getName().equals(key)) {
					value = cookie.getValue();
					break;
				}
			}
		 }
		 
		 return value;
	}
	
}
