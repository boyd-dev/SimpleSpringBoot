package com.example.demo;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MyStartedEvent implements ApplicationListener<ApplicationStartedEvent> {

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {		
		System.out.println("Hello, World! from ApplicationStartedEvent");		
	}	


}
