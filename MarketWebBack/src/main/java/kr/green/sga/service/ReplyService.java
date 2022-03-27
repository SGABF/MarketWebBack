package kr.green.sga.service;

import java.util.List;

import kr.green.sga.vo.BoardVO;
import kr.green.sga.vo.ReplyVO;
import kr.green.sga.vo.UserVO;

public interface ReplyService {

	// <!-- 01. insert_글 쓰기 -->
	void insertReply(ReplyVO replyVO, BoardVO boardVO, String user_id);

	// <!-- 02. select_글 1개 가져오기 -->
	ReplyVO selectByIdx(int reply_idx);

	// <!-- 03. update_글 수정하기 -->
	void updateReply(ReplyVO replyVO);

	// <!-- 04. delete_글 삭제하기 -->
	void deleteByIdx(int reply_idx);

	// <!-- 04. delete_board_idx -->
	void deleteByBoardIdx(BoardVO boardVO, UserVO userVO);

	// --------------------------------------------------

	// <!-- 05. 해당 board_idx의 첨부 이미지 가져오기 -->
	List<ReplyVO> selectByRef(int board_idx);

	// <!-- 06. 전체 첨부파일 가져오기 -->
	List<ReplyVO> selectList();

	// <!-- 52. 가장 마지막 idx값 가져오기 -->
	int selectMaxIdx();

}
