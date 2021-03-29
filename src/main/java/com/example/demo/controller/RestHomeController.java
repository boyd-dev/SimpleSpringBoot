package com.example.demo.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestHomeController {
	
	@PostMapping(value="/user")
    public String userInfo(Model model) {

		//TODO
		return null;
    }


}
