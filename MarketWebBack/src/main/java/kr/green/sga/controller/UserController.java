package kr.green.sga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.green.sga.service.UserService;
import kr.green.sga.vo.UserVO;
import kr.green.sga.vo.loginVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(name = "/user")
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

	// 회원정보 수정 페이지 진입할 경우 user_id 값으로 userVO 리턴하여 보내기
	@RequestMapping(value = "/updateUserPage", method = RequestMethod.POST)
	@PostMapping
	public String updateUserPagePOST(@RequestHeader(value = "user_id") String user_id) throws JsonProcessingException {
		log.info("UserController-updateUserPagePOST 호출 : " + user_id);
		UserVO dbUserVO = null;
		if (user_id != null) {
			log.info("UserController-updateUserPagePOST 아이디 확인 " + user_id);
			dbUserVO = userService.selectUserId(user_id);
			log.info("UserController-updateUserPagePOST 조회된 회원정보 : " + dbUserVO);
		}
		log.info("UserController-updateUserPagePOST 조회된 회원정보 : " + mapper.writeValueAsString(dbUserVO));
		return mapper.writeValueAsString(dbUserVO);
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	@PostMapping
	public String updateUserPOST(@RequestBody UserVO userVO) throws JsonProcessingException {
		log.info("UserController-updateUserPOST 호출 : " + userVO);
		if (userVO != null && userVO.getUser_id() != null && userVO.getUser_name() != null
				&& userVO.getUser_email() != null) {
			userService.updateUser(userVO);
			log.info("UserController-updateUserPOST 수정된 회원정보: " + mapper.writeValueAsString(userVO));
			return "1";
		}
		log.info("UserController-updateUserPOST 수정 실패");
		return "0";
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	@GetMapping
	public String deleteUserGET(@RequestHeader(value = "user_id") String user_id) throws JsonProcessingException {
		log.info("UserController-deleteUserGET 호출 : " + user_id);
		return "";
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	@PostMapping
	public String deleteUserPOST(@RequestHeader(value = "user_id") String user_id) throws JsonProcessingException {
		log.info("UserController-deleteUserPOST 호출 : user_id " + user_id);
		UserVO dbUserVO = userService.selectUserId(user_id);
		if (dbUserVO != null) {
			userService.deleteUser(dbUserVO);
			log.info("UserController-deleteUserPOST 리턴 : 회원정보 삭제완료");
			return "1";
		} else {
			log.info("UserController-deleteUserPOST 리턴 : 회원정보 못찾음_" + mapper.writeValueAsString(dbUserVO));
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
	public String checkPasswordPOST(@RequestBody UserVO userVO, @RequestHeader(value = "user_id") String user_id)
			throws JsonProcessingException {
		log.info("UserController-checkPasswordPOST 호출 : " + user_id);
		log.info("UserController-checkPasswordPOST 호출 : " + userVO);
		int count = 0;
		if (user_id != null) {
			count = userService.countCheckPassword(user_id, userVO.getUser_password());
			if (count != 0) {
				log.info("UserController-checkPasswordPOST 리턴(비번재확인검증성공_1): " + count);
				return mapper.writeValueAsString(count);
			}
		}
		log.info("UserController-checkPasswordPOST 리턴 : count_" + count);
		return mapper.writeValueAsString(count);
	}

}
