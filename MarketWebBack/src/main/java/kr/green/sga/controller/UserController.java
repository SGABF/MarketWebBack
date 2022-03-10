package kr.green.sga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kr.green.sga.dao.UserDAO;
import kr.green.sga.service.UserService;
import kr.green.sga.vo.UserVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDAO userDAO;

	@RequestMapping(value = "/")
	public String root() throws Exception {
		UserVO userVO = null;
		userService.insertUser(userVO);
		System.out.println(userVO);
		return "/";
	}
	
	@RequestMapping(value = "/idCheck", method = RequestMethod.GET)
	public String idCheckGET(@RequestParam(required = false) String user_id) {
		// 추후 업데이트 예정 "잘못된 접근입니다."
		return "";
	}

	@RequestMapping(value = "/idCheck", method = RequestMethod.POST)
	public String idCheckPOST(@RequestParam(required = false) String user_id) {
		String userids[] = "chlehddh,admin,root,master,webmaster,administrator".split(","); // 금지 아이디 목록
		int count = 0;
		for (String id : userids) {
			if (user_id.equals(id)) {
				count = 1;
				break;
			}
		}
		if (count == 0) {
			count = userService.idCheck(user_id);
		}
		return count + "";
	}
	
	@RequestMapping(value = "/BannedUser", method = RequestMethod.GET)
	public String BannedUserGET(@RequestParam(required = false) int user_idx) {
		// 추후 업데이트 예정 "잘못된 접근입니다."
		return "";
	}
	
	@RequestMapping(value = "/BannedUser", method = RequestMethod.POST)
	public String BannedUserPOST(@RequestParam(required = false) int user_idx) {
		UserVO userVO = userDAO.selectByIdx(user_idx);
		userService.BannedUser(userVO);
		return userVO.toString();
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String rootGET() {
		System.out.println("get");
		return "get";
	}

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public String rootPOST() {
		System.out.println("post");
		return "post";
	}

}
