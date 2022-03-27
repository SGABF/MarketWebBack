package kr.green.sga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import kr.green.sga.service.BoardService;
import kr.green.sga.service.ReplyService;
import kr.green.sga.service.UserService;
import kr.green.sga.vo.BoardVO;
import kr.green.sga.vo.ReplyVO;
import kr.green.sga.vo.UserVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(name = "/reply")
public class ReplyController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReplyService replyService;

	@PostMapping(value = "/insertReply")
	public ReplyVO insertReplyPOST(@RequestBody ReplyVO replyVO, @RequestBody BoardVO boardVO,
			@RequestBody UserVO userVO) throws JsonProcessingException {
		log.info("ReplyController-insertReplyPOST 호출1 : 현재 로그인 계정 " + userVO.getUser_id() + ", 작성 시도 댓글 : " + replyVO);
		log.info("ReplyController-insertReplyPOST 호출2 : 댓글이 작성될 게시글 " + boardVO);
		UserVO dbUserVO = null;
		ReplyVO dbReplyVO = null;
		int selectMaxIdx = 0;
		if (replyVO != null && boardVO != null && userVO != null) {
			dbUserVO = userService.selectUserId(userVO.getUser_id());
			replyVO.setBoard_idx(boardVO.getBoard_idx());
			replyVO.setUser_idx(dbUserVO.getUser_idx());
			replyService.insertReply(replyVO, boardVO, userVO.getUser_id());
			selectMaxIdx = replyService.selectMaxIdx();
			dbReplyVO = replyService.selectByIdx(selectMaxIdx);
		} // if (boardVO != null && user_id != null) {
		log.info("BoardController-insertBoardPOST 리턴 : " + dbReplyVO);
		return dbReplyVO;
	}

	
	// 작업 진행중 
	@PostMapping(value = "/updateReply")
	public ReplyVO updateReplyPOST(@RequestBody ReplyVO replyVO, @RequestBody BoardVO boardVO,
			@RequestBody UserVO userVO) throws JsonProcessingException {
		log.info("ReplyController-insertReplyPOST 호출1 : 현재 로그인 계정 " + userVO.getUser_id() + ", 작성 시도 댓글 : " + replyVO);
		log.info("ReplyController-insertReplyPOST 호출2 : 댓글이 작성될 게시글 " + boardVO);
		UserVO dbUserVO = null;
		ReplyVO dbReplyVO = null;
		int selectMaxIdx = 0;
		if (replyVO != null && boardVO != null && userVO != null) {
			dbUserVO = userService.selectUserId(userVO.getUser_id());
			replyVO.setBoard_idx(boardVO.getBoard_idx());
			replyVO.setUser_idx(dbUserVO.getUser_idx());
			replyService.insertReply(replyVO, boardVO, userVO.getUser_id());
			selectMaxIdx = replyService.selectMaxIdx();
			dbReplyVO = replyService.selectByIdx(selectMaxIdx);
		} // if (boardVO != null && user_id != null) {
		log.info("BoardController-insertBoardPOST 리턴 : " + dbReplyVO);
		return dbReplyVO;
	}
}
