package kr.green.sga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TmpController {
	
	@RequestMapping(value = "/hello")
	@ResponseBody
    public String firstPage() {
        return "Hello. you have valid JWT (JSon Web Token)!";
	}
	
	@RequestMapping(value = "/")
	public String login() {
		return "/login-form";
	}
}
