package kr.green.sga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping(value = "/index")
	public String root() throws Exception {
		System.out.println("root");
		return "index";
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

	@RequestMapping(value = "/insertUser", method = RequestMethod.GET)
	public String insertUserGET(@RequestParam(required = false) UserVO userVO) {
		log.info("UserController-insertUserGET 호출 : " + userVO);
		// 추후 업데이트 예정 "잘못된 접근입니다."
		return "";
	}

	@RequestMapping(value = "/insertUser", method = RequestMethod.POST)
	public String insertUserPOST(@ModelAttribute UserVO userVO, Model model) throws JsonProcessingException {
		log.info("UserController-insertUserPOST 호출 : " + userVO, ", " + model);
//		userVO.setUser_password(UUID.randomUUID().toString());
		userVO.setUser_password(bCryptPasswordEncoder.encode(userVO.getUser_password())); // 비번 암호화
		userService.insertUser(userVO); // DB에 저장
		model.addAttribute("vo", userVO);
		return mapper.writeValueAsString(userVO);
	}

//	insertUser() 메서드 userVO.toString 확인용.
//	@RequestMapping(value = "/insertUser", method = RequestMethod.POST)
//	public String insertUserPOST(@ModelAttribute UserVO userVO, Model model) {
//		log.info("UserController-insertUserPOST 호출 : " + userVO, ", " + model);
////		userVO.setUser_password(UUID.randomUUID().toString());
//		userVO.setUser_password(bCryptPasswordEncoder.encode(userVO.getUser_password())); // 비번 암호화
//		userService.insertUser(userVO); // DB에 저장
//		model.addAttribute("vo", userVO);
//		return userVO.toString();
//	}
	
	@RequestMapping(value = "/updateUser", method = RequestMethod.GET)
	public String updateUserGET(@ModelAttribute UserVO userVO, Model model) throws JsonProcessingException {
		log.info("UserController-updateUserGET 호출 : " + userVO, ", " + model);
		return "";
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUserPOST(@ModelAttribute UserVO userVO, Model model) throws JsonProcessingException {
		log.info("UserController-updateUserPOST 호출 : " + userVO, ", " + model);
		UserVO dbVO = null;
		if (userVO.getUser_phone() != null) {
			
			return mapper.writeValueAsString(userVO);
		}
		log.info("UserController-updateUserPOST 리턴: " + mapper.writeValueAsString(userVO));
		return null;
	}
	
	
	

	@RequestMapping(value = "/idCheck", method = RequestMethod.GET)
	public String idCheckGET(@RequestParam(required = false) String user_id) {
		log.info("UserController-idCheck 호출 : " + user_id);
		// 추후 업데이트 예정 "잘못된 접근입니다."
		return "";
	}

	@RequestMapping(value = "/idCheck", method = RequestMethod.POST)
	public String idCheckPOST(@RequestParam(required = false) String user_id) {
		String userids[] = "admin,root,master,webmaster,administrator".split(","); // 금지 아이디 목록
		int count = 0;
		for (String id : userids) {
			if (user_id.equals(id)) {
				count = 1;
				break;
			}
		}
		UserVO userVO = userService.selectUserId(user_id);
		if (count == 0 && userVO != null) {
			count = 1;
		}
		return count + "";
	}

	@RequestMapping(value = "/BannedUser", method = RequestMethod.GET)
	public String BannedUserGET(@RequestParam(required = false) int user_idx) {
		// 추후 업데이트 예정 "잘못된 접근입니다."
		return "";
	}

	@RequestMapping(value = "/BannedUser", method = RequestMethod.POST)
	public String BannedUserPOST(@RequestParam(required = false) int user_idx) throws JsonProcessingException {
		UserVO userVO = userService.selectByIdx(user_idx);
		userService.BannedUser(userVO);
		return mapper.writeValueAsString(userVO);
	}
}
