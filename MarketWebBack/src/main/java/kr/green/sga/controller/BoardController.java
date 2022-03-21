package kr.green.sga.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

	@Autowired
	private ObjectMapper mapper;

	private String os = System.getProperty("os.name").toLowerCase();

	@RequestMapping(value = "board/insertBoard", method = RequestMethod.POST)
	@PostMapping
	@RequestBody
	public String insertBoardPOST(@RequestPart(value = "fileUp") MultipartFile mfile, BoardVO boardVO, String user_id)
			throws JsonProcessingException {
		log.info("BoardController-insertBoardPOST 호출 : 현재 로그인 계정 " + user_id + " 작성 시도 게시글 : " + boardVO + "\n");
		UserVO originUserVO = userService.selectUserId(user_id);
		BoardImageVO boardImageFile = new BoardImageVO();
		String path = "";
		if (boardVO != null && originUserVO != null) {
			boardService.insertBoard(boardVO, user_id); // DB에 저장을 위한 service 호출
			try {
				if (os.contains("win")) {
					path = "C:/image/";
				} else {
					path = "/resources/Back/";
				}
				String saveName = UUID.randomUUID() + "_" + mfile.getOriginalFilename();

				if (path != null && path != "") {
					File target = new File(path, saveName);
					mfile.transferTo(target);
					boardImageFile.setBoardImage_oriName(mfile.getOriginalFilename());
					boardImageFile.setBoardImage_saveName(saveName);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		boardImageService.insertBoardImage(boardImageFile);
		log.info("BoardController-insertBoardPOST json 리턴 : " + boardVO);
		return mapper.writeValueAsString(boardVO);
	}

	@RequestMapping(value = "board/selectList", method = RequestMethod.POST)
	@PostMapping
	public String selectListPOST(@RequestParam(required = false) String user_id) throws JsonProcessingException {
		log.info("BoardController-selectListPOST 호출 : 현재 로그인 계정 " + user_id + "\n");
		UserVO originUserVO = null;
		List<BoardVO> list = null;
		originUserVO = userService.selectUserId(user_id);
		if (originUserVO != null && originUserVO.getUser_id().equals(user_id)) {
			list = boardService.selectList();
			log.info("BoardController-selectListPOST 게시글 리스트 가져오기 완료");
			return mapper.writeValueAsString(list);
		} else {
			log.info("BoardController-selectListPOST 비정상적인 접근_DB에 현 로그인 계정이 없음 : 빈 목록 출력");
			list = new ArrayList<BoardVO>(); // 빈 배열을 list에 담아 리턴 시킴.
			return mapper.writeValueAsString(list);
		}
	}

}
