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
	
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {"classpath:/static/", "classpath:/public/"};

	
	// 정적 리소스는 보통 제한없이(로그인이 필요없는) 서비스되도록 설정한다.
	// 정적 리소스의 디폴트 루트는 /resources/static이다. 
	// 예를 들어 /resources/static/main.html은 http://localhost:8080/main.html으로 볼 수 있다.
	// 시큐리티가 적용되면 필터에 의해 모두 차단되므로 필터 예외 설정을 해주어야 한다.
	// .antMatchers("/static/**").permitAll()

	// 요청된 URL 경로와 물리적인 저장 경로를 매핑하는 역할
	// TODO 명확한 의미는 아직?
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {	
		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);	
	}
	
		
	// 컨트롤러 작성 없이(모델 없이, 아마도 정적 리소스들) 특정 뷰로 이동하기 
//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/").setViewName("index.html");		
//	}
	
	
	// 원래는 JSP와 같은 뷰 처리를 지원하기 위해 설정하지만 스프링 부트에서는 JSP를 권장하지 않는다.
	// 스프링 부트에서 JSP를 쓰지 않는한 별도로 설정할 이유는 없다.
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setViewNames(new String[] {"*.htm", "*.html"}); //뷰 페이지의 패턴을 제한
		return resolver;
	}
	  
	
	// 스프링 부트에서 서블릿 등록하기
	// ServletRegistrationBean을 등록
//	@Bean
//	public ServletRegistrationBean<LoginServlet> servletRegistrationBean(){
//		ServletRegistrationBean<LoginServlet> bean = new ServletRegistrationBean<>(new LoginServlet());
//		bean.addUrlMappings("/socialLogin");
//		return bean;
//	}
	
	
}
