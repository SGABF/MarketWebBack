package kr.green.sga;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
	
	@RequestMapping(value = "/testapi", method = RequestMethod.GET)
	public String testRestApi() {
		return "index";
	}
	
	@RequestMapping(value = "/admin/testapi")
	@ResponseBody
	public void testAdminRestApi() {
		
	}
	
	@RequestMapping(value = "/main/testapi")
	@ResponseBody
	public String testMainRestApi(Principal principal) {
		return principal.getName();
	}

	@RequestMapping(value = "/login")
	public String login() {
		return "login-form";
	}
}
