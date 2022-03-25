package kr.green.sga.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
//		List<BoardVO> list = new ArrayList<BoardVO>();
		log.info("BoardController-selectDescLimitPOST 리턴 : 메인페이지 최근 등록된 8개의 게시글 리스트 가져오기 완료 " + list);
		return list;
	}
	
//	@PostMapping(value = "/selectByIdxBoard")
//	public BoardVO selectByIdxPOST(@RequestBody BoardVO boardVO)
//			throws JsonProcessingException {
//		log.info("HomeController-selectByIdxPOST 호출 : 상세보기 시도 게시글 " + boardVO);
//		BoardVO dbBoardVO = null;
//		if (boardVO != null) {
//			dbBoardVO = boardService.selectByIdx(boardVO.getBoard_idx());
//		}
//		log.info("HomeController-selectByIdxPOST 리턴 : 게시글 상세보기 리턴 " + dbBoardVO);
//		return dbBoardVO;
//	}
	
	
	@PostMapping(value = "/selectByIdxBoard")
	public BoardVO selectByIdxPOST()
			throws JsonProcessingException {
		log.info("HomeController-selectByIdxPOST 호출 : 상세보기 시도 게시글 수진");
		BoardVO dbBoardVO = null;
			dbBoardVO = boardService.selectByIdx(14);
		log.info("HomeController-selectByIdxPOST 리턴 : 게시글 상세보기 리턴 " + dbBoardVO);
		return dbBoardVO;
	}
	
}
