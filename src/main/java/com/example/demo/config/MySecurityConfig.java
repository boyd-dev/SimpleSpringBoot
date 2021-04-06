package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.demo.login.OAuth2UserAttribute;

@Configuration
@EnableWebSecurity(debug=false)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

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
		 .oauth2Login()  // http://localhost:8080/login 이면 디폴트 로그인 화면 표시
		    .loginPage("/oauth2Login")		     
		       .redirectionEndpoint()
		          .baseUri("/oauth2/callback/*") // 디폴트는 login/oauth2/code/*
		          .and()
		       .userInfoEndpoint().userService(customOAuth2UserService())
		          .and()
		       .defaultSuccessUrl("/main.do", true)
		       //.failureUrl("/main.do")		     
		       .and()		      
		    .logout()
		       //.clearAuthentication(true)
		       .deleteCookies("JSESSIONID")
		       //.invalidateHttpSession(true) 
		       .and();		     
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
				
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();		
		auth.inMemoryAuthentication()
		    .withUser("user")
		    .password(encoder.encode("1234"))
		    .roles("USER");
	}
	
	@Bean
	public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {		
		return new CustomOAuth2UserService();
	}

	
	@Bean
	public OAuth2UserAttribute oauth2UserAttribute() {		
		return new OAuth2UserAttribute();
	}
	
	

}
