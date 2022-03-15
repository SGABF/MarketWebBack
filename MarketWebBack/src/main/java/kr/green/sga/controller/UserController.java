package kr.green.sga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.green.sga.service.UserService;
import kr.green.sga.vo.UserVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RestController
public class UserController {

	@Autowired
	private UserService userService;

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

	@RequestMapping(value = "/updateUser", method = RequestMethod.GET)
	public String updateUserGET(@RequestBody UserVO userVO, Model model) throws JsonProcessingException {
		log.info("UserController-updateUserGET 호출 : " + userVO, ", " + model);
		return "";
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUserPOST(@ModelAttribute UserVO userVO, Model model) throws JsonProcessingException {
		// selectuserid
		log.info("UserController-updateUserPOST 호출 : " + userVO, ", " + model);
		if (userVO.getUser_phone() != null) {
			userVO.setUser_password(bCryptPasswordEncoder.encode(userVO.getUser_password())); // 비번 암호화
			userService.updateUser(userVO);
			return mapper.writeValueAsString(userVO);
		}
		log.info("UserController-updateUserPOST 리턴: " + mapper.writeValueAsString(userVO));
		return null;
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	public String deleteUserGET(@RequestParam(required = false) UserVO userVO, Model model)
			throws JsonProcessingException {
		log.info("UserController-deleteUserGET 호출 : " + userVO);
		return "";
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public String deleteUserPOST(@RequestParam(required = false) String user_id, Model model) throws JsonProcessingException {
		log.info("UserController-deleteUserPOST 호출 : user_id " + user_id);
		UserVO dbVO = userService.selectUserId(user_id);
		if (dbVO != null) {
			userService.deleteUser(dbVO);
			model.addAttribute("msg", "정상적으로 탈퇴 되었습니다.");
			log.info("UserController-deleteUserPOST 리턴 : " + dbVO);
			return mapper.writeValueAsString(dbVO);
		} else {
			model.addAttribute("msg", "회원정보를 찾을 수 없습니다.");
			log.info("UserController-deleteUserPOST 리턴 : 회원정보 못찾음_" + mapper.writeValueAsString(dbVO));
			return "/";
		}
	}

	@RequestMapping(value = "/BannedUser", method = RequestMethod.GET)
	public String BannedUserGET(@RequestParam(required = false) int user_idx) {
		// 추후 업데이트 예정 "잘못된 접근입니다."
		return "";
	}

	@RequestMapping(value = "/BannedUser", method = RequestMethod.POST)
	public String BannedUserPOST(@RequestParam(required = false) int user_idx) throws JsonProcessingException {
		log.info("UserController-BannedUserPOST 호출 : " + user_idx);
		UserVO userVO = userService.selectByIdx(user_idx);
		userService.BannedUser(userVO);
		return mapper.writeValueAsString(userVO);
	}

	// 비밀번호 변경
	@RequestMapping(value = "/updatePassword", method = RequestMethod.GET)
	public void updatePasswordGET(@RequestBody UserVO userVO, Model model) throws JsonProcessingException {
		log.info("UserController-updatePasswordGET 호출 : " + userVO, ", " + model);
	}

	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public void updatePasswordPOST(@ModelAttribute UserVO userVO, Model model) throws JsonProcessingException {
		log.info("UserController-updatePasswordPOST 호출 : userVO_" + userVO);
		if (userVO != null) {
			userVO.setUser_password(bCryptPasswordEncoder.encode(userVO.getUser_password())); // 비번 암호화
			userService.updatePassword(userVO);
			log.info("UserController-updatePasswordPOST 리턴: " + mapper.writeValueAsString(userVO));
		}
	}

}
