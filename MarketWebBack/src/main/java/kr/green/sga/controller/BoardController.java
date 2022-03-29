package kr.green.sga.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import kr.green.sga.service.BoardImageService;
import kr.green.sga.service.BoardService;
import kr.green.sga.service.ReplyService;
import kr.green.sga.service.UserService;
import kr.green.sga.vo.BoardImageVO;
import kr.green.sga.vo.BoardVO;
import kr.green.sga.vo.ReplyVO;
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

	@Autowired
	private ReplyService replyService;

	private String os = System.getProperty("os.name").toLowerCase();

	@PostMapping(value = "/insertBoard", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BoardVO insertBoardPOST(@RequestBody BoardVO boardVO, @RequestHeader(value = "user_id") String user_id,
			@RequestPart(required = false) List<MultipartFile> multipartFiles) throws JsonProcessingException {
		String path = "";
		if (boardVO != null && user_id != null) {
			log.info("BoardController-insertBoardPOST 호출1 : 현재 로그인 계정 " + user_id + ", 작성 시도 게시글 : " + boardVO);
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
	public BoardVO updateBoardPOST(@RequestBody BoardVO boardVO, @RequestHeader(value = "user_id") String user_id,
			@RequestPart List<MultipartFile> multipartFiles) throws JsonProcessingException {
		UserVO dbUserVO = null;
		String path = "";
		if (boardVO != null) {
			log.info("BoardController-updateBoardPOST 호출1 : 현재 로그인 계정 " + user_id + ", 수정 시도 게시글 : " + boardVO);
			log.info("BoardController-updateBoardPOST 호출2 : 수정 시도 첨부파일 : " + multipartFiles + "\n");
			dbUserVO = userService.selectUserId(user_id);
			if (boardVO.getUser_idx() == dbUserVO.getUser_idx()) {
				log.info("BoardController-updateBoardPOST 현재의 수정희망 계정과 과거의 게시물작성한 계정의 일치함을 확인했습니다.");
				boardService.updateBoard(boardVO, user_id);
				// 첨부파일을 boardImageList에 담아 boardVO
				if (multipartFiles != null) {
					List<BoardImageVO> fileList = new ArrayList<>();
					for (MultipartFile multipartFile : multipartFiles) {
						if (multipartFile != null && multipartFile.getSize() > 0) {
							BoardImageVO boardImageVO = new BoardImageVO();
							log.info("BoardController-updateBoardPOST 새로운 BoardImageVO 객체 생성 완료 : " + boardImageVO);
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
									multipartFile.transferTo(target);
									boardImageVO.setBoardImage_oriName(multipartFile.getOriginalFilename());
									log.info("생성한 BoardImageVO 객체 내 BoardImage_oriName set완료 : " + boardImageVO);
									boardImageVO.setBoardImage_saveName(saveName);
									log.info("생성한 BoardImageVO 객체 내 BoardImage_saveName set완료 : " + boardImageVO);
									boardImageVO.setBoard_idx(boardVO.getBoard_idx());
									log.info("생성한 BoardImageVO 객체 내 Board_idx 외래키 set완료 : " + boardImageVO);
									fileList.add(boardImageVO);
									log.info("생성한 List<BoardImageVO> 리스트에 BoardImageVO 최종 add 완료 : " + fileList);
									boardImageService.insertBoardImage(boardImageVO);
									log.info("board_idx 외래키 set 작업한 BoardImageVO DB 저장");
									boardVO.setBoardImageList(fileList);
									log.info("boardVO의 BoardImageList 컬럼에 리스트 담는 중. \n");
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} // if (multipartFiles != null) {
				List<ReplyVO> replyList = replyService.selectByRef(boardVO.getBoard_idx());
				if (replyList != null) {
					log.info("BoardController-updateBoardPOST 댓글 리스트 확인  " + replyList);
					boardVO.setReplyList(replyList);
				}
			} else {
				log.info("BoardController-updateBoardPOST 리턴 : 게시글 수정 계정과 게시글 작성 계정이 일치하지 않습니다.");
				return null;
			}
		}
		log.info("BoardController-updateBoardPOST 리턴 : " + boardVO);
		return boardVO;
	}

	@PostMapping(value = "/deleteBoard")
	public BoardVO deleteBoardPOST(@RequestBody BoardVO boardVO, @RequestHeader(value = "user_id") String user_id)
			throws JsonProcessingException {
		log.info("BoardController-deleteBoardPOST 호출1 : 현재 로그인 계정 " + user_id);
		log.info("BoardController-deleteBoardPOST 호출2 : 삭제 시도 게시글 " + boardVO);
		String path = "";
		UserVO loginUserVO = null;
		if (boardVO != null && user_id != null) {
			loginUserVO = userService.selectUserId(user_id); 
			UserVO boardUserVO = userService.selectByIdx(boardVO.getUser_idx());
			if (boardUserVO.getUser_id().equals(loginUserVO.getUser_id())) {
				log.info("BoardController-deleteBoardPOST 게시글의 작성자와 삭제 요청자가 일치합니다.");
				if (os.contains("win")) {
					path = "C:/image/";
					log.info("wind path");
				} else {
					path = "/resources/Back/";
					log.info("linux path");
				}
				boardService.deleteBoard(boardVO, path);
			}
		}
		log.info("BoardController-deleteBoardPOST 리턴 : 삭제 성공 ");
		return boardVO;
	}

	@GetMapping(value = "/test")
	public String test() {
		return "hi";
	}

}
