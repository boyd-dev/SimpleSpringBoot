package com.example.demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestHomeController {
	
	//API 호출 테스트
	@PostMapping(value="/api/hello/{value}")
    public ResponseEntity<String> test(@PathVariable String value, HttpServletResponse resp) throws IOException {

		System.out.println(this.getClass().getName() + "=" + value);		
		return ResponseEntity.ok(this.getClass().getName() + "=" + value);
    }

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exception(Exception e) {		
		return new ResponseEntity<String>("TEST Failed!", HttpStatus.OK);
	}

}
