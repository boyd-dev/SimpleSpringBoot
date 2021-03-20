package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.SpringVersion;


// @SpringBootApplication은 @Configuration, @EnableAutoConfiguration, @ComponentScan 으로 구성
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// System.out.println("Hello, World!");
		System.out.println(SpringVersion.getVersion());
		
		// SpringApplication.run
		// ApplicationContext 컨테이너를 만들고 빈을 생성한다.
		// 웹 애플리케이션의 경우 내장 톰캣을 실행한다. http://localhost:8080 으로 접속
		ApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
		
		//String str = (String) ctx.getBean("helloWorld");
		
		//
		IHelloWorldService hsb = (IHelloWorldService) ctx.getBean("helloWorldService");
		String str = hsb.sayHello();
		
		System.out.println(str);
		
	}
	
	// 스프링 빈 정의하기
	@Bean(name = "helloWorld")
	public String HelloWorld(){
		return "Hello, World! from helloWorld bean";
	}

	

}
