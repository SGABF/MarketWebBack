package kr.green.sga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
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
@RequestMapping(value = "/reply")
public class ReplyController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReplyService replyService;

	@PostMapping(value = "/insertReply")
	public ReplyVO insertReplyPOST(@RequestPart(value = "ReplyVO", required = false) ReplyVO replyVO,
			@RequestHeader(value = "user_id", required = false) String user_id,
			@RequestParam(value = "board_idx", required = false) int board_idx) throws JsonProcessingException {
		log.info("ReplyController-insertReplyPOST 호출1 : 현재 로그인 계정 " + user_id + ", 작성 시도 댓글 : " + replyVO);
		log.info("ReplyController-insertReplyPOST 호출2 : 댓글이 작성될 게시글번호 " + board_idx);
		UserVO dbUserVO = null;
		ReplyVO dbReplyVO = null;
		BoardVO dbBoardVO = null;
		int selectMaxIdx = 0;
		if (replyVO != null && board_idx != 0 && user_id != null) {
			dbUserVO = userService.selectUserId(user_id);
			dbBoardVO = boardService.selectByIdx(board_idx);
			if (dbUserVO != null && dbBoardVO != null) {
				log.info("ReplyController-insertReplyPOST : 게시글 DB, 유저DB 정보 확인.");
				replyVO.setBoard_idx(board_idx);
				replyVO.setUser_idx(dbUserVO.getUser_idx());
				replyVO.setUser_id(user_id);
				log.info("ReplyController-insertReplyPOST : 외래키 처리 완료");
				replyService.insertReply(replyVO, dbBoardVO, user_id);
				selectMaxIdx = replyService.selectMaxIdx();
				dbReplyVO = replyService.selectByIdx(selectMaxIdx);
			}
		}
		log.info("BoardController-insertBoardPOST 리턴 : " + dbReplyVO);
		return dbReplyVO;
	}

	@PostMapping(value = "/updateReply")
	public void updateReplyPOST(@RequestPart(value = "ReplyVO", required = false) ReplyVO replyVO,
			@RequestHeader(value = "user_id", required = false) String user_id) {
		log.info("ReplyController-updateReplyPOST 호출1 : 현재 로그인 계정 " + user_id + ", 수정 시도 댓글 : " + replyVO);
		BoardVO dbBoardVO = null;
		UserVO dbUserVO = null;
		ReplyVO dbReplyVO = null;
		ReplyVO updateReplyVO = null;
		if (replyVO != null && user_id != null) {
			dbBoardVO = boardService.selectByIdx(replyVO.getBoard_idx());
			if (dbBoardVO != null) {
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
	public void deleteReplyPOST(@RequestParam(value = "reply_idx", required = false) int reply_idx,
			@RequestHeader(value = "user_id", required = false) String user_id) {
		log.info("ReplyController-updateReplyPOST 호출1 : 현재 로그인 계정 " + user_id + ", 삭제 시도 댓글 reply_idx " + reply_idx);
		ReplyVO dbReplyVO = null;
		if (reply_idx != 0 && user_id != null) {
			dbReplyVO = replyService.selectByIdx(reply_idx);
			if (dbReplyVO.getUser_id().equals(user_id)) { // 댓글 작성자와 현 로그인 계정의 일치 여부 확인
				replyService.deleteByIdx(reply_idx);
			} else {
				log.info("ReplyController-updateReplyPOST 댓글 작성 계정과 현 로그인 계정 불일치함! 댓글 삭제 시도한 아이디 : " + user_id);
			}
		}
	}

}
