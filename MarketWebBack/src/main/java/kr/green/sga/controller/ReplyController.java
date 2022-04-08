package kr.green.sga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

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
	public void insertReplyPOST(
			@RequestBody ReplyVO replyVO
			) throws JsonProcessingException {
		log.info("ReplyController-insertReplyPOST 호출1 : 작성 시도 댓글 : " + replyVO);
		UserVO dbUserVO = null;
		ReplyVO dbReplyVO = null;
		BoardVO dbBoardVO = null;
		int selectMaxIdx = 0;
		if (replyVO != null) {
			dbUserVO = userService.selectUserId(replyVO.getUser_id());
			dbBoardVO = boardService.selectByIdx(replyVO.getBoard_idx());
			if (dbUserVO != null && dbBoardVO != null) {
				log.info("ReplyController-insertReplyPOST : 게시글 DB, 유저DB 정보 확인.");
				replyVO.setBoard_idx(replyVO.getBoard_idx());
				replyVO.setUser_idx(dbUserVO.getUser_idx());
				log.info("ReplyController-insertReplyPOST : 외래키 처리 완료");
				replyService.insertReply(replyVO, dbBoardVO, replyVO.getUser_id());
				selectMaxIdx = replyService.selectMaxIdx();
				dbReplyVO = replyService.selectByIdx(selectMaxIdx);
				log.info("ReplyController-insertReplyPOST 댓글 작성 완료 및 리턴 : " + dbReplyVO);
			}
		}
	}

	@PostMapping(value = "/updateReply")
	public void updateReplyPOST(
			@RequestBody ReplyVO replyVO
			) {
		log.info("ReplyController-updateReplyPOST 호출1 : 수정 시도 댓글 : " + replyVO);
		BoardVO dbBoardVO = null;
		UserVO dbUserVO = null;
		ReplyVO dbReplyVO = null;
		ReplyVO updateReplyVO = null;
		if (replyVO != null) {
			dbBoardVO = boardService.selectByIdx(replyVO.getBoard_idx());
			if (dbBoardVO != null) {
				log.info("ReplyController-updateReplyPOST 호출2 : 댓글이 수정될 게시글 " + dbBoardVO);
				dbUserVO = userService.selectUserId(replyVO.getUser_id()); // 현재 로그인한 계정의 dbVO
				dbReplyVO = replyService.selectByIdx(replyVO.getReply_idx()); // 기존 수정 전의 댓글VO
				if (replyVO.getUser_id().equals(dbReplyVO.getUser_id())) { // 댓글 작성자와 현 로그인 계정의 일치 여부 확인
					replyService.updateReply(replyVO);
					updateReplyVO = replyService.selectByIdx(replyVO.getReply_idx());
					log.info("ReplyController-updateReplyPOST 리턴 : " + updateReplyVO);
				}
			}
		}
	}

	@PostMapping(value = "/deleteReply")
	public void deleteReplyPOST(
			@RequestBody ReplyVO replyVO
			) {
		log.info("ReplyController-deleteReplyPOST 호출1 : 삭제 시도 댓글 replyVO " + replyVO);
		ReplyVO dbReplyVO = null;
		if (replyVO != null) {
			dbReplyVO = replyService.selectByIdx(replyVO.getReply_idx());
			if (dbReplyVO.getUser_id().equals(replyVO.getUser_id())) { // 댓글 작성자와 현 로그인 계정의 일치 여부 확인
				replyService.deleteByIdx(replyVO.getReply_idx());
			} else {
				log.info("ReplyController-deleteReplyPOST 댓글 작성 계정과 현 로그인 계정 불일치함! 댓글 삭제 시도한 아이디 : " + replyVO.getUser_id());
			}
		}
	}

}
