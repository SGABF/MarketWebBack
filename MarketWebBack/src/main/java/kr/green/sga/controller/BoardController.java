package kr.green.sga.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import kr.green.sga.service.BoardImageService;
import kr.green.sga.service.BoardService;
import kr.green.sga.service.UserService;
import kr.green.sga.vo.BoardImageVO;
import kr.green.sga.vo.BoardVO;
import kr.green.sga.vo.UserVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BoardController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private BoardImageService boardImageService;

	@Autowired
	private UserService userService;

	private String os = System.getProperty("os.name").toLowerCase();

	@PostMapping(value = "board/insertBoard", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
	public BoardVO insertBoardPOST(@RequestBody BoardVO boardVO,
			@RequestPart List<MultipartFile> multipartFiles) throws JsonProcessingException {
		log.info("BoardController-insertBoardPOST 호출1 : 현재 로그인 계정 " + boardVO.getUser_id() + ", 작성 시도 게시글 : " + boardVO);
		log.info("BoardController-insertBoardPOST 호출2 : 저장 시도 첨부파일 : " + multipartFiles + "\n");
		UserVO dbUserVO = userService.selectUserId(boardVO.getUser_id());
		String path = "";
		if (boardVO != null && dbUserVO.getUser_id().equals(boardVO.getUser_id())) {
			boardService.insertBoard(boardVO, boardVO.getUser_id());
		} 
		if (multipartFiles != null) {
			log.info("" + multipartFiles);
			for (MultipartFile multipartFile : multipartFiles) {
				if (multipartFile != null && multipartFile.getSize() > 0) {
					BoardImageVO boardImageVO = new BoardImageVO();
					log.info("새로운 BoardImageVO 객체 생성 완료 : " + boardImageVO);
					try {
						if (os.contains("win")) {
							path = "C:/image/";
							log.info("wind path");
						} else {
							path = "/resources/Back/";
							log.info("linux path");
						}
						String saveName = Long.toString(System.nanoTime()) + "_" + multipartFile.getOriginalFilename();
						log.info("saveName : " + saveName);
						

						if (path != null && path != "") {
							File target = new File(path, saveName);
							int ref = boardService.selectMaxIdx();
							multipartFile.transferTo(target);
							boardImageVO.setBoardImage_oriName(multipartFile.getOriginalFilename());
							log.info("생성한 BoardImageVO 객체 내 BoardImage_oriName 주입중 : " + boardImageVO);
							boardImageVO.setBoardImage_saveName(saveName);
							log.info("생성한 BoardImageVO 객체 내 BoardImage_saveName 주입중 : " + boardImageVO);
							boardImageVO.setBoard_idx(ref);
							log.info("생성한 BoardImageVO 객체 내 Board_idx 주입중 : " + boardImageVO);
							boardImageService.insertBoardImage(boardImageVO);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		log.info("BoardController-insertBoardPOST 리턴 : " + boardVO);
		return boardVO;
	}

	@RequestMapping(value = "board/selectList", method = RequestMethod.POST)
	@PostMapping
	public List<BoardVO> selectListPOST(@RequestParam(required = false) String user_id) throws JsonProcessingException {
		log.info("BoardController-selectListPOST 호출 : 현재 로그인 계정 " + user_id + "\n");
		UserVO originUserVO = null;
		List<BoardVO> list = null;
		originUserVO = userService.selectUserId(user_id);
		if (originUserVO != null && originUserVO.getUser_id().equals(user_id)) {
			list = boardService.selectList();
			log.info("BoardController-selectListPOST 게시글 리스트 가져오기 완료");
			return list;
		} else {
			log.info("BoardController-selectListPOST 비정상적인 접근_DB에 현 로그인 계정이 없음 : 빈 목록 출력");
			list = new ArrayList<BoardVO>(); // 빈 배열을 list에 담아 리턴 시킴.
			return list;
		}
	}

}
