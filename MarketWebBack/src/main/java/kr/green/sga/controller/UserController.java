package kr.green.sga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kr.green.sga.dao.UserDAO;
import kr.green.sga.service.UserService;
import kr.green.sga.vo.UserVO;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/")
	@ResponseBody
	public String root() throws Exception {
		UserVO userVO = null;
		userService.insertUser(userVO);
		System.out.println(userVO);
		return "/";
	}
	
	@RequestMapping(value = "/idCheck")
	public int idCheck() {
		String id = "chlehddh"; 
		return userService.idCheck(id); 
	}
	
	@RequestMapping(value = "/deleteUser")
	public String deleteUser(@ModelAttribute UserVO userVO, Model model) {
//		UserVO vo = userService.deleteUser(userVO.getUser);
		System.out.println(userVO);
//		model.addAttribute("vo", vo);
		return "deleteUser";
	}
	
	@RequestMapping(value = "/selectCountUserId")
	@ResponseBody
	public String selectCountUserId() {
		UserVO userVO = null;
		userService.insertUser(userVO);
		System.out.println(userVO);
		return "selectCountUserId";
	}
	
	@RequestMapping(value = "/selectByUsername")
	@ResponseBody
	public String selectByUsername() {
		UserVO userVO = null;
		userService.insertUser(userVO);
		System.out.println(userVO);
		return "selectByIdx";
	}
	
	@RequestMapping(value = "/selectByUserId")
	@ResponseBody
	public String selectByUserId() {
		UserVO userVO = null;
		userService.insertUser(userVO);
		System.out.println(userVO);
		return "selectByIdx";
	}
	
	@RequestMapping(value = "/updatePassword")
	@ResponseBody
	public String updatePassword() {
		UserVO userVO = null;
		userService.insertUser(userVO);
		System.out.println(userVO);
		return "selectByIdx";
	}
	
	@RequestMapping(value = "/selectUserId")
	@ResponseBody
	public String selectUserId() {
		UserVO userVO = null;
		userService.insertUser(userVO);
		System.out.println(userVO);
		return "selectUserId";
	}   

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public String rootGET() {
		System.out.println("get");
		return "get";
	}

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	@ResponseBody
	public String rootPOST() {
		System.out.println("post");
		return "post";
	}
	

}
