package kr.green.sga.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.green.sga.service.BoardService;
import kr.green.sga.service.UserService;
import kr.green.sga.vo.BoardVO;
import kr.green.sga.vo.UserVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/notToken")
public class NotTokenController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BoardService boardService;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping(value = "/insertUser", method = RequestMethod.GET)
	@GetMapping
	public String insertUserGET(@RequestParam(required = false) UserVO userVO) {
		log.info("UserController-insertUserGET 호출 : " + userVO);
		// 추후 업데이트 예정 "잘못된 접근입니다."
		return "";
	}

	@RequestMapping(value = "/insertUser", method = RequestMethod.POST)
	@PostMapping
	public String insertUserPOST(@RequestBody(required = false) UserVO userVO) throws JsonProcessingException {
		log.info("UserController-insertUserPOST 호출 : " + userVO);
		if (userVO != null) {
			userVO.setUser_password(bCryptPasswordEncoder.encode(userVO.getUser_password())); // 비번 암호화
			userService.insertUser(userVO); // DB에 저장
			log.info("UserController-insertUserPOST 리턴 : \n userVO : " + userVO);
		}
		return mapper.writeValueAsString(userVO);
	}

	@RequestMapping(value = "/idCheck", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@GetMapping
	public String idCheckGET(@RequestParam(required = false) String user_id) {
		log.info("NotTokenController-idCheckGET 호출 : " + user_id);
		// 추후 업데이트 예정 "잘못된 접근입니다."
		return "";
	}

	@RequestMapping(value = "/idCheck", method = RequestMethod.POST)
	@PostMapping
	public String idCheckPOST(@RequestBody(required = false) UserVO userVO) throws JsonProcessingException {
		// 0:없음/사용가능 1:있음/사용불가
		String user_id = userVO.getUser_id();
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
		return mapper.writeValueAsString(dbcount);
	}

	@RequestMapping(value = "/findId", method = RequestMethod.POST)
	@PostMapping
	public String findIdPOST(@RequestBody(required = false) UserVO userVO)
			throws JsonProcessingException {
		log.info("NotTokenController-findIdPOST 호출 : 이름 " + userVO.getUser_name() + ", 이메일 " + userVO.getUser_email());
		String user_id = "";
		if (userVO.getUser_name() != null && userVO.getUser_email() != null) {
			user_id = userService.findId(userVO.getUser_name(), userVO.getUser_email());
			if (user_id != null) {
				log.info("NotTokenController-findIdPOST 고객 아이디 리턴 : " + user_id);
			} else {
				log.info("NotTokenController-findIdPOST 고객 아이디 리턴 : " + user_id);
			}
		}
		return mapper.writeValueAsString(user_id);
	}

	@RequestMapping(value = "/findPw", method = RequestMethod.POST)
	@PostMapping
	public String findPwPOST(@RequestBody(required = false) UserVO userVO)
			throws JsonProcessingException {
		String user_id = userVO.getUser_id();
		String user_email = userVO.getUser_email();
		String user_name = userVO.getUser_name();
		log.info("NotTokenController-findPwPOST 호출 : " + user_id + ", " + user_email + ", " + user_name);
		int count = 0;
		String new_password = "";
		UserVO dbVO = null;
		count = userService.findPw(user_id, user_email, user_name);
		dbVO = userService.selectUserId(user_id);
		if (count == 1) {
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
	@PostMapping
	public String loginPOST(@RequestBody UserVO userVO) throws JsonProcessingException {
		log.info("NotTokenController-loginPOST 호출 : 유저Json_" + userVO);
		UserVO dbVO = userService.selectUserId(userVO.getUser_id());
		if (userVO != null) {
			dbVO = userService.selectByUserId(userVO);
			if (dbVO == null) {
				log.info("NotTokenController-loginPOST 리턴 : 사용자 정보 없음.");
			}
		}
		log.info("NotTokenController-loginPOST 리턴 : dbVO.getUser_id()_" + dbVO.getUser_id());
		return dbVO.getUser_id();
	}
	

	@RequestMapping(value = "board/main", method = RequestMethod.GET)
	@GetMapping
	public String test(){
		return "hi";
	}
	
	
	@RequestMapping(value = "board/selectList", method = RequestMethod.POST)
	@PostMapping
	public List<BoardVO> selectListPOST() throws JsonProcessingException {
		log.info("BoardController-selectListPOST 호출 : ");
		List<BoardVO> list = null;
			list = boardService.selectList();
			log.info("BoardController-selectListPOST 게시글 리스트 가져오기 완료");
			return list;
	}

	@RequestMapping(value = "board/main", method = RequestMethod.POST)
	@PostMapping
	public List<BoardVO> selectDescLimitPOST() throws JsonProcessingException {
		log.info("BoardController-selectListPOST 호출 : ");
		List<BoardVO> list = boardService.selectDescLimit();
		log.info("BoardController-selectListPOST 리턴 : " + list);
		return list;
	}
}
