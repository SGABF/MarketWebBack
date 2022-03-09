package kr.green.sga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.green.sga.service.UserService;
import kr.green.sga.vo.TestVO;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String rootGET() {
		return "/";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String rootPOST() {
		return "/";
	}
	
}
