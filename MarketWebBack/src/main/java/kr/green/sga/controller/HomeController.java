package kr.green.sga.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import kr.green.sga.service.BoardImageService;
import kr.green.sga.service.BoardService;
import kr.green.sga.service.ReplyService;
import kr.green.sga.vo.BoardImageVO;
import kr.green.sga.vo.BoardVO;
import kr.green.sga.vo.ReplyVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/home")
public class HomeController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private BoardImageService boardImageService;

	@Autowired
	private ReplyService replyService;

	@RequestMapping(value = "/boardList", method = RequestMethod.POST)
	@PostMapping
	public List<BoardVO> selectListPOST() throws JsonProcessingException {
		log.info("BoardController-selectListPOST 호출 : ");
		List<BoardVO> list = boardService.selectList();
		log.info("BoardController-selectListPOST 리턴 : 게시글 리스트 가져오기 완료");
		return list;
	}

	@RequestMapping(value = "/main", method = RequestMethod.POST)
	@PostMapping
	public List<BoardVO> selectDescLimitPOST() throws JsonProcessingException {
		log.info("HomeController-selectDescLimitPOST 호출 : ");
		List<BoardVO> list = boardService.selectDescLimit();
		log.info("HomeController-selectDescLimitPOST 리턴 : 메인페이지 최근 등록된 8개의 게시글 리스트 가져오기 완료 " + list);
		return list;
	}

	@PostMapping(value = "/selectByIdxBoard")
	public BoardVO selectByIdxPOST(@RequestParam(value = "board_idx") int board_idx) throws JsonProcessingException {
		log.info("HomeController-selectByIdxPOST 호출 : 게시글 상세보기 " + board_idx);
		BoardVO dbBoardVO = null;
		if (board_idx != 0) {
			dbBoardVO = boardService.selectByIdx(board_idx);
			log.info("HomeController-selectByIdxPOST 리턴 : 게시글 상세보기 리턴 " + dbBoardVO);
		}
		return dbBoardVO;
	}

	@PostMapping(value = "/sellBoard")
	public List<BoardVO> selectSellBoardPOST() throws JsonProcessingException {
		log.info("HomeController-selectSellBoardPOST 호출 : 판매글 리스트 조회");
		List<BoardVO> list = boardService.selectSellBoard();
		log.info("HomeController-selectSellBoardPOST 리턴 : 판매글 리스트 조회 리턴 " + list);
		return list;
	}

	@PostMapping(value = "/buyBoard")
	public List<BoardVO> selectBuyBoardPOST() throws JsonProcessingException {
		log.info("HomeController-selectSellBoardPOST 호출 : 구매글 리스트 조회");
		List<BoardVO> list = boardService.selectBuyBoard();
		log.info("HomeController-selectSellBoardPOST 리턴 : 구매글 리스트 조회 리턴 " + list);
		return list;
	}

	@PostMapping(value = "/auctionBoard")
	public List<BoardVO> selectAuctionBoardPOST() throws JsonProcessingException {
		log.info("HomeController-selectSellBoardPOST 호출 : 경매글 리스트 조회");
		List<BoardVO> list = boardService.selectAuctionBoard();
		log.info("HomeController-selectSellBoardPOST 리턴 : 경매글 리스트 조회 리턴 " + list);
		return list;
	}
//  // 추후 작업할 검색조건(type) + 키워드(keyword)
//	@GetMapping("/searchBoardList")
//	private List<BoardVO> searchBoardListGET(
//			@RequestParam(value = "type", required = false) String type,
//			@RequestParam(value = "keyword", required = false) String keyword
//			) throws Exception {
//		log.info("HomeController-searchBoardListGET 호출 " + type + ", " + keyword);
//		if (type != null && keyword != null) {
//			log.info("HomeController-searchBoardListGET 호출 : 검색 작동");
//			BoardVO boardVO = new BoardVO();
//			boardVO.setType(type);
//			boardVO.setKeyword(keyword);
//			List<BoardVO> list = boardService.searchBoardList(boardVO.getType(), boardVO.getKeyword());
//			log.info("HomeController-searchBoardListGET 리턴 : 검색 결과 " + list);
//			return list;
//		}
//		return null;
//	}

	@GetMapping("/searchBoardList")
	private List<BoardVO> searchBoardListGET(
			@RequestParam(value = "keyword", required = false) String keyword
			) throws Exception {
		log.info("HomeController-searchBoardListGET 호출 " + keyword);
		List<BoardVO> list = null;
		if (keyword != null) {
			log.info("HomeController-searchBoardListGET 호출 : 검색 작동");
			BoardVO boardVO = new BoardVO();
			boardVO.setKeyword(keyword);
			list = boardService.searchBoardList(boardVO.getKeyword());
			log.info("HomeController-searchBoardListGET 리턴 : 검색 결과 " + list);
			return list;
		}
		return null;
	}
	
	@PostMapping(value = "/soldoutSellBoard")
	public List<BoardVO> selectSoldoutSellBoardPOST() throws JsonProcessingException {
		log.info("HomeController-selectSellBoardPOST 호출 : 판매글 리스트 조회");
		List<BoardVO> list = boardService.selectSoldoutSellBoard();
		log.info("HomeController-selectSellBoardPOST 리턴 : 판매글 리스트 조회 리턴 " + list);
		return list;
	}
	
	@PostMapping(value = "/soldoutAuctionBoard")
	public List<BoardVO> selectSoldoutAuctionBoardPOST() throws JsonProcessingException {
		log.info("HomeController-selectSellBoardPOST 호출 : 경매글 리스트 조회");
		List<BoardVO> list = boardService.selectSoldoutAuctionBoard();
		log.info("HomeController-selectSellBoardPOST 리턴 : 경매글 리스트 조회 리턴 " + list);
		return list;
	}

	
}
