package kr.green.sga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping(value = "/updateUser", method = RequestMethod.GET)
	@GetMapping
	public String updateUserGET(@RequestBody UserVO userVO) throws JsonProcessingException {
		log.info("UserController-updateUserGET 호출 : " + userVO);
		return "";
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	@PostMapping
	public String updateUserPOST(@RequestBody UserVO userVO) throws JsonProcessingException {
		log.info("UserController-updateUserPOST 호출 : " + userVO);
		UserVO dbVO = userService.selectUserId(userVO.getUser_id());
		if (dbVO != null) {
			userService.updateUser(userVO);
			log.info("UserController-updateUserPOST 수정된 회원정보: " + mapper.writeValueAsString(userVO));
			return mapper.writeValueAsString(userVO);
		}
		log.info("UserController-updateUserPOST 리턴: null!!!" + mapper.writeValueAsString(userVO));
		return null;
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	@GetMapping
	public String deleteUserGET(@RequestParam(required = false) UserVO userVO)
			throws JsonProcessingException {
		log.info("UserController-deleteUserGET 호출 : " + userVO);
		return "";
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	@PostMapping
	public String deleteUserPOST(@RequestParam(required = false) String user_id) throws JsonProcessingException {
		log.info("UserController-deleteUserPOST 호출 : user_id " + user_id);
		UserVO dbVO = userService.selectUserId(user_id);
		if (dbVO != null) {
			userService.deleteUser(dbVO);
			log.info("UserController-deleteUserPOST 리턴 : " + dbVO);
			return mapper.writeValueAsString(dbVO);
		} else {
			log.info("UserController-deleteUserPOST 리턴 : 회원정보 못찾음_" + mapper.writeValueAsString(dbVO));
			return "/";
		}
	}

	@RequestMapping(value = "/BannedUser", method = RequestMethod.GET)
	@GetMapping
	public String BannedUserGET(@RequestParam(required = false) int user_idx) {
		// 추후 업데이트 예정 "잘못된 접근입니다."
		return "";
	}

	@RequestMapping(value = "/BannedUser", method = RequestMethod.POST)
	@PostMapping
	public String BannedUserPOST(@RequestParam(required = false) int user_idx) throws JsonProcessingException {
		log.info("UserController-BannedUserPOST 호출 : " + user_idx);
		UserVO userVO = userService.selectByIdx(user_idx);
		userService.BannedUser(userVO);
		return mapper.writeValueAsString(userVO);
	}

	// 비밀번호 변경
	@RequestMapping(value = "/updatePassword", method = RequestMethod.GET)
	@GetMapping
	public void updatePasswordGET(@RequestBody UserVO userVO) throws JsonProcessingException {
		log.info("UserController-updatePasswordGET 호출 : " + userVO);
	}

	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	@PostMapping
	public void updatePasswordPOST(@RequestBody UserVO userVO) throws JsonProcessingException {
		log.info("UserController-updatePasswordPOST 호출 : userVO_" + userVO);
		if (userVO != null) {
			userVO.setUser_password(bCryptPasswordEncoder.encode(userVO.getUser_password())); // 비번 암호화
			userService.updatePassword(userVO);
			log.info("UserController-updatePasswordPOST 리턴: " + mapper.writeValueAsString(userVO));
		}
	}
	
	// 비밀번호 확인
	@RequestMapping(value = "/checkPassword", method = RequestMethod.GET)
	@GetMapping
	public String checkPasswordGET(@RequestBody UserVO userVO) throws JsonProcessingException {
		// 추후 업데이트 예정 "잘못된 접근입니다."
		return "";
	}
	
	@RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
	@PostMapping
	public String checkPasswordPOST(@RequestBody UserVO userVO) throws JsonProcessingException {
		log.info("UserController-checkPasswordPOST 호출 : userVO_" + userVO);
		int count = 0;
		if (userVO != null) {
			count = userService.countCheckPassword(userVO);
			if(count != 0) {
				log.info("UserController-checkPasswordPOST 리턴(비번재확인검증성공_1): " + count);
				return mapper.writeValueAsString(count);
			} 
		}
		log.info("UserController-checkPasswordPOST 리턴 : count_" + count);
		return mapper.writeValueAsString(count);
	}

}
