package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity(debug=false)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {	
		
		 http
		 .authorizeRequests()
		     // 첫번째로 만나는 패턴이 일치하면 적용된다.
		     //.antMatchers("/resources/**").permitAll()
		     .anyRequest().authenticated()
		     .and()
         .formLogin()
             .defaultSuccessUrl("/main.do", false); // 로그인 성공 후 이동하는 곳 true=성공하면 항상 정해진 페이지로 이동. false=처음 요청을 처리
		
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();		
		auth.inMemoryAuthentication()
		    .withUser("user")
		    .password(encoder.encode("1234"))
		    .roles("USER");
	}

	
	

}
