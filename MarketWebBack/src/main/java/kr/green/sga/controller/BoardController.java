package kr.green.sga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ObjectMapper mapper;
	
	@RequestMapping(value = "/insertBoard", method = RequestMethod.POST)
	@PostMapping
	public String insertBoardPOST(@RequestBody BoardVO boardVO, String user_id) throws JsonProcessingException {
		log.info("BoardController-insertBoardPOST 호출 : 현재 로그인 계정 " + user_id + " 작성 시도 게시글 : " + boardVO + "\n");
		UserVO originUserVO = userService.selectUserId(user_id);
		if (boardVO != null && originUserVO != null) {
			boardService.insertBoard(boardVO, user_id); // DB에 저장을 위한 service 호출
			log.info("BoardController-insertBoardPOST 최종 리턴 : " + boardVO);
		} 
		log.info("BoardController-insertBoardPOST json 리턴 : " + boardVO);
		return mapper.writeValueAsString(boardVO);
	}
	
}
