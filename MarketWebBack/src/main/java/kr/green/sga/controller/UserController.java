package kr.green.sga.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	public String updateUserGET(@ModelAttribute UserVO userVO, Model model) throws JsonProcessingException {
		log.info("UserController-updateUserGET 호출 : " + userVO, ", " + model);
		return "";
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUserPOST(@ModelAttribute UserVO userVO, Model model) throws JsonProcessingException {
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
	public String deleteUserPOST(@ModelAttribute UserVO userVO, HttpServletRequest request, HttpServletResponse response, Model model) throws JsonProcessingException {
		UserVO dbVO = userService.selectByIdx(userVO.getUser_idx());
		log.info("UserController-deleteUserPOST 호출 : " + userVO);
		if (dbVO != null) {
			System.out.println("dbVO:\n" + dbVO);
			System.out.println("userVO:\n" + userVO);
			userService.deleteUser(userVO);
			// 시크리트의 로그인 정보도 지워줘야 한다.
			// 인증정보를 얻어낸다.
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			// 인증정보가 있다면
			if (authentication != null) {
				// 로그아웃을 시킨다.
				new SecurityContextLogoutHandler().logout(request, response, authentication);
			}
			model.addAttribute("msg", "정상적으로 탈퇴 되었습니다.");
			log.info("UserController-deleteUserPOST 리턴 : " + dbVO);
			return mapper.writeValueAsString(dbVO);
		} else {
			model.addAttribute("msg", "회원정보를 찾을 수 없습니다.");
			log.info("UserController-deleteUserPOST 리턴: " + mapper.writeValueAsString(dbVO));
			return "/";
		}
	}
//	
//	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
//	public String deleteUserPOST(@RequestParam(required = false) UserVO userVO, Model model) throws JsonProcessingException {
//		log.info("UserController-deleteUserPOST 호출 : " + userVO);
//		if (userVO != null) {
//			userService.deleteUser(userVO);
//			return mapper.writeValueAsString(userVO);
//		}
//		model.addAttribute("msg", "정상적으로 탈퇴 되었습니다.");
//		log.info("UserController-deleteUserPOST 리턴: " + mapper.writeValueAsString(userVO));
//		return null;
//	}

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

}
