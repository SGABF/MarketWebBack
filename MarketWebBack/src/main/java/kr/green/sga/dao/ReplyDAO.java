package kr.green.sga.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.green.sga.vo.ReplyVO;

@Mapper
public interface ReplyDAO { 
	
	// <!-- 01. insert_첨부이미지 저장하기 -->
	void insertReply(ReplyVO replyVO);
	
	// <!-- 02. select_첨부 이미지 1개 가져오기 -->
	ReplyVO selectByIdx(int reply_idx);
	
	// <!-- 00. update_글 수정하기 -->
	void updateReply(ReplyVO replyVO);
	
	// <!-- 03. delete_이미지 boardImage_idx 삭제하기 -->
	void deleteByIdx(int reply_idx);
	
	// <!-- 03. delete_board_idx -->
	void deleteByBoardIdx(int board_idx);

	// --------------------------------------------------
	
	// <!-- 04. 해당 board_idx의 첨부 이미지 가져오기 --> 
	List<ReplyVO> selectByRef(int board_idx);
	
	// <!-- 05.	전체 댓글 가져오기 -->
	List<ReplyVO> selectList();
	
	// <!-- 52. 가장 마지막 idx값 가져오기 -->
	int selectMaxIdx();
	
}
