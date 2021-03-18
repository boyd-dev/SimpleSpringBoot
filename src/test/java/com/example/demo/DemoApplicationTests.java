package com.example.demo;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
	
	 @Autowired
	 @Qualifier("helloWorldService")
	 private IHelloWorldService helloWorld;
	
	 
	@Test
	void contextLoads() {
		assertThat(helloWorld.sayHello()).isEqualTo("Hello, World! from HelloWorldService bean");
		 
	}

}
