package com.example.demo;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
	
	 @Autowired
	 @Qualifier("helloWorld")
	 private String s;
	
	 
	@Test
	void contextLoads() {
		assertThat(s).isEqualTo("Hello, World! from helloWorld bean");
		 
	}

}
