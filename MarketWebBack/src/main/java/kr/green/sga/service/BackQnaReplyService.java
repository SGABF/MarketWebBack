package kr.green.sga.service;

import kr.green.sga.vo.BackQnaReplyVO;

public interface BackQnaReplyService {
	//댓글 추가
	void insert(BackQnaReplyVO backQnaReplyVO);
	//댓글 수정
	void update(BackQnaReplyVO backQnaReplyVO);
	//댓글 삭제
	void delete(int idx);
	//해당 게시물에 댓글이 몇개( 답변완료 확인용)
	int commentCount(int ref);
	//댓글 가져오기
	BackQnaReplyVO selectComment(int idx);
}
