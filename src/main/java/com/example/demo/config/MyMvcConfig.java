package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
public class MyMvcConfig implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {	
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");		
	}

	// 스프링 부트 2.4 이후부터 스프링 부트에 내장된 톰캣(embedded tomcat)에 디폴트 서블릿이 자동으로 등록되지 않는다. 
	// 디폴트 서블릿을 등록하려면 application.properties 파일에 server.servlet.register-default-servlet=true 을 설정한다.
	// 다음 설정은 war로 패키징하고 외부 톰캣에 배포할 때만 적용될 수 있을 것 같다.
	//  
	//@Override
//	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//		configurer.enable();		
//	}
		
	// 컨트롤러 작성 없이(모델 없이, 아마도 정적 리소스들) 특정 뷰로 이동하기 
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index.html");		
	}
	
	
	// 원래는 JSP와 같은 뷰 처리를 지원하기 위해 설정하지만 스프링 부트에서는 JSP를 권장하지 않는다.
	// 스프링 부트에서 JSP를 쓰지 않는한 별도로 설정할 이유는 없다.
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		//resolver.setPrefix("/"); //실제 물리적인 경로명이 아니라 URL기준으로 만들어지는 경로명 
		resolver.setViewNames(new String[] {"*.htm", "*.html"}); //뷰 페이지의 페턴을 줄 수도 있다.
		return resolver;
	}
	  
}
