package com.example.demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RestHomeController {
	
	@GetMapping(value="/test/{value}")
    public void test(@PathVariable String value, HttpServletResponse resp) throws IOException {

		System.out.println(this.getClass().getName() + "=" + value);
    }

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exception(Exception e) {		
		return new ResponseEntity<String>("TEST Failed!", HttpStatus.OK);
	}

}
