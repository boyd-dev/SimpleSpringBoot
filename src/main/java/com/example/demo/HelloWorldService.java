package com.example.demo;

import org.springframework.stereotype.Service;

@Service(value = "helloWorldService")
public class HelloWorldService implements IHelloWorldService {

	@Override
	public String sayHello() {
		return "Hello, World! from HelloWorldService bean";
	}
	

}
