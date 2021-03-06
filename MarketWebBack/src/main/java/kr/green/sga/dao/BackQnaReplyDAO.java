package kr.green.sga.dao;


import org.apache.ibatis.annotations.Mapper;

import kr.green.sga.vo.BackQnaReplyVO;

@Mapper
public interface BackQnaReplyDAO {
	// 저장 (1:1 문의 답변등록)
	void insert(BackQnaReplyVO backQnaReplyVO) ;
	// 수정 (1:1 문의 답변수정) 
	void update(BackQnaReplyVO backQnaReplyVO) ;
	// 삭제 (1:1 문의 답변삭제)
	void delete(int ref) ;
	// 댓글 개수 세기
	int commentCount(int ref);
	// 게시글의 댓글 가져오기
	BackQnaReplyVO selectComment(int idx);
	// 댓글의 글번호 가져오기
	int selectByContent(BackQnaReplyVO backQnaReplyVO);
	// 업뎃
	void selectContentIdx(BackQnaReplyVO backQnaReplyVO);
}
