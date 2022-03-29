package kr.green.sga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
			@RequestHeader(value = "user_id") String user_id) throws JsonProcessingException {
		log.info("ReplyController-insertReplyPOST 호출1 : 현재 로그인 계정 " + user_id + ", 작성 시도 댓글 : " + replyVO);
		log.info("ReplyController-insertReplyPOST 호출2 : 댓글이 작성될 게시글 " + boardVO);
		UserVO dbUserVO = null;
		ReplyVO dbReplyVO = null;
		int selectMaxIdx = 0;
		if (replyVO != null && boardVO != null && user_id != null) {
			dbUserVO = userService.selectUserId(user_id);
			replyVO.setBoard_idx(boardVO.getBoard_idx());
			replyVO.setUser_idx(dbUserVO.getUser_idx());
			replyService.insertReply(replyVO, boardVO, user_id);
			selectMaxIdx = replyService.selectMaxIdx();
			dbReplyVO = replyService.selectByIdx(selectMaxIdx);
		} // if (boardVO != null && user_id != null) {
		log.info("BoardController-insertBoardPOST 리턴 : " + dbReplyVO);
		return dbReplyVO;
	}

	@PostMapping(value = "/updateReply")
	public void updateReplyPOST(@RequestBody ReplyVO replyVO, @RequestHeader(value = "user_id") String user_id) {
		log.info("ReplyController-updateReplyPOST 호출1 : 현재 로그인 계정 " + user_id + ", 수정 시도 댓글 : " + replyVO);
		BoardVO dbBoardVO = null;
		UserVO dbUserVO = null;
		ReplyVO dbReplyVO = null;
		ReplyVO updateReplyVO = null;
		if (replyVO != null && user_id != null) {
			dbBoardVO = boardService.selectByIdx(replyVO.getBoard_idx());
			if(dbBoardVO != null) {
				log.info("ReplyController-updateReplyPOST 호출2 : 댓글이 수정될 게시글 " + dbBoardVO);
				dbUserVO = userService.selectUserId(user_id); // 현재 로그인한 계정의 dbVO
				dbReplyVO = replyService.selectByIdx(replyVO.getReply_idx()); // 기존 수정 전의 댓글VO
				if (dbReplyVO.getUser_idx() == dbUserVO.getUser_idx()) { // 댓글 작성자와 현 로그인 계정의 일치 여부 확인
					replyService.updateReply(replyVO);
					updateReplyVO = replyService.selectByIdx(replyVO.getReply_idx());
					log.info("ReplyController-updateReplyPOST 리턴 : " + updateReplyVO);
				} else {
					log.info("ReplyController-updateReplyPOST 댓글 작성 계정과 현 로그인 계정 불일치함! 댓글 수정 시도한 아이디 : " + user_id);
				}
			}
		}
	}

	@PostMapping(value = "/deleteReply")
	public void deleteReplyPOST(@RequestBody ReplyVO replyVO, @RequestHeader(value = "user_id") String user_id) {
		log.info("ReplyController-updateReplyPOST 호출1 : 현재 로그인 계정 " + user_id + ", 삭제 시도 댓글 : " + replyVO);
		BoardVO dbBoardVO = null;
		UserVO dbUserVO = null;
		ReplyVO dbReplyVO = null;
		if (replyVO != null && user_id != null) {
			dbBoardVO = boardService.selectByIdx(replyVO.getBoard_idx());
			if (dbBoardVO != null) {
				log.info("ReplyController-updateReplyPOST : 댓글삭제 요청 게시글 " + dbBoardVO);
				dbUserVO = userService.selectUserId(user_id); // 현재 로그인한 계정의 dbVO
				dbReplyVO = replyService.selectByIdx(replyVO.getReply_idx()); // 기존 수정 전의 댓글VO
				if (dbReplyVO.getUser_idx() == dbUserVO.getUser_idx()) { // 댓글 작성자와 현 로그인 계정의 일치 여부 확인
					replyService.deleteByIdx(replyVO.getReply_idx());
				} else {
					log.info("ReplyController-updateReplyPOST 댓글 작성 계정과 현 로그인 계정 불일치함! 댓글 삭제 시도한 아이디 : " + user_id);
				}
			}
		}
	}

}
