package kr.green.sga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@RestController
@RequestMapping(value = "/notToken")
public class NotTokenController {

	@Autowired
	private UserService userService;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping(value = "/login")
	public String LoginPage() {
		return "/login-form";
	}

	@RequestMapping(value = "/test")
	public String testPage() {
		return "/index";
	}

	@RequestMapping(value = "/insertUser", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public String insertUserGET(@RequestParam(required = false) UserVO userVO) {
		log.info("UserController-insertUserGET 호출 : " + userVO);
		// 추후 업데이트 예정 "잘못된 접근입니다."
		return "";
	}

	@RequestMapping(value = "/insertUser", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String insertUserPOST(@RequestBody UserVO userVO) throws JsonProcessingException {
		log.info("UserController-insertUserPOST 호출 : " + userVO);
//		userVO.setUser_password(UUID.randomUUID().toString());
		userVO.setUser_password(bCryptPasswordEncoder.encode(userVO.getUser_password())); // 비번 암호화
		userService.insertUser(userVO); // DB에 저장
		log.info("UserController-insertUserPOST 리턴 : \n userVO : " + userVO);
		return mapper.writeValueAsString(userVO);
	}

	@RequestMapping(value = "/idCheck", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public String idCheckGET(@RequestParam(required = false) String user_id) {
		log.info("NotTokenController-idCheckGET 호출 : " + user_id);
		// 추후 업데이트 예정 "잘못된 접근입니다."
		return "";
	}

	@RequestMapping(value = "/idCheck", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String idCheckPOST(@RequestParam(required = false) String user_id) {
		// 0:없음/사용가능 1:있음/사용불가
		log.info("NotTokenController-idCheckPOST 호출 : " + user_id);
		String userids[] = "test,admin,root,master,webmaster,administrator".split(","); // 금지 아이디 목록
		int count = 0;
		int dbcount = 0;
		for (String id : userids) {
			if (user_id.equals(id)) {
				count = 1;
				if (count == 1) {
					log.info("NotTokenController-idCheckPOST count 리턴 : " + count + "_사용불가");
					return count + "";
				}
			}
		}
		if (count == 0) {
			dbcount = userService.idCheck(user_id);
			if (dbcount == 0) {
				log.info("NotTokenController-idCheckPOST dbcount 리턴 : " + dbcount + "_사용가능");
			} else {
				log.info("NotTokenController-idCheckPOST dbcount 리턴 : " + dbcount + "_사용불가");
			}
		}
		return dbcount + "";
	}

	@RequestMapping(value = "/findIdPOST", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String findIdPOST(@RequestParam(required = false) String user_name, String user_email)
			throws JsonProcessingException {
		log.info("NotTokenController-findIdPOST 호출 : " + "이름:" + user_name + ", 이메일:" + user_email);
		String user_id = "";
		user_id = userService.findId(user_name, user_email);
		if (user_id != null) {
			log.info("NotTokenController-findIdPOST 고객 아이디 리턴 : " + user_id);
		} else {
			log.info("NotTokenController-findIdPOST 고객 아이디 리턴 : " + user_id);
		}
		return mapper.writeValueAsString(user_id);
	}

	@RequestMapping(value = "/findPwPOST", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String findPwPOST(@RequestParam(required = false) String user_id, String user_email, String user_name)
			throws JsonProcessingException {
		log.info("NotTokenController-findPwPOST 호출 : " + user_id + ", " + user_email + ", " + user_name);
		int count = 0;
		String new_password = "";
		UserVO dbVO = null;
		count = userService.findPw(user_id, user_email, user_name);
		dbVO = userService.selectUserId(user_id);
		if(count==1) {
			new_password = userService.makePassword(10);
			log.info("NotTokenController-findPwPOST-임시 비밀번호 생성 : " + new_password);
			dbVO.setUser_password(new_password);
			userService.updatePassword(dbVO);
			log.info("NotTokenController-findPwPOST 리턴 : " + new_password);
		} else {
			log.info("NotTokenController-findPwPOST 고객 정보 없음.");
		}
		return mapper.writeValueAsString(new_password);
	}
	
	@RequestMapping(value = "/loginPOST", method = RequestMethod.POST)
	public String loginPOST(@RequestBody UserVO userVO) throws JsonProcessingException {
		log.info("NotTokenController-loginPOST 호출 : 유저Json_" + userVO);
		UserVO dbVO = userService.selectUserId(userVO.getUser_id());
		if(userVO != null) {
			dbVO = userService.selectByUserId(userVO);
			if(dbVO == null) {
				log.info("NotTokenController-loginPOST 리턴 : 사용자 정보 없음.");
			}
		}
		log.info("NotTokenController-loginPOST 리턴 : dbVO.getUser_id()_" + dbVO.getUser_id());
		return dbVO.getUser_id();
	}
}
