package kr.green.sga.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.green.sga.dao.BoardDAO;
import kr.green.sga.dao.ReplyDAO;
import kr.green.sga.dao.UserDAO;
import kr.green.sga.vo.BoardVO;
import kr.green.sga.vo.ReplyVO;
import kr.green.sga.vo.UserVO;
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
	public void insertReply(ReplyVO replyVO, BoardVO boardVO, String user_id) {
		log.info("ReplyServiceImpl-insertReply 호출 : replyVO " + replyVO);
		UserVO replyUserVO = null;
		ReplyVO dbReplyVO = null;
		int selectMaxIdx = 0;
		if (replyVO != null && boardVO != null && user_id != null) {
			replyUserVO = userDAO.selectUserId(user_id);
			replyDAO.insertReply(replyVO);
			selectMaxIdx = replyDAO.selectMaxIdx();
			dbReplyVO = replyDAO.selectByIdx(selectMaxIdx);
		} else {
			log.info("ReplyServiceImpl-insertReply 오류 리턴 : replyVO, boardVO, user_id 중 null 값 확인");
		}
		log.info("ReplyServiceImpl-insertReply 리턴 : dbReplyVO " + dbReplyVO);
	}

	@Override
	public ReplyVO selectByIdx(int reply_idx) {
		log.info("ReplyServiceImpl-selectByIdx 호출 : " + reply_idx + "번 호출");
		ReplyVO dbReplyVO = null;
		if (reply_idx != 0) {
			dbReplyVO = replyDAO.selectByIdx(reply_idx);
		}
		log.info("ReplyServiceImpl-selectByIdx 리턴 : " + dbReplyVO);
		return dbReplyVO;
	}
	
	@Override
	public void updateReply(ReplyVO replyVO) {
		log.info("ReplyServiceImpl-updateReply 호출 : 수정 시도 댓글 " + replyVO);
		ReplyVO dbReplyVO = null;
		if (replyVO != null) {
			replyDAO.updateReply(replyVO);
			dbReplyVO = replyDAO.selectByIdx(replyVO.getReply_idx());
		}
		log.info("ReplyServiceImpl-updateReply 리턴 : " + dbReplyVO);
	}

	@Override
	public void deleteByIdx(int reply_idx) {
		log.info("ReplyServiceImpl-deleteByIdx 호출 : 삭제 시도 댓글 reply_idx " + reply_idx);
		ReplyVO dbReplyVO = replyDAO.selectByIdx(reply_idx);
		BoardVO dbBoardVO = boardDAO.selectByIdx(dbReplyVO.getBoard_idx());
		if (dbReplyVO != null && dbBoardVO != null) {
			replyDAO.deleteByIdx(reply_idx);
			log.info("ReplyServiceImpl-deleteByIdx 리턴 : reply_idx " + reply_idx + "번글 DB 삭제 완료");
		}
	}

	@Override
	public void deleteByBoardIdx(BoardVO boardVO, UserVO userVO) {
		log.info("ReplyServiceImpl-deleteByBoardIdx 호출 : 게시물 삭제 요청 진행중 및 해당 게시글의 댓글 전체삭제 시도중");
		UserVO dbUserVO = null;
		if(boardVO != null) {
			dbUserVO = userDAO.selectUserId(userVO.getUser_id());
			if(boardVO.getUser_idx() == dbUserVO.getUser_idx()) {
				replyDAO.deleteByBoardIdx(boardVO.getBoard_idx());
				log.info("ReplyServiceImpl-deleteByBoardIdx 리턴 : 게시물 삭제 요청 진행중 및 해당 게시글의 댓글 전체삭제 완료");
			}
		} else {
			log.info("ReplyServiceImpl-deleteByBoardIdx 리턴 : '게시물 삭제요청자'와 '게시물 작성인원' 불일치");
		}
	}
	
	@Override
	public void deleteByUserIdx(int user_idx) {
		log.info("ReplyServiceImpl-deleteByUserIdx 호출1 user_idx : " + user_idx + " 해당 user_idx의 댓글 전체삭제 시도중");
		log.info("ReplyServiceImpl-deleteByUserIdx 해당 user_idx의 댓글 전체삭제");
		UserVO dbUserVO = null;
		if(user_idx != 0) {
			dbUserVO = userDAO.selectByIdx(user_idx);
			if(dbUserVO != null) {
				log.info("ReplyServiceImpl-deleteByUserIdx dbUserVO 확인 및 삭제 시작 : " + dbUserVO);
				replyDAO.deleteByUserIdx(user_idx);
				log.info("ReplyServiceImpl-deleteByUserIdx 해당 user_idx의 댓글 전체삭제 완료");
			}
		}
	}
	

	@Override
	public List<ReplyVO> selectByRef(int board_idx) {
		log.info("ReplyServiceImpl-selectByRef 호출 : " + board_idx);
		List<ReplyVO> dbBoardReplyList = null;
		if (board_idx != 0) {
			dbBoardReplyList = replyDAO.selectByRef(board_idx);
		}
		log.info("ReplyServiceImpl-selectByRef 리턴 : " + dbBoardReplyList);
		return dbBoardReplyList;
	}
	
	@Override
	public List<ReplyVO> selectByUserRef(int user_idx) {
		log.info("ReplyServiceImpl-selectByUserRef 호출 : " + user_idx);
		List<ReplyVO> dbUserReplyList = null;
		if (user_idx != 0) {
			dbUserReplyList = replyDAO.selectByUserRef(user_idx);
		}
		log.info("ReplyServiceImpl-selectByUserRef 리턴 : " + dbUserReplyList);
		return dbUserReplyList;
	}

	@Override
	public List<ReplyVO> selectList() {
		log.info("ReplyServiceImpl-selectList 호출");
		List<ReplyVO> dbReplylist = null;
		dbReplylist = replyDAO.selectList();
		if (dbReplylist == null) {
			log.info("ReplyServiceImpl-selectList 빈 VO객체 리턴함.");
			dbReplylist = new ArrayList<ReplyVO>();
		}
		log.info("ReplyServiceImpl-selectList 리턴 : " + dbReplylist);
		return dbReplylist;
	}

	@Override
	public int selectMaxIdx() {
		log.info("ReplyServiceImpl-selectMaxIdx 호출 ");
		int maxIdx = 0;
		maxIdx = replyDAO.selectMaxIdx();
		if (maxIdx == 0) {
			log.info("ReplyServiceImpl-selectMaxIdx idx 값 0.");
			;
		}
		log.info("ReplyServiceImpl-selectMaxIdx 리턴 " + maxIdx);
		return maxIdx;
	}



}
