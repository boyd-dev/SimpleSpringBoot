package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.demo.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug=false)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
	
	@Autowired
	private CustomLogoutSuccessHandler customLogoutSuccessHandler;
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {	
		
		 http
		 .authorizeRequests()
		     // 첫번째로 만나는 패턴이 일치하면 적용된다.
		     .antMatchers("/index.html").permitAll()
		     .antMatchers("/static/**").permitAll() // 리액트 애플리케이션에도 static 폴더가 생기므로 허용한다.
		     .antMatchers("/oauth2Login").permitAll()
		     .anyRequest().authenticated()
		     .and()
		 .csrf().disable() // 활성화하면 로그아웃시 POST 메소드로 해야 한다!
		 .oauth2Login()
		    .loginPage("/oauth2Login")
		       .redirectionEndpoint()
		          .baseUri("/oauth2/callback/*") // 디폴트는 login/oauth2/code/*
		          .and()
		       .userInfoEndpoint().userService(customOAuth2UserService())
		          .and()		          
		       .successHandler(customOAuth2SuccessHandler)
		       //.failureUrl("/main.do")		     
		       .and()		    
		    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //JWT는 세션을 이용하지 않는다
		       .and()
		    .logout()
		       .deleteCookies("JSESSIONID")
		       .logoutSuccessHandler(customLogoutSuccessHandler)
		       .and()		    
		   //TODO 필터의 위치는 어떻게 결정하지?		       
		  .addFilterBefore(jwtAuthenticationFilter(), OAuth2AuthorizationRequestRedirectFilter.class); 
		  
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
				
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();		
		auth.inMemoryAuthentication()
		    .withUser("user")
		    .password(encoder.encode("1234"))
		    .roles("USER");
	}
	
	// 이렇게 빈을 명시적으로 등록해보자!
	@Bean
	public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {		
		return new CustomOAuth2UserService();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}
	
	

}
