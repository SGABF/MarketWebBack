package kr.green.sga.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

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
@RequestMapping(name = "/board")
public class BoardController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private BoardImageService boardImageService;

	@Autowired
	private UserService userService;

	private String os = System.getProperty("os.name").toLowerCase();

	@PostMapping(value = "/insertBoard", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BoardVO insertBoardPOST(@RequestBody BoardVO boardVO, @RequestBody UserVO userVO,
			@RequestPart(required = false) List<MultipartFile> multipartFiles) throws JsonProcessingException {
		UserVO dbUserVO = null;
		String user_id = null;
		String path = "";
		if (boardVO != null) {
			dbUserVO = userService.selectUserId(userVO.getUser_id());
			user_id = dbUserVO.getUser_id();
			log.info("BoardController-insertBoardPOST 호출1 : 현재 로그인 계정 " + userVO.getUser_id() + ", 작성 시도 게시글 : "
					+ boardVO);
			log.info("BoardController-insertBoardPOST 호출2 : 저장 시도 첨부파일 : " + multipartFiles + "\n");
			boardService.insertBoard(boardVO, user_id);
			if (multipartFiles != null) {
				List<BoardImageVO> fileList = new ArrayList<>();
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
							String saveName = Long.toString(System.nanoTime()) + "_"
									+ multipartFile.getOriginalFilename();
							log.info("saveName : " + saveName);

							if (path != null && path != "") {
								File target = new File(path, saveName);
								int boardMaxIdx = boardService.selectMaxIdx();
								multipartFile.transferTo(target);
								boardImageVO.setBoardImage_oriName(multipartFile.getOriginalFilename());
								log.info("생성한 BoardImageVO 객체 내 BoardImage_oriName set완료 : " + boardImageVO);
								boardImageVO.setBoardImage_saveName(saveName);
								log.info("생성한 BoardImageVO 객체 내 BoardImage_saveName set완료 : " + boardImageVO);
								boardImageVO.setBoard_idx(boardMaxIdx);
								log.info("생성한 BoardImageVO 객체 내 Board_idx 외래키 set완료 : " + boardImageVO + "\n");
								fileList.add(boardImageVO);
								log.info("생성한 List<BoardImageVO> 리스트에 BoardImageVO 최종 add 완료 : " + fileList);
								boardImageService.insertBoardImage(boardImageVO);
								log.info("board_idx 외래키 set 작업한 BoardImageVO 저장 \n");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					} // if (multipartFile != null && multipartFile.getSize() > 0) {
				} // for (MultipartFile multipartFile : multipartFiles) {
			} // if (multipartFiles != null) {
		} // if (boardVO != null && user_id != null) {
		log.info("BoardController-insertBoardPOST 리턴 : " + boardVO);
		return boardVO;
	}

	@PostMapping(value = "/updateBoard", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BoardVO updateBoardPOST(@RequestBody BoardVO boardVO, @RequestBody UserVO userVO,
			@RequestPart List<MultipartFile> multipartFiles) throws JsonProcessingException {
		UserVO dbUserVO = userService.selectUserId(userVO.getUser_id());
		String user_id = dbUserVO.getUser_id();
		String path = "";
		if (boardVO != null && dbUserVO.getUser_id() != null) {
			log.info("BoardController-insertBoardPOST 호출1 : 현재 로그인 계정 " + dbUserVO.getUser_id() + ", 수정 시도 게시글 : "
					+ boardVO);
			log.info("BoardController-insertBoardPOST 호출2 : 저장 시도 첨부파일 : " + multipartFiles + "\n");
			boardService.updateBoard(boardVO, user_id, null, path);
			if (multipartFiles != null) {
				List<BoardImageVO> fileList = new ArrayList<>();
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
							String saveName = Long.toString(System.nanoTime()) + "_"
									+ multipartFile.getOriginalFilename();
							log.info("saveName : " + saveName);

							if (path != null && path != "") {
								File target = new File(path, saveName);
								int boardMaxIdx = boardService.selectMaxIdx();
								multipartFile.transferTo(target);
								boardImageVO.setBoardImage_oriName(multipartFile.getOriginalFilename());
								log.info("생성한 BoardImageVO 객체 내 BoardImage_oriName set완료 : " + boardImageVO);
								boardImageVO.setBoardImage_saveName(saveName);
								log.info("생성한 BoardImageVO 객체 내 BoardImage_saveName set완료 : " + boardImageVO);
								boardImageVO.setBoard_idx(boardMaxIdx);
								log.info("생성한 BoardImageVO 객체 내 Board_idx 외래키 set완료 : " + boardImageVO + "\n");
								fileList.add(boardImageVO);
								log.info("생성한 List<BoardImageVO> 리스트에 BoardImageVO 최종 add 완료 : " + fileList);
								boardImageService.insertBoardImage(boardImageVO);
								log.info("board_idx 외래키 set 작업한 BoardImageVO 저장 \n");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					} // if (multipartFile != null && multipartFile.getSize() > 0) {
				} // for (MultipartFile multipartFile : multipartFiles) {
			} // if (multipartFiles != null) {
		} // if (boardVO != null && user_id != null) {
		log.info("BoardController-insertBoardPOST 리턴 : " + boardVO);
		return boardVO;
	}

	@GetMapping(value = "/test")
	public String test() {
		return "hi";
	}

}
