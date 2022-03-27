package kr.green.sga.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.green.sga.dao.BoardDAO;
import kr.green.sga.dao.ReplyDAO;
import kr.green.sga.dao.UserDAO;
import kr.green.sga.vo.ReplyVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("replyService")
public class ReplyServiceImpl implements ReplyService {
	
	@Autowired
	private ReplyDAO replyDAO;
	
	@Autowired
	private BoardDAO boardDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	
	
	@Override
	public void insertReply(ReplyVO replyVO) {
		log.info("ReplyServiceImpl-insertReply 호출 : " + replyVO);
		if(replyVO != null) {
			replyDAO.insertReply(replyVO);
		}
		
		log.info("ReplyServiceImpl-insertReply 리턴 : " + replyVO);
	}

	@Override
	public ReplyVO selectByIdx(int reply_idx) {
		log.info("ReplyServiceImpl-selectByIdx 호출 : " + reply_idx + "번 호출");
		ReplyVO dbReplyVO = null;
		if(reply_idx != 0) {
			dbReplyVO = replyDAO.selectByIdx(reply_idx);
		}
		log.info("ReplyServiceImpl-selectByIdx 리턴 : " + dbReplyVO);
		return dbReplyVO;
	}

	@Override
	public void updateReply(ReplyVO replyVO) {
		log.info("ReplyServiceImpl-updateReply 호출 : 수정 시도 댓글 " + replyVO);
		ReplyVO dbReplyVO = null;
		if(replyVO != null) {
			replyDAO.updateReply(replyVO);
			dbReplyVO = replyDAO.selectByIdx(replyVO.getReply_idx());
		}
		log.info("ReplyServiceImpl-updateReply 리턴 : " + dbReplyVO);
	}

	@Override
	public void deleteByIdx(int reply_idx) {
		log.info("ReplyServiceImpl-deleteByIdx 호출 : 삭제 시도 댓글 " + reply_idx);
		ReplyVO dbReplyVO = null;
		if(reply_idx != 0) {
			replyDAO.deleteByIdx(reply_idx);
			dbReplyVO = replyDAO.selectByIdx(reply_idx);
		}
		log.info("ReplyServiceImpl-deleteByIdx 리턴 : " + dbReplyVO);
	}

	@Override
	public void deleteByBoardIdx(int board_idx) {
		log.info("ReplyServiceImpl-deleteByBoardIdx 호출 : 게시물 삭제 요청 진행중 및 삭제 시도 댓글 목록 " + board_idx);
		
		
	}

	@Override
	public List<ReplyVO> selectByRef(int board_idx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReplyVO> selectList() {
		// TODO Auto-generated method stub
		return null;
	}

}
