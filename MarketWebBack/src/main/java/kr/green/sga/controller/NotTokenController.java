package kr.green.sga.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.green.sga.service.BoardImageService;
import kr.green.sga.service.BoardService;
import kr.green.sga.service.ReplyService;
import kr.green.sga.service.UserService;
import kr.green.sga.vo.BoardImageVO;
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

	@Autowired
	private BoardImageService boardImageService;

	private String os = System.getProperty("os.name").toLowerCase();

	@Autowired
	private ReplyService replyService;

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
			log.info("UserController-insertUserPOST 리턴 : userVO : " + userVO);
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
	public String findIdPOST(@RequestBody(required = false) UserVO userVO) throws JsonProcessingException {
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
	public String findPwPOST(@RequestBody(required = false) UserVO userVO) throws JsonProcessingException {
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

	@RequestMapping(value = "test", method = RequestMethod.GET)
	@GetMapping
	public String test() {
		return "hi";
	}

	@PostMapping(value = "/insertBoard")
	public String insertBoardPOST(
//			@RequestHeader(value = "user_id") String user_id,
			@RequestPart(value = "testobject", required = false) BoardVO boardVO,
			@RequestParam(value = "file", required = false) List<MultipartFile> multipartFiles)
			throws JsonProcessingException {
		String user_id = "cdo90";
		if (boardVO != null) {
			String path = "";
			log.info("BoardController-insertBoardPOST 호출1 : 현재 로그인 계정 " + user_id + ", 작성 시도 게시글 : " + boardVO);
			log.info("BoardController-insertBoardPOST 호출2 : 저장 시도 첨부파일 : " + multipartFiles + "\n");
			boardService.insertBoard(boardVO, user_id);
			if (multipartFiles != null) {
				log.info("BoardController-insertBoardPOST 첨부파일 존재확인");
				String boardImage_profileName = "";
				int boardMaxIdx = boardService.selectMaxIdx();
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
							String saveName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
							log.info("saveName : " + saveName);

							if (path != null && path != "") {
								File target = new File(path, saveName);
								multipartFile.transferTo(target);
								boardImageVO.setBoardImage_oriName(multipartFile.getOriginalFilename());
								log.info("생성한 BoardImageVO 객체 내 BoardImage_oriName set완료 : " + boardImageVO);
								boardImageVO.setBoardImage_saveName(saveName);
								log.info("생성한 BoardImageVO 객체 내 BoardImage_saveName set완료 : " + boardImageVO);
								boardImageVO.setBoard_idx(boardMaxIdx);
								log.info("생성한 BoardImageVO 객체 내 Board_idx 외래키 set완료 : " + boardImageVO);
								fileList.add(boardImageVO);
								log.info("생성한 List<BoardImageVO> fileList에 BoardImageVO add 완료 : " + fileList);
								boardImageService.insertBoardImage(boardImageVO);
								log.info("board_idx 외래키 set 작업한 BoardImageVO DB 저장");
								boardVO.setBoardImageList(fileList);
								log.info("BoardVO BoardImageList컬럼에 fileList를 최종 set 완료 : " + boardVO);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				log.info("BoardController-insertBoardPOST 게시글 대표이미지 설정");
				boardImage_profileName = boardImageService.selectProfile(boardMaxIdx);
				log.info("BoardController-insertBoardPOST boardImage_profileName : " + boardImage_profileName);
				BoardVO updateBoardVO = boardService.selectByIdx(boardMaxIdx);
				log.info("BoardController-insertBoardPOST updateBoardVO : " + updateBoardVO);
				updateBoardVO.setBoard_profile(boardImage_profileName);
				log.info("BoardController-insertBoardPOST updateBoardVO.setBoard_profile : " + updateBoardVO);
				boardService.updateBoard(updateBoardVO, user_id);
			} // if (multipartFile != null && multipartFile.getSize() > 0) {
		}
		return "return";
	}
//
//	@PostMapping(value = "/updateBoard", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public BoardVO updateBoardPOST(@RequestBody BoardVO boardVO, @RequestHeader(value = "user_id") String user_id,
//			@RequestPart List<MultipartFile> multipartFiles) throws JsonProcessingException {
//		UserVO dbUserVO = null;
//		String path = "";
//		if (boardVO != null) {
//			log.info("BoardController-updateBoardPOST 호출1 : 현재 로그인 계정 " + user_id + ", 수정 시도 게시글 : " + boardVO);
//			log.info("BoardController-updateBoardPOST 호출2 : 수정 시도 첨부파일 : " + multipartFiles + "\n");
//			dbUserVO = userService.selectUserId(user_id);
//			if (boardVO.getUser_idx() == dbUserVO.getUser_idx()) {
//				log.info("BoardController-updateBoardPOST 현재의 수정희망 계정과 과거의 게시물작성한 계정의 일치함을 확인했습니다.");
//				boardService.updateBoard(boardVO, user_id);
//				// 첨부파일을 boardImageList에 담아 boardVO
//				if (multipartFiles != null) {
//					List<BoardImageVO> fileList = new ArrayList<>();
//					for (MultipartFile multipartFile : multipartFiles) {
//						if (multipartFile != null && multipartFile.getSize() > 0) {
//							BoardImageVO boardImageVO = new BoardImageVO();
//							log.info("BoardController-updateBoardPOST 새로운 BoardImageVO 객체 생성 완료 : " + boardImageVO);
//							try {
//								if (os.contains("win")) {
//									path = "C:/image/";
//									log.info("wind path");
//								} else {
//									path = "/resources/Back/";
//									log.info("linux path");
//								}
//								String saveName = Long.toString(System.nanoTime()) + "_"
//										+ multipartFile.getOriginalFilename();
//								log.info("saveName : " + saveName);
//
//								if (path != null && path != "") {
//									File target = new File(path, saveName);
//									multipartFile.transferTo(target);
//									boardImageVO.setBoardImage_oriName(multipartFile.getOriginalFilename());
//									log.info("생성한 BoardImageVO 객체 내 BoardImage_oriName set완료 : " + boardImageVO);
//									boardImageVO.setBoardImage_saveName(saveName);
//									log.info("생성한 BoardImageVO 객체 내 BoardImage_saveName set완료 : " + boardImageVO);
//									boardImageVO.setBoard_idx(boardVO.getBoard_idx());
//									log.info("생성한 BoardImageVO 객체 내 Board_idx 외래키 set완료 : " + boardImageVO);
//									fileList.add(boardImageVO);
//									log.info("생성한 List<BoardImageVO> 리스트에 BoardImageVO 최종 add 완료 : " + fileList);
//									boardImageService.insertBoardImage(boardImageVO);
//									log.info("board_idx 외래키 set 작업한 BoardImageVO DB 저장");
//									boardVO.setBoardImageList(fileList);
//									log.info("boardVO의 BoardImageList 컬럼에 리스트 담는 중. \n");
//								}
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				} // if (multipartFiles != null) {
//				List<ReplyVO> replyList = replyService.selectByRef(boardVO.getBoard_idx());
//				if (replyList != null) {
//					log.info("BoardController-updateBoardPOST 댓글 리스트 확인  " + replyList);
//					boardVO.setReplyList(replyList);
//				}
//			} else {
//				log.info("BoardController-updateBoardPOST 리턴 : 게시글 수정 계정과 게시글 작성 계정이 일치하지 않습니다.");
//				return null;
//			}
//		}
//		log.info("BoardController-updateBoardPOST 리턴 : " + boardVO);
//		return boardVO;
//	}

	@PostMapping(value = "/updateBoard", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BoardVO updateBoardPOST(@RequestBody BoardVO boardVO, @RequestHeader(value = "user_id") String user_id,
			@RequestParam MultipartFile multipartFiles) throws JsonProcessingException {
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
						String saveName = Long.toString(System.nanoTime()) + "_" + multipartFiles.getOriginalFilename();
						log.info("saveName : " + saveName);

						if (path != null && path != "") {
							File target = new File(path, saveName);
							multipartFiles.transferTo(target);
							boardImageVO.setBoardImage_oriName(multipartFiles.getOriginalFilename());
							log.info("생성한 BoardImageVO 객체 내 BoardImage_oriName set완료 : " + boardImageVO);
							boardImageVO.setBoardImage_saveName(saveName);
							log.info("생성한 BoardImageVO 객체 내 BoardImage_saveName set완료 : " + boardImageVO);
							boardImageVO.setBoard_idx(boardVO.getBoard_idx());
							log.info("생성한 BoardImageVO 객체 내 Board_idx 외래키 set완료 : " + boardImageVO);
							boardImageService.insertBoardImage(boardImageVO);
							log.info("board_idx 외래키 set 작업한 BoardImageVO DB 저장");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
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

}
