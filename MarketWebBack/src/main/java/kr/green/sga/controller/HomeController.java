package kr.green.sga.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import kr.green.sga.service.BoardService;
import kr.green.sga.vo.BoardVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/home")
public class HomeController {
	
	@Autowired
	private BoardService boardService;

	@RequestMapping(value = "boardList", method = RequestMethod.POST)
	@PostMapping
	public List<BoardVO> selectListPOST() throws JsonProcessingException {
		log.info("BoardController-selectListPOST 호출 : ");
		List<BoardVO> list = boardService.selectList();
		log.info("BoardController-selectListPOST 리턴 : 게시글 리스트 가져오기 완료");
		return list;
	}

	@RequestMapping(value = "main", method = RequestMethod.POST)
	@PostMapping
	public List<BoardVO> selectDescLimitPOST() throws JsonProcessingException {
		log.info("BoardController-selectDescLimitPOST 호출 : ");
		List<BoardVO> list = boardService.selectDescLimit();
		log.info("BoardController-selectDescLimitPOST 리턴 : 메인페이지 최근 등록된 8개의 게시글 리스트 가져오기 완료 " + list);
		return list;
	}
	
}
